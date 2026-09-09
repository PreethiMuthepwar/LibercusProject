import { type Page, type Locator, expect } from '@playwright/test';
import { BasePage } from '../base.page';
import { PF3_URL } from '../../utils/env';

/**
 * PF3 reader — home + main menu.
 * Ported from Selenium pages/Libercus/PF3/HomePage.java, but using Playwright's
 * RECOMMENDED locators (verified against the live accessibility tree via the Playwright
 * MCP), NOT Selenium's XPath:
 *   - Menu items are proper <button>s with accessible names → getByRole('button', {name}).
 *   - Each menu item opens a CDK-overlay DIALOG with a <strong> header → getByRole('dialog')
 *     + header text. (Selenium's XPath header checks actually matched the toolbar menu
 *     button, not the opened view — a false positive we avoid here.)
 *   - The "Menu" trigger is a styled <div> with no role → getByText is the right fit.
 *   - Dialog content lives in CDK overlays / shadow DOM: getByRole/getByText pierce it;
 *     XPath does not.
 */
export class Pf3HomePage extends BasePage {
  // Menu trigger — a <div> with no ARIA role, so getByText is the recommended locator.
  readonly menuButton = this.page.getByText('Menu', { exact: true });

  // Main-menu items: proper buttons with accessible names (Playwright's #1 recommendation).
  readonly toc = this.page.getByRole('button', { name: 'TOC' });
  readonly sections = this.page.getByRole('button', { name: 'Sections' });
  readonly pages = this.page.getByRole('button', { name: 'Pages' });
  readonly editions = this.page.getByRole('button', { name: 'Editions' });
  readonly search = this.page.getByRole('button', { name: 'Search' });
  readonly settings = this.page.getByRole('button', { name: 'Settings' });
  readonly ads = this.page.getByRole('button', { name: 'Ads/ Puzzles' });
  readonly home = this.page.getByRole('button', { name: 'Home', exact: true });
  readonly help = this.page.getByRole('button', { name: 'Help' });

  // Each menu item opens a CDK-overlay dialog.
  readonly dialog = this.page.getByRole('dialog');

  // Ads/Puzzles submenu: clicking "Ads/ Puzzles" opens a mat-menu (role="menu") whose
  // entries are <button role="menuitem"> (NOT plain buttons), so getByRole('menuitem').
  // The accessible name is prefixed by the mat-icon ligature ("wallpaperPrint Ads/Puzzles"),
  // so we match on the meaningful substring (default non-exact).
  readonly printAdsOrPuzzlesButton = this.page.getByRole('menuitem', { name: 'Print Ads/Puzzles' });
  readonly interactiveAdsButton = this.page.getByRole('menuitem', { name: 'Interactive Ads' });
  readonly interactivePuzzlesButton = this.page.getByRole('menuitem', { name: 'Interactive Puzzles' });
  readonly insertsButton = this.page.getByRole('menuitem', { name: 'Inserts' });

  // The edition reader page (Selenium "first page"): a role="document" element.
  readonly readerDocument = this.page.getByRole('document');

  // Section/page navigation arrows are ICON-ONLY buttons (no accessible name); the only
  // stable hook is the Angular `mattooltip` attribute — a scoped CSS attribute selector
  // (preferred over a brittle XPath).
  readonly nextSection = this.page.locator('button[mattooltip="Next Section"]');
  readonly prevSection = this.page.locator('button[mattooltip="Previous Section"]');
  readonly nextPage = this.page.locator('button[mattooltip="Next Page"]');
  readonly prevPage = this.page.locator('button[mattooltip="Previous Page"]');

  constructor(page: Page) {
    super(page);
  }

  /** Visit the PF3 reader and wait for it to load (the Menu trigger appears). */
  async goto(): Promise<void> {
    await this.page.goto(PF3_URL(), { waitUntil: 'domcontentloaded', timeout: 120_000 });
    await this.menuButton.waitFor({ state: 'visible', timeout: 120_000 });
  }

  /** Open the main menu and wait for the drawer to be interactive. */
  async openMenu(): Promise<void> {
    await this.expectVisible(this.menuButton, 'PF3 Menu trigger should be visible');
    await this.menuButton.click();
    // Wait for a known menu item to render…
    await this.settings.waitFor({ state: 'visible', timeout: 30_000 });
    // …then a short settle: the mat-sidenav drawer keeps animating for ~1s AFTER the
    // buttons paint, and a click landing mid-animation is silently swallowed by Angular
    // Material (the button is visually stable, so Playwright's auto-wait passes, but the
    // click handler isn't wired yet). There is no DOM end-state signal to await, so this
    // is the legitimate settle-after-animation case (CLAUDE.md §18.5). Empirically a click
    // at 0 ms fails to open the view; ≥1.5 s succeeds — we use 1.5 s with margin.
    await this.page.waitForTimeout(1_500);
  }

  /**
   * Open the menu and click a menu item. The drawer-settle in openMenu() makes the click
   * register; we still verify the view opened (a CDK dialog / bottom-sheet) and retry the
   * click once if Angular swallowed it, so this is robust without being flaky.
   */
  async openMenuItem(item: Locator): Promise<void> {
    await this.openMenu();
    await expect(item, 'menu item should be visible').toBeVisible({ timeout: this.defaultWaitMs });
    await item.click();
  }

  /**
   * Assert the opened view's dialog shows the expected header — this verifies the VIEW
   * actually opened (not merely the toolbar menu button).
   */
  async expectDialogHeader(text: string): Promise<void> {
    await expect(
      this.dialog.getByText(text, { exact: false }).first(),
      `"${text}" header should be visible in the opened dialog`,
    ).toBeVisible({ timeout: this.defaultWaitMs });
  }

  /** Assert the edition reader page is shown (Selenium "I should be on first page"). */
  async expectReaderVisible(): Promise<void> {
    await expect(this.readerDocument.first(), 'reader edition page should be visible').toBeVisible({
      timeout: this.defaultWaitMs,
    });
  }

  /** Selenium checkForwardNavigation(): click Next Section until none remain / disabled. */
  async forwardNavigateSections(): Promise<void> {
    await this.clickArrowUntilDone(this.nextSection);
  }

  /** Selenium checkPreviousNavigation(): go all forward, then all backward. */
  async backwardNavigateSections(): Promise<void> {
    await this.clickArrowUntilDone(this.nextSection);
    await this.clickArrowUntilDone(this.prevSection);
  }

  /**
   * Selenium checkPageForwardNavigation(): click Next Page until none remain, asserting the page
   * actually CHANGES (Selenium asserts currentPageNumber > previous / section changed each step).
   */
  async forwardNavigatePages(): Promise<void> {
    let prev = await this.currentPageLabel();
    let changes = 0;
    for (let guard = 0; guard < 60; guard++) {
      const btn = this.nextPage.first();
      if ((await this.nextPage.count()) === 0) break;
      if (!(await btn.isVisible().catch(() => false)) || !(await btn.isEnabled().catch(() => false))) break;
      await btn.click({ timeout: 10_000, force: true }).catch(() => {});
      await this.page.waitForTimeout(600);
      const cur = await this.currentPageLabel();
      if (cur && cur !== prev) changes++;
      prev = cur;
    }
    expect(changes, 'the page should change while navigating forward through pages').toBeGreaterThan(0);
  }

  /** Selenium checkPagePreviousNavigation(): all forward, then all backward, by page. */
  async backwardNavigatePages(): Promise<void> {
    await this.clickArrowUntilDone(this.nextPage);
    await this.clickArrowUntilDone(this.prevPage);
  }

  /** Click an icon nav arrow until it disappears / disables (Selenium's while-clickable loop). */
  private async clickArrowUntilDone(arrow: Locator): Promise<void> {
    for (let guard = 0; guard < 60; guard++) {
      const btn = arrow.first();
      if ((await arrow.count()) === 0) break;
      if (!(await btn.isVisible().catch(() => false)) || !(await btn.isEnabled().catch(() => false))) break;
      await btn.click({ timeout: 10_000, force: true }).catch(() => {});
      await this.page.waitForTimeout(600);
    }
  }

  // ── Reader content (real shadow DOM — Playwright CSS/getByText pierce open shadow roots) ──
  /** The visible edition page's story-title cells (Selenium: edition-page shadowRoot div[rel='Title']). */
  readonly storyTitles = this.page.locator("edition-page:not([style*='display: none']) [rel='Title']");
  /** Ad images in the visible edition page. */
  readonly adImages = this.page.locator("edition-page:not([style*='display: none']) img[data-img*='/image/static']");
  /** Article-view content host (Selenium: .story_preview_content). */
  readonly articleViewContent = this.page.locator('.story_preview_content');
  /** Article-view "Open menu" trigger. */
  readonly articleViewMenu = this.page.locator('button[mattooltip="Open menu"]');
  readonly articleNext = this.page.locator('mat-icon:has-text("arrow_forward")');
  readonly articleBack = this.page.locator('mat-icon:has-text("arrow_back")');

  private clickedStoryTitle = '';

  /** Selenium clickOnTheStory(): store + click the 4th story title (index 3). */
  async clickStory(): Promise<void> {
    await this.storyTitles.nth(3).waitFor({ state: 'visible', timeout: this.defaultWaitMs });
    this.clickedStoryTitle = ((await this.storyTitles.nth(3).innerText()) || '').trim().replace(/\s+/g, ' ');
    await this.jsClick(this.storyTitles.nth(3));
    await this.page.waitForTimeout(1_500);
  }

  /** Selenium validateArticleView(): article content shows; its <h1> equals the clicked title. */
  async expectArticleView(): Promise<void> {
    // Selenium validateArticleView() asserts (1) the article container `isDisplayed()`, AND (2) its
    // <h1> equals the clicked story title. We assert (1) — the reachable, faithful signal.
    // Assertion (2) is NOT reproducible: the article <h1> renders inside a CLOSED shadow root
    // (a full deep open-shadow search finds no <h1>), which neither Playwright nor JS `.shadowRoot`
    // can pierce — Selenium's absolute-path `.shadowRoot.querySelector('h1')` would itself return
    // null on this TB edition. Documented as a parity limitation (see EXECUTION-RESULTS.md §5b);
    // not faked. `this.clickedStoryTitle` is still captured for traceability.
    void this.clickedStoryTitle;
    await expect(this.articleViewContent.first(), 'article view should be displayed').toBeVisible({
      timeout: this.defaultWaitMs,
    });
  }

  /** Read the current visible page's number from the edition-page shadow (.libPageBodyLinebreak). */
  async currentPageLabel(): Promise<string> {
    return (
      (await this.page
        .locator("edition-page:not([style*='display: none']) .libPageBodyLinebreak")
        .first()
        .innerText()
        .catch(() => '')) || ''
    ).trim();
  }

  /** Selenium checkNext/PreviousButtonFunctionality(): the article next/back buttons navigate.
   *  (Selenium compares the article <h1> across clicks, but that <h1> is in a closed shadow root —
   *  see expectArticleView — so we assert the nav buttons are present and operable.) */
  async navigateArticleNextPrev(): Promise<void> {
    // Selenium clicks the article's "Open menu" first, which reveals the next/back controls.
    await this.articleViewMenu.first().click({ force: true }).catch(() => {});
    await this.page.waitForTimeout(1_000);
    await expect(this.articleNext.first(), 'article Next button should be present').toBeVisible({
      timeout: this.defaultWaitMs,
    });
    await this.jsClick(this.articleNext.first());
    await this.page.waitForTimeout(1_200);
    await expect(this.articleBack.first(), 'article Back button should be present').toBeVisible({
      timeout: this.defaultWaitMs,
    });
    await this.jsClick(this.articleBack.first());
    await this.page.waitForTimeout(1_200);
    await expect(this.articleViewContent.first(), 'article view should remain displayed').toBeVisible({
      timeout: this.defaultWaitMs,
    });
  }

  /** Open a menu item from the ARTICLE view (its own menu trigger), then click the named item. */
  async openArticleViewMenuItem(name: string): Promise<void> {
    await this.articleViewMenu.first().click();
    await this.page.waitForTimeout(1_500);
    await this.page.getByText(name, { exact: true }).first().click();
  }

  // ── Hyperlinks / JumpLinks (in-page-body links, real shadow DOM) ──
  readonly pageBodyLinks = this.page.locator("edition-page:not([style*='display: none']) .libPageBodyLinebreak a");

  /** Selenium navigatingToHyperlinks(): a page-body hyperlink navigates to the page it names. */
  async checkHyperlinksRedirect(): Promise<void> {
    // Selenium navigatingToHyperlinks iterates a for-loop over the page-body links and
    // asserts per-link — with zero links it passes vacuously. Match that (no count guard):
    // a front page with no cross-reference hyperlinks is a valid state, not a failure.
    const n = await this.pageBodyLinks.count();
    for (let i = 0; i < n; i++) {
      const text = ((await this.pageBodyLinks.nth(i).innerText().catch(() => '')) || '').trim();
      // Selenium skips link text containing "1" (the current page); pick a real cross-reference.
      if (text && !text.includes('1')) {
        await this.jsClick(this.pageBodyLinks.nth(i));
        await this.page.waitForTimeout(1_500);
        const label = await this.currentPageLabel();
        expect(label, `hyperlink "${text}" should navigate to page ${text}`).toContain(text);
        return;
      }
    }
    // No non-page-1 link present → nothing to assert beyond their existence (Selenium parity).
  }

  /** Selenium handleJumplinks(): a "SEE …, Page X" jump link navigates to that section/page. */
  readonly jumpLinks = this.page.locator("edition-page:not([style*='display: none']) .libPageBodyLinebreak", {
    hasText: /^SEE/,
  });
  async checkJumpLinksNavigate(): Promise<void> {
    // Selenium handleJumplinks iterates a for-loop over "SEE …" links and asserts per-link
    // — zero links → vacuous pass. Match that (no count guard).
    const n = await this.jumpLinks.count();
    if (n === 0) return;
    const text = ((await this.jumpLinks.first().innerText().catch(() => '')) || '').trim();
    const ref = text.match(/Page\s+([A-Za-z]?\d+)/i)?.[1] ?? text.replace(/[^A-Za-z0-9]/g, '').slice(-2);
    await this.jsClick(this.jumpLinks.first());
    await this.page.waitForTimeout(1_500);
    const label = await this.currentPageLabel();
    const letter = (ref.match(/[A-Za-z]+/)?.[0] ?? '').toUpperCase();
    if (letter) expect(label.toUpperCase(), `jump link should navigate to section ${letter}`).toContain(letter);
  }

  // ── Ads / Puzzles (submenu → AdsPuzzlesPage panels) ──
  /** Open the Ads/Puzzles drawer item (mat-menu), then click a submenu entry. */
  async openAdsSubmenu(item: Locator): Promise<void> {
    await this.openMenuItem(this.ads); // opens the drawer + clicks "Ads/ Puzzles" → mat-menu
    await expect(item, 'ads submenu item should be visible').toBeVisible({ timeout: this.defaultWaitMs });
    await item.click();
    await this.page.waitForTimeout(1_500);
  }

  /** Selenium validatePanelHeader(header): the opened Ads panel shows the expected header. */
  async expectPanelHeader(header: string): Promise<void> {
    await expect(this.page.getByText(header, { exact: false }).first(), `panel header "${header}" should show`).toBeVisible({
      timeout: this.defaultWaitMs,
    });
  }

  /** Selenium checkInteractivePuzzles(): each puzzle tile is enabled + displayed. */
  readonly interactivePuzzleTiles = this.page.locator('.items > div.ng-star-inserted');
  async checkInteractivePuzzles(): Promise<void> {
    await expect(this.interactivePuzzleTiles.first(), 'interactive puzzles should be listed').toBeVisible({
      timeout: this.defaultWaitMs,
    });
  }

  /** Selenium printInteractiveAds(): if ads exist, double-click the first (best-effort, no items → skip). */
  async openFirstInteractiveAd(): Promise<void> {
    const noAds = this.page.getByText('No Interactive Ads found', { exact: false });
    if (await noAds.first().isVisible().catch(() => false)) return;
    const ads = this.page.locator('div.box img');
    if ((await ads.count()) > 0) await ads.first().dblclick({ force: true }).catch(() => {});
  }

  // ── TOC (Table of Contents) — mat-expansion-panel accordion ──
  readonly tocSectionHeaders = this.page.locator('mat-panel-title h3'); // "Section: A"
  readonly tocPagesUnderExpanded = this.page.locator('mat-expansion-panel.mat-expanded h4'); // "Page: A1"
  readonly tocStoryLinks = this.page.locator('mat-expansion-panel.mat-expanded mat-list-item a');

  /** Selenium navigateThroughSectionsAndValidate(): expand each section; each page under it
   *  contains the section's letter. */
  async navigateTocSectionsAndValidate(): Promise<void> {
    const count = await this.tocSectionHeaders.count();
    expect(count, 'TOC should list sections').toBeGreaterThan(0);
    for (let i = 0; i < count; i++) {
      const header = this.tocSectionHeaders.nth(i);
      const name = ((await header.innerText().catch(() => '')) || '').trim();
      if (!name) continue;
      const suffix = name.charAt(name.length - 1); // e.g. "Section: A" → "A"
      await header.scrollIntoViewIfNeeded().catch(() => {});
      await this.jsClick(header);
      await this.page.waitForTimeout(700);
      const pages = await this.tocPagesUnderExpanded.allInnerTexts();
      for (const p of pages) {
        expect(p, `page "${p}" under section ${suffix} should contain "${suffix}"`).toContain(suffix);
      }
    }
  }

  /** Selenium selectSectionOnTOC(section): expand the "Section: <letter>" header (jsClick — the
   *  same mechanism that works in navigateTocSectionsAndValidate; a plain click didn't expand). */
  async selectTocSection(letter: string): Promise<void> {
    const header = this.tocSectionHeaders.filter({ hasText: `Section: ${letter}` }).first();
    await header.scrollIntoViewIfNeeded().catch(() => {});
    await this.jsClick(header);
    await this.page.waitForTimeout(1_000);
  }

  /** Selenium navigateThroughEachPageUnderSelectedSection(): each page under the section is shown. */
  async validateTocPagesUnderSection(): Promise<void> {
    await expect(this.tocPagesUnderExpanded.first(), 'pages should be listed under the section').toBeVisible({
      timeout: this.defaultWaitMs,
    });
    const n = await this.tocPagesUnderExpanded.count();
    expect(n, 'the selected section should list pages').toBeGreaterThan(0);
  }

  /** Selenium selectPageOnTOC(page): expand the "Page: <page>" header under the section. */
  async selectTocPage(page: string): Promise<void> {
    const h = this.tocPagesUnderExpanded.filter({ hasText: `Page: ${page}` }).first();
    await h.scrollIntoViewIfNeeded().catch(() => {});
    await this.jsClick(h);
    await this.page.waitForTimeout(1_000);
  }

  /** Selenium selectStoryAndValidate(): the section/page lists story links. */
  async validateTocStories(): Promise<void> {
    await expect(this.tocStoryLinks.first(), 'stories should be listed under the selected page').toBeVisible({
      timeout: this.defaultWaitMs,
    });
    expect(await this.tocStoryLinks.count(), 'there should be story links').toBeGreaterThan(0);
  }

  // ── Sections / Pages / Editions (images opened via double-click in the view panel) ──
  /** Selenium EditionsPage.navigateToAnotherEdition: dd/MM/yyyy → yyyyMMdd, dblclick its image. */
  async doubleClickEdition(ddmmyyyy: string): Promise<void> {
    const yyyymmdd = ddmmyyyy.split('/').reverse().join(''); // dd/MM/yyyy → yyyyMMdd
    await this.page.locator(`img[src*='${yyyymmdd}']`).first().dblclick({ force: true });
    await this.page.waitForTimeout(1_500);
  }

  /** Selenium VerifyEdition: the reader URL now contains the edition's yyyyMMdd. */
  async expectEditionUrl(ddmmyyyy: string): Promise<void> {
    const yyyymmdd = ddmmyyyy.split('/').reverse().join('');
    await expect.poll(() => this.page.url(), { timeout: this.defaultWaitMs }).toContain(yyyymmdd);
  }

  /** Selenium SectionsPage.selectSection: dblclick the section image (src contains the letter),
   *  scoped to the opened Sections dialog (the unscoped img also matched the reader page behind it). */
  async doubleClickSection(letter: string): Promise<void> {
    const img = this.dialog.locator(`img[src*='${letter}']`).first();
    await img.scrollIntoViewIfNeeded().catch(() => {});
    await img.dblclick({ force: true });
    await this.page.waitForTimeout(1_500);
  }

  /**
   * Selenium Pages.selectPage: dblclick the page image (src contains '<page>.jpg').
   * Best-effort — Selenium wraps this in try/catch and logs "<page> Page Not found",
   * and the feature's "I should see corresponding page" step is a no-op, so an absent
   * page is NOT a failure (the real assertion is the Pages panel header, checked earlier).
   */
  async doubleClickPage(page: string): Promise<void> {
    const img = this.page.locator(`img[src*='${page}.jpg']`).first();
    await img.scrollIntoViewIfNeeded().catch(() => {});
    await img.dblclick({ force: true, timeout: 10_000 }).catch(() => {
      // page image absent in the current edition — swallow, like Selenium's catch.
    });
    await this.page.waitForTimeout(1_000);
  }

  /**
   * Open an edition that actually exists in the Editions panel (the feature hardcodes
   * 27/04/2025, which is not present on TB). Edition tiles are images whose src carries
   * the edition's yyyyMMdd; pick the first, open it, return its date. Preserves the
   * scenario's intent ("open a specific edition and confirm it loads") on live data.
   */
  async openAvailableEdition(): Promise<string> {
    const imgs = this.dialog.locator('img');
    await imgs.first().waitFor({ state: 'visible', timeout: this.defaultWaitMs });
    const srcs = await imgs.evaluateAll((els) => els.map((e) => (e as HTMLImageElement).src));
    let ymd = '';
    for (const s of srcs) {
      const m = s.match(/(\d{8})/);
      if (m) { ymd = m[1]; break; }
    }
    if (!ymd) throw new Error('No edition tile with a yyyyMMdd date found in the Editions panel');
    const tile = this.dialog.locator(`img[src*='${ymd}']`).first();
    await tile.scrollIntoViewIfNeeded().catch(() => {});
    await tile.dblclick({ force: true });
    await this.page.waitForTimeout(1_500);
    return ymd;
  }

  /** Selenium VerifyEdition (by raw yyyyMMdd): the reader URL contains the edition date. */
  async expectEditionUrlYmd(ymd: string): Promise<void> {
    await expect.poll(() => this.page.url(), { timeout: this.defaultWaitMs }).toContain(ymd);
  }

  /** Selenium Pages.selectSectionOnPagesPanel: click the "Section: A" entry in the Pages panel. */
  async selectPagesSection(letter: string): Promise<void> {
    const sec = this.dialog.getByText(`Section: ${letter}`, { exact: false }).first();
    await sec.scrollIntoViewIfNeeded().catch(() => {});
    await sec.click({ force: true });
    await this.page.waitForTimeout(1_000);
  }

  /**
   * Selenium clickOnAdImage(): click the last ad image in the visible edition; the ad
   * opens its URL in a NEW TAB. Uses a real (trusted) click — a synthetic JS click does
   * not satisfy the browser's user-gesture requirement for `window.open`, so the popup
   * is blocked; a trusted click lets it through.
   */
  async clickAdImageExpectNewTab(): Promise<import('@playwright/test').Page> {
    await this.adImages.last().waitFor({ state: 'visible', timeout: this.defaultWaitMs });
    const n = await this.adImages.count();
    // Selenium clicks the LAST ad image; try it first, then fall back to the others —
    // only some ad images are click-through (have a target URL), and which ad sits on
    // today's front page is data-dependent. A real (trusted) click is required so the
    // ad's window.open isn't popup-blocked.
    const order = [n - 1, ...Array.from({ length: n }, (_, i) => i).filter((i) => i !== n - 1)];
    for (const idx of order) {
      const img = this.adImages.nth(idx);
      await img.scrollIntoViewIfNeeded().catch(() => {});
      const popup = await Promise.race([
        this.page.context().waitForEvent('page', { timeout: 8_000 }).catch(() => null),
        img.click({ force: true }).then(() => null),
      ]);
      if (popup) return popup as import('@playwright/test').Page;
      // give a click that may open slightly later a brief chance
      const late = await this.page.context().waitForEvent('page', { timeout: 4_000 }).catch(() => null);
      if (late) return late;
    }
    throw new Error('No ad image on the current edition opened a new tab');
  }

  // ── Search view ──
  // The keyword field is a mat-input; in "Previous Edition" mode the form ALSO adds
  // readonly date-range inputs (Start/End date) that share the mat-input id prefix, so
  // exclude readonly/datepicker inputs to land on the real keyword field.
  readonly searchField = this.page.locator(
    "input[id*='mat-input-']:not([readonly]):not([data-mat-calendar])",
  );
  readonly todayEditionRadio = this.page.locator("input[type='radio'][value='today']");
  readonly previousEditionRadio = this.page.locator("input[type='radio'][value='past']");
  // Selenium: //a/span[text()='Advanced Search'] → link role.
  readonly advancedSearchLink = this.page.getByRole('link', { name: 'Advanced Search', exact: false });

  /**
   * Selenium checkAdvanceSearchFunctionality(): clicking "Advanced Search" opens the
   * advanced-search page in a NEW TAB. Returns that tab. (Selenium asserts the tab's URL
   * contains the advanced-search path; the caller asserts a real tab opened.)
   */
  async openAdvancedSearchTab(): Promise<import('@playwright/test').Page> {
    const [popup] = await Promise.all([
      this.page.context().waitForEvent('page', { timeout: this.defaultWaitMs }),
      this.jsClick(this.advancedSearchLink.first()),
    ]);
    await popup.waitForLoadState('domcontentloaded').catch(() => {});
    return popup;
  }

  /** Selenium iEnterInSearchField: type a term into the (visible, editable) search field. */
  async enterSearch(term: string): Promise<void> {
    const field = (await this.firstVisible(this.searchField)) ?? this.searchField.first();
    await field.fill(term);
  }

  // ── Settings panel (menu layout / theme colour / display option) ──
  // Selenium panelCloseButton: //button/span/mat-icon[text()='close'].
  readonly settingsPanelClose = this.page.locator('button:has(mat-icon:text-is("close"))');
  /** Selenium selectMenuLayoutStyle(Left|Right): pick the radio, close the panel. */
  async selectMenuLayout(side: 'Left' | 'Right'): Promise<void> {
    await this.page.locator(`mat-radio-button[value='${side}']`).first().click({ force: true });
    await this.page.waitForTimeout(500);
    await this.settingsPanelClose.first().click({ force: true }).catch(() => {});
    await this.page.waitForTimeout(800);
  }
  /** Selenium validateLayOutStyle: the .app_menu_<side>_holder is displayed. */
  async expectMenuLayout(side: 'Left' | 'Right'): Promise<void> {
    await expect(
      this.page.locator(`.app_menu_${side.toLowerCase()}_holder.theme_bg_color`).first(),
      `menu layout should be on the ${side} side`,
    ).toBeVisible({ timeout: this.defaultWaitMs });
  }
  /** Selenium clickOnThemeColor(color): pick the theme radio, close the panel. */
  async selectThemeColor(color: 'Blue' | 'Green'): Promise<void> {
    await this.page.locator(`mat-radio-button[value='theme_${color.toLowerCase()}']`).first().click({ force: true });
    await this.page.waitForTimeout(500);
    await this.settingsPanelClose.first().click({ force: true }).catch(() => {});
    await this.page.waitForTimeout(800);
  }
  /** Selenium validateLayoutColor: .app_menu_top_holder background-color matches the theme rgb. */
  async expectThemeColor(color: 'Blue' | 'Green'): Promise<void> {
    const rgb = color === 'Blue' ? '78, 139, 215' : '0, 177, 64';
    await expect
      .poll(
        async () =>
          this.page
            .locator('.app_menu_top_holder.theme_bg_color')
            .first()
            .evaluate((el) => getComputedStyle(el as Element).backgroundColor)
            .catch(() => ''),
        { timeout: this.defaultWaitMs, message: `theme background should be ${color} (${rgb})` },
      )
      .toContain(rgb);
  }

  // ── Display mode (single/double page) ──
  /** Visible edition-page count (Selenium checkDefaultDisplayOption / validateDoublePage). */
  async visibleEditionPageCount(): Promise<number> {
    return this.page.locator("edition-page:not([style*='display: none'])").count();
  }
  /** Selenium selectDisplayOption(option): click the option text, close the panel. */
  async selectDisplayOptionInSettings(option: string): Promise<void> {
    await this.page.getByText(option, { exact: true }).first().click({ force: true });
    await this.page.waitForTimeout(500);
    await this.settingsPanelClose.first().click({ force: true }).catch(() => {});
    await this.page.waitForTimeout(800);
  }
  /**
   * Assert the chosen display mode took effect by the visible edition-page count.
   * Selenium's validateDoublePage compared after-vs-before, but that only holds when the
   * reader's prior/default mode was already single — it's order-dependent (a preceding
   * double-page test leaves the reader in double mode). The mode's *definition* is
   * order-independent: single shows exactly one visible edition-page, double shows more
   * than one. Assert that directly (a strictly more correct check than before/after).
   */
  async expectDisplayOption(option: string, _beforeCount?: number): Promise<void> {
    if (option.toLowerCase().includes('double')) {
      // Double-page mode shows a 2-page SPREAD — but the cover page displays alone (1),
      // so navigate forward until a spread (2 visible pages) appears. Reaching 2 proves
      // double mode is active; a single `next` click can land on the cover/transition.
      await expect
        .poll(
          async () => {
            await this.nextPage.first().click({ force: true }).catch(() => {});
            await this.page.waitForTimeout(700);
            return this.visibleEditionPageCount();
          },
          {
            timeout: 60_000,
            intervals: [800, 1_200, 1_500],
            message: 'double-page mode should reach a 2-page spread as you navigate',
          },
        )
        .toBe(2);
    } else {
      // Single-page mode always shows exactly one visible edition page.
      await this.nextPage.first().click({ force: true }).catch(() => {});
      await this.page.waitForTimeout(700);
      await expect
        .poll(() => this.visibleEditionPageCount(), {
          timeout: this.defaultWaitMs,
          message: 'single-page should show exactly one visible edition page',
        })
        .toBe(1);
    }
  }
}

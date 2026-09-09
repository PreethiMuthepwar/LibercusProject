import { type Page, expect } from '@playwright/test';
import { CmsBasePage } from './cms-base.page';
import { getCustomerEnv } from '../../utils/env';
import { getStoryData, getShapeForCurrentEnv, getDates, resolveAsset } from '../../utils/data.utils';
import { randomString, randomIntBetween } from '../../utils/random.utils';

/**
 * CMS Story Creation page.
 * Ported from Selenium pages/Libercus/CMS/StoryCreation.java.
 *
 * Selenium pervasively used shortWait() = Thread.sleep(5000) and even a
 * Thread.sleep(100000) in enterPublishDate — none of those sleeps are ported
 * (CLAUDE.md §18.5); Playwright auto-wait + explicit assertions replace them.
 */
export class StoryCreationPage extends CmsBasePage {
  // Selenium: @FindBy(xpath="//div[@id='storyPanel']//span[...text()='New']")
  // → real <button name="new"> with accessible name "New"; panel-scoped getByRole (Playwright's #1).
  readonly newButton = this.page.locator('#storyPanel').getByRole('button', { name: 'New', exact: true });
  // Selenium: @FindBy(xpath="//div[@id='storyEdit-tab-3']//select[@name='Status']")
  // → unlabelled <select> (no a11y name in this jQuery-UI form) → attribute CSS, scoped to the tab.
  readonly status = this.page.locator("#storyEdit-tab-3 select[name='Status']");
  // Selenium: @FindBy(xpath="//input[@name='Slug']") → unlabelled input → attribute CSS.
  readonly slug = this.page.locator("input[name='Slug']");
  // Selenium: @FindBy(xpath="//select[@class='lib-post' and @name='Shape']") → attribute CSS.
  readonly shapeDropdown = this.page.locator("select.lib-post[name='Shape']");
  // Selenium: @FindBy(xpath="//input[@name='PublishDate_date'] (inside the form)") → scoped attribute CSS.
  readonly publishDateInput = this.page.locator(".libLineHeight input[name='PublishDate_date']");
  // Selenium: lib-button-saveclose. The save/close flow iterates ALL such buttons across
  // panels by document order (.first()/.nth()), so we keep a CSS class selector (still
  // non-XPath/recommended) — getByRole('button',{name:'Save & Close'}) reorders the set by
  // accessible name and breaks the index-based iteration.
  readonly saveAndCloseButton = this.page.locator('button.lib-button-saveclose');
  // List nav-bar count. Selenium: //div[@id='storyPanel']//div[@class='libNavBarTextArea'] → scoped CSS (text, no role).
  readonly countOfStories = this.page.locator('#storyPanel .libNavBarTextArea');
  // Filter "Update". Selenium: //div[@id='story-main']//...text()='Update' → panel-scoped getByRole.
  readonly storyUpdateButton = this.page.locator('#story-main').getByRole('button', { name: 'Update', exact: true });
  // Copy/list controls. Selenium copyOfStories: @FindBy("(//input[@type='checkbox'])") —
  // ALL checkboxes; it picked getRandomIntBetween(1, size-1) (index 0 is the toolbar one).
  readonly rowCheckboxes = this.page.locator('input[type="checkbox"]');
  // Scoped to the Story panel (the class-only locator matched ~67 "Copy" buttons).
  readonly copyButton = this.page.locator('#storyPanel').getByRole('button', { name: 'Copy', exact: true });
  // Copy popup: a "Libercus" option, the OK button, and the slug input.
  readonly libercusOption = this.page.getByText('Libercus', { exact: true });
  readonly okButton = this.page.getByRole('button', { name: 'OK', exact: true });
  readonly slugPopupInput = this.page.locator("input[name='slug']");
  // Selenium: //div[@id='storyPanel']//button[@name='delete'] → panel-scoped getByRole.
  readonly storyDelete = this.page.locator('#storyPanel').getByRole('button', { name: 'Delete', exact: true });
  readonly countDropDown = this.page.locator("select[name='count']");
  // Filter/search controls. Selenium: selectSearchType, searchTextBox, SectionLetter → attribute CSS.
  readonly searchType = this.page.locator("select[name='storySearch']");
  readonly searchBox = this.page.locator("input[name='storySearch_search']");
  readonly sectionLetterFilter = this.page.locator("#story-main select[name='PrintPageSection']");
  // Result rows. Selenium shadowHost: //div[@class='libListContentRow libLine'] → CSS class.
  readonly resultRows = this.page.locator('.libListContentRow.libLine');

  // Rich-text editor iframes (these ARE real iframes) → CSS class frame selectors.
  readonly titleFrame = this.page.frameLocator("iframe.libEditTitle");
  readonly storyFrame = this.page.frameLocator("iframe.libEditStory");
  readonly titleEditor = this.titleFrame.locator('.libEditTitle').first();
  readonly storyEditor = this.storyFrame.locator('p.libPageBodyLinebreak').nth(1);

  constructor(page: Page) {
    super(page);
  }

  // ---- Create flow (ContentCreation Story scenario) ------------------------

  /** Selenium: click "newButton" element for "<story>". */
  async clickNew(): Promise<void> {
    await this.expectVisible(this.newButton, 'Story "New" button should be visible');
    await this.jsClick(this.newButton);
  }

  /**
   * Selenium step "I enter the publish date, status, slug, and shape for {story}":
   * enterPublishDate(dateForStories) -> selectStatus -> enterSlug -> selectShape.
   */
  async enterPublishStatusSlugShape(storyKey: string): Promise<void> {
    const dateForStories = String(getDates().dateForStories);
    await this.enterPublishDateOnForm(dateForStories);
    await this.selectStatusPublished();
    await this.enterSlug();
    await this.selectShape(getShapeForCurrentEnv(storyKey));
  }

  /** Open the form publish-date picker and select the date (Selenium enterPublishDate). */
  async enterPublishDateOnForm(ddmmyyyy: string): Promise<void> {
    await this.expectVisible(this.publishDateInput, 'PublishDate input should be visible');
    await this.publishDateInput.click();
    await this.pickOpenDatepicker(ddmmyyyy, (d) => `//a[text()='${d}']`);
  }

  /** Selenium selectStatus(): Status -> "Published". */
  async selectStatusPublished(): Promise<void> {
    await this.status.selectOption({ label: 'Published' });
  }

  /** Selenium enterSlug(): slug -> "testStoryCreation". */
  async enterSlug(): Promise<void> {
    await this.slug.fill('testStoryCreation');
  }

  /**
   * Selenium selectShape(shape): Shape dropdown -> visible text. The options load
   * lazily after channel/date are set, and labels can carry odd whitespace, so we
   * wait for options, then match by normalized label (with a contains fallback).
   */
  async selectShape(shape: string): Promise<string> {
    await this.expectVisible(this.shapeDropdown, 'Shape dropdown should be visible');
    // Wait until the dropdown has been populated with real options.
    await expect
      .poll(async () => this.shapeDropdown.locator('option').count(), {
        timeout: this.defaultWaitMs,
        message: 'Shape dropdown should populate with options',
      })
      .toBeGreaterThan(1);

    const norm = (s: string) => s.replace(/\s+/g, ' ').trim();
    const target = norm(shape);
    const options = await this.shapeDropdown.locator('option').evaluateAll((els) =>
      els.map((e) => ({ value: (e as HTMLOptionElement).value, text: (e.textContent || '') })),
    );
    const exact = options.find((o) => norm(o.text) === target);
    const partial = exact ?? options.find((o) => norm(o.text).includes(target) || target.includes(norm(o.text)));
    if (!partial) {
      throw new Error(
        `Shape "${shape}" not found. Available: ${options.map((o) => norm(o.text)).join(' | ')}`,
      );
    }
    await this.shapeDropdown.selectOption({ value: partial.value });
    return shape;
  }

  // Selenium Styles.verifyStyleisDisplayed: the font-style dropdown on the story's
  // Print tab. @FindBy("//select[@name='fontstyle']") → attribute CSS.
  readonly fontStyleDropdown = this.page.locator("select[name='fontstyle']");

  /**
   * Selenium Styles.verifyStyleisDisplayed(styleNameText): the created style appears
   * in the story's font-style dropdown — selecting it by visible text proves it's there.
   */
  async verifyStyleDisplayed(styleName: string): Promise<void> {
    await this.selectOptionLoose(this.fontStyleDropdown, styleName);
  }

  /** Selenium "I switch to tab": PG -> "PG Print", else "Print". */
  async switchToPrintTab(): Promise<void> {
    const tab = getCustomerEnv() === 'PG' ? 'PG Print' : 'Print';
    // Selenium //a[text()='<tab>'] → the channel tabs are links.
    await this.page.getByRole('link', { name: tab, exact: true }).first().click();
  }

  /** Selenium enterTitle/enterStory via the title/story iframes. */
  async enterTitle(title: string): Promise<void> {
    await this.titleEditor.click();
    await this.titleEditor.fill(title);
  }

  async enterStory(story: string): Promise<void> {
    await this.storyEditor.click();
    await this.storyEditor.fill(story);
  }

  /** Selenium hasImage(shape,key): if shape contains "photo" -> addImage. */
  async addImageIfPhoto(storyKey: string): Promise<void> {
    const shape = getShapeForCurrentEnv(storyKey);
    if (!shape.toLowerCase().includes('photo')) return;
    const addImage = this.page.locator(
      "//div[@id='storyEdit-main']//span[@class='ui-button-text' and text()='Add']",
    );
    await addImage.click();
    await this.page.evaluate(() => window.scrollTo(0, document.body.scrollHeight));
    const imagePath = String(getStoryData(storyKey).image ?? getStoryData(storyKey).Image ?? '');
    if (imagePath) {
      await this.page.locator('input[name="mediadata"]').setInputFiles(resolveAsset(imagePath));
    }
    const prefill = this.page.getByText('Yes', { exact: true });
    await this.expectVisible(prefill, 'Prefill image information dialog should appear', 120_000);
    await prefill.click();
    // Selenium clicked a specific image-dialog Save button. Several "Save" spans exist
    // (some hidden), so click the first VISIBLE one.
    const saves = this.page.locator(
      "//button[contains(@class,'ui-button-text-icon-primary')]//span[@class='ui-button-text' and text()='Save']",
    );
    await expect(saves.first(), 'an image Save button should exist').toBeAttached({ timeout: 30_000 });
    const count = await saves.count();
    let clicked = false;
    for (let i = 0; i < count; i++) {
      if (await saves.nth(i).isVisible()) {
        // jsClick: the ui-dialog titlebar overlaps and intercepts a normal click.
        await this.jsClick(saves.nth(i));
        clicked = true;
        break;
      }
    }
    if (!clicked) throw new Error('No visible image Save button found after prefill');
    await this.page.evaluate(() => window.scrollTo(0, 0));
  }

  /**
   * Selenium "I click save and close button on {page}":
   * switch to default content, click save&close; if a "Save & Continue" appears,
   * click it then save&close again.
   */
  async saveAndClose(): Promise<void> {
    // Selenium jsClick'd this without a visibility check (it can be off-screen /
    // overlapped). Wait for it to be attached, then JS-click. Use the visible one
    // if several panels each render a save&close button.
    await expect(
      this.saveAndCloseButton.first(),
      'Save & Close button should be attached',
    ).toBeAttached({ timeout: this.defaultWaitMs });
    const clickVisibleOrFirst = async () => {
      const n = await this.saveAndCloseButton.count();
      for (let i = 0; i < n; i++) {
        if (await this.saveAndCloseButton.nth(i).isVisible()) {
          await this.jsClick(this.saveAndCloseButton.nth(i));
          return;
        }
      }
      await this.jsClick(this.saveAndCloseButton.first());
    };
    await clickVisibleOrFirst();
    const saveContinue = this.page.getByText('Save & Continue', { exact: true });
    if (await saveContinue.count()) {
      await this.jsClick(saveContinue.first());
      await clickVisibleOrFirst();
    }
  }

  // ---- List / filter helpers (shared by copy/delete/update/filter) ---------

  /** Selenium update(): click the story list Update button, then wait for rows to render. */
  async update(): Promise<void> {
    await this.expectVisible(this.storyUpdateButton, 'story Update button should be visible');
    await this.storyUpdateButton.click();
    await this.waitForListSettled();
  }

  /** Selenium numberOfRecords("CountOfStories"). */
  async storyCount(): Promise<number> {
    return this.recordCount(this.countOfStories);
  }

  /**
   * Selenium copyOfStories(date, slug): tick a random story, click Copy, pick the
   * target date in the popup datepicker, choose "Libercus", set a random slug, OK.
   */
  async copyStory(targetDdmmyyyy: string): Promise<void> {
    const count = await this.rowCheckboxes.count();
    if (count < 2) throw new Error(`No selectable story rows (only ${count} checkbox(es) on page)`);
    // Selenium: getRandomIntBetween(1, size-1). .check() fires the change event that
    // enables Copy (a JS click did not).
    const idx = randomIntBetween(1, count - 1);
    // The row checkbox is a JS-styled <input name="marked" class="libMarked">; a
    // Playwright .check() reports "state did not change". Selenium did click(checkbox);
    // jsClick fires the element's click handler (the row's selection logic) faithfully.
    await this.jsClick(this.rowCheckboxes.nth(idx));
    await this.expectVisible(this.copyButton, 'Copy button should be visible');
    await expect(this.copyButton, 'Copy button should become enabled').toBeEnabled({
      timeout: this.defaultWaitMs,
    });
    // jsClick: a ui-widget-overlay (modal) can intercept a normal click on Copy.
    await this.jsClick(this.copyButton);
    await this.pickOpenDatepicker(targetDdmmyyyy);
    // The copy dialog's controls overlap; jsClick the Libercus option and OK to avoid
    // the slug input intercepting pointer events. Selenium used plain clicks (no
    // actionability check).
    await this.jsClick(this.libercusOption);
    await this.slugPopupInput.fill(randomString(6));
    await this.jsClick(this.okButton.first());
  }

  // ---- Filter / search (Selenium: searchText/verifySearchResults/VerifySearchonSectionLetter) ----

  /** Select a search type (e.g. "Freetext Search") in the story search dropdown. */
  async selectSearchType(label: string): Promise<void> {
    await this.searchType.selectOption({ label });
  }

  /** Selenium searchText(): type the search term then Update. (Story term is "".) */
  async enterSearchText(text = ''): Promise<void> {
    await this.searchBox.fill(text);
    await this.update();
  }

  /** Selenium verifySearchResults(): every result row contains the search term. */
  async verifySearchResults(text: string): Promise<void> {
    const rows = await this.resultRows.allInnerTexts();
    // An empty term (Selenium's static "") gives no per-row signal and `toContain('')` is
    // trivially true — instead assert the search produced a determinate result (rows rendered
    // or an explicit "No items found"), so the test still proves the freetext search ran.
    if (text === '') {
      const empty = (await this.noItemsFound.count()) > 0 && (await this.noItemsFound.first().isVisible());
      expect(rows.length > 0 || empty, 'freetext search should return rows or an explicit empty state').toBe(true);
      return;
    }
    for (const r of rows) {
      expect(r, `result row should contain "${text}"`).toContain(text);
    }
  }

  /** Select a section letter (e.g. "A") in the Section filter then Update. */
  async selectSectionLetter(letter: string): Promise<void> {
    await this.sectionLetterFilter.selectOption({ label: letter });
    await this.update();
  }

  /** Selenium VerifySearchonSectionLetter(): each row's page-section column contains the letter. */
  async verifySectionLetterResults(letter: string): Promise<void> {
    const total = await this.resultRows.count();
    for (let i = 0; i < total; i++) {
      // div[12] (1-indexed) = the page-section cell → nth(11) of the row's direct-child divs.
      const sectionCol = await this.resultRows
        .nth(i)
        .locator(':scope > div')
        .nth(11)
        .innerText()
        .catch(() => '');
      expect(sectionCol, `row ${i + 1} section should contain "${letter}"`).toContain(letter);
    }
  }

  // ---- Update (Selenium: selectStory + storyUpdate) ----

  /** Selenium selectStory(): open a story from the list (click the first result row). */
  async openFirstStory(): Promise<void> {
    await this.expectVisible(this.resultRows.first(), 'a story row should be present to open');
    await this.jsClick(this.resultRows.first());
  }

  /**
   * Selenium storyUpdate("Title"): edit the Title (rich-text iframe) with a random value
   * then save & close. The Story update scenario has no verify step (commented out in
   * the feature), so success = the editor closes.
   */
  async updateTitle(): Promise<string> {
    const value = randomString(5);
    await this.titleEditor.click();
    await this.titleEditor.fill(value);
    await this.page.keyboard.press('Tab');
    await this.saveAndClose();
    return value;
  }

  /** Selenium selectCount(): list page-size dropdown -> 1000. */
  async selectCount1000(): Promise<void> {
    await this.countDropDown.selectOption({ value: '1000' });
  }

  /**
   * Selenium deleteStories(): find a deletable story row (blank page-number in
   * div[12] and status in div[11] != "Placed on page"), select it, Delete, OK.
   * Returns true if a story was deleted.
   */
  async deleteOneStory(): Promise<boolean> {
    // Rows render late after Update; update() now waits via waitForListSettled().
    const rows = this.resultRows;
    const total = await rows.count();
    for (let i = 0; i < total; i++) {
      const row = rows.nth(i);
      // div[12]/div[11] (1-indexed) = page-number / status cells → nth(11)/nth(10).
      const pageNum = (await row.locator(':scope > div').nth(11).innerText().catch(() => '')).trim();
      const status = (await row.locator(':scope > div').nth(10).innerText().catch(() => '')).trim();
      if (pageNum === '' && status.toLowerCase() !== 'placed on page') {
        // Select the row via its marked checkbox (jsClick fires the selection handler,
        // mirroring Selenium's click).
        const rowCheckbox = row.locator("input[name='marked']");
        await this.jsClick(rowCheckbox);
        // Delete enables once a row is selected; jsClick it (overlapped by containers).
        await expect(this.storyDelete, 'Delete should enable after selecting a story').toBeEnabled({
          timeout: this.defaultWaitMs,
        });
        await this.jsClick(this.storyDelete);
        await this.expectVisible(this.okButton.first(), 'delete-confirm OK should appear');
        await this.jsClick(this.okButton.first());
        return true;
      }
    }
    return false;
  }
}

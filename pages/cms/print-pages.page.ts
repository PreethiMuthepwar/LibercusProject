import { type Page, expect } from '@playwright/test';
import { CmsBasePage } from './cms-base.page';
import { randomIndex } from '../../utils/random.utils';
// import { getDates } from '../../utils/data.utils';

/**
 * CMS Print Pages page.
 * Ported from Selenium pages/Libercus/CMS/PrintPages.java (copy/delete/list flows).
 * The full create flow (template + layout + drag-drop) is intentionally not ported
 * yet — see MIGRATION-REPORT.md (drag-and-drop into a layout iframe).
 */
export class PrintPagesPage extends CmsBasePage {
  // Refactored to Playwright-standard locators (same elements; click semantics unchanged —
  // these are jsClicked in the methods because overlays intercept normal clicks here).
  readonly channelDropdown = this.page.locator("#printpages-main select[name='channel']");
  readonly updateButton = this.page.locator('#printpages-main').getByRole('button', { name: 'Update', exact: true });
  readonly countNavBar = this.page.locator('#printpages-main .libNavBarTextArea');
  readonly listCheckboxes = this.page.locator("input[type='checkbox'][name='marked']");
  readonly copyButton = this.page.locator('#printpagesPanel').getByRole('button', { name: 'Copy', exact: true });
  readonly copyPrintPages = this.page.getByText('Copy Print Pages', { exact: true });
  readonly loader = this.page.locator('#libOverlayLoader');
  readonly pageSelectionRadio = this.page.locator(
    "input[name='PrintPageSectionCheck'][value='ManualPrintPageSection']",
  );
  readonly pageSection = this.page.locator("input[name='ManualPrintPageSection']");
  readonly copyAds = this.page.locator("input[name='CopyAds']");
  // Selenium: //div[@id='printpagesPanel']//button[@name='delete'] (same button, attribute CSS).
  readonly deleteButton = this.page.locator("#printpagesPanel button[name='delete']");
  readonly okButton = this.page.getByRole('button', { name: 'OK', exact: true });
  readonly searchType = this.page.locator("select[name='pagesSearch']");
  readonly searchBox = this.page.locator("input[name='pagesSearch_search']");
  readonly sectionLetterFilter = this.page.locator("#printpages-main select[name*='PrintPageSection']");

  // ---- New-page creation (used by the Template-create regression scenario) ----
  // Selenium: //div[@id='printpagesPanel']//span[text()='New'].
  readonly newButton = this.page.locator('#printpagesPanel').getByRole('button', { name: 'New', exact: true });
  // Selenium: //a[text()='Layout'] — the editor "Layout" tab (creation page is ready when present).
  readonly layoutLink = this.page.getByRole('link', { name: 'Layout', exact: true });
  // Selenium: //select[@name='Template'].
  readonly printPages = this.page.locator("(//a[text()='Print Pages'])[2]");
  readonly templateDropdown = this.page.locator("select[name='Template']");

  constructor(page: Page) {
    super(page);
  }

  /** Selenium "I click on newButton element" on Print Pages. */
  async clickNew(): Promise<void> {
    await this.expectVisible(this.newButton, 'Print Pages "New" button should be visible');
    await this.jsClick(this.newButton);
  }

  /** Selenium pageCreation(): the page-creation editor is ready when the Layout tab is present. */
  async creationPageDisplayed(): Promise<void> {
    await this.expectVisible(this.layoutLink.first(), 'Print Pages creation "Layout" tab should show');
  }

  /**
   * Selenium selectTemplateOnPage(): the just-created template appears in the page's
   * Template dropdown — selecting it by visible text proves it's available.
   */
  async verifyTemplateOnPage(templateName: string): Promise<void> {
    await this.selectOptionLoose(this.templateDropdown, templateName);
  }

  async clickUpdate(): Promise<void> {
    await this.expectVisible(this.updateButton, 'Print Pages Update button should be visible');
    // jsClick: an overlay (e.g. a lingering #help tooltip) can intercept the Update click.
    await this.dismissHelpPopup().catch(() => {});
    await this.jsClick(this.updateButton);
    await this.waitForListSettled();
  }

  /** Selenium numberOfRecords("CountOfPages"). */
  async pageCount(): Promise<number> {
    return this.recordCount(this.countNavBar);
  }

  /**
   * Selenium CopyOfPages(date): tick a random page, Copy, pick target date,
   * "Copy Print Pages", choose channel, manual section "A", copy ads, double-OK,
   * then a possible completion OK (≤240s). Faithful port (the Selenium Copy-of-Pages
   * scenario does not verify the count afterwards — its Then step is commented out).
   */
  async copyPage(targetDdmmyyyy: string): Promise<void> {
    const n = await this.listCheckboxes.count();
    if (n === 0) throw new Error('No print pages to copy');
    const idx = n > 1 ? randomIndex(n - 1) + 1 : 0;
    await this.jsClick(this.listCheckboxes.nth(idx)); // JS-styled marked checkbox
    await this.jsClick(this.copyButton);
    await this.pickOpenDatepicker(targetDdmmyyyy);
    await this.jsClick(this.copyPrintPages);
    await this.page.locator("select[name='Channel']").selectOption({ label: this.channelValue() });
    await this.jsClick(this.pageSelectionRadio);
    await this.pageSection.fill('A');
    await this.jsClick(this.copyAds);
    await this.jsClick(this.okButton.first());
    await this.jsClick(this.okButton.first());
    // Optional completion dialog (Selenium waited ≤240s, swallowed if absent).
    const completion = this.page.getByRole('button', { name: 'OK', exact: true });
    await completion
      .first()
      .waitFor({ state: 'visible', timeout: 240_000 })
      .then(() => completion.first().click())
      .catch(() => {});
  }

  /** Selenium DeleteOfPages(): tick a random page, Delete, confirm OK. */
  async deletePage(): Promise<void> {
    // Index off the actual checkbox set, NOT the nav-bar record total (pageCount) — the two
    // differ (page-size cap, header box, late render), so a count-based index can overrun
    // the checkbox set and time out on a non-existent nth().
    const n = await this.listCheckboxes.count();
    if (n === 0) throw new Error('No print pages to delete');
    let idx = n > 1 ? randomIndex(n - 1) + 1 : 0;
    await this.jsClick(this.listCheckboxes.nth(idx)); // JS-styled marked checkbox
    await expect(this.deleteButton, 'Delete should enable after selecting a page').toBeEnabled({
      timeout: this.defaultWaitMs,
    });
    await this.jsClick(this.deleteButton);
    await this.jsClick(this.okButton.first());
  }

  // ---- Filter / search ----
  async selectSearchType(label: string): Promise<void> {
    await this.searchType.selectOption({ label });
  }

  /** Selenium searchTextOfPrintpages(): type term ("test") then Update. */
  async enterSearchText(text: string): Promise<void> {
    await this.searchBox.fill(text);
    await this.clickUpdate();
  }

  async verifySearchResults(text: string): Promise<void> {
    const rows = await this.page.locator('.libListContentRow.libLine').allInnerTexts();
    for (const r of rows) expect(r, `result row should contain "${text}"`).toContain(text);
  }

  async selectSectionLetter(letter: string): Promise<void> {
    await this.sectionLetterFilter.selectOption({ label: letter });
    await this.clickUpdate();
  }

  // ---- Update (Section Letter) ----
  // The actual page rows (24), not the over-broad libListContent/div (which matched
  // 15 cells × rows = 360 and clicked a wrong sub-div).
  readonly pageRows = this.page.locator("#printpagesPanel div[class*='libListContentRow']");
  // Selenium: //li[@class='ui-state-default ui-corner-top']/a[text()='Print Pages'] — the editor
  // TAB link (NOT the left-nav menu link of the same name). Scope to the tab <li> to disambiguate.
  // readonly printPagesTab = this.page
  //   .locator('li.ui-state-default.ui-corner-top')
  //   .getByText('Print Pages', { exact: true });
  readonly printPagesTab = this.page.locator("//li[@class='ui-state-default ui-corner-top']/a[text()='Print Pages']"); 
  readonly sectionLetterInput = this.page.locator("input[name='SectionLetter']");
  readonly saveAndCloseButton = this.page.locator('button.lib-button-saveclose');

  /** Selenium selectPageForUpdate(): open a random page from the list (double-click to open). */
  async openRandomPage(): Promise<void> {
    const n = await this.pageRows.count();
    expect(n, 'there should be a print page row to open').toBeGreaterThan(0);
    let idx = randomIndex(n);
    if (idx === 0) idx = 1;
    const row = this.pageRows.nth(Math.min(idx, n - 1));
    await row.scrollIntoViewIfNeeded().catch(() => {});
    // A single click only selects the row; double-click opens the page editor.
    // force: true matches Selenium's Actions.doubleClick — it clicks through the sticky list
    // chrome (libHeader/libNavBar/libSplitContainer) that Playwright's actionability flags as
    // "intercepting"; Selenium has no such check, so this reproduces its behaviour (open editor).
    await row.dblclick({ force: true });
    // Opening a page editor triggers a "Caching alert: Loading print shapes and styles, please
    // wait..." dialog (the same shapes/styles load the login waits on). The editor isn't usable
    // until it clears — Selenium absorbed this with shortWait()s. Wait it out (best-effort).
    await this.waitForPrintShapesLoaded();
    await this.printPages.first();
    await this.sectionLetterInput.first().waitFor({ state: 'attached', timeout: this.defaultWaitMs }).catch(() => {});
  }

  /** Wait for the "Loading print shapes and styles…" caching alert to clear (≤180s). */
  private async waitForPrintShapesLoaded(): Promise<void> {
    const loading = this.page.getByText('Loading print shapes and styles', { exact: false });
    // It may already be gone (race) — only wait if it actually appeared.
    if (await loading.first().isVisible().catch(() => false)) {
      await loading.first().waitFor({ state: 'hidden', timeout: 180_000 }).catch(() => {});
    }
    await this.page.waitForTimeout(1_000);
  }

  /** Selenium printPageUpdate("sectionletter"): set Section Letter to "B", save & close. */
  async updateSectionLetter(value = 'B'): Promise<string> {
    await this.waitForPrintShapesLoaded();
    await this.jsClick(this.printPagesTab.first()).catch(() => {});
    // SectionLetter renders only once the Print Pages tab is active — wait for it.
    await this.sectionLetterInput.first().waitFor({ state: 'visible', timeout: this.defaultWaitMs }).catch(() => {});
    await this.sectionLetterInput.clear();
    await this.sectionLetterInput.fill(value);
    await this.jsClick(this.saveAndCloseButton.first());
    await this.loader.waitFor({
    state: 'hidden',
    timeout: 60000,
    });
    return value;
  }

  /** Selenium verifyPageUpdate("sectionletter"): reopen a page, assert the Section Letter value. */
  async verifySectionLetterUpdated(value: string): Promise<void> {
    // After save & close the list re-renders (~12s); wait before reopening a row.
    await this.waitForListSettled();
    const n = await this.pageRows.count();
    let idx = randomIndex(n);
    if (idx === 0) idx = 1;
    await this.pageRows.nth(Math.min(idx, n - 1)).scrollIntoViewIfNeeded().catch(() => {});
    // force: true matches Selenium's Actions.doubleClick — it clicks through the sticky list
    // chrome (libHeader/libNavBar/libSplitContainer) that Playwright's actionability flags as
    // "intercepting"; Selenium has no such check, so this reproduces its behaviour (open editor).
    await this.pageRows.nth(Math.min(idx, n - 1)).dblclick({ force: true });
    await this.jsClick(this.printPagesTab.first()).catch(() => {});
    await expect(this.sectionLetterInput.first(), `Section Letter should be "${value}"`).toHaveValue(value, {
      timeout: this.defaultWaitMs,
    });
  }

  // ── Page-creation form + Layout + drag-drop + PDF proof (ported from PrintPages.java /
  //    OutputOfPrintPages.java; the drag-into-iframe + PDF-proof flows that were deferred). ──
  readonly statusDropdown = this.page.locator("select[name='Status']");
  readonly sectionLetterFormInput = this.page.locator("input[name='SectionLetter']");
  readonly pageNumberInput = this.page.locator("input[name='Page']");
  readonly addStoryButton = this.page.locator("button[name='add']");
  // Selenium: (//select[@name='PublishDateRange'])[2] — the add-stories panel's range select.
  readonly addPanelRange = this.page.locator("select[name='PublishDateRange']");
  readonly addStoriesMarked = this.page.locator("#addstoriestopage-main input[name='marked']");
  readonly addAdsMarked = this.page.locator("#addadstopage-main input[name='marked']");
  readonly saveStoryButton = this.page.locator("//button[@class='ui-button ui-widget ui-state-default ui-corner-all ui-button-text-icon-primary']//span[@class='ui-button-text'][text()='Save']")
  // this.page
  //   .locator('button.ui-button-text-icon-primary')
  //   .filter({ has: this.page.getByText('Save', { exact: true }) });

  // readonly saveStoryButton = this.page.locator('span').filter({ hasText: 'Save' }).first();
  readonly pageSaveButton = this.page.locator('button.lib-button-save');
  readonly adEditModeOn = this.page.locator("label[for='modeonprintpages']").getByText('On', { exact: true });
  readonly addAdButton = this.page.locator("button[name='addAd']");
  // Output / PDF-proof (OutputOfPrintPages.java).
  readonly existingPageRow = this.page.locator('.libListContentRow.libLine').first();
  readonly outputButton = this.page.locator("//button[@class='lib-button-output ui-button ui-widget ui-state-default ui-corner-all ui-button-text-icon-primary btnFixed']//span[@class='ui-button-text'][text()='Output']");
  readonly pdfProofToDesktop = this.page.locator("//div[@role='dialog']//input[2]");
  readonly pdfProofOk = this.page.locator('span').filter({ hasText: 'OK' }).last();
  readonly pdfDialog = this.page.locator('.libGeneralDialogContent.libDialogPadding');
  readonly pdfClickLink = this.page.getByRole('link', { name: 'Click this link', exact: false });
  readonly popupCloseOk = this.page
    .locator('button.ui-button-text-icon-primary')
    .filter({ has: this.page.getByText('OK', { exact: true }) });
  // Layout iframe + drag source/target (jQuery-UI draggable/droppable).
  get layoutFrame() {
    return this.page.frameLocator('iframe.libPageLayoutAreaframe');
  }
  readonly dragSourceStory = this.page.locator("xpath=//div[@class='libPageStory libTruncated']/..");
  readonly dragSourceAd = this.page.locator("xpath=//div[@class='libPageSidebar libTruncated']/..");
  readonly layoutTarget = this.layoutFrame.locator('.layoutContent.ui-droppable');
  readonly droppedStory = this.layoutFrame.locator('.libPageElement.libPageShapeElement.ui-droppable');
  readonly droppedAd = this.layoutFrame.locator('.libPageElement.libPageShapeElement');

  /** Selenium: fill the new-page header — publish date (form picker), status, section, page#, template. */
  async fillNewPageForm(section: string, pageNum: string, templateText: string, status: string): Promise<void> {
    // "I enter the publish date for the page" is Thread.sleep(100000) in Selenium (a manual
    // pause for the human to pick a date). We pick it programmatically via the form datepicker.
    // await this.enterFormPublishDate(String(getDates().dateForStories)).catch(() => {});
    await this.selectOptionLoose(this.statusDropdown.first(), status);
    await this.fillVisible(this.sectionLetterFormInput, section);
    await this.fillVisible(this.pageNumberInput, pageNum);
    await this.selectOptionLoose(this.templateDropdown.first(), templateText);
  }

  /** Selenium tap_Layout(): click the Layout tab, then press "1" (selects the page-1 layout area). */
  async tapLayout(): Promise<void> {
    await this.expectVisible(this.layoutLink.first(), 'Layout tab should be visible');
    await this.jsClick(this.layoutLink.first());
    await this.page.keyboard.press('1').catch(() => {});
    await this.page.waitForTimeout(1_000);
  }

  /**
   * Selenium "the Layout page should be displayed": output.isDisplayed(). The Output
   * button is rendered (often DISABLED for an unsaved page — still "displayed") and the
   * panel has hidden duplicates, so assert ANY VISIBLE instance (not .first(), which can
   * be a hidden duplicate), matching Selenium's isDisplayed check.
   */
  async expectLayoutDisplayed(): Promise<void> {
    await this.expectAnyVisible(this.outputButton, 'Layout/Output view should be displayed');
  }

  /** Selenium: add-stories panel → range "Last 3 Months" → Update → pick a story → Save. */
  async addStoryToPage(): Promise<void> {
    await this.jsClick(this.addStoryButton.first());
    const range = (await this.firstVisible(this.addPanelRange)) ?? this.addPanelRange.first();
    await this.selectOptionLoose(range, 'Last 3 Months').catch(() => {});
    await this.jsClick(this.page.locator('#addstoriestopage-main').getByRole('button', { name: 'Update', exact: true })).catch(() => {});
    await this.waitForListSettled().catch(() => {});
    const n = await this.addStoriesMarked.count();
    if (n === 0) throw new Error('No stories available to add to the page (date-dependent)');
    await this.jsClick(this.addStoriesMarked.first());
    await this.jsClick(this.saveStoryButton);
    // await this.jsClick((await this.firstVisible(this.saveStoryButton)) ?? this.saveStoryButton.first());
    // await this.saveStoryButton.click();
    await this.page.waitForTimeout(1_900);
  }

  /** Selenium adEditMode() + add a print ad to the page. */
  async addPrintAdToPage(): Promise<void> {
    await this.jsClick(this.adEditModeOn.first());
    await this.jsClick(this.addAdButton.first());
    await this.waitForListSettled().catch(() => {});
    const n = await this.addAdsMarked.count();
    if (n === 0) throw new Error('No print ads available to add to the page (date/data-dependent)');
    await this.jsClick(this.addAdsMarked.first());
    await this.jsClick((await this.firstVisible(this.saveStoryButton)) ?? this.saveStoryButton.first());
    await this.page.waitForTimeout(1_500);
  }

  /**
   * Selenium drag_Drop(): cross-frame drag of the story/ad panel element onto the layout
   * canvas inside iframe.libPageLayoutAreaframe, then JS-position the dropped element.
   * Playwright boundingBox() on a frameLocator element is page-relative, so a manual
   * mouse drag (down on source → move into the iframe target → up) drives the jQuery-UI
   * draggable/droppable.
   */
  async dragOntoLayout(kind: 'story' | 'printad'): Promise<void> {
    const source = kind === 'story' ? this.dragSourceStory : this.dragSourceAd;
    await source.first().scrollIntoViewIfNeeded().catch(() => {});
    const src = await source.first().boundingBox();
    const tgt = await this.layoutTarget.first().boundingBox();
    if (!src || !tgt) throw new Error('Drag source or layout target not found (page editor not ready)');
    await this.page.mouse.move(src.x + src.width / 2, src.y + src.height / 2);
    await this.page.mouse.down();
    await this.page.mouse.move(tgt.x + tgt.width / 2, tgt.y + tgt.height / 2, { steps: 12 });
    await this.page.mouse.move(tgt.x + tgt.width / 2 + 5, tgt.y + tgt.height / 2 + 5, { steps: 4 });
    await this.page.mouse.up();
    // Selenium JS-sets the dropped element's position style; mirror it so it lands on-page.
    const dropped = kind === 'story' ? this.droppedStory : this.droppedAd;
    await dropped
      .first()
      .evaluate((el) => ((el as HTMLElement).style.cssText = 'position:absolute;top:40pt;left:2pt;'))
      .catch(() => {});
    await this.page.waitForTimeout(1_000);
  }

  /** Selenium "I click save and close button on CMS PrintPages": jsClick the visible save&close. */
  async saveAndClose(): Promise<void> {
    await expect(this.saveAndCloseButton.first(), 'Save & Close should be attached').toBeAttached({
      timeout: this.defaultWaitMs,
    });
    const n = await this.saveAndCloseButton.count();
    for (let i = 0; i < n; i++) {
      if (await this.saveAndCloseButton.nth(i).isVisible()) {
        await this.jsClick(this.saveAndCloseButton.nth(i));
        return;
      }
    }
    await this.jsClick(this.saveAndCloseButton.first());
  }

  /** Selenium tapOnPage(): open the first existing page row (click), wait for the Layout tab (≤180s). */
  async openExistingPage(): Promise<void> {
    await this.jsClick(this.existingPageRow);
    await this.expectVisible(this.layoutLink.first(), 'opened page Layout tab should appear', 180_000);
  }

  /** Selenium PDF-proof flow: Output → PDF Proof to desktop → OK → completion dialog → link → close → save&close. */
  async generatePdfProof(): Promise<void> {
    await this.jsClick(this.outputButton.first());
    await this.expectVisible(this.pdfProofToDesktop, 'PDF proof option should appear');
    await this.jsClick(this.pdfProofToDesktop);
    await this.jsClick(this.pdfProofOk.first());
    await this.expectVisible(this.pdfDialog.first(), 'PDF generation dialog should appear', 120_000);
    await this.expectVisible(this.pdfClickLink.first(), '"Click this link" should appear when the PDF is ready', 120_000);
    await this.jsClick(this.pdfClickLink.first());
    await this.jsClick((await this.firstVisible(this.popupCloseOk)) ?? this.popupCloseOk.first()).catch(() => {});
    await this.jsClick(this.saveAndCloseButton.first()).catch(() => {});
  }

  /** Selenium verifySectoionLetterSearchForPages(): each row's section column == letter. */
  async verifySectionLetterResults(letter: string): Promise<void> {
    const rows = this.page.locator('.libListContentRow.libLine');
    const total = await rows.count();
    for (let i = 0; i < total; i++) {
      // Selenium read the row's div[7] (the section column) → nth(6) of the row's direct-child divs.
      const col = await rows.nth(i).locator(':scope > div').nth(6).innerText().catch(() => '');
      expect(col.trim(), `row ${i + 1} section should be "${letter}"`).toContain(letter);
    }
  }
}

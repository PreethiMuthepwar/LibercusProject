import { type Page } from '@playwright/test';
import { CmsBasePage } from './cms-base.page';

/**
 * CMS Feeds module: Wire Stories and Wire Photos ("Use" + publish flow).
 * Ported from Selenium pages/Libercus/CMS/{WireStories,WirePhotos}.java + RegressionSteps.java.
 *
 * These scenarios are date-driven (they pick a wire transmission range and a publish
 * date via the jQuery-UI datepicker) and data-dependent (wire items must exist in the
 * chosen range on the target environment).
 */
export class WireFeedsPage extends CmsBasePage {
  // Selenium @FindBy provenance kept inline.
  readonly transmittedDateRange = this.page.locator("select[name='TransmittedDateRange']");
  readonly wireStoriesUpdate = this.page.locator('button.libButtonRefresh');
  readonly photosUpdate = this.page.locator('#feedphotos-main').getByRole('button', { name: 'Update', exact: true });
  readonly storyCheckboxes = this.page.locator('div.libCheckColumn.libNoClick > input');
  readonly photoCheckColumns = this.page.locator('div.libCheckColumn.rp3');
  readonly useButton = this.page.locator('button.lib-button-assign');
  readonly popupChannels = this.page.locator("select[name='channels']");
  readonly omitPhotos = this.page.locator("input[name='skipusingphotos']");
  readonly usePublishDate = this.page.locator("input[name='publishdate']");
  readonly slugInput = this.page.locator("input[name='slug']");
  readonly titlePanel = this.page.locator('div.libListContentPanelTitle');
  readonly loader = this.page.locator('div.libListContent.libLoading');
  // The Use confirmation button in the "Use Story/Photo" dialog.
  readonly useConfirm = this.page.locator('.ui-dialog').getByRole('button', { name: 'Use', exact: true });

  private capturedSlug = '';
  private capturedTitle = '';

  constructor(page: Page) {
    super(page);
  }

  get slug(): string {
    return this.capturedSlug;
  }
  get title(): string {
    return this.capturedTitle;
  }

  /** Selenium: select a transmitted-date range (e.g. "Last Month") on the visible dropdown. */
  async selectTransmittedRange(label: string): Promise<void> {
    const sel = (await this.firstVisible(this.transmittedDateRange)) ?? this.transmittedDateRange.first();
    await this.selectOptionLoose(sel, label);
  }

  private async clickWireUpdate(): Promise<void> {
    const btn = (await this.firstVisible(this.wireStoriesUpdate)) ?? this.wireStoriesUpdate.first();
    await btn.click();
    // Wait for the list to settle (rows render or "No items found").
    await this.waitForListSettled().catch(() => {});
  }

  /**
   * Selenium verifyStoriesAreAvailable: Update; if "No items found", widen the
   * transmitted range to "Last 3 Months" and Update again.
   */
  async ensureStoriesAvailable(): Promise<void> {
    await this.clickWireUpdate();
    if (await this.isEmptyList()) {
      await this.selectTransmittedRange('Last 3 Months');
      await this.clickWireUpdate();
    }
  }

  /**
   * Selenium verifyPhotosAreAvailable: Update; widen to "Last 3 Months" then
   * "Last Year" if still empty.
   */
  async ensurePhotosAvailable(): Promise<void> {
    await this.photosUpdate.click();
    await this.loader.waitFor({
    state: 'hidden',
    timeout: 60000,
    });
    if (await this.isEmptyList()) {
      await this.selectTransmittedRange('Last 3 Months');
      await this.photosUpdate.click();
      if (await this.isEmptyList()) {
        await this.selectTransmittedRange('Last Year');
        await this.photosUpdate.click();
      }
    }
  }

  /** Selenium selectStory: jsClick a random wire-story checkbox (index ≥ 1). */
  async selectStory(): Promise<void> {
    const n = await this.storyCheckboxes.count();
    if (n < 1) throw new Error('No wire stories available to select');
    let idx = Math.floor(Math.random() * n);
    if (idx === 0 && n > 1) idx = Math.floor(Math.random() * (n - 1)) + 1;
    await this.jsClick(this.storyCheckboxes.nth(idx));
  }

  /** Selenium selectPhoto: jsClick a random wire-photo check column; capture the title. */
  async selectPhoto(): Promise<void> {
    const n = await this.photoCheckColumns.count();
    if (n < 1) throw new Error('No wire photos available to select');
    const idx = Math.floor(Math.random() * n);
    await this.jsClick(this.photoCheckColumns.nth(idx));
    // Selenium read titleInput.getAttribute("value"); the panel is a <div>, so value
    // is usually empty — fall back to its text so the later search is meaningful.
    const byValue = (await this.titlePanel.first().getAttribute('value').catch(() => null)) ?? '';
    this.capturedTitle = byValue || (await this.titlePanel.first().innerText().catch(() => '')).trim();
  }

  /** Selenium: click the list "Use" button (lib-button-assign). */
  async clickUse(): Promise<void> {
    const btn = (await this.firstVisible(this.useButton)) ?? this.useButton.first();
    await this.expectVisible(btn, 'Use button should be visible');
    await this.jsClick(btn);
  }

  /**
   * Selenium enterPublishDate(date): open the popup datepicker (input[name='publishdate'])
   * and pick the date. Uses the same jQuery-UI navigation as the rest of the suite.
   */
  async enterUsePublishDate(ddmmyyyy: string): Promise<void> {
    const input = (await this.firstVisible(this.usePublishDate)) ?? this.usePublishDate.first();
    await this.expectVisible(input, 'publish-date input should be visible in the Use popup');
    await input.click();
    await this.pickOpenDatepicker(ddmmyyyy, (d) => `//a[text()='${d}' and contains(@class,'ui-state-default')]`);
  }

  /** Selenium: select the customer channel in the Use popup's channel dropdown. */
  async selectPopupChannel(): Promise<void> {
    const sel = (await this.firstVisible(this.popupChannels)) ?? this.popupChannels.first();
    if (await sel.count().then((c) => c > 0).catch(() => false)) {
      await this.selectOptionLoose(sel, this.channelValue());
      return;
    }
    // Fallback to the generic channel dropdown if the popup uses name='channel'.
    await this.selectChannel();
  }

  /** Selenium tapOnCheckbox: capture the slug, then tick "Omit photos". */
  async omitPhotosAndCaptureSlug(): Promise<void> {
    this.capturedSlug = (await this.slugInput.first().inputValue().catch(() => '')) || '';
    await this.jsClick(this.omitPhotos.first());
  }

  /** Selenium tapOnUseButton: confirm "Use" and wait for the overlay loader to clear. */
  async confirmUse(): Promise<void> {
    const btn = (await this.firstVisible(this.useConfirm)) ?? this.useConfirm.first();
    await this.jsClick(btn);
    await this.page
      .locator('#libOverlayLoader')
      .waitFor({ state: 'hidden', timeout: this.defaultWaitMs })
      .catch(() => {});
  }
}

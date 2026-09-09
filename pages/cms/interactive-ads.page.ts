import { type Page, expect } from '@playwright/test';
import { CmsBasePage } from './cms-base.page';
import { getInteractiveAdsData, resolveAsset } from '../../utils/data.utils';

/**
 * CMS Interactive Ads page.
 * Ported from Selenium pages/Libercus/CMS/InteractiveAds.java (create flow).
 */
export class InteractiveAdsPage extends CmsBasePage {
  // Refactored to Playwright-standard locators (same elements as the Selenium @FindBy XPath,
  // just non-XPath style; click semantics in the methods are unchanged). Selenium provenance
  // kept inline.
  // Selenium: //div[@id='interactiveads-main']//select[@name='channel']
  readonly channelDropdown = this.page.locator("#interactiveads-main select[name='channel']");
  // Selenium: //div[@id='interactiveadsPanel']//button[@name='new'] (same button, attribute CSS).
  readonly newButton = this.page.locator("#interactiveadsPanel button[name='new']");
  // Selenium: //label[text()='Interactive AdID']
  readonly interactiveAdId = this.page.getByText('Interactive AdID', { exact: true });
  // Selenium: //div[@id='interactiveadsEdit-main']//select[@name='Status']
  readonly statusDropdown = this.page.locator("#interactiveadsEdit-main select[name='Status']");
  readonly adTypeDropdown = this.page.locator("select[name='AdType']");
  readonly interactiveAdIdInput = this.page.locator("input[name='InteractiveAdID']");
  // Selenium: //input[@class='lib-post libTextFieldSize libValidate' and @name='Title'] (exact class+name).
  readonly title = this.page.locator("input.lib-post.libTextFieldSize.libValidate[name='Title']");
  readonly slug = this.page.locator("input.lib-post.libTextFieldSize.libValidate[name='Slug']");
  readonly mobileCheckbox = this.page.locator("input[name='SupportsMobile']");
  readonly tabletCheckbox = this.page.locator("input[name='SupportsTablet']");
  readonly desktopCheckbox = this.page.locator("input[name='SupportsDesktop']");
  readonly imageField = this.page.locator('input#mediadata');
  readonly zipField = this.page.locator('input#zipmediadata');
  readonly imageSrc = this.page.locator('#lib-preview-area img');
  // Selenium class lib-button-saveclose (jsClick in the method, unchanged).
  readonly saveAndCloseButton = this.page.locator('button.lib-button-saveclose');
  // Toolbar buttons: Selenium targets the <span> text inside the panel button; panel-scoped
  // getByRole('button') activates the SAME button (validated pattern from story.page).
  readonly updateButton = this.page
    .locator('#interactiveads-main')
    .getByRole('button', { name: 'Update', exact: true });
  // Selenium: //div[@class='libCheckColumn rp3']/input[@name='marked']
  readonly listCheckboxes = this.page.locator("div.libCheckColumn.rp3 > input[name='marked']");
  readonly copyButton = this.page
    .locator('#interactiveadsPanel')
    .getByRole('button', { name: 'Copy', exact: true });
  readonly deleteButton = this.page
    .locator('#interactiveadsPanel')
    .getByRole('button', { name: 'Delete', exact: true });
  readonly downloadButton = this.page
    .locator('#interactiveadsPanel')
    .getByRole('button', { name: 'Download', exact: true });
  readonly okButton = this.page.getByRole('button', { name: 'OK', exact: true });
  readonly countNavBar = this.page.locator('#interactiveads-main .libNavBarTextArea');
  readonly searchType = this.page.locator("select[name='interactiveadsSearch']");
  readonly searchBox = this.page.locator("input[name='interactiveadsSearch_search']");
  // Selenium: //div[@id='interactiveads-main']//select[contains(@name,'Device')]
  readonly deviceType = this.page.locator("#interactiveads-main select[name*='Device']");
  readonly resultThumbnails = this.page.locator('.libLine.libListContentPanel.libThumbnailPanel');
  readonly resultTitles = this.page.locator(
    '.libLine.libListContentPanel.libThumbnailPanel .libListContentPanelTitle',
  );
  readonly supportsTablet = this.page.locator("input[name='SupportsTablet']");
  readonly supportsMobile = this.page.locator("input[name='SupportsMobile']");
  readonly supportsDesktop = this.page.locator("input[name='SupportsDesktop']");

  constructor(page: Page) {
    super(page);
  }

  /** Selenium numberOfRecords("CountOfInteractiveAds"). */
  async adCount(): Promise<number> {
    return this.recordCount(this.countNavBar);
  }

  /**
   * Selenium copyOfAds(date): tick a random ad, click Copy, pick target date,
   * choose the customer channel in the popup, OK.
   */
  async copyAd(targetDdmmyyyy: string): Promise<void> {
    const n = await this.listCheckboxes.count();
    if (n === 0) throw new Error('No interactive ads to copy');
    await this.jsClick(this.listCheckboxes.nth(Math.floor(Math.random() * n)));
    await this.copyButton.click();
    await this.pickOpenDatepicker(targetDdmmyyyy);
    await this.page
      .locator("select[name='Channel']")
      .selectOption({ label: this.channelValue() });
    await this.okButton.first().click();
  }

  /** Selenium downloadOfAds(): tick a random ad, click Download, capture the file. */
  async downloadAd(): Promise<import('@playwright/test').Download> {
    const n = await this.listCheckboxes.count();
    if (n === 0) throw new Error('No interactive ads to download');
    await this.jsClick(this.listCheckboxes.nth(Math.floor(Math.random() * n)));
    const [download] = await Promise.all([
      this.page.waitForEvent('download', { timeout: this.defaultWaitMs }),
      this.downloadButton.click(),
    ]);
    return download;
  }

  // ---- Filter / search ----
  async selectSearchType(label: string): Promise<void> {
    await this.searchType.selectOption({ label });
  }

  /** Selenium searchTextOfInteractiveAd(): type term ("761996") then Update. */
  async enterSearchText(text: string): Promise<void> {
    await this.searchBox.fill(text);
    await this.clickUpdate();
  }

  /** Selenium verifySearchResultsOfInteractiveAd(): each result title contains the term. */
  async verifySearchResults(text: string): Promise<void> {
    const titles = await this.resultTitles.allInnerTexts();
    for (const t of titles) expect(t, `result should contain "${text}"`).toContain(text);
  }

  /** Selenium "Tablet" device filter: select device, Update. */
  async selectDeviceType(label: string): Promise<void> {
    await this.deviceType.selectOption({ label });
    await this.clickUpdate();
  }

  /**
   * Selenium VerifyDeviceTypeSearchResults(device): open a random result and assert the
   * matching SupportsX checkbox is selected.
   */
  async verifyDeviceSelected(device: string): Promise<void> {
    if (await this.noItemsFound.first().isVisible().catch(() => false)) {
      await this.selectPastItems();
      await this.clickUpdate();
    }
    const n = await this.resultThumbnails.count();
    if (n === 0) {
      // Selenium VerifyDeviceTypeSearchResults: when no items match the device filter,
      // it logs and skips the assertion. Mirror that (no data → nothing to verify).
      console.log(`[IA device filter] no ${device} interactive ads to inspect — skipping (Selenium parity)`);
      return;
    }
    await this.jsClick(this.resultThumbnails.nth(Math.floor(Math.random() * n)));
    const map: Record<string, typeof this.supportsTablet> = {
      Tablet: this.supportsTablet,
      Mobile: this.supportsMobile,
      Desktop: this.supportsDesktop,
    };
    const cb = map[device] ?? this.supportsTablet;
    await expect(cb, `${device} support should be selected`).toBeChecked({ timeout: this.defaultWaitMs });
  }

  // ---- Update ----
  // Selenium: a generic jQuery-UI dialog button (exact class) → CSS class chain (same element).
  readonly popupButton = this.page.locator(
    'button.ui-button.ui-widget.ui-state-default.ui-corner-all.ui-button-text-icon-primary',
  );

  /** Selenium selectInteractiveAd(): open a random ad from the list. */
  async openRandomAd(): Promise<void> {
    if (await this.noItemsFound.first().isVisible().catch(() => false)) {
      await this.selectPastItems();
      await this.clickUpdate();
    }
    const n = await this.resultThumbnails.count();
    expect(n, 'there should be an interactive ad to open').toBeGreaterThan(0);
    await this.jsClick(this.resultThumbnails.nth(Math.floor(Math.random() * n)));
  }

  /** Selenium interactiveAdUpdate("title"): clear + set a random Title. Returns the value. */
  async updateTitle(): Promise<string> {
    const value = `IA${Math.floor(Math.random() * 1e6)}`;
    await this.title.clear();
    await this.jsClick(this.popupButton.first()).catch(() => {});
    await this.title.fill(value);
    return value;
  }

  /** Selenium verifyInteractiveAdUpdate("title"): reopen the ad and assert the Title value. */
  async verifyTitleUpdated(value: string): Promise<void> {
    const tile = this.page.getByText(value, { exact: true }).first();
    if (await tile.count()) await this.jsClick(tile);
    await expect(this.title, `Interactive Ad title should be "${value}"`).toHaveValue(value, {
      timeout: this.defaultWaitMs,
    });
  }

  /** Selenium DeleteOfAds(): tick a random ad, Delete, confirm OK. */
  async deleteAd(): Promise<void> {
    const n = await this.listCheckboxes.count();
    if (n === 0) throw new Error('No interactive ads to delete');
    await this.jsClick(this.listCheckboxes.nth(Math.floor(Math.random() * n)));
    await this.deleteButton.click();
    await this.okButton.first().click();
  }

  /** Selenium clickUpdateOnInteractiveAdsPage(); waits for the list to render. */
  async clickUpdate(): Promise<void> {
    await this.expectVisible(this.updateButton, 'Interactive Ads Update button should be visible');
    await this.updateButton.click();
    await this.waitForListSettled();
  }

  async clickNew(): Promise<void> {
    await this.expectVisible(this.newButton, 'Interactive Ads "New" button should be visible');
    await this.newButton.click();
  }

  /** Selenium interactiveAdCreationPage(): the "Interactive AdID" label is shown. */
  async expectCreationPage(): Promise<void> {
    await expect(this.interactiveAdId, 'Interactive Ad creation page should show "Interactive AdID"').toHaveText(
      /Interactive AdID/i,
      { timeout: this.defaultWaitMs },
    );
  }

  async selectStatusPublished(): Promise<void> {
    await this.statusDropdown.selectOption({ label: 'Published' });
  }

  /** Selenium adType(): select "Fullscreen Ad" + fill InteractiveAdID. */
  async enterAdType(): Promise<void> {
    await this.adTypeDropdown.selectOption({ label: 'Fullscreen Ad' });
    await this.interactiveAdIdInput.fill('123456');
  }

  async enterTitleSlug(): Promise<void> {
    await this.title.fill('Title');
    await this.slug.fill('Slug');
  }

  /** Selenium clickCheckboxes(): tick mobile/tablet/desktop if not already set. */
  async selectAllDeviceCheckboxes(): Promise<void> {
    for (const cb of [this.mobileCheckbox, this.tabletCheckbox, this.desktopCheckbox]) {
      if ((await cb.count()) && !(await cb.first().isChecked().catch(() => false))) {
        await this.jsClick(cb.first());
      }
    }
  }

  /** Selenium addImageOnInteractivePage(image). key -> data/InteractiveAds.yml Image. */
  async uploadImage(key: string): Promise<void> {
    const image = String(getInteractiveAdsData(key).Image);
    await this.imageField.setInputFiles(resolveAsset(image));
  }

  /** Selenium imageIsUploaded(): preview img src contains /admin/imgpreview/. */
  async expectImageUploaded(): Promise<void> {
    await expect(this.imageSrc, 'uploaded image preview should be present').toHaveAttribute(
      'src',
      /\/admin\/imgpreview\//,
      { timeout: this.defaultWaitMs },
    );
  }

  /** Selenium addZipFile(zip). key -> data/InteractiveAds.yml ZipFile. */
  async uploadZip(key: string): Promise<void> {
    const zip = String(getInteractiveAdsData(key).ZipFile);
    await this.zipField.setInputFiles(resolveAsset(zip));
  }

  async expectZipUploaded(): Promise<void> {
    await expect
      .poll(async () => (await this.zipField.inputValue()).length, {
        timeout: this.defaultWaitMs,
        message: 'ZIP file should be selected',
      })
      .toBeGreaterThan(0);
  }

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
}

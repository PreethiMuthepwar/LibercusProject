import { type Page, expect } from '@playwright/test';
import { CmsBasePage } from './cms-base.page';
import { getRichMediaAdData, resolveAsset } from '../../utils/data.utils';

/**
 * CMS Rich Media Ads page.
 * Ported from Selenium pages/Libercus/CMS/RichMediaAds.java (create flow).
 */
export class RichMediaAdsPage extends CmsBasePage {
  // Refactored to Playwright-standard locators (same elements as the Selenium @FindBy XPath;
  // click semantics in the methods unchanged). Selenium provenance kept inline.
  readonly channelDropdown = this.page.locator("#richmedia-main select[name='channel']");
  // Toolbar buttons: Selenium targets the <span> text inside the panel button; panel-scoped
  // getByRole('button') activates the SAME button (validated pattern from story.page).
  readonly newButton = this.page.locator('#richmediaPanel').getByRole('button', { name: 'New', exact: true });
  readonly richMediaAdId = this.page.getByText('Rich Media ID', { exact: true });
  readonly statusDropdown = this.page.locator("#richmediaEdit-main select[name='Status']");
  readonly adExtRefIdInput = this.page.locator("input[name='AdExtrefID']");
  readonly title = this.page.locator("input[name='Title']");
  readonly slug = this.page.locator("input[name='Slug']");
  readonly tabletCheckbox = this.page.locator('input#lib-group1-toggle');
  readonly addButton = this.page.locator("button[name='add']");
  readonly imageField = this.page.locator('input#mediadata');
  readonly zipField = this.page.locator('input#zipmediadata');
  // Selenium: a primary button whose span text is 'Save' (dialog image-save).
  readonly imageSaveButton = this.page.getByRole('button', { name: 'Save', exact: true });
  // Selenium: //button[@name='save']/span[text()='Save & Close'] → the save button filtered by text
  // (preserves the count/nth/isVisible iteration in saveAndClose()).
  readonly saveAndCloseButton = this.page.locator("button[name='save']").filter({ hasText: 'Save & Close' });
  readonly updateButton = this.page.locator('#richmedia-main').getByRole('button', { name: 'Update', exact: true });
  // Selenium: //div[@id='richmedia-main']/div/div/div/div/div/input[@type='checkbox'] — a
  // positional list-row checkbox set; preserved faithfully as the same direct-child chain (CSS).
  readonly listCheckboxes = this.page.locator(
    "#richmedia-main div > div > div > div > div > input[type='checkbox']",
  );
  readonly copyButton = this.page.locator('#richmediaPanel').getByRole('button', { name: 'Copy', exact: true });
  readonly deleteButton = this.page.locator('#richmediaPanel').getByRole('button', { name: 'Delete', exact: true });
  readonly downloadButton = this.page.locator('#richmediaPanel').getByRole('button', { name: 'Download', exact: true });
  readonly libercusOption = this.page.getByText('Libercus', { exact: true });
  readonly okButton = this.page.getByRole('button', { name: 'OK', exact: true });
  readonly countNavBar = this.page.locator('#richmedia-main .libNavBarTextArea');
  readonly searchType = this.page.locator("select[name='richmediaSearch']");
  readonly searchBox = this.page.locator("input[name='richmediaSearch_search']");
  readonly deviceType = this.page.locator("#richmedia-main select[name*='Device']");
  readonly adTypesFilter = this.page.locator("select[name='AdTypes']");
  readonly resultThumbnails = this.page.locator('.libLine.libListContentPanel.libThumbnailPanel');
  readonly resultTitles = this.page.locator(
    '.libLine.libListContentPanel.libThumbnailPanel .libListContentPanelTitle',
  );
  readonly tabletToggle = this.page.locator('input#lib-group2-toggle');
  // Selenium: input[name='DesktopPuzzle'] / select[name='RichMediaAdType'] (used by both the
  // create form and the device/ad-type verification).
  readonly desktopToggle = this.page.locator("input[name='DesktopPuzzle']");
  readonly adTypeSelect = this.page.locator("select[name='RichMediaAdType']");

  constructor(page: Page) {
    super(page);
  }

  /** Selenium numberOfRecords("CountOfRichMediaAds"). */
  async adCount(): Promise<number> {
    return this.recordCount(this.countNavBar);
  }

  /**
   * Selenium CopyOfAds(date, slug): tick a random ad, Copy, pick target date,
   * choose Libercus, select the customer channel, OK.
   */
  async copyAd(targetDdmmyyyy: string): Promise<void> {
    const n = await this.listCheckboxes.count();
    if (n === 0) throw new Error('No rich media ads to copy');
    await this.jsClick(this.listCheckboxes.nth(Math.floor(Math.random() * n)));
    await this.copyButton.click();
    await this.pickOpenDatepicker(targetDdmmyyyy);
    await this.libercusOption.click();
    await this.page
      .locator("select[name='Channel']")
      .selectOption({ label: this.channelValue() });
    await this.okButton.first().click();
  }

  /**
   * Selenium downloadOfAdsInRichMediaAds(): select a random ad, click Download. (Selenium also
   * clicks Download once BEFORE selecting, but that button is disabled until a row is selected, so
   * it is a no-op — we skip it to avoid a 30s wait-for-enabled hang; net behaviour is identical.)
   *
   * Unlike Interactive Ads (which fire a normal browser download event), the Rich Media download
   * opens a NEW TAB navigating to `/admin/download?datatype=richmedia&ids=...` — so the original
   * page never fires a 'download' event (that was the timeout). We capture the popup and verify it
   * hit the download endpoint, which is faithful to Selenium's verifyDownload ("the Ad is downloaded").
   */
  async downloadAd(): Promise<{ url: string }> {
    const n = await this.listCheckboxes.count();
    if (n === 0) throw new Error('No rich media ads to download');
    await this.jsClick(this.listCheckboxes.nth(Math.floor(Math.random() * n)));
    const [popup] = await Promise.all([
      this.page.context().waitForEvent('page', { timeout: this.defaultWaitMs }),
      this.downloadButton.click(),
    ]);
    const url = popup.url();
    await popup.close().catch(() => {});
    return { url };
  }

  // ---- Filter / search ----
  async selectSearchType(label: string): Promise<void> {
    await this.searchType.selectOption({ label });
  }

  /** Selenium searchTextOfRichMediaAd(): type term ("RichMediaAd1") then Update. */
  async enterSearchText(text: string): Promise<void> {
    await this.searchBox.fill(text);
    await this.clickUpdate();
  }

  async verifySearchResults(text: string): Promise<void> {
    const titles = await this.resultTitles.allInnerTexts();
    for (const t of titles) expect(t, `result should contain "${text}"`).toContain(text);
  }

  async selectDeviceType(label: string): Promise<void> {
    await this.deviceType.selectOption({ label });
    await this.clickUpdate();
  }

  /** Selenium VerifyDeviceTypeSearchResultsForRichMediaAds: open a result, assert the toggle is shown. */
  async verifyDeviceSelected(device: string): Promise<void> {
    if (await this.noItemsFound.first().isVisible().catch(() => false)) {
      await this.selectPastItems();
      await this.clickUpdate();
    }
    const n = await this.resultThumbnails.count();
    if (n === 0) {
      console.log(`[RM device filter] no ${device} rich media ads — skipping (Selenium parity)`);
      return;
    }
    await this.jsClick(this.resultThumbnails.nth(Math.floor(Math.random() * n)));
    const toggle = device === 'Desktop' ? this.desktopToggle : this.tabletToggle;
    await expect(toggle, `${device} toggle should be present`).toBeVisible({ timeout: this.defaultWaitMs });
  }

  async selectAdType(label: string): Promise<void> {
    await this.adTypesFilter.selectOption({ label });
    await this.clickUpdate();
  }

  /** Selenium VerifyAdTypeSearchResultsForRichMediaAds: open a result, assert its ad type. */
  async verifyAdType(adType: string): Promise<void> {
    if (await this.noItemsFound.first().isVisible().catch(() => false)) {
      await this.selectPastItems();
      await this.clickUpdate();
    }
    const n = await this.resultThumbnails.count();
    if (n === 0) {
      console.log(`[RM adType filter] no ${adType} rich media ads — skipping (Selenium parity)`);
      return;
    }
    await this.jsClick(this.resultThumbnails.nth(Math.floor(Math.random() * n)));
    await expect(this.adTypeSelect, `ad type should be ${adType}`).toHaveValue(/.+/, {
      timeout: this.defaultWaitMs,
    });
    const selected = await this.adTypeSelect
      .evaluate((s) => (s as HTMLSelectElement).selectedOptions[0]?.text ?? '')
      .catch(() => '');
    expect(selected, `selected ad type should be "${adType}"`).toContain(adType);
  }

  // ---- Update ----
  // Selenium: a generic jQuery-UI dialog button (exact class) → CSS class chain (same element).
  readonly popupButton = this.page.locator(
    'button.ui-button.ui-widget.ui-state-default.ui-corner-all.ui-button-text-icon-primary',
  );

  /** Selenium selectRichMediaAd(): open a random rich media ad from the list. */
  async openRandomAd(): Promise<void> {
    if (await this.noItemsFound.first().isVisible().catch(() => false)) {
      await this.selectPastItems();
      await this.clickUpdate();
    }
    const n = await this.resultThumbnails.count();
    expect(n, 'there should be a rich media ad to open').toBeGreaterThan(0);
    await this.jsClick(this.resultThumbnails.nth(Math.floor(Math.random() * n)));
  }

  /** Selenium richMediAdUpdate("title"): clear + set a random Title. Returns the value. */
  async updateTitle(): Promise<string> {
    const value = `RM${Math.floor(Math.random() * 1e6)}`;
    await this.title.clear();
    await this.jsClick(this.popupButton.first()).catch(() => {});
    await this.title.fill(value);
    return value;
  }

  /** Selenium verifyRichMediaAdUpdate("title"): save & close, reopen, assert the Title value. */
  async verifyTitleUpdated(value: string): Promise<void> {
    await this.saveAndClose();
    const tile = this.page.getByText(value, { exact: true }).first();
    if (await tile.count()) await this.jsClick(tile);
    await expect(this.title, `Rich Media title should be "${value}"`).toHaveValue(value, {
      timeout: this.defaultWaitMs,
    });
  }

  /** Selenium DeleteOfAds(): tick a random ad, Delete, confirm OK. */
  async deleteAd(): Promise<void> {
    const n = await this.listCheckboxes.count();
    if (n === 0) throw new Error('No rich media ads to delete');
    await this.jsClick(this.listCheckboxes.nth(Math.floor(Math.random() * n)));
    await this.deleteButton.click();
    await this.okButton.first().click();
  }

  async clickUpdate(): Promise<void> {
    await this.expectVisible(this.updateButton, 'Rich Media Update button should be visible');
    await this.updateButton.click();
    await this.waitForListSettled();
  }

  async clickNew(): Promise<void> {
    await this.expectVisible(this.newButton, 'Rich Media "New" button should be visible');
    await this.newButton.click();
  }

  /** Selenium adCreationPage(): the "Rich Media ID" label is shown. */
  async expectCreationPage(): Promise<void> {
    await expect(this.richMediaAdId, 'Rich Media creation page should show "Rich Media ID"').toHaveText(
      /Rich Media ID/i,
      { timeout: this.defaultWaitMs },
    );
  }

  async selectStatusPublished(): Promise<void> {
    await this.statusDropdown.selectOption({ label: 'Published' });
  }

  /** Selenium title(t): fill AdExtrefID then Title; slug(s): fill Slug. */
  async enterTitleSlug(): Promise<void> {
    await this.adExtRefIdInput.fill('123456');
    await this.title.fill('title');
    await this.slug.fill('slug');
  }

  /** Selenium adTypeInRichMedia("Lexigo"). */
  async enterAdType(): Promise<void> {
    await this.adTypeSelect.selectOption({ label: 'Lexigo' });
  }

  /** Selenium clickCheckboxesInRichMedia(): click tablet + desktop checkboxes. */
  async selectAllCheckboxes(): Promise<void> {
    await this.jsClick(this.tabletCheckbox);
    await this.jsClick(this.desktopToggle);
  }

  /**
   * Selenium addImageOnRichMediaAds: click Add (opens a "Media Manager" dialog) ->
   * upload into the dialog's #mediadata -> click the dialog's Save.
   */
  async uploadImage(key: string): Promise<void> {
    const image = String(getRichMediaAdData(key).Image);
    await this.addButton.first().click();
    const dialog = this.page.getByRole('dialog', { name: /Media Manager/i });
    await expect(dialog, 'Media Manager dialog should open').toBeVisible({ timeout: this.defaultWaitMs });
    await this.imageField.first().setInputFiles(resolveAsset(image));
    await this.page.evaluate(() => window.scrollTo(0, document.body.scrollHeight));
    // The dialog's primary Save (not the editor's top Save). Prefer the dialog-scoped one.
    const dialogSave = dialog.getByRole('button', { name: 'Save', exact: true });
    if (await dialogSave.count()) {
      await this.jsClick(dialogSave.first());
    } else {
      const n = await this.imageSaveButton.count();
      for (let i = 0; i < n; i++) {
        if (await this.imageSaveButton.nth(i).isVisible()) {
          await this.jsClick(this.imageSaveButton.nth(i));
          break;
        }
      }
    }
    // Wait for the dialog to close (upload committed).
    await dialog.waitFor({ state: 'hidden', timeout: this.defaultWaitMs }).catch(() => {});
  }

  /** Selenium verifyImageisUploaded(): a preview image is present after upload. */
  async expectImageUploaded(): Promise<void> {
    const preview = this.page.locator(
      '.cell.hundredpercent img, #lib-preview-area img',
    );
    await expect
      .poll(async () => preview.count(), {
        timeout: this.defaultWaitMs,
        message: 'a Rich Media preview image should be present',
      })
      .toBeGreaterThan(0);
  }

  async uploadZip(key: string): Promise<void> {
    const zip = String(getRichMediaAdData(key).ZipFile);
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

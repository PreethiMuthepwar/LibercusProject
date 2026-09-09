import { type Page, expect } from '@playwright/test';
import { CmsBasePage } from './cms-base.page';
import { resolveAsset } from '../../utils/data.utils';

/**
 * CMS Print Ads page.
 * Ported from Selenium pages/Libercus/CMS/PrintAds.java (create flow).
 */
export class PrintAdsPage extends CmsBasePage {
  // Refactored to Playwright-standard locators (same elements; click semantics unchanged).
  readonly channelDropdown = this.page.locator("#ads-main select[name='channel']");
  // Selenium targets the panel button's <span> text; panel-scoped getByRole activates the same button.
  readonly newButton = this.page.locator('#adsPanel').getByRole('button', { name: 'New', exact: true });
  readonly statusDropdown = this.page.locator("select[name='Status']");
  readonly adNameInput = this.page.locator("input[name='AdName']");
  readonly pdfFile = this.page.locator("input[name='mediadata']");
  readonly previewImg = this.page.locator('#lib-preview-area img');
  readonly uploadingLabel = this.page.getByText('Uploading File(s)', { exact: true });
  readonly saveAndCloseButton = this.page
    .locator('#adsPanelEdit')
    .getByRole('button', { name: 'Save & Close', exact: true });

  constructor(page: Page) {
    super(page);
  }

  async clickNew(): Promise<void> {
    await this.expectVisible(this.newButton, 'Print Ads "New" button should be visible');
    await this.jsClick(this.newButton);
  }

  async selectStatusPublished(): Promise<void> {
    await this.statusDropdown.selectOption({ label: 'Published' });
  }

  async enterAdName(name = 'PrintAds'): Promise<void> {
    await this.adNameInput.fill(name);
  }

  /** Selenium addPdfFile(): upload data/649995.pdf, wait for the loader to clear. */
  async uploadPdf(): Promise<void> {
    await this.pdfFile.setInputFiles(resolveAsset('649995.pdf'));
  }

  /** Selenium verifyPdfIsUploaded(): "Uploading File(s)" gone, file set, preview present. */
  async expectPdfUploaded(): Promise<void> {
    if (await this.uploadingLabel.count()) {
      await this.uploadingLabel.first().waitFor({ state: 'hidden', timeout: this.defaultWaitMs });
    }
    await expect
      .poll(async () => (await this.pdfFile.inputValue()).length, {
        timeout: this.defaultWaitMs,
        message: 'PDF file should be selected',
      })
      .toBeGreaterThan(0);
    await expect(this.previewImg, 'PDF preview image should be present').toHaveAttribute(
      'src',
      /\/admin\/imgpreview\//,
      { timeout: this.defaultWaitMs },
    );
  }

  async saveAndClose(): Promise<void> {
    await expect(this.saveAndCloseButton.first(), 'Save & Close should be attached').toBeAttached({
      timeout: this.defaultWaitMs,
    });
    await this.jsClick(this.saveAndCloseButton.first());
  }
}

import { type Page, expect } from '@playwright/test';
import { CmsBasePage } from './cms-base.page';
import { getImageData, resolveAsset } from '../../utils/data.utils';
import { randomString } from '../../utils/random.utils';

/**
 * CMS Media Files page.
 * Ported from Selenium pages/Libercus/CMS/MediaFiles.java (create flow).
 */
export class MediaFilesPage extends CmsBasePage {
  // Refactored to Playwright-standard locators (same elements; click semantics unchanged).
  // Panel-scoped getByRole('button') activates the SAME panel button Selenium's span-click did.
  readonly newButton = this.page.locator('#mediafilesPanel').getByRole('button', { name: 'New', exact: true });
  readonly title = this.page.locator("input[name='Title']");
  readonly caption = this.page.locator("textarea[name='Caption']");
  readonly uploadElement = this.page.locator('input#mediadata');
  readonly imageSrc = this.page.locator('#lib-preview-area img');
  // Selenium class lib-button-saveclose (jsClick .first() in the method).
  readonly saveAndCloseButton = this.page.locator("//button[@class='lib-button-saveclose ui-button ui-widget ui-state-default ui-corner-all ui-button-text-icon-primary btnFixed']//span[@class='ui-button-text'][text()='Save & Close']");
  readonly updateButton = this.page.locator('#mediafilesPanel').getByRole('button', { name: 'Update', exact: true });
  readonly listOfTitles = this.page.locator('.libListContentPanelTitle');
  // Filter/search. Selenium: selectSearchType (mediaSearch), searchTextBox, shadowHost.
  readonly searchType = this.page.locator("select[name='mediaSearch']");
  readonly searchBox = this.page.locator("input[name='mediaSearch_search']");
  // Selenium: //div[@id='mediafiles-main']//div[@class='libListContent']/div//div[@class='libListContentPanelTitle']
  readonly resultTitles = this.page.locator(
    '#mediafiles-main .libListContent .libListContentPanelTitle',
  );

  private createdTitle = '';

  constructor(page: Page) {
    super(page);
  }

  async clickNew(): Promise<void> {
    await this.expectVisible(this.newButton, 'Media Files "New" button should be visible');
    await this.newButton.click();
  }

  /** Selenium: enter publish date + Title (+ random suffix) + Caption. */
  async enterTitleCaption(): Promise<void> {
    const baseTitle = String(getImageData('Image1').Title);
    const caption = String(getImageData('Image1').Caption);
    this.createdTitle = `${baseTitle} ${randomString(4)}`;
    await this.title.fill(this.createdTitle);
    await this.caption.fill(caption);
  }

  /** Selenium uploadImage(): set the media file from Image1.Image. */
  async uploadImage(): Promise<void> {
    const image = String(getImageData('Image1').Image);
    await this.uploadElement.setInputFiles(resolveAsset(image));
  }

  async expectImageUploaded(): Promise<void> {
    await expect(this.imageSrc, 'uploaded media preview should be present').toHaveAttribute(
      'src',
      /\/admin\/imgpreview\//,
      { timeout: this.defaultWaitMs },
    );
  }

  // async saveAndClose(): Promise<void> {
  //   await expect(this.saveAndCloseButton.first(), 'Save & Close should be attached').toBeAttached({
  //     timeout: this.defaultWaitMs,
  //   });
  //   await this.jsClick(this.saveAndCloseButton.first());
  // }

  async saveAndClose(): Promise<void> {
  await expect(this.saveAndCloseButton.first()).toBeVisible({
    timeout: this.defaultWaitMs,
  });

  await this.saveAndCloseButton.first().click();

  await this.waitForListSettled();
}

  /** Refresh the media list and wait for it to render. */
  async update(): Promise<void> {
    await this.updateButton.click();
    await this.waitForListSettled();
  }

  // ---- Filter / search ----
  async selectSearchType(label: string): Promise<void> {
    await this.searchType.selectOption({ label });
  }

  /** Selenium searchTextOfMediaFiles(): type term ("Bills") then Update. */
  async enterSearchText(text: string): Promise<void> {
    await this.searchBox.fill(text);
    await this.updateButton.click();
    await this.waitForListSettled();
  }

  async verifySearchResults(text: string): Promise<void> {
    const titles = await this.resultTitles.allInnerTexts();
    for (const t of titles) expect(t, `result should contain "${text}"`).toContain(text);
  }

  // ---- Update ----
  readonly thumbnails = this.page.locator('.libLine.libListContentPanel.libThumbnailPanel');
  readonly popupButton = this.page.locator(
    'button.ui-button.ui-widget.ui-state-default.ui-corner-all.ui-button-text-icon-primary',
  );

  /** Selenium selectMediaFile(): open a random media file from the list. */
  async openRandomMediaFile(): Promise<void> {
    const n = await this.thumbnails.count();
    expect(n, 'there should be a media file to open').toBeGreaterThan(0);
    await this.jsClick(this.thumbnails.nth(Math.floor(Math.random() * n)));
  }

  /** Selenium mediaFileUpdate("title"): clear + set a random Title. Returns the value. */
  async updateTitle(): Promise<string> {
    const value = `MF${Math.floor(Math.random() * 1e6)}`;
    await this.title.clear();
    await this.jsClick(this.popupButton.first()).catch(() => {});
    await this.title.fill(value);
    return value;
  }

  /** Selenium verifyMediadUpdate("title"): reopen the first media file, assert the Title. */
  async verifyTitleUpdated(value: string): Promise<void> {
    if (await this.thumbnails.count()) await this.jsClick(this.thumbnails.first());
    await expect(this.title, `Media file title should be "${value}"`).toHaveValue(value, {
      timeout: this.defaultWaitMs,
    });
  }

  /** Selenium verifyMediaFileisCreated(title): refresh list, find the created title. */
  async verifyCreated(): Promise<void> {
    // Selenium re-filters then searches for the created title. The file's publish date is in the
    // past, so a plain refresh won't list it — search by the title (filters across dates), like
    // searchTextOfMediaFiles, then assert it appears (eachTitle.equalsIgnoreCase(mediafileTitle)).
    await this.enterSearchText(this.createdTitle).catch(() => {});
    await expect
      .poll(
        async () => {
          const titles = await this.listOfTitles.allInnerTexts();
          return titles.some((t) => t.trim().toLowerCase().includes(this.createdTitle.toLowerCase()));
        },
        { timeout: this.defaultWaitMs, message: `created media file "${this.createdTitle}" should appear in the list` },
      )
      .toBe(true);
  }
}

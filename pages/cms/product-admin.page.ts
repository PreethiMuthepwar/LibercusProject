import { type Page, expect } from '@playwright/test';
import { CmsBasePage } from './cms-base.page';
import { getUserData, getTagData } from '../../utils/data.utils';
import { randomString } from '../../utils/random.utils';

/**
 * CMS Product Administration module: Users and Tags.
 * Ported from Selenium pages/Libercus/CMS/{Users,Tags}.java + RegressionSteps.java.
 */
export class ProductAdminPage extends CmsBasePage {
  readonly saveAndCloseButton = this.page.locator('button.lib-button-saveclose');

  constructor(page: Page) {
    super(page);
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

  // ---- Users ----------------------------------------------------------------
  // Selenium @FindBy provenance kept inline.
  readonly userNewButton = this.page.locator('#siteusersPanel').getByRole('button', { name: 'New', exact: true });
  readonly userCreationTab = this.page.getByRole('link', { name: 'Returned E-mails', exact: true });
  readonly loginNameInput = this.page.locator("input[name='LoginName']");
  readonly userNameInput = this.page.locator("input[name='UserName']");
  readonly passwordInput = this.page.locator("input[name='Password']");
  readonly repeatPasswordInput = this.page.locator("input[name='Password2']");
  readonly emailInput = this.page.locator("input[name='EMailAddress']");
  readonly usersNavBar = this.page.locator('#siteusers-main .libNavBarTextArea');
  readonly usersUpdateButton = this.page.locator('#siteusers-main').getByRole('button', { name: 'Update', exact: true });
  // Login form (for re-login as the created user).
  readonly loginUsername = this.page.getByLabel('Username');
  readonly loginPassword = this.page.getByLabel('Password');
  readonly loginSubmit = this.page.getByRole('button', { name: 'Login' });

  private createdLoginName = '';
  private createdPassword = '';
  private beforeUserCount = 0;

  async clickUserNew(): Promise<void> {
    await this.expectVisible(this.userNewButton, 'Users "New" button should be visible');
    await this.jsClick(this.userNewButton);
  }

  async userCreationDisplayed(): Promise<void> {
    await this.expectAnyVisible(this.userCreationTab, 'User creation "Returned E-mails" tab should show');
  }

  /** Load the users list (Update + settle) so the count is accurate. */
  private async loadUsers(): Promise<void> {
    await this.jsClick(this.usersUpdateButton);
    await this.waitForListSettled().catch(() => {});
  }

  /** Selenium iShouldNoteTheCountOfTheUsers: beforeCount = numberOfRecords(countOfUsers). */
  async noteUserCount(): Promise<void> {
    await this.loadUsers();
    this.beforeUserCount = await this.recordCount(this.usersNavBar);
  }

  /** Selenium enterLoginName/UserName/Password/EmailAddress (login name gets a random suffix). */
  async fillNewUser(): Promise<void> {
    const d = getUserData();
    this.createdLoginName = `${String(d.LoginName)}${randomString(3)}`;
    this.createdPassword = String(d.password);
    await this.fillVisible(this.loginNameInput, this.createdLoginName);
    await this.fillVisible(this.userNameInput, String(d.UserName));
    await this.fillVisible(this.passwordInput, this.createdPassword);
    await this.fillVisible(this.repeatPasswordInput, String(d.RepeatPassword));
    await this.fillVisible(this.emailInput, String(d.EmailAddress));
  }

  /** Selenium verifyUserCreation: afterCount == beforeCount + 1. */
  async verifyUserCreated(): Promise<void> {
    await this.loadUsers();
    await expect
      .poll(async () => this.recordCount(this.usersNavBar), {
        timeout: this.defaultWaitMs,
        message: `user count should reach ${this.beforeUserCount + 1}`,
      })
      .toBe(this.beforeUserCount + 1);
  }

  /**
   * Selenium userCredentials(): on the login page, enter the created user's
   * loginName/password and click Login. The feature's "see Home" step is commented
   * out, so we don't assert the dashboard — only that the form was submitted (we
   * leave the login page or the dashboard appears).
   */
  async loginAsCreatedUser(): Promise<void> {
    await this.expectVisible(this.loginUsername, 'login Username field should be visible');
    await this.loginUsername.fill(this.createdLoginName);
    await this.loginPassword.fill(this.createdPassword);
    await this.loginSubmit.click();
    // Faithful to Selenium (which has no Home assertion here): wait for navigation
    // away from the login form OR the dashboard, tolerating either outcome.
    await Promise.race([
      this.page.getByRole('tab', { name: 'Home' }).waitFor({ state: 'visible', timeout: 60_000 }),
      this.loginUsername.waitFor({ state: 'hidden', timeout: 60_000 }),
    ]).catch(() => {});
  }

  // ---- Tags -----------------------------------------------------------------
  readonly tagNewButton = this.page.locator('#tagsPanel').getByRole('button', { name: 'New', exact: true });
  readonly tagEnabledLabel = this.page.locator("label[for='Active']");
  readonly tagDisplayName = this.page.locator("input[name='DisplayVersion']");
  readonly tagShortName = this.page.locator("input[name='ShortVersion']");
  readonly tagSearchBox = this.page.locator("input[name='Tag']");
  readonly tagUpdateButton = this.page.locator('#tags-main').getByRole('button', { name: 'Update', exact: true });
  readonly tagsNavBar = this.page.locator('#tags-main .libNavBarTextArea');
  readonly tagRows = this.page.locator('#tagsPanel .libListContentRow.libLine');

  private beforeTagCount = 0;

  async clickTagNew(): Promise<void> {
    await this.expectVisible(this.tagNewButton, 'Tags "New" button should be visible');
    await this.jsClick(this.tagNewButton);
  }

  async tagCreationDisplayed(): Promise<void> {
    await this.expectAnyVisible(this.tagEnabledLabel, 'Tag creation "Enabled" label should show');
  }

  private async loadTags(): Promise<void> {
    await this.jsClick(this.tagUpdateButton);
    await this.waitForListSettled().catch(() => {});
  }

  async noteTagCount(): Promise<void> {
    await this.loadTags();
    this.beforeTagCount = await this.recordCount(this.tagsNavBar);
  }

  async fillNewTag(): Promise<void> {
    const d = getTagData();
    await this.fillVisible(this.tagDisplayName, String(d.TagDisplayName));
    await this.fillVisible(this.tagShortName, String(d.TagShortName));
  }

  /** Selenium verifyTagCreation: afterCount == beforeCount + 1. */
  async verifyTagCreated(): Promise<void> {
    await this.loadTags();
    await expect
      .poll(async () => this.recordCount(this.tagsNavBar), {
        timeout: this.defaultWaitMs,
        message: `tag count should reach ${this.beforeTagCount + 1}`,
      })
      .toBe(this.beforeTagCount + 1);
  }

  /** Selenium enterTextInSearchBox: type display name then click Update. */
  async searchTags(): Promise<void> {
    const d = getTagData();
    await this.tagSearchBox.fill(String(d.TagDisplayName));
    await this.jsClick(this.tagUpdateButton);
    await this.waitForListSettled();
  }

  /** Selenium verifySearchResultsOfTags: each result row contains the display name. */
  async verifyTagSearchResults(): Promise<void> {
    const term = String(getTagData().TagDisplayName);
    const total = await this.tagRows.count();
    for (let i = 0; i < total; i++) {
      const txt = await this.tagRows.nth(i).innerText();
      expect(txt, `tag result row ${i + 1} should contain "${term}"`).toContain(term);
    }
  }
}

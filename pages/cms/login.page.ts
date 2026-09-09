import { type Page, expect } from '@playwright/test';
import { BasePage } from '../base.page';
import { CMS_URL, credentials } from '../../utils/env';

/**
 * CMS Login page.
 * Ported from Selenium pages/Libercus/CMS/Login.java.
 *
 * Selenium validLogin(): sendKeys(username) -> sendKeys(password) -> click(login)
 * then waited up to 240s for the banner "Print shapes and styles loading complete!".
 */
export class LoginPage extends BasePage {
  // Playwright-recommended: the login form is a labelled dialog (verified via the live
  // a11y tree). Selenium provenance: @FindBy(name="username"|"password"|"login").
  readonly usernameInput = this.page.getByLabel('Username');
  readonly passwordInput = this.page.getByLabel('Password');
  readonly loginButton = this.page.getByRole('button', { name: 'Login' });
  // Selenium login() waited on this readiness banner. In practice it is a
  // transient toast that fades quickly, so we treat it as a best-effort signal.
  readonly readyBanner = this.page.getByText('Print shapes and styles loading complete!', {
    exact: false,
  });
  // Stable post-login signal: the dashboard "Home" content tab (Selenium used
  // @FindBy(xpath="//a[text()='Home']"); the live a11y tree exposes it as a tab).
  readonly homeLink = this.page.getByRole('tab', { name: 'Home' });
  // Shown by the app when credentials are rejected.
  readonly loginFailed = this.page.getByText('Login failed', { exact: false });

  constructor(page: Page) {
    super(page);
  }

  /** Navigate to the CMS login URL. Selenium: visit("CMS Login"). */
  async goto(): Promise<void> {
    await this.page.goto(CMS_URL(), { waitUntil: 'domcontentloaded', timeout: 120_000 });
  }

  /**
   * Full UI login. Mirrors Login.validLogin(). Defaults to env credentials
   * (Selenium read these from data/LoginDetails.yml).
   */
  async login(user?: string, pass?: string): Promise<void> {
    const c = user && pass ? { username: user, password: pass } : credentials();
    await this.usernameInput.fill(c.username);
    await this.passwordInput.fill(c.password);
    await this.loginButton.click();
    await this.waitForReady();
  }

  /**
   * Wait until the CMS dashboard is ready after login. Selenium waited (≤240s)
   * for the transient "Print shapes and styles loading complete!" toast; we wait
   * for the stable Home link instead, while failing fast on "Login failed.".
   */
  async waitForReady(timeoutMs = 240_000): Promise<void> {
    const outcome = await Promise.race([
      this.homeLink.waitFor({ state: 'visible', timeout: timeoutMs }).then(() => 'ready' as const),
      this.loginFailed
        .waitFor({ state: 'visible', timeout: timeoutMs })
        .then(() => 'failed' as const),
    ]);
    if (outcome === 'failed') {
      throw new Error('CMS login failed — credentials rejected for this environment (PROJECT_ENV).');
    }
  }

  /**
   * Navigate to the CMS and ensure we are authenticated. Fast path: if storageState
   * already restored the session, the Home link is present and we return. Otherwise
   * perform a full UI login (faithful to each Selenium scenario's Given/When).
   */
  async ensureLoggedIn(): Promise<void> {
    await this.goto();
    const outcome = await Promise.race([
      this.homeLink.waitFor({ state: 'visible', timeout: 30_000 }).then(() => 'in' as const),
      this.usernameInput.waitFor({ state: 'visible', timeout: 30_000 }).then(() => 'login' as const),
    ]).catch(() => 'login' as const);
    if (outcome === 'login') {
      await this.login();
    }
  }

  /** Assert we are on (or back on) the login page. Selenium onPage("CMS Login"). */
  async expectOnLoginPage(): Promise<void> {
    await expect(this.usernameInput, 'Username field should be visible on the login page').toBeVisible({
      timeout: this.defaultWaitMs,
    });
  }
}

import { type Page, expect } from '@playwright/test';
import { BasePage } from '../base.page';
import { CMS_URL } from '../../utils/env';

/** Escape a string for safe use inside a RegExp. */
function escapeRegExp(s: string): string {
  return s.replace(/[.*+?^${}()|[\]\\]/g, '\\$&');
}

/**
 * CMS Home page (dashboard + top navigation).
 * Ported from Selenium pages/Libercus/CMS/HomePage.java.
 */
export class HomePage extends BasePage {
  // Playwright-recommended (verified via the live a11y tree). Selenium provenance:
  //   homeText  @FindBy("//a[text()='Home']")        -> dashboard "Home" content tab
  //   logout    @FindBy("//a[@id='logout']")          -> link "Log Out"
  //   content   @FindBy("//h3/a[text()='Content']")   -> link "Content"
  readonly homeText = this.page.getByRole('tab', { name: 'Home' });
  readonly logout = this.page.getByRole('link', { name: 'Log Out' });
  readonly content = this.page.getByRole('link', { name: 'Content', exact: true });

  constructor(page: Page) {
    super(page);
  }

  /** Navigate directly to the CMS home (used when reusing an authed session). */
  async goto(): Promise<void> {
    await this.page.goto(CMS_URL(), { waitUntil: 'domcontentloaded', timeout: 120_000 });
  }

  /** Selenium clickOnMainMenu(menuName): the main-menu items are links. */
  async clickMainMenu(menuName: string): Promise<void> {
    const menu = this.page.getByRole('link', { name: menuName, exact: true });
    await this.expectVisible(menu, `Main menu "${menuName}" should be visible`);
    await menu.click();
  }

  /**
   * Selenium clickSubMenu(subMenuName): //div[text()='subMenuName']. The Content
   * submenus (Stories/Print Pages/…) render as links, but the Print/Product/Congero/
   * Feeds submenus render as `div.menuItem` text nodes. Try the link role first
   * (validated for smoke), then fall back to the exact-text element Selenium used.
   */
  async clickSubMenu(subMenuName: string): Promise<void> {
    const link = this.page.getByRole('link', { name: subMenuName, exact: true }).first();
    if (await link.isVisible().catch(() => false)) {
      await link.click();
      return;
    }
    // Selenium: //div[text()='subMenuName'] → the `div.menuItem` text node.
    // CSS + exact-text filter (recommended over XPath).
    const div = this.page
      .locator('div.menuItem')
      .filter({ hasText: new RegExp(`^\\s*${escapeRegExp(subMenuName)}\\s*$`) })
      .first();
    await this.expectVisible(div, `Sub menu "${subMenuName}" should be visible`);
    await this.jsClick(div);
  }

  /**
   * Selenium step: "I navigate to {subMenu} from {mainMenu} on {page}"
   * -> clickOnMainMenu(mainMenu); clickSubMenu(subMenu).
   */
  async navigate(subMenu: string, mainMenu: string): Promise<void> {
    await this.clickMainMenu(mainMenu);
    await this.clickSubMenu(subMenu);
  }

  /** Selenium VerifyHomeMessage(): on the dashboard, the "Home" content tab is shown. */
  async verifyHomeText(): Promise<void> {
    await expect(this.homeText, 'CMS "Home" tab should be visible').toBeVisible({
      timeout: this.defaultWaitMs,
    });
  }

  /** Selenium: I click on "Logout" element. */
  async clickLogout(): Promise<void> {
    await this.expectVisible(this.logout, 'Log Out link should be visible');
    await this.logout.click();
  }
}

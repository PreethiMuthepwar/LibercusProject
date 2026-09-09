import { type Page, expect } from '@playwright/test';
import { CmsBasePage } from './cms-base.page';

/**
 * CMS Production View (Rich Media Ad edit mode).
 * Ported from Selenium pages/Libercus/CMS/ProductionView.java + the ProductionView smoke
 * feature. Uses the same list date-filter (`calender`) path as the rest of the suite —
 * the app/env `#help`-overlay issue gates it in BOTH suites on TB.
 */
export class ProductionViewPage extends CmsBasePage {
  // Selenium @FindBy provenance inline.
  readonly tabletView = this.page.getByRole('link', { name: 'Tablet View', exact: true });
  // Selenium update = (//button[@name='refresh'])[13] — the Production View refresh; pick the visible one.
  readonly refreshButtons = this.page.locator("button[name='refresh']");
  readonly richAdPanel = this.page.locator("fieldset[name='ProdViewAds']");
  readonly draggableAds = this.page.locator("div[id*='libDraggableAd_']");

  constructor(page: Page) {
    super(page);
  }

  /** Selenium "I click on tabletView element". */
  async clickTabletView(): Promise<void> {
    await this.expectVisible(this.tabletView.first(), 'Tablet View tab should be visible');
    await this.jsClick(this.tabletView.first());
  }

  /** Selenium "I click on update element": click the visible Production View refresh button. */
  async clickUpdate(): Promise<void> {
    const btn = (await this.firstVisible(this.refreshButtons)) ?? this.refreshButtons.last();
    await this.jsClick(btn);
    await this.page.waitForTimeout(2_000);
  }

  /** Selenium selectRichMediaEditMode("On"): click the edit-mode "On" toggle. */
  async turnOnAdEditMode(): Promise<void> {
    await this.jsClick(this.page.getByText('On', { exact: true }).first());
    await this.page.waitForTimeout(1_000);
  }

  /** Selenium richAdPanel() + adsAreDisplayed(): the Rich Ad panel is displayed. */
  async expectRichAdPanel(): Promise<void> {
    await expect(this.richAdPanel.first(), 'the Rich Ad (ProdViewAds) panel should be displayed').toBeVisible({
      timeout: this.defaultWaitMs,
    });
  }
}

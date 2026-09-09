import { test, expect } from '../../fixtures';

/**
 * Ported from PF3 SmokeTesting/PF3/Ads_Puzzles Functionality.feature:
 * the four Ads/Puzzles submenu entries open their respective panels.
 */
test.describe('@pf3 @ads PF3 Ads/Puzzles', () => {
  test.beforeEach(async ({ pf3Home }) => {
    test.slow();
    await pf3Home.goto();
  });

  test('Print Ads/Puzzles panel opens', async ({ pf3Home }) => {
    await pf3Home.openAdsSubmenu(pf3Home.printAdsOrPuzzlesButton);
    await pf3Home.expectPanelHeader('Download Ads, Puzzles and Crosswords to Print');
  });

  test('Interactive Ads panel opens', async ({ pf3Home }) => {
    await pf3Home.openAdsSubmenu(pf3Home.interactiveAdsButton);
    await pf3Home.expectPanelHeader('Interactive Ads');
    await pf3Home.openFirstInteractiveAd();
  });

  test('Interactive Puzzles panel opens with puzzle tiles', async ({ pf3Home }) => {
    await pf3Home.openAdsSubmenu(pf3Home.interactivePuzzlesButton);
    await pf3Home.expectPanelHeader('Interactive Puzzles');
    await pf3Home.checkInteractivePuzzles();
  });

  test('Inserts opens the inserts provider in a new tab', async ({ pf3Home, page }) => {
    // Selenium checkInsertsFunctionality(): lands on a new tab at flipp.com/weekly_ads.
    await pf3Home.openMenuItem(pf3Home.ads);
    await expect(pf3Home.insertsButton, 'Inserts submenu item should be visible').toBeVisible({ timeout: 90_000 });
    const [popup] = await Promise.all([
      page.context().waitForEvent('page', { timeout: 90_000 }),
      pf3Home.insertsButton.click(),
    ]);
    await popup.waitForLoadState('domcontentloaded').catch(() => {});
    expect(popup.url(), 'Inserts should open the flipp.com inserts page').toContain('flipp.com');
    await popup.close().catch(() => {});
  });
});

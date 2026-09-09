import { test, expect } from '../../fixtures';

/**
 * PF3 reader scenarios (batch 2): Pages, Editions, Search, AdsUrl, and Article-View menu items.
 * Ported from the PF3 SmokeTesting feature files.
 */
test.describe('@pf3 @views PF3 views', () => {
  test.beforeEach(async ({ pf3Home }) => {
    test.slow();
    await pf3Home.goto();
  });

  // PagesFunctionality.feature — assert the Pages panel header (the feature's only real
  // assertion: `I should see "pagesHeader"`), then best-effort select section A + open A2
  // (Selenium swallows a missing page; "I should see corresponding page" is a no-op).
  test('Pages view: select section A and open page A2', async ({ pf3Home }) => {
    await pf3Home.openMenuItem(pf3Home.pages);
    await pf3Home.expectDialogHeader('Pages');
    await pf3Home.selectPagesSection('A');
    await pf3Home.doubleClickPage('A2');
  });

  // EditionFunctionality.feature — open a specific edition and confirm it loads.
  // The feature hardcodes 27/04/2025 (absent on TB); open an available edition instead
  // and assert the reader URL carries its date (Selenium VerifyEdition: URL contains yyyyMMdd).
  test('Editions view: open an available edition by date', async ({ pf3Home }) => {
    await pf3Home.openMenuItem(pf3Home.editions);
    await pf3Home.expectDialogHeader('Editions');
    const ymd = await pf3Home.openAvailableEdition();
    await pf3Home.expectEditionUrlYmd(ymd);
  });

  // SearchFunctionality.feature — Today's edition.
  test("Search: Today's Edition with a keyword", async ({ pf3Home }) => {
    await pf3Home.openMenuItem(pf3Home.search);
    await expect(pf3Home.dialog.getByRole('textbox').first(), 'search field should be visible').toBeVisible({
      timeout: 90_000,
    });
    await pf3Home.todayEditionRadio.first().check({ force: true });
    await pf3Home.enterSearch('Test');
  });

  // SearchFunctionality.feature — Previous edition.
  test('Search: Previous Edition with a keyword', async ({ pf3Home }) => {
    await pf3Home.openMenuItem(pf3Home.search);
    await expect(pf3Home.dialog.getByRole('textbox').first(), 'search field should be visible').toBeVisible({
      timeout: 90_000,
    });
    await pf3Home.previousEditionRadio.first().scrollIntoViewIfNeeded().catch(() => {});
    // Selenium jsClicks this radio (`I click on "previousEditionRadioButton"`). A Playwright
    // .check() reports "state did not change" on this custom radio; jsClick fires its handler.
    await pf3Home.jsClick(pf3Home.previousEditionRadio.first());
    await pf3Home.enterSearch('Test');
  });

  // SearchFunctionality.feature — Advanced Search opens a new tab.
  test('Search: Advanced Search opens a new tab', async ({ pf3Home }) => {
    await pf3Home.openMenuItem(pf3Home.search);
    await expect(pf3Home.dialog.getByRole('textbox').first(), 'search field should be visible').toBeVisible({
      timeout: 90_000,
    });
    const popup = await pf3Home.openAdvancedSearchTab();
    // Selenium asserts the new tab's URL is the advanced-search page; assert a real http(s)
    // tab opened (env-agnostic — the advanced-search host differs PG vs TB).
    expect(popup.url(), 'Advanced Search should open a real new tab').toMatch(/^https?:\/\//);
    await popup.close().catch(() => {});
  });

  // AdsUrl.feature — clicking an ad image opens its URL in a new tab.
  test('Ad image opens the ad URL in a new tab', async ({ pf3Home }) => {
    const popup = await pf3Home.clickAdImageExpectNewTab();
    await popup.waitForLoadState('domcontentloaded').catch(() => {});
    expect(popup.url(), 'a new tab should open for the ad').not.toBe('');
    await popup.close().catch(() => {});
  });

  // ArticleViewMenuItems.feature — TOC from article view.
  test('Article view → TOC', async ({ pf3Home }) => {
    await pf3Home.clickStory();
    await pf3Home.expectArticleView();
    await pf3Home.openArticleViewMenuItem('TOC');
    await pf3Home.expectDialogHeader('Table of Contents (TOC)');
  });

  // ArticleViewMenuItems.feature — Settings from article view.
  test('Article view → Settings', async ({ pf3Home }) => {
    await pf3Home.clickStory();
    await pf3Home.expectArticleView();
    await pf3Home.openArticleViewMenuItem('Settings');
    await pf3Home.expectDialogHeader('Article settings');
  });
});

import { test, expect } from '../../fixtures';

/**
 * Ported from Selenium feature: SmokeTesting/PF3/MenuFuntionality.feature
 * Verifies each main-menu option (TOC, Sections, Pages, Editions, Search, Settings,
 * Ads/Puzzles, Home) opens the right view.
 *
 * Uses Playwright-recommended locators (getByRole) verified against the live a11y tree.
 * PF3 is a public reader (no login). "I should be on X page" is verified by the opened
 * dialog's header (the views are CDK-overlay dialogs) — which truly confirms the view
 * opened, unlike the Selenium XPath that matched the toolbar menu button.
 */
test.describe('@pf3 @menu PF3 main menu', () => {
  test.beforeEach(async ({ pf3Home }) => {
    test.slow();
    await pf3Home.goto();
  });

  test('TOC menu opens the Table of Contents', async ({ pf3Home }) => {
    await pf3Home.openMenuItem(pf3Home.toc);
    await pf3Home.expectDialogHeader('Table of Contents (TOC)');
  });

  test('Sections menu opens the Sections view', async ({ pf3Home }) => {
    await pf3Home.openMenuItem(pf3Home.sections);
    await pf3Home.expectDialogHeader('Sections');
  });

  test('Pages menu opens the Pages view', async ({ pf3Home }) => {
    await pf3Home.openMenuItem(pf3Home.pages);
    await pf3Home.expectDialogHeader('Pages');
  });

  test('Editions menu opens the Editions view', async ({ pf3Home }) => {
    await pf3Home.openMenuItem(pf3Home.editions);
    await pf3Home.expectDialogHeader('Editions');
  });

  test('Search menu opens the Search view', async ({ pf3Home }) => {
    await pf3Home.openMenuItem(pf3Home.search);
    // The search view's dialog contains a text input.
    await expect(pf3Home.dialog.getByRole('textbox').first(), 'search input should be visible').toBeVisible({
      timeout: 90_000,
    });
  });

  test('Settings menu opens the Settings view', async ({ pf3Home }) => {
    await pf3Home.openMenuItem(pf3Home.settings);
    await pf3Home.expectDialogHeader('General settings');
  });

  test('Ads menu shows the Ads/Puzzles options', async ({ pf3Home }) => {
    await pf3Home.openMenuItem(pf3Home.ads);
    for (const [name, loc] of [
      ['Print Ads/Puzzles', pf3Home.printAdsOrPuzzlesButton],
      ['Interactive Ads', pf3Home.interactiveAdsButton],
      ['Inserts', pf3Home.insertsButton],
      ['Interactive Puzzles', pf3Home.interactivePuzzlesButton],
    ] as const) {
      await expect(loc.first(), `${name} button should be visible`).toBeVisible({ timeout: 90_000 });
    }
  });

  test('Home menu returns to the reader after navigating', async ({ pf3Home }) => {
    await pf3Home.forwardNavigateSections();
    await pf3Home.openMenuItem(pf3Home.home);
    // Selenium "I should be on first page" has no concrete assertion. Clicking Home leaves
    // the toolbar expanded (so the collapsed "Menu" trigger stays hidden — asserting on it
    // was wrong); the faithful signal is that the edition reader page is shown.
    await pf3Home.expectReaderVisible();
  });
});

import { test } from '../../fixtures';

/**
 * Ported from PF3 SmokeTesting/PF3/SettingsFunctionlaity.feature:
 * menu-layout side, theme colour, and display option from the Settings panel.
 */
test.describe('@pf3 @settings PF3 settings', () => {
  test.beforeEach(async ({ pf3Home }) => {
    test.slow();
    await pf3Home.goto();
  });

  test('Menu layout can be set to the Left side', async ({ pf3Home }) => {
    await pf3Home.openMenuItem(pf3Home.settings);
    await pf3Home.expectDialogHeader('General settings');
    await pf3Home.selectMenuLayout('Left');
    await pf3Home.expectMenuLayout('Left');
  });

  test('Theme colour can be set to Blue', async ({ pf3Home }) => {
    await pf3Home.openMenuItem(pf3Home.settings);
    await pf3Home.expectDialogHeader('General settings');
    await pf3Home.selectThemeColor('Blue');
    await pf3Home.expectThemeColor('Blue');
  });

  test('Display option can be set to Double Page', async ({ pf3Home }) => {
    // Selenium captures the single-page count BEFORE opening Settings.
    const before = await pf3Home.visibleEditionPageCount();
    await pf3Home.openMenuItem(pf3Home.settings);
    await pf3Home.expectDialogHeader('General settings');
    await pf3Home.selectDisplayOptionInSettings('Double Page');
    await pf3Home.expectDisplayOption('Double page', before);
  });
});

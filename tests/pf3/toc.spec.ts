import { test } from '../../fixtures';

/**
 * Ported from PF3 SmokeTesting/PF3/TOCFunctionality.feature:
 * traverse sections → pages → stories in the Table of Contents.
 */
test.describe('@pf3 @toc PF3 Table of Contents', () => {
  test.beforeEach(async ({ pf3Home }) => {
    test.slow();
    await pf3Home.goto();
    await pf3Home.openMenuItem(pf3Home.toc);
    await pf3Home.expectDialogHeader('Table of Contents (TOC)');
  });

  test('Each section lists pages that belong to it', async ({ pf3Home }) => {
    await pf3Home.navigateTocSectionsAndValidate();
  });

  test('Selecting section A lists its pages', async ({ pf3Home }) => {
    await pf3Home.selectTocSection('A');
    await pf3Home.validateTocPagesUnderSection();
  });

  test('Section A / Page A1 lists its stories', async ({ pf3Home }) => {
    await pf3Home.selectTocSection('A');
    await pf3Home.selectTocPage('A1');
    await pf3Home.validateTocStories();
  });
});

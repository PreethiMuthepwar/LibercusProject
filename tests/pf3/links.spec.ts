import { test } from '../../fixtures';

/**
 * Ported from PF3 SmokeTesting/PF3:
 *  - HyperLinksFunctionality.feature: page-body hyperlinks redirect.
 *  - JumpLinksFunctionality.feature: "SEE …, Page X" jump links navigate.
 *  - ArticleViewMenuItems.feature (2nd scenario): article Next/Previous navigation.
 */
test.describe('@pf3 @links PF3 links & article navigation', () => {
  test.beforeEach(async ({ pf3Home }) => {
    test.slow();
    await pf3Home.goto();
  });

  test('Page-body hyperlinks redirect to the referenced page', async ({ pf3Home }) => {
    await pf3Home.checkHyperlinksRedirect();
  });

  test('Jump links navigate to the referenced section/page', async ({ pf3Home }) => {
    await pf3Home.checkJumpLinksNavigate();
  });

  test('Article view Next/Previous navigation works', async ({ pf3Home }) => {
    await pf3Home.clickStory();
    await pf3Home.expectArticleView();
    await pf3Home.navigateArticleNextPrev();
  });
});

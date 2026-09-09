import { test, expect } from '../../fixtures';
import { getDates } from '../../utils/data.utils';

/**
 * Ported from Selenium feature: SmokeTesting/CMS/UpdateFunctionality.feature
 * Update Story / Interactive Ad / Rich Media / Media File / Print Page, then verify
 * the change (the Story scenario's verify is commented out in the feature → we assert
 * the editor closed). Shares the now-fixed date-filter + list-render handling.
 */
const D = () => getDates();

test.describe('@smoke @CMS @Update Update functionality', () => {
  test.beforeEach(async ({ loginPage, homePage }) => {
    test.slow();
    await loginPage.ensureLoggedIn();
    await homePage.verifyHomeText();
  });

  test('Update a Story title', async ({ homePage, storyPage }) => {
    await homePage.navigate('Stories', 'Content');
    await storyPage.selectChannel();
    await storyPage.filterByDate(String(D().dateForUpdateStories));
    await storyPage.update();
    if (await storyPage.isEmptyList()) { await storyPage.selectPastItems(); await storyPage.update(); }
    await storyPage.openFirstStory();
    await storyPage.updateTitle();
    // Story update has no verify step in Selenium; assert the editor closed.
    await expect(homePage.content, 'should return to dashboard after save & close').toBeVisible({ timeout: 120_000 });
  });

  test('Update an Interactive Ad title', async ({ homePage, interactiveAdsPage: ia }) => {
    await homePage.navigate('Interactive Ads', 'Content');
    await ia.selectChannel();
    await ia.filterByDate(String(D().fromDateForInteractiveAds));
    await ia.clickUpdate();
    await ia.openRandomAd();
    const value = await ia.updateTitle();
    await ia.saveAndClose();
    await ia.verifyTitleUpdated(value);
  });

  test('Update a Rich Media Ad title', async ({ homePage, richMediaAdsPage: rm }) => {
    await homePage.navigate('Rich Media', 'Content');
    await rm.selectChannel();
    await rm.filterByDate(String(D().fromDateForRichMediaAds));
    await rm.clickUpdate();
    await rm.openRandomAd();
    const value = await rm.updateTitle();
    await rm.verifyTitleUpdated(value); // saves & close internally
  });

  test('Update a Media File title', async ({ homePage, mediaFilesPage: mf }) => {
    await homePage.navigate('Media Files', 'Content');
    await mf.filterByDate(String(D().fromDateForCopyOfStories));
    await mf.update();
    if (await mf.isEmptyList()) { await mf.selectPastItems(); await mf.update(); }
    await mf.openRandomMediaFile();
    const value = await mf.updateTitle();
    await mf.saveAndClose();
    await mf.verifyTitleUpdated(value);
  });

  test('Update a Print Page Section Letter', async ({ homePage, printPagesPage: pp }) => {
    await homePage.navigate('Print Pages', 'Content');
    await pp.selectChannel();
    await pp.filterByDate(String(D().fromDateForPages));
    await pp.clickUpdate();
    if (await pp.isEmptyList()) { await pp.selectPastItems(); await pp.clickUpdate(); }
    await pp.openRandomPage();
    const value = await pp.updateSectionLetter('B');
    await pp.verifySectionLetterUpdated(value);
  });
});

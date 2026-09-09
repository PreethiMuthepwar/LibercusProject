import { test, expect } from '../../fixtures';
import { getDates } from '../../utils/data.utils';

/**
 * Ported from Selenium feature: SmokeTesting/CMS/DeleteFunctionality.feature
 * Delete Stories / Interactive Ads / Rich Media Ads / Print Pages — each verifies
 * the record count decreases by 1 (Selenium verifyCopyOfStories(before, after, "Delete")).
 */
const D = () => getDates();

test.describe('@smoke @CMS @Delete Delete functionality', () => {
  test.beforeEach(async ({ loginPage, homePage }) => {
    test.slow();
    await loginPage.ensureLoggedIn();
    await homePage.verifyHomeText();
  });

  test('Delete a Story', async ({ homePage, storyPage }) => {
    await homePage.navigate('Stories', 'Content');
    await storyPage.selectChannel();

    await storyPage.filterByDate(String(D().dateForStories));
    await storyPage.selectCount1000();
    await storyPage.update();
    if (await storyPage.isEmptyList()) {
      await storyPage.selectPastItems();
      await storyPage.update();
    }
    const before = await storyPage.storyCount();

    let deleted = await storyPage.deleteOneStory();
    if (!deleted) {
      // Selenium fallback: widen to past items and retry.
      await storyPage.selectPastItems();
      await storyPage.update();
      deleted = await storyPage.deleteOneStory();
    }
    expect(deleted, 'a deletable story should have been found').toBe(true);

    await storyPage.update();
    const after = await storyPage.storyCount();
    storyPage.assertCountDelta(before, after, 'Delete');
  });

  test('Delete an Interactive Ad', async ({ homePage, interactiveAdsPage: ia }) => {
    await homePage.navigate('Interactive Ads', 'Content');
    await ia.selectChannel();
    await ia.filterByDate(String(D().fromDateForInteractiveAds));
    await ia.clickUpdate();
    if ((await ia.noItemsFound.count()) && (await ia.noItemsFound.first().isVisible())) {
      await ia.selectPastItems();
      await ia.clickUpdate();
    }
    const before = await ia.adCount();
    await ia.deleteAd();
    await ia.clickUpdate();
    const after = await ia.adCount();
    ia.assertCountDelta(before, after, 'Delete');
  });

  test('Delete a Rich Media Ad', async ({ homePage, richMediaAdsPage: rm }) => {
    await homePage.navigate('Rich Media', 'Content');
    await rm.selectChannel();
    await rm.filterByDate(String(D().fromDateForRichMediaAds));
    await rm.clickUpdate();
    if ((await rm.noItemsFound.count()) && (await rm.noItemsFound.first().isVisible())) {
      await rm.selectPastItems();
      await rm.clickUpdate();
    }
    const before = await rm.adCount();
    await rm.deleteAd();
    await rm.clickUpdate();
    const after = await rm.adCount();
    rm.assertCountDelta(before, after, 'Delete');
  });

  test('Delete a Print Page', async ({ homePage, printPagesPage: pp }) => {
    await homePage.navigate('Print Pages', 'Content');
    await pp.selectChannel();
    await pp.filterByDate(String(D().fromDateForPages));
    await pp.clickUpdate();
    if (await pp.isEmptyList()) {
      await pp.selectPastItems();
      await pp.clickUpdate();
    }
    const before = await pp.pageCount();
    await pp.deletePage();
    await pp.clickUpdate();
    const after = await pp.pageCount();
    pp.assertCountDelta(before, after, 'Delete');
  });
});

import { test, expect } from '../../fixtures';
import { getDates } from '../../utils/data.utils';

/**
 * Ported from Selenium feature: SmokeTesting/CMS/CopyFunctionality.feature
 * Copy Stories / Interactive Ads / Rich Media Ads / Print Pages — each verifies
 * the record count increases by 1 (Selenium verifyCopyOfStories(before, after, "Copy")).
 * (The Pages copy scenario's count check is commented out in Selenium, so we assert
 * the copy completes and we remain on the Print Pages list.)
 */
const D = () => getDates();

test.describe('@smoke @CMS @Copy Copy functionality', () => {
  test.beforeEach(async ({ loginPage, homePage }) => {
    test.slow();
    await loginPage.ensureLoggedIn();
    await homePage.verifyHomeText();
  });

  test('Copy a Story', async ({ homePage, storyPage }) => {
    await homePage.navigate('Stories', 'Content');
    await storyPage.selectChannel();

    await storyPage.filterByDate(String(D().toDateForCopyOfStories));
    await storyPage.update();
    const before = await storyPage.storyCount();

    await storyPage.filterByDate(String(D().dateForUpdateStories));
    await storyPage.update();
    if (await storyPage.isEmptyList()) {
      await storyPage.selectPastItems();
      await storyPage.update();
    }

    await storyPage.copyStory(String(D().toDateForCopyOfStories));

    await storyPage.filterByDate(String(D().toDateForCopyOfStories));
    // The copied story is not indexed immediately; Selenium did a double-update. Poll
    // (re-update + re-count) until the count increments to before+1.
    await expect
      .poll(
        async () => {
          await storyPage.update();
          return storyPage.storyCount();
        },
        { timeout: 120_000, message: `story count should reach ${before + 1} after copy` },
      )
      .toBe(before + 1);
  });

  test('Copy an Interactive Ad', async ({ homePage, interactiveAdsPage: ia }) => {
    await homePage.navigate('Interactive Ads', 'Content');
    await ia.selectChannel();

    await ia.filterByDate(String(D().toDateForInteractiveAds));
    await ia.clickUpdate();
    const before = await ia.adCount();

    await ia.filterByDate(String(D().fromDateForInteractiveAds));
    await ia.clickUpdate();
    if ((await ia.noItemsFound.count()) && (await ia.noItemsFound.first().isVisible())) {
      await ia.selectPastItems();
      await ia.clickUpdate();
    }

    await ia.copyAd(String(D().toDateForInteractiveAds));

    await ia.filterByDate(String(D().toDateForInteractiveAds));
    await ia.clickUpdate();
    const after = await ia.adCount();
    ia.assertCountDelta(before, after, 'Copy');
  });

  test('Copy a Rich Media Ad', async ({ homePage, richMediaAdsPage: rm }) => {
    await homePage.navigate('Rich Media', 'Content');
    await rm.selectChannel();

    await rm.filterByDate(String(D().toDateForRichMediaAds));
    await rm.clickUpdate();
    const before = await rm.adCount();

    await rm.filterByDate(String(D().fromDateForRichMediaAds));
    await rm.clickUpdate();
    if ((await rm.noItemsFound.count()) && (await rm.noItemsFound.first().isVisible())) {
      await rm.selectPastItems();
      await rm.clickUpdate();
    }

    await rm.copyAd(String(D().toDateForRichMediaAds));

    await rm.filterByDate(String(D().toDateForRichMediaAds));
    await rm.clickUpdate();
    const after = await rm.adCount();
    rm.assertCountDelta(before, after, 'Copy');
  });

  test('Copy a Print Page', async ({ homePage, printPagesPage: pp }) => {
    await homePage.navigate('Print Pages', 'Content');
    await pp.selectChannel();

    await pp.filterByDate(String(D().toDateForPages));
    await pp.clickUpdate();

    await pp.filterByDate(String(D().fromDateForPages));
    await pp.clickUpdate();
    if (await pp.isEmptyList()) {
      await pp.selectPastItems();
      await pp.clickUpdate();
    }

    await pp.copyPage(String(D().toDateForPages));
    // Selenium's count verification for pages is commented out; assert we returned
    // to the Print Pages list (copy completed without error).
    await expect(pp.updateButton, 'should remain on the Print Pages list').toBeVisible({
      timeout: 120_000,
    });
  });
});

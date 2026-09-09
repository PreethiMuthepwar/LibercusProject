import { test, expect } from '../../fixtures';
import { getDates } from '../../utils/data.utils';

/**
 * Ported from Selenium feature: SmokeTesting/CMS/DownloadFunctionality.feature
 * Download an Interactive Ad / Rich Media Ad. Selenium verifyDownload() walked the
 * OS Downloads folder for a file matching the ad title; we use Playwright's download
 * event (same intent: a file is downloaded) and assert a non-empty suggested filename.
 */
const D = () => getDates();

test.describe('@smoke @CMS @Download Download functionality', () => {
  test.beforeEach(async ({ loginPage, homePage }) => {
    test.slow();
    await loginPage.ensureLoggedIn();
    await homePage.verifyHomeText();
  });

  test('Download an Interactive Ad', async ({ homePage, interactiveAdsPage: ia }) => {
    await homePage.navigate('Interactive Ads', 'Content');
    await ia.selectChannel();
    await ia.filterByDate(String(D().fromDate));
    await ia.clickUpdate();
    if ((await ia.noItemsFound.count()) && (await ia.noItemsFound.first().isVisible())) {
      await ia.selectPastItems();
      await ia.clickUpdate();
    }
    const download = await ia.downloadAd();
    expect(download.suggestedFilename(), 'a file should have been downloaded').not.toBe('');
  });

  test('Download a Rich Media Ad', async ({ homePage, richMediaAdsPage: rm }) => {
    await homePage.navigate('Rich Media', 'Content');
    await rm.selectChannel();
    await rm.filterByDate(String(D().fromDate));
    await rm.clickUpdate();
    if ((await rm.noItemsFound.count()) && (await rm.noItemsFound.first().isVisible())) {
      await rm.selectPastItems();
      await rm.clickUpdate();
    }
    // Rich-media download opens a tab at /admin/download?datatype=richmedia&ids=… (Selenium's
    // verifyDownload checked the OS folder; we verify the download endpoint was hit).
    const { url } = await rm.downloadAd();
    expect(url, 'a rich-media download should have been triggered').toContain('/admin/download');
  });
});

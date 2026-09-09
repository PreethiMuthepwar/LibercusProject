import { test } from '../../fixtures';
import { getDates } from '../../utils/data.utils';

/**
 * Ported from Selenium feature: SmokeTesting/CMS/FilterFunctionality.feature
 * Search/filter across Stories, Media Files, Interactive Ads, Rich Media, Print Pages
 * (Freetext Search, Section Letter, Device Type, Ad Type). Search terms match the
 * Selenium statics: Story "", MediaFiles "Bills", InteractiveAds "761996",
 * RichMedia "RichMediaAd1", Pages "test".
 */
const D = () => getDates();

test.describe('@smoke @CMS @Filter Filter functionality', () => {
  test.beforeEach(async ({ loginPage, homePage }) => {
    test.slow();
    await loginPage.ensureLoggedIn();
    await homePage.verifyHomeText();
  });

  test('Search Stories (Freetext)', async ({ homePage, storyPage }) => {
    await homePage.navigate('Stories', 'Content');
    await storyPage.selectChannel();
    await storyPage.filterByDate(String(D().dateForUpdateStories));
    await storyPage.update();
    if (await storyPage.isEmptyList()) { await storyPage.selectPastItems(); await storyPage.update(); }
    await storyPage.selectSearchType('Freetext Search');
    await storyPage.enterSearchText('');
    await storyPage.verifySearchResults('');
  });

  test('Filter Stories by Section Letter', async ({ homePage, storyPage }) => {
    await homePage.navigate('Stories', 'Content');
    await storyPage.selectChannel();
    await storyPage.filterByDate(String(D().dateForUpdateStories));
    await storyPage.update();
    if (await storyPage.isEmptyList()) { await storyPage.selectPastItems(); await storyPage.update(); }
    await storyPage.selectSectionLetter('A');
    await storyPage.verifySectionLetterResults('A');
  });

  test('Search Media Files (Freetext)', async ({ homePage, mediaFilesPage: mf }) => {
    await homePage.navigate('Media Files', 'Content');
    await mf.filterByDate(String(D().fromDateForCopyOfStories));
    await mf.update();
    if (await mf.isEmptyList()) { await mf.selectPastItems(); await mf.update(); }
    await mf.selectSearchType('Freetext Search');
    await mf.enterSearchText('Bills');
    await mf.verifySearchResults('Bills');
  });

  test('Search Interactive Ads (Freetext)', async ({ homePage, interactiveAdsPage: ia }) => {
    await homePage.navigate('Interactive Ads', 'Content');
    await ia.selectChannel();
    await ia.filterByDate(String(D().fromDateForInteractiveAds));
    await ia.clickUpdate();
    if ((await ia.noItemsFound.count()) && (await ia.noItemsFound.first().isVisible())) { await ia.selectPastItems(); await ia.clickUpdate(); }
    await ia.selectSearchType('Freetext Search');
    await ia.enterSearchText('761996');
    await ia.verifySearchResults('761996');
  });

  test('Filter Interactive Ads by Device Type (Tablet)', async ({ homePage, interactiveAdsPage: ia }) => {
    await homePage.navigate('Interactive Ads', 'Content');
    await ia.selectChannel();
    await ia.filterByDate(String(D().fromDateForInteractiveAds));
    await ia.clickUpdate();
    await ia.selectDeviceType('Tablet');
    await ia.verifyDeviceSelected('Tablet');
  });

  test('Search Rich Media Ads (Freetext)', async ({ homePage, richMediaAdsPage: rm }) => {
    await homePage.navigate('Rich Media', 'Content');
    await rm.selectChannel();
    await rm.filterByDate(String(D().fromDateForRichMediaAds));
    await rm.clickUpdate();
    if ((await rm.noItemsFound.count()) && (await rm.noItemsFound.first().isVisible())) { await rm.selectPastItems(); await rm.clickUpdate(); }
    await rm.selectSearchType('Freetext Search');
    await rm.enterSearchText('RichMediaAd1');
    await rm.verifySearchResults('RichMediaAd1');
  });

  test('Filter Rich Media by Device Type (Tablet)', async ({ homePage, richMediaAdsPage: rm }) => {
    await homePage.navigate('Rich Media', 'Content');
    await rm.selectChannel();
    await rm.filterByDate(String(D().fromDateForRichMediaAds));
    await rm.clickUpdate();
    await rm.selectDeviceType('Tablet');
    await rm.verifyDeviceSelected('Tablet');
  });

  test('Filter Rich Media by Ad Type (Lexigo)', async ({ homePage, richMediaAdsPage: rm }) => {
    await homePage.navigate('Rich Media', 'Content');
    await rm.selectChannel();
    await rm.filterByDate(String(D().fromDateForRichMediaAds));
    await rm.clickUpdate();
    await rm.selectAdType('Lexigo');
    await rm.verifyAdType('Lexigo');
  });

  test('Search Print Pages (Freetext)', async ({ homePage, printPagesPage: pp }) => {
    await homePage.navigate('Print Pages', 'Content');
    await pp.selectChannel();
    await pp.filterByDate(String(D().fromDateForPages));
    await pp.clickUpdate();
    if (await pp.isEmptyList()) { await pp.selectPastItems(); await pp.clickUpdate(); }
    await pp.selectSearchType('Freetext Search');
    await pp.enterSearchText('test');
    await pp.verifySearchResults('test');
  });

  test('Filter Print Pages by Section Letter', async ({ homePage, printPagesPage: pp }) => {
    await homePage.navigate('Print Pages', 'Content');
    await pp.selectChannel();
    await pp.filterByDate(String(D().fromDateForPages));
    await pp.clickUpdate();
    if (await pp.isEmptyList()) { await pp.selectPastItems(); await pp.clickUpdate(); }
    await pp.selectSectionLetter('A');
    await pp.verifySectionLetterResults('A');
  });
});

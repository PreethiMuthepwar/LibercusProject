import { test } from '../../fixtures';
import { getDates } from '../../utils/data.utils';

/**
 * Ported from Selenium feature:
 *   SmokeTesting/RegressionTesting/CMS/WireFeeds.feature (2 scenarios)
 * Use a wire story (then find it in Stories by slug); use a wire photo (then find it
 * in Media Files by title).
 *
 * These are date- and data-dependent: a wire item must exist in the chosen
 * transmitted-date range, and the published item must appear under the publish date
 * (Dates.yml dateForWireStories). They exercise the jQuery-UI datepicker + list
 * date-filter (the same code path the smoke date scenarios use).
 */
const WIRE_DATE = String(getDates().dateForWireStories);

test.describe('@regression @CMS @WireFeeds Wire Feeds', () => {
  test.beforeEach(async ({ loginPage, homePage }) => {
    test.slow();
    await loginPage.ensureLoggedIn();
    await homePage.verifyHomeText();
  });

  test('Use a wire story and find it in Stories by slug', async ({
    homePage,
    wireFeedsPage: wf,
    storyPage,
  }) => {
    await homePage.navigate('Wire Stories', 'Feeds');
    await wf.selectTransmittedRange('Last Month');
    await wf.ensureStoriesAvailable();
    await wf.selectStory();
    await wf.clickUse();
    await wf.enterUsePublishDate(WIRE_DATE);
    await wf.selectPopupChannel();
    await wf.omitPhotosAndCaptureSlug();
    await wf.confirmUse();

    await homePage.navigate('Stories', 'Content');
    await storyPage.selectChannel();
    await storyPage.filterByDate(WIRE_DATE);
    await storyPage.update();
    await storyPage.selectSearchType('Freetext Search');
    await storyPage.enterSearchText(wf.slug);
    await storyPage.verifySearchResults(wf.slug);
  });

  test('Use a wire photo and find it in Media Files by title', async ({
    homePage,
    wireFeedsPage: wf,
    mediaFilesPage: mf,
  }) => {
    await homePage.navigate('Wire Photos', 'Feeds');
    await wf.selectTransmittedRange('Last Week');
    await wf.ensurePhotosAvailable();
    await wf.selectPhoto();
    await wf.clickUse();
    await wf.enterUsePublishDate(WIRE_DATE);
    await wf.confirmUse();

    await homePage.navigate('Media Files', 'Content');
    await mf.filterByDate(WIRE_DATE);
    await mf.update();
    await mf.selectSearchType('Freetext Search');
    await mf.enterSearchText(wf.title);
    await mf.verifySearchResults(wf.title);
  });
});

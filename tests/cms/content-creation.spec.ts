import { test, expect } from '../../fixtures';

/**
 * Ported from Selenium feature:
 *   SmokeTesting/CMS/ContentCreation.feature  (Story Creation scenario)
 *
 *   Given I visit "CMS Login" page
 *   When  I log in ...
 *   Then  I should be on "CMS HomePage" page / I should see The Home text
 *   When  I navigate to "Stories" from "Content" on "CMS HomePage"
 *   Then  I should be on "CMS StoryCreation" page
 *   Then  I select the customer-specific channel in "channelDropdown" dropdown
 *   And   I click on "newButton" element for "story 1"
 *   Then  I should see following elements: | slug | status |
 *   When  I enter the publish date, status, slug, and shape for "story 1"
 *   And   I switch to tab on "CMS StoryCreation" page
 *   And   I enter the Kicker,Title,Byline,Story for "story 1"
 *   And   I add an image to the story if it contains an image for "story 1"
 *   And   I click save and close button on "CMS StoryCreation"
 *
 * Selenium Examples activates only "story 1" (story 2-5 are commented out).
 */
import { getStoryData } from '../../utils/data.utils';

const STORY_KEY = 'story 1';

test.describe('@smoke @CMS @Creation Content creation', () => {
  test.beforeEach(async ({ loginPage, homePage }) => {
    test.slow();
    await loginPage.ensureLoggedIn();
    await homePage.verifyHomeText();
  });

  test('Create a Story in the Print channel', async ({ homePage, storyPage }) => {
    console.log('[story] navigating Content > Stories');
    await homePage.navigate('Stories', 'Content');

    console.log('[story] selecting channel');
    await storyPage.selectChannel();

    console.log('[story] clicking New');
    await storyPage.clickNew();

    // Selenium: "I should see following elements: | slug | status |"
    await expect(storyPage.slug, 'slug field should be visible').toBeVisible();
    await expect(storyPage.status, 'status field should be visible').toBeVisible();

    console.log('[story] entering publish date, status, slug, shape');
    await storyPage.enterPublishStatusSlugShape(STORY_KEY);

    console.log('[story] switching to print tab');
    await storyPage.switchToPrintTab();

    console.log('[story] entering title + story body');
    const title = String(getStoryData(STORY_KEY).Title);
    const body = String(getStoryData(STORY_KEY).story);
    await storyPage.enterTitle(title);
    await storyPage.enterStory(body);

    console.log('[story] adding image if shape has a photo slot');
    await storyPage.addImageIfPhoto(STORY_KEY);

    console.log('[story] save & close');
    await storyPage.saveAndClose();

    // Selenium ended after save&close (implicit success). The app closes the editor
    // and returns to the dashboard. Assert that transition (real post-condition) and
    // that no error dialog is present.
    await expect(homePage.content, 'should return to the dashboard after save & close').toBeVisible({
      timeout: 120_000,
    });
    await expect(
      storyPage.saveAndCloseButton.first(),
      'story editor should have closed',
    ).toBeHidden({ timeout: 30_000 });
  });

  test('Create an Interactive Ad', async ({ homePage, interactiveAdsPage: ia }) => {
    await homePage.navigate('Interactive Ads', 'Content');
    await ia.selectChannel();
    await ia.clickNew();
    await ia.expectCreationPage();
    await ia.enterFormPublishDate('12/01/2025');
    await ia.selectStatusPublished();
    await ia.enterAdType();
    await ia.enterTitleSlug();
    await ia.selectAllDeviceCheckboxes();
    await ia.uploadImage('interactiveAd1');
    await ia.expectImageUploaded();
    await ia.uploadZip('interactiveAd1');
    await ia.expectZipUploaded();
    await ia.saveAndClose();
    await expect(homePage.content, 'should return to dashboard after save').toBeVisible({ timeout: 120_000 });
  });

  test('Create a Rich Media Ad', async ({ homePage, richMediaAdsPage: rm }) => {
    await homePage.navigate('Rich Media', 'Content');
    await rm.selectChannel();
    await rm.clickNew();
    await rm.expectCreationPage();
    await rm.enterFormPublishDate('21/07/2025');
    await rm.selectStatusPublished();
    await rm.enterTitleSlug();
    await rm.enterAdType();
    await rm.selectAllCheckboxes();
    await rm.uploadImage('RichMediaAd1');
    await rm.expectImageUploaded();
    await rm.uploadZip('RichMediaAd1');
    await rm.expectZipUploaded();
    await rm.saveAndClose();
    await expect(homePage.content, 'should return to dashboard after save').toBeVisible({ timeout: 120_000 });
  });

  test('Create a Print Ad', async ({ homePage, printAdsPage: pa }) => {
    await homePage.navigate('Print Ads', 'Content');
    await pa.selectChannel();
    await pa.clickNew();
    await expect(pa.statusDropdown, 'status dropdown should be visible').toBeVisible();
    await pa.enterFormPublishDate('12/01/2025');
    await pa.selectStatusPublished();
    await pa.enterAdName('PrintAds');
    await pa.uploadPdf();
    await pa.expectPdfUploaded();
    await pa.saveAndClose();
    await expect(homePage.content, 'should return to dashboard after save').toBeVisible({ timeout: 120_000 });
  });

  test('Create a Media File', async ({ homePage, mediaFilesPage: mf }) => {
    await homePage.navigate('Media Files', 'Content');
    await mf.clickNew();
    await mf.enterFormPublishDate('01/01/2026');
    await mf.enterTitleCaption();
    await mf.uploadImage();
    await mf.expectImageUploaded();
    await mf.saveAndClose();
    await mf.filterByDate('01/01/2026');
    await mf.update();
    await expect(homePage.content, 'should return to the dashboard after save & close').toBeVisible({
      timeout: 120_000,
    });
    // Selenium post-create check: refresh the list and assert the created title appears
    // (eachTitle.equalsIgnoreCase(mediafileTitle)). Restored for assertion parity.
    await mf.verifyCreated();
  });
});

import { test, expect } from '../../fixtures';
import { getCustomerEnv } from '../../utils/env';

/**
 * Ported from Selenium feature:
 *   SmokeTesting/RegressionTesting/CMS/PDFScenarios.feature (3 scenarios)
 *
 * Scenario 2 (create a Print Ad) is fully portable. Scenarios 1 and 3 build a Print Page
 * in the Layout editor, then generate a PDF proof (1) or drag-and-drop a Print Ad onto the
 * page canvas (3) — `libPageLayoutAreaframe` cross-frame drag + PDF-proof dialog chain.
 *
 * Env adaptation: the feature hardcodes PG values (status "Done", template "BIZ_Sunday
 * cover NEW FONTS" / "6 col Inside Left NEW FONTS"); on TB we use the env-appropriate
 * values so the form fills. Selenium's own framework has this PG/else mapping.
 * Note: "I enter the publish date for the page" is `Thread.sleep(100000)` (a manual pause)
 * in Selenium — we set the date via the form datepicker instead.
 */
const STATUS = getCustomerEnv() === 'PG' ? 'Done' : 'Published';
const TEMPLATE = getCustomerEnv() === 'PG' ? '6 col Inside Left NEW FONTS' : '6 col Inside Left - CLE';

test.describe('@regression @CMS @PDFScenarios PDF / Print Page scenarios', () => {
  test.beforeEach(async ({ loginPage, homePage }) => {
    test.slow();
    await loginPage.ensureLoggedIn();
    await homePage.verifyHomeText();
  });

  test('Create a Print Ad in the CMS', async ({ homePage, printAdsPage: pa }) => {
    await homePage.navigate('Print Ads', 'Content');
    await pa.selectChannel();
    await pa.clickNew();
    await expect(pa.statusDropdown, 'status dropdown should be visible').toBeVisible({ timeout: 90_000 });
    await pa.selectStatusPublished();
    await pa.enterAdName('PrintAds');
    await pa.uploadPdf();
    await pa.expectPdfUploaded();
    await pa.saveAndClose();
  });

  test('Create a Print Page and generate a PDF proof', async ({ homePage, printPagesPage: pp }) => {
    await homePage.navigate('Print Pages', 'Content');
    await pp.selectChannel();
    await pp.clickNew();
    await pp.fillNewPageForm('A', '2', TEMPLATE, STATUS);
    await pp.tapLayout();
    await pp.expectLayoutDisplayed();
    await pp.generatePdfProof();
  });

  test('Add a created Print Ad to a Print Page', async ({ homePage, printPagesPage: pp }) => 
    {
    await homePage.navigate('Print Pages', 'Content');
    await pp.selectChannel();
    await pp.clickNew();
    await pp.fillNewPageForm('A', '2', TEMPLATE, STATUS);
    await pp.tapLayout();
    await pp.expectLayoutDisplayed();
    await pp.addPrintAdToPage();
    await pp.dragOntoLayout('printad');
    await pp.saveAndClose();
  });
});

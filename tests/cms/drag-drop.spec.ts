import { test } from '../../fixtures';
import { getCustomerEnv } from '../../utils/env';
import { getDates } from '../../utils/data.utils';

/**
 * The three previously-deferred smoke scenarios that build/edit a Print Page in the Layout
 * editor: drag-and-drop a story onto a page (ContentCreation), generate a PDF proof for an
 * existing page (OutputOfPrintpages), and the Production View rich-media edit panel
 * (ProductionView).
 *
 * These exercise cross-frame drag into `iframe.libPageLayoutAreaframe`, the multi-popup
 * PDF-proof chain, and the list date-filter (`calender`) path. The date-filter is the same
 * `#help`-overlay app/env issue that gates Copy/Delete/Update in BOTH suites on TB; where
 * these block there, it is app/env parity (documented), not a migration defect. Env values
 * (status/template) are adapted PG→TB as the Selenium framework itself does.
 */
const STATUS = getCustomerEnv() === 'PG' ? 'Done' : 'Published';
const TEMPLATE = getCustomerEnv() === 'PG' ? '6 col Inside Left NEW FONTS' : '6 col Inside Left - CLE';
const D = () => getDates();

test.describe('@smoke @CMS @layout Print Page layout / PDF / Production View', () => {
  test.beforeEach(async ({ loginPage, homePage }) => {
    test.slow();
    await loginPage.ensureLoggedIn();
    await homePage.verifyHomeText();
  });

  // ContentCreation.feature — create a print page and add a story to its layout (drag-drop).
  test('Create a print page and add a story to the layout', async ({ homePage, printPagesPage: pp }) => {
    await homePage.navigate('Print Pages', 'Content');
    await pp.selectChannel();
    await pp.clickNew();
    await pp.creationPageDisplayed();
    await pp.fillNewPageForm('A', '1', TEMPLATE, STATUS);
    await pp.tapLayout();
    await pp.expectLayoutDisplayed();
    await pp.addStoryToPage();
    await pp.dragOntoLayout('story');
    await pp.saveAndClose();
  });

  // OutputOfPrintpages.feature — open an existing page and generate its PDF proof.
  test('Generate a PDF proof for an existing print page', async ({ homePage, printPagesPage: pp }) => {
    await homePage.navigate('Print Pages', 'Content');
    await pp.selectChannel();
    await pp.filterByDate(String(D().fromDate)); // "select the date that has edition"
    await pp.clickUpdate();
    await pp.openExistingPage();
    await pp.tapLayout();
    await pp.expectLayoutDisplayed();
    await pp.generatePdfProof();
  });

  // ProductionView.feature — Tablet View + rich-media edit mode shows the Rich Ad panel.
  test('Production View shows the Rich Ad panel in edit mode', async ({ homePage, productionViewPage: pv }) => {
    await homePage.navigate('Production View', 'Content');
    await pv.selectChannel();
    await pv.clickTabletView();
    await pv.filterByDate(String(D().fromDateForRichMediaAds)); // "select the date which has rich media ads"
    await pv.clickUpdate();
    await pv.turnOnAdEditMode();
    await pv.expectRichAdPanel();
  });
});

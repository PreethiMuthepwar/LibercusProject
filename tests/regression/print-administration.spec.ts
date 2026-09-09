import { test, expect } from '../../fixtures';
import { PrintAdminPage } from '../../pages/cms/print-admin.page';
import { getCustomerEnv } from '../../utils/env';

/**
 * Ported from Selenium feature:
 *   SmokeTesting/RegressionTesting/CMS/PrintAdministration.feature (14 scenarios)
 * Shapes, Styles, Templates, Image Styles, Jump Styles — create / search / copy / delete.
 *
 * Channel: the feature literally selects "PG Print" (a PG-env value). On TB we select
 * the customer-specific channel (selectChannel) — the same accommodation the smoke port
 * made, since the literal "PG Print" option does not exist on TB.
 */
// The shape's Canvas/template dropdown is env-specific: the Selenium framework's own
// template steps map PG → "6 col Inside Left NEW FONTS", else (TB/CLE) → "6 col Inside
// Left - CLE". The feature literal is the PG value; use the env-appropriate one so the
// option actually exists on the target environment.
const TEMPLATE_TEXT =
  getCustomerEnv() === 'PG' ? '6 col Inside Left NEW FONTS' : '6 col Inside Left - CLE';

test.describe('@regression @CMS @PrintAdmin Print Administration', () => {
  test.beforeEach(async ({ loginPage, homePage }) => {
    test.slow();
    await loginPage.ensureLoggedIn();
    await homePage.verifyHomeText();
  });

  // ---- Shapes ----
  test('Create a shape and confirm it is available when creating a story', async ({
    homePage,
    printAdminPage: pa,
    storyPage,
  }) => {
    await homePage.navigate('Shapes', 'Print Administration');
    await pa.clickNew('printshapesPanel');
    await pa.shapesCreationDisplayed();
    await pa.fillNewShape(TEMPLATE_TEXT);
    await pa.saveAndClose();
    await pa.refreshAndWaitForShapes();
    await homePage.navigate('Stories', 'Content');
    await storyPage.selectChannel();
    await storyPage.clickNew();
    await expect(storyPage.slug, 'slug field should be visible').toBeVisible({ timeout: 90_000 });
    await expect(storyPage.status, 'status dropdown should be visible').toBeVisible({ timeout: 90_000 });
    // Selenium verifyShapeisDisplayed: the created shape is selectable in the Shape dropdown.
    await storyPage.selectShape(PrintAdminPage.SHAPE_NAME);
  });

  test('Search shapes returns matching results', async ({ homePage, printAdminPage: pa }) => {
    await homePage.navigate('Shapes', 'Print Administration');
    await pa.searchShapes();
  });

  test('Copy an existing shape', async ({ homePage, printAdminPage: pa }) => {
    await homePage.navigate('Shapes', 'Print Administration');
    await pa.copyShape();
  });

  test('Delete a shape', async ({ homePage, printAdminPage: pa }) => {
    await homePage.navigate('Shapes', 'Print Administration');
    await pa.deleteShape();
  });

  // ---- Styles ----
  test('Create a style and confirm it is displayed on a story', async ({
    homePage,
    printAdminPage: pa,
    storyPage,
  }) => {
    await homePage.navigate('Styles', 'Print Administration');
    await pa.clickNew('printstylesPanel');
    await pa.stylesCreationDisplayed();
    await pa.fillNewStyle();
    await pa.saveAndClose();
    await pa.refreshAndWaitForShapes();
    await homePage.navigate('Stories', 'Content');
    await storyPage.selectChannel();
    await storyPage.clickNew();
    await expect(storyPage.slug, 'slug field should be visible').toBeVisible({ timeout: 90_000 });
    await expect(storyPage.status, 'status dropdown should be visible').toBeVisible({ timeout: 90_000 });
    await storyPage.enterSlug();
    await storyPage.switchToPrintTab();
    await storyPage.verifyStyleDisplayed(PrintAdminPage.STYLE_NAME);
  });

  test('Search styles returns matching results', async ({ homePage, printAdminPage: pa }) => {
    await homePage.navigate('Styles', 'Print Administration');
    await pa.searchStyles();
  });

  test('Copy a style', async ({ homePage, printAdminPage: pa }) => {
    await homePage.navigate('Styles', 'Print Administration');
    await pa.copyStyle();
  });

  test('Delete a style', async ({ homePage, printAdminPage: pa }) => {
    await homePage.navigate('Styles', 'Print Administration');
    await pa.deleteStyle();
  });

  // ---- Templates ----
  test('Create a template and confirm it appears on a print page', async ({
    homePage,
    printAdminPage: pa,
    printPagesPage: pp,
  }) => {
    await homePage.navigate('Templates', 'Print Administration');
    await pa.clickNew('printpagetemplatesPanel');
    await pa.templatesCreationDisplayed();
    await pa.fillNewTemplate();
    await pa.saveAndClose();
    await pa.refreshAndWaitForShapes();
    await homePage.verifyHomeText();
    await homePage.navigate('Print Pages', 'Content');
    await pp.selectChannel();
    await pp.clickNew();
    await pp.creationPageDisplayed();
    await pp.verifyTemplateOnPage(PrintAdminPage.TEMPLATE_NAME);
  });

  test('Search templates returns matching results', async ({ homePage, printAdminPage: pa }) => {
    await homePage.navigate('Templates', 'Print Administration');
    await pa.searchTemplates();
  });

  test('Delete a template', async ({ homePage, printAdminPage: pa }) => {
    await homePage.navigate('Templates', 'Print Administration');
    await pa.selectTemplateAndConfirm('Delete');
  });

  test('Copy a template', async ({ homePage, printAdminPage: pa }) => {
    await homePage.navigate('Templates', 'Print Administration');
    await pa.selectTemplateAndConfirm('Copy');
  });

  // ---- Image Styles ----
  test('Create an image style', async ({ homePage, printAdminPage: pa }) => {
    await homePage.navigate('Image Styles', 'Print Administration');
    await pa.clickNewGeneric();
    await pa.imageStyleCreationDisplayed();
    await pa.fillNewImageStyle();
    await pa.saveAndClose();
    await pa.verifyImageStyleCreated();
  });

  // ---- Jump Styles ----
  test('Create a jump style', async ({ homePage, printAdminPage: pa }) => {
    await homePage.navigate('Jump Styles', 'Print Administration');
    await pa.clickNew('printjumpstylesPanel');
    await pa.jumpStyleCreationDisplayed();
    await pa.fillNewJumpStyle();
    await pa.saveAndClose();
    await pa.verifyJumpStyleCreated();
  });
});

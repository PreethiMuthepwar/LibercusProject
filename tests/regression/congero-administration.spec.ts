import { test } from '../../fixtures';

/**
 * Ported from Selenium feature:
 *   SmokeTesting/RegressionTesting/CMS/CongeroAdministration.feature (2 scenarios)
 * Lookup Data creation, Congero Type creation (with a field definition + verification).
 */
test.describe('@regression @CMS @Congero Congero Administration', () => {
  test.beforeEach(async ({ loginPage, homePage }) => {
    test.slow();
    await loginPage.ensureLoggedIn();
    await homePage.verifyHomeText();
  });

  test('Create a Lookup Data entry', async ({ homePage, congeroPage: cg }) => {
    await homePage.navigate('Lookup Data', 'Congero Administration');
    await cg.clickLookupNew();
    await cg.lookupNameDisplayed();
    await cg.fillNewLookupData();
    await cg.saveAndClose();
    // Selenium's "Verify LookupData is created" step is commented out — success = save & close.
  });

  test('Create a Congero Type with a field definition', async ({ homePage, congeroPage: cg }) => {
    await homePage.navigate('Congero Types', 'Congero Administration');
    await cg.clickTypeNew();
    await cg.typeNameDisplayed();
    await cg.fillNewCongeroType();
    await cg.addFieldDefinition();
    await cg.fillFieldDefinition();
    await cg.saveAndClose();
    await cg.refresh();
    await cg.verifyCongeroTypeCreated();
    await cg.clickNewOfCongeroAndVerifyField();
  });
});

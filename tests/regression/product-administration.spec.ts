import { test } from '../../fixtures';

/**
 * Ported from Selenium feature:
 *   SmokeTesting/RegressionTesting/CMS/ProductAdministration.feature (3 scenarios)
 * User creation (+ re-login), Tag creation, Tag search.
 */
test.describe('@regression @CMS @ProductAdmin Product Administration', () => {
  test.beforeEach(async ({ loginPage, homePage }) => {
    test.slow();
    await loginPage.ensureLoggedIn();
    await homePage.verifyHomeText();
  });

  test('Create a new user and log in as that user', async ({ homePage, productAdminPage: pa }) => {
    await homePage.navigate('Users', 'Product Administration');
    await pa.noteUserCount();
    await pa.clickUserNew();
    await pa.userCreationDisplayed();
    await pa.fillNewUser();
    await pa.saveAndClose();
    await pa.verifyUserCreated();
    await homePage.clickLogout();
    await pa.loginAsCreatedUser();
  });

  test('Create a new tag', async ({ homePage, productAdminPage: pa }) => {
    await homePage.navigate('Tags', 'Product Administration');
    await pa.noteTagCount();
    await pa.clickTagNew();
    await pa.tagCreationDisplayed();
    await pa.fillNewTag();
    await pa.saveAndClose();
    await pa.verifyTagCreated();
  });

  test('Search tags returns matching results', async ({ homePage, productAdminPage: pa }) => {
    await homePage.navigate('Tags', 'Product Administration');
    await pa.searchTags();
    await pa.verifyTagSearchResults();
  });
});

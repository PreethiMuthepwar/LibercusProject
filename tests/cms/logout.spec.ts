import { test } from '../../fixtures';

/**
 * Ported from Selenium feature:
 *   SmokeTesting/CMS/LogoutFunctionality.feature
 *
 *   Given I visit "CMS Login" page
 *   When  I log in to the Libercus application with valid user credentials
 *   Then  I should be on "CMS HomePage" page
 *   And   I should see The Home text
 *   When  I click on "Logout" element
 *   Then  I should be on "CMS Login" page
 *
 * This scenario tests the login→logout round-trip, so it runs WITHOUT the
 * shared storageState (fresh, unauthenticated context) to reproduce the
 * Selenium flow faithfully.
 */
test.use({ storageState: { cookies: [], origins: [] } });

test('@smoke @CMS @logout User can log out of the CMS', async ({ loginPage, homePage }) => {
  test.slow(); // app is slow; Selenium used a 240s login-ready wait.

  console.log('[logout] visiting CMS login');
  await loginPage.goto();

  console.log('[logout] logging in');
  await loginPage.login();

  console.log('[logout] verifying Home text');
  await homePage.verifyHomeText();

  console.log('[logout] clicking Logout');
  await homePage.clickLogout();

  console.log('[logout] verifying back on login page');
  await loginPage.expectOnLoginPage();
});

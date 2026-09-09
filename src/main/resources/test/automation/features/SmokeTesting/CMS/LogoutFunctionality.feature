@Logout, @Smoke
Feature: This feature verifies the Logout functionality in the Libercus CMS application.

  @CMS
  Scenario: This scenario validates that a logged-in user can successfully log out of the CMS.
    Given I visit "CMS Login" page
    When I log in to the Libercus application with valid user credentials
    Then I should be on "CMS HomePage" page
    And  I should see The Home text
    When I click on "Logout" element
    Then I should be on "CMS Login" page
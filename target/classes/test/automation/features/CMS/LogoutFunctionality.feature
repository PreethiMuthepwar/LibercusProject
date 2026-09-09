@Logout
Feature: Logout Functionality

  @CMS
  Scenario: User logs out from the application
    Given I visit "CMS Login" page
    When I log in to the Libercus application with valid user credentials
    Then I should be on "CMS HomePage" page
    And  I should see The Home text
    When I click on "Logout" element
    Then I should be on "CMS Login" page

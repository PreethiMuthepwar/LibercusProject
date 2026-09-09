Feature: This feature validates the Help Content functionality in the PF3 application.
  It ensures that when a user navigates to the Help section from the main menu, all help-related information and guidance are displayed correctly to assist users in understanding the app’s features.
  @pf3
  Scenario: This scenario verifies that the Help page opens successfully from the main menu and that all help sections or articles are properly displayed.
  It ensures that users can access relevant help content.
    Given I visit "PF3 HomePage" page
    Then I should be on "PF3 HomePage" page
    When I click on "menuButton" element
    And I click on "help" element on "PF3 HomePage" page
    Then I should be on "PF3 HelpPage" page
    And  I should see "helpHeader" element
    And I should see each help content
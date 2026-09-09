Feature: This feature validates the settings functionality in the PF3 application.
  It ensures that users can access the settings panel, view the settings header, and make changes to menu layout, screen mode (theme color), and display options.

  @pf3
  Scenario: Validates that the user can select a menu layout style and that the menu layout is applied on the correct side of the interface.
    Given I visit "PF3 HomePage" page
    Then I should be on "PF3 HomePage" page
    When I click on "menuButton" element
    And I click on "settings" element on "PF3 HomePage" page
    Then I should be on "PF3 SettingsPage" page
    And  I should see "settingsHeader" element
    When I select menu layout style as "Left"
    Then I should see menu layout on "Left" side

  @pf3
  Scenario: Verifies that the user can select a theme color and that the application layout reflects the chosen theme.
    Given I visit "PF3 HomePage" page
    Then I should be on "PF3 HomePage" page
    When I click on "menuButton" element
    And I click on "settings" element on "PF3 HomePage" page
    Then I should be on "PF3 SettingsPage" page
    And  I should see "settingsHeader" element
    When I select "Blue" as theme color
    Then I should see layout in "Blue" color

  @pf3
  Scenario: Checks that the user can select a display mode and that the page layout updates accordingly to the chosen display option.
    Given I visit "PF3 HomePage" page
    Then I should be on "PF3 HomePage" page
    When I click on "menuButton" element
    And  I check single page default display option
    And I click on "settings" element on "PF3 HomePage" page
    Then I should be on "PF3 SettingsPage" page
    And  I should see "settingsHeader" element
    When I select "Double Page" as display option
    Then I validate display option feature "Double page"
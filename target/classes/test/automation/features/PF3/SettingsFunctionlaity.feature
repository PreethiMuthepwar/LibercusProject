Feature: Settings Functionality

  @pf3
  Scenario: 1.Menu layout style
    Given I visit "PF3 HomePage" page
    Then I should be on "PF3 HomePage" page
    When I click on "menuButton" element
    And I click on "settings" element on "PF3 HomePage" page
    Then I should be on "PF3 SettingsPage" page
    And  I should see "settingsHeader" element
    When I select menu layout style as "Left"
    Then I should see menu layout on "Left" side

  @pf3
  Scenario: 2.Screen Mode
    Given I visit "PF3 HomePage" page
    Then I should be on "PF3 HomePage" page
    When I click on "menuButton" element
    And I click on "settings" element on "PF3 HomePage" page
    Then I should be on "PF3 SettingsPage" page
    And  I should see "settingsHeader" element
    When I select "Blue" as theme color
    Then I should see layout in "Blue" color

  @pf3
  Scenario: 3.Display options
    Given I visit "PF3 HomePage" page
    Then I should be on "PF3 HomePage" page
    When I click on "menuButton" element
    And  I check single page default display option
    And I click on "settings" element on "PF3 HomePage" page
    Then I should be on "PF3 SettingsPage" page
    And  I should see "settingsHeader" element
    When I select "Double Page" as display option
    Then I validate display option feature "Double page"



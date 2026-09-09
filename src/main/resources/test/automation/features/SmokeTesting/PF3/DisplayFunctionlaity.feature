Feature: This feature verifies the display mode functionality on the PF3.
  It ensures that users can successfully switch between Single Page and Double Page display modes and that the correct mode is applied and displayed on the screen.

  @pf3
  Scenario: This scenario validates that the user can change the display mode from Single Page (default) to Double Page mode from the display settings menu and confirm that the view updates accordingly.
    Given I visit "PF3 HomePage" page
    Then I should be on "PF3 HomePage" page
    When I click on "menuButton" element
    And  I check single page default display option
    And I click on "display" element on "PF3 HomePage" page
    And I click on "doublePage" element on "PF3 HomePage" page
    Then I validate display option feature "doublePage"

  @pf3
  Scenario: This scenario verifies that the user can switch the display back from Double Page to Single Page mode and that the system correctly reflects the single-page view.
    Given I visit "PF3 HomePage" page
    Then I should be on "PF3 HomePage" page
    When I click on "menuButton" element
    And  I check single page default display option
    And I click on "display" element on "PF3 HomePage" page
    And I click on "singlePage" element on "PF3 HomePage" page
    Then I validate display option feature "singlePage"

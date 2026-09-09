Feature: This feature verifies the sections functionality in the PF3 application.
  It ensures that users can access the sections panel, view the section header, and open a specific section by double-clicking, confirming that the correct content for the selected section is displayed.
  @pf3
  Scenario: Validates that the sections panel is accessible from the menu, the sections header is visible, and that double-clicking on a section navigates the user to the corresponding section content successfully.
    Given I visit "PF3 HomePage" page
    Then I should be on "PF3 HomePage" page
    When I click on "menuButton" element
    And I click on "sections" element on "PF3 HomePage" page
    Then I should be on "PF3 SectionsPage" page
    And  I should see "sectionsHeader" element
    When I double click on section "A"
    Then I should see corresponding section
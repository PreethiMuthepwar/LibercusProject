Feature: Section Functionality

  @pf3
  Scenario: Check Sections functionality
    Given I visit "PF3 HomePage" page
    Then I should be on "PF3 HomePage" page
    When I click on "menuButton" element
    And I click on "sections" element on "PF3 HomePage" page
    Then I should be on "PF3 SectionsPage" page
    And  I should see "sectionsHeader" element
    When I double click on section "A"
    Then I should see corresponding section
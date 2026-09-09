Feature: Display Functionality

  @pf3
  Scenario: 1.Display Double page mode
    Given I visit "PF3 HomePage" page
    Then I should be on "PF3 HomePage" page
    When I click on "menuButton" element
    And  I check single page default display option
    And I click on "display" element on "PF3 HomePage" page
    And I click on "doublePage" element on "PF3 HomePage" page
    Then I validate display option feature "doublePage"

  @pf3
  Scenario: 1.Display single page mode
    Given I visit "PF3 HomePage" page
    Then I should be on "PF3 HomePage" page
    When I click on "menuButton" element
    And  I check single page default display option
    And I click on "display" element on "PF3 HomePage" page
    And I click on "singlePage" element on "PF3 HomePage" page
    Then I validate display option feature "singlePage"

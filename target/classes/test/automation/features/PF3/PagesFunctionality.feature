Feature: Pages Functionality

  @pf3
  Scenario: Check Sections functionality
    Given I visit "PF3 HomePage" page
    Then I should be on "PF3 HomePage" page
    When I click on "menuButton" element
    And I click on "pages" element on "PF3 HomePage" page
    Then I should be on "PF3 Pages" page
    And  I should see "pagesHeader" element
    When I select "A" on Pages panel
    And  I double click on page "A2"
    Then I should see corresponding page
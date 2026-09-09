Feature: This feature validates the Pages functionality in the PF3 application.
  It ensures that users can successfully open the Pages section from the menu, view the list of available pages, and navigate to a specific page by selecting it from the panel.

  @pf3
  Scenario: Verifies that upon selecting a specific section in the Pages panel and double-clicking on a page, the corresponding page is correctly displayed in the viewer.
    Given I visit "PF3 HomePage" page
    Then I should be on "PF3 HomePage" page
    When I click on "menuButton" element
    And I click on "pages" element on "PF3 HomePage" page
    Then I should be on "PF3 Pages" page
    And  I should see "pagesHeader" element
    When I select "A" on Pages panel
    And  I double click on page "A2"
    Then I should see corresponding page
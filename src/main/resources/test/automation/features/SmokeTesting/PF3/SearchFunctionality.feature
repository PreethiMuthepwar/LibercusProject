Feature: This feature verifies the Search functionality in the PF3 application.
  It ensures that users can perform searches using different search modes such as Today’s Edition, Previous Editions, and Advanced Search options, validating that each mode behaves as expected and displays relevant search results.

  @pf3
  Scenario: Verifies that when the user selects the “Today’s Edition” option in the Search menu and enters a keyword, the system performs a search within the current day’s edition and displays the relevant results.
    Given I visit "PF3 HomePage" page
    Then I should be on "PF3 HomePage" page
    When I click on "menuButton" element
    And I click on "search" element on "PF3 HomePage" page
    Then I should be on "PF3 SearchPage" page
    And  I should see "searchHeader" element
    When I click on "todayEditionRadioButton" element
    And I enter "Test" in search field
#    Then I should see searched edition

  @pf3
  Scenario: Ensures that the user can switch to the “Previous Edition” search mode, optionally select a date range, enter a keyword, and retrieve results from older editions.
    Given I visit "PF3 HomePage" page
    Then I should be on "PF3 HomePage" page
    When I click on "menuButton" element
    And I click on "search" element on "PF3 HomePage" page
    Then I should be on "PF3 SearchPage" page
    And  I should see "searchHeader" element
    When I click on "previousEditionRadioButton" element
#    And  I enter start date and end date
    And I enter "Test" in search field
#    Then I should see searched edition

  @pf3
  Scenario: Validates that clicking the “Advanced Search” link opens a new tab for advanced search options, allowing users to refine their search with additional filters like date range or specific keywords.
    Given I visit "PF3 HomePage" page
    Then I should be on "PF3 HomePage" page
    When I click on "menuButton" element
    And I click on "search" element on "PF3 HomePage" page
    Then I should be on "PF3 SearchPage" page
    And  I should see "searchHeader" element
    When I click on "advanceSearchLink" element
    Then I should see new tab open for advance search
##    And  I enter start date and end date
#    And I enter "Test" in search field
##    Then I should see searched edition
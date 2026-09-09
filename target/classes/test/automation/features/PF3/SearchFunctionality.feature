Feature: Search Functionality

  @pf3
  Scenario: Check Today's Edition functionality
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
  Scenario: Check Previous Edition functionality
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
  Scenario: Check Advance Search functionality
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
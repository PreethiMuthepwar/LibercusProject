Feature: Edition Functionality

  @pf3
  Scenario: Navigate to Editions
    Given I visit "PF3 HomePage" page
    Then I should be on "PF3 HomePage" page
    When I click on "menuButton" element
    And I click on "editions" element on "PF3 HomePage" page
    Then I should be on "PF3 EditionsPage" page
    And  I should see "EditionsHeader" element
    And  I double click on Edition "27/04/2025"
    Then I should see corresponding Edition "27/04/2025"
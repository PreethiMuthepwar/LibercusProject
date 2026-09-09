Feature: HelpContent functionality

  @pf3
  Scenario: Help Content is displayed
    Given I visit "PF3 HomePage" page
    Then I should be on "PF3 HomePage" page
    When I click on "menuButton" element
    And I click on "help" element on "PF3 HomePage" page
    Then I should be on "PF3 HelpPage" page
    And  I should see "helpHeader" element
    And I should see each help content
Feature: Ads/Puzzles Functionality

  @pf3
  Scenario: Check Print Ads/Puzzles functionality
    Given I visit "PF3 HomePage" page
    Then I should be on "PF3 HomePage" page
    When I click on "menuButton" element
    And I click on "Ads" element on "PF3 HomePage" page
    Then I should see following elements:
      | printAdsOrPuzzlesButton |
    When I click on "printAdsOrPuzzlesButton" element
    Then I should be on "PF3 AdsPuzzlesPage" page
    And I should see panel with header "Download Ads, Puzzles and Crosswords to Print"
    When I search ads by date
    And I select ad and print


  @pf3
  Scenario: Check Interactive Ads functionality
    Given I visit "PF3 HomePage" page
    Then I should be on "PF3 HomePage" page
    When I click on "menuButton" element
    And I click on "Ads" element on "PF3 HomePage" page
    Then I should see following elements:
      | interactiveAdsButton |
    When I click on "interactiveAdsButton" element
    Then I should be on "PF3 AdsPuzzlesPage" page
    And I should see panel with header "Interactive Ads"
    And I check Interactive ads functionality


  @pf3
  Scenario: Check Interactive Puzzles functionality
    Given I visit "PF3 HomePage" page
    Then I should be on "PF3 HomePage" page
    When I click on "menuButton" element
    And I click on "Ads" element on "PF3 HomePage" page
    Then I should see following elements:
      | interactivePuzzlesButton |
    When I click on "interactivePuzzlesButton" element
    Then I should be on "PF3 AdsPuzzlesPage" page
    And I should see panel with header "Interactive Puzzles"
    And I check Interactive Puzzles functionality


  @pf3
  Scenario: Check Inserts functionality
    Given I visit "PF3 HomePage" page
    Then I should be on "PF3 HomePage" page
    When I click on "menuButton" element
    And I click on "Ads" element on "PF3 HomePage" page
    Then I should see following elements:
      | insertsButton |
    When I click on "insertsButton" element
    Then I should be on "PF3 AdsPuzzlesPage" page
    And I should land on new tab

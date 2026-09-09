Feature: This feature validates different types of ad and puzzle functionalities in the PF3 application,
  including Print Ads/Puzzles, Interactive Ads, Interactive Puzzles, and Inserts. It ensures that all navigation flows, UI panels.

  @pf3
  Scenario: This scenario verifies the Print Ads and Puzzles functionality in PF3.
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
  Scenario: This scenario validates the Interactive Ads section.
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
  Scenario: This scenario checks the Interactive Puzzles section of PF3.
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
  Scenario: This scenario ensures the working of Inserts under the Ads section.
    Given I visit "PF3 HomePage" page
    Then I should be on "PF3 HomePage" page
    When I click on "menuButton" element
    And I click on "Ads" element on "PF3 HomePage" page
    Then I should see following elements:
      | insertsButton |
    When I click on "insertsButton" element
    Then I should be on "PF3 AdsPuzzlesPage" page
    And I should land on new tab

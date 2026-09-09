Feature:Menu Functionality

  @pf3
  Scenario: Check TOC functionality
    Given I visit "PF3 HomePage" page
    Then I should be on "PF3 HomePage" page
    When I click on "menuButton" element
    And I click on "TOC" element on "PF3 HomePage" page
    Then I should be on "PF3 TOCPage" page
    And  I should see "TOCHeader" element

  @pf3
  Scenario: Check Sections functionality
    Given I visit "PF3 HomePage" page
    Then I should be on "PF3 HomePage" page
    When I click on "menuButton" element
    And I click on "sections" element on "PF3 HomePage" page
    Then I should be on "PF3 SectionsPage" page
    And  I should see "sectionsHeader" element

  @pf3
  Scenario: Check Pages functionality
    Given I visit "PF3 HomePage" page
    Then I should be on "PF3 HomePage" page
    When I click on "menuButton" element
    And I click on "pages" element on "PF3 HomePage" page
    Then I should be on "PF3 Pages" page
    And  I should see "pagesHeader" element

  @pf3
  Scenario: Check Editions functionality
    Given I visit "PF3 HomePage" page
    Then I should be on "PF3 HomePage" page
    When I click on "menuButton" element
    And I click on "editions" element on "PF3 HomePage" page
    Then I should be on "PF3 EditionsPage" page
    And  I should see "EditionsHeader" element

  @pf3
  Scenario: Check Search functionality
    Given I visit "PF3 HomePage" page
    Then I should be on "PF3 HomePage" page
    When I click on "menuButton" element
    And I click on "search" element on "PF3 HomePage" page
    Then I should be on "PF3 SearchPage" page
    And  I should see "searchHeader" element

  @pf3
  Scenario: Check Settings functionality
    Given I visit "PF3 HomePage" page
    Then I should be on "PF3 HomePage" page
    When I click on "menuButton" element
    And I click on "settings" element on "PF3 HomePage" page
    Then I should be on "PF3 SettingsPage" page
    And  I should see "settingsHeader" element

  @pf3
  Scenario: Check Ads/Puzzles functionality
    Given I visit "PF3 HomePage" page
    Then I should be on "PF3 HomePage" page
    When I click on "menuButton" element
    And I click on "Ads" element on "PF3 HomePage" page
    Then I should see following elements:
      | printAdsOrPuzzlesButton  |
      | interactiveAdsButton     |
      | insertsButton            |
      | interactivePuzzlesButton |

  @pf3
  Scenario: Check Home functionality
    Given I visit "PF3 HomePage" page
    Then I should be on "PF3 HomePage" page
    And  I check the the forward navigation for sections
    When I click on "menuButton" element
    And I click on "home" element on "PF3 HomePage" page
    Then I should be on first page












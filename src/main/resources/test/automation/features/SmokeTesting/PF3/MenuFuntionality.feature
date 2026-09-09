Feature:This feature verifies the functionality of the main menu options in the PF3 application.
  It ensures that each menu item — such as TOC, Sections, Pages, Editions, Search, Settings, Ads/Puzzles, and Home — navigates to the correct page.

  @pf3
  Scenario: Check TOC functionalityValidates that selecting the “TOC” option from the menu navigates to the Table of Contents page and displays the correct header.
    Given I visit "PF3 HomePage" page
    Then I should be on "PF3 HomePage" page
    When I click on "menuButton" element
    And I click on "TOC" element on "PF3 HomePage" page
    Then I should be on "PF3 TOCPage" page
    And  I should see "TOCHeader" element

  @pf3
  Scenario: Ensures that clicking the “Sections” menu item takes the user to the Sections page and that the section header is displayed properly.
    Given I visit "PF3 HomePage" page
    Then I should be on "PF3 HomePage" page
    When I click on "menuButton" element
    And I click on "sections" element on "PF3 HomePage" page
    Then I should be on "PF3 SectionsPage" page
    And  I should see "sectionsHeader" element

  @pf3
  Scenario: Confirms that the “Pages” option from the menu redirects to the correct Pages screen with the appropriate page header visible.
    Given I visit "PF3 HomePage" page
    Then I should be on "PF3 HomePage" page
    When I click on "menuButton" element
    And I click on "pages" element on "PF3 HomePage" page
    Then I should be on "PF3 Pages" page
    And  I should see "pagesHeader" element

  @pf3
  Scenario: Verifies that choosing “Editions” from the menu opens the Editions page and displays the correct Editions header.
    Given I visit "PF3 HomePage" page
    Then I should be on "PF3 HomePage" page
    When I click on "menuButton" element
    And I click on "editions" element on "PF3 HomePage" page
    Then I should be on "PF3 EditionsPage" page
    And  I should see "EditionsHeader" element

  @pf3
  Scenario: Checks that the “Search” option from the menu takes the user to the Search page and displays the search header element.
    Given I visit "PF3 HomePage" page
    Then I should be on "PF3 HomePage" page
    When I click on "menuButton" element
    And I click on "search" element on "PF3 HomePage" page
    Then I should be on "PF3 SearchPage" page
    And  I should see "searchHeader" element

  @pf3
  Scenario: Validates that selecting “Settings” from the menu navigates to the Settings page where the settings header is shown.
    Given I visit "PF3 HomePage" page
    Then I should be on "PF3 HomePage" page
    When I click on "menuButton" element
    And I click on "settings" element on "PF3 HomePage" page
    Then I should be on "PF3 SettingsPage" page
    And  I should see "settingsHeader" element

  @pf3
  Scenario: Ensures that clicking “Ads” from the menu opens the Ads/Puzzles options and that all related buttons (Print Ads/Puzzles, Interactive Ads, Inserts, and Interactive Puzzles) are displayed.
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
  Scenario: Confirms that selecting the “Home” option from the menu navigates back to the first page of the publication after performing other navigation's.
    Given I visit "PF3 HomePage" page
    Then I should be on "PF3 HomePage" page
    And  I check the the forward navigation for sections
    When I click on "menuButton" element
    And I click on "home" element on "PF3 HomePage" page
    Then I should be on first page












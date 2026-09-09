Feature:Article View Menu Functionality

  @pf3
  Scenario: 1.Validate Article view > Menu> TOC
    Given I visit "PF3 HomePage" page
    Then I should be on "PF3 HomePage" page
    When I click on the story
    Then I should be on "PF3 ArticleViewPage" page
    When I click on "articleViewMenu" element
    And I click on "TOC" element on "PF3 HomePage" page
    Then I should be on "PF3 TOCPage" page
    And  I should see "TOCHeader" element

  @pf3
  Scenario: 2.Validate Article view > Menu > Next and Previous buttons
    Given I visit "PF3 HomePage" page
    Then I should be on "PF3 HomePage" page
    When I click on the story
    Then I should be on "PF3 ArticleViewPage" page
    When I click on "articleViewMenu" element
    Then I navigate to through the pages using next button and validate
    And I navigate to through the pages using previous button and validate

  @pf3
  Scenario: 3.Validate Article view > Menu > Settings
    Given I visit "PF3 HomePage" page
    Then I should be on "PF3 HomePage" page
    When I click on the story
    Then I should be on "PF3 ArticleViewPage" page
    When I click on "articleViewMenu" element
    And I click on "settings" element on "PF3 HomePage" page
    Then I should be on "PF3 SettingsPage" page
    And  I should see "articleSettingsHeader" element


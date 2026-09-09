Feature:This feature validates the article view menu items

  @pf3
  Scenario: This scenario Validates the TOC elements from article view
    Given I visit "PF3 HomePage" page
    Then I should be on "PF3 HomePage" page
    When I click on the story
    Then I should be on "PF3 ArticleViewPage" page
    When I click on "articleViewMenu" element
    And I click on "TOC" element on "PF3 HomePage" page
    Then I should be on "PF3 TOCPage" page
    And  I should see "TOCHeader" element

  @pf3
  Scenario: This scenario validates the article view next and previous buttons
    Given I visit "PF3 HomePage" page
    Then I should be on "PF3 HomePage" page
    When I click on the story
    Then I should be on "PF3 ArticleViewPage" page
    When I click on "articleViewMenu" element
    Then I navigate to through the pages using next button and validate
    And I navigate to through the pages using previous button and validate

  @pf3
  Scenario: Validates the Settings from the article view
    Given I visit "PF3 HomePage" page
    Then I should be on "PF3 HomePage" page
    When I click on the story
    Then I should be on "PF3 ArticleViewPage" page
    When I click on "articleViewMenu" element
    And I click on "settings" element on "PF3 HomePage" page
    Then I should be on "PF3 SettingsPage" page
    And  I should see "articleSettingsHeader" element


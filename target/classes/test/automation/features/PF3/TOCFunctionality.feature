Feature: TOC Functionality

  @pf3
  Scenario: 1.Navigating the sections from TOC
    Given I visit "PF3 HomePage" page
    Then I should be on "PF3 HomePage" page
    When I click on "menuButton" element
    And I click on "TOC" element on "PF3 HomePage" page
    Then I should be on "PF3 TOCPage" page
    And  I should see "TOCHeader" element
    When I click on each section and validate the pages under it

  @pf3
  Scenario: 1.Navigating the Pages from TOC
    Given I visit "PF3 HomePage" page
    Then I should be on "PF3 HomePage" page
    When I click on "menuButton" element
    And I click on "TOC" element on "PF3 HomePage" page
    Then I should be on "PF3 TOCPage" page
    And  I should see "TOCHeader" element
    When I select section "A" on TOC
    And I click each page and validate


  @pf3
  Scenario: Navigating the stories from TOC
    Given I visit "PF3 HomePage" page
    Then I should be on "PF3 HomePage" page
    When I click on "menuButton" element
    And I click on "TOC" element on "PF3 HomePage" page
    Then I should be on "PF3 TOCPage" page
    And  I should see "TOCHeader" element
    When I select section "A" on TOC
    When I select Page "A1" on TOC
    Then I validate stories under selected section and page

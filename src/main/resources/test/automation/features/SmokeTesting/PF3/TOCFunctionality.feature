Feature: This feature validates the Table of Contents (TOC) functionality in the PF3 application.
  It ensures that users can navigate through sections, pages, and stories directly from the TOC and that the content under each selection is displayed correctly.

  @pf3
  Scenario: Verifies that a user can click on each section in the TOC and validate the pages listed under that section.
    Given I visit "PF3 HomePage" page
    Then I should be on "PF3 HomePage" page
    When I click on "menuButton" element
    And I click on "TOC" element on "PF3 HomePage" page
    Then I should be on "PF3 TOCPage" page
    And  I should see "TOCHeader" element
    When I click on each section and validate the pages under it

  @pf3
  Scenario: Checks that a user can select a specific section in the TOC and then click on each page within that section to validate its content.
    Given I visit "PF3 HomePage" page
    Then I should be on "PF3 HomePage" page
    When I click on "menuButton" element
    And I click on "TOC" element on "PF3 HomePage" page
    Then I should be on "PF3 TOCPage" page
    And  I should see "TOCHeader" element
    When I select section "A" on TOC
    And I click each page and validate

  @pf3
  Scenario: Ensures that a user can select a section and page in the TOC and then verify all stories listed under that section and page.
    Given I visit "PF3 HomePage" page
    Then I should be on "PF3 HomePage" page
    When I click on "menuButton" element
    And I click on "TOC" element on "PF3 HomePage" page
    Then I should be on "PF3 TOCPage" page
    And  I should see "TOCHeader" element
    When I select section "A" on TOC
    When I select Page "A1" on TOC
    Then I validate stories under selected section and page
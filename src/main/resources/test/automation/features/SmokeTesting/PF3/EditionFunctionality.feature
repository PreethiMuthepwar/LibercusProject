Feature: This feature verifies the Edition functionality within the PF3 application.
  It ensures that users can successfully navigate from the homepage to the Editions section, view the available editions, and open a specific edition by date to confirm that the correct edition content is displayed.

  @pf3
  Scenario: This scenario validates that the user can open the Editions section from the main menu, view the list of available editions, and open a specific edition.
  It confirms that the selected edition loads and displays correctly on the screen.
    Given I visit "PF3 HomePage" page
    Then I should be on "PF3 HomePage" page
    When I click on "menuButton" element
    And I click on "editions" element on "PF3 HomePage" page
    Then I should be on "PF3 EditionsPage" page
    And  I should see "EditionsHeader" element
    And  I double click on Edition "27/04/2025"
    Then I should see corresponding Edition "27/04/2025"
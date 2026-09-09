Feature:This feature verifies the page navigation functionality in the PF3 application.
  It ensures that users can smoothly navigate forward ( > ) and backward ( < ) between pages.

  @pf3
  Scenario: Validates that when a user navigates forward and backward using the page navigation arrows, the correct pages are displayed, and the navigation works as expected in both directions.
    Given I visit "PF3 HomePage" page
    Then I should be on "PF3 HomePage" page
    And  I check the the forward navigation and backward navigation for page

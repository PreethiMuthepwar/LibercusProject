Feature:This feature validates that all hyperlinks present on the PF3 HomePage are functioning as expected.
  It ensures that each link properly redirects the user to its intended destination without errors or broken pages.

  @pf3
  Scenario: This scenario verifies that all hyperlinks available on the PF3 HomePage are active and redirect users to the correct pages.
    Given I visit "PF3 HomePage" page
    Then I should be on "PF3 HomePage" page
    And I check all the hyperlinks are redirecting
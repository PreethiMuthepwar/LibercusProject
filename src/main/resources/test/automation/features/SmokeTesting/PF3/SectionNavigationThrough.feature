Feature:This feature validates the section-level navigation in the PF3 application.
  It ensures that users can move between different newspaper sections using the next (>>) and previous (<<) navigation controls.

  @pf3
  Scenario: Verifies that the user can move forward using the next (>>) navigation button, ensuring that the next section loads correctly.
    Given I visit "PF3 HomePage" page
    Then I should be on "PF3 HomePage" page
    And  I check the the forward navigation for sections

  @pf3
  Scenario: Ensures that the user can navigate backward through using the previous (<<) navigation button, confirming that the previous section is displayed as expected.
    Given I visit "PF3 HomePage" page
    Then I should be on "PF3 HomePage" page
    And  I check the the previous navigation for sections


Feature:Section Navigation through(>> & <<)

  @pf3
  Scenario: Check next(>>) navigation for Sections
    Given I visit "PF3 HomePage" page
    Then I should be on "PF3 HomePage" page
    And  I check the the forward navigation for sections

  @pf3
  Scenario: Check previous(<<) navigation for Sections
    Given I visit "PF3 HomePage" page
    Then I should be on "PF3 HomePage" page
    And  I check the the previous navigation for sections


Feature:Page Navigation(> & <)

  @pf3
  Scenario: Check next(>) and Previous (<) navigation for page
    Given I visit "PF3 HomePage" page
    Then I should be on "PF3 HomePage" page
    And  I check the the forward navigation and backward navigation for page

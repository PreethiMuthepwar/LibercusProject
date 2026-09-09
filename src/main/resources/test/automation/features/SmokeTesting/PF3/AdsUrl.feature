Feature: This feature validates that ads displayed on the PF3 are linked correctly.
  It ensures that clicking an ad image opens the corresponding ad URL in a new browser tab

  @pf3
  Scenario: This scenario verifies that clicking on an ad on the PF3 opens the correct URL in a new tab.
    Given I visit "PF3 HomePage" page
    Then I should be on "PF3 HomePage" page
    When I click on Ad image
    Then I should see new tab opening for the ad
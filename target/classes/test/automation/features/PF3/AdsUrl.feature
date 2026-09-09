Feature: Ads URL

  @pf3
  Scenario: Check Ads
    Given I visit "PF3 HomePage" page
    Then I should be on "PF3 HomePage" page
    When I click on Ad image
    Then I should see new tab opening for the ad



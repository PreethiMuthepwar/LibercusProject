Feature: Article View Functionality

  @pf3
  Scenario: Validate article view is displayed from different stories
    Given I visit "PF3 HomePage" page
    Then I should be on "PF3 HomePage" page
    When I click on the story
    Then I should see article view for the clicked story


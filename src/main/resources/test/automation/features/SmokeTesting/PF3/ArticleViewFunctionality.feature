Feature: This feature validates the Article View in the PF3 application.
  It ensures that when a user clicks on a story from the homepage, the corresponding article view is displayed correctly, allowing users to read the full content of the selected story.
  @pf3
  Scenario: Validate article view is displayed from different stories
    Given I visit "PF3 HomePage" page
    Then I should be on "PF3 HomePage" page
    When I click on the story
    Then I should see article view for the clicked story


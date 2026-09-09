Feature:This feature verifies the functionality of jump links within the PF3 application.
  It ensures that all jump links correctly navigate to their respective target sections or articles within the same or related pages.

  @pf3
  Scenario: This scenario validates that all jump links on the PF3 HomePage are functional and properly navigate the user to the corresponding sections or content.
    Given I visit "PF3 HomePage" page
    Then I should be on "PF3 HomePage" page
    And  I check the the Jumplinks are navigating or not
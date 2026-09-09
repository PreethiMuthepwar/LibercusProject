@ProductionView, @Smoke
Feature: This feature validates the Production View functionality in the Libercus CMS.
  It ensures that users can access and interact with the Production View to monitor, manage, and edit Rich Media Ads.

  @CMS
  Scenario:This scenario verifies that the Production View page in CMS functions correctly and displays Rich Media Ads when Ad edit mode is enabled.
    Given I visit "CMS Login" page
    When I log in to the Libercus application with valid user credentials
    Then I should be on "CMS HomePage" page
    Then I should see The Home text
    When I navigate to "Production View" from "Content" on "CMS HomePage"
    Then I should be on "CMS ProductionView" page
    And I select the customer-specific channel in "channelDropdown" dropdown
    And I click on "tabletView" element on "CMS ProductionView" page
    And I select the date which has rich media ads
    And I click on "update" element on "CMS ProductionView" page
    And I turn "On" rich media edit mode
    Then verify the Rich Ad panel is displayed
#    When I delete rich ads
#    Then i shouldn't see rich ad in the list

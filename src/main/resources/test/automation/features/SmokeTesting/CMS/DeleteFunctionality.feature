@Delete, @Smoke
Feature:This feature validates the Delete functionality in the Libercus CMS application for multiple content types — Stories, Interactive Ads, Rich Media Ads, and Print Pages.

  @CMS
  Scenario: This scenario verifies that users can delete an existing Story from the CMS Print Channel.
    Given I visit "CMS Login" page
    When I log in to the Libercus application with valid user credentials
    Then I should be on "CMS HomePage" page
    Then I should see The Home text
    When I navigate to "Stories" from "Content" on "CMS HomePage"
    Then I should be on "CMS StoryCreation" page
    Then I select the customer-specific channel in "channelDropdown" dropdown
    And I note the count of stories before delete
    And I navigate to the delete method with parameters
    Then I verify that the story has been deleted successfully

  @CMS
  Scenario: This scenario confirms that users can successfully delete an Interactive Ad within the CMS.
    Given I visit "CMS Login" page
    When I log in to the Libercus application with valid user credentials
    Then I should be on "CMS HomePage" page
    Then I should see The Home text
    When I navigate to "Interactive Ads" from "Content" on "CMS HomePage"
    Then I should be on "CMS InteractiveAds" page
    Then I select the customer-specific channel in "channelDropdown" dropdown
    And I note the count of Ad before delete
    And I Should delete the InterActiveAd
    Then I verify that the Interactive ad  has been deleted successfully

  @CMS
  Scenario: This scenario ensures that Rich Media Ads can be deleted from the CMS Print Channel.
    Given I visit "CMS Login" page
    When I log in to the Libercus application with valid user credentials
    Then I should be on "CMS HomePage" page
    Then I should see The Home text
    When I navigate to "Rich Media" from "Content" on "CMS HomePage"
    Then I should be on "CMS RichMediaAds" page
    Then I select the customer-specific channel in "channelDropdown" dropdown
    And I note the count of RichMediaAd before delete
    And I Should delete the RichMediaAd
    Then I verify that the RichMediaAd has been deleted successfully

  @CMS
  Scenario: This scenario validates that users can remove Print Pages from the CMS.
    Given I visit "CMS Login" page
    When I log in to the Libercus application with valid user credentials
    Then I should be on "CMS HomePage" page
    Then I should see The Home text
    When I navigate to "Print Pages" from "Content" on "CMS HomePage"
    Then I should be on "CMS PrintPages" page
    Then I select the customer-specific channel in "channelDropdown" dropdown
    And I note the count of Page before delete
    And I Should delete the Page
    Then I verify that the Page has been deleted successfully
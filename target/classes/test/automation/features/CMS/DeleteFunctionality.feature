@Delete
Feature:CMS - Delete Stories,PrintPages, RichMediaAds and Interactive ads

  @CMS
  Scenario: Delete of Stories
    Given I visit "CMS Login" page
    When I log in to the Libercus application with valid user credentials
    Then I should be on "CMS HomePage" page
    Then I should see The Home text
    When I navigate to "Stories" from "Content" on "CMS HomePage"
    Then I should be on "CMS StoryCreation" page
    And I select "PG Print" text in "channelDropdown" dropdown
    And I note the count of stories before delete
    And I navigate to the delete method with parameters
    Then I verify that the story has been deleted successfully

  @CMS
  Scenario: Delete of InteractiveAds
    Given I visit "CMS Login" page
    When I log in to the Libercus application with valid user credentials
    Then I should be on "CMS HomePage" page
    Then I should see The Home text
    When I navigate to "Interactive Ads" from "Content" on "CMS HomePage"
    Then I should be on "CMS InteractiveAds" page
    And I select "PG Print" text in "channelDropdown" dropdown
    And I note the count of Ad before delete
    And I Should delete the InterActiveAd
    Then I verify that the Interactive ad  has been deleted successfully

  @CMS
  Scenario: Delete of RichMediaAds
    Given I visit "CMS Login" page
    When I log in to the Libercus application with valid user credentials
    Then I should be on "CMS HomePage" page
    Then I should see The Home text
    When I navigate to "Rich Media" from "Content" on "CMS HomePage"
    Then I should be on "CMS RichMediaAds" page
    And I select "PG Print" text in "channelDropdown" dropdown
    And I note the count of RichMediaAd before delete
    And I Should delete the RichMediaAd
    Then I verify that the RichMediaAd has been deleted successfully

  @CMS
  Scenario: Delete of PrintPages
    Given I visit "CMS Login" page
    When I log in to the Libercus application with valid user credentials
    Then I should be on "CMS HomePage" page
    Then I should see The Home text
    When I navigate to "Print Pages" from "Content" on "CMS HomePage"
    Then I should be on "CMS PrintPages" page
    And I select "PG Print" text in "channelDropdown" dropdown
    And I note the count of Page before delete
    And I Should delete the Page
    Then I verify that the Page has been deleted successfully
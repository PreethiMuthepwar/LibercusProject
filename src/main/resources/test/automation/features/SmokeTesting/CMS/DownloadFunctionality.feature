@Download, @Smoke
Feature:This feature verifies the Download functionality in the Libercus CMS application for Interactive Ads and Rich Media Ads.

  @CMS, @Download
  Scenario: This scenario validates that a user can successfully download an Interactive Ad from the CMS.
    Given I visit "CMS Login" page
    When I log in to the Libercus application with valid user credentials
    Then I should be on "CMS HomePage" page
    Then I should see The Home text
    When I navigate to "Interactive Ads" from "Content" on "CMS HomePage"
    Then I should be on "CMS InteractiveAds" page
    Then I select the customer-specific channel in "channelDropdown" dropdown
    And I navigate to ad
    And I Should download the InteractiveAd
    Then I should verify the Ad is downloaded

  @CMS, @Download
  Scenario: This scenario verifies that a user can successfully download a Rich Media Ad from the CMS.
    Given I visit "CMS Login" page
    When I log in to the Libercus application with valid user credentials
    Then I should be on "CMS HomePage" page
    Then I should see The Home text
    When I navigate to "Rich Media" from "Content" on "CMS HomePage"
    Then I should be on "CMS RichMediaAds" page
    Then I select the customer-specific channel in "channelDropdown" dropdown
    And I navigate to RichMedia ad
    And I Should download the RichMedia Ad
    Then I should verify the Ad is downloaded
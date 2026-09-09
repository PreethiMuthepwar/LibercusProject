@Download
Feature:CMS- Download of interactive ads and richmedia ads

  @CMS
  Scenario: Download of InteractiveAds
    Given I visit "CMS Login" page
    When I log in to the Libercus application with valid user credentials
    Then I should be on "CMS HomePage" page
    Then I should see The Home text
    When I navigate to "Interactive Ads" from "Content" on "CMS HomePage"
    Then I should be on "CMS InteractiveAds" page
    And I select "PG Print" text in "channelDropdown" dropdown
    And I navigate to ad
    And I Should download the InteractiveAd
    Then I should verify the Ad is downloaded

  @CMS
  Scenario: Download of RichMediaAds
    Given I visit "CMS Login" page
    When I log in to the Libercus application with valid user credentials
    Then I should be on "CMS HomePage" page
    Then I should see The Home text
    When I navigate to "Rich Media" from "Content" on "CMS HomePage"
    Then I should be on "CMS RichMediaAds" page
    And I select "PG Print" text in "channelDropdown" dropdown
    And I navigate to RichMedia ad
    And I Should download the RichMedia Ad
    Then I should verify the Ad is downloaded
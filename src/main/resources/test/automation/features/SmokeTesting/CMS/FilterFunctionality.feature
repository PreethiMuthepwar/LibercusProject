@Filter, @Smoke
Feature: This feature validates the search and filter functionalities across various content modules in the Libercus CMS — including Stories, Media Files, Interactive Ads, Rich Media Ads, and Print Pages.
  It ensures that users can efficiently filter and search for content using different conditions such as Freetext Search, Section Letter, Device Type, and Ad Type, confirming that the CMS returns accurate and relevant results based on the selected filters.

  @CMS
  Scenario: This scenario verifies that users can perform a Search for stories in the CMS.
    Given I visit "CMS Login" page
    When I log in to the Libercus application with valid user credentials
    Then I should be on "CMS HomePage" page
    Then I should see The Home text
    When I navigate to "Stories" from "Content" on "CMS HomePage"
    Then I should be on "CMS StoryCreation" page
    Then I select the customer-specific channel in "channelDropdown" dropdown
    And I select a date that has stories
    And I select "Freetext Search" text in "selectSearchType" dropdown on "CMS StoryCreation" page
    And I enter text in search field
    Then Verify the search results are displayed

  @CMS
  Scenario: This scenario ensures that users can filter stories by Section Letter.
    Given I visit "CMS Login" page
    When I log in to the Libercus application with valid user credentials
    Then I should be on "CMS HomePage" page
    Then I should see The Home text
    When I navigate to "Stories" from "Content" on "CMS HomePage"
    Then I should be on "CMS StoryCreation" page
    Then I select the customer-specific channel in "channelDropdown" dropdown
    And I select a date that has stories
    And I select "A" text in "SectionLetter" dropdown on "CMS StoryCreation" page
    Then Verify the search results are displayed for section letter


  @CMS
  Scenario: This scenario confirms that users can search for Media Files using the Search option
    Given I visit "CMS Login" page
    When I log in to the Libercus application with valid user credentials
    Then I should be on "CMS HomePage" page
    Then I should see The Home text
    When I navigate to "Media Files" from "Content" on "CMS HomePage"
    Then I should be on "CMS MediaFiles" page
    And I select a date that has MediaFiles
    And I select "Freetext Search" text in "selectSearchType" dropdown on "CMS MediaFiles" page
    And I enter text in search field of MediaFiles
    Then Verify the search results are displayed for MediaFiles

  @CMS
  Scenario: This scenario validates that Interactive Ads can be filtered using Search.
    Given I visit "CMS Login" page
    When I log in to the Libercus application with valid user credentials
    Then I should be on "CMS HomePage" page
    Then I should see The Home text
    When I navigate to "Interactive Ads" from "Content" on "CMS HomePage"
    Then I should be on "CMS InteractiveAds" page
    Then I select the customer-specific channel in "channelDropdown" dropdown
    And I select a date that has Interactive Ad
    And I select "Freetext Search" text in "selectSearchType" dropdown
    And I enter text in search field of interactive ads
    Then Verify the search results are displayed for Interactive ads

  @CMS
  Scenario: This scenario ensures that users can filter Interactive Ads based on Device Type.
    Given I visit "CMS Login" page
    When I log in to the Libercus application with valid user credentials
    Then I should be on "CMS HomePage" page
    Then I should see The Home text
    When I navigate to "Interactive Ads" from "Content" on "CMS HomePage"
    Then I should be on "CMS InteractiveAds" page
    Then I select the customer-specific channel in "channelDropdown" dropdown
    And I select a date that has Interactive Ad
    And I select "Tablet" text in "deviceType" dropdown on "CMS InteractiveAds" page
    Then  verify that the "Tablet" checkbox is selected for Interactive Ads.

  @CMS
  Scenario:This scenario validates Search functionality for Rich Media Ads.
    Given I visit "CMS Login" page
    When I log in to the Libercus application with valid user credentials
    Then I should be on "CMS HomePage" page
    Then I should see The Home text
    When I navigate to "Rich Media" from "Content" on "CMS HomePage"
    Then I should be on "CMS RichMediaAds" page
    Then I select the customer-specific channel in "channelDropdown" dropdown
    And I select a date that has RichMedia Ad
    And I select "Freetext Search" text in "selectSearchType" dropdown
    And I enter text in search field of Richmedia ad
    Then Verify the search results are displayed for Richmedia ad

  @CMS
  Scenario:This scenario confirms that Rich Media Ads can be filtered by Device Type.
    Given I visit "CMS Login" page
    When I log in to the Libercus application with valid user credentials
    Then I should be on "CMS HomePage" page
    Then I should see The Home text
    When I navigate to "Rich Media" from "Content" on "CMS HomePage"
    Then I should be on "CMS RichMediaAds" page
    Then I select the customer-specific channel in "channelDropdown" dropdown
    And I select a date that has RichMedia Ad
    And I select "Tablet" text in "deviceType" dropdown on "CMS RichMediaAds" page
    Then verify that the "Tablet" checkbox is selected for RichMedia Ads.


  @CMS
  Scenario:This scenario verifies filtering of Rich Media Ads by Ad Type.
    Given I visit "CMS Login" page
    When I log in to the Libercus application with valid user credentials
    Then I should be on "CMS HomePage" page
    Then I should see The Home text
    When I navigate to "Rich Media" from "Content" on "CMS HomePage"
    Then I should be on "CMS RichMediaAds" page
    Then I select the customer-specific channel in "channelDropdown" dropdown
    And I select a date that has RichMedia Ad
    And I select "Lexigo" text in "adTypes" dropdown on "CMS RichMediaAds" page
    Then Verify the "Lexigo" is selected as adType

  @CMS
  Scenario: This scenario ensures that users can perform a Freetext Search within Print Pages.
    Given I visit "CMS Login" page
    When I log in to the Libercus application with valid user credentials
    Then I should be on "CMS HomePage" page
    Then I should see The Home text
    When I navigate to "Print Pages" from "Content" on "CMS HomePage"
    Then I should be on "CMS PrintPages" page
    Then I select the customer-specific channel in "channelDropdown" dropdown
    And I select a date that has pages
    And I select "Freetext Search" text in "selectSearchType" dropdown on "CMS PrintPages" page
    And I enter text in search field of Printpages
    Then Verify the search results are displayed for Printpages

  @CMS
  Scenario: This scenario validates that users can filter Print Pages by Section Letter.
    Given I visit "CMS Login" page
    When I log in to the Libercus application with valid user credentials
    Then I should be on "CMS HomePage" page
    Then I should see The Home text
    When I navigate to "Print Pages" from "Content" on "CMS HomePage"
    Then I should be on "CMS PrintPages" page
    Then I select the customer-specific channel in "channelDropdown" dropdown
    And I select a date that has pages
    And I select "A" text in "SectionLetterName" dropdown on "CMS PrintPages" page
    Then Verify the search results are displayed for Printpages on section letter

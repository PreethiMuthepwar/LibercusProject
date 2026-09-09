@Filter
Feature: Filter Conditions

  @CMS
  Scenario: Story search results
    Given I visit "CMS Login" page
    When I log in to the Libercus application with valid user credentials
    Then I should be on "CMS HomePage" page
    Then I should see The Home text
    When I navigate to "Stories" from "Content" on "CMS HomePage"
    Then I should be on "CMS StoryCreation" page
    And I select "PG Print" text in "channelDropdown" dropdown on "CMS StoryCreation" page
    And I select a date that has stories
    And I select "Freetext Search" text in "selectSearchType" dropdown on "CMS StoryCreation" page
    And I enter text in search field
    Then Verify the search results are displayed

  @CMS
  Scenario: Story section search results
    Given I visit "CMS Login" page
    When I log in to the Libercus application with valid user credentials
    Then I should be on "CMS HomePage" page
    Then I should see The Home text
    When I navigate to "Stories" from "Content" on "CMS HomePage"
    Then I should be on "CMS StoryCreation" page
    And I select "PG Print" text in "channelDropdown" dropdown on "CMS StoryCreation" page
    And I select a date that has stories
    And I select "A" text in "SectionLetter" dropdown on "CMS StoryCreation" page
    Then Verify the search results are displayed for section letter


  @CMS
  Scenario: MediaFiles search results
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
  Scenario: Interactive ads search results
    Given I visit "CMS Login" page
    When I log in to the Libercus application with valid user credentials
    Then I should be on "CMS HomePage" page
    Then I should see The Home text
    When I navigate to "Interactive Ads" from "Content" on "CMS HomePage"
    Then I should be on "CMS InteractiveAds" page
    And I select "PG Print" text in "channelDropdown" dropdown on "CMS InteractiveAds" page
    And I select a date that has Interactive Ad
    And I select "Freetext Search" text in "selectSearchType" dropdown
    And I enter text in search field of interactive ads
    Then Verify the search results are displayed for Interactive ads

  @CMS
  Scenario: Interactive ads device search results
    Given I visit "CMS Login" page
    When I log in to the Libercus application with valid user credentials
    Then I should be on "CMS HomePage" page
    Then I should see The Home text
    When I navigate to "Interactive Ads" from "Content" on "CMS HomePage"
    Then I should be on "CMS InteractiveAds" page
    And I select "PG Print" text in "channelDropdown" dropdown on "CMS InteractiveAds" page
    And I select a date that has Interactive Ad
    And I select "Tablet" text in "deviceType" dropdown on "CMS InteractiveAds" page
    Then  verify that the "Tablet" checkbox is selected for Interactive Ads.

  @CMS
  Scenario:RichMedia Ad search results
    Given I visit "CMS Login" page
    When I log in to the Libercus application with valid user credentials
    Then I should be on "CMS HomePage" page
    Then I should see The Home text
    When I navigate to "Rich Media" from "Content" on "CMS HomePage"
    Then I should be on "CMS RichMediaAds" page
    And I select "PG Print" text in "channelDropdown" dropdown on "CMS RichMediaAds" page
    And I select a date that has RichMedia Ad
    And I select "Freetext Search" text in "selectSearchType" dropdown
    And I enter text in search field of Richmedia ad
    Then Verify the search results are displayed for Richmedia ad

  @CMS
  Scenario:RichMedia Ad device search results
    Given I visit "CMS Login" page
    When I log in to the Libercus application with valid user credentials
    Then I should be on "CMS HomePage" page
    Then I should see The Home text
    When I navigate to "Rich Media" from "Content" on "CMS HomePage"
    Then I should be on "CMS RichMediaAds" page
    And I select "PG Print" text in "channelDropdown" dropdown on "CMS RichMediaAds" page
    And I select a date that has RichMedia Ad
    And I select "Tablet" text in "deviceType" dropdown on "CMS RichMediaAds" page
    Then verify that the "Tablet" checkbox is selected for RichMedia Ads.


  @CMS
  Scenario:RichMedia Ad type search results
    Given I visit "CMS Login" page
    When I log in to the Libercus application with valid user credentials
    Then I should be on "CMS HomePage" page
    Then I should see The Home text
    When I navigate to "Rich Media" from "Content" on "CMS HomePage"
    Then I should be on "CMS RichMediaAds" page
    And I select "PG Print" text in "channelDropdown" dropdown on "CMS RichMediaAds" page
    And I select a date that has RichMedia Ad
    And I select "Lexigo" text in "adTypes" dropdown on "CMS RichMediaAds" page
    Then Verify the "Lexigo" is selected as adType

  @CMS
  Scenario: Page search results
    Given I visit "CMS Login" page
    When I log in to the Libercus application with valid user credentials
    Then I should be on "CMS HomePage" page
    Then I should see The Home text
    When I navigate to "Print Pages" from "Content" on "CMS HomePage"
    Then I should be on "CMS PrintPages" page
    And I select "PG Print" text in "channelDropdown" dropdown on "CMS PrintPages" page
    And I select a date that has pages
    And I select "Freetext Search" text in "selectSearchType" dropdown on "CMS PrintPages" page
    And I enter text in search field of Printpages
    Then Verify the search results are displayed for Printpages

  @CMS
  Scenario: Page Section letter search results
    Given I visit "CMS Login" page
    When I log in to the Libercus application with valid user credentials
    Then I should be on "CMS HomePage" page
    Then I should see The Home text
    When I navigate to "Print Pages" from "Content" on "CMS HomePage"
    Then I should be on "CMS PrintPages" page
    And I select "PG Print" text in "channelDropdown" dropdown on "CMS PrintPages" page
    And I select a date that has pages
    And I select "A" text in "SectionLetterName" dropdown on "CMS PrintPages" page
    Then Verify the search results are displayed for Printpages on section letter

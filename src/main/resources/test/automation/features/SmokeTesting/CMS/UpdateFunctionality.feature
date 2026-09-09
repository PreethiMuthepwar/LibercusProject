@update@Smoke
Feature: This feature validates the update functionality in the Libercus CMS application for multiple content types — Stories, Interactive Ads, Rich Media Ads, Media Files, and Print Pages.
  It ensures that users can modify existing content fields, save the changes, and verify that updates are reflected correctly in the system.
  @CMS
  Scenario: This scenario verifies that a user can successfully update an existing Story in the CMS.
    Given I visit "CMS Login" page
    When I log in to the Libercus application with valid user credentials
    Then I should be on "CMS HomePage" page
    Then I should see The Home text
    When I navigate to "Stories" from "Content" on "CMS HomePage"
    Then I should be on "CMS StoryCreation" page
    And I select the customer-specific channel in "channelDropdown" dropdown
    And I select a date that has stories
    And I open any available story
    When I update the "Title" field of the story
    And  I click save and close button on "CMS StoryCreation"
#    And I click on save and close button
#    Then I should verify that the story has been updated successfully

  @CMS
  Scenario: This scenario validates that users can update details of an Interactive Ad in the CMS.
    Given I visit "CMS Login" page
    When I log in to the Libercus application with valid user credentials
    Then I should be on "CMS HomePage" page
    Then I should see The Home text
    When I navigate to "Interactive Ads" from "Content" on "CMS HomePage"
    Then I should be on "CMS InteractiveAds" page
    And I select the customer-specific channel in "channelDropdown" dropdown
    And I select a date that has Interactive Ad
    And I open any available Interactive Ad
    When I update the "title" field of the Interactive Ad
    And I click save and close button on "CMS InteractiveAds"
    Then I should verify that the "title" field of the interactiveAd has been updated successfully
  @CMS
  Scenario: This scenario confirms that users can edit and update Rich Media Ads within the CMS.
    Given I visit "CMS Login" page
    When I log in to the Libercus application with valid user credentials
    Then I should be on "CMS HomePage" page
    Then I should see The Home text
    When I navigate to "Rich Media" from "Content" on "CMS HomePage"
    Then I should be on "CMS RichMediaAds" page
    And I select the customer-specific channel in "channelDropdown" dropdown
    And I select a date that has RichMedia Ad
    And I open any available RichMedia Ad
    When I update the "title" field of the RichMedia Ad
    Then I should verify that the "title" field of the RichMedia has been updated successfully
  @CMS
  Scenario: This scenario ensures that users can update metadata for Media Files in the CMS.
    Given I visit "CMS Login" page
    When I log in to the Libercus application with valid user credentials
    Then I should be on "CMS HomePage" page
    Then I should see The Home text
    When I navigate to "Media Files" from "Content" on "CMS HomePage"
    Then I should be on "CMS MediaFiles" page
    And I select a date that has MediaFiles
    And I open any available MediaFile
    When I update the "title" field of the MediaFile
    And  I click save and close button on "CMS MediaFiles"
    Then I should verify that the "title" field of the MediaFile has been updated successfully

  @CMS
  Scenario:This scenario validates that a user can update Print Page details in the CMS.
    Given I visit "CMS Login" page
    When I log in to the Libercus application with valid user credentials
    Then I should be on "CMS HomePage" page
    Then I should see The Home text
    When I navigate to "Print Pages" from "Content" on "CMS HomePage"
    Then I should be on "CMS PrintPages" page
    And I select the customer-specific channel in "channelDropdown" dropdown
    And I select a date that has pages
    And I open any available Page
    When I update the "SectionLetter" in Printpages
    Then I should verify that the "SectionLetter" has been updated successfully
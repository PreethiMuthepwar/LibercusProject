Feature: This feature validates the 'Use' functionality for wire stories and wire photos in the Libercus CMS.
  @CMS,@Regression
  Scenario:This scenario verifies that a wire story can be used and published in the CMS.
    Given I visit "CMS Login" page
    When I log in to the Libercus application with valid user credentials
    Then I should be on "CMS HomePage" page
    Then I should see The Home text
    When I navigate to "Wire Stories" from "Feeds" on "CMS HomePage"
    Then I should be on "CMS WireStories" page
    And I select "Last Month" text in "TransmittedDateRange" dropdown
    And I should check whether the stories are available.
    And I select a story
    When I click on "Use" element
#    Then I should see the Use Story popup
    When I enter the publish date for story
    And I select the customer-specific channel in "channelDropdown" dropdown
#    And I select "PG Print" text in "channels" dropdown
    And I select the checkbox for Omit photos
    Then I tap on the use button
    When I navigate to "Stories" from "Content" on "CMS HomePage"
    Then I should be on "CMS StoryCreation" page
    And I select "PG Print" text in "channelDropdown" dropdown
    And I select a date for "Stories"
    And I select "Freetext Search" text in "selectSearchType" dropdown
    And I enter  slug in the search box
    Then Verify the search results are displayed
  @CMS,@Regression
  Scenario:This scenario validates that a wire photo can be used and published in the CMS.
    Given I visit "CMS Login" page
    When I log in to the Libercus application with valid user credentials
    Then I should be on "CMS HomePage" page
    Then I should see The Home text
    When I navigate to "Wire Photos" from "Feeds" on "CMS HomePage"
    And I select "Last Month" text in "TransmittedDateRange" dropdown on "CMS WireStories" page
    Then I should be on "CMS WirePhotos" page
    And I should check whether the photos are available.
    And I select a Media file
    When I click on "Use" element
    When I enter the publish date for image on "CMS WireStories" page
    Then I tap on the use button
    When I navigate to "Media Files" from "Content" on "CMS HomePage"
    And I select a date for "MediaFiles"
    And I select "Freetext Search" text in "selectSearchType" dropdown on "CMS MediaFiles" page
    And I enter title in search field of MediaFiles
    Then Verify the search results are displayed for MediaFiles
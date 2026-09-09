@Copy, @Smoke
Feature:This feature validates the Copy functionality in the Libercus CMS application for various content types — Stories, Interactive Ads, Rich Media Ads, and Print Pages
  @CMS
  Scenario: This scenario verifies that the user can successfully copy an existing Story from the selected publish date in the CMS Print Channel. It checks the story count before and after copying to ensure a new story entry is created.
    Given I visit "CMS Login" page
    When I log in to the Libercus application with valid user credentials
    Then I should be on "CMS HomePage" page
    Then I should see The Home text
    When I navigate to "Stories" from "Content" on "CMS HomePage"
    Then I should be on "CMS StoryCreation" page
    Then I select the customer-specific channel in "channelDropdown" dropdown
    When I Note the count of stories before the copy of stories
    And I select a date that has stories
    And I navigate to the CopyOfStories method with parameters
    Then I verify that the story has been copied successfully

  @CMS
  Scenario: This scenario ensures that the Interactive Ads allows users to duplicate existing ads.
    Given I visit "CMS Login" page
    When I log in to the Libercus application with valid user credentials
    Then I should be on "CMS HomePage" page
    And I should see The Home text
    When I navigate to "Interactive Ads" from "Content" on "CMS HomePage"
    Then I should be on "CMS InteractiveAds" page
    When I select the customer-specific channel in "channelDropdown" dropdown
    And I note the count of InteractiveAds before the copy
    And I select a date that has Interactive Ad
    When I navigate to the CopyOfInteractive method with parameters
    Then I verify that the Interactive ad has been copied successfully

  @CMS
  Scenario: This scenario confirms that users can copy Rich Media Ads from a given publish date.
    Given I visit "CMS Login" page
    When I log in to the Libercus application with valid user credentials
    Then I should be on "CMS HomePage" page
    And I should see The Home text
    When I navigate to "Rich Media" from "Content" on "CMS HomePage"
    Then I should be on "CMS RichMediaAds" page
    When I select the customer-specific channel in "channelDropdown" dropdown
    And I note the count of RichMedia before the copy
    And I select a date that has RichMedia Ad
    When I navigate to the CopyOfRichMedia method with parameters
    Then I verify that the RichMedia ad has been copied successfully

  @CMS
  Scenario: This scenario validates that Print Pages within the CMS can be successfully duplicated.
    Given I visit "CMS Login" page
    When I log in to the Libercus application with valid user credentials
    Then I should be on "CMS HomePage" page
    Then I should see The Home text
    When I navigate to "Print Pages" from "Content" on "CMS HomePage"
    Then I should be on "CMS PrintPages" page
    When I select the customer-specific channel in "channelDropdown" dropdown
    And I Note the count of pages before the copy of page
    And I select a date that has pages
    And I navigate to the CopyOfPage method with parameters
#    Then I verify that the page has been copied successfully
@Copy
Feature:CMS Copy of Stories, Interactive ads, RichMedia Ads, Pages

  @CMS
  Scenario: Copy of Stories
    Given I visit "CMS Login" page
    When I log in to the Libercus application with valid user credentials
    Then I should be on "CMS HomePage" page
    Then I should see The Home text
    When I navigate to "Stories" from "Content" on "CMS HomePage"
    Then I should be on "CMS StoryCreation" page
    And I select "PG Print" text in "channelDropdown" dropdown on "CMS StoryCreation" page
    When I Note the count of stories before the copy of stories
    And I select a date that has stories
    And I navigate to the CopyOfStories method with parameters
    Then I verify that the story has been copied successfully

  @CMS
  Scenario: Copy of Interactive Ads
    Given I visit "CMS Login" page
    When I log in to the Libercus application with valid user credentials
    Then I should be on "CMS HomePage" page
    And I should see The Home text
    When I navigate to "Interactive Ads" from "Content" on "CMS HomePage"
    Then I should be on "CMS InteractiveAds" page
    And I select "PG Print" text in "channelDropdown" dropdown on "CMS InteractiveAds" page
    And I note the count of InteractiveAds before the copy
    And I select a date that has Interactive Ad
    When I navigate to the CopyOfInteractive method with parameters
    Then I verify that the Interactive ad has been copied successfully

  @CMS
  Scenario: Copy of RichMedia Ads
    Given I visit "CMS Login" page
    When I log in to the Libercus application with valid user credentials
    Then I should be on "CMS HomePage" page
    And I should see The Home text
    When I navigate to "Rich Media" from "Content" on "CMS HomePage"
    Then I should be on "CMS RichMediaAds" page
    And I select "PG Print" text in "channelDropdown" dropdown on "CMS RichMediaAds" page
    And I note the count of RichMedia before the copy
    And I select a date that has RichMedia Ad
    When I navigate to the CopyOfRichMedia method with parameters
    Then I verify that the RichMedia ad has been copied successfully

  @CMS
  Scenario: Copy of Pages
    Given I visit "CMS Login" page
    When I log in to the Libercus application with valid user credentials
    Then I should be on "CMS HomePage" page
    Then I should see The Home text
    When I navigate to "Print Pages" from "Content" on "CMS HomePage"
    Then I should be on "CMS PrintPages" page
    And I select "PG Print" text in "channelDropdown" dropdown on "CMS PrintPages" page
    And I Note the count of pages before the copy of page
    And I select a date that has pages
    And I navigate to the CopyOfPage method with parameters
    Then I verify that the page has been copied successfully
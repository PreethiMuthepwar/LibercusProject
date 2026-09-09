#@update
#Feature: update of stories, pages, interactive ads and richmedia ads
#
#  @CMS
#  Scenario: Story update
#    Given I visit "CMS Login" page
#    When I log in to the Libercus application with valid user credentials
#    Then I should be on "CMS HomePage" page
#    Then I should see The Home text
#    When I navigate to "Stories" from "Content" on "CMS HomePage"
#    Then I should be on "CMS StoryCreation" page
#    And I select "PG Print" text in "channelDropdown" dropdown on "CMS StoryCreation" page
#    And I select a date that has stories
#    And I open any available story
#    When I update the "Title" field of the story
##    And  I click save and close button on "CMS StoryCreation"
##    And I click on save and close button
#    Then I should verify that the story has been updated successfully
#
#  @CMS
#  Scenario: update an Interactive Ad
#    Given I visit "CMS Login" page
#    When I log in to the Libercus application with valid user credentials
#    Then I should be on "CMS HomePage" page
#    Then I should see The Home text
#    When I navigate to "Interactive Ads" from "Content" on "CMS HomePage"
#    Then I should be on "CMS InteractiveAds" page
#    And I select "PG Print" text in "channelDropdown" dropdown on "CMS InteractiveAds" page
#    And I select a date that has Interactive Ad
#    And I open any available Interactive Ad
#    When I update the "title" field of the Interactive Ad
#    And I click save and close button on "CMS InteractiveAds"
#    Then I should verify that the "title" field of the interactiveAd has been updated successfully
#  @CMS
#  Scenario: update an RichMedia Ad
#    Given I visit "CMS Login" page
#    When I log in to the Libercus application with valid user credentials
#    Then I should be on "CMS HomePage" page
#    Then I should see The Home text
#    When I navigate to "Rich Media" from "Content" on "CMS HomePage"
#    Then I should be on "CMS RichMediaAds" page
#    And I select "PG Print" text in "channelDropdown" dropdown on "CMS RichMediaAds" page
#    And I select a date that has RichMedia Ad
#    And I open any available RichMedia Ad
#    When I update the "title" field of the RichMedia Ad
#    And  I click save and close button on "CMS RichMediaAds"
#    Then I should verify that the "title" field of the RichMedia has been updated successfully
#  @CMS
#  Scenario: update of an Media File
#    Given I visit "CMS Login" page
#    When I log in to the Libercus application with valid user credentials
#    Then I should be on "CMS HomePage" page
#    Then I should see The Home text
#    When I navigate to "Media Files" from "Content" on "CMS HomePage"
#    Then I should be on "CMS MediaFiles" page
#    And I select a date that has MediaFiles
#    And I open any available MediaFile
#    When I update the "title" field of the MediaFile
#    And  I click save and close button on "CMS MediaFiles"
#    Then I should verify that the "title" field of the MediaFile has been updated successfully
#
#  @CMS
#  Scenario:Update of a Page
#    Given I visit "CMS Login" page
#    When I log in to the Libercus application with valid user credentials
#    Then I should be on "CMS HomePage" page
#    Then I should see The Home text
#    When I navigate to "Print Pages" from "Content" on "CMS HomePage"
#    Then I should be on "CMS PrintPages" page
#    And I select "PG Print" text in "channelDropdown" dropdown on "CMS PrintPages" page
#    And I select a date that has pages
#    And I open any available Page
#    When I update the "SectionLetter" in Printpages
#    Then I should verify that the "SectionLetter" has been updated successfully

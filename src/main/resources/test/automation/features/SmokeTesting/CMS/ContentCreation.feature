@Creation, @Smoke
Feature:This feature automates and validates the end-to-end content creation workflows in the Libercus CMS platform for the Print Channel.
  It covers:
  Story creation
  Interactive Ad creation
  Rich Media Ad creation
  Print Ad creation
  Page creation (including adding stories to a print page layout)
  Media file creation

  @CMS
  Scenario Outline:This scenario verifies that a user can successfully navigate to the Story Creation page in the CMS Print Channel, fill in the required story metadata and content fields (like slug, status, publish date, kicker, title, byline, story content, and images), and save the story.
    Given I visit "CMS Login" page
    When I log in to the Libercus application with valid user credentials
    Then I should be on "CMS HomePage" page
    Then I should see The Home text
    When I navigate to "Stories" from "Content" on "CMS HomePage"
    Then I should be on "CMS StoryCreation" page
    Then I select the customer-specific channel in "channelDropdown" dropdown
    And I click on "newButton" element for "<Nth story>"
    Then I should see following elements:
      | slug   |
      | status |
    When I enter the publish date, status, slug, and shape for "<Nth story>"
    And I switch to tab on "CMS StoryCreation" page
#    And I switch to "PG Print" tab on "CMS StoryCreation" page
    And I enter the Kicker,Title,Byline,Story for "<Nth story>"
    And I add an image to the story if it contains an image for "<Nth story>"
    And  I click save and close button on "CMS StoryCreation"
    Examples:
      | Nth story |
      | story 1   |
#      | story 2   |
#      | story 3   |
#      | story 4   |
#      | story 5   |

  @CMS
  Scenario Outline: This scenario validates the creation process of an Interactive Ad in the Print Channel. It ensures that the user can set publication details, select status, upload images, attach ZIP files, and successfully save the ad.
    Given I visit "CMS Login" page
    When I log in to the Libercus application with valid user credentials
    Then I should be on "CMS HomePage" page
    Then I should see The Home text
    When I navigate to "Interactive Ads" from "Content" on "CMS HomePage"
    Then I should be on "CMS InteractiveAds" page
    When I select the customer-specific channel in "channelDropdown" dropdown
#    When I select "PG Print" text in "channelDropdown" dropdown on "CMS InteractiveAds" page
    And I click on "newButton" element for "<Nth interactiveAds>"
    Then Interactive Ad creation page should be displayed
    When I enter the publish date for the interactive ad
    And I select "Published" text in "statusDropdown" dropdown on "CMS InteractiveAds" page
    And I enter the Ad Type
    And I enter the Title,slug for the interactive ad
    And I select all checkboxes
    And I upload an image for "<Nth interactiveAds>"
    Then I verify that the image is uploaded successfully
    When I upload a ZIP file for "<Nth interactiveAds>"
    Then I verify that the ZIP file is uploaded successfully
    And  I click save and close button on "CMS InteractiveAds"
    Examples:
      | Nth interactiveAds |
      | interactiveAd1     |
#      | interactiveAd2     |
#      | interactiveAd3     |

  @CMS
  Scenario Outline: This scenario tests the workflow for creating a Rich Media Ad in the CMS.It verifies ad metadata entry, upload functionality, and publication status setup.
    Given I visit "CMS Login" page
    When I log in to the Libercus application with valid user credentials
    Then I should be on "CMS HomePage" page
    Then I should see The Home text
    When I navigate to "Rich Media" from "Content" on "CMS HomePage"
    Then I should be on "CMS RichMediaAds" page
    And I select the customer-specific channel in "channelDropdown" dropdown
#    And I select "PG Print" text in "channelDropdown" dropdown on "CMS RichMediaAds" page
    And I click on "newButton" element for "<Nth RichMediaAds>"
    Then RichMedia Ad creation page should be displayed
    When I enter the publish date for the RichMedia Ad
    And I select "Published" text in "statusDropdown" dropdown on "CMS RichMediaAds" page
    And I enter the Title,Slug for the RichMedia Ad
    And I enter the AdType for the RichMedia Ad
    And I select all checkboxes of RichMedia Ads
    And I upload an image for rich media ads "<Nth RichMediaAds>"
    Then I verify that the image is uploaded successfully for the RichMedia Ad
    When I upload a ZIP file for rich media ads "<Nth RichMediaAds>"
    Then I verify that the ZIP file is uploaded successfully for the RichMedia Ad
    And  I click save and close button on "CMS RichMediaAds"
    Examples:
      | Nth RichMediaAds |
      | RichMediaAd1     |
#      | RichMediaAd2     |
#      | RichMediaAd3     |
#      | RichMediaAd4     |

  @CMS
  Scenario: This scenario ensures that a user can create a Print Ad in the CMS for the PG Print channel. It tests the upload functionality of PDF.
    Given I visit "CMS Login" page
    When I log in to the Libercus application with valid user credentials
    Then I should be on "CMS HomePage" page
    Then I should see The Home text
    When I navigate to "Print Ads" from "Content" on "CMS HomePage"
    Then I should be on "CMS PrintAds" page
    And I select the customer-specific channel in "channelDropdown" dropdown
#    And I select "PG Print" text in "channelDropdown" dropdown on "CMS PrintAds" page
    And I click on "newButton" element on "CMS PrintAds" page
    Then I should see "statusDropdown" element
    When I enter the publish date for the PrintAd
    And I select "Published" text in "statusDropdown" dropdown on "CMS PrintAds" page
    And I type "PrintAds" in "adNameInputField" text box on "CMS PrintAds" page
    When I upload the PDF file for the PrintAd
    Then I verify that the PDF file is uploaded successfully for the PrintAd
    And  I click save and close button on "CMS PrintAds"

  @CMS
  Scenario Outline:This scenario verifies that a user can create a Print Page, assign a template, and add existing stories to the page layout.
    Given I visit "CMS Login" page
    When I log in to the Libercus application with valid user credentials
    Then I should be on "CMS HomePage" page
    Then I should see The Home text
    When I navigate to "Print Pages" from "Content" on "CMS HomePage"
    Then I should be on "CMS PrintPages" page
    Then I select the customer-specific channel in "channelDropdown" dropdown
#    When I select "PG Print" text in "channelDropdown" dropdown on "CMS PrintPages" page
    And I click on "newButton" element
    Then Creation page should be displayed
    When I enter the publish date for the page
    And I select text in "statusDropdown" dropdown on "CMS PrintPages" page
#    And I select "Done" text in "statusDropdown" dropdown on "CMS PrintPages" page
    And I type "A" in "SectionLetter" text box on "CMS PrintPages" page
    And I type PageNumber in text box "<Nth Page>"
    And I select template text in "templeteDropdown" dropdown on "CMS PrintPages" page
#    And I select "6 col Inside Left NEW FONTS" text in "templeteDropdown" dropdown on "CMS PrintPages" page
    And I tap on Layout
    Then the Layout page should be displayed
    When I click on "addButton" element on "CMS PrintPages" page
#    When I click on "" element on "CMS PrintPages" page
    And I select "Last 3 Months" text in "publishDateDropDown" dropdown on "CMS PrintPages" page
    And I click on "updateButtonOnPanel" element
#    And I click on "selectStory" element
    And I select a story from the stories tab
    And I click on "saveStory" element on "CMS PrintPages" page
    And Drag and drop the story on page and adjust the story
    And  I click save and close button on "CMS PrintPages"
    Examples:
      | Nth Page |
      | 1        |
#      | 2        |
#      | 3        |
#      | 4        |

  @CMS
  Scenario: This scenario ensures the successful creation of Media Files within the CMS. It validates metadata entry, image upload functionality, and creation confirmation.
    Given I visit "CMS Login" page
    When I log in to the Libercus application with valid user credentials
    Then I should be on "CMS HomePage" page
    Then I should see The Home text
    When I navigate to "Media Files" from "Content" on "CMS HomePage"
    Then I should be on "CMS MediaFiles" page
    When I click on "newButton" element
#    Then I should see "usages" element
    When I enter the publish date,Title,Caption for the media files
    And I upload an image for Media files
    Then I verify that the image is uploaded successfully for the media files
    And  I click save and close button on "CMS MediaFiles"
    And Verify that the media file is created
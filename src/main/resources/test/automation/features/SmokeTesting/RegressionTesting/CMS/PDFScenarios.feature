Feature: This feature validates that users can create Print Pages, add Stories and Print Ads, configure page layouts, and generate PDFproofs.
  @CMS,@Regression
  Scenario:This scenario verifies the process of creating a Print Page using a specific template and generating a PDF proof.
    Given I visit "CMS Login" page
    When I log in to the Libercus application with valid user credentials
    Then I should be on "CMS HomePage" page
    Then I should see The Home text
    When I navigate to "Print Pages" from "Content" on "CMS HomePage"
    Then I should be on "CMS PrintPages" page
    And I select the customer-specific channel in "channelDropdown" dropdown
#    When I select "PG Print" text in "channelDropdown" dropdown on "CMS PrintPages" page
    And I click on "newButton" element
    And I enter the publish date for the page
    And I select "Done" text in "statusDropdown" dropdown on "CMS PrintPages" page
    And I type "A" in "SectionLetter" text box on "CMS PrintPages" page
    And I type PageNumber in text box "2"
  #  And I select "WK | DINE" text in "templeteDropdown" dropdown on "CMS PrintPages" page
    And I select "BIZ_Sunday cover NEW FONTS" text in "templeteDropdown" dropdown on "CMS PrintPages" page
    And I tap on Layout
    Then the Layout page should be displayed
#    When I click on "addButton" element on "CMS PrintPages" page
#    When I click on "publishDate" element on "CMS PrintPages" page
#    And I select a date that has story
#    And I click on "updateButtonOnPanel" element
#    And I click on "selectStory" element
#    And I click on "saveStory" element on "CMS PrintPages" page
#    Then Drag and drop the story on page and adjust the story
    When  I click save button on "CMS PrintPages"
    And I click on "output" element on "CMS OutputOfPrintPages" page
    And I select PDF Proof to desktop and i tap on ok button
    Then A dialog  will open once complete popup should be displayed
    When Your pdf is ready popup should be displayed
    And Tap on click this link to open pdf
    When I click on "popupCloseButton" element on "CMS PrintPages" page
    Then  I click save and close button on "CMS PrintPages"

  @CMS,@Regression
  Scenario: This scenario validates the creation of a Print Ad in the CMS.
    Given I visit "CMS Login" page
    When I log in to the Libercus application with valid user credentials
    Then I should be on "CMS HomePage" page
    Then I should see The Home text
    When I navigate to "Print Ads" from "Content" on "CMS HomePage"
    Then I should be on "CMS PrintAds" page
    And I select the customer-specific channel in "channelDropdown" dropdown
    And I click on "newButton" element on "CMS PrintAds" page
    Then I should see "statusDropdown" element
    And I select "Published" text in "statusDropdown" dropdown on "CMS PrintAds" page
    And I type "PrintAds" in "adNameInputField" text box on "CMS PrintAds" page
    When I upload the PDF file for the PrintAd
    Then I verify that the PDF file is uploaded successfully for the PrintAd
    And  I click save and close button on "CMS PrintAds"

  @CMS,@Regression
  Scenario:This scenario verifies that a created Print Ad can be added to a Print Page.
    Given I visit "CMS Login" page
    When I log in to the Libercus application with valid user credentials
    Then I should be on "CMS HomePage" page
    Then I should see The Home text
    When I navigate to "Print Pages" from "Content" on "CMS HomePage"
    Then I should be on "CMS PrintPages" page
    And I select the customer-specific channel in "channelDropdown" dropdown
    And I click on "newButton" element
    And I enter the publish date for the page
    And I select "Done" text in "statusDropdown" dropdown on "CMS PrintPages" page
    And I type "A" in "SectionLetter" text box on "CMS PrintPages" page
    And I type PageNumber in text box "2"
    And I select "6 col Inside Left NEW FONTS" text in "templeteDropdown" dropdown on "CMS PrintPages" page
    And I tap on Layout
    Then the Layout page should be displayed
    When I turn on the Ad Edit Mode
    And I click on "PrintAdsAddButton" element on "CMS PrintPages" page
    And I click on "selectAd" element
    And I click on "saveStory" element on "CMS PrintPages" page
    And Drag and drop the ad on page and adjust the ad
    And  I click save and close button on "CMS PrintPages"
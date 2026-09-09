@OutputOfPages, @Smoke
Feature: This feature verifies the Output functionality of Print Pages in the Libercus CMS.
  It ensures that users can successfully generate and download PDF proofs of pages from the CMS.
  @CMS
  Scenario:This scenario validates that a user can generate the PDF output for an existing print page in the CMS.
    Given I visit "CMS Login" page
    When I log in to the Libercus application with valid user credentials
    Then I should be on "CMS HomePage" page
    Then I should see The Home text
    When I navigate to "Print Pages" from "Content" on "CMS HomePage"
    Then I should be on "CMS PrintPages" page
    Then I select the customer-specific channel in "channelDropdown" dropdown
    And I select the date that has edition
    And Open any existing page
    Then I should see "pageLayout" element
    And I tap on Layout
    Then the Layout page should be displayed
    And I click on "output" element on "CMS OutputOfPrintPages" page
    And I select PDF Proof to desktop and i tap on ok button
    Then A dialog  will open once complete popup should be displayed
    When Your pdf is ready popup should be displayed
    And Tap on click this link to open pdf
    When I click on "popupCloseButton" element on "CMS PrintPages" page
    Then  I click save and close button on "CMS PrintPages"
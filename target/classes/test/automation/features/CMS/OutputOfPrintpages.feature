@OutputOfPages
Feature: CMS - Output of pages

  @CMS
  Scenario:output of pages
    Given I visit "CMS Login" page
    When I log in to the Libercus application with valid user credentials
    Then I should be on "CMS HomePage" page
    Then I should see The Home text
    When I navigate to "Print Pages" from "Content" on "CMS HomePage"
    Then I should be on "CMS PrintPages" page
    When I select "PG Print" text in "channelDropdown" dropdown on "CMS PrintPages" page
    And I select the date that has edition
    And Open any existing page
    Then I should see "pageLayout" element
    And I tap on Layout
    Then the Layout page should be displayed
    And I click on "output" element on "CMS OutputOfPrintPages" page
    And I select PDF Proof to desktop and i tap on ok button
    And A dilagouge will open once complete popup should be displayed
    And Your pdf is ready popup should be displayed
    And Tap on click this link to open pdf




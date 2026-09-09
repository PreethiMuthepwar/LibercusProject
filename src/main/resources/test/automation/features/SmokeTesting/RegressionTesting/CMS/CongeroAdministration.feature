Feature: This feature validates the data creation functionality in the Congero Administration module of the Libercus CMS. It ensures that users can successfully create and configure Lookup Data and Congero Types, including defining fields, selecting options, and saving the entries.
  @CMS,@Regression
  Scenario: This scenario verifies that a user can create a Lookup Data entry in the Congero Administration module.
    Given I visit "CMS Login" page
    When I log in to the Libercus application with valid user credentials
    Then I should be on "CMS HomePage" page
    Then I should see The Home text
    When I navigate to "Lookup Data" from "Congero Administration" on "CMS HomePage"
    Then I should be on "CMS LookupData" page
    When I click on "newButton" element
    Then I should see "LookupName" element
    When I enter text in LookupName
    And I select "enabled" check box
    And I enter text in Description
    And I select "Tags" text in "Source" dropdown
    And  I click save and close button on "CMS LookupData"
#    Then I Verify that the LookupData is created

  @CMS,@Regression
  Scenario: This scenario validates that a user can create a new Congero Type with associated field definitions.
    Given I visit "CMS Login" page
    When I log in to the Libercus application with valid user credentials
    Then I should be on "CMS HomePage" page
    Then I should see The Home text
    When I navigate to "Congero Types" from "Congero Administration" on "CMS HomePage"
    Then I should be on "CMS CongeroTypes" page
    When I click on "newButton" element
    Then I should see "TypeName" element
    When I enter text in TypeName
    And I select "tags" text in "sectionId" dropdown
    And I enter text in the objectFileName
    And I select "enabled" check box
    And I select "showInMenu" check box
    And I select "includeInSearch" check box
    And I select "Select Field" text in "FieldDefinitionsDataType" dropdown
    When I click on "Add" element
    Then Verify the field is added under the field defination
    When I enter ID for fieldDefination
    And I enter Name for fieldDefination
    And I select "Tags" text in "dataSource" dropdown
    When  I click save and close button on "CMS CongeroTypes"
    And  Refresh the page
    Then I should verify the created congero type is displayed
    When I click on "newButtonOfCongero" element on "CMS CongeroTypes" page
    Then verify congero field is displayed
Feature: This feature validates the user and tag management functionality in the Product Administration module of the Libercus CMS.
  @CMS,@Regression
  Scenario:This scenario verifies that an administrator can create a new user in the CMS.
    Given I visit "CMS Login" page
    When I log in to the Libercus application with valid user credentials
    Then I should be on "CMS HomePage" page
    Then I should see The Home text
    When I navigate to "Users" from "Product Administration" on "CMS HomePage"
    Then I should be on "CMS Users" page
    When I should note the count of the users
    When I click on "newButton" element on "CMS Users" page
    Then Verify user creation page is displayed
    When I enter loginName,UserName,Password,RepeatPassword,Email Address
    And I click save and close button on "CMS Users"
    Then I verify that the user is created
    When I click on "Logout" element on "CMS HomePage" page
    Then I should be on "CMS Login" page
    When I log in into the application with created user credentials
#    Then I should see The Home text

  @CMS,@Regression
  Scenario:This scenario validates that an administrator can create a new tag in the CMS.
    Given I visit "CMS Login" page
    When I log in to the Libercus application with valid user credentials
    Then I should be on "CMS HomePage" page
    Then I should see The Home text
    When I navigate to "Tags" from "Product Administration" on "CMS HomePage"
    Then I should be on "CMS Tags" page
    When I should note the count of the tags
    When I click on "newButton" element on "CMS Tags" page
    Then I should see the tag creation page
    When I enter tagDisplayName and tagShortName
    And I click save and close button on "CMS Tags"
    Then I verify that the tag is created

  @CMS,@Regression
  Scenario: This scenario verifies that the search functionality for tags works correctly.
    Given I visit "CMS Login" page
    When I log in to the Libercus application with valid user credentials
    Then I should be on "CMS HomePage" page
    Then I should see The Home text
    When I navigate to "Tags" from "Product Administration" on "CMS HomePage"
    Then I should be on "CMS Tags" page
    When I enter text in the search field of tags
    Then I verify that the search results are displayed
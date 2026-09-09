Feature: This feature validates the creation, search, copy, and deletion functionalities for Shapes, Styles, Templates, Image Styles, and Jump Styles in the Print Administration module of the Libercus CMS.
  @CMS,@Regression
  Scenario:Verifies that a user can create a new shape, assign ID, Name, and template, and confirm its availability when creating a story.
    Given I visit "CMS Login" page
    When I log in to the Libercus application with valid user credentials
    Then I should be on "CMS HomePage" page
    Then I should see The Home text
    When I navigate to "Shapes" from "Print Administration" on "CMS HomePage"
    Then I should be on "CMS Shapes" page
    When I click on "newButton" element
    Then Shapes creation page should be displayed
    When  I select "enabled" check box
    And I enter ID for shape
    And I enter Name for shape
    And I select "6 col Inside Left NEW FONTS" text in "templetDropdown" dropdown
    Then I click save and close button on "CMS Shapes"
    When Refresh the page
    When I navigate to "Stories" from "Content" on "CMS HomePage"
    Then I should be on "CMS StoryCreation" page
    And I select "PG Print" text in "channelDropdown" dropdown on "CMS StoryCreation" page
    And I click on "newButton" element for "<Nth story>"
    When I should see following elements:
      | slug   |
      | status |
    Then I verify that the shape is displayed
  @CMS,@Regression
  Scenario: Verifies that searching by shape name or ID returns correct results.
    Given I visit "CMS Login" page
    When I log in to the Libercus application with valid user credentials
    Then I should be on "CMS HomePage" page
    Then I should see The Home text
    When I navigate to "Shapes" from "Print Administration" on "CMS HomePage"
    Then I should be on "CMS Shapes" page
    When I enter text in search field of shapes
    And I click on "updateButton" element
    Then Verify the search results are displayed for shapes
  @CMS,@Regression
  Scenario: Ensures that an existing shape can be copied successfully.
    Given I visit "CMS Login" page
    When I log in to the Libercus application with valid user credentials
    Then I should be on "CMS HomePage" page
    Then I should see The Home text
    When I navigate to "Shapes" from "Print Administration" on "CMS HomePage"
    Then I should be on "CMS Shapes" page
    When I select a shape to copy
    And I click on "Copy" element on "CMS Shapes" page
    And I click on Ok button
    Then I should verify that the shape has been copied successfully
  @CMS,@Regression
  Scenario: Confirms that a shape can be deleted and is removed from the list.
    Given I visit "CMS Login" page
    When I log in to the Libercus application with valid user credentials
    Then I should be on "CMS HomePage" page
    Then I should see The Home text
    When I navigate to "Shapes" from "Print Administration" on "CMS HomePage"
    Then I should be on "CMS Shapes" page
    When I select a shape to copy
    And I click on "Delete" element on "CMS Shapes" page
    And I click on Ok button
    Then I should verify that the shape has been deleted successfully

  @CMS,@Regression
  Scenario:Verifies creation of a style, assigns it to a story, and ensures it is displayed properly.
    Given I visit "CMS Login" page
    When I log in to the Libercus application with valid user credentials
    Then I should be on "CMS HomePage" page
    Then I should see The Home text
    When I navigate to "Styles" from "Print Administration" on "CMS HomePage"
    Then I should be on "CMS Styles" page
    When I click on "newButton" element
    Then Styles creation page should be displayed
    When I enter Name for Style
    Then I click save and close button on "CMS Styles"
    When Refresh the page
    When I navigate to "Stories" from "Content" on "CMS HomePage"
    Then I should be on "CMS StoryCreation" page
    And I select "PG Print" text in "channelDropdown" dropdown on "CMS StoryCreation" page
    And I click on "newButton" element for "<Nth story>"
    When I should see following elements:
      | slug   |
      | status |
    And I enter the slug for story
    And I switch to "PG Print" tab on "CMS StoryCreation" page
    Then I verify that the style is displayed
  @CMS,@Regression
  Scenario: Ensures searching styles returns accurate results.
    Given I visit "CMS Login" page
    When I log in to the Libercus application with valid user credentials
    Then I should be on "CMS HomePage" page
    Then I should see The Home text
    When I navigate to "Styles" from "Print Administration" on "CMS HomePage"
    Then I should be on "CMS Styles" page
    When I enter text in search field of styles
    And I click on "updateButton" element
    Then Verify the search results are displayed for styles
  @CMS,@Regression
  Scenario: Confirms a style can be duplicated successfully.
    Given I visit "CMS Login" page
    When I log in to the Libercus application with valid user credentials
    Then I should be on "CMS HomePage" page
    Then I should see The Home text
    When I navigate to "Styles" from "Print Administration" on "CMS HomePage"
    Then I should be on "CMS Styles" page
    When I select a style to copy
    And I click on "Copy" element on "CMS Styles" page
    And I click on Ok button for styles
    Then I should verify that the style has been copied successfully
  @CMS,@Regression
  Scenario: Confirms that a style can be deleted and is removed from the list.
    Given I visit "CMS Login" page
    When I log in to the Libercus application with valid user credentials
    Then I should be on "CMS HomePage" page
    Then I should see The Home text
    When I navigate to "Styles" from "Print Administration" on "CMS HomePage"
    Then I should be on "CMS Styles" page
    When I select a style to copy
    And I click on "Delete" element on "CMS Styles" page
    And I click on Ok button for styles
    Then I should verify that the style has been deleted successfully
  @CMS,@Regression
  Scenario:Validates that a template can be created, assigned column sizes, and verified on print pages.
    Given I visit "CMS Login" page
    When I log in to the Libercus application with valid user credentials
    Then I should be on "CMS HomePage" page
    Then I should see The Home text
    When I navigate to "Templates" from "Print Administration" on "CMS HomePage"
    Then I should be on "CMS Templates" page
    When I click on "newButton" element
    Then Templates creation page should be displayed
    When I enter name for template
    And I enter columns size for template
    Then I click save and close button on "CMS Templates"
    When Refresh the page
    Then I should see The Home text
    When I navigate to "Print Pages" from "Content" on "CMS HomePage"
    Then I should be on "CMS PrintPages" page
    When I select "PG Print" text in "channelDropdown" dropdown on "CMS PrintPages" page
    And I click on "newButton" element
    When Creation page should be displayed
    Then I verify that the template is displayed on page

  @CMS,@Regression
  Scenario: Ensures template search returns correct results.
    Given I visit "CMS Login" page
    When I log in to the Libercus application with valid user credentials
    Then I should be on "CMS HomePage" page
    Then I should see The Home text
    When I navigate to "Templates" from "Print Administration" on "CMS HomePage"
    Then I should be on "CMS Templates" page
    When I enter text in search field of Templates
    And I click on "updateButton" element
    Then Verify the search results are displayed for Templates
  @CMS,@Regression
  Scenario: Confirms a template can be deleted successfully.
    Given I visit "CMS Login" page
    When I log in to the Libercus application with valid user credentials
    Then I should be on "CMS HomePage" page
    Then I should see The Home text
    When I navigate to "Templates" from "Print Administration" on "CMS HomePage"
    Then I should be on "CMS Templates" page
    When I select a template
    And I click on "Delete" element on "CMS Templates" page
    And I click on Ok button for template
  @CMS,@Regression
  Scenario: Ensures a template can be copied successfully.
    Given I visit "CMS Login" page
    When I log in to the Libercus application with valid user credentials
    Then I should be on "CMS HomePage" page
    Then I should see The Home text
    When I navigate to "Templates" from "Print Administration" on "CMS HomePage"
    Then I should be on "CMS Templates" page
    When I select a template
    And I click on "Copy" element on "CMS Templates" page
    And I click on ok button for template copy
  @CMS,@Regression
  Scenario: Verifies creation of new image styles and confirms they are saved successfully.
    Given I visit "CMS Login" page
    When I log in to the Libercus application with valid user credentials
    Then I should be on "CMS HomePage" page
    Then I should see The Home text
    When I navigate to "Image Styles" from "Print Administration" on "CMS HomePage"
    Then I should be on "CMS ImageStyles" page
    When I click on "newButton" element
    Then Image Style creation page should be displayed
    When I enter name for image style
    And I click save and close button on "CMS ImageStyles"
    Then I should verify that the image style is created
  @CMS,@Regression
  Scenario: Validates that new jump styles can be created and are saved correctly.
    Given I visit "CMS Login" page
    When I log in to the Libercus application with valid user credentials
    Then I should be on "CMS HomePage" page
    Then I should see The Home text
    When I navigate to "Jump Styles" from "Print Administration" on "CMS HomePage"
    Then I should be on "CMS JumpStyles" page
    When I click on "newButton" element
    Then JumpStyles creation page should be displayed
    When I enter name for jump style
    And I click save and close button on "CMS JumpStyles"
    Then I should verify that the jump style is created
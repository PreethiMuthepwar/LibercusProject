package test.automation.steps.Libercus;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import test.automation.framework.Browser;
import test.automation.pages.Libercus.CMS.StoryCreation;
import java.text.ParseException;
import java.time.Duration;
import static test.automation.framework.Actions.jsClick;
import static test.automation.framework.Actions.shortWait;
import static test.automation.pages.Libercus.CMS.CongeroTypes.*;
import static test.automation.pages.Libercus.CMS.ImageStyles.*;
import static test.automation.pages.Libercus.CMS.JumpStyles.*;
import static test.automation.pages.Libercus.CMS.LookupData.enterTextInDescription;
import static test.automation.pages.Libercus.CMS.LookupData.enterTextInlookupName;
import static test.automation.pages.Libercus.CMS.PrintPages.adEditMode;
import static test.automation.pages.Libercus.CMS.PrintPages.drag_Drop;
import static test.automation.pages.Libercus.CMS.Shapes.*;
import static test.automation.pages.Libercus.CMS.StoryCreation.enterSlug;
import static test.automation.pages.Libercus.CMS.StoryCreation.storyCalender;
import static test.automation.pages.Libercus.CMS.Styles.*;
import static test.automation.pages.Libercus.CMS.Styles.okButtonOfStyles;
import static test.automation.pages.Libercus.CMS.Tags.*;
import static test.automation.pages.Libercus.CMS.Templates.*;
import static test.automation.pages.Libercus.CMS.Users.*;
import static test.automation.pages.Libercus.CMS.WirePhotos.selectPhoto;
import static test.automation.pages.Libercus.CMS.WirePhotos.verifyPhotosAreAvailable;
import static test.automation.pages.Libercus.CMS.WireStories.*;
import static test.automation.pages.Libercus.CMS.WireStories.enterPublishDate;
import static test.automation.utils.testDataUtils.getDates;
public class RegressionSteps
{
    @And("^I should check whether the stories are available\\.$")
    public void iShouldCheckWhetherTheStoriesAreAvailable() throws Exception {
        verifyStoriesAreAvailable();
    }
    @And("^I select a story$")
    public void iSelectAStory() throws InterruptedException {
        selectStory();
    }
    @Then("^I should see the Use Story popup$")
    public void iShouldSeeTheUseStoryPopup() throws InterruptedException {
        useStoryPopup();
    }
    @And("^I enter the publish date for story$")
    public void iEnterThePublishDateForStory() throws Exception
    {
        String Date = (String) getDates().get("dateForWireStories");
        enterPublishDate(Date);
    }
    @And("^I select the checkbox for Omit photos$")
    public void iSelectTheCheckboxForOmitPhotos() throws InterruptedException {
        tapOnCheckbox();
    }
    @And("^I tap on the use button$")
    public void iTapOnTheUseButton() throws Exception {
        tapOnUseButton();
    }
    @And("^I enter  slug in the search box$")
    public void iEnterSlugInTheSearchBox() throws Exception {
        enterslugText();
    }
    @And("^I should check whether the photos are available\\.$")
    public void iShouldCheckWhetherThePhotosAreAvailable() throws InterruptedException {
        verifyPhotosAreAvailable();
    }
    @And("^I select a Media file$")
    public void iSelectAMediaFile() throws InterruptedException {
        selectPhoto();
    }
    @And("^I select a date for \"([^\"]*)\"$")
    public void iSelectADateFor(String moduleName) throws Throwable
    {
        enterPublishdateForStories(moduleName);
    }
    @And("^I enter title in search field of MediaFiles$")
    public void iEnterTitleInSearchFieldOfMediaFiles()
    {
        enterTitle();
    }
    @And("^I click save button on \"([^\"]*)\"$")
    public void iClickSaveButtonOn(String pageClassName ) throws Throwable
    {
        onPage(pageClassName);
        Browser.getDriver(By.cssSelector("story-preview")).switchTo().defaultContent();
        jsClick("saveButton");
        shortWait();
    }
    @And("I select a date that has story")
    public void iSelectADateThatHasStory() throws ParseException, InterruptedException {
        storyCalender("30/08/2025");
    }
    @When("I enter text in LookupName")
    public void iEnterTextInLookupName()
    {
        enterTextInlookupName();
    }
    @And("I enter text in Description")
    public void iEnterTextInDescription()
    {
        enterTextInDescription();
    }
    @When("I enter text in TypeName")
    public void iEnterTextInTypeName()
    {
        enterTypeName();
    }
    @And("I enter text in the objectFileName")
    public void iEnterTextInTheObjectFileName()
    {
        enterObjectFileName();
    }
    @Then("Verify the field is added under the field defination")
    public void verifyTheFieldIsAddedUnderTheFieldDefination()
    {
        verifyFieldisAdded();
    }
    @When("I enter ID for fieldDefination")
    public void iEnterIDForFieldDefination()
    {
        enterFieldId();
    }
    @And("I enter Name for fieldDefination")
    public void iEnterNameForFieldDefination()
    {
        enterFieldName();
    }
    @And("Refresh the page")
    public void refreshThePage()
    {
        refresh();
        WebDriverWait wait = new WebDriverWait(Browser.getDriver(By.cssSelector("story-preview")), Duration.ofSeconds(240));
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(),'Print shapes and styles loading complete!')]")));
    }
    @Then("I should verify the created congero type is displayed")
    public void iShouldVerifyTheCreatedCongeroTypeIsDisplayed() throws Exception
    {
        verifyCongeroTypeisCreated();
    }
    @Then("verify congero field is displayed")
    public void verifyCongeroFieldIsDisplayed()
    {
        verifyFieldIsDisplayed();
    }
    @Then("Shapes creation page should be displayed")
    public void shapesCreationPageShouldBeDisplayed() throws InterruptedException {
        creationPage();
    }
    @And("I enter ID for shape")
    public void iEnterIDForShape() throws InterruptedException {
        enterShapeId();
    }
    @And("I enter Name for shape")
    public void iEnterNameForShape()
    {
        enterShapeName();
    }
    @Then("I verify that the shape is displayed")
    public void iVerifyThatTheShapeIsDisplayed() throws InterruptedException {
        verifyShapeisDisplayed();
    }
    @When("I enter text in search field of shapes")
    public void iEnterTextInSearchFieldOfShapes()
    {
        searchForShape();
    }
    @Then("Verify the search results are displayed for shapes")
    public void verifyTheSearchResultsAreDisplayedForShapes()
    {
        verifySearchResults();
    }
    @When("I select a shape to copy")
    public void iSelectAShapeToCopy() throws InterruptedException {
        selectShapeToCopy();
    }
    @Then("I click on Ok button")
    public void iClickOnOkButton() throws InterruptedException {
        okButton();
    }
    @Then("I should verify that the shape has been copied successfully")
    public void iShouldVerifyThatTheShapeHasBeenCopiedSuccessfully() throws InterruptedException {
        verifyCount("Copy");
    }
    @Then("Styles creation page should be displayed")
    public void stylesCreationPageShouldBeDisplayed() throws InterruptedException {
        stylesCreationPage();
    }
    @And("I enter Name for Style")
    public void iEnterNameForStyle()
    {
        enterStyleName();
    }
    @And("I enter the slug for story")
    public void iEnterTheSlugForStory()
    {
        enterSlug();
    }
    @Then("I verify that the style is displayed")
    public void iVerifyThatTheStyleIsDisplayed() throws InterruptedException {
        verifyStyleisDisplayed();
    }
    @When("I enter text in search field of styles")
    public void iEnterTextInSearchFieldOfStyles()
    {
        searchForStyle();
    }
    @Then("Verify the search results are displayed for styles")
    public void verifyTheSearchResultsAreDisplayedForStyles()
    {
        verifySearchResultsForStyles();
    }
    @When("I select a style to copy")
    public void iSelectAStyleToCopy() throws InterruptedException {
        selectStyleToCopy();
    }
    @And("I click on Ok button on styles")
    public void iClickOnOkButtonOnStyles() throws InterruptedException {
        okButtonOfStyles();
    }
    @Then("Templates creation page should be displayed")
    public void templatesCreationPageShouldBeDisplayed() throws InterruptedException {
        templateCreationPage();
    }
    @When("I enter name for template")
    public void iEnterNameForTemplate()
    {
        enterTemplateName();
    }
    @And("I enter columns size for template")
    public void iEnterColumnsSizeForTemplate()
    {
        enterColumnSizeforTemplate();
    }
    @When("Creation page should be displayed")
    public void creationPageShouldBeDisplayed()
    {
        pageCreation();
    }
    @When("I enter text in search field of Templates")
    public void iEnterTextInSearchFieldOfTemplates()
    {
        searchForTemplate();
    }
    @Then("Verify the search results are displayed for Templates")
    public void verifyTheSearchResultsAreDisplayedForTemplates()
    {
        verifySearchResultsForTemplate();
    }
    @When("I select a template")
    public void iSelectATemplate() throws InterruptedException {
        selectTemplate();
    }
    @And("I click on Ok button for template")
    public void iClickOnOkButtonForTemplate() throws InterruptedException {
        okButtonForTemplates();
    }
    @And("I click on ok button for template copy")
    public void iClickOnOkButtonForTemplateCopy() throws InterruptedException {
        okButtonForTemplates();
    }
    @Then("I should verify that the shape has been deleted successfully")
    public void iShouldVerifyThatTheShapeHasBeenDeletedSuccessfully() throws InterruptedException {
        verifyCount("Delete");
    }
    @Then("Image Style creation page should be displayed")
    public void imageStyleCreationPageShouldBeDisplayed()
    {
        imageStylesCreationPage();
    }
    @When("I enter name for image style")
    public void iEnterNameForImageStyle()
    {
        enterImageStyleName();
    }
    @Then("I should verify that the image style is created")
    public void iShouldVerifyThatTheImageStyleIsCreated()
    {
        verifyimagestyleIsCreated();
    }
    @Then("JumpStyles creation page should be displayed")
    public void jumpstylesCreationPageShouldBeDisplayed()
    {
        jumpStyleCreationPage();
    }
    @When("I enter name for jump style")
    public void iEnterNameForJumpStyle()
    {
        enterNameForJumpStyle();
    }
    @Then("I should verify that the jump style is created")
    public void iShouldVerifyThatTheJumpStyleIsCreated()
    {
        verifyJumpStyleIsCreated();
    }
    @And("I click on Ok button for styles")
    public void iClickOnOkButtonForStyles() throws InterruptedException {
        okButtonOfStyles();
    }
    @Then("I should verify that the style has been copied successfully")
    public void iShouldVerifyThatTheStyleHasBeenCopiedSuccessfully() throws InterruptedException {
        verifyCountOfStyles("Copy");
    }
    @Then("I should verify that the style has been deleted successfully")
    public void iShouldVerifyThatTheStyleHasBeenDeletedSuccessfully() throws InterruptedException {
        verifyCountOfStyles("Delete");
    }
    @Then("I verify that the template is displayed on page")
    public void iVerifyThatTheTemplateIsDisplayedOnPage()
    {
        selectTemplateOnPage();
    }
    @Then("Verify user creation page is displayed")
    public void verifyUserCreationPageIsDisplayed()
    {
        creationPageIsDisplayed();
    }
    @And("I enter loginName,UserName,Password,RepeatPassword,Email Address")
    public void iEnterLoginNameUserNamePasswordRepeatPasswordEmailAddress()
    {
        enterLoginName();
        enterUserName();
        enterPassword();
        enterEmailAddress();
    }
    @Then("I verify that the user is created")
    public void iVerifyThatTheUserIsCreated() throws InterruptedException {
        verifyUserCreation();
    }
    @When("I should note the count of the users")
    public void iShouldNoteTheCountOfTheUsers() throws InterruptedException {
        StoryCreation storyCreation=new StoryCreation();
        beforeCount = storyCreation.numberOfRecords("countOfUsers");
    }
    @When("I log in into the application with created user credentials")
    public void iLogInIntoTheApplicationWithCreatedUserCredentials()
    {
        userCredentials();
    }
    @Then("I should see the tag creation page")
    public void iShouldSeeTheTagCreationPage() throws InterruptedException {
        TagscreationPage();
    }
    @When("I enter tagDisplayName and tagShortName")
    public void iEnterTagDisplayNameAndTagShortName()
    {
        enterTagDisplayName();
        enterTagShortName();
    }
    @When("I should note the count of the tags")
    public void iShouldNoteTheCountOfTheTags() throws InterruptedException
    {
        shortWait();
        StoryCreation storyCreation=new StoryCreation();
        beforeCountOfTags = storyCreation.numberOfRecords("countOfTags");
    }
    @Then("I verify that the tag is created")
    public void iVerifyThatTheTagIsCreated() throws InterruptedException {
        verifyTagCreation();
    }
    @When("I enter text in the search field of tags")
    public void iEnterTextInTheSearchFieldOfTags()
    {
        enterTextInSearchBox();
    }
    @Then("I verify that the search results are displayed")
    public void iVerifyThatTheSearchResultsAreDisplayed()
    {
        verifySearchResultsOfTags();
    }
    @When("I turn on the Ad Edit Mode")
    public void iTurnOnTheAdEditMode()
    {
        adEditMode();
    }
    @And("Drag and drop the ad on page and adjust the ad")
    public void dragAndDropTheAdOnPageAndAdjustTheAd() throws InterruptedException {
        drag_Drop("printad");
    }
    @When("I enter the publish date for image on {string} page")
    public void iEnterThePublishDateForImageOnPage(String arg0) throws ParseException, InterruptedException {
        String Date = (String) getDates().get("dateForWireStories");
        enterPublishDate(Date);
    }



}
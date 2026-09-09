package test.automation.steps.Libercus;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import test.automation.framework.Browser;
import static test.automation.pages.Libercus.PF3.AdsPuzzlesPage.*;
import static test.automation.pages.Libercus.PF3.ArticleViewPage.checkNextButtonFunctionality;
import static test.automation.pages.Libercus.PF3.ArticleViewPage.checkPreviousButtonFunctionality;
import static test.automation.pages.Libercus.PF3.EditionsPage.VerifyEdition;
import static test.automation.pages.Libercus.PF3.EditionsPage.navigateToAnotherEdition;
import static test.automation.pages.Libercus.PF3.HomePage.*;
import static test.automation.pages.Libercus.PF3.HelpPage.*;
import static test.automation.pages.Libercus.PF3.Hyperlinks.navigatingToHyperlinks;
import static test.automation.pages.Libercus.PF3.JumpLinks.handleJumplinks;
import static test.automation.pages.Libercus.PF3.Pages.selectPage;
import static test.automation.pages.Libercus.PF3.Pages.selectSectionOnPagesPanel;
import static test.automation.pages.Libercus.PF3.SearchPage.checkAdvanceSearchFunctionality;
import static test.automation.pages.Libercus.PF3.SearchPage.search;
import static test.automation.pages.Libercus.PF3.SectionsPage.selectSection;
import static test.automation.pages.Libercus.PF3.SettingsPage.*;
import static test.automation.pages.Libercus.PF3.TOCPage.*;
public class PF3steps {
    @And("I check the the forward navigation for sections")
    public void iCheckTheTheForwardNavigationForSections() throws Exception {
        checkForwardNavigation();
    }
    @Then("I should be on first page")
    public void iShouldBeOnFirstPage() {
    }
    @And("I check the the previous navigation for sections")
    public void iCheckTheThePreviousNavigationForSections() throws Exception {
        checkPreviousNavigation();
    }
    @When("I double click on section {string}")
    public void iDoubleClickOnSection(String section) throws InterruptedException {
        selectSection(section);
    }
    @Then("I should see corresponding section")
    public void iShouldSeeCorrespondingSection() {
    }
    @When("I select {string} on Pages panel")
    public void iSelectOnPagesPanel(String section) throws Exception {
        selectSectionOnPagesPanel(section);
    }
    @And("I double click on page {string}")
    public void iDoubleClickOnPage(String page) throws InterruptedException {
        selectPage(page);
    }
    @Then("I should see corresponding page")
    public void iShouldSeeCorrespondingPage() {
    }
    @When("I enter {string} in search field")
    public void iEnterInSearchField(String searchItem) throws InterruptedException {
        search(searchItem);
    }
    @Then("I should see new tab open for advance search")
    public void iShouldSeeNewTabOpenForAdvanceSearch() throws InterruptedException {
        checkAdvanceSearchFunctionality();
    }
    @And("I check the the forward navigation and backward navigation for page")
    public void iCheckTheTheForwardNavigationAndBackwordNavigationForPage() throws Exception {
        checkPageForwardNavigation();
        checkPagePreviousNavigation();
    }
    @When("I click on each section and validate the pages under it")
    public void iClickOnEachSectionAndValidateThePagesUnderIt() throws Exception {
        navigateThroughSectionsAndValidate();
    }
    @And("I click each page and validate")
    public void iClickEachPageAndValidate() throws Exception {
        navigateThroughEachPageUnderSelectedSection();
    }
    @When("I select section {string} on TOC")
    public void iSelectSectionOnTOC(String section) throws Exception {
        selectSectionOnTOC(section);
    }
    @When("I select Page {string} on TOC")
    public void iSelectPageOnTOC(String page) throws Exception {
        selectPageOnTOC(page);
    }
    @Then("I validate stories under selected section and page")
    public void iValidateStoriesUnderSelectedSectionAndPage() throws Exception {
        selectStoryAndValidate();
    }
    @Then("I should see panel with header {string}")
    public void iShouldSeePanelWithHeader(String header) throws InterruptedException {
        JavascriptExecutor js = (JavascriptExecutor) Browser.getDriver(By.cssSelector("story-preview"));
        js.executeScript("document.body.style.zoom='70%';");
        validatePanelHeader(header);
    }
    @When("I search ads by date")
    public void iSearchAdsByDate() throws Exception {
        enterDateAndClickSearchAds();
    }
    @And("I select ad and print")
    public void iSelectAdAndPrint() throws Exception {
        selectAdAndPrint();
    }
    @And("I check Interactive ads functionality")
    public void iCheckInteractiveAdsFunctionality() throws InterruptedException {
        printInteractiveAds();
    }
    @And("I check Interactive Puzzles functionality")
    public void iCheckInteractivePuzzlesFunctionality() {
        checkInteractivePuzzles();
    }
    @And("I should land on new tab")
    public void iShouldLandOnNewTab() {
    }
    @When("I click on the story")
    public void iClickOnTheStory() throws Exception {
        clickOnTheStory();
    }
    @Then("I should see article view for the clicked story")
    public void iShouldSeeArticleViewForTheClickedStory() throws InterruptedException {
        validateArticleView();
    }
    @Then("I navigate to through the pages using next button and validate")
    public void iNavigateToThroughThePagesUsingNextButtonAndValidate() throws InterruptedException {
        checkNextButtonFunctionality();
    }
    @And("I navigate to through the pages using previous button and validate")
    public void iNavigateToThroughThePagesUsingPreviousButtonAndValidate() throws InterruptedException {
        checkPreviousButtonFunctionality();
    }
    @When("I select menu layout style as {string}")
    public void iSelectMenuLayoutStyleAs(String layOutStyle) throws Exception {
        selectMenuLayoutStyle(layOutStyle);
    }
    @Then("I should see menu layout on {string} side")
    public void iShouldSeeMenuLayoutOnSide(String layOutStyle) throws InterruptedException {
        validateLayOutStyle(layOutStyle);
    }
    @When("I select {string} as theme color")
    public void iSelectAsThemeColor(String color) throws Exception {
        clickOnThemeColor(color);
    }
    @Then("I should see layout in {string} color")
    public void iShouldSeeLayoutInColor(String color) throws InterruptedException {
        validateLayoutColor(color);
    }
    @When("I select {string} as display option")
    public void iSelectAsDisplayOption(String displayOption) throws Exception {
        selectDisplayOption(displayOption);
    }
    @And("I check single page default display option")
    public void iCheckSinglePageDefaultDisplayOption() throws Exception {
        checkDefaultDisplayOption();
    }
    @Then("I validate display option feature {string}")
    public void iValidateDisplayOptionFeature(String displayOption) throws Exception {
        validateDoublePage(displayOption);
    }
    @And("I double click on Edition {string}")
    public void iDoubleClickOnEdition(String editionDate) throws InterruptedException {
        navigateToAnotherEdition(editionDate);
    }
    @Then("I should see corresponding Edition {string}")
    public void iShouldSeeCorrespondingEdition(String expectedDate) {
        VerifyEdition(expectedDate);
    }
    @And("I should see each help content")
    public void iShouldSeeEachHelpContent() throws InterruptedException {
        eachHelpElement();
    }
    @And("I check all the hyperlinks are redirecting")
    public void iCheckAllTheHyperlinksAreRedirecting() throws Exception {
        navigatingToHyperlinks();
    }
    @And("I check the the Jumplinks are navigating or not")
    public void iCheckTheTheJumplinksAreNavigatingOrNot() throws InterruptedException {
        handleJumplinks();
    }
    @When("I click on Ad image")
    public void iClickOnAdImage() throws InterruptedException {
        clickOnAdImage();
    }
    @Then("I should see new tab opening for the ad")
    public void iShouldSeeNewTabOpeningForTheAd() throws InterruptedException {
        validateActionsOnAds();
    }
}
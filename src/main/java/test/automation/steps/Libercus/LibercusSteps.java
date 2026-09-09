package test.automation.steps.Libercus;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import test.automation.framework.Actions;
import test.automation.framework.Browser;
import test.automation.pages.Libercus.CMS.*;
import java.nio.file.Paths;
import java.util.List;
import static test.automation.framework.Actions.*;
import static test.automation.framework.Config.getCustomerEnv;
import static test.automation.framework.Page.onPage;
import static test.automation.pages.Libercus.CMS.HomePage.*;
import static test.automation.pages.Libercus.CMS.InteractiveAds.*;
import static test.automation.pages.Libercus.CMS.Login.*;
import static test.automation.pages.Libercus.CMS.MediaFiles.*;
import static test.automation.pages.Libercus.CMS.OutputOfPrintPages.*;
import static test.automation.pages.Libercus.CMS.PrintAds.*;
import static test.automation.pages.Libercus.CMS.PrintPages.*;
import static test.automation.pages.Libercus.CMS.ProductionView.*;
import static test.automation.pages.Libercus.CMS.RichMediaAds.*;
import static test.automation.pages.Libercus.CMS.StoryCreation.*;
import static test.automation.utils.testDataUtils.*;

public class LibercusSteps {
    public static String beforeCopyStory;
    public static String afterCopyStory;
    public static String countBeforeDeleteStory;
    public static String BeforeCopy_InteractiveAds;
    public static String AfterCopy_InteractiveAds;
    public static String BeforeCopy_RichMediaAds, AfterCopy_RichMediaAds;
    public static String BeforeCopy_Pages, AfterCopy_Pages;
    @When("I log in to the Libercus application with valid user credentials")
    public void iLogInToTheLibercusApplicationWithValidUserCredentials() throws InterruptedException {
        validLogin();
    }
    @Then("I should see The Home text")
    public void iShouldSeeTheHomeText() throws InterruptedException {
        VerifyHomeMessage();
    }
    @When("I enter the publish date, status, slug, and shape for {string}")
    public void iEnterThePublishDateStatusSlugAndShapeFor(String key) throws Exception
    {
        String Date2 = (String) getDates().get("dateForStories");
        enterPublishDate(Date2);
        onPage("CMS StoryCreation");
        System.out.println(key);
        selectStatus();
        enterSlug();
        String shapeValue = getShapeForCurrentEnv(key);
        selectShape(shapeValue);
        shortWait();
    }
    @And("I enter the Kicker,Title,Byline,Story for {string}")
    public void iEnterTheKickerTitleBylineStoryFor(String key) throws InterruptedException {
        String Title = (String) getStoryData(key).get("Title");
        String story = (String) getStoryData(key).get("story");
        enterTitle(Title);
        enterStory(story);
    }
    @And("I add an image to the story if it contains an image for {string}")
    public void iAddAnImageToTheStoryIfItContainsAnImageFor(String key) throws Exception {
//        String Shape = (String) getStoryData(key).get("Shape");
        String Shape = getShapeForCurrentEnv(key);
        hasImage(Shape,key);
    }
    @Then("Interactive Ad creation page should be displayed")
    public void theInteractiveAdCreationPageShouldBeDisplayed() throws InterruptedException {
        interactiveAdCreationPage();
    }
    @When("I enter the publish date for the interactive ad")
    public void iEnterThePublishDateForTheInteractiveAd() throws Exception {
        enterPublishDate("12/01/2025");
    }
    @And("I enter the Ad Type")
    public void iEnterTheAdType() throws InterruptedException {
        adType();
    }
    @And("I enter the Title,slug for the interactive ad")
    public void iEnterTheTitleSlugForTheInteractiveAd() throws InterruptedException {
        enterTitleOnInteractiveAds("Title");
        enterSlugOnInteractiveAds("Slug");
    }
    @And("I select all checkboxes")
    public void iSelectAllCheckboxes() throws InterruptedException {
        clickCheckboxes();
    }
    @Then("I verify that the image is uploaded successfully")
    public void iVerifyThatTheImageIsUploadedSuccessfully() {
        imageIsUploaded();
    }
    @Then("I verify that the ZIP file is uploaded successfully")
    public void iVerifyThatTheZIPFileIsUploadedSuccessfully() {
        verifyZipFileIsUploaded();
    }
    @Then("RichMedia Ad creation page should be displayed")
    public void theRichMediaAdCreationPageShouldBeDisplayed() {
        adCreationPage();
    }
    @When("I enter the publish date for the RichMedia Ad")
    public void iEnterThePublishDateForTheRichMediaAd() throws Exception {
        enterPublishDate("21/07/2025");
    }
    @And("I enter the Title,Slug for the RichMedia Ad")
    public void iEnterTheTitleSlugForTheRichMediaAd() throws InterruptedException {
        RichMediaAds richMediaAd = new RichMediaAds();
        richMediaAd.title("title");
        richMediaAd.slug("slug");
    }
    @And("I enter the AdType for the RichMedia Ad")
    public void iEnterTheAdTypeForTheRichMediaAd() throws InterruptedException {
        adTypeInRichMedia("Lexigo");
    }
    @And("I select all checkboxes of RichMedia Ads")
    public void iSelectAllCheckboxesOfRichMediaAds() throws Exception {
        clickCheckboxesInRichMedia();
    }
    @Then("I verify that the image is uploaded successfully for the RichMedia Ad")
    public void iVerifyThatTheImageIsUploadedSuccessfullyForTheRichMediaAd() throws InterruptedException {
        verifyImageisUploaded();
    }
    @Then("I verify that the ZIP file is uploaded successfully for the RichMedia Ad")
    public void iVerifyThatTheZIPFileIsUploadedSuccessfullyForTheRichMediaAd() {
        verifyZipFileIsUploadedInRichMedia();
    }
    @Then("PrintAd creation page should be displayed")
    public void thePrintAdCreationPageShouldBeDisplayed() {
        printAdAdCreationPage();
    }
    @When("I enter the publish date for the PrintAd")
    public void iEnterThePublishDateForThePrintAd() throws Exception {
        enterPublishDate("12/01/2025");
    }
    @When("I upload the PDF file for the PrintAd")
    public void iUploadThePDFFileForThePrintAd() throws InterruptedException {
        addPdfFile();
    }
    @Then("I verify that the PDF file is uploaded successfully for the PrintAd")
    public void iVerifyThatThePDFFileIsUploadedSuccessfullyForThePrintAd() throws InterruptedException {
        verifyPdfIsUploaded();
    }
    @And("I Note the count of stories before the copy of stories")
    public void iNoteTheCountOfStoriesBeforeTheCopyOfStories() throws Exception {
        String Date = (String) getDates().get("toDateForCopyOfStories");
        StoryCreation storyCreation = new StoryCreation();
        storyCreation.calender(Date);
        storyCreation.update();
        beforeCopyStory = storyCreation.numberOfRecords("CountOfStories");
    }
    @And("I select a date that has stories")
    public void iSelectADateThatHasStories() throws Exception {
        String Date2 = (String) getDates().get("dateForUpdateStories");
        StoryCreation storyCreation = new StoryCreation();
        storyCreation.calender(Date2);
        storyCreation.update();
        List<WebElement> noItemsList = Browser.getDriver(By.cssSelector("story-preview")).findElements(By.xpath("//div[text()='No items found']"));
        if (!noItemsList.isEmpty() && noItemsList.get(0).isDisplayed()) {
            storyCreation.selectPastItems();
            storyCreation.update();
        }
    }
    @And("I navigate to the CopyOfStories method with parameters")
    public void iNavigateToTheCopyOfStoriesMethodWithParameters() throws Exception {
        String Date = (String) getDates().get("toDateForCopyOfStories");
        StoryCreation storyCreation = new StoryCreation();
        storyCreation.copyOfStories(Date, "test");
    }
    @Then("I verify that the story has been copied successfully")
    public void iVerifyThatTheStoryHasBeenCopiedSuccessfully() throws Exception {
        String Date = (String) getDates().get("toDateForCopyOfStories");
        StoryCreation storyCreation = new StoryCreation();
        calender(Date);
        storyCreation.update();
        afterCopyStory = storyCreation.numberOfRecords("CountOfStories");
        storyCreation.update();
        System.out.println(beforeCopyStory);
        System.out.println(afterCopyStory);
        storyCreation.verifyCopyOfStories(beforeCopyStory, afterCopyStory, "Copy");
    }
    @And("I note the count of InteractiveAds before the copy")
    public void iNoteTheCountOfInteractiveAdsBeforeTheCopy() throws Exception {
        String Date = (String) getDates().get("toDateForInteractiveAds");
        StoryCreation storyCreation = new StoryCreation();
        storyCreation.calender(Date);
        InteractiveAds interActiveAds = new InteractiveAds();
        interActiveAds.clickUpdateOnInteractiveAdsPage();
        BeforeCopy_InteractiveAds = storyCreation.numberOfRecords("CountOfInteractiveAds");
    }
    @And("I select a date that has Interactive Ad")
    public void iSelectADateThatHasInteractiveAd() throws Exception {
        String Date = (String) getDates().get("fromDateForInteractiveAds");
        StoryCreation storyCreation = new StoryCreation();
        storyCreation.calender(Date);
        InteractiveAds interactiveAds = new InteractiveAds();
        interactiveAds.clickUpdateOnInteractiveAdsPage();
        List<WebElement> noItemsList = Browser.getDriver(By.cssSelector("story-preview")).findElements(By.xpath("//div[text()='No items found']"));
        if (!noItemsList.isEmpty() && noItemsList.get(0).isDisplayed()) {
            storyCreation.selectPastItems();
            interactiveAds.clickUpdateOnInteractiveAdsPage();
        }
    }
    @When("I navigate to the CopyOfInteractive method with parameters")
    public void iNavigateToTheCopyOfInteractiveMethodWithParameters() throws Exception {
        String Date = (String) getDates().get("toDateForInteractiveAds");
        InteractiveAds interactiveAds = new InteractiveAds();
        interactiveAds.copyOfAds(Date);
    }
    @Then("I verify that the Interactive ad has been copied successfully")
    public void iVerifyThatTheInteractiveAdHasBeenCopiedSuccessfully() throws Exception {
        shortWait();
        String Date = (String) getDates().get("toDateForInteractiveAds");
        StoryCreation storyCreation = new StoryCreation();
        storyCreation.calender(Date);
        InteractiveAds interactiveAds = new InteractiveAds();
        interactiveAds.clickUpdateOnInteractiveAdsPage();
        AfterCopy_InteractiveAds = storyCreation.numberOfRecords("CountOfInteractiveAds");
        interactiveAds.clickUpdateOnInteractiveAdsPage();
        storyCreation.verifyCopyOfStories(BeforeCopy_InteractiveAds, AfterCopy_InteractiveAds, "Copy");
    }
    @And("I note the count of RichMedia before the copy")
    public void iNoteTheCountOfRichMediaBeforeTheCopy() throws Exception {
        String Date = (String) getDates().get("toDateForRichMediaAds");
        StoryCreation storyCreation = new StoryCreation();
        storyCreation.calender(Date);
        RichMediaAds richMediaAds = new RichMediaAds();
        richMediaAds.clickUpdateOnRichMedia();
        BeforeCopy_RichMediaAds = storyCreation.numberOfRecords("CountOfRichMediaAds");
    }
    @And("I select a date that has RichMedia Ad")
    public static void iSelectADateThatHasRichMediaAd() throws Exception {
        String Date = (String) getDates().get("fromDateForRichMediaAds");
        shortWait();
        StoryCreation storyCreation = new StoryCreation();
        storyCreation.calender(Date);
        RichMediaAds richMediaAds = new RichMediaAds();
        richMediaAds.clickUpdateOnRichMedia();
        List<WebElement> noItemsList = Browser.getDriver(By.cssSelector("story-preview")).findElements(By.xpath("//div[text()='No items found']"));
        if (!noItemsList.isEmpty() && noItemsList.get(0).isDisplayed()) {
            storyCreation.selectPastItems();
            richMediaAds.clickUpdateOnRichMedia();
        }
    }
    @When("I navigate to the CopyOfRichMedia method with parameters")
    public void iNavigateToTheCopyOfRichMediaMethodWithParameters() throws Exception {
        RichMediaAds richMediaAds = new RichMediaAds();
        String Date = (String) getDates().get("toDateForRichMediaAds");
        richMediaAds.CopyOfAds(Date, "test");
    }
    @Then("I verify that the RichMedia ad has been copied successfully")
    public void iVerifyThatTheRichMediaAdHasBeenCopiedSuccessfully() throws Exception {
        String date = (String) getDates().get("toDateForRichMediaAds");
        StoryCreation storyCreation = new StoryCreation();
        storyCreation.calender(date);
        RichMediaAds richMediaAds = new RichMediaAds();
        richMediaAds.clickUpdateOnRichMedia();
        AfterCopy_RichMediaAds = storyCreation.numberOfRecords("CountOfRichMediaAds");
        richMediaAds.clickUpdateOnRichMedia();
        storyCreation.verifyCopyOfStories(BeforeCopy_RichMediaAds, AfterCopy_RichMediaAds, "Copy");
    }
    @And("I note the count of stories before delete")
    public void iNoteTheCountOfStoriesBeforeDelete() throws Exception {
        StoryCreation storyCreation = new StoryCreation();
        String Date = (String) getDates().get("dateForStories");
        storyCreation.calender(Date);
        storyCreation.selectCount();
        storyCreation.update();
        List<WebElement> noItemsList = Browser.getDriver(By.cssSelector("story-preview")).findElements(By.xpath("//div[text()='No items found']"));
        if (!noItemsList.isEmpty() && noItemsList.get(0).isDisplayed()) {
            System.out.println("No items found – nothing to process.");
        } else {
            beforeCopyStory = storyCreation.numberOfRecords("CountOfStories");
        }
    }
    @And("I navigate to the delete method with parameters")
    public void iNavigateToTheDeleteMethodWithParameters() throws Exception {
        List<WebElement> noItemsList = Browser.getDriver(By.cssSelector("story-preview")).findElements(By.xpath("//div[text()='No items found']"));
        if (!noItemsList.isEmpty() && noItemsList.get(0).isDisplayed()) {
            StoryCreation storyCreation = new StoryCreation();
            storyCreation.selectPastItems();
            storyCreation.update();
            beforeCopyStory = storyCreation.numberOfRecords("CountOfStories");
        }
        StoryCreation storyCreation = new StoryCreation();
        countBeforeDeleteStory = storyCreation.deleteStories();
        shortWait();
    }
    @Then("I verify that the story has been deleted successfully")
    public void iVerifyThatTheStoryHasBeenDeletedSuccessfully() throws InterruptedException {
        StoryCreation storyCreation = new StoryCreation();
        shortWait();
        if (countBeforeDeleteStory.equalsIgnoreCase("0")) {
            countBeforeDeleteStory = beforeCopyStory;
            System.out.println("beforeDeleteStory: " + countBeforeDeleteStory);
        }
        afterCopyStory = storyCreation.numberOfRecords("CountOfStories");
        storyCreation.verifyCopyOfStories(countBeforeDeleteStory, afterCopyStory, "Delete");
    }
    @And("I note the count of Ad before delete")
    public void iNoteTheCountOfAdBeforeDelete() throws Exception {
        String Date = (String) getDates().get("fromDateForInteractiveAds");
        StoryCreation storyCreation = new StoryCreation();
        storyCreation.calender(Date);
        InteractiveAds interactiveAds = new InteractiveAds();
        interactiveAds.clickUpdateOnInteractiveAdsPage();
        List<WebElement> noItemsList = Browser.getDriver(By.cssSelector("story-preview")).findElements(By.xpath("//div[text()='No items found']"));
        if (!noItemsList.isEmpty() && noItemsList.get(0).isDisplayed()) {
            System.out.println("No items found – nothing to process.");
        } else {
            BeforeCopy_InteractiveAds = storyCreation.numberOfRecords("CountOfInteractiveAds");
        }
    }
    @And("I Should delete the InterActiveAd")
    public void iShouldDeleteTheInteractivead() throws Exception {
        List<WebElement> noItemsList = Browser.getDriver(By.cssSelector("story-preview")).findElements(By.xpath("//div[text()='No items found']"));
        if (!noItemsList.isEmpty() && noItemsList.get(0).isDisplayed()) {
            StoryCreation storyCreation = new StoryCreation();
            storyCreation.selectPastItems();
            InteractiveAds interactiveAds = new InteractiveAds();
            interactiveAds.clickUpdateOnInteractiveAdsPage();
            BeforeCopy_InteractiveAds = storyCreation.numberOfRecords("CountOfInteractiveAds");
        }
        InteractiveAds interactiveAds = new InteractiveAds();
        interactiveAds.DeleteOfAds();
    }
    @Then("I verify that the Interactive ad  has been deleted successfully")
    public void iVerifyThatTheInteractiveAdHasBeenDeletedSuccessfully() throws InterruptedException {
        StoryCreation storyCreation = new StoryCreation();
        shortWait();
        AfterCopy_InteractiveAds = storyCreation.numberOfRecords("CountOfInteractiveAds");
        storyCreation.verifyCopyOfStories(BeforeCopy_InteractiveAds, AfterCopy_InteractiveAds, "Delete");
    }
    @And("I note the count of RichMediaAd before delete")
    public void iNoteTheCountOfRichMediaAdBeforeDelete() throws Exception {
        String Date = (String) getDates().get("fromDateForRichMediaAds");
        StoryCreation storyCreation = new StoryCreation();
        storyCreation.calender(Date);
        RichMediaAds richMediaAds = new RichMediaAds();
        richMediaAds.clickUpdateOnRichMedia();
        List<WebElement> noItemsList = Browser.getDriver(By.cssSelector("story-preview")).findElements(By.xpath("//div[text()='No items found']"));
        if (!noItemsList.isEmpty() && noItemsList.get(0).isDisplayed()) {
            System.out.println("No items found – nothing to process.");
        } else {
            BeforeCopy_RichMediaAds = storyCreation.numberOfRecords("CountOfRichMediaAds");
        }
    }
    @And("I Should delete the RichMediaAd")
    public void iShouldDeleteTheRichMediaAd() throws Exception {
        List<WebElement> noItemsList = Browser.getDriver(By.cssSelector("story-preview")).findElements(By.xpath("//div[text()='No items found']"));
        if (!noItemsList.isEmpty() && noItemsList.get(0).isDisplayed()) {
            StoryCreation storyCreation = new StoryCreation();
            storyCreation.selectPastItems();
            RichMediaAds richMediaAds = new RichMediaAds();
            richMediaAds.clickUpdateOnRichMedia();
            BeforeCopy_RichMediaAds = storyCreation.numberOfRecords("CountOfRichMediaAds");
        }
        RichMediaAds richMediaAds = new RichMediaAds();
        richMediaAds.DeleteOfAds();
    }
    @Then("I verify that the RichMediaAd has been deleted successfully")
    public void iVerifyThatTheRichMediaAdHasBeenDeletedSuccessfully() throws InterruptedException {
        shortWait();
        StoryCreation storyCreation = new StoryCreation();
        AfterCopy_RichMediaAds = storyCreation.numberOfRecords("CountOfRichMediaAds");
        storyCreation.verifyCopyOfStories(BeforeCopy_RichMediaAds, AfterCopy_RichMediaAds, "Delete");
    }
    @And("I note the count of Page before delete")
    public void iNoteTheCountOfPageBeforeDelete() throws Exception {
        String Date = (String) getDates().get("fromDateForPages");
        StoryCreation storyCreation = new StoryCreation();
        storyCreation.calender(Date);
        PrintPages printPages = new PrintPages();
        printPages.clickUpdateOnPrintPages();
        List<WebElement> noItemsList = Browser.getDriver(By.cssSelector("story-preview")).findElements(By.xpath("//div[text()='No items found']"));
        if (!noItemsList.isEmpty() && noItemsList.get(0).isDisplayed()) {
            System.out.println("No items found – nothing to process.");
        } else {

            BeforeCopy_Pages = storyCreation.numberOfRecords("CountOfPages");
        }
    }
    @And("I Should delete the Page")
    public void iShouldDeleteThePage() throws Exception {
        List<WebElement> noItemsList = Browser.getDriver(By.cssSelector("story-preview")).findElements(By.xpath("//div[text()='No items found']"));
        if (!noItemsList.isEmpty() && noItemsList.get(0).isDisplayed()) {
            StoryCreation storyCreation = new StoryCreation();
            storyCreation.selectPastItems();
            PrintPages printpages = new PrintPages();
            printpages.clickUpdateOnPrintPages();
            BeforeCopy_Pages = storyCreation.numberOfRecords("CountOfPages");
        }
        PrintPages printpages = new PrintPages();
        printpages.DeleteOfPages();
    }
    @Then("I verify that the Page has been deleted successfully")
    public void iVerifyThatThePageHasBeenDeletedSuccessfully() throws InterruptedException {

        StoryCreation storyCreation = new StoryCreation();
        AfterCopy_Pages = storyCreation.numberOfRecords("CountOfPages");
        storyCreation.verifyCopyOfStories(BeforeCopy_Pages, AfterCopy_Pages, "Delete");
    }
    @And("I navigate to ad")
    public void iNavigateToAd() throws Exception {
        String Date = (String) getDates().get("fromDate");
        StoryCreation storyCreation = new StoryCreation();
        storyCreation.calender(Date);
        InteractiveAds interactiveAds = new InteractiveAds();
        interactiveAds.clickUpdateOnInteractiveAdsPage();
        List<WebElement> noItemsList = Browser.getDriver(By.cssSelector("story-preview")).findElements(By.xpath("//div[text()='No items found']"));
        if (!noItemsList.isEmpty() && noItemsList.get(0).isDisplayed()) {
            storyCreation.selectPastItems();
            interactiveAds.clickUpdateOnInteractiveAdsPage();
        }
    }
    @And("I Should download the InteractiveAd")
    public void iShouldDownloadTheInteractiveAd() throws Exception {
        downloadOfAds();

    }
    @And("I navigate to RichMedia ad")
    public void iNavigateToRichMediaAd() throws Exception {
        String Date = (String) getDates().get("fromDate");
        StoryCreation storyCreation = new StoryCreation();
        storyCreation.calender(Date);
        RichMediaAds richMediaAds = new RichMediaAds();
        richMediaAds.clickUpdateOnRichMedia();
        List<WebElement> noItemsList = Browser.getDriver(By.cssSelector("story-preview")).findElements(By.xpath("//div[text()='No items found']"));
        if (!noItemsList.isEmpty() && noItemsList.get(0).isDisplayed()) {
            storyCreation.selectPastItems();
            richMediaAds.clickUpdateOnRichMedia();
        }
    }
    @And("I Should download the RichMedia Ad")
    public void iShouldDownloadTheRichMediaAd() throws Exception {
        downloadOfAdsInRichMediaAds();
    }
    @And("I select the Channel as {string} for the page")
    public void iSelectTheChannelAsForThePage(String channel) {
        onPage("CMS PrintPages");
        Actions.performAction("select_Channel", channel);
    }
    @When("I enter the publish date for the page")
    public void iEnterThePublishDateForThePage() throws Exception
    {
        shortWait();
        Thread.sleep(100000);
    }
    @And("I tap on Layout")
    public void iTapOnLayout() throws Exception {
        tap_Layout();
    }
    @Then("the Layout page should be displayed")
    public void theLayoutPageShouldBeDisplayed() {
        onPage("CMS OutputOfPrintPages");
        Assertions.assertTrue(output.isDisplayed());
    }
    @And("Drag and drop the story on page and adjust the story")
    public void dragAndDropTheStoryOnPageAndAdjustTheStory() throws InterruptedException {
        drag_Drop("story");
    }
    @And("I Note the count of pages before the copy of page")
    public void iNoteTheCountOfPagesBeforeTheCopyOfPage() throws Exception {
        String Date = (String) getDates().get("toDateForPages");
        StoryCreation storyCreation = new StoryCreation();
        storyCreation.calender(Date);
        PrintPages printPages = new PrintPages();
        printPages.clickUpdateOnPrintPages();
        BeforeCopy_Pages = storyCreation.numberOfRecords("CountOfPages");
        System.out.println("Before :" + BeforeCopy_Pages);
    }
    @And("I select a date that has pages")
    public void iSelectADateThatHasPages() throws Exception {
        String Date = (String) getDates().get("fromDateForPages");
        StoryCreation storyCreation = new StoryCreation();
        shortWait();
        storyCreation.calender(Date);
        clickUpdateOnPrintPages();
        List<WebElement> noItemsList = Browser.getDriver(By.cssSelector("story-preview")).findElements(By.xpath("//div[text()='No items found']"));
        if (!noItemsList.isEmpty() && noItemsList.get(0).isDisplayed()) {
            storyCreation.selectPastItems();
            clickUpdateOnPrintPages();
        }
    }
    @And("I navigate to the CopyOfPage method with parameters")
    public void iNavigateToTheCopyOfPageMethodWithParameters() throws Exception {
        String Date = (String) getDates().get("toDateForPages");
        CopyOfPages(Date);
    }
    @Then("I verify that the page has been copied successfully")
    public void iVerifyThatThePageHasBeenCopiedSuccessfully() throws Exception
    {
        shortWait();
        String Date = (String) getDates().get("toDateForPages");
        StoryCreation storyCreation = new StoryCreation();
        storyCreation.calender(Date);
        clickUpdateOnPrintPages();
        AfterCopy_Pages = storyCreation.numberOfRecords("CountOfPages");
        clickUpdateOnPrintPages();
        storyCreation.verifyCopyOfStories(BeforeCopy_Pages, AfterCopy_Pages, "Copy");
    }
    @And("I select the date that has edition")
    public void iSelectTheDateThatHasEdition() throws Exception {
        String Date = (String) getDates().get("fromDate");
        StoryCreation storyCreation = new StoryCreation();
        storyCreation.calender(Date);
        clickUpdateOnPrintPages();
    }
    @Then("Open any existing page")
    public void openAnyExistingPage() throws Exception {
        onPage("CMS OutputOfPrintPages");
        tapOnPage();

    }
    @And("Verify the page is opened")
    public void verifyThePageIsOpened() {
        verifyPageIsOpened();
    }
    @And("I select PDF Proof to desktop and i tap on ok button")
    public void iSelectPDFProoftodesktopAndITapOnOkButton() throws Exception {
        PDFProofToDesktop();
    }
    @Then("A dialog  will open once complete popup should be displayed")
    public void aDialogWillOpenOnceCompletePopupShouldBeDisplayed()
    {
        dialogueShouldDisplay();
    }
    @And("Your pdf is ready popup should be displayed")
    public void yourPdfIsReadyPopupShouldBeDisplayed() {
        pdfIsReadyPopup();
    }
    @And("Tap on click this link to open pdf")
    public void tapOnClickThisLinkToOpenPdf() throws Exception {
        pdfLink();
    }
    @And("I select the date which has rich media ads")
    public void iSelectTheDateWhichHasRichmediaAds() throws Exception {
        String Date = (String) getDates().get("fromDateForRichMediaAds");
        ProductionView productionView = new ProductionView();
        shortWait();
        onPage("CMS ProductionView");
        productionView.selectDateWhichHasStory(Date);
        shortWait();
        onPage("CMS ProductionView");
    }
    @And("I turn {string} rich media edit mode")
    public void iTurnRichMediaEditMode(String richMediaEditMode) throws Exception {
        selectRichMediaEditMode(richMediaEditMode);
    }
    @Then("verify the Rich Ad panel is displayed")
    public void verifyTheRichAdpanelIsDisplayed() throws InterruptedException {
        richAdPanel();
        adsAreDisplayed();
    }
    @And("I click on {string} element for {string}")
    public void iClickOnElementFor(String element, String arg1) {
        jsClick(element);
    }
    @And("I click save and close button on {string}")
    public void iClickSaveAndCloseButtonOn(String pageClassName) throws InterruptedException {
        onPage(pageClassName);
        shortWait();
        Browser.getDriver(By.cssSelector("story-preview")).switchTo().defaultContent();
//        wait.until(ExpectedConditions.elementToBeClickable(By.xpath())));
//        jsClick("saveAndCloseButton");
        shortWait();
        jsClick("saveAndCloseButton");
        shortWait();
        shortWait();
        List<WebElement> saveContinueList = Browser.getDriver(By.cssSelector("story-preview"))
                .findElements(By.xpath("//span[text()='Save & Continue']"));

        if (!saveContinueList.isEmpty()) {
            WebElement saveContinue = saveContinueList.get(0);
            jsClick(saveContinue);
            shortWait();
            jsClick("saveAndCloseButton");
        }
    }
    @When("I navigate to {string} from {string} on {string}")
    public void iNavigateToFromOn(String subMenu, String mainMenu, String pageClassName) throws Exception {
        onPage(pageClassName);
        clickOnMainMenu(mainMenu);
        clickSubMenu(subMenu);
    }
    @And("I switch to {string} tab on {string} page")
    public void iSwitchToTabOnPage(String tabName, String pageClassName) throws Exception {
        onPage(pageClassName);
        click(Browser.getDriver(By.cssSelector("story-preview")).findElement(By.xpath("//a[text()='" + tabName + "']")));
    }
    @And("I switch to tab on {string} page")
    public void iSwitchToTabOnPage(String pageClassName) throws Exception {
        onPage(pageClassName);
        String currentProject = getCustomerEnv();
        String tabName;
        if (currentProject.equals("PG")) {
            tabName = "PG Print";
        } else {
            tabName = "Print";
        }
        click(Browser.getDriver(By.cssSelector("story-preview")).findElement(By.xpath("//a[text()='" + tabName + "']")));

    }
    @And("I upload an image for {string}")
    public void iUploadAnImageFor(String key) throws InterruptedException {
        String Image = (String) getInteractiveAdsData(key).get("Image");
        addImageOnInteractivePage(Image);
    }
    @When("I upload a ZIP file for {string}")
    public void iUploadAZIPFileFor(String key) throws InterruptedException {
        String ZipFile = (String) getInteractiveAdsData(key).get("ZipFile");
        addZipFile(ZipFile);
    }
    @And("I upload an image for rich media ads {string}")
    public void iUploadAnImageForRichMediaAds(String key) throws InterruptedException {
        String Image = (String) getRichMediaAdData(key).get("Image");
        addImageOnRichMediaAds(Image);
    }
    @When("I upload a ZIP file for rich media ads {string}")
    public void iUploadAZIPFileForRichMediaAds(String key) throws InterruptedException {
        String ZipFile = (String) getRichMediaAdData(key).get("ZipFile");
        addZipFileOnRichMediaAds(ZipFile);
    }
    @Then("I should verify the Ad is downloaded")
    public void iShouldVerifyTheAdIsDownloaded() {
        verifyDownload();
    }
    @When("I delete rich ads")
    public void iDeleteRichAds() {
    }
    @Then("i shouldn't see rich ad in the list")
    public void iShouldnTSeeRichAdInTheList() {
    }
    String titleValue;
    @And("I enter the publish date,Title,Caption for the media files")
    public void iEnterThePublishDateTitleCaptionForTheMediaFiles() throws Exception {
        enterPublishDate("12/01/2025");
        String title = (String) getImageData("Image1").get("Title");
        String caption = (String) getImageData("Image1").get("Caption");
        MediaFiles mediaFiles = new MediaFiles();
        titleValue = mediaFiles.enterTitle(title);
        enterCaption(caption);
    }
    @And("I upload an image for Media files")
    public void iUploadAnImageForMediaFiles() throws InterruptedException {
        String relativePath = (String) getImageData("Image1").get("Image");
        String absolutePath = Paths.get(relativePath).toAbsolutePath().toString();
        uploadImage(absolutePath);
    }
    @Then("I verify that the image is uploaded successfully for the media files")
    public void iVerifyThatTheImageIsUploadedSuccessfullyForTheMediaFiles() {
        verifyimageIsUploaded();
    }
    @And("Verify that the media file is created")
    public void verifyThatTheMediaFileIsCreated() throws Exception {
        StoryCreation storyCreation = new StoryCreation();
        storyCreation.calender("12/01/2025");
        MediaFiles mediaFiles = new MediaFiles();
        mediaFiles.verifyMediaFileisCreated(titleValue);
    }
    @And("I enter text in search field")
    public void iEnterTextInSearchField() throws Exception {
        searchText();
    }
    @And("Verify the search results are displayed")
    public void verifyTheSearchResultsAreDisplayed() {
        verifySearchResults();
    }
    @And("Verify the search results are displayed for section letter")
    public void verifyTheSearchResultsAreDisplayedForSectionLetter() throws Exception {
        VerifySearchonSectionLetter();
    }
    @And("I enter text in search field of MediaFiles")
    public void iEnterTextInSearchFieldOfMediaFiles() throws InterruptedException {
        searchTextOfMediaFiles();
    }
    @And("Verify the search results are displayed for MediaFiles")
    public void verifyTheSearchResultsAreDisplayedForMediaFiles() throws InterruptedException {
        verifySearchResultsOfMediaFiles();
    }
    @And("I enter text in search field of interactive ads")
    public void iEnterTextInSearchFieldOfInteractiveAds() throws Exception {
        searchTextOfInteractiveAd();
    }
    @And("Verify the search results are displayed for Interactive ads")
    public void verifyTheSearchResultsAreDisplayedForInteractiveAds() throws Exception {
        verifySearchResultsOfInteractiveAd();
    }
    @And("verify that the {string} checkbox is selected for Interactive Ads.")
    public void verifyThatTheCheckboxIsSelectedForInteractiveAds(String device) throws Exception {
        VerifyDeviceTypeSearchResults(device);
    }
    @And("I enter text in search field of Richmedia ad")
    public void iEnterTextInSearchFieldOfRichmediaAd() throws Exception {
        searchTextOfRichMediaAd();
    }
    @And("Verify the search results are displayed for Richmedia ad")
    public void verifyTheSearchResultsAreDisplayedForRichmediaAd() throws Exception {
        verifySearchResultsOfRichMediaAd();
    }
    @Then("verify that the {string} checkbox is selected for RichMedia Ads.")
    public void verifyThatTheCheckboxIsSelectedForRichMediaAds(String device) throws Exception {
        VerifyDeviceTypeSearchResultsForRichMediaAds(device);
    }
    @Then("Verify the {string} is selected as adType")
    public void verifyTheIsSelectedAsAdType(String adType) throws Exception {
        VerifyAdTypeSearchResultsForRichMediaAds(adType);
    }
    @And("I enter text in search field of Printpages")
    public void iEnterTextInSearchFieldOfPrintpages() throws Exception {
        searchTextOfPrintpages();
    }
    @And("Verify the search results are displayed for Printpages")
    public void verifyTheSearchResultsAreDisplayedForPrintpages() throws Exception {
        verifySearchResultsOfPrintpages();
    }
    @And("Verify the search results are displayed for Printpages on section letter")
    public void verifyTheSearchResultsAreDisplayedForPrintpagesOnSectionLetter() throws Exception {
        verifySectoionLetterSearchForPages();
    }
    String storyId;
    @And("I open any available story")
    public void iOpenAnyAvailableStory() throws InterruptedException {
        storyId = selectStory();
    }
    @When("I update the {string} field of the story")
    public void iUpdateTheFieldOfTheStory(String updateField) throws Exception {
        storyUpdate(updateField);
    }
    @Then("I should verify that the story has been updated successfully")
    public void iShouldVerifyThatTheStoryHasBeenUpdatedSuccessfully() throws InterruptedException {
        verifyStoryUpdate(storyId);
    }
    @And("I open any available Interactive Ad")
    public void iOpenAnyAvailableInteractiveAd() throws Exception
    {
        List<WebElement> noItemsList = Browser.getDriver(By.cssSelector("story-preview")).findElements(By.xpath("//div[text()='No items found']"));
        if (!noItemsList.isEmpty() && noItemsList.get(0).isDisplayed())
        {
            StoryCreation storyCreation = new StoryCreation();
            storyCreation.selectPastItems();
            InteractiveAds interactiveAds = new InteractiveAds();
            clickUpdateOnInteractiveAdsPage();
        }
        selectInteractiveAd();
    }
    @When("I update the {string} field of the Interactive Ad")
    public void iUpdateTheFieldOfTheInteractiveAd(String fieldName) throws InterruptedException {
        interactiveAdUpdate(fieldName);
    }
    @Then("I should verify that the {string} field of the interactiveAd has been updated successfully")
    public void iShouldVerifyThatTheFieldOfTheInteractiveAdHasBeenUpdatedSuccessfully(String fieldName ) throws InterruptedException {
        verifyInteractiveAdUpdate(fieldName);
    }
    @And("I select a date that has MediaFiles")
    public static void iSelectADateThatHasMediaFiles() throws Exception {
        String Date2 = (String) getDates().get("fromDateForCopyOfStories");
        StoryCreation storyCreation = new StoryCreation();
        storyCreation.calender(Date2);
        MediaFiles mediaFiles = new MediaFiles();
        mediaFiles.updateButton.click();
        List<WebElement> noItemsList = Browser.getDriver(By.cssSelector("story-preview")).findElements(By.xpath("//div[text()='No items found']"));
        if (!noItemsList.isEmpty() && noItemsList.get(0).isDisplayed()) {
            storyCreation.selectPastItems();
            storyCreation.update();
        }
    }
    @And("I open any available RichMedia Ad")
    public void iOpenAnyAvailableRichMediaAd() throws Exception {
        List<WebElement> noItemsList = Browser.getDriver(By.cssSelector("story-preview")).findElements(By.xpath("//div[text()='No items found']"));
        if (!noItemsList.isEmpty() && noItemsList.get(0).isDisplayed())
        {
            StoryCreation storyCreation = new StoryCreation();
            storyCreation.selectPastItems();
            clickUpdateOnRichMedia();
        }
        selectRichMediaAd();
    }
    @When("I update the {string} field of the RichMedia Ad")
    public void iUpdateTheFieldOfTheRichMediaAd(String filedName) throws InterruptedException {
        richMediAdUpdate(filedName);
    }
    @Then("I should verify that the {string} field of the RichMedia has been updated successfully")
    public void iShouldVerifyThatTheFieldOfTheRichMediaHasBeenUpdatedSuccessfully(String fieldName) throws InterruptedException {
        verifyRichMediaAdUpdate(fieldName);
    }
    @And("I open any available MediaFile")
    public void iOpenAnyAvailableMediaFile() throws InterruptedException {
        selectMediaFile();
    }
    @When("I update the {string} field of the MediaFile")
    public void iUpdateTheFieldOfTheMediaFile(String fieldName) throws InterruptedException {
        mediaFileUpdate(fieldName);
    }
    @Then("I should verify that the {string} field of the MediaFile has been updated successfully")
    public void iShouldVerifyThatTheFieldOfTheMediaFileHasBeenUpdatedSuccessfully(String fieldName) throws InterruptedException
    {
        verifyMediadUpdate(fieldName);
    }
    @And("I open any available Page")
    public void iOpenAnyAvailablePage() throws InterruptedException {
        selectPageForUpdate();
    }
    @When("I update the {string} in Printpages")
    public void iUpdateTheInPrintpages(String fieldName) throws InterruptedException {
        printPageUpdate(fieldName);
    }
    @Then("I should verify that the {string} has been updated successfully")
    public void iShouldVerifyThatTheHasBeenUpdatedSuccessfully(String fieldName) throws InterruptedException {
        verifyPageUpdate(fieldName);
    }

    @And("I type PageNumber in text box {string}")
    public void iTypePageNumberInTextBox(String key)
    {
        pageNumber(key);
    }

    @When("^I navigate to \"([^\"]*)\"$")
    public void iNavigateTo(String key) throws Throwable
    {
        TapOnPrintPages();
    }

    @When("I will verify the customer")
    public void iWillVerifyTheCustomer()
    {

    }

    @And("I select a story from the stories tab")
    public void iSelectAStoryFromTheStoriesTab()
    {
        selectStoryFromStories();
    }
}
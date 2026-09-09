package test.automation.pages.Libercus.CMS;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import test.automation.framework.Browser;
import test.automation.framework.Page;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static test.automation.framework.Actions.*;
import static test.automation.framework.Random.getRandomIndex;
import static test.automation.pages.Libercus.CMS.HomePage.content;
import static test.automation.pages.Libercus.CMS.MediaFiles.updateButton;
import static test.automation.pages.Libercus.CMS.StoryCreation.*;
public class WireStories extends Page
{
    @FindBy(xpath = "//select[@name='TransmittedDateRange']")
    public static WebElement TransmittedDateRange;
    @FindBy(xpath = "//button[@class='libButtonRefresh ui-button ui-widget ui-state-default ui-corner-all ui-button-text-icon-primary btnFixed']//span[@class='ui-button-text'][normalize-space()='Update']")
    public static WebElement UpdateButton;
    @FindBy(xpath = "//div[@class='libCheckColumn libNoClick']/input")
    public static List<WebElement> listOfStories;
    @FindBy(xpath = "//button[@class='lib-button-assign ui-button ui-widget ui-state-default ui-corner-all ui-button-text-icon-primary btnFixed']//span[@class='ui-button-text'][text()='Use']")
    public static WebElement Use;
    @FindBy(xpath = "//div[@class='ui-dialog ui-widget ui-widget-content ui-corner-all ui-front ui-dialog-buttons ui-draggable']")
    public static WebElement popup;
    @FindBy(xpath = "//div[@class='ui-datepicker-title']")
    public static WebElement MonthYear;
    @FindBy(xpath = "//select[@name='channels']")
    public static WebElement channels;
    @FindBy(xpath = "//input[@name='skipusingphotos']")
    public static WebElement omitPhotos;
    @FindBy(xpath = "//button[@class='ui-button ui-widget ui-state-default ui-corner-all ui-button-text-icon-primary']//span[@class='ui-button-text'][text()='Use']")
    public static WebElement useButton;
    @FindBy(xpath = "//input[@name='slug']")
    public static WebElement slug;
    @FindBy(xpath = "//span[text()='Prev']")
    public WebElement previous;
    @FindBy(xpath = "//span[@class='ui-icon ui-icon-circle-triangle-e']")
    public WebElement Next;
    @FindBy(xpath = "//div[text()='This item has already been selected for use. Would you like to make another copy?']")
    public static WebElement dialog;
    @FindBy(xpath = "//span[text()='Yes']")
    public static WebElement yesButton;
    @FindBy(xpath = "//input[@name='publishdate']")
    public static WebElement calender;
    @FindBy(xpath = "//div[@class='libListContentPanelTitle']")
    public static WebElement titleInput;
    public static void verifyStoriesAreAvailable() throws Exception
    {
        UpdateButton.click();
        shortWait();
        List<WebElement> noItemsList = Browser.getDriver(By.cssSelector("story-preview")).findElements(By.xpath("//div[text()='No items found']"));
        if (!noItemsList.isEmpty() && noItemsList.get(0).isDisplayed())
        {
            Select select = new Select(TransmittedDateRange);
            Assertions.assertTrue(isDisplayed(TransmittedDateRange));
            select.selectByVisibleText("Last 3 Months");
            UpdateButton.click();
            shortWait();
        }
    }
    public static int randomNumber;
    public static void selectStory() throws InterruptedException {
        randomNumber=getRandomIndex(listOfStories.size());
        if(randomNumber==0)
        {
            randomNumber=getRandomIndex(listOfStories.size());
        }
        System.out.println("Random number is: " + randomNumber);
        System.out.println(listOfStories.size());
        jsClick(listOfStories.get(randomNumber));
    }
    public static void useStoryPopup() throws InterruptedException
    {
        popup.isDisplayed();
        if (!Browser.getDriver(By.cssSelector("story-preview")).findElements(By.xpath("//div[text()='This item has already been selected for use. Would you like to make another copy?']")).isEmpty()) {
            WebElement dialog = Browser.getDriver(By.cssSelector("story-preview")).findElement(By.xpath("//div[text()='This item has already been selected for use. Would you like to make another copy?']"));
            if (dialog.isDisplayed()) {
                yesButton.click();
            }
        }
    }
    public static String publishDate="";
    public static void enterPublishDate(String date) throws ParseException, InterruptedException
    {
        calender.click();
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date1 = inputFormat.parse(date);
        publishDate=date;
        SimpleDateFormat dayFormat = new SimpleDateFormat("d");
        String dateNumber = dayFormat.format(date1);
        int day = Integer.parseInt(dateNumber);
        SimpleDateFormat monthYearFormat = new SimpleDateFormat("MMMM yyyy");
        String monthYear = monthYearFormat.format(date1);
        int dateToSelect = day;
        String monthYearToSelect = monthYear;
        while (true) {
            String displayedMonthYear = Browser.getDriver(By.cssSelector("story-preview")).findElement(By.xpath("//div[@class='ui-datepicker-title']")).getText();
            if (displayedMonthYear.equals(monthYearToSelect)) {
                break;
            }
            if (isBefore(displayedMonthYear, monthYearToSelect)) {
                jsClick("previous");
            } else {
                click("Next");
            }
        }
        WebElement select_date = Browser.getDriver(By.cssSelector("story-preview")).findElement(By.xpath("//a[text()='" + dateToSelect + "' and contains(@class,'ui-state-default')]"));
        select_date.click();
    }
    public static String slugText="";
    public static void tapOnCheckbox() throws InterruptedException {
        slugText=slug.getAttribute("value");
        System.out.println("publish date: " + publishDate);
        System.out.println("slug text: " + slugText);
        omitPhotos.click();
    }
    public static String title="";
    public static void tapOnUseButton() throws Exception
    {
        jsClick(useButton);
        waitUntilElementPresent(content,10000);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("libOverlayLoader")));

    }
    public static void enterPublishdateForStories(String moduleName) throws Exception
    {
        StoryCreation storyCreation = new StoryCreation();
        storyCreation.calender(publishDate);
        if(moduleName.equalsIgnoreCase("Stories"))
        {
            update();
        } else if (moduleName.equalsIgnoreCase("MediaFiles"))
        {
            updateButton.click();
        }
    }
    public static void enterslugText() throws Exception {
        searchTextBox.sendKeys(slugText);
        update();
    }
    public static void enterTitle()
    {
        MediaFiles.searchTextBox.sendKeys(title);
        updateButton.click();
    }
}
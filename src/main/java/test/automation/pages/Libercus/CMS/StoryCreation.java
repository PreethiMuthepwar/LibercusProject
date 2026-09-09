package test.automation.pages.Libercus.CMS;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import test.automation.framework.Browser;
import test.automation.framework.Page;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.Date;
import java.util.List;
import static test.automation.framework.Actions.*;
import static test.automation.framework.Random.*;
import static test.automation.framework.Util.getRandomString;
import static test.automation.utils.testDataUtils.getStoryData;
public class StoryCreation extends Page
{
    @FindBy(xpath = "//div[@id='storyPanel']//span[@class='ui-button-text' and text()='New']")
    public static WebElement newButton;
    @FindBy(xpath = "//a[text()='Properties']")
    public static WebElement creationPage;
    @FindBy(xpath = "//div[@id='storyEdit-tab-3']//select[@name='Status']")
    public static WebElement status;
    @FindBy(xpath = "//select[@name='count']")
    public WebElement countDropDown;
    @FindBy(xpath = "(//div[@class='libFieldEditorContent libTextFieldSizeFullWidth libEditKicker'])")
    public WebElement kicker;
    @FindBy(xpath = "(//div[@class='libFieldEditorContent libTextFieldSizeFullWidth libEditTitle'])")
    public static WebElement title;
    @FindBy(xpath = "(//p[@class='libPageBodyLinebreak'])[2]")
    public static WebElement story;
    @FindBy(name = "mediadata")
    public WebElement image;
    @FindBy(xpath = "//button[@class='lib-button-saveclose ui-button ui-widget ui-state-default ui-corner-all ui-button-text-icon-primary btnFixed']")
    public static WebElement saveAndCloseButton;
    @FindBy(xpath = "//input[@name='Slug']")
    public static WebElement slug;
    @FindBy(xpath = "//input[@name='slug']")
    public WebElement slugInputOnPopUp;
    @FindBy(xpath = "//select[@class='lib-post' and @name='Shape']")
    public static WebElement shapeDropdown;
    @FindBy(xpath = "//iframe[@class='libFieldBaseEditor libEditKicker']")
    public static WebElement iframeKicker;
    @FindBy(xpath = "//iframe[@class='libFieldBaseEditor libEditTitle lib-group-show-collapsed']")
    public static WebElement iframeTitle;
    @FindBy(xpath = "//iframe[@class='libFieldBaseEditor libEditStory']")
    public static WebElement iframeStory;
    @FindBy(xpath = "//div[@id='storyEdit-main']//span[@class='ui-button-text' and text()='Add']")
    public static WebElement AddImage;
    @FindBy(xpath = "//span[text()='Yes']")
    public static WebElement prefillImageInformation;
    @FindBy(xpath = "//button[@class='ui-button ui-widget ui-state-default ui-corner-all ui-button-text-icon-primary']//span[@class='ui-button-text' and text()='Save']")
    public WebElement saveImage;
    @FindBy(xpath = "//a[text()='Publish Date']")
    public WebElement publishDate;
    @FindBy(xpath = "//select[@name='channel']")
    public static WebElement channelDropdown;
    @FindBy(xpath = "//input[@class='ui-widget libFilterInput libFilterDate hasDatepicker']")
    public static WebElement calender;
    @FindBy(xpath = "//div[@class='ui-datepicker-title']")
    public static WebElement MonthYear;
    @FindBy(xpath = "//span[text()='Prev']")
    public WebElement previous;
    @FindBy(xpath = "//span[@class='ui-icon ui-icon-circle-triangle-e']")
    public WebElement Next;
    @FindBy(xpath = "//a[text()='Stories']")
    public WebElement stories;
    @FindBy(xpath = "//div[@id='story-main']//span[@class='ui-button-text' and text()='Update']")
    public static WebElement update;
    @FindBy(xpath = "(//input[@type='checkbox'])")
    public List<WebElement> checkboxes;
    @FindBy(xpath = "//button[@class='lib-button-copy ui-button ui-widget ui-state-default ui-corner-all ui-button-text-icon-primary btnFixed']//span[@class='ui-button-text' and text()='Copy']")
    public WebElement copyOfStories;
    @FindBy(xpath = "//span[text()='Libercus']")
    public WebElement libercus;
    @FindBy(xpath = "//span[@class='ui-button-text' and text()='OK']")
    public WebElement Ok;
    @FindBy(xpath = "//div[@id='storyPanel']//div[@class='libNavBarTextArea']")
    public WebElement countOfStories;
    @FindBy(xpath = "//div[@id='interactiveads-main']//div[@class='libNavBarTextArea']")
    public WebElement countOfInteractiveAds;
    @FindBy(xpath = "//div[@id='richmedia-main']//div[@class='libNavBarTextArea']")
    public WebElement countOfRichMediaAds;
    @FindBy(xpath = "//div[@id='printpages-main']//div[@class='libNavBarTextArea']")
    public WebElement countOfPages;
    @FindBy(xpath = "//div[@id='printshapes-main']//div[@class='libNavBarTextArea']")
    public WebElement countOfShapes;
    @FindBy(xpath = "//div[@id='printstyles-main']//div[@class='libNavBarTextArea']")
    public WebElement countOfStyles;
    @FindBy(xpath = "(//select[@class='ui-widget libFilterSelect libFilterDate'])[1]")
    public WebElement filterOptionElements;
    @FindBy(xpath = "//div[@id='storyPanel']//button[@name='delete']")
    public WebElement storyDelete;
    @FindBy(xpath = "(//*[@class='libListContentRow libLine'])/div[12]")
    public static List<WebElement> storiesPageNumber;
    @FindBy(xpath = "//select[@name='storySearch']")
    public WebElement selectSearchType;
    @FindBy(xpath = "//input[@name='storySearch_search']")
    public static WebElement searchTextBox;
    @FindBy(xpath = "//div[@id='story-main']//select[@name='PrintPageSection']")
    public static WebElement SectionLetter;
    @FindBy(xpath = "//div[@class='libListContentRow libLine']")
    public static List<WebElement> shadowHost;
    @FindBy(xpath = "//div[@class='libListContentRow libLine']")
    public static List<WebElement> pageNumber;
    @FindBy(xpath = "//a[@id='ui-id-161']")
    public static WebElement visualEdit;
    @FindBy(xpath = "//div[@id='siteusers-main']//div[@class='libNavBarTextArea']")
    public static WebElement countOfUsers;
    @FindBy(xpath = "//div[@id='tags-main']//div[@class='libNavBarTextArea']")
    public static WebElement countOfTags;
    String beforeCopyStory = "";
    public String deleteStories() throws Exception {
        StoryCreation storyCreation = new StoryCreation();
        boolean storyFound = false;
        for (int i = 1; i <= storiesPageNumber.size(); i++) {
            WebElement pageNumber = Browser.getDriver(By.cssSelector("story-preview")).findElement(By.xpath("(//*[@class='libListContentRow libLine'])[" + i + "]/div[12]"));
            WebElement PageStatus = Browser.getDriver(By.cssSelector("story-preview")).findElement(By.xpath("(//*[@class='libListContentRow libLine'])[" + i + "]/div[11]"));
            String pageText = pageNumber.getText();
            if (pageText.isBlank() & !(PageStatus.getText().equalsIgnoreCase("Placed on page"))) {
                storyFound = true;
                Browser.getDriver(By.cssSelector("story-preview")).findElement(By.xpath("(//*[@class='libListContentRow libLine'])[" + i + "]/div[3]")).click();
                break;
            }
        }
        if (!storyFound) {
            shortWait();
            storyCreation.selectPastItems();
            storyCreation.update();
            shortWait();
            shortWait();
            beforeCopyStory = storyCreation.numberOfRecords("CountOfStories");
            System.out.println(beforeCopyStory + "in delete story");
            for (int i = 1; i <= storiesPageNumber.size(); i++) {
                WebElement pageNumber = Browser.getDriver(By.cssSelector("story-preview")).findElement(By.xpath("(//*[@class='libListContentRow libLine'])[" + i + "]/div[12]"));
                WebElement PageStatus = Browser.getDriver(By.cssSelector("story-preview")).findElement(By.xpath("(//*[@class='libListContentRow libLine'])[" + i + "]/div[11]"));
                String pageText = pageNumber.getText();
                if (pageText.isBlank() & !(PageStatus.getText().equalsIgnoreCase("Placed on page"))) {
                    Browser.getDriver(By.cssSelector("story-preview")).findElement(By.xpath("(//*[@class='libListContentRow libLine'])[" + i + "]/div[3]")).click();
                    break;
                }
            }
        }
        System.out.println(beforeCopyStory + "in delete of stories");
        shortWait();
        click(storyDelete);
        shortWait();
        click(storyCreation.Ok);
        if (!storyFound) {
            return beforeCopyStory;
        } else {
            return "0";
        }
    }
    public static void selectStatus() throws InterruptedException {
        Select select = new Select(status);
        select.selectByVisibleText("Published");
    }
    public void selectCount() {
        Select select = new Select(countDropDown);
        select.selectByValue("1000");
    }
    public static void enterSlug() {
        sendKeys("slug", "testStoryCreation");
    }
    public static String selectShape(String Shape) throws InterruptedException {
        Select select3 = new Select(shapeDropdown);
        select3.selectByVisibleText(Shape);
        String selectedText = select3.getFirstSelectedOption().getText();
        return Shape;
    }
    public static void enterTitle(String title) throws InterruptedException {
        Browser.getDriver(By.cssSelector("story-preview")).switchTo().frame(iframeTitle);
        sendKeys("title", title);
        Browser.getDriver(By.cssSelector("story-preview")).switchTo().defaultContent();
    }
    public static void enterStory(String story) throws InterruptedException {
        Browser.getDriver(By.cssSelector("story-preview")).switchTo().frame(iframeStory);
        sendKeys("story", story);
        Browser.getDriver(By.cssSelector("story-preview")).switchTo().defaultContent();
    }
    public static void hasImage(String shape,String key) throws Exception {
        String selectedText = selectShape(shape);
        if (selectedText.contains("photo")) addImage(key);
    }
    public static void addImage(String key) throws Exception {
        shortWait();
        click(AddImage);
        shortWait();
        execJavascript("window.scrollTo(0, document.body.scrollHeight);");
        shortWait();
        String image = (String) getStoryData(key).get("image");
        String absolutePath = Paths.get(image).toAbsolutePath().toString();
        sendKeys("image", absolutePath);
        shortWait();
        waitUntil(() -> prefillImageInformation.isDisplayed());
        waitUntilElementPresent(prefillImageInformation, 120);
        click("prefillImageInformation");
        shortWait();
        click("saveImage");
        shortWait();
        execJavascript("window.scrollTo(0, 0);");
    }
    public void selectPastItems() throws Exception {
        click(publishDate);
        Select select3 = new Select(filterOptionElements);
        select3.selectByVisibleText("Last Year");
    }
    public static boolean isBefore(String displayed, String target) {
        String[] displayedParts = displayed.split(" ");
        String[] targetParts = target.split(" ");
        String displayedMonth = displayedParts[0];
        int displayedYear = Integer.parseInt(displayedParts[1]);
        String targetMonth = targetParts[0];
        int targetYear = Integer.parseInt(targetParts[1]);
        if (displayedYear > targetYear) {
            return true;
        } else if (displayedYear < targetYear) {
            return false;
        }
        return getMonthNumber(displayedMonth) > getMonthNumber(targetMonth);
    }
    public static int getMonthNumber(String month) {
        int monthValue = Month.valueOf(month.toUpperCase()).getValue();
        return monthValue;
    }
    public static void calender(String Date1) throws Exception {
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date1 = inputFormat.parse(Date1);
        SimpleDateFormat dayFormat = new SimpleDateFormat("d");
        String dateNumber = dayFormat.format(date1);
        int day = Integer.parseInt(dateNumber);
        SimpleDateFormat monthYearFormat = new SimpleDateFormat("MMMM yyyy");
        String monthYear = monthYearFormat.format(date1);
        shortWait();
        WebElement dropDown = Browser.getDriver(By.cssSelector("story-preview")).findElement(By.xpath("//select[@name='PublishDateRange']"));
        Select select = new Select(dropDown);
        String selectedText = select.getFirstSelectedOption().getText().trim();
        if (selectedText.matches(".*[a-zA-Z]+.*"))
        {
            shortWait();
            click("publishDate");
            shortWait();
//            if (Browser.getDriver().findElement(By.xpath("//div[@id='help-outer']")).isDisplayed()) {
//                Browser.getDriver().findElement(By.xpath("//span[@class='ui-icon ui-icon-close right']")).click();
//            }
            List<WebElement> helpPopup = Browser.getDriver(By.cssSelector("story-preview")).findElements(By.xpath("//div[@id='help-outer']"));
            if (!helpPopup.isEmpty() && helpPopup.get(0).isDisplayed()) {
                Browser.getDriver(By.cssSelector("story-preview")).findElement(By.xpath("//span[@class='ui-icon ui-icon-close right']")).click();
            }
        }
        waitUntilElementPresent(calender);
        calender.click();
        int dateToSelect = day;
        String monthYearToSelect = monthYear;
        while (true)
        {
            String displayedMonthYear = MonthYear.getText();
            if (displayedMonthYear.equals(monthYearToSelect))
            {
                break;
            }
            if (isBefore(displayedMonthYear, monthYearToSelect)) {
                click("previous");
            } else {
                click("Next");
            }
        }
        shortWait();
        WebElement select_date = Browser.getDriver(By.cssSelector("story-preview")).findElement(By.xpath("//a[text()='" + dateToSelect + "' and contains(@class,'ui-state-default')]"));
        select_date.click();
        shortWait();
    }
    public static void update() throws Exception {
        shortWait();
        click(update);
        shortWait();
    }
    public void copyOfStories(String date, String slug) throws Exception {
        shortWait();
        WebElement checkbox = checkboxes.get(getRandomIntBetween(1, checkboxes.size() - 1));
        click(checkbox);
        shortWait();
        click(copyOfStories);
        shortWait();
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date1 = inputFormat.parse(date);
        SimpleDateFormat dayFormat = new SimpleDateFormat("d");
        String dateNumber = dayFormat.format(date1);
        int day = Integer.parseInt(dateNumber);
        SimpleDateFormat monthYearFormat = new SimpleDateFormat("MMMM yyyy");
        String monthYear = monthYearFormat.format(date1);
        int dateToSelect = day;
        String monthYearToSelect = monthYear;
        while (true) {
            String displayedMonthYear = MonthYear.getText();
            if (displayedMonthYear.equals(monthYearToSelect)) {
                break;
            }
            if (isBefore(displayedMonthYear, monthYearToSelect)) {
                click("previous");
            } else {
                click("Next");
            }
        }
        shortWait();
        WebElement select_date = Browser.getDriver(By.cssSelector("story-preview")).findElement(By.xpath("//a[text()='" + dateToSelect + "' and contains(@class,'ui-state-default')]"));
        select_date.click();
        click("libercus");
        shortWait();
        slugInputOnPopUp.sendKeys(getRandomString(6));
        shortWait();
        Ok.click();
        shortWait();
    }
    public String numberOfRecords(String Count) throws InterruptedException {
        shortWait();
        String NumberOfRecords = "";
        if (Count.equalsIgnoreCase("CountOfStories")) {
            if (Browser.getDriver(By.cssSelector("story-preview")).findElements(By.xpath("//div[text()='No items found']")).size() > 0) {
                NumberOfRecords = "0";
            } else {
                NumberOfRecords = this.countOfStories.getText();
            }
        } else if (Count.equalsIgnoreCase("CountOfInteractiveAds")) {
            List<WebElement> noItemsList = Browser.getDriver(By.cssSelector("story-preview")).findElements(By.xpath("//div[text()='No items found']"));
            if (Browser.getDriver(By.cssSelector("story-preview")).findElements(By.xpath("//div[text()='No items found']")).size() > 0) {
                NumberOfRecords = "0";
            } else {
                NumberOfRecords = this.countOfInteractiveAds.getText();
            }
        } else if (Count.equalsIgnoreCase("CountOfRichMediaAds")) {
            NumberOfRecords = this.countOfRichMediaAds.getText();
        } else if (Count.equalsIgnoreCase("CountOfPages"))
        {
            NumberOfRecords = this.countOfPages.getText();
        } else if (Count.equalsIgnoreCase("countOfShapes"))
        {
            NumberOfRecords=this.countOfShapes.getText();

        } else if (Count.equalsIgnoreCase("countOfStyles"))
        {
            NumberOfRecords=this.countOfStyles.getText();
        }
        else if(Count.equalsIgnoreCase("countOfUsers"))
        {
            NumberOfRecords=this.countOfUsers.getText();
        }
        else if(Count.equalsIgnoreCase("countOfTags"))
        {
            NumberOfRecords=this.countOfTags.getText();
        }
        String[] parts = NumberOfRecords.split(" ");
        String totalRecords = parts[4];
        return totalRecords;
    }
    public static void verifyCopyOfStories(String BeforeCopyCount, String AfterCopyCount, String Functionality) throws InterruptedException
    {
        int BeforeCopyCount1 = Integer.parseInt(BeforeCopyCount);
        int AfterCopyCount1 = Integer.parseInt(AfterCopyCount);
        if (Functionality.equalsIgnoreCase("Copy"))
        {
            Assertions.assertTrue(BeforeCopyCount1 + 1 == AfterCopyCount1);
        }
        else if (Functionality.equalsIgnoreCase("Delete"))
        {
            Assertions.assertTrue(BeforeCopyCount1 - 1 == AfterCopyCount1);
        }
    }
    public static String searchText = "";

    public static void searchText() throws Exception {
        searchTextBox.sendKeys(searchText);
        update();
    }
    public static void verifySearchResults() {
        for (WebElement host : shadowHost) {
            Assertions.assertTrue(host.getText().contains(searchText), "search item is not displayed");
        }
    }

    public static void VerifySearchonSectionLetter() throws Exception {
        update();
        System.out.println(pageNumber.size());
        for (int i = 1; i <= pageNumber.size(); i++) {
            WebElement eachPagenumber = Browser.getDriver(By.cssSelector("story-preview")).findElement(By.xpath("(//*[@class='libListContentRow libLine'])[" + i + "]/div[12]"));
            System.out.println(eachPagenumber.getText());
            Assertions.assertTrue(eachPagenumber.getText().contains("A"), "search item is not displayed");
        }
    }
    public static String selectStory() throws InterruptedException {
        String randomStory = getRandomNumber(1);
        int randomnum = Integer.parseInt(randomStory);
        if (randomnum == 0) randomnum = 1;
        WebElement story = Browser.getDriver(By.cssSelector("story-preview")).findElement(By.xpath("(//div[@class='libListContentRow libLine'])[" + randomnum + "]"));
        String storyId = Browser.getDriver(By.cssSelector("story-preview")).findElement(By.xpath("(//*[@class='libListContentRow libLine'])[" + randomnum + "]/div[14]")).getText();
        story.click();
        shortWait();
        return storyId;
    }
    static String text = "Virat Kohli";
    public static String randomValue;
    public static void storyUpdate(String filedName) throws Exception {
        switch (filedName) {
            case "Title":
                Browser.getDriver(By.cssSelector("story-preview")).switchTo().frame(iframeTitle);
                randomValue = getRandomString(5);
                title.clear();
                title.sendKeys(randomValue);
                title.sendKeys(Keys.TAB);
                title.sendKeys(Keys.CONTROL, Keys.ENTER);
                shortWait();
                System.out.println(title.getText() + " in first story");
                title.sendKeys(Keys.TAB);
                Browser.getDriver(By.cssSelector("story-preview")).switchTo().defaultContent();
                wait.until(ExpectedConditions.elementToBeClickable(saveAndCloseButton));
                jsClick(saveAndCloseButton);
                shortWait();
                break;
            case "Story":
                Browser.getDriver(By.cssSelector("story-preview")).switchTo().frame(iframeStory);
                story.clear();
                randomValue = getRandomString(5);
                story.sendKeys(randomValue);
                shortWait();
                System.out.println(story.getText() + " in first story");
                Browser.getDriver(By.cssSelector("story-preview")).switchTo().defaultContent();
//                click(saveAndCloseButton);
                break;
            case "Slug":
                shortWait();
                visualEdit.click();
                slug.clear();
                randomValue = getRandomString(5);
                slug.sendKeys(randomValue);
                Browser.getDriver(By.cssSelector("story-preview")).switchTo().defaultContent();
                click(saveAndCloseButton);
                break;
            case "Shape":
                shortWait();
                visualEdit.click();
                String shape = (String) getStoryData("story1").get("Shape");
                selectShape(shape);
                break;
            case "Status":
                shortWait();
                visualEdit.click();
                Select select = new Select(status);
                select.selectByVisibleText("Draft");
                Browser.getDriver(By.cssSelector("story-preview")).switchTo().defaultContent();
                click(saveAndCloseButton);
                break;
            default:
                System.out.println("Invalid field name: " + filedName);
                break;
        }
    }
    public static void verifyStoryUpdate(String storyId) throws InterruptedException {
        shortWait();
//        searchTextBox.sendKeys(storyId);
        List<WebElement> storyIds = Browser.getDriver(By.cssSelector("story-preview")).findElements(By.xpath("(//*[@class='libListContentRow libLine'])/div[14]"));
        for (int i = 1; i <= storyIds.size(); i++) {
            WebElement eachStory = Browser.getDriver(By.cssSelector("story-preview")).findElement(By.xpath("(//*[@class='libListContentRow libLine'])[" + i + "]/div[14]"));
            System.out.println(eachStory.getText());
            if (eachStory.getText().equalsIgnoreCase(storyId)) {
                eachStory.click();
                shortWait();
                System.out.println(eachStory.getText());
                break;
            }
        }
        Browser.getDriver(By.cssSelector("story-preview")).switchTo().frame(iframeTitle);
        System.out.println(randomValue);
        shortWait();
        System.out.println(title.getAttribute("value") + "in last story");
        String title2=title.getAttribute("value");
        Browser.getDriver(By.cssSelector("story-preview")).switchTo().defaultContent();
        shortWait();
        Assertions.assertTrue(title2.equalsIgnoreCase(randomValue));
    }
    public static void storyCalender(String date) throws ParseException, InterruptedException {
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date1 = inputFormat.parse(date);
        SimpleDateFormat dayFormat = new SimpleDateFormat("d");
        String dateNumber = dayFormat.format(date1);
        int day = Integer.parseInt(dateNumber);
        SimpleDateFormat monthYearFormat = new SimpleDateFormat("MMMM yyyy");
        String monthYear = monthYearFormat.format(date1);
        shortWait();
        shortWait();
        Browser.getDriver(By.cssSelector("story-preview")).findElement(By.xpath("(//input[@name='PublishDateRange'])[2]")).click();
        shortWait();
        int dateToSelect = day;
        String monthYearToSelect = monthYear;
        shortWait();
        while (true) {
            String displayedMonthYear=Browser.getDriver(By.cssSelector("story-preview")).findElement(By.xpath("//div[@class='ui-datepicker-title']")).getText();
            if (displayedMonthYear.equals(monthYearToSelect)) {
                break;
            }
            if (isBefore(displayedMonthYear, monthYearToSelect)) {
                click("previous");
            } else {
                click("Next");
            }
        }
        shortWait();
        WebElement select_date = Browser.getDriver(By.cssSelector("story-preview")).findElement(By.xpath("//a[text()='" + dateToSelect + "' and contains(@class,'ui-state-default')]"));
        select_date.click();
        shortWait();
    }
}

package test.automation.pages.Libercus.CMS;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import test.automation.framework.Browser;
import test.automation.framework.Page;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.List;
import java.util.stream.Stream;
import static test.automation.framework.Actions.*;
import static test.automation.framework.Config.getCustomerEnv;
import static test.automation.framework.Random.*;
import static test.automation.pages.Libercus.CMS.InteractiveAds.number;
import static test.automation.pages.Libercus.CMS.StoryCreation.isBefore;
public class PrintPages extends Page {
    @FindBy(xpath = "//div[@id='printpages-main']//select[@name='channel']")
    public WebElement channelDropdown;
    @FindBy(xpath = "//div[@class='ui-datepicker-title']")
    public static WebElement MonthYear;
    @FindBy(xpath = "//span[text()='Prev']")
    public WebElement previous;
    @FindBy(xpath = "//span[@class='ui-icon ui-icon-circle-triangle-e']")
    public WebElement Next;
    @FindBy(xpath = "//select[@name='Status']")
    public static WebElement statusDropdown;
    @FindBy(xpath = "//div[@id='printpagesPanel']//span[@class='ui-button-text' and text()='New']")
    public WebElement newButton;
    @FindBy(xpath = "//input[@name='SectionLetter']")
    public static WebElement SectionLetter;
    @FindBy(xpath = "(//a[text()='Publish Date'])[2]")
    public static WebElement publishDate;
    @FindBy(xpath = "//input[@name='Page']")
    public static WebElement PageNumber;
    @FindBy(xpath = "//a[text()='Layout']")
    public static WebElement Layout;
    @FindBy(xpath = "//button[@name='add']")
    public WebElement addButton;
    @FindBy(xpath = "//select[@name='Template']")
    public static WebElement templeteDropdown;
    @FindBy(xpath = "(//select[@name='PublishDateRange'])[2]")
    public static WebElement publishDateDropDown;
    @FindBy(xpath = "//div[@id='addstoriestopage-main']//input[@name='marked']")
    public static WebElement selectStory;
    @FindBy(xpath = "//button[@class='ui-button ui-widget ui-state-default ui-corner-all ui-button-text-icon-primary']//span[@class='ui-button-text' and text()='Save']")
    public static WebElement saveStory;
    @FindBy(xpath = "//div[@class='libPageStory libTruncated']/..")
    public static WebElement source;
    @FindBy(xpath = "//div[@class='libPageSidebar libTruncated']/..")
    public static WebElement sourceOfAd;
    @FindBy(xpath = "//iframe[@class='libPageLayoutAreaframe']")
    public static WebElement iframe;
    @FindBy(xpath = "//div[@class='layoutContent ui-droppable']")
    public static WebElement target;
    @FindBy(xpath = "//div[@class='libPageElement libPageShapeElement ui-droppable']")
    public static WebElement droppedStory;
    @FindBy(xpath = "//div[@class='libPageElement libPageShapeElement']")
    public static WebElement droppedAd;
    @FindBy(xpath = "(//*[@class='lib-buttonbar'])[5]/button[@name='save']/span[text()='Save & Close']")
    public static WebElement saveAndCloseButton;
    @FindBy(xpath = "//div[@id='printpagesPanel']//span[@class='ui-button-text' and text()='Copy']")
    public static WebElement copyOfPage;
    @FindBy(xpath = "//input[@name='PublishDate']")
    public static WebElement PublishDate;
    @FindBy(xpath = "//span[text()='Copy Print Pages']")
    public static WebElement copyOfPrintpages;
    @FindBy(xpath = "//input[@name='ManualPrintPageSection']")
    public static WebElement pageSection;
    @FindBy(xpath = "//input[@name='PrintPageSectionCheck' and @value='ManualPrintPageSection']")
    public static WebElement pageSelectionRadioButton;
    @FindBy(xpath = "(//input[@name='CopyAds'])")
    public static WebElement copyAds;
    @FindBy(xpath = "//div[@id='printpages-main']//span[@class='ui-button-text' and text()='Update']")
    public static WebElement update;
    @FindBy(xpath = "//input[@type='checkbox' and @name='marked']")
    public static List<WebElement> listOFPages;
    @FindBy(xpath = "//div[@id='printpagesPanel']//button[@name='delete']")
    public WebElement deleteOfPages;
    @FindBy(xpath = "//select[@name='pagesSearch']")
    public WebElement selectSearchType;
    @FindBy(xpath = "//input[@name='pagesSearch_search']")
    public static WebElement searchTextBox;
    @FindBy(xpath = "//div[@id='printpages-main']//select[contains(@name,'PrintPageSection')]")
    public static WebElement SectionLetterName;
    @FindBy(xpath = "//div[@class='libListContentRow libLine']//div")
    public static List<WebElement> shadowHost;
    @FindBy(xpath = "//div[@id='printpages-main']/div[contains(@class,'libSplitContainer')]/div[contains(@class,'libListContent')]/div")
    public static List<WebElement> pageNumber;
    @FindBy(xpath = "//button[@class='lib-button-save ui-button ui-widget ui-state-default ui-corner-all ui-button-text-icon-primary btnFixed']//span[@class='ui-button-text'][text()='Save']")
    public static WebElement saveButton;
    @FindBy(xpath = "/html/body/div[10]/div[2]/div/div/div/div[1]/div[1]/div[2]/div[2]/div/button[1]/span[2]")
    public static WebElement updateButtonOnPanel;
    @FindBy(xpath = "//button[@class='ui-button ui-widget ui-state-default ui-corner-all ui-button-text-icon-primary']//span[@class='ui-button-text'][text()='OK']")
    public static WebElement popupCloseButton;
    @FindBy(xpath = "//label[@for='modeonprintpages']//span[@class='ui-button-text'][text()='On']")
    public static WebElement AdEditMode;
    @FindBy(xpath = "//button[@name='addAd']//span[@class='ui-button-text'][text()='Add']")
    public static WebElement PrintAdsAddButton;
    @FindBy(xpath = "//div[@id='addadstopage-main']//input[@name='marked']")
    public static WebElement selectAd;
    public static void clickUpdateOnPrintPages() throws Exception {
        click(update);
    }
    public static void CopyOfPages(String Date) throws Exception {
        click(listOFPages.get(getRandomIntBetween(1, listOFPages.size() - 1)));
        jsClick(copyOfPage);
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
        java.util.Date date1 = inputFormat.parse(Date);
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
        shortWait();
        click(copyOfPrintpages);
        Select select = new Select(Browser.getDriver(By.cssSelector("story-preview")).findElement(By.xpath("//select[@name='Channel']")));
        String currentProject = getCustomerEnv();
        String channelValue;
        if (currentProject.equals("PG")) {
            channelValue = "PG Print";
        } else {
            channelValue = "Print";
        }
        select.selectByVisibleText(channelValue);
        shortWait();
        jsClick(pageSelectionRadioButton);
        pageSection.sendKeys("A");
        jsClick(copyAds);
        StoryCreation storyCreation = new StoryCreation();
        jsClick(storyCreation.Ok);
        shortWait();
        jsClick(storyCreation.Ok);
        WebDriverWait wait = new WebDriverWait(Browser.getDriver(By.cssSelector("story-preview")), Duration.ofSeconds(240));
        try {
            WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[@class='ui-button-text' and text()='OK']")));
            element.click();
            System.out.println("Element was found and clicked.");
        } catch (Exception e) {
            System.out.println("Element did not appear within the timeout. Skipping click.");
        }
    }
    public static void tap_Layout() throws Exception {
        shortWait();
        shortWait();
        wait.until(ExpectedConditions.elementToBeClickable(Layout));
        click(Layout);
        shortWait();
        Actions actions = new Actions(Browser.getDriver(By.cssSelector("story-preview")));
        actions.sendKeys("1").perform();
        shortWait();
    }
    public static String originalWindow="";
    public static void drag_Drop(String sourceType) throws InterruptedException {
        shortWait();
        Browser.getDriver(By.cssSelector("story-preview")).switchTo().frame(iframe);
        Actions actions = new Actions(Browser.getDriver(By.cssSelector("story-preview")));
        Browser.getDriver(By.cssSelector("story-preview")).switchTo().defaultContent();
        if(sourceType.equalsIgnoreCase("story"))
        {
            actions.clickAndHold(source).perform();
        }
        else if(sourceType.equalsIgnoreCase("printad"))
        {
            actions.clickAndHold(sourceOfAd).perform();
        }
        Browser.getDriver(By.cssSelector("story-preview")).switchTo().frame(iframe);
        actions.moveToElement(target).click().perform();
        if(sourceType.equalsIgnoreCase("story")) {
            execJavascript("arguments[0].style='" + buildElementStyle(40, 2) + "';", droppedStory);
        } else if (sourceType.equalsIgnoreCase("printad"))
        {
            execJavascript("arguments[0].style='" + buildElementStyle(40, 2) + "';", droppedAd);
        }
        Browser.getDriver(By.cssSelector("story-preview")).switchTo().defaultContent();
        originalWindow = Browser.getDriver(By.cssSelector("story-preview")).getWindowHandle();
    }
    private static String buildElementStyle(int top_pt, int left_pt) {
        return "top: " + top_pt * 10 + "px; left: " + left_pt * 10 + "px; position: absolute; overflow: hidden; user-select: none; z-index: 5; text-transform: none; font-variant: normal; margin: 0px; padding: 0px; font-size: 120px; line-height: 120px; letter-spacing: 0px; word-spacing: 0px; text-align: left; text-indent: 0px; height: 6205px; width: 4380px; cursor: auto;";
    }
    public void DeleteOfPages() throws Exception {
        StoryCreation storyCreation = new StoryCreation();
        shortWait();
        String text = storyCreation.countOfPages.getText();
        String[] parts = text.split(" ");
        int number = Integer.parseInt(parts[2]);
        System.out.println(number);
        int randomNumber = getRandomIndex(number);
        if (randomNumber == 0) {
            randomNumber = 1;
        }
        click(storyCreation.checkboxes.get(randomNumber));
        click(deleteOfPages);
        shortWait();
        click(storyCreation.Ok);
    }
    public static String searchText = "test";
    public static void searchTextOfPrintpages() throws Exception {
        searchTextBox.sendKeys(searchText);
        clickUpdateOnPrintPages();
    }
    public static void pageNumber(String pageNumber)
    {
        PageNumber.sendKeys(pageNumber);
    }
    public static void verifySearchResultsOfPrintpages() throws Exception {
        for (WebElement host : shadowHost) {
            Assertions.assertTrue(host.getText().contains(searchText), "search item is not displayed");
        }
    }
    public static void verifySectoionLetterSearchForPages() throws Exception {
        clickUpdateOnPrintPages();
        System.out.println(pageNumber.size());
        for (int i = 1; i <= pageNumber.size(); i++) {
            WebElement eachPagenumber = Browser.getDriver(By.cssSelector("story-preview")).findElement(By.xpath("//div[@id='printpagesPanel']//div[contains(@class,'libListContent')]//div[" + i + "]//div[7]"));
            System.out.println(eachPagenumber.getText());
            Assertions.assertTrue(eachPagenumber.getText().equalsIgnoreCase("A"), "search item is not displayed");
        }
    }
    static int randomNumber;
    public static String pageId;
    public static void selectPageForUpdate() throws InterruptedException {
        StoryCreation storyCreation = new StoryCreation();
        String text=storyCreation.countOfPages.getText();
        String[] parts = text.split(" ");
        int number = Integer.parseInt(parts[2]);
        System.out.println(number);
        randomNumber=getRandomIndex(number);
        if (randomNumber == 0)
            randomNumber=1;
        WebElement page= Browser.getDriver(By.cssSelector("story-preview")).findElement(By.xpath("(//div[@id='printpagesPanel']/div[@id='printpages-main']/div[@class='libSplitContainer']/div[@class='libListContent']/div)["+randomNumber+"]"));
        System.out.println(randomNumber);
        pageId=Browser.getDriver(By.cssSelector("story-preview")).findElement(By.xpath("(//*[@class='libListContentRow libLine'])["+randomNumber+"]/div[14]")).getText();
        page.click();
        shortWait();
    }
    public static String randomValue;
    public static String temp="B";
    public static void printPageUpdate(String fieldName) throws InterruptedException
    {
        randomValue=getRandomString(5);
        shortWait();
        shortWait();
        shortWait();
        wait.until(ExpectedConditions.elementToBeClickable(Layout));
        switch (fieldName.toLowerCase()) {
            case "story":
                Layout.click();
                Browser.getDriver(By.cssSelector("story-preview")).findElement(By.xpath("//span[text()='Update All']")).click();
                shortWait();
                break;
            case "sectionletter":
                shortWait();
                Browser.getDriver(By.cssSelector("story-preview")).findElement(By.xpath("//li[@class='ui-state-default ui-corner-top']/a[text()='Print Pages']")).click();
                shortWait();
                SectionLetter.clear();
                SectionLetter.sendKeys(temp);
                break;
        }
        Browser.getDriver(By.cssSelector("story-preview")).findElement(By.xpath("//button[@class='lib-button-saveclose ui-button ui-widget ui-state-default ui-corner-all ui-button-text-icon-primary btnFixed']//span[@class='ui-button-text'][text()='Save & Close']")).click();
        shortWait();
        shortWait();
    }
    public static void verifyPageUpdate(String fieldName) throws InterruptedException
    {
        shortWait();
        List<WebElement> pageIds=Browser.getDriver(By.cssSelector("story-preview")).findElements(By.xpath("(//*[@class='libListContentRow libLine'])/div[14]"));
        for(int i=1;i<=pageIds.size();i++)
        {
            WebElement eachPage=Browser.getDriver(By.cssSelector("story-preview")).findElement(By.xpath("(//*[@class='libListContentRow libLine'])["+i+"]/div[14]"));
            System.out.println(eachPage.getText());
            if(eachPage.getText().equalsIgnoreCase(pageId))
            {
                eachPage.click();
                System.out.println(eachPage.getText());
                break;
            }
        }
        shortWait();
        shortWait();
        shortWait();
        switch (fieldName.toLowerCase())
        {
            case "story":
                List<WebElement> updateButton=Browser.getDriver(By.cssSelector("story-preview")).findElements(By.xpath("//span[@class='libSidebarItemUpdated inlblk ui-icon ui-icon-alert']"));
                Assertions.assertTrue(updateButton.size()==0);
                break;

            case "sectionletter":
                shortWait();
                Browser.getDriver(By.cssSelector("story-preview")).findElement(By.xpath("//li[@class='ui-state-default ui-corner-top']/a[text()='Print Pages']")).click();
                shortWait();
                String sectionLetter=SectionLetter.getAttribute("value");
                Assertions.assertTrue(temp.equalsIgnoreCase(sectionLetter));
        }
        Browser.getDriver(By.cssSelector("story-preview")).findElement(By.xpath("//button[@class='lib-button-saveclose ui-button ui-widget ui-state-default ui-corner-all ui-button-text-icon-primary btnFixed']//span[@class='ui-button-text'][text()='Save & Close']")).click();
        shortWait();
        Browser.getDriver(By.cssSelector("story-preview")).switchTo().defaultContent();
    }
    public static void verifyPdfDownload()
    {
        number=number+1;
        String titleOftheFile=Browser.getDriver(By.cssSelector("story-preview")).findElement(By.xpath("(//div[@class=\"libListContentPanelTitle\"])["+number+"]")).getText();
        System.out.println(titleOftheFile +" "+number);

        try (Stream<Path> paths = Files.walk(Path.of(Browser.downloadFilePath)))
        {
            boolean fileExists = paths
                    .filter(Files::isRegularFile)
                    .anyMatch(path -> path.getFileName().toString().contains(titleOftheFile));

            if (fileExists) {
                System.out.println("File found!");
            } else {
                System.out.println("File not found.");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void adEditMode()
    {
        AdEditMode.click();
    }

    public static void selectStoryFromStories()
    {
        while(true)
        {
            List<WebElement> stories=Browser.getDriver(By.cssSelector("story-preview")).findElements(By.xpath("//div[@id='addstoriestopage-main']//input[@name='marked']"));
            if(stories.size()>1)
            {
                jsClick(stories.get(1));
                break;
            }
            else
            {
                Browser.getDriver(By.cssSelector("story-preview")).findElement(By.xpath("//button[@class='libButtonNext ui-button ui-widget ui-state-default ui-corner-all ui-button-text-icon-primary']//span[@class='ui-button-text'][text()='Next']")).click();
            }
        }

    }
}
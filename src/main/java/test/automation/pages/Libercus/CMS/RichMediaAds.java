package test.automation.pages.Libercus.CMS;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import test.automation.framework.Browser;
import test.automation.framework.Page;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import static test.automation.framework.Actions.*;
import static test.automation.framework.Config.getCustomerEnv;
import static test.automation.framework.Random.getRandomIndex;
import static test.automation.framework.Random.getRandomString;
import static test.automation.pages.Libercus.CMS.InteractiveAds.number;
import static test.automation.pages.Libercus.CMS.StoryCreation.isBefore;
import static test.automation.utils.testDataUtils.getRichMediaAdData;
public class RichMediaAds extends Page {
    @FindBy(xpath = "//div[@id='richmedia-main']//select[@name='channel']")
    public WebElement channelDropdown;
    @FindBy(xpath = "//div[@id='richmediaPanel']//span[@class='ui-button-text' and text()='Delete']")
    public WebElement deleteOfRichMedia;
    @FindBy(xpath = "//div[@class='ui-datepicker-title']")
    public static WebElement MonthYear;
    @FindBy(xpath = "//span[text()='Prev']")
    public WebElement previous;
    @FindBy(xpath = "//span[@class='ui-icon ui-icon-circle-triangle-e']")
    public WebElement Next;
    @FindBy(xpath = "(//*[@class='lib-buttonbar'])[5]/button[@name='save']/span[text()='Save & Close']")
    public static WebElement saveAndCloseButton;
    @FindBy(xpath = "//div[@id='richmediaPanel']//span[@class='ui-button-text' and text()='New']")
    public static WebElement newButton;
    @FindBy(xpath = "//div[@id='richmediaEdit-main']//select[@name='Status']")
    public static WebElement statusDropdown;
    @FindBy(xpath = "//input[@name='Title']")
    public static WebElement title;
    @FindBy(xpath = "//input[@name='Slug']")
    public static WebElement slug;
    @FindBy(xpath = "//select[@name='RichMediaAdType']")
    public static WebElement adTypeDropdown;
    @FindBy(xpath = "//input[@id='lib-group1-toggle']")
    public static WebElement suportTabletCheckbox;
    @FindBy(xpath = "//input[@name='DesktopPuzzle']")
    public static WebElement suportDesktopCheckbox;
    @FindBy(xpath = "//button[@name='add']")
    public WebElement add;
    @FindBy(xpath = "//input[@id='mediadata']")
    public WebElement mediadata;
    @FindBy(xpath = "//input[@id='zipmediadata']")
    public static WebElement zipFile;
    @FindBy(xpath = "//div[@class='cell hundredpercent']//img")
    public static List<WebElement> imageElements;
    @FindBy(xpath = "//button[@class='ui-button ui-widget ui-state-default ui-corner-all ui-button-text-icon-primary']//span[@class='ui-button-text' and text()='Save']")
    public WebElement save;
    @FindBy(xpath = "//label[text()='Rich Media ID']")
    public static WebElement richMediaAdId;
    @FindBy(xpath = "//div[@id='richmediaPanel']//span[@class='ui-button-text' and text()='Copy']")
    public static WebElement copyOfRichMediaAds;
    @FindBy(xpath = "//div[@id='richmedia-main']//span[@class='ui-button-text' and text()='Update']")
    public static WebElement updateOfRichMediaAd;
    @FindBy(xpath = "//a[@id='ui-id-24']")
    public WebElement RichMedia;
    @FindBy(xpath = "//div[@id='richmedia-main']/div/div/div/div/div/input[@type='checkbox']")
    public static List<WebElement> richMediaAdsList;
    @FindBy(xpath = "//div[@id='richmediaPanel']//span[@class='ui-button-text' and text()='Download']")
    public static WebElement downloadOfRichMediaAds;
    @FindBy(xpath = "//select[@name='richmediaSearch']")
    public WebElement selectSearchType;
    @FindBy(xpath = "//input[@name='richmediaSearch_search']")
    public static WebElement searchTextBox;
    @FindBy(xpath = "//div[@id='richmedia-main']//select[contains(@name,'Device')]")
    public static WebElement deviceType;
    @FindBy(xpath = "//select[@name='AdTypes']")
    public static WebElement adTypes;
    @FindBy(xpath = "//div[@class='libLine libListContentPanel libThumbnailPanel']//div/div[@class='libListContentPanelTitle']")
    public static List<WebElement> shadowHost;
    @FindBy(xpath = "//input[@id='lib-group2-toggle']")
    public static WebElement tablet;
    @FindBy(xpath = "//input[@name='DesktopPuzzle']")
    public static WebElement desktop;
    @FindBy(xpath = "//select[@name=\"RichMediaAdType\"]")
    public static WebElement richMediaAdType;
    @FindBy(xpath = "//button[@class='ui-button ui-widget ui-state-default ui-corner-all ui-button-text-icon-primary']")
    public static WebElement popup;
    @FindBy(xpath = "//div[@class='cell middle hundredpercent libTextFieldSize libReadOnlyDisplay_IndexFilePath']/span")
    public static WebElement indexPath;
    public static void adCreationPage() {
        String Expected = richMediaAdId.getText();
        String Actual = "Rich Media ID";
        Assertions.assertTrue(Actual.equalsIgnoreCase(Expected));

    }
    public void title(String title) throws InterruptedException {
        Browser.getDriver(By.cssSelector("story-preview")).findElement(By.xpath("//input[@name='AdExtrefID']")).sendKeys("123456");
        sendKeys("title", title);
    }
    public void slug(String slug) {
        sendKeys("slug", slug);
    }
    public static void adTypeInRichMedia(String adType) throws InterruptedException {
        Select select2 = new Select(adTypeDropdown);
        select2.selectByVisibleText(adType);
    }
    public static void clickCheckboxesInRichMedia() throws Exception {
        click(suportTabletCheckbox);
        click(suportDesktopCheckbox);
    }
    public static void addImageOnRichMediaAds(String image) throws InterruptedException {
        String absolutePath = Paths.get(image).toAbsolutePath().toString();
        click("add");
        sendKeys("mediadata", absolutePath);
        shortWait();
        shortWait();
        execJavascript("window.scrollTo(0, document.body.scrollHeight);");
        click("save");
    }
    public static void verifyImageisUploaded() throws InterruptedException {
        Assertions.assertTrue(!imageElements.isEmpty(), "image is uploaded");
    }

    public static void addZipFileOnRichMediaAds(String zipFile) throws InterruptedException {
        String absolutePath = Paths.get(zipFile).toAbsolutePath().toString();
        sendKeys("zipFile", absolutePath);
        shortWait();
        shortWait();
    }
    public static void verifyZipFileIsUploadedInRichMedia() {
        Assertions.assertTrue(!zipFile.getAttribute("value").isEmpty(), "File selected to upload");
    }
    public static void clickUpdateOnRichMedia() throws Exception {
        shortWait();
        click(updateOfRichMediaAd);
        shortWait();

    }
    public String CopyOfAds(String date, String slug) throws Exception {
        click(richMediaAdsList.get(getRandomIndex(richMediaAdsList.size())));
        click(copyOfRichMediaAds);
        StoryCreation storyCreation = new StoryCreation();
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
        click(storyCreation.libercus);
        Select select = new Select(Browser.getDriver(By.cssSelector("story-preview")).findElement(By.xpath("//select[@name='Channel']")));
        String currentProject = getCustomerEnv();
        String channelValue;
        if (currentProject.equals("PG")) {
            channelValue = "PG Print";
        } else {
            channelValue = "Print";
        }
        select.selectByVisibleText(channelValue);
        click(storyCreation.Ok);
        return date;
    }
    public void DeleteOfAds() throws Exception {
        StoryCreation storyCreation = new StoryCreation();
        shortWait();
        click(richMediaAdsList.get(getRandomIndex(richMediaAdsList.size())));
        click(deleteOfRichMedia);
        shortWait();
        click(storyCreation.Ok);
    }
    public static void downloadOfAdsInRichMediaAds() throws Exception {
        shortWait();
        click(downloadOfRichMediaAds);
        shortWait();
        number=getRandomIndex(richMediaAdsList.size());
        jsClick(richMediaAdsList.get(number));
        click(downloadOfRichMediaAds);
        shortWait();
    }
    public static String searchText = "RichMediaAd1";
    public static void searchTextOfRichMediaAd() throws Exception {
        shortWait();
        searchTextBox.sendKeys(searchText);
        clickUpdateOnRichMedia();
    }
    public static void verifySearchResultsOfRichMediaAd() throws Exception {
        for (WebElement host : shadowHost) {
            Assertions.assertTrue(host.getText().contains(searchText), "search item is not displayed");
        }
    }
    public static void VerifyDeviceTypeSearchResultsForRichMediaAds(String device) throws Exception {
        clickUpdateOnRichMedia();
        if (shadowHost.isEmpty()) {
            StoryCreation storyCreation = new StoryCreation();
            storyCreation.selectPastItems();
            clickUpdateOnRichMedia();
        }
        if (!shadowHost.isEmpty()) {
            int randomAd = getRandomIndex(shadowHost.size());
            if (randomAd == 0) randomAd = getRandomIndex(shadowHost.size());
            if (randomAd != 0) {
                Browser.getDriver(By.cssSelector("story-preview")).findElement(By.xpath("(//div[@class='libLine libListContentPanel libThumbnailPanel'])[" + randomAd + "]")).click();
                shortWait();
                shortWait();
                if (device.equalsIgnoreCase("Tablet")) {
                    Assertions.assertTrue(tablet.isDisplayed());
                } else if (device.equalsIgnoreCase("Desktop")) {
                    Assertions.assertTrue(desktop.isDisplayed());
                } else if (device.equalsIgnoreCase("Both")) {
                    Assertions.assertTrue(tablet.isDisplayed());
                    Assertions.assertTrue(desktop.isDisplayed());
                }
            } else {
                System.out.println(device + "is not displayed");
            }
        }
    }
    public static void VerifyAdTypeSearchResultsForRichMediaAds(String adType) throws Exception {
        clickUpdateOnRichMedia();
        if (shadowHost.isEmpty()) {
            StoryCreation storyCreation = new StoryCreation();
            storyCreation.selectPastItems();
            clickUpdateOnRichMedia();
        }
        if (!shadowHost.isEmpty()) {
            int randomAd = getRandomIndex(shadowHost.size());
            if (randomAd == 0) randomAd = getRandomIndex(shadowHost.size());
            if (randomAd != 0) {
                Browser.getDriver(By.cssSelector("story-preview")).findElement(By.xpath("(//div[@class='libLine libListContentPanel libThumbnailPanel'])[" + randomAd + "]")).click();
                shortWait();
                shortWait();
                WebElement dropdownElement = Browser.getDriver(By.cssSelector("story-preview")).findElement(By.name("RichMediaAdType"));
                Select dropdown = new Select(dropdownElement);
                String selectedText = dropdown.getFirstSelectedOption().getText();
                if (selectedText.equalsIgnoreCase("Lexigo")) {
                    System.out.println("Lexigo is selected");
                }
            } else {
                System.out.println(adType + "is not displayed");
            }
        }
    }
    static int randomNumber;
    public static void selectRichMediaAd()
    {
        StoryCreation storyCreation = new StoryCreation();
        String text=storyCreation.countOfRichMediaAds.getText();
        String[] parts = text.split(" ");
        int number = Integer.parseInt(parts[2]);
        System.out.println(number);
        randomNumber=getRandomIndex(number);
        if (randomNumber == 0)
            randomNumber=1;
        WebElement richMediAd= Browser.getDriver(By.cssSelector("story-preview")).findElement(By.xpath("(//div[@class='libListContent']/div)["+randomNumber+"]"));
        richMediAd.click();
    }
    public static String randomValue;
    public static void richMediAdUpdate(String fieldName) throws InterruptedException {
        randomValue=getRandomString(5);
        switch (fieldName.toLowerCase()) {
            case "title":
                title.clear();
                popup.click();
                title.sendKeys(randomValue);
                shortWait();
                shortWait();
                shortWait();
                System.out.println(title.getText() + " in first story");
                break;
            case "slug":
                slug.clear();
                popup.click();
                slug.sendKeys(randomValue);
                shortWait();
                System.out.println(slug.getText() + " in first story");
                break;
            case "image":
                randomValue=imageElements.get(0).getAttribute("src");
                String image = (String) getRichMediaAdData("RichMediaAd2").get("Image");
                addImageOnRichMediaAds(image);
                break;
            case "zipFile":
                randomValue=indexPath.getText();
                String zipFile = (String) getRichMediaAdData("RichMediaAd2").get("ZipFile");
                addZipFileOnRichMediaAds(zipFile);
        }
    }
    public static void verifyRichMediaAdUpdate(String fieldName) throws InterruptedException
    {
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//ul[@class='ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-top ui-corner-all']//span[@class='ui-button-text'][normalize-space()='Save & Close']")));
        Browser.getDriver(By.cssSelector("story-preview")).findElement(By.xpath("//ul[@class='ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-top ui-corner-all']//span[@class='ui-button-text'][normalize-space()='Save & Close']")).click();
        shortWait();
        shortWait();
        WebElement richMediAd= Browser.getDriver(By.cssSelector("story-preview")).findElement(By.xpath("(//div[@class='libListContent']/div)["+randomNumber+"]"));
        richMediAd.click();
        shortWait();
        switch (fieldName.toLowerCase())
        {
            case "title":
                Assertions.assertTrue(title.getAttribute("value").equalsIgnoreCase(randomValue));
                break;
            case "slug":
                Assertions.assertTrue(slug.getAttribute("value").equalsIgnoreCase(randomValue));
                break;
            case "image":
                Assertions.assertTrue(!(imageElements.get(0).getAttribute("src").equalsIgnoreCase(randomValue)));
                break;
            case "zipFile":
                String currentValue=indexPath.getText();
                Assertions.assertTrue(!(currentValue.equalsIgnoreCase(randomValue)));
                break;
        }
        Browser.getDriver(By.cssSelector("story-preview")).switchTo().defaultContent();
    }
}
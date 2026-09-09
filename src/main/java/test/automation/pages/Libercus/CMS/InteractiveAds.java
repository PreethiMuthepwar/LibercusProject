package test.automation.pages.Libercus.CMS;

import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;
import test.automation.framework.Browser;
import test.automation.framework.Page;
import test.automation.framework.Random;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;
import static test.automation.framework.Actions.*;
import static test.automation.framework.Config.getCustomerEnv;
import static test.automation.framework.Random.getRandomIndex;
import static test.automation.framework.Random.getRandomIntBetween;
import static test.automation.framework.Runner.log;
import static test.automation.pages.Libercus.CMS.StoryCreation.isBefore;
import static test.automation.utils.testDataUtils.getInteractiveAdsData;

public class InteractiveAds extends Page {
    @FindBy(xpath = "//div[@id='interactiveads-main']//select[@name='channel']")
    public WebElement channelDropdown;
    @FindBy(xpath = "//div[@id='interactiveadsPanel']//button[@name='new']")
    public WebElement newButton;
    @FindBy(xpath = "//div[@id='interactiveadsEdit-main']//select[@name='Status']")
    public static WebElement statusDropdown;
    @FindBy(xpath = "//select[@name='AdType']")
    public static WebElement adType;
    @FindBy(xpath = "//input[@class='lib-post libTextFieldSize libValidate' and @name='Title']")
    public static WebElement title;
    @FindBy(xpath = "//input[@class='lib-post libTextFieldSize libValidate' and @name='Slug']")
    public static WebElement slug;
    @FindBy(xpath = "//input[@class='manualsize lib-post libCheckboxNoValue' and @ name='SupportsMobile']")
    public static WebElement mobileCheckBox;
    @FindBy(xpath = "//input[@class='manualsize lib-post libCheckboxNoValue' and @ name='SupportsTablet']")
    public static WebElement tabletCheckbox;
    @FindBy(xpath = "//input[@class='manualsize lib-post libCheckboxNoValue' and @ name='SupportsDesktop']")
    public static WebElement desktopCheckbox;
    @FindBy(xpath = "//input[@id='mediadata']")
    public static WebElement imageField;
    @FindBy(xpath = "//input[@id='zipmediadata']")
    public static WebElement zipFile;
    @FindBy(xpath = "//button[@class='lib-button-saveclose ui-button ui-widget ui-state-default ui-corner-all ui-button-text-icon-primary btnFixed']")
    public WebElement saveAndCloseButton;
    @FindBy(xpath = "//label[text()='Interactive AdID']")
    public static WebElement interactiveAdId;
    @FindBy(xpath = "//div[@id='lib-preview-area']//img")
    public static WebElement imageSrc;
    @FindBy(xpath = "//div[@class='ui-datepicker-title']")
    public static WebElement MonthYear;
    @FindBy(xpath = "//input[@class='manualsize lib-datefield hasDatepicker']")
    public static WebElement publishDateInputField;
    @FindBy(xpath = "//div[@id='interactiveadsPanel']//span[@class='ui-button-text' and text()='Copy']")
    public static WebElement copyOfInteractiveADs;
    @FindBy(xpath = "//div[@id='interactiveads-main']//span[@class='ui-button-text' and text()='Update']")
    public static WebElement updateOfInteractiveAds;
    @FindBy(xpath = "//div[@class='libCheckColumn rp3']/input[@type='checkbox' and @name='marked']")
    public static List<WebElement> listOfInteractiveAds;
    @FindBy(xpath = "//div[@id='interactiveadsPanel']//span[@class='ui-button-text' and text()='Delete']")
    public WebElement deleteOfInteractiveAds;
    @FindBy(xpath = "//div[@id='interactiveadsPanel']//span[@class='ui-button-text' and text()='Download']")
    public static WebElement downloadOfInteractiveAds;
    @FindBy(xpath = "//span[text()='Prev']")
    public WebElement previous;
    @FindBy(xpath = "//span[@class='ui-icon ui-icon-circle-triangle-e']")
    public WebElement Next;
    @FindBy(xpath = "//input[@name='interactiveadsSearch_search']")
    public static WebElement searchTextBox;
    @FindBy(xpath = "//div[@id='interactiveads-main']//select[contains(@name,'Device')]")
    public static WebElement deviceType;
    @FindBy(xpath = "//div[@class='libLine libListContentPanel libThumbnailPanel']//div/div[@class='libListContentPanelTitle']")
    public static List<WebElement> shadowHost;
    @FindBy(xpath = "//input[@name='SupportsTablet']")
    public static WebElement supportsTablet;
    @FindBy(xpath = "//input[@name='SupportsMobile']")
    public static WebElement supportsMobile;
    @FindBy(xpath = "//input[@name='SupportsDesktop']")
    public static WebElement supportsDesktop;
    @FindBy(xpath = "//select[@name='interactiveadsSearch']")
    public static WebElement selectSearchType;
    @FindBy(xpath = "//button[@class='ui-button ui-widget ui-state-default ui-corner-all ui-button-text-icon-primary']")
    public static WebElement popup;
    @FindBy(xpath = "//div[@class='cell middle hundredpercent libTextFieldSize libReadOnlyDisplay_IndexFilePath']/span")
    public static WebElement indexPath;
    public static void clickUpdateOnInteractiveAdsPage() throws Exception {
        shortWait();
        click(updateOfInteractiveAds);
    }
    public static void interactiveAdCreationPage() throws InterruptedException {
        String Actual = "Interactive AdID";
        shortWait();
        String Expected = interactiveAdId.getText();
        Assertions.assertTrue(Actual.equalsIgnoreCase(Expected));
    }
    public static void adType() throws InterruptedException {
        Select select2 = new Select(adType);
        select2.selectByVisibleText("Fullscreen Ad");
        Browser.getDriver(By.cssSelector("story-preview")).findElement(By.xpath("//input[@name='InteractiveAdID']")).sendKeys("123456");
    }
    public static void enterTitleOnInteractiveAds(String title) throws InterruptedException {
        sendKeys("title", title);
    }
    public static void enterSlugOnInteractiveAds(String slug) throws InterruptedException {
        sendKeys("slug", slug);
    }
    public static void clickCheckboxes() throws InterruptedException {
        if (!mobileCheckBox.isSelected()) {
            click("mobileCheckBox");
        }
        if (!tabletCheckbox.isSelected()) {
            click("tabletCheckbox");
        }
        if (!desktopCheckbox.isSelected()) {
            click("desktopCheckbox");
        }
    }
    public static void addImageOnInteractivePage(String image) throws InterruptedException {
        String absolutePath = Paths.get(image).toAbsolutePath().toString();
        shortWait();
        sendKeys("imageField", absolutePath);
        shortWait();
        shortWait();
    }
    public static void imageIsUploaded() {
        String imageUrl = imageSrc.getAttribute("src");
        Assertions.assertTrue(imageUrl.contains("/admin/imgpreview/"), "image is uploaded");
    }
    public static void addZipFile(String zipFile) throws InterruptedException
    {
        String absolutePath = Paths.get(zipFile).toAbsolutePath().toString();
        sendKeys("zipFile", absolutePath);
        shortWait();
        shortWait();
    }
    public static void verifyZipFileIsUploaded() {
        Assertions.assertTrue(!zipFile.getAttribute("value").isEmpty(), "File selected to upload");
    }
    public static void enterPublishDate(String Date1) throws Exception {
        StoryCreation storyCreation = new StoryCreation();
        Thread.sleep(100000);
        click(Browser.getDriver(By.cssSelector("story-preview")).findElement(By.xpath("//div[@class='table hundredpercent lp4 libLineHeight']/div/div/input[@name='PublishDate_date']")));
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date1 = inputFormat.parse(Date1);
        SimpleDateFormat dayFormat = new SimpleDateFormat("d");
        String dateNumber = dayFormat.format(date1);
        int day = Integer.parseInt(dateNumber);
        SimpleDateFormat monthYearFormat = new SimpleDateFormat("MMMM yyyy");
        String monthYear = monthYearFormat.format(date1);
        int dateToSelect = day;
        String monthYearToSelect = monthYear;
        while (true) {
            String displayedMonthYear = storyCreation.MonthYear.getText();
            if (displayedMonthYear.equals(monthYearToSelect)) {
                break;
            }
            if (isBefore(displayedMonthYear, monthYearToSelect)) {
                storyCreation.previous.click();
            } else {
                storyCreation.Next.click();
            }
        }
        WebElement select_date = Browser.getDriver(By.cssSelector("story-preview")).findElement(By.xpath("//a[text()='" + dateToSelect + "']"));
        select_date.click();
        shortWait();
    }
    public void copyOfAds(String date) throws Exception {
        shortWait();
        jsClick(listOfInteractiveAds.get(getRandomIntBetween(0, listOfInteractiveAds.size() - 1)));
        shortWait();
        click(copyOfInteractiveADs);
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
        StoryCreation storyCreation = new StoryCreation();
        click(storyCreation.Ok);
    }
    public void DeleteOfAds() throws Exception {
        shortWait();
        jsClick(listOfInteractiveAds.get(getRandomIndex(listOfInteractiveAds.size())));
        click(deleteOfInteractiveAds);
        shortWait();
        StoryCreation storyCreation = new StoryCreation();
        click(storyCreation.Ok);
    }
    public static int number;
    public static void downloadOfAds() throws Exception {
        shortWait();
        number=getRandomIndex(listOfInteractiveAds.size());
        jsClick(listOfInteractiveAds.get(number));
        click(downloadOfInteractiveAds);
        shortWait();
        shortWait();
        shortWait();
    }
    public void deleteExistingFile(Path destinationPath) throws IOException {
        try {
            Files.walk(destinationPath)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        } catch (Exception e) {
            log().info("There is no directory");
        }
    }
    public static void moveFileToCustomDirectory() throws IOException {
        Path destinationDirectory = Paths.get(String.valueOf(new File(Browser.downloadFilePath)));
        deleteFiles(destinationDirectory);
        File destinationDir = new File(Browser.downloadFilePath);
        if (!destinationDir.exists()) {
            destinationDir.mkdirs();
        }
        String destinationFileName = destinationDir + "\\ads100";
        Path temp = Files.move(Paths.get(getTheNewestFile()), Paths.get(destinationFileName));
    }
    public static String getTheNewestFile() {
        File theNewestFile = null;
        File dir = new File(String.valueOf(Paths.get(System.getProperty("user.dir"), "Downloads")));
        FileFilter fileFilter = new WildcardFileFilter("*." + "zip");
        File[] files = dir.listFiles(fileFilter);
        if (files.length > 0) {
            Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
            theNewestFile = files[0];
        }
        return theNewestFile.toString();
    }
    public static void verifyDownload()
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
    public static void deleteFiles(Path destinationPath) throws IOException {
        try {
            Files.walk(destinationPath)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        } catch (Exception e) {
            log().info("There is no directory");
        }
    }
    public static String searchText = "761996";
    public static void searchTextOfInteractiveAd() throws Exception {
        shortWait();
        searchTextBox.sendKeys(searchText);
        clickUpdateOnInteractiveAdsPage();
    }
    public static void verifySearchResultsOfInteractiveAd() throws Exception {
        for (WebElement host : shadowHost) {
            Assertions.assertTrue(host.getText().contains(searchText), "search item is not displayed");
        }
    }
    public static void VerifyDeviceTypeSearchResults(String device) throws Exception {
        clickUpdateOnInteractiveAdsPage();
        if (shadowHost.isEmpty()) {
            StoryCreation storyCreation = new StoryCreation();
            storyCreation.selectPastItems();
            clickUpdateOnInteractiveAdsPage();
        }
        if (!shadowHost.isEmpty()) {
            int randomAd = getRandomIndex(shadowHost.size());
            if (randomAd == 0) randomAd = getRandomIndex(shadowHost.size());
            if (randomAd != 0) {
                Browser.getDriver(By.cssSelector("story-preview")).findElement(By.xpath("(//div[@class='libLine libListContentPanel libThumbnailPanel'])[" + randomAd + "]")).click();
                shortWait();
                shortWait();
                if (device.equalsIgnoreCase("Tablet")) {
                    Assertions.assertTrue(supportsTablet.isSelected());
                } else if (device.equalsIgnoreCase("Mobile")) {
                    Assertions.assertTrue(supportsMobile.isSelected());
                } else if (device.equalsIgnoreCase("Desktop")) {
                    Assertions.assertTrue(supportsDesktop.isSelected());
                } else if (device.equalsIgnoreCase("Mobile & Tablet")) {
                    Assertions.assertTrue(supportsTablet.isSelected());
                    Assertions.assertTrue(supportsMobile.isSelected());
                } else if (device.equalsIgnoreCase("All")) {
                    Assertions.assertTrue(supportsTablet.isSelected());
                    Assertions.assertTrue(supportsMobile.isSelected());
                    Assertions.assertTrue(supportsDesktop.isSelected());
                }
            } else {
                System.out.println(device + "is not displayed");
            }
        }
    }
    static int randomNumber;
    public static void selectInteractiveAd()
    {
        StoryCreation storyCreation = new StoryCreation();
        String text=storyCreation.countOfInteractiveAds.getText();
        String[] parts = text.split(" ");
        System.out.println(text);
        int number = Integer.parseInt(parts[2]);
        randomNumber=getRandomIndex(number);
        if (randomNumber == 0)
            randomNumber=1;
        WebElement interactiveAd= Browser.getDriver(By.cssSelector("story-preview")).findElement(By.xpath("(//div[@class='libLine libListContentPanel libThumbnailPanel'])["+randomNumber+"]"));
        interactiveAd.click();
    }
    public static String randomValue;
    public static void interactiveAdUpdate(String filedName) throws InterruptedException {
        InteractiveAds interactiveAds=new InteractiveAds();
        randomValue= Random.getRandomString(5);
        switch (filedName.toLowerCase()) {
            case "title":
                interactiveAds.title.clear();
                popup.click();
                interactiveAds.title.sendKeys(randomValue);
                shortWait();
                System.out.println(interactiveAds.title.getText() + " in first story");
                break;
            case "slug":
                interactiveAds.slug.clear();
                popup.click();
                interactiveAds.slug.sendKeys(randomValue);
                shortWait();
                System.out.println(interactiveAds.slug.getText() + " in first story");
                break;
            case "image":
                randomValue=interactiveAds.imageSrc.getAttribute("src");
                String Image = (String) getInteractiveAdsData("interactiveAd3").get("Image");
                addImageOnInteractivePage(Image);
                break;
            case "zipFile":
                randomValue=indexPath.getText();
                String zipFile = (String) getInteractiveAdsData("interactiveAd3").get("ZipFile");
                addZipFile(zipFile);
                break;
        }
    }
    public static void verifyInteractiveAdUpdate(String fieldName) throws InterruptedException
    {
//        WebElement interactiveAd= Browser.getDriver().findElement(By.xpath("(//div[@class='libListContentPanelTitle'])["+randomValue+"]"));
        WebElement interactiveAd=Browser.getDriver(By.cssSelector("story-preview")).findElement(By.xpath("(//div[text()='"+randomValue+"'])"));
        interactiveAd.click();
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
                Assertions.assertTrue(!(imageSrc.getAttribute("src").equalsIgnoreCase(randomValue)));
                break;
            case "zipFile":
                String currentValue=indexPath.getText();
                Assertions.assertTrue(!(currentValue.equalsIgnoreCase(randomValue)));
                break;
        }
        Browser.getDriver(By.cssSelector("story-preview")).switchTo().defaultContent();
    }
}
package test.automation.pages.Libercus.CMS;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import test.automation.framework.Browser;
import test.automation.framework.Page;
import java.util.List;
import static test.automation.framework.Actions.*;
import static test.automation.framework.Random.getRandomIndex;
import static test.automation.framework.Random.getRandomString;
import static test.automation.framework.Wait.waitToPerformAction;
import static test.automation.utils.testDataUtils.getImageData;
public class MediaFiles extends Page {
    @FindBy(xpath = "//div[@id='story-main']//select[@name='channel']")
    public WebElement channelDropdown;
    @FindBy(xpath = "//button[@class='lib-button-new ui-button ui-widget ui-state-default ui-corner-all ui-button-text-icon-primary btnFixed']//span[@class='ui-button-text'][normalize-space()='New']")
    public WebElement newButton;
    @FindBy(xpath = "//a[@id='ui-id-161']")
    public static WebElement usages;
    @FindBy(xpath = "//select[@name='Status']")
    public static WebElement statusDropdown;
    @FindBy(xpath = "//input[@name='Title']")
    public static WebElement title;
    @FindBy(xpath = "//textarea[@name='Caption']")
    public static WebElement caption;
    @FindBy(xpath = "//input[@id='mediadata']")
    public static WebElement uploadElement;
    @FindBy(xpath = "//div[@id='lib-preview-area']//img")
    public static WebElement imageSrc;
    @FindBy(xpath = "//button[@class='lib-button-saveclose ui-button ui-widget ui-state-default ui-corner-all ui-button-text-icon-primary btnFixed']//span[@class='ui-button-text'][text()='Save & Close']")
    public static WebElement saveAndCloseButton;
    @FindBy(xpath = "//div[@class='libListContentPanelTitle']")
    public static List<WebElement> listOfTitles;
    @FindBy(xpath = "//button[@class='libButtonRefresh ui-button ui-widget ui-state-default ui-corner-all ui-button-text-icon-primary btnFixed']//span[@class='ui-button-text'][text()='Update']")
    public static WebElement updateButton;
    @FindBy(xpath = "//select[@name='mediaSearch']")
    public WebElement selectSearchType;
    @FindBy(xpath = "//input[@name='mediaSearch_search']")
    public static WebElement searchTextBox;
    @FindBy(xpath = "//div[@id='mediafiles-main']//div[@class='libListContent']/div//div[@class='libListContentPanelTitle']")
    public static List<WebElement> shadowHost;
    public static void mediaCreationPage() throws InterruptedException {
        waitToPerformAction();
        Assertions.assertTrue(usages.isDisplayed());
    }
    private String mediafileTitle = "";
    public String enterTitle(String entertitle) {
        String randomString = entertitle + " " + getRandomString(4);
        title.sendKeys(randomString);
        mediafileTitle = randomString;
        return randomString;
    }
    public static void enterCaption(String entercaption) {
        caption.sendKeys(entercaption);
    }
    public static void uploadImage(String Image) throws InterruptedException {
        uploadElement.sendKeys(Image);
        waitToPerformAction();
    }
    public static void verifyimageIsUploaded() {
        String imageUrl = imageSrc.getAttribute("src");
        Assertions.assertTrue(imageUrl.contains("/admin/imgpreview/"), "image is uploaded");
    }
    public void verifyMediaFileisCreated(String mediafileTitle) throws InterruptedException {
        updateButton.click();
        shortWait();
        for (WebElement eachTitle : listOfTitles) {
            System.out.println(eachTitle.getText());
            System.out.println(mediafileTitle);
            if (mediafileTitle.equalsIgnoreCase(eachTitle.getText())) {
                Assertions.assertTrue(eachTitle.getText().equalsIgnoreCase(mediafileTitle));
                break;
            }
        }
    }
    static  String searchText= "Bills";
    public static void searchTextOfMediaFiles() throws InterruptedException {
        searchTextBox.sendKeys(searchText);
        updateButton.click();
    }
    public static void verifySearchResultsOfMediaFiles() throws InterruptedException {
        for (WebElement host : shadowHost) {
            Assertions.assertTrue(host.getText().contains(searchText), "search item is not displayed");
        }
    }
    static int randomNumber;
    public static void selectMediaFile() throws InterruptedException {
        shortWait();
        WebElement countOfMediafiles= Browser.getDriver(By.cssSelector("story-preview")).findElement(By.xpath("//div[@id='mediafiles-main'] //div[@class='libNavBarTextArea']"));
        String text=countOfMediafiles.getText();
        String[] parts = text.split(" ");
        int number = Integer.parseInt(parts[2]);
        System.out.println(number);
        randomNumber=getRandomIndex(number);
        if (randomNumber == 0)
            randomNumber=1;
        WebElement mediaFile= Browser.getDriver(By.cssSelector("story-preview")).findElement(By.xpath("(//div[@class='libLine libListContentPanel libThumbnailPanel'])["+randomNumber+"]"));
        mediaFile.click();
    }
    public static String randomValue;
    public static void mediaFileUpdate(String fieldName) throws InterruptedException {
        MediaFiles mediaFiles = new MediaFiles();
        randomValue=getRandomString(5);
        switch (fieldName.toLowerCase()) {
            case "title":
                mediaFiles.title.clear();
                Browser.getDriver(By.cssSelector("story-preview")).findElement(By.xpath("//button[@class='ui-button ui-widget ui-state-default ui-corner-all ui-button-text-icon-primary']")).click();
                mediaFiles.title.sendKeys(randomValue);
                shortWait();
                System.out.println(mediaFiles.title.getText() + " in first story");
                break;
            case "image":
                randomValue=mediaFiles.imageSrc.getAttribute("src");
                String image = (String) getImageData("Image2").get("Image");
                uploadImage(image);
                break;
        }
    }
    public static void verifyMediadUpdate(String fieldName) throws InterruptedException
    {
        MediaFiles mediaFiles = new MediaFiles();
        shortWait();
        WebElement mediaFile= Browser.getDriver(By.cssSelector("story-preview")).findElement(By.xpath("(//div[@class='libLine libListContentPanel libThumbnailPanel'])[1]"));
        mediaFile.click();
        shortWait();
        switch (fieldName.toLowerCase())
        {
            case "title":
                System.out.println(mediaFiles.title.getAttribute("value")+" ad title");
                System.out.println(randomValue +" random value");
                Assertions.assertTrue(mediaFiles.title.getAttribute("value").equalsIgnoreCase(randomValue));
                break;
            case "image":
                System.out.println(mediaFiles.imageSrc.getAttribute("src")+" image after");
                System.out.println(randomValue +" random value");
                Assertions.assertTrue(!(mediaFiles.imageSrc.getAttribute("src").equalsIgnoreCase(randomValue)));
                break;
        }
        Browser.getDriver(By.cssSelector("story-preview")).switchTo().defaultContent();
    }
}
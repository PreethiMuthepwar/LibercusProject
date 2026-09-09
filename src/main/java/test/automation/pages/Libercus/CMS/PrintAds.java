package test.automation.pages.Libercus.CMS;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import test.automation.framework.Page;
import java.nio.file.Paths;
import java.util.function.BooleanSupplier;

import static test.automation.framework.Actions.*;
public class PrintAds extends Page {
    @FindBy(xpath = "//div[@id='ads-main']//select[@name='channel']")
    public static WebElement channelDropdown;
    @FindBy(xpath = "//select[@name='Status']")
    public WebElement statusDropdown;
    @FindBy(xpath = "//div[@id='adsPanel']//span[@class='ui-button-text' and text()='New']")
    public WebElement newButton;
    @FindBy(xpath = "//input[@name='AdName']")
    public WebElement adNameInputField;
    @FindBy(xpath = "//input[@name='mediadata']")
    public static WebElement pdfFile;
    @FindBy(xpath = "//div[@id='adsPanelEdit']//span[@class='ui-button-text' and text()='Save & Close']")
    public static WebElement saveAndCloseButton;
    @FindBy(xpath = "//div[@class='cell middle']//label[text()='Channel ID']")
    public static WebElement channelId;
    @FindBy(xpath = "//div[@id='lib-preview-area']//img")
    public static WebElement src;
    @FindBy(xpath = "//select[@name='PublishDateRange']")
    public static  WebElement PublishdateDropdown;
    public static void printAdAdCreationPage() {
        String Actual = "Channel ID";
        String Expected = channelId.getText();
        Assertions.assertTrue(Actual.equalsIgnoreCase(Expected));
    }
    public static void addPdfFile() throws InterruptedException {
        String Filepath = "./src/main/resources/test/automation/data/649995.pdf";
        String absolutePath = Paths.get(Filepath).toAbsolutePath().toString();
        sendKeys("pdfFile", absolutePath);
        wait.until(ExpectedConditions.numberOfElementsToBe(By.cssSelector(".loader"), 0));
    }
    public static void verifyPdfIsUploaded() throws InterruptedException {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//span[text()='Uploading File(s)']")));
        shortWait();
        shortWait();
        Assertions.assertTrue(!pdfFile.getAttribute("value").isEmpty(), "File selected to upload");
        String imageUrl = src.getAttribute("src");
        Assertions.assertTrue(imageUrl.contains("/admin/imgpreview/"), "image is not uploaded");
    }
}
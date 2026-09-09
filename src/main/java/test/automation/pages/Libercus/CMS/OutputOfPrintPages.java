package test.automation.pages.Libercus.CMS;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import test.automation.framework.Browser;
import test.automation.framework.Page;
import static test.automation.framework.Actions.*;
public class OutputOfPrintPages extends Page {
    @FindBy(xpath = "//div[@class='libListContentRow libLine']")
    public static WebElement page;
    @FindBy(xpath = "//a[text()='Layout']")
    public static WebElement pageLayout;
    @FindBy(xpath = "//button[@class='lib-button-output ui-button ui-widget ui-state-default ui-corner-all ui-button-text-icon-primary btnFixed']//span[@class='ui-button-text' and text()='Output']")
    public static WebElement output;
    @FindBy(xpath = "//div[@role='dialog']//input[2]")
    public static WebElement pdfProoftoDesktop;
    @FindBy(xpath = "//span[@class='ui-button-icon-primary ui-icon ui-icon-check']")
    public static WebElement ok;
    @FindBy(xpath = "//div[@class='libGeneralDialogContent libDialogPadding']")
    public static WebElement pdfDailougePopup;
    @FindBy(xpath = "//div[@class='libGeneralDialogContent libDialogPadding']")
    public static WebElement pdfGeneratedtext;
    @FindBy(xpath = "//a[text()='Click this link']")
    public static WebElement pdfLink;
    @FindBy(xpath = "//span[text()='Save & Continue']")
    public static WebElement saveContinue;
    public static void tapOnPage() throws Exception {
        shortWait();
        click(page);
        shortWait();
        waitUntilElementPresent(pageLayout, 180);
    }
    public static void verifyPageIsOpened() {
        Assertions.assertTrue(pageLayout.isDisplayed());
    }
    public static void PDFProofToDesktop() throws Exception
    {
        shortWait();
        click(pdfProoftoDesktop);
        click(ok);
    }
    public static void dialogueShouldDisplay() {
        waitUntil(() -> pdfDailougePopup.isDisplayed());
        waitUntilElementPresent(pdfDailougePopup, 60);
    }
    public static void pdfIsReadyPopup() {
        pdfGeneratedtext.isDisplayed();
    }
    public static void pdfLink() throws Exception {
        shortWait();
        click(pdfLink);
        shortWait();
    }
}
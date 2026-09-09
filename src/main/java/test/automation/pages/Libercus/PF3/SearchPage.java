package test.automation.pages.Libercus.PF3;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import test.automation.framework.Browser;
import test.automation.framework.Page;
import static test.automation.framework.Actions.*;
public class SearchPage extends Page {
    @FindBy(xpath = "//input[contains(@id,'mat-input-')]")
    public static WebElement searchHeader;
    @FindBy(xpath = "//input[@value='past' and @type='radio']")
    public static WebElement previousEditionRadioButton;
    @FindBy(xpath = "//*[@value='today' and @type='radio']")
    public static WebElement todayEditionRadioButton;
    @FindBy(xpath = "//a/span[text()='Advanced Search']")
    public static WebElement advanceSearchLink;
    @FindBy(xpath = "//input[contains(@id,'mat-input-')]")
    public static WebElement searchField;
    @FindBy(xpath = "//button/span[text()='Reset']")
    public static WebElement resetButton;
    public static void search(String searchKey) throws InterruptedException {
        shortWait();
        searchField.sendKeys(searchKey);
    }
    public static void checkAdvanceSearchFunctionality() throws InterruptedException {
        shortWait();
        shortWait();
        switchToWindow(1);
        shortWait();
        Assertions.assertTrue(Browser.getDriver(By.cssSelector("story-preview")).getCurrentUrl().contains("https://www.toledoblade.com/search"));
    }
}
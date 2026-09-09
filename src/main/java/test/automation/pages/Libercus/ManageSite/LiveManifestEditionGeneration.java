package test.automation.pages.Libercus.ManageSite;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import test.automation.framework.Page;
import static test.automation.framework.Actions.*;
public class LiveManifestEditionGeneration extends Page {
    @FindBy(xpath = "//pre[contains(text(),'Connection successful')]")
    public WebElement connectionSucessful;
    @FindBy(xpath = "//span[text()='Loading complete!']")
    public WebElement loadingComplete;
    @FindBy(xpath = "//a[text()='Build Live Manifest (After Midnight)']")
    public static WebElement afterMidnight;
    @FindBy(xpath = "//a[text()='Build Stage Manifest (Before Midnight)']")
    public static WebElement beforeMidnight;
    @FindBy(xpath = "//button[text()='ACKNOWLEDGE']")
    public static WebElement acknowledge;
    public void AfterMidnight() throws Exception {
        jsClick(afterMidnight);
    }
    public void BeforeMidnight() throws Exception {
        jsClick(beforeMidnight);
    }
    public void VerifyEditionGeneration() throws InterruptedException {
        waitUntilElementPresent(loadingComplete, 900);
        Assertions.assertTrue(connectionSucessful.isDisplayed());
    }
}

package test.automation.pages.Libercus.ManageSite;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import test.automation.framework.Page;
import static test.automation.framework.Actions.click;
public class ThumbnailsGeneration extends Page {
    @FindBy(xpath = "//pre[contains(text(),'Connection successful')]")
    public WebElement connectionSucessful;
    @FindBy(xpath = "//span[text()='Loading complete!']")
    public WebElement loadingComplete;
    @FindBy(xpath = "//a[text()='Build Thumbnails (After Midnight)']")
    public static WebElement afterMidnight;
    @FindBy(xpath = "//a[text()='Build Thumbnails (Before Midnight)']")
    public static WebElement beforeMidnight;
    @FindBy(xpath = "//button[text()='ACKNOWLEDGE']")
    public static WebElement acknowledge;
    public void AfterMidnight() throws Exception {
        click(afterMidnight);
    }
    public void BeforeMidnight() throws Exception {
        click(beforeMidnight);
    }
}

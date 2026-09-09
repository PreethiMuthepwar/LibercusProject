package test.automation.pages.Libercus.ManageSite;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import test.automation.framework.Browser;
import test.automation.framework.Page;
import static test.automation.framework.Actions.*;
public class RecacheSite extends Page
{
    @FindBy(xpath = "//pre")
    public static WebElement recacheText;
    @FindBy(xpath = "//a[text()='Recache Live eBlade']")
    public static WebElement recacheLiveEBlade;
    public static void reacheIsSucessfull() throws InterruptedException {
        shortWait();
        WebElement iframe = Browser.getDriver(By.cssSelector("story-preview")).findElement(By.xpath("//iframe[@id='run']"));
        Browser.getDriver(By.cssSelector("story-preview")).switchTo().frame(iframe);
        waitUntilElementPresent(recacheText, 60);
        WebElement preElement = Browser.getDriver(By.cssSelector("story-preview")).findElement(By.xpath("//pre"));
        preElement.getText();
        Browser.getDriver(By.cssSelector("story-preview")).switchTo().defaultContent();
    }
}

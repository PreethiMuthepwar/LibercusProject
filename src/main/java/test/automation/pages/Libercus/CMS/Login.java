package test.automation.pages.Libercus.CMS;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import test.automation.framework.Browser;
import test.automation.framework.Page;
import java.time.Duration;

import static org.codehaus.groovy.tools.shell.util.Preferences.get;
import static test.automation.framework.Actions.*;
import static test.automation.utils.testDataUtils.getCredentials;
import test.automation.framework.Config;
public class Login extends Page
{
    public static final String URL = Config.getUrl();
    @FindBy(name = "username")
    public static WebElement userName;
    @FindBy(name = "password")
    public static WebElement passWord;
    @FindBy(name = "login")
    public static WebElement loginButton;
    public static void validLogin() throws InterruptedException {
        String username = (String) getCredentials().get("username");
        String password = (String) getCredentials().get("password");
        sendKeys("userName", username);
        sendKeys("passWord", password);
        click("loginButton");
        WebDriverWait wait = new WebDriverWait(Browser.getDriver(By.cssSelector("story-preview")), Duration.ofSeconds(240));
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(),'Print shapes and styles loading complete!')]")));
    }
}
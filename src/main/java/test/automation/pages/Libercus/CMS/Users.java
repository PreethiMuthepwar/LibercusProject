package test.automation.pages.Libercus.CMS;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import test.automation.framework.Page;
import test.automation.framework.Random;
import static test.automation.framework.Actions.*;
import static test.automation.utils.testDataUtils.getUserData;
public class Users extends Page
{
    @FindBy(xpath = "//div[@id='siteusersPanel']//span[@class='ui-button-text'][text()='New']")
    public static WebElement newButton;
    @FindBy(xpath = "//a[text()='Returned E-mails']")
    public static WebElement creationPage;
    @FindBy(xpath = "//input[@name='LoginName']")
    public static WebElement loginName;
    @FindBy(xpath = "//input[@name='UserName']")
    public static WebElement userName;
    @FindBy(xpath = "//input[@name='Password']")
    public static WebElement password;
    @FindBy(xpath = "//input[@name='Password2']")
    public static WebElement repeatPassword;
    @FindBy(xpath = "//input[@name='EMailAddress']")
    public static WebElement email;
    @FindBy(xpath = "//button[@class='lib-button-saveclose ui-button ui-widget ui-state-default ui-corner-all ui-button-text-icon-primary btnFixed']//span[@class='ui-button-text'][text()='Save & Close']")
    public static WebElement saveAndCloseButton;
    public static String beforeCount;
    public static String afterCount;
    public static void creationPageIsDisplayed()
    {
        waitUntilElementPresent(creationPage);
        creationPage.isDisplayed();
    }
    public static String randomValue= Random.getRandomString(3);
    public static String name = (String) getUserData().get("LoginName");
    public static void enterLoginName()
    {
        name=name+randomValue;
        loginName.sendKeys(name);
    }
    public static String user = (String) getUserData().get("UserName");
    public static void enterUserName()
    {
        userName.sendKeys(user);
    }
    public static String password1 = (String) getUserData().get("password");
    public static void enterPassword()
    {
        String password2 = (String) getUserData().get("RepeatPassword");
        password.sendKeys(password1);
        repeatPassword.sendKeys(password2);
    }
    public static void enterEmailAddress()
    {
        String emailAddress=(String) getUserData().get("EmailAddress");
        email.sendKeys(emailAddress);
    }
    public static void verifyUserCreation() throws InterruptedException {
        StoryCreation storyCreation=new StoryCreation();
        afterCount = storyCreation.numberOfRecords("countOfUsers");
        storyCreation.verifyCopyOfStories(beforeCount, afterCount, "Copy");
    }
    public static void userCredentials()
    {
        Login login=new Login();
        sendKeys("userName", name);
        sendKeys("passWord", password1);
        click("loginButton");
    }

}

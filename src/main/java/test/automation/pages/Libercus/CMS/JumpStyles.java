package test.automation.pages.Libercus.CMS;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import test.automation.framework.Browser;
import test.automation.framework.Page;
import static test.automation.framework.Actions.waitUntilElementPresent;
public class JumpStyles extends Page
{
    @FindBy(xpath = "//div[@id='printjumpstylesPanel']//span[@class='ui-button-text'][text()='New']")
    public static WebElement newButton;
    @FindBy(xpath = "//div[@id='printjumpstylesEdit-main']//label[@for='Enabled'][text()='Enabled']")
    public static WebElement enabledText;
    @FindBy(xpath = "//div[@id='printjumpstylesEdit-main']//input[@name='StyleName']")
    public static WebElement styleName;
    @FindBy(xpath = "//div[@id='printjumpstylesPanelEdit']//span[@class='ui-button-text'][text()='Save & Close']")
    public static WebElement saveAndCloseButton;
    @FindBy(xpath = "//div[@id='printjumpstyles-main']//div[@class='cell top'][text()='Style Name']")
    public static WebElement styleNameLabel;
    public static void jumpStyleCreationPage()
    {
        waitUntilElementPresent(enabledText);
    }
    public static String style="created through auto";
    public static void enterNameForJumpStyle()
    {
        styleName.sendKeys(style);
    }
    public static void verifyJumpStyleIsCreated()
    {
        waitUntilElementPresent(styleNameLabel);
        String imageStyleName= Browser.getDriver(By.cssSelector("story-preview")).findElement(By.xpath("(//div[@id='printjumpstylesPanel']//div[@class='libListContent']//div[@class='libListContentRow libLine']//div[@class='libListContentItem libCol '])[1]")).getText();
        Assertions.assertTrue(style.equalsIgnoreCase(imageStyleName));
    }
}

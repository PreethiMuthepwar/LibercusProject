package test.automation.pages.Libercus.CMS;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import test.automation.framework.Browser;
import test.automation.framework.Page;
import static test.automation.framework.Actions.waitUntilElementPresent;
public class ImageStyles extends Page
{
    @FindBy (xpath = "//button[@class='lib-button-new ui-button ui-widget ui-state-default ui-corner-all ui-button-text-icon-primary btnFixed']//span[@class='ui-button-text'][normalize-space()='New']")
    public static WebElement newButton;
    @FindBy(xpath = "//a[text()='Notes']")
    public static WebElement notes;
    @FindBy(xpath = "//input[@name='StyleName']")
    public static WebElement styleName;
    @FindBy(xpath ="//button[@class='lib-button-saveclose ui-button ui-widget ui-state-default ui-corner-all ui-button-text-icon-primary btnFixed']//span[@class='ui-button-text'][text()='Save & Close']")
    public static WebElement saveAndCloseButton;
    @FindBy(xpath = "//div[contains(text(),'Style Name')]")
    public static WebElement styleNameLabel;
    public static void imageStylesCreationPage()
    {
        waitUntilElementPresent(notes);
        notes.isDisplayed();
    }
    public static String style="Created through auto";
    public static void enterImageStyleName()
    {
        styleName.sendKeys(style);
    }
    public static void verifyimagestyleIsCreated()
    {
        waitUntilElementPresent(styleNameLabel);
        String imageStyleName=Browser.getDriver(By.cssSelector("story-preview")).findElement(By.xpath("(//div[@class='libListContentRow libLine']//div[@class='libListContentItem libCol '])[1]")).getText();
        Assertions.assertTrue(style.equalsIgnoreCase(imageStyleName));
    }
}

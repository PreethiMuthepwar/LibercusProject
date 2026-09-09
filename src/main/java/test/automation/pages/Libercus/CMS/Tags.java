package test.automation.pages.Libercus.CMS;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import test.automation.framework.Browser;
import test.automation.framework.Page;
import java.util.List;
import static test.automation.framework.Wait.untilElementPresent;
import static test.automation.utils.testDataUtils.getTagData;
public class Tags extends Page
{
    @FindBy(xpath = "//div[@id='tagsPanel']//span[@class='ui-button-text'][text()='New']")
    public static WebElement newButton;
    @FindBy(xpath = "//label[@for='Active']")
    public static WebElement enabled;
    @FindBy(xpath = "//input[@name='DisplayVersion']")
    public static WebElement tagDisplayName;
    @FindBy(xpath = "//input[@name='ShortVersion']")
    public static WebElement tagShortName;
    @FindBy(xpath = "//div[@id='tagsPanelEdit']//span[@class='ui-button-text'][text()='Save & Close']")
    public static WebElement saveAndCloseButton;
    @FindBy(xpath = "//input[@name='Tag']")
    public static WebElement searchBox;
    @FindBy(xpath = "//div[@id='tagsPanel']//div[@class='libListContent']//div[5]")
    public static List<WebElement> shadowHost;
    public static String beforeCountOfTags;
    public static void TagscreationPage()
    {
        untilElementPresent(enabled);
        Assertions.assertTrue(enabled.isDisplayed());
    }
    public static String DisplayName=(String) getTagData().get("TagDisplayName");
    public static void enterTagDisplayName()
    {
        tagDisplayName.sendKeys(DisplayName);
    }
    public static String ShortName=(String) getTagData().get("TagShortName");
    public static void enterTagShortName()
    {
        tagShortName.sendKeys(ShortName);
    }
    public static String afterCount;
    public static void verifyTagCreation() throws InterruptedException
    {
        StoryCreation storyCreation=new StoryCreation();
        afterCount = storyCreation.numberOfRecords("countOfTags");
        storyCreation.verifyCopyOfStories(beforeCountOfTags, afterCount, "Copy");
    }
    public static void enterTextInSearchBox()
    {
        searchBox.sendKeys(DisplayName);
        Browser.getDriver(By.cssSelector("story-preview")).findElement(By.xpath("//div[@id='tags-main']//span[@class='ui-button-text'][text()='Update']")).click();
    }
    public static void verifySearchResultsOfTags()
    {
        for(WebElement host: shadowHost)
        {
            System.out.println(host.getText());
            Assertions.assertTrue(host.getText().contains(DisplayName) || host.getText().equalsIgnoreCase(DisplayName), "search item is not displayed");
        }
    }
}
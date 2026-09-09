package test.automation.pages.Libercus.CMS;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;
import test.automation.framework.Browser;
import test.automation.framework.Page;
import java.util.List;
import static test.automation.framework.Actions.*;
import static test.automation.framework.Random.getRandomIntBetween;
import static test.automation.framework.Wait.untilElementPresent;
public class Styles extends Page
{
    @FindBy(xpath = "//div[@id='printstylesPanel']//span[@class='ui-button-text'][text()='New']")
    public static WebElement newButton;
    @FindBy(xpath = "//a[text()='Notes']")
    public static WebElement notesButton;
    @FindBy(xpath = "//input[@name='StyleName']")
    public static WebElement styleName;
    @FindBy(xpath = "//div[@id='printstylesPanelEdit']//span[@class='ui-button-text'][text()='Save & Close']")
    public static WebElement saveAndCloseButton;
    @FindBy(xpath = "//select[@name='fontstyle']")
    public static WebElement styleNameDropdown;
    @FindBy(xpath = "//input[@name='Style']")
    public static WebElement searchStyle;
    @FindBy(xpath = "//div[@id='printstyles-main']//span[@class='ui-button-text'][text()='Update']")
    public static WebElement updateButton;
    @FindBy(xpath = "//div[@id='printstyles-main']//input[@name='marked']")
    public static List<WebElement> listOfStyles;
    @FindBy(xpath = "//button[@class='lib-button-copy ui-button ui-widget ui-state-default ui-corner-all ui-button-text-icon-primary btnFixed']")
    public static WebElement Copy;
    @FindBy(xpath = "(//span[@class='ui-button-ext'][text()='OK'])[2]")
    public static WebElement okayButton;
    @FindBy(xpath = "//button[@class='lib-button-delete ui-button ui-widget ui-state-default ui-corner-all ui-button-text-icon-primary btnFixed']//span[@class='ui-button-text'][text()='Delete']")
    public static WebElement Delete;
    public static void stylesCreationPage() throws InterruptedException {
        untilElementPresent(notesButton);
        Assertions.assertTrue(notesButton.isDisplayed());
    }
    public static String styleNameText="styleNameText";
    public static void enterStyleName()
    {
        styleName.sendKeys(styleNameText);
    }
    public static void verifyStyleisDisplayed() throws InterruptedException
    {
        Select select = new Select(styleNameDropdown);
        Assertions.assertTrue(isDisplayed(styleNameDropdown), styleNameDropdown + " not displayed on " + getCurrentPageName());
        select.selectByVisibleText(styleNameText);
    }
    public static void searchForStyle()
    {
        searchStyle.sendKeys(styleNameText);
    }
    public static void verifySearchResultsForStyles()
    {
        List<WebElement> searchResults= Browser.getDriver(By.cssSelector("story-preview")).findElements(By.xpath("(//div[@id='printstyles-main']//div[@class='libListContentRow libLine'])"));
        if(!searchResults.isEmpty())
        {
            for(int i=1;i<=searchResults.size();i=i+3)
            {
                WebElement text=Browser.getDriver(By.cssSelector("story-preview")).findElement(By.xpath("(//div[@id='printstyles-main']//div[@class='libListContentItem libCol '])["+i+"]"));
                if(text.isDisplayed()) {
                    Assertions.assertTrue(text.getText().contains(styleNameText), "search item is not displayed");
                }
            }
        }
    }
    static String beforeCopyCount="";
    public static void selectStyleToCopy() throws InterruptedException
    {
        StoryCreation storyCreation = new StoryCreation();
        beforeCopyCount = storyCreation.numberOfRecords("countOfStyles");
        shortWait();
        jsClick(listOfStyles.get(getRandomIntBetween(0, listOfStyles.size() - 1)));
    }
    static String afterCopyCount="";
    public static void  okButtonOfStyles() throws InterruptedException
    {
        waitUntilElementPresent(Browser.getDriver(By.cssSelector("story-preview")).findElement(By.xpath("//span[text()='OK']")));
        Browser.getDriver(By.cssSelector("story-preview")).findElement(By.xpath("//span[text()='OK']")).click();
        shortWait();
        StoryCreation storyCreation = new StoryCreation();
        afterCopyCount = storyCreation.numberOfRecords("countOfStyles");
    }
    public static void verifyCountOfStyles(String functionality) throws InterruptedException
    {
        if(functionality.equalsIgnoreCase("Copy"))
            StoryCreation.verifyCopyOfStories(beforeCopyCount,afterCopyCount,"Copy");
        else
           StoryCreation.verifyCopyOfStories(beforeCopyCount,afterCopyCount,"Delete");
    }
}
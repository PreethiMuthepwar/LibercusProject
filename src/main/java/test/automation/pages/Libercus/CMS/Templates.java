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
import static test.automation.framework.Browser.getScreenShot;
import static test.automation.framework.Random.getRandomIntBetween;
import static test.automation.framework.Wait.untilElementPresent;
public class Templates extends Page
{
    @FindBy(xpath = "//div[@id='printpagetemplatesPanel']//span[@class='ui-button-text'][text()='New']")
    public static WebElement newButton;
    @FindBy(xpath = "//a[text()='Layout']")
    public static WebElement layoutButton;
    @FindBy(xpath = "//input[@name='PageDefName']")
    public static WebElement templateName;
    @FindBy(xpath = "//input[@name='PageDefColumns']")
    public static WebElement columns;
    @FindBy(xpath = "//div[@id='printpagetemplatesPanelEdit']//span[@class='ui-button-text'][text()='Save & Close']")
    public static WebElement saveAndCloseButton;
    @FindBy(xpath = "//input[@name='Template']")
    public static WebElement enterSearchText;
    @FindBy(xpath = "//div[@id='printpagetemplates-main']//span[@class='ui-button-text'][text()='Update']")
    public static WebElement updateButton;
    @FindBy(xpath = "//div[@id='printpagetemplates-main']/div[@class='libSplitContainer']/div[@class='libListContent']/div[@class='libListContentRow libLine']/div[@class='libCheckColumn libNoClick']/input")
    public static List<WebElement> listOfTemplates;
    @FindBy(xpath = "//button[@class='lib-button-delete ui-button ui-widget ui-state-default ui-corner-all ui-button-text-icon-primary btnFixed']//span[@class='ui-button-text'][text()='Delete']")
    public static WebElement Delete;
    @FindBy(xpath = "//button[@class='lib-button-copy ui-button ui-widget ui-state-default ui-corner-all ui-button-text-icon-primary btnFixed']//span[@class='ui-button-text'][text()='Copy']")
    public static WebElement Copy;
    public static void templateCreationPage()
    {
        untilElementPresent(layoutButton);
        Assertions.assertTrue(layoutButton.isDisplayed());
    }
    public static String selectTemplateName="Created";
    public static void enterTemplateName()
    {
        templateName.sendKeys(selectTemplateName);
    }
    public static void enterColumnSizeforTemplate()
    {
        columns.sendKeys("6");
    }
    public static void selectTemplateOnPage()
    {
        Select select = new Select(PrintPages.templeteDropdown);
        Assertions.assertTrue(isDisplayed(PrintPages.templeteDropdown), selectTemplateName + " not displayed on " + getCurrentPageName());
        select.selectByVisibleText(selectTemplateName);
    }
    public static void pageCreation()
    {
        untilElementPresent(PrintPages.Layout);
    }
    public static void searchForTemplate()
    {
        enterSearchText.sendKeys(selectTemplateName);
    }

    public static void verifySearchResultsForTemplate()
    {
        List<WebElement> searchResults= Browser.getDriver(By.cssSelector("story-preview")).findElements(By.xpath("//div[@id='printpagetemplates-main']/div[@class='libSplitContainer']/div[@class='libListContent']/div"));
        if(!searchResults.isEmpty())
        {
            for(int i=1;i<=searchResults.size();i++)
            {
                WebElement text=Browser.getDriver(By.cssSelector("story-preview")).findElement(By.xpath("(//div[@id='printpagetemplates-main']/div[@class='libSplitContainer']/div[@class='libListContent']/div[@class='libListContentRow libLine']//div[@class='libListContentItem libCol '][1])["+i+"]"));
                System.out.println(text.getText());
                Assertions.assertTrue(text.getText().contains(selectTemplateName) || text.getText().equalsIgnoreCase(selectTemplateName), "search item is not displayed");
            }
        }
    }
    static String beforeCopyCount="";
    public static void selectTemplate() throws InterruptedException
    {
        shortWait();
        jsClick(listOfTemplates.get(getRandomIntBetween(0, listOfTemplates.size() - 1)));
    }
    public static void okButtonForTemplates() throws InterruptedException
    {
        shortWait();
        getScreenShot();
        waitUntilElementPresent(Browser.getDriver(By.cssSelector("story-preview")).findElement(By.xpath("//span[text()='OK']")));
        Browser.getDriver(By.cssSelector("story-preview")).findElement(By.xpath("//span[text()='OK']")).click();
        shortWait();
    }
}
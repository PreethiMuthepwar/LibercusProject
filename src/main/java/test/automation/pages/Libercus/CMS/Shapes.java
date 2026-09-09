package test.automation.pages.Libercus.CMS;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import test.automation.framework.Browser;
import test.automation.framework.Page;
import java.util.List;
import static test.automation.framework.Actions.*;
import static test.automation.framework.Random.getRandomIndex;
import static test.automation.framework.Random.getRandomIntBetween;
import static test.automation.framework.Wait.untilElementPresent;
import static test.automation.pages.Libercus.CMS.StoryCreation.selectShape;

public class Shapes extends Page
{
    @FindBy(xpath = "//div[@id='printshapesPanel']//span[@class='ui-button-text'][text()='New']")
    public static WebElement newButton;
    @FindBy(xpath = "//label[text()='Enabled']")
    public static WebElement enabledText;
    @FindBy(xpath = "//input[@name='Enabled']")
    public static WebElement enabled;
    @FindBy(xpath = "//input[@name='ShapeID']")
    public static WebElement shapeId;
    @FindBy(xpath = "//input[@name='ShapeName']")
    public static WebElement shapeName;
    @FindBy(xpath = "//select[@name='Canvas']")
    public static WebElement templetDropdown;
    @FindBy(xpath = "//div[@id='printshapesPanelEdit']//span[@class='ui-button-text'][text()='Save & Close']")
    public static WebElement saveAndCloseButton;
    @FindBy(xpath = "//input[@name='Shape']")
    public static WebElement searchShape;
    @FindBy(xpath = "//div[@id='printshapes-main']//span[@class='ui-button-text'][text()='Update']")
    public static WebElement updateButton;
    @FindBy(xpath = "//div[@id='printshapes-main']//input[@name='marked']")
    public static List<WebElement> listOfShapes;
    @FindBy(xpath = "//button[@class='lib-button-copy ui-button ui-widget ui-state-default ui-corner-all ui-button-text-icon-primary btnFixed']//span[@class='ui-button-text'][text()='Copy']")
    public static WebElement Copy;
    @FindBy(xpath = "//button[@class='ui-button ui-widget ui-state-default ui-corner-all ui-button-text-icon-primary']//span[@class='ui-button-text'][text()='OK']")
    public static WebElement okayButton;
    @FindBy(xpath = "//button[@class='lib-button-delete ui-button ui-widget ui-state-default ui-corner-all ui-button-text-icon-primary btnFixed']//span[@class='ui-button-text'][text()='Delete']")
    public static WebElement Delete;


    public static void creationPage() throws InterruptedException {
        shortWait();
        untilElementPresent(enabledText);
        Assertions.assertTrue(enabledText.isDisplayed());
    }
    public static void enterShapeId() throws InterruptedException {
        shortWait();
        int randomNumber=getRandomIndex(100);
        shapeId.sendKeys("test"+randomNumber);
    }
    public static String shapeNameText="testShapeCreation";
    public static void enterShapeName()
    {
        shapeName.sendKeys(shapeNameText);
    }
    public static void verifyShapeisDisplayed() throws InterruptedException {
        selectShape(shapeNameText);
    }
    public static void searchForShape()
    {
        searchShape.sendKeys(shapeNameText);
    }
    public static void verifySearchResults()
    {
        List<WebElement> searchResults=Browser.getDriver(By.cssSelector("story-preview")).findElements(By.xpath("(//div[@id='printshapesPanel']//div[@class='libListContentItem libCol '])"));
        if(!searchResults.isEmpty())
        {
            for(int i=1;i<=searchResults.size();i=i+4)
            {
                WebElement text=Browser.getDriver(By.cssSelector("story-preview")).findElement(By.xpath("(//div[@id='printshapesPanel']//div[@class='libListContentItem libCol '])["+i+"]"));
                Assertions.assertTrue(text.getText().contains(shapeNameText), "search item is not displayed");
            }
        }
    }
    static String beforeCopyCount="";
    public static void selectShapeToCopy() throws InterruptedException
    {
        StoryCreation storyCreation = new StoryCreation();
        beforeCopyCount = storyCreation.numberOfRecords("countOfShapes");
        shortWait();
        jsClick(listOfShapes.get(getRandomIntBetween(0, listOfShapes.size() - 1)));
    }
    static String afterCopyCount="";
    public static void okButton() throws InterruptedException
    {
        waitUntilElementPresent(Browser.getDriver(By.cssSelector("story-preview")).findElement(By.xpath("//span[text()='OK']")));
        Browser.getDriver(By.cssSelector("story-preview")).findElement(By.xpath("//span[text()='OK']")).click();;
        shortWait();
        StoryCreation storyCreation = new StoryCreation();
        afterCopyCount = storyCreation.numberOfRecords("countOfShapes");
    }
    public static void verifyCount(String functionality) throws InterruptedException
    {
        if (functionality.equalsIgnoreCase("Copy"))
        {
            StoryCreation.verifyCopyOfStories(beforeCopyCount,afterCopyCount,"Copy");
        } else if (functionality.equalsIgnoreCase("Delete")) {
            StoryCreation.verifyCopyOfStories(beforeCopyCount, afterCopyCount, "Delete");
        }
    }
}
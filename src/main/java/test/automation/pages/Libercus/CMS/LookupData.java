package test.automation.pages.Libercus.CMS;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import test.automation.framework.Page;
public class LookupData extends Page
{
    @FindBy(xpath = "//div[@id='congerolookuptypesPanel']//span[@class='ui-button-text'][text()='New']")
    public static WebElement newButton;
    @FindBy(xpath = "//label[text()='Lookup name']")
    public static WebElement LookupName;
    @FindBy(xpath = "//input[@name='LookupName']")
    public static WebElement lookupNameData;
    @FindBy(xpath = "//input[@class='manualsize lib-post libCheckboxNoValue']")
    public static WebElement enabled;
    @FindBy(xpath = "//textarea[@name='Description']")
    public static WebElement description;
    @FindBy(xpath = "//select[@name='Source']")
    public static WebElement Source;
    @FindBy(xpath = "//button[@class='lib-button-saveclose ui-button ui-widget ui-state-default ui-corner-all ui-button-text-icon-primary btnFixed']//span[@class='ui-button-text'][text()='Save & Close']")
    public static WebElement saveAndCloseButton;
    public static void enterTextInlookupName()
    {
        lookupNameData.sendKeys("Base Media Files");
    }
    public static void enterTextInDescription()
    {
        description.sendKeys("Base Media files");
    }
}
package test.automation.pages.Libercus.CMS;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import test.automation.framework.Browser;
import test.automation.framework.Page;
import static test.automation.pages.Libercus.CMS.HomePage.clickOnMainMenu;
import static test.automation.pages.Libercus.CMS.HomePage.clickSubMenu;
import static test.automation.utils.testDataUtils.getCongeroData;

public class CongeroTypes extends Page
{
   @FindBy(xpath = "//div[@id='congerotypesPanel']//span[@class='ui-button-text'][text()='New']")
    public static WebElement newButton;
   @FindBy(xpath = "//label[text()='Type Name']")
    public static WebElement TypeName;
   @FindBy(xpath = "//input[@name='TypeName']")
   public static WebElement typeNameData;
   @FindBy(xpath = "//select[@name='SectionID']")
   public static WebElement sectionId;
   @FindBy(xpath = "//input[@name='CongeroLabel']")
   public static WebElement objectFileName;
   @FindBy(xpath = "//div[@id='congerotypesEdit-main']//input[@name='Active']")
   public static WebElement enabled;
   @FindBy(xpath = "//input[@name='InMenu']")
   public static WebElement showInMenu;
   @FindBy(xpath = "//input[@name='IndexSearch']")
   public static WebElement includeInSearch;
   @FindBy(xpath = "//div[@id='congerotypesPanelEdit']//span[@class='ui-button-text'][text()='Save & Close']")
   public static WebElement  saveAndCloseButton;
   @FindBy(xpath = "//select[@class='libListSelectButtonBarItem lib-framefield-select-FieldDefs']")
   public static WebElement FieldDefinitionsDataType;
   @FindBy(xpath = "//div[@class='libAdvancedField libListField lib-group1']//span[@class='ui-button-text'][text()='Add']")
   public static WebElement Add;
   @FindBy(xpath = "//div[@class='libListLine']")
   public static WebElement fieldDefinationisAdded;
   @FindBy(xpath = "//input[@name='FieldID']")
   public static WebElement fieldId;
   @FindBy(xpath = "//input[@name='FieldName']")
   public static WebElement fieldName;
   @FindBy(xpath = "//select[@name='FieldSource']")
   public static WebElement dataSource;
   @FindBy(xpath = "//button[@class='lib-button-new ui-button ui-widget ui-state-default ui-corner-all ui-button-text-icon-primary btnFixed']//span[@class='ui-button-text'][text()='New']")
   public static WebElement newButtonOfCongero;
   public static String subMenu = (String) getCongeroData("CongeroType").get("TypeName");
   public static void enterTypeName()
   {
       typeNameData.sendKeys(subMenu);
   }
   public static String objectName = (String) getCongeroData("CongeroType").get("ObjectFileName");
   public static void enterObjectFileName()
   {
       objectFileName.sendKeys(objectName);
   }
   public static void verifyFieldisAdded()
   {
       Assertions.assertTrue(fieldDefinationisAdded.isDisplayed());
   }
   public static String typeFieldId = (String) getCongeroData("CongeroType").get("FieldId");
   public static void enterFieldId()
   {
       fieldId.sendKeys(typeFieldId);
   }
   public static String getTypeFieldName = (String) getCongeroData("CongeroType").get("FieldName");
   public static void enterFieldName()
   {
       fieldName.sendKeys(getTypeFieldName);
   }
   public static void refresh()
   {
      Browser.getDriver(By.cssSelector("story-preview")).navigate().refresh();
   }
   public static void verifyCongeroTypeisCreated() throws Exception {
      clickOnMainMenu("Congero");
      clickSubMenu(subMenu);
   }
   public static void verifyFieldIsDisplayed()
   {
      Browser.getDriver(By.cssSelector("story-preview")).findElement(By.xpath("//label[text()='"+getTypeFieldName+"']")).isDisplayed();
   }
}

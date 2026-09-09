package test.automation.pages.Libercus.CMS;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import test.automation.framework.Browser;
import test.automation.framework.Config;
import test.automation.framework.Page;

import static org.codehaus.groovy.tools.shell.util.Preferences.get;
import static test.automation.framework.Actions.*;
import static test.automation.framework.Actions.click;
public class HomePage extends Page {
    public static final String URL = get(Config.getUrl());
    @FindBy(xpath = "//a[text()='Home']")
    public static WebElement HomeText;
    @FindBy(xpath = "//a[@id='logout']")
    public static WebElement Logout;
    @FindBy(xpath = "//h3/a[text()='Content']")
    public static WebElement content;
    @FindBy(xpath = "//div[@class='menuItem' and text()='Stories']")
    public WebElement stories;
    @FindBy(xpath = "//div[text()='Interactive Ads']")
    public static WebElement interactiveAds;
    @FindBy(xpath = "//a[@id='richmediaaction']")
    public static WebElement richMedia;
    @FindBy(xpath = "//div[text()='Print Ads']")
    public WebElement printAds;
    @FindBy(xpath = "//a[@id='printpagesaction']")
    public static WebElement PrintPages;
    @FindBy(xpath = "//a[text()='Feeds']")
    public WebElement feeds;
    @FindBy(xpath = "//div[text()='Wire Stories']")
    public WebElement WireStories;
    @FindBy(xpath = "//div[text()='Wire Photos']")
    public WebElement WirePhotos;
    @FindBy(xpath = "//a[text()='Congero Administration']")
    public WebElement CongeroAdministration;
    @FindBy(xpath = "//div[text()='Lookup Data']")
    public WebElement LookupData;
    @FindBy(xpath = "//div[@class='menuItem ui-state-highlight menuItemSelected']")
    public static WebElement CongeroTypes;
    @FindBy(xpath = "//a[text()='Print Administration']")
    public static WebElement PrintAdministration;
    @FindBy(xpath = "//div[text()='Shapes']")
    public static WebElement Shapes;
    @FindBy(xpath = "//div[text()='Styles']")
    public static WebElement Styles;
    @FindBy(xpath = "//div[text()='Templates']")
    public static WebElement Templates;
    @FindBy(xpath = "//div[text()='Image Styles']")
    public static WebElement ImageStyles;
    @FindBy(xpath ="//div[text()='Jump Styles']")
    public static WebElement JumpStyles;
    @FindBy(xpath = "//a[text()='Product Administration']")
    public static WebElement ProductAdministration;
    @FindBy(xpath = "//div[@class='menuItem ui-state-highlight menuItemSelected']")
    public static WebElement Users;
    @FindBy(xpath = "//div[text()='Tags']")
    public static WebElement Tags;
    public static void clickOnMainMenu(String menuName) throws Exception {
        shortWait();
        WebElement menu = Browser.getDriver(By.cssSelector("story-preview")).findElement(By.xpath("//h3/a[text()='" + menuName + "']"));
        waitUntil(() -> menu.isDisplayed());
        waitUntilElementPresent(menu, 30);
        click(menu);
    }
    public static void clickSubMenu(String subMenuName) throws Exception {
        shortWait();
        WebElement subMenu = Browser.getDriver(By.cssSelector("story-preview")).findElement(By.xpath("//div[text()='" + subMenuName + "']"));
        waitUntil(() -> subMenu.isDisplayed());
        waitUntilElementPresent(subMenu, 30);
        click(subMenu);
    }
    public static void VerifyHomeMessage() throws InterruptedException {
        shortWait();
        String HomeMsg = HomeText.getText();
        String expectedText = "Home";
        Assertions.assertTrue(HomeMsg.equalsIgnoreCase(expectedText));
    }
    public static void TapOnPrintPages() throws InterruptedException {
        shortWait();
        PrintPages.click();
    }
}

package test.automation.pages.Libercus.PF3;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import test.automation.framework.Browser;
import test.automation.framework.Page;
import java.util.List;
import static test.automation.framework.Actions.*;
import static test.automation.framework.Actions.shortWait;
import static test.automation.pages.Libercus.PF3.HomePage.nextArrowForPage;
public class SettingsPage extends Page {
    @FindBy(xpath = "//*[text()=' General settings']")
    public static WebElement settingsHeader;
    @FindBy(xpath = "//span[text()='Article settings']")
    public static WebElement articleSettingsHeader;
    @FindBy(xpath = "//button/span/mat-icon[text()='close']")
    public static WebElement panelCloseButton;
    static int numberOfPagesBeforeDoublePage;
    public static void selectMenuLayoutStyle(String layoutType) throws Exception {
        shortWait();
        click(Browser.getDriver(By.cssSelector("story-preview")).findElement(By.xpath("//div[2]/div[2]/div[1]/mat-radio-group/mat-radio-button[@value='" + layoutType + "']")));
        shortWait();
        jsClick(panelCloseButton);
        shortWait();
    }
    public static void validateLayOutStyle(String layoutType) throws InterruptedException {
        layoutType = layoutType.toLowerCase();
        shortWait();
        WebElement ele = Browser.getDriver(By.cssSelector("story-preview")).findElement(By.xpath("//*[@class='app_menu_" + layoutType + "_holder theme_bg_color']"));
        Assertions.assertTrue(ele.isDisplayed());
    }
    public static void clickOnThemeColor(String themeColor) throws Exception {
        shortWait();
        themeColor = themeColor.toLowerCase();
        click(Browser.getDriver(By.cssSelector("story-preview")).findElement(By.xpath("//mat-radio-button[@value='theme_" + themeColor + "']")));
        shortWait();
        jsClick(panelCloseButton);
    }
    public static void validateLayoutColor(String expectedColor) throws InterruptedException {
        shortWait();
        expectedColor = expectedColor.toLowerCase();
        String actualColor = Browser.getDriver(By.cssSelector("story-preview")).findElement(By.xpath("//*[@class='app_menu_top_holder theme_bg_color']")).getCssValue("background-color");
        System.out.println(actualColor);
        switch (expectedColor) {
            case "green":
                Assertions.assertTrue(actualColor.equals("rgba(0, 177, 64, 1)"));
                break;
            case "blue":
                Assertions.assertTrue(actualColor.equals("rgba(78, 139, 215, 1)"));
                break;
        }
    }
    public static void validateDoublePage(String displayOption) throws Exception {
        shortWait();
        List<WebElement> shadowHost = Browser.getDriver(By.cssSelector("story-preview")).findElements(By.cssSelector("edition-page:not([style*='display: none'])"));
        shortWait();
        jsClick(nextArrowForPage.get(0));
        shortWait();
        JavascriptExecutor js = (JavascriptExecutor) Browser.getDriver(By.cssSelector("story-preview"));
        WebElement ele = (WebElement) js.executeScript(
                "return document.querySelector(arguments[1])", shadowHost, ".pf3Ads");
        if (ele == null) {
            shortWait();
            jsClick(nextArrowForPage.get(0));
            shortWait();
        }
        shortWait();
        shadowHost = Browser.getDriver(By.cssSelector("story-preview")).findElements(By.cssSelector("edition-page:not([style*='display: none'])"));
        shortWait();

        int numberOfPagesAfterDoublePage = shadowHost.size();
        System.out.println(numberOfPagesBeforeDoublePage);
        System.out.println(numberOfPagesAfterDoublePage);
        if (displayOption.toLowerCase().contains("double")) {
            shortWait();
            Assertions.assertTrue(!(numberOfPagesBeforeDoublePage == numberOfPagesAfterDoublePage));
        } else {
            shortWait();
            Assertions.assertTrue(numberOfPagesBeforeDoublePage == numberOfPagesAfterDoublePage);
        }
    }
    public static void selectDisplayOption(String displayOption) throws Exception {
        shortWait();
        click(Browser.getDriver(By.cssSelector("story-preview")).findElement(By.xpath("//*[text()='" + displayOption + "']")));
        shortWait();
        jsClick(panelCloseButton);
        shortWait();
    }
    public static void checkDefaultDisplayOption() throws Exception {
        shortWait();
        List<WebElement> shadowHost = Browser.getDriver(By.cssSelector("story-preview")).findElements(By.cssSelector("edition-page:not([style*='display: none'])"));
        numberOfPagesBeforeDoublePage = shadowHost.size();

    }
}
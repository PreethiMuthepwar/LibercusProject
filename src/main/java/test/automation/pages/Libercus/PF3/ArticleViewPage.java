package test.automation.pages.Libercus.PF3;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import test.automation.framework.Browser;
import test.automation.framework.Page;
import static test.automation.framework.Actions.*;
public class ArticleViewPage extends Page {
    @FindBy(xpath = "//button[@mattooltip='Open menu']")
    public static WebElement articleViewMenu;
    @FindBy(xpath = "//mat-icon[text()='arrow_forward']")
    public static WebElement nextButton;
    @FindBy(xpath = "//mat-icon[text()='arrow_back']")
    public static WebElement backButton;
    public static void selectMenuOnArticleView(String menu) throws Exception {
        shortWait();
        waitUntilElementPresent(Browser.getDriver(By.cssSelector("story-preview")).findElement(By.xpath("//span[text()='" + menu + "']")));
        click(Browser.getDriver(By.cssSelector("story-preview")).findElement(By.xpath("//span[text()='" + menu + "']")));
        shortWait();
    }
    public static void checkNextButtonFunctionality() throws InterruptedException {
        JavascriptExecutor js = (JavascriptExecutor) Browser.getDriver(By.cssSelector("story-preview"));
        shortWait();
        shortWait();
        while (nextButton.isEnabled()) {
            try {
                shortWait();
                String storyText = (String) js.executeScript(
                        "return document.querySelector('body > app-root > mat-drawer-container > mat-drawer-content > div > div > edition > story-preview > div > div.story_preview_content > div').shadowRoot.querySelector('h1').innerText;");
                shortWait();
                System.out.println(storyText);
                jsClick(nextButton);
                shortWait();
                String storyText1 = (String) js.executeScript(
                        "return document.querySelector('body > app-root > mat-drawer-container > mat-drawer-content > div > div > edition > story-preview > div > div.story_preview_content > div').shadowRoot.querySelector('h1').innerText;");
                shortWait();
                System.out.println(storyText1);
                Assertions.assertFalse(storyText.equals(storyText1));
            } catch (Exception e) {
                System.out.println("Element is NOT clickable");
                break;
            }
        }
    }
    public static void checkPreviousButtonFunctionality() throws InterruptedException {
        JavascriptExecutor js = (JavascriptExecutor) Browser.getDriver(By.cssSelector("story-preview"));
        shortWait();
        shortWait();
        while (backButton.isEnabled()) {
            try {
                shortWait();
                String storyText = (String) js.executeScript(
                        "return document.querySelector('body > app-root > mat-drawer-container > mat-drawer-content > div > div > edition > story-preview > div > div.story_preview_content > div').shadowRoot.querySelector('h1').innerText;");
                shortWait();
                System.out.println(storyText);
                jsClick(backButton);
                shortWait();
                String storyText1 = (String) js.executeScript(
                        "return document.querySelector('body > app-root > mat-drawer-container > mat-drawer-content > div > div > edition > story-preview > div > div.story_preview_content > div').shadowRoot.querySelector('h1').innerText;");
                shortWait();
                System.out.println(storyText1);
                Assertions.assertFalse(storyText.equals(storyText1));
            } catch (Exception e) {
                System.out.println("Element is NOT clickable");
                break;
            }
        }
    }
}
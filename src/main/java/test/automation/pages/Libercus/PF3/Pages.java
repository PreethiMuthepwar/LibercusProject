package test.automation.pages.Libercus.PF3;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import test.automation.framework.Browser;
import test.automation.framework.Page;
import static test.automation.framework.Actions.*;
import static test.automation.pages.Libercus.PF3.HomePage.*;
public class Pages extends Page {

    @FindBy(xpath = "//*[text()='Pages']")
    public static WebElement pagesHeader;
    @FindBy(xpath = "//div[@class='popup_toolbar_holder']/div/div/button/span/mat-icon[text()='close']")
    public static WebElement popUpCloseButton;
    public static void selectPage(String pageToBeSelected) throws InterruptedException {
        shortWait();
        try {
            WebElement webElement = Browser.getDriver(By.cssSelector("story-preview")).findElement(By.xpath("//img[contains(@src,'" + pageToBeSelected + ".jpg')]"));
            doubleClick(webElement);
            shortWait();
        } catch (Exception e) {
            System.out.println(pageToBeSelected + " Page Not found");
        }
        shortWait();
    }
    public static void selectSectionOnPagesPanel(String sectionToBeSelected) throws Exception {
        shortWait();
        try {
            click(popUpCloseButton);
            click(pages);
            shortWait();
            WebElement webElement = Browser.getDriver(By.cssSelector("story-preview")).findElement(By.xpath("//*[contains(text(), 'Section: '" + sectionToBeSelected + "')]"));
            click(webElement);
        } catch (Exception e) {
            System.out.println(sectionToBeSelected + " Page Not found");
        }
        shortWait();
    }
    public static void getPageNumber(int loopCount) throws InterruptedException {
        shortWait();
        JavascriptExecutor js = (JavascriptExecutor) Browser.getDriver(By.cssSelector("story-preview"));
        String textContent = (String) js.executeScript(
                "let host = document.querySelector('edition-page')[" + loopCount + "];" +
                        "if (!host) return 'Shadow host not found';" +
                        "let shadowRoot = host.shadowRoot;" +
                        "if (!shadowRoot) return 'No shadowRoot';" +
                        "let target = shadowRoot.querySelector('.libPageBodyLinebreak');" +
                        "return target ? target.textContent.trim() : 'Element not found in shadow root';"
        );
        System.out.println();
    }
}




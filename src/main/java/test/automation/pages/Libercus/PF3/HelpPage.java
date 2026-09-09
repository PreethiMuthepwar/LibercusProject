package test.automation.pages.Libercus.PF3;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import test.automation.framework.Browser;
import test.automation.framework.Page;
import java.util.List;
import java.util.Map;
import static test.automation.framework.Actions.shortWait;
import static test.automation.utils.testDataUtils.*;
public class HelpPage extends Page {
    @FindBy(xpath = "//*[text() ='Help']")
    public static WebElement helpHeader;
    @FindBy(xpath = "//mat-panel-title[@class='mat-expansion-panel-header-title']/h2")
    public static List<WebElement> helpMenuItems;
    @FindBy(xpath = "//mat-expansion-panel[contains(@class,'mat-expanded')]/div/div/mat-accordion/mat-expansion-panel")
    public static List<WebElement> subHelpMenuItems;
    public static void eachHelpElement() throws InterruptedException {
        for (WebElement eachContent : helpMenuItems) {
            shortWait();
            Assertions.assertTrue(eachContent.isDisplayed());
            eachContent.click();
            shortWait();
            for (WebElement subMenu : subHelpMenuItems) {
                Assertions.assertTrue(subMenu.isDisplayed());
                subMenu.click();
                String questionTitle = Browser.getDriver(By.cssSelector("story-preview")).findElement(By.xpath("//mat-expansion-panel[contains(@class,'mat-expanded')]/div/div/mat-accordion/mat-expansion-panel/mat-expansion-panel-header[@aria-expanded='true']")).getText();
                String eachContentText = eachContent.getText();
                System.out.println(eachContentText);
                Map<String, Object> helpContentMap = getHelpContent(eachContentText.trim());
                String actualKey = null;
                for (String key : helpContentMap.keySet()) {
                    if (key.trim().equalsIgnoreCase(questionTitle.trim())) {
                        actualKey = key;
                        break;
                    }
                }
                System.out.println(questionTitle);
                System.out.println(actualKey);
                shortWait();
                Assertions.assertTrue(actualKey.equalsIgnoreCase(questionTitle.trim()));
                String AnswerText = (String) getHelpContent(eachContentText).get(questionTitle.trim());
                System.out.println(AnswerText);
                String ExpectedAnswer = Browser.getDriver(By.cssSelector("story-preview"))
                        .findElement(By.xpath("//mat-expansion-panel[contains(@class,'mat-expanded')]/div/div/mat-accordion/mat-expansion-panel[contains(@class,'expanded')]/div/div/div"))
                        .getText().trim();
                System.out.println(ExpectedAnswer);
                Assertions.assertTrue(AnswerText.equalsIgnoreCase(ExpectedAnswer.trim()));
                shortWait();
            }
        }
    }
}
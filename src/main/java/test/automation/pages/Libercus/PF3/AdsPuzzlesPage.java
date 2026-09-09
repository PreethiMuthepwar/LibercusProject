package test.automation.pages.Libercus.PF3;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import test.automation.framework.Browser;
import test.automation.framework.Page;
import java.util.List;
import static test.automation.framework.Actions.*;
public class AdsPuzzlesPage extends Page {
    @FindBy(xpath = "//span[text()='Print Ads/Puzzles']")
    public static WebElement printAdsOrPuzzlesButton;
    @FindBy(xpath = "//span[text()='Interactive Ads']")
    public static WebElement interactiveAdsButton;
    @FindBy(xpath = "//span[text()='Interactive Puzzles']")
    public static WebElement insertsButton;
    @FindBy(xpath = "//span[text()='Inserts']")
    public static WebElement interactivePuzzlesButton;
    @FindBy(xpath = "//input[@formcontrolname='adDate']")
    public static WebElement dateInputElement;
    @FindBy(xpath = "//button/span[text()='Search Ads']")
    public static WebElement searchAdsButton;
    @FindBy(xpath = "//*[text()='file_download']")
    public static List<WebElement> listOfAdsForSearchedDate;
    @FindBy(xpath = "//mat-datepicker-toggle[contains(@class,'mat-datepicker-')]/button")
    public static WebElement calender;
    @FindBy(xpath = "//div[@class='items']/div[@class='ng-star-inserted']")
    public static List<WebElement> interactivePuzzles;
    public static void validatePanelHeader(String headerValue) throws InterruptedException {
        shortWait();
        WebElement header = Browser.getDriver(By.cssSelector("story-preview")).findElement(By.xpath("//*[text()='" + headerValue + "']"));
        Assertions.assertTrue(header.isDisplayed());
    }
    public static void enterDateAndClickSearchAds() throws Exception {
        click(calender);
        while (true) {
            String displayedMonthYear = Browser.getDriver(By.cssSelector("story-preview")).findElement(By.xpath("//span[contains(@id,'mat-calendar-button-')]")).getText();
//            System.out.println("Displayed Month-Year: " + displayedMonthYear);
            if (displayedMonthYear.contains("MAR")) {
                break;
            } else {
                Browser.getDriver(By.cssSelector("story-preview")).findElement(By.xpath("//button[@aria-label='Previous month']")).click();
                shortWait();
            }
        }
        WebElement dayElement = Browser.getDriver(By.cssSelector("story-preview")).findElement(By.xpath("//td[contains(@aria-label,'" + 24 + "')]"));
        click(dayElement);
        shortWait();
        click(searchAdsButton);
    }
    public static void selectAdAndPrint() throws Exception {
        try {
            if (!(Browser.getDriver(By.cssSelector("story-preview")).findElement(By.xpath("//div[text()='No Ads found.']")).isDisplayed())) {
                jsClick(listOfAdsForSearchedDate.get(0));
                switchToWindow(1);
                shortWait();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public static void printInteractiveAds() throws InterruptedException {
        shortWait();
        if ((Browser.getDriver(By.cssSelector("story-preview")).findElements(By.xpath("//*[text()='No Interactive Ads found.']")).size() == 0)) {
            List<WebElement> interactiveAds = Browser.getDriver(By.cssSelector("story-preview")).findElements(By.xpath("//div[@class='box']/img"));
            doubleClick(interactiveAds.get(0));
            shortWait();
        }
    }
    public static void checkInteractivePuzzles() {
        for (WebElement ele : interactivePuzzles) {
            Assertions.assertTrue(ele.isEnabled() && ele.isDisplayed());
        }

    }
    public static void checkInsertsFunctionality() {
        switchToWindow(1);
        Assertions.assertTrue(Browser.getDriver(By.cssSelector("story-preview")).getCurrentUrl().contains("https://flipp.com/weekly_ads"));

    }
}
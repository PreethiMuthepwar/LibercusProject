package test.automation.pages.Libercus.PF3;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import test.automation.framework.Browser;
import test.automation.framework.Page;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import static test.automation.framework.Actions.doubleClick;
import static test.automation.framework.Actions.shortWait;
public class EditionsPage extends Page {
    @FindBy(xpath = "//*[text()='Editions']")
    public static WebElement EditionsHeader;
    public static void navigateToAnotherEdition(String EditionDate) throws InterruptedException {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate date = LocalDate.parse(EditionDate, inputFormatter);
        String formattedDate = date.format(outputFormatter);
        WebElement eachEdition = Browser.getDriver(By.cssSelector("story-preview")).findElement(By.xpath("//img[contains(@src,'" + formattedDate + "')]"));
        shortWait();
        doubleClick(eachEdition);
        shortWait();
    }
    public static void VerifyEdition(String expectedDate) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate date = LocalDate.parse(expectedDate, inputFormatter);
        String formattedDate = date.format(outputFormatter);
        String currentUrl = Browser.getDriver(By.cssSelector("story-preview")).getCurrentUrl();
        Assertions.assertTrue(currentUrl.contains(formattedDate));
    }
}
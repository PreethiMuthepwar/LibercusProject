package test.automation.pages.Libercus.CMS;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;
import test.automation.framework.Browser;
import test.automation.framework.Page;
import java.util.List;
import static test.automation.framework.Actions.*;
import static test.automation.framework.Random.getRandomIndex;
import static test.automation.pages.Libercus.CMS.WireStories.*;
public class WirePhotos extends Page
{
    @FindBy(xpath = "//div[@id='feedphotos-main']//span[@class='ui-button-text'][text()='Update']")
    private static WebElement UpdateButton;
    @FindBy(xpath = "//div[@class='libCheckColumn rp3']")
    private static List<WebElement> listOfMediaFiles;
    @FindBy(xpath = "//button[@class='lib-button-assign ui-button ui-widget ui-state-default ui-corner-all ui-button-text-icon-primary btnFixed']//span[@class='ui-button-text'][text()='Use']")
    public static WebElement Use;
    @FindBy(xpath = "//input[@name='publishdate']")
    public static WebElement PublishDate;
    @FindBy(xpath = "//span[text()='Prev']")
    public WebElement previous;
    public static void verifyPhotosAreAvailable() throws InterruptedException {
        UpdateButton.click();
        List<WebElement> noItemsList = Browser.getDriver(By.cssSelector("story-preview")).findElements(By.xpath("//div[text()='No items found']"));
        if (!noItemsList.isEmpty() && noItemsList.get(0).
                isDisplayed()) {
            Select select = new Select(TransmittedDateRange);
            Assertions.assertTrue(isDisplayed(TransmittedDateRange));
            select.selectByVisibleText("Last 3 Months");
            UpdateButton.click();
            shortWait();
            if (!noItemsList.isEmpty() && noItemsList.get(0).isDisplayed()) {
                Select select2 = new Select(TransmittedDateRange);
                Assertions.assertTrue(isDisplayed(TransmittedDateRange));
                select2.selectByVisibleText("Last Year");
                UpdateButton.click();
            }
        }
    }
    public static int randomNumber;
    public static void selectPhoto() throws InterruptedException {
        randomNumber=getRandomIndex(listOfMediaFiles.size());
        System.out.println("Random number is: " + randomNumber);
        System.out.println(listOfMediaFiles.size());
        jsClick(listOfMediaFiles.get(randomNumber));
        title=titleInput.getAttribute("value");
    }
}

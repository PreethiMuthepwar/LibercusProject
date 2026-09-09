package test.automation.pages.Libercus.CMS;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import test.automation.framework.Browser;
import test.automation.framework.Page;
import java.text.SimpleDateFormat;
import java.util.List;
import static test.automation.framework.Actions.click;
import static test.automation.framework.Actions.shortWait;
import static test.automation.framework.Random.getRandomIntBetween;
import static test.automation.pages.Libercus.CMS.StoryCreation.isBefore;
public class ProductionView extends Page {
    static boolean evenFound = false;
    static boolean oddFound = false;
    @FindBy(xpath = "//div[text()='Production View']")
    public WebElement productionView;
    @FindBy(xpath = "//select[@name='channel']")
    public WebElement channelDropdown;
    @FindBy(xpath = "//a[text()='Tablet View']")
    public WebElement tabletView;
    @FindBy(xpath = "(//button[@name='refresh'])[13]")
    public static WebElement update;
    @FindBy(xpath = "(//a[text()='Publish Date'])[11]")
    public WebElement publishDate;
    @FindBy(xpath = "//fieldset[@name='ProdViewAds']")
    public static WebElement richAdPanel;
    @FindBy(xpath = "//div[contains(@id,'libDraggableAd_')]")
    public static List<WebElement> listOfDragableElements;
    public void selectDateWhichHasStory(String Date1) throws Exception {
        StoryCreation storyCreation = new StoryCreation();
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
        java.util.Date date2 = inputFormat.parse(Date1);
        SimpleDateFormat dayFormat = new SimpleDateFormat("DD");
        String dateNumber = dayFormat.format(date2);
        SimpleDateFormat monthYearFormat = new SimpleDateFormat("MMMM yyyy");
        String monthYear = monthYearFormat.format(date2);
        shortWait();
        click(storyCreation.publishDate);
        wait.until(ExpectedConditions.elementToBeClickable(storyCreation.calender));
        click(storyCreation.calender);
        shortWait();
        String dateToSelect = dateNumber;
        String monthYearToSelect = monthYear;
        while (true) {
            String displayedMonthYear = storyCreation.MonthYear.getText();
            if (displayedMonthYear.equals(monthYearToSelect)) {
                break;
            }
            if (isBefore(displayedMonthYear, monthYearToSelect)) {
                shortWait();
                click(storyCreation.previous);
            } else {
                shortWait();
                click(storyCreation.Next);
            }
        }
        WebElement select_date = Browser.getDriver(By.cssSelector("story-preview")).findElement(By.xpath("//a[text()='" + dateToSelect + "']"));
        select_date.click();
    }
    public static void selectRichMediaEditMode(String editMode) throws Exception {
        shortWait();
        click(Browser.getDriver(By.cssSelector("story-preview")).findElement(By.xpath("//span[text()='" + editMode + "']")));
        shortWait();
    }
    public static void richAdPanel() {
        richAdPanel.isDisplayed();
    }
    public static void adsAreDisplayed() throws InterruptedException {
        Actions actions = new Actions(Browser.getDriver(By.cssSelector("story-preview")));
        shortWait();
        shortWait();
        if (!listOfDragableElements.isEmpty()) {
            List<WebElement> PageNumbers = Browser.getDriver(By.cssSelector("story-preview")).findElements(By.xpath("//div[contains(@id, 'libPageBarTop_') and contains(@id, '_3')]//div[contains(@id, 'libPageSectionField_') and contains(@id, '_3')]"));
            for (int i = 1; i <= PageNumbers.size(); i++) {
                PageNumbers = Browser.getDriver(By.cssSelector("story-preview")).findElements(By.xpath("//div[contains(@id, 'libPageBarTop_') and contains(@id, '_3')]//div[contains(@id, 'libPageSectionField_') and contains(@id, '_3')]"));
                String text = PageNumbers.get(i).getText().trim();
                System.out.println(text);
                String numericPart = text.replaceAll("\\D", "");
                System.out.println(numericPart);
                if (!numericPart.isEmpty()) {
                    int pageNumber = Integer.parseInt(numericPart);
                    if (pageNumber % 2 == 0) {
                        evenFound = true;
                        int size = listOfDragableElements.size();
                        int xpathIndex = Math.max(2, size);
                        shortWait();
                        shortWait();
                        List<WebElement> source = Browser.getDriver(By.cssSelector("story-preview")).findElements(By.xpath("//div[contains(@id,'libDraggableAd_')]"));
                        WebElement target = Browser.getDriver(By.cssSelector("story-preview")).findElement(By.xpath("(//div[contains(@id, 'libPageBarTop_') and contains(@id, '_3')]//div[contains(@id, 'libPageSectionField_') and contains(@id, '_3')])[" + pageNumber + "]"));
                        shortWait();
                        actions.clickAndHold(source.get(getRandomIntBetween(0, source.size()))).moveToElement(target).release().build().perform();
                        shortWait();
                        Alert alert = Browser.getDriver(By.cssSelector("story-preview")).switchTo().alert();
                        System.out.println("Popup says: " + alert.getText());
                        alert.accept();
                        shortWait();
                    } else {
                        oddFound = true;
                        int size = listOfDragableElements.size();
                        int xpathIndex = Math.max(1, size);
                        WebElement source = Browser.getDriver(By.cssSelector("story-preview")).findElement(By.xpath("//div[contains(@id,'libDraggableAd_')][" + xpathIndex + "]"));
                        WebElement target = Browser.getDriver(By.cssSelector("story-preview")).findElement(By.xpath("(//div[contains(@id, 'libPageBarTop_') and contains(@id, '_3')]//div[contains(@id, 'libPageSectionField_') and contains(@id, '_3')])[" + pageNumber + "]"));
                        shortWait();
                        actions.clickAndHold(source).moveToElement(target).release().build().perform();
                        shortWait();
                        int numberOfFramesAfterDragAndDrop = Browser.getDriver(By.cssSelector("story-preview")).findElements(By.xpath("//div[contains(@class,'libThumbnailShape libThumbnailShapeAd libSnapTarget libSnapOverrideOuter ui-droppable')]/img[contains(@src,'/.media')]")).size();
                        System.out.println(numberOfFramesAfterDragAndDrop + " after drop");
                        Assertions.assertTrue(numberOfFramesAfterDragAndDrop > 1);
                    }
                    if (evenFound && oddFound) {
                        System.out.println("Found both even and odd numbers. Breaking loop.");
                        break;
                    }
                }
            }
        }
    }
}
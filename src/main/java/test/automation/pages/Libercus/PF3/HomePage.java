package test.automation.pages.Libercus.PF3;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;
import test.automation.framework.Browser;
import test.automation.framework.Config;
import test.automation.framework.Page;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;
import java.util.List;
import static org.codehaus.groovy.tools.shell.util.Preferences.get;
import static test.automation.framework.Actions.*;
import static test.automation.framework.Actions.shortWait;
import static test.automation.framework.Wait.waitToPerformAction;

public class HomePage extends Page {


//    public static final String URL = "https://pgmuate-edition.libercus.net/pf3/edition/20250428/3";
    public static final String URL = Config.getUrl();

    public static String story;

    @FindBy(xpath = "//div[contains(text(),'Menu')]")
    public static WebElement menuButton;

    @FindBy(xpath = "//span[contains(text(),'TOC')]")
    public static WebElement TOC;

    @FindBy(xpath = "//*[contains(text(),'Sections')]")
    public static WebElement sections;

    @FindBy(xpath = "//*[contains(text(),'Pages')]")
    public static WebElement pages;

    @FindBy(xpath = "//*[contains(text(),'Editions')]")
    public static WebElement editions;

    @FindBy(xpath = "//button[@mattooltip='Search']")
    public static WebElement search;

    @FindBy(xpath = "//div[contains(text(),'Print')]")
    public static WebElement printEditions;


    @FindBy(xpath = "//span[contains(text(),'Settings')]")
    public static WebElement settings;


    @FindBy(xpath = "//*[contains(text(),'Ads/')]")
    public static WebElement Ads;


    @FindBy(xpath = "//span[text()='Print Ads/Puzzles']")
    public static WebElement printAdsOrPuzzlesButton;

    @FindBy(xpath = "//span[text()='Interactive Ads']")
    public static WebElement interactiveAdsButton;

    @FindBy(xpath = "//span[text()='Interactive Puzzles']")
    public static WebElement interactivePuzzlesButton;

    @FindBy(xpath = "//span[text()='Inserts']")
    public static WebElement insertsButton;

    @FindBy(xpath = "//button[contains(@mattooltip,'Previous Section')]")
    public static WebElement homeButton;


    @FindBy(xpath = "//button[@mattooltip ='Next Section']")
    public static List<WebElement> nextArrowForSections;

    @FindBy(xpath = "//button[@mattooltip ='Next Page']")
    public static List<WebElement> nextArrowForPage;

    @FindBy(xpath = "//button[@mattooltip ='Previous Section']")
    public static List<WebElement> previousArrowForSections;

    @FindBy(xpath = "//button[@mattooltip ='Previous Page']")
    public static List<WebElement> previousArrowForPage;

    @FindBy(xpath = "//span[text()='Display']")
    public static WebElement display;

    @FindBy(xpath = "//*[contains(text(),'Help')]")
    public static WebElement help;

    @FindBy(xpath = "//*[contains(text(),'Home')]")
    public static WebElement home;

    @FindBy(xpath = "//*[contains(text(),'Double page')]")
    public static WebElement doublePage;

    @FindBy(xpath = "//*[contains(text(),'Single page')]")
    public static WebElement singlePage;


    public static void clickOnTheStory() throws Exception {
        shortWait();
        WebElement shadowHost = Browser.getDriver(By.cssSelector("story-preview")).findElement(By.cssSelector("edition-page:not([style*='display: none'])"));
        JavascriptExecutor js = (JavascriptExecutor) Browser.getDriver(By.cssSelector("story-preview"));
        List<WebElement> pageNumberElement = (List<WebElement>) js.executeScript(
                "return arguments[0].shadowRoot.querySelectorAll(arguments[1])", shadowHost, "div[rel='Title']");
        shortWait();
        shortWait();
        story = pageNumberElement.get(3).getText();
        jsClick(pageNumberElement.get(3));
        shortWait();
    }

    public static void validateArticleView() throws InterruptedException {
        shortWait();
        WebElement articleView = Browser.getDriver(By.cssSelector("story-preview")).findElement(By.cssSelector(".story_preview_content"));
        Assertions.assertTrue(articleView.isDisplayed());
        shortWait();
        JavascriptExecutor js = (JavascriptExecutor) Browser.getDriver(By.cssSelector("story-preview"));
        shortWait();
        shortWait();
//       WebElement storyPreviewHost= waitUntilElementPresent((WebElement) Browser.getDriver(By.cssSelector("story-preview")));
        String storyText = (String) js.executeScript(
                "return document.querySelector('body > app-root > mat-drawer-container > mat-drawer-content > div > div > edition > story-preview > div > div.story_preview_content > div').shadowRoot.querySelector('h1').innerText;");
        shortWait();
        shortWait();
        shortWait();
        System.out.println(storyText.trim());
        System.out.println(story.trim().replaceAll("\\s+", " "));
        Assertions.assertTrue(storyText.trim().equals(story.trim().replaceAll("\\s+", " ")));

    }


    public static void checkForwardNavigation() throws Exception {
        waitToPerformAction();
        WebDriverWait wait = new WebDriverWait(Browser.getDriver(By.cssSelector("story-preview")), Duration.ofSeconds(10));
        nextArrowForSections.get(0);
        while (nextArrowForSections.size() > 0) {
            try {
                wait.until(ExpectedConditions.elementToBeClickable(nextArrowForSections.get(0)));
                click(nextArrowForSections.get(0));
                waitToPerformAction();
            } catch (Exception e) {
                System.out.println("Element is NOT clickable");
                break;
            }
        }
        waitToPerformAction();
    }

    public static void checkPreviousNavigation() throws Exception {
        waitToPerformAction();
        waitToPerformAction();
        checkForwardNavigation();
        WebDriverWait wait = new WebDriverWait(Browser.getDriver(By.cssSelector("story-preview")), Duration.ofSeconds(10));
        previousArrowForSections.get(0);
        while (previousArrowForSections.size() > 0) {
            try {
                wait.until(ExpectedConditions.elementToBeClickable(previousArrowForSections.get(0)));
                click(previousArrowForSections.get(0));
                waitToPerformAction();
            } catch (Exception e) {
                System.out.println("Element is NOT clickable");
                break;
            }
        }
        waitToPerformAction();
    }


    public static void checkPageForwardNavigation() throws Exception {
        waitToPerformAction();
        int previuosPageNumber = 0;
        String previuosSectionValue = "A";
        while (nextArrowForPage.size() > 0) {
            WebElement shadowHost = Browser.getDriver(By.cssSelector("story-preview")).findElement(By.cssSelector("edition-page:not([style*='display: none'])"));
            JavascriptExecutor js = (JavascriptExecutor) Browser.getDriver(By.cssSelector("story-preview"));
            WebElement pageNumberElement = (WebElement) js.executeScript("return arguments[0].shadowRoot.querySelector('.libPageBodyLinebreak')", shadowHost);
            waitToPerformAction();
            if (pageNumberElement != null && pageNumberElement.getText().contains("-")) {
                if (pageNumberElement.getText().length() <= 4) {
                    String numberPartStr = pageNumberElement.getText().replaceAll("\\D", "");
                    String currentSectionValue = pageNumberElement.getText().replaceAll("[^A-Za-z]", "");
                    int currentPageNumber = Integer.parseInt(numberPartStr);
                    boolean flag = (currentPageNumber > previuosPageNumber) || (!currentSectionValue.equals(previuosSectionValue));
                    Assertions.assertTrue(flag, "Page not changed");
                }
            }
            click(nextArrowForPage.get(0));
            waitToPerformAction();
            nextArrowForPage = Browser.getDriver(By.cssSelector("story-preview")).findElements(By.xpath("//button[@mattooltip ='Next Page']"));
        }
    }

    public static void checkPagePreviousNavigation() throws Exception {
        waitToPerformAction();
        int previuosPageNumber = 0;
        String previuosSectionValue = "";
        while (previousArrowForPage.size() > 0) {
            WebElement shadowHost = Browser.getDriver(By.cssSelector("story-preview")).findElement(By.cssSelector("edition-page:not([style*='display: none'])"));
            JavascriptExecutor js = (JavascriptExecutor) Browser.getDriver(By.cssSelector("story-preview"));
            WebElement pageNumberElement = (WebElement) js.executeScript("return arguments[0].shadowRoot.querySelector('.libPageBodyLinebreak')", shadowHost);
            waitToPerformAction();
            if (pageNumberElement != null && pageNumberElement.getText().contains("-")) {
                if (pageNumberElement.getText().length() <= 4) {
                    String numberPartStr = pageNumberElement.getText().replaceAll("\\D", "");
                    String currentSectionValue = pageNumberElement.getText().replaceAll("[^A-Za-z]", "");
                    int currentPageNumber = Integer.parseInt(numberPartStr);
                    boolean flag = (currentPageNumber < previuosPageNumber) || (!currentSectionValue.equals(previuosSectionValue));
                }
            }
            click(previousArrowForPage.get(0));
            waitToPerformAction();
            previousArrowForPage = Browser.getDriver(By.cssSelector("story-preview")).findElements(By.xpath("//button[@mattooltip ='Previous Page']"));
        }
    }


    public static void selectMenuOnHomePage(String menu) throws InterruptedException {
        shortWait();
        waitUntilElementPresent(Browser.getDriver(By.cssSelector("story-preview")).findElement(By.xpath("//span[text()='" + menu + "']")));
        shortWait();
        Browser.getDriver(By.cssSelector("story-preview")).findElement(By.xpath("//*[text()='" + menu + "']")).click();
        shortWait();
    }

    public static void clickOnAdImage() throws InterruptedException {
        WebElement shadowHost = Browser.getDriver(By.cssSelector("story-preview")).findElement(By.cssSelector("edition-page:not([style*='display: none'])"));
        JavascriptExecutor js = (JavascriptExecutor) Browser.getDriver(By.cssSelector("story-preview"));
        List<WebElement> pageNumberElement = (List<WebElement>) js.executeScript(
                "return arguments[0].shadowRoot.querySelectorAll(arguments[1])", shadowHost, "img[data-img*='/image/static']");
        shortWait();
        shortWait();
        jsClick(pageNumberElement.get(pageNumberElement.size() - 1));
        shortWait();
    }
    public static void validateActionsOnAds() throws InterruptedException {
        shortWait();
        switchToWindow(1);
    }
}

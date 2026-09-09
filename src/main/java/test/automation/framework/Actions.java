package test.automation.framework;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.logging.Level;
import java.util.stream.Collectors;
import static test.automation.framework.Elements.getPanel;
import static test.automation.framework.Elements.getWebElement;
import static test.automation.framework.Page.getCurrentPage;
import static test.automation.framework.Page.getCurrentPageName;
import static test.automation.framework.Runner.log;

public final class Actions {

    static int waitSeconds = 90;

    public static synchronized Object execJavascript(String script, Object... args) {
        JavascriptExecutor scriptExe = ((JavascriptExecutor) Browser.getDriver(By.cssSelector("story-preview")));
        return scriptExe.executeScript(script, args);
    }

    public static synchronized Object tryJavascript(String script, Object... args) {
        try {
            return execJavascript(script, args);
        } catch (Exception ignore) {
            return "";
        }
    }

    public static synchronized Object tryJavascript(String script) {
        try {
            return execJavascript(script);
        } catch (Exception ignore) {
            return "";
        }
    }

    public static void doubleClick(WebElement element) {
        org.openqa.selenium.interactions.Actions actions = new org.openqa.selenium.interactions.Actions(Browser.getDriver(By.cssSelector("story-preview")));
        actions.doubleClick(element).build().perform();
    }

    public static boolean isPageLoaded() {
        String state = (String) tryJavascript("return document.readyState;");
        return state.matches("complete|loaded|interactive");
    }

    public static boolean isJQueryDone() {
        Object jsResponse = tryJavascript("return jQuery.active;");
        if (jsResponse instanceof Long) {
            return ((Long) jsResponse) == 0;
        } else if (jsResponse instanceof String response) {
            return (response.startsWith("{\"hCode\"") || response.isEmpty());
        } else {
            return true;
        }
    }

    public static boolean isAngularDone() {
        Object jsResponse = tryJavascript("return window.getAllAngularTestabilities().filter(x=>!x.isStable()).length;");
        if (jsResponse instanceof Long) {
            return ((Long) jsResponse) == 0;
        } else if (jsResponse instanceof String response) {
            return response.isEmpty();
        } else {
            return true;
        }
    }

    public static void waitUntil(BooleanSupplier condition, int seconds) {
        new WebDriverWait(Browser.getDriver(By.cssSelector("story-preview")), Duration.ofSeconds(seconds)).until((WebDriver driver) -> condition.getAsBoolean());
    }

    public static void waitUntil(BooleanSupplier condition) {
        waitUntil(condition, waitSeconds);
    }

    public static void waitUntil(ExpectedCondition<WebElement> condition, int seconds) {
        new WebDriverWait(Browser.getDriver(By.cssSelector("story-preview")), Duration.ofSeconds(seconds)).until(condition);
    }

    public static void waitUntil(ExpectedCondition<WebElement> condition) {
        waitUntil(condition, waitSeconds);
    }

    public static WebElement waitUntilElementPresent(WebElement element, int seconds) {
        waitUntil(element::isDisplayed, seconds);
        return element;
    }

    public static void waitUntilAllElementsPresent(List<WebElement> element) {
        element.stream().parallel().forEach(Actions::waitUntilElementPresent);
    }

    public static WebElement waitUntilElementPresent(WebElement element) {
        waitUntilElementPresent(element, waitSeconds);
        return element;
    }

    public static void waitUntilElementNotPresent(WebElement element, int seconds) {
        waitUntil(() -> !(element.isDisplayed()), seconds);
    }

    public static void waitUntilElementNotPresent(WebElement element) {
        waitUntilElementNotPresent(element, waitSeconds);
    }

    public static void waitForJQuery(int seconds) {
        waitUntil(Actions::isJQueryDone, seconds);
    }

    public static void waitForJQuery() {
        waitForJQuery(waitSeconds);
    }

    public static void waitForAngular(int seconds) {
        waitUntil(Actions::isAngularDone, seconds);
    }

    public static void waitForAngular() {
        waitForAngular(waitSeconds);
    }

    public static void waitForLoadingMask(WebElement loadingMask, int seconds) {
        try {
            waitUntilElementPresent(loadingMask);
        } catch (TimeoutException ignore) {
        }
        waitUntilElementNotPresent(loadingMask, seconds);
    }

    public static void waitForLoadingMask(WebElement loadingMask) {
        waitForLoadingMask(loadingMask, waitSeconds);
    }

    public static void click(String element) {
        getWebElement(element).click();
    }

    public static void click(WebElement element) throws Exception {
        waitUntil(ExpectedConditions.visibilityOf(element), 120);
        element.click();
    }

    public static void jsClick(String element)
    {

        execJavascript("arguments[0].click();", getWebElement(element));
    }

    public static void jsClick(WebElement element) {
        execJavascript("arguments[0].click();", element);
    }

    public static boolean isSelected(WebElement element) {
        return element.isSelected();
    }

    public static void selectRadio(String element) {
        if (isSelected(element))
            getWebElement(element).click();
    }

    public static void selectRadio(WebElement element) {
        if (isSelected(element))
            element.click();
    }

    public static void selectCheckbox(String element) {
        if (isSelected(element))
            getWebElement(element).click();
    }

    public static void selectCheckbox(WebElement element) {
        if (isSelected(element))
            element.click();
    }

    public static boolean isEnabled(String element) {
        return getWebElement(element).isEnabled();
    }

    public static boolean isSelected(String element) {
        return getWebElement(element).isSelected();
    }

    public static void submit(String element) {
        getWebElement(element).submit();
    }

    public static void sendKeys(String element, String text) {
        getWebElement(element).sendKeys(text);
    }

    public static void clear(String element) {
        getWebElement(element).clear();
    }

    public static String getText(String element) {
        return getWebElement(element).getText();
    }

    public static String getTagName(String element) {
        return getWebElement(element).getTagName();
    }

    public static String getAttribute(String element, String attributeName) {
        return getWebElement(element).getDomAttribute(attributeName);
    }

    public static String getCssValue(String element, String propertyName) {
        return getWebElement(element).getCssValue(propertyName);
    }

    public static void click(String element, String panel) {
        getWebElement(element, panel).click();
    }

    public static void jsClick(String element, String panel) {
        execJavascript("arguments[0].click();", getWebElement(element, panel));
    }

    public static boolean isPanelDisplayed(String panel) {
        WebElement webElement = getPanel(panel);
        return isDisplayed(webElement);
    }

    public static boolean isDisplayed(String element, String panel) {
        WebElement webElement = getWebElement(element, panel);
        return isDisplayed(webElement);
    }

    public static boolean isDisplayed(String element, String panel, int waitSeconds) {
        try {
            waitUntilElementPresent(getWebElement(element, panel), waitSeconds);
            return true;
        } catch (TimeoutException ignore) {
            return false;
        }
    }

    public static boolean isDisplayed(String element) {
        WebElement webElement = getWebElement(element);
        return isDisplayed(webElement);
    }

    public static boolean isDisplayed(String element, int waitSeconds) {
        try {
            waitUntilElementPresent(getWebElement(element), waitSeconds);
            return true;
        } catch (TimeoutException ignore) {
            return false;
        }
    }

    public static boolean isDisplayed(WebElement element) {
        return isDisplayed(element, waitSeconds);
    }

    public static boolean isDisplayed(List<WebElement> element) {
        try {
            waitUntilAllElementsPresent(element);
            return true;
        } catch (TimeoutException ignore) {
            return false;
        }
    }

    public static boolean isDisplayed(WebElement element, int waitSeconds) {
        try {
            waitUntilElementPresent(element, waitSeconds);
            return true;
        } catch (TimeoutException ignore) {
            return false;
        }
    }

    public static boolean isEnabled(String element, String panel) {
        return getWebElement(element, panel).isEnabled();
    }

    public static boolean isSelected(String element, String panel) {
        return getWebElement(element, panel).isSelected();
    }

    public static void submit(String element, String panel) {
        getWebElement(element, panel).submit();
    }

    public static void sendKeys(String element, String text, String panel) {
        getWebElement(element, panel).sendKeys(text);
    }

    public static void clear(String element, String panel) {
        getWebElement(element, panel).clear();
    }

    public static String getText(String element, String panel) {
        return getWebElement(element, panel).getText();
    }

    public static String getTagName(String element, String panel) {
        return getWebElement(element, panel).getTagName();
    }

    public static String getAttribute(String element, String attributeName, String panel) {
        return getWebElement(element, panel).getDomAttribute(attributeName);
    }

    public static String getCssValue(String element, String propertyName, String panel) {
        return getWebElement(element, panel).getCssValue(propertyName);
    }

    public static Object performAction(String method, Object... args) {
        if (getCurrentPage() == null) {
            throw new RuntimeException("Not on valid page!");
        }
        List<? extends Class<?>> argsClass = Arrays.stream(args).map(Object::getClass).toList();
        String methodSignature = method + argsClass.stream().map(Class::getSimpleName).collect(Collectors.toList()).toString().replace("[", "(").replace("]", ")");
        String pageClass = getCurrentPageName();
        try {
            return getCurrentPage().getClass().getDeclaredMethod(method, argsClass.toArray(new Class<?>[args.length])).invoke(getCurrentPage(), args);
        } catch (NoSuchMethodException e) {
            log().log(Level.WARNING, e.getMessage());
            throw new RuntimeException(methodSignature + " is not declared in " + pageClass);
        } catch (IllegalArgumentException e) {
            log().log(Level.WARNING, e.getMessage());
            throw new RuntimeException("Invalid arguments for " + methodSignature + " of " + pageClass);
        } catch (IllegalAccessException e) {
            log().log(Level.WARNING, e.getMessage());
            throw new RuntimeException(methodSignature + " is not accessible from " + pageClass);
        } catch (InvocationTargetException e) {
            log().log(Level.WARNING, e.getMessage());
            throw new RuntimeException(e.getCause());
        }
    }

    public static Object performPanelAction(String method, String panel, Object... args) {
        WebElement panelE = getPanel(panel);
        String panelClass = panelE.getClass().getSimpleName();
        List<? extends Class<?>> argsClass = Arrays.stream(args).map(Object::getClass).toList();
        String methodSignature = method + argsClass.stream().map(Class::getSimpleName).toList().toString().replace("[", "(").replace("]", ")");
        try {
            return panelE.getClass().getDeclaredMethod(method, argsClass.toArray(new Class<?>[args.length])).invoke(panelE, args);
        } catch (NoSuchMethodException e) {
            log().log(Level.WARNING, e.getMessage());
            throw new RuntimeException(methodSignature + " is not declared in " + panelClass);
        } catch (IllegalArgumentException e) {
            log().log(Level.WARNING, e.getMessage());
            throw new RuntimeException("Invalid arguments for " + methodSignature + " of " + panelClass);
        } catch (IllegalAccessException e) {
            log().log(Level.WARNING, e.getMessage());
            throw new RuntimeException(methodSignature + " is not accessible from " + panelClass);
        } catch (InvocationTargetException e) {
            log().log(Level.WARNING, e.getMessage());
            throw new RuntimeException(e.getCause());
        }
    }

    public static String getDocumentsPath(String pageFileName) {
        return new File("").getAbsolutePath().replace('/', '\\') +
                "\\src\\main\\resources\\test\\automation\\uploaddocs\\" + pageFileName;
    }

    public static void uploadFile(WebElement uploadButton, String fileName) throws AWTException, InterruptedException {
        uploadButton.click();
        String fileUploadPathType = getDocumentsPath(fileName);
        StringSelection stringSelection = new StringSelection(fileUploadPathType);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
        Robot robot = new Robot();
        robot.delay(250);
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.delay(50);
        robot.keyRelease(KeyEvent.VK_ENTER);
    }

    public static void openNewWindow() {
        tryJavascript("window.open('');");
        log().info("New Window Opened");
    }

    public static void openUrlInNewWindow(String url) {
        try {
            openNewWindow();
            switchToWindow(1);
            Browser.getDriver(By.cssSelector("story-preview")).navigate().to(url);
            log().info("Navigated to: " + url);
        } catch (Exception e) {
            throw new RuntimeException("Unable to open url in new window: " + url);
        }
    }

    public static void switchToWindow(int sessionWindow) {
        ArrayList<String> newTab = new ArrayList<String>(Browser.getDriver(By.cssSelector("story-preview")).getWindowHandles());
        Browser.getDriver(By.cssSelector("story-preview")).switchTo().window(newTab.get(sessionWindow));
        log().info("On " + sessionWindow);
    }

    public static void shortWait() throws InterruptedException {
        Thread.sleep(5000);
    }

    public static void scrollToView(WebElement element) {
        execJavascript("arguments[0].scrollIntoView(true);", element);
    }

    public static String getCurrentDate() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = today.format(formatter);
        return formattedDate;

    }

}
package test.automation.framework;
import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import java.util.logging.Level;
import static test.automation.framework.Actions.waitUntil;
import static test.automation.framework.Config.getUrl;
import static test.automation.framework.Runner.log;
public class Page
{
    private static Page currentPage;
    public static WebDriverWait wait = new WebDriverWait(Browser.getDriver(By.cssSelector("story-preview")), Duration.ofSeconds(150));
    public Page() {
        PageFactory.initElements(Browser.getDriver(By.cssSelector("story-preview")), this);
        currentPage = this;
        log().info("Page Initialized: " + currentPage.getClass().getName());
    }
    public static Page getCurrentPage() {
        return currentPage;
    }
    public static String getCurrentPageName() {
        return currentPage.getClass().getSimpleName();
    }
    public static String getPageClassName(String pageClassName) {
        return Page.class.getName().replace("framework.Page", "pages.Libercus." + pageClassName.replace(" ", "."));
    }
    public static void verifyPage(String pageClassName) {
        pageClassName = getPageClassName(pageClassName);
        try {
            verifyPage(Class.forName(pageClassName));
        }
        catch (ClassNotFoundException e)
        {
            log().log(Level.WARNING, e.getMessage());
            throw new RuntimeException(pageClassName + " not found!");
        }
    }
    public static void verifyPage(Class<?> pageClass) {
        if (!pageClass.getSuperclass().getSimpleName().equals("Page")) {
            throw new RuntimeException("Invalid page " + pageClass.getSimpleName());
        }
        String url = null;
        try {
            url = (String) pageClass.getDeclaredField("URL").get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            log().log(Level.WARNING, "URL not defined for " + pageClass.getSimpleName());
        }
        if (url != null) {
            String finalUrl = url;
            try {
//                waitUntil(() -> Objects.equals(Browser.getDriver().getCurrentUrl().contains(finalUrl));
                waitUntil(() -> Browser.getDriver(By.cssSelector("story-preview")).getCurrentUrl().contains(finalUrl));
            } catch (TimeoutException e) {
                log().log(Level.WARNING, e.getMessage());
                throw new RuntimeException("Not on page " + pageClass.getSimpleName() + " (" + url + ") ("
                        + Browser.getDriver(By.cssSelector("story-preview")).getCurrentUrl() + ")");
            }
        }
        try {
            waitUntil(Actions::isPageLoaded);
        } catch (TimeoutException e) {
            log().log(Level.WARNING, e.getMessage());
            throw new RuntimeException(pageClass.getSimpleName() + "page (" + url + ") is taking too long to load.");
        }
        By verifyBy = null;
        try {
            verifyBy = (By) pageClass.getDeclaredField("VERIFY_BY").get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            log().log(Level.WARNING, "VERIFY_BY not defined for " + pageClass.getSimpleName());
        }
        if (verifyBy != null) {
            try {
                waitUntil(ExpectedConditions.visibilityOfElementLocated(verifyBy));
            } catch (TimeoutException e) {
                log().log(Level.WARNING, e.getMessage());
                throw new RuntimeException(pageClass.getSimpleName() + "page VERIFY_BY (" + verifyBy.toString() + ") is not displayed.");
            }
        }
        log().info("On Page: " + pageClass.getSimpleName());
    }
    public static Page onPage(String pageClassName) {
        pageClassName = getPageClassName(pageClassName);
        try {
            return onPage(Class.forName(pageClassName));
        } catch (ClassNotFoundException e) {
            log().log(Level.WARNING, e.getMessage());
            throw new RuntimeException(pageClassName + " not found!");
        }
    }
    public static Page onPage(Class<?> pageClass) {
        verifyPage(pageClass);
        try {
            pageClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            log().log(Level.WARNING, e.getMessage());
            throw new RuntimeException("Instantiation failed for " + pageClass.getName());
        }
        return getCurrentPage();
    }
    public static Page visit(String pageClassName) {
        pageClassName = getPageClassName(pageClassName);
        try {
            return visit(Class.forName(pageClassName));
        } catch (ClassNotFoundException e) {
            log().log(Level.WARNING, e.getMessage());
            throw new RuntimeException(pageClassName + " not found!");
        }
    }
    public static Page visit(Class<?> pageClass) {
        try {
            String baseUrl = getUrl();
            String pageUrl = (String) pageClass.getDeclaredField("URL").get(null);
            if (pageUrl.startsWith("https")) {
                Browser.getDriver(By.cssSelector("story-preview")).navigate().to(pageUrl);
            } else {
                Browser.getDriver(By.cssSelector("story-preview")).navigate().to(baseUrl + pageUrl);
            }

        } catch (NoSuchFieldException e) {
            log().log(Level.WARNING, e.getMessage());
            throw new RuntimeException("URL not found for " + pageClass.getSimpleName());
        } catch (IllegalAccessException e) {
            log().log(Level.WARNING, e.getMessage());
            throw new RuntimeException(pageClass.getName() + " is not accessible.");
        }

        return onPage(pageClass);
    }
    public static void visitPage(String url) {
        try {
            Browser.getDriver(By.cssSelector("story-preview")).navigate().to(url);
        } catch (Exception e) {
            throw new RuntimeException("Unable to navigate to " + url);
        }
    }
}

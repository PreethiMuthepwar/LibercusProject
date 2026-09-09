package test.automation.framework;

import org.openqa.selenium.WebElement;
import java.util.List;
import java.util.logging.Level;
import static test.automation.framework.Page.getCurrentPage;
import static test.automation.framework.Page.getCurrentPageName;
import static test.automation.framework.Runner.log;

public final class Elements {

    public static WebElement getWebElement(String element) {
        if (getCurrentPage() == null) {
            throw new RuntimeException("Not on valid page!");
        }
        String pageClass = getCurrentPageName();
        try {
            return ((WebElement) getCurrentPage().getClass().getDeclaredField(element).get(getCurrentPage()));
        } catch (NoSuchFieldException e) {
            log().log(Level.WARNING, e.getMessage());
            throw new RuntimeException(element + " is not declared in " + pageClass);
        } catch (IllegalAccessException e) {
            log().log(Level.WARNING, e.getMessage());
            throw new RuntimeException(element + " is not accessible from " + pageClass);
        } catch (ClassCastException e) {
            log().log(Level.WARNING, e.getMessage());
            throw new RuntimeException(element + " is not an element in " + pageClass);
        }
    }

    public static List<WebElement> getWebElements(String element) {
        if (getCurrentPage() == null) {
            throw new RuntimeException("Not on valid page!");
        }
        String pageClass = getCurrentPageName();
        try {
            return ((List<WebElement>) getCurrentPage().getClass().getDeclaredField(element).get(getCurrentPage()));
        } catch (NoSuchFieldException e) {
            log().log(Level.WARNING, e.getMessage());
            throw new RuntimeException(element + " is not declared in " + pageClass);
        } catch (IllegalAccessException e) {
            log().log(Level.WARNING, e.getMessage());
            throw new RuntimeException(element + " is not accessible from "+ pageClass);
        } catch (ClassCastException e) {
            log().log(Level.WARNING, e.getMessage());
            throw new RuntimeException(element + " is not an element list in " + pageClass);
        }
    }

    public static WebElement getPanel(String panel) {
        try {
            return getWebElement(panel);
        } catch (ClassCastException e) {
            log().log(Level.WARNING, e.getMessage());
            throw new RuntimeException(panel + " is not an panel in " + getCurrentPageName());
        }
    }

    public static WebElement getWebElement(String element, String panel) {
        WebElement panelE = getPanel(panel);
        String panelClass = panelE.getClass().getSimpleName();
        try {
            return ((WebElement) panelE.getClass().getDeclaredField(element).get(panelE));
        } catch (NoSuchFieldException e) {
            log().log(Level.WARNING, e.getMessage());
            throw new RuntimeException(element + " is not declared in " + panelClass);
        } catch (IllegalAccessException e) {
            log().log(Level.WARNING, e.getMessage());
            throw new RuntimeException(element + " is not accessible from " + panelClass);
        } catch (ClassCastException e) {
            log().log(Level.WARNING, e.getMessage());
            throw new RuntimeException(element + " is not an element in " + panelClass);
        }
    }

    public static List<WebElement> getWebElements(String element, String panel) {
        WebElement panelE = getPanel(panel);
        String panelClass = panelE.getClass().getSimpleName();
        try {
            return ((List<WebElement>) panelE.getClass().getDeclaredField(element).get(panelE));
        } catch (NoSuchFieldException e) {
            log().log(Level.WARNING, e.getMessage());
            throw new RuntimeException(element + " is not declared in " + panelClass);
        } catch (IllegalAccessException e) {
            log().log(Level.WARNING, e.getMessage());
            throw new RuntimeException(element + " is not accessible from " + panelClass);
        } catch (ClassCastException e) {
            log().log(Level.WARNING, e.getMessage());
            throw new RuntimeException(element + " is not a element list in " + panelClass);
        }
    }

}

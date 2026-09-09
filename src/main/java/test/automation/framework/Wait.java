package test.automation.framework;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;

import java.util.logging.Level;

import static test.automation.framework.Runner.log;

public final class Wait {

    public static boolean untilElementPresent(WebElement element) {
        return secondsUntilElementPresent(element, 480);
    }

    public static boolean secondsUntilElementPresent(WebElement webElement, int seconds) {
        if (webElement == null) {
            return false;
        }
        try {
            Actions.waitUntil(webElement::isDisplayed, seconds);
            return true;
        } catch (TimeoutException e) {
            log().log(Level.WARNING, "Cannot locate an element using " + webElement);
        }
        return false;
    }

    public static boolean UntilElementNotPresent(WebElement webElement) {
        return secondsUntilElementPresent(webElement, 30);
    }

    public static boolean secondsUntilElementNotPresent(WebElement webElement, int seconds) {
        if (webElement == null) {
            return false;
        }
        try {
            Actions.waitUntil(() -> !webElement.isDisplayed(), seconds);
            return true;
        } catch (TimeoutException e) {
            log().log(Level.WARNING, "Cannot locate an element using " + webElement);
        }
        return false;
    }

    public static void waitToPerformAction() throws InterruptedException {
        Thread.sleep(5000);
    }

}

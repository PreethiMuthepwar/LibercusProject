package test.automation.pages.Libercus.PF3;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import test.automation.framework.Browser;
import test.automation.framework.Page;
import static test.automation.pages.Libercus.PF3.HomePage.*;
import static test.automation.framework.Actions.*;
public class SectionsPage extends Page {
    public static int count;
    @FindBy(xpath = "//*[text() ='Sections']")
    public static WebElement sectionsHeader;
    @FindBy(xpath = "//*[@class='popup_toolbar_holder']/div/div/button")
    public static WebElement popUpCloseButton;
    public static void selectSection(String sectionToBeSelected) throws InterruptedException {
        shortWait();
        try {
            click(popUpCloseButton);
            click(sections);
            WebElement webElement = Browser.getDriver(By.cssSelector("story-preview")).findElement(By.xpath("//img[contains(@src,'" + sectionToBeSelected + "')]"));
            doubleClick(webElement);
        } catch (Exception e) {
            System.out.println(sectionToBeSelected + " Page Not found");
        }
    }
}

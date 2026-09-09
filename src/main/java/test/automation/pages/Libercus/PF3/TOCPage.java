package test.automation.pages.Libercus.PF3;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import test.automation.framework.Browser;
import test.automation.framework.Page;
import java.util.List;
import static test.automation.framework.Actions.*;
import static test.automation.pages.Libercus.PF3.HomePage.TOC;
public class TOCPage extends Page {
    @FindBy(xpath = "//*[text()='Table of Contents (TOC)']")
    public static WebElement TOCHeader;
    @FindBy(xpath = "//mat-panel-title/div/h3")
    public static List<WebElement> listOfTOCs;
    @FindBy(xpath = "//mat-expansion-panel[contains(@class,'expanded')]/div/div/mat-accordion/mat-expansion-panel/mat-expansion-panel-header/span/mat-panel-title/div/h4")
    public static List<WebElement> listOfPagesUnderExpandedSection;
    @FindBy(xpath = "//mat-expansion-panel[contains(@class,'expanded ')]/div/div/mat-list/span/span/mat-list-item/div/a")
    public static List<WebElement> storyLinks;
    @FindBy(xpath = "//div[@class='popup_toolbar_holder']/div/button/span/mat-icon[text()='close']")
    public static WebElement TOCCloseIcon;
    public static void navigateThroughSectionsAndValidate() throws Exception {
        shortWait();
        for (WebElement sections : listOfTOCs) {
            String sectionName = sections.getText().trim();
            char sectionSuffix = sectionName.charAt(sectionName.length() - 1);
            scrollToView(sections);
            Assertions.assertTrue(sections.isDisplayed() && sections.isEnabled());
            shortWait();
            jsClick(sections);
            shortWait();
            for (WebElement page : listOfPagesUnderExpandedSection) {
                if (page != null) {
                    Assertions.assertTrue(page.getText().contains(String.valueOf(sectionSuffix)));
                    System.out.println("Section " + sectionSuffix + " Validated");
                }
                shortWait();
            }
        }
    }
    public static void selectSectionOnTOC(String section) throws Exception {
        shortWait();
        try {
            TOCCloseIcon.click();
            click(TOC);
            WebElement ele = Browser.getDriver(By.cssSelector("story-preview")).findElement(By.xpath("//h3[text()='Section: " + section + "']"));
            click(ele);
        } catch (Exception e) {
            System.out.println(section + " not found");
        }
    }
    public static void navigateThroughEachPageUnderSelectedSection() throws Exception {
        shortWait();
        for (WebElement page : listOfPagesUnderExpandedSection) {
            Assertions.assertTrue(page.isDisplayed() && page.isEnabled());
            shortWait();
            scrollToView(page);
            click(page);
            shortWait();
        }
    }
    public static void selectPageOnTOC(String Page) throws Exception {
        shortWait();
        try {

            WebElement ele = Browser.getDriver(By.cssSelector("story-preview")).findElement(By.xpath("//h4[text()='Page: " + Page + "']"));
            click(ele);
        } catch (Exception e) {
            System.out.println(e);
        }
        shortWait();
    }
    public static void selectStoryAndValidate() throws Exception {
        for (WebElement link : storyLinks) {
            shortWait();
            scrollToView(link);
            Assertions.assertTrue(link.isDisplayed() && link.isEnabled());
            click(link);
            shortWait();
        }
    }
}

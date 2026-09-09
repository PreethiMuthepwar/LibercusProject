package test.automation.pages.Libercus.PF3;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import test.automation.framework.Browser;
import java.util.List;
import static test.automation.framework.Actions.*;
import static test.automation.pages.Libercus.PF3.HomePage.selectMenuOnHomePage;
public class Hyperlinks extends Pages
{
    public static void navigatingToHyperlinks() throws Exception
    {
        shortWait();
        WebElement shadowHost = Browser.getDriver(By.cssSelector("story-preview")).findElement(By.cssSelector("edition-page:not([style*='display: none'])"));
        click(Browser.getDriver(By.cssSelector("story-preview")).findElement(By.xpath("//div[@class='app_menu_top_open theme_bg_color']")));
        List<WebElement> links = (List<WebElement>) ((JavascriptExecutor) Browser.getDriver(By.cssSelector("story-preview"))).executeScript("return Array.from(arguments[0].shadowRoot.querySelectorAll('.libPageBodyLinebreak a'));", shadowHost);
        for (WebElement link : links)
        {
            shortWait();
            scrollToView(link);
            link.click();
            JavascriptExecutor js = (JavascriptExecutor) Browser.getDriver(By.cssSelector("story-preview"));
            WebElement pageNumberElement = (WebElement) js.executeScript("return arguments[0].shadowRoot.querySelector('.libPageBodyLinebreak')", shadowHost);
            String pageNumber=pageNumberElement.getText();
            if(!(link.getText().contains("1")))
            {
                Assertions.assertTrue(pageNumber.equalsIgnoreCase(link.getText()));
            }
            selectMenuOnHomePage("Home");
        }
    }
}

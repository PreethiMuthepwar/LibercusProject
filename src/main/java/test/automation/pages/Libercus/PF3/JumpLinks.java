package test.automation.pages.Libercus.PF3;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import test.automation.framework.Browser;
import test.automation.framework.Page;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static test.automation.framework.Actions.scrollToView;
import static test.automation.framework.Actions.shortWait;
import static test.automation.pages.Libercus.PF3.HomePage.selectMenuOnHomePage;
public class JumpLinks extends Page
{
    @FindBy(xpath = "//div[@class='app_menu_top_open theme_bg_color']")
    public static WebElement Menu;
    public static void handleJumplinks() throws InterruptedException {
        shortWait();
        WebElement shadowHost = Browser.getDriver(By.cssSelector("story-preview")).findElement(By.cssSelector("edition-page:not([style*='display: none'])"));
        Browser.getDriver(By.cssSelector("story-preview")).findElement(By.xpath("//div[@class='app_menu_top_open theme_bg_color']")).click();
        List<WebElement> links = (List<WebElement>) ((JavascriptExecutor) Browser.getDriver(By.cssSelector("story-preview"))).executeScript("const elements = Array.from(arguments[0].shadowRoot.querySelectorAll('div'));" + "return elements.filter(el => el.classList.contains('libPageBodyLinebreak') && el.textContent.trim().startsWith('SEE'));", shadowHost);
        shortWait();
        for (WebElement link : links)
        {
            String numberPartStr = link.getText().replaceAll("\\D", "");
            Pattern pattern = Pattern.compile("PAGE\\s+([A])-\\d+");
            Matcher matcher = pattern.matcher(link.getText());
            String letter = matcher.find() ? matcher.group(1) : null;
            int currentPageNumber = Integer.parseInt(numberPartStr);
            scrollToView(link);
            link.click();
            WebElement shadowHost2 = Browser.getDriver(By.cssSelector("story-preview")).findElement(By.cssSelector("edition-page:not([style*='display: none'])"));
            JavascriptExecutor js = (JavascriptExecutor) Browser.getDriver(By.cssSelector("story-preview"));
            WebElement pageNumberElement = (WebElement) js.executeScript("return arguments[0].shadowRoot.querySelector('.libPageBodyLinebreak')", shadowHost2);
            System.out.println(pageNumberElement.getText());
            String numberPartStr2 = pageNumberElement.getText().replaceAll("\\D", "");
            String currentSectionValue = pageNumberElement.getText().replaceAll("[^A-Za-z]", "");
            int currentPageNumber2 = Integer.parseInt(numberPartStr2);
            System.out.println(currentPageNumber2+","+currentSectionValue);
            Assertions.assertTrue(letter.equalsIgnoreCase(currentSectionValue) && currentPageNumber==currentPageNumber2);
            selectMenuOnHomePage("Home");
        }
    }
}

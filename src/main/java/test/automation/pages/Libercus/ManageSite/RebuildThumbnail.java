package test.automation.pages.Libercus.ManageSite;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import test.automation.framework.Page;

public class RebuildThumbnail extends Page {
    @FindBy(xpath = "//input[@id='pageid']")
    public static WebElement pageIDInput;

    @FindBy(xpath = "//button[text()='Submit']")
    public static WebElement submitButton;
}

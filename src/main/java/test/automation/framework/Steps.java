package test.automation.framework;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Random;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static test.automation.framework.Actions.*;
import static test.automation.framework.Config.getCustomerEnv;
import static test.automation.framework.Page.*;
import static test.automation.framework.Elements.*;
import static test.automation.framework.Rest.response;
import static test.automation.framework.Rest.validatableResponse;
import static test.automation.framework.Runner.log;
//import static test.automation.pages.Libercus.CMS.StoryCreation.text;

public final class Steps {

    @Given("^I visit \"([^\"]*)\" page$")
    public void iVisitPage(String page) throws Throwable {
        visit(page);
    }

    @Then("^I should be on \"([^\"]*)\" page$")
    public void iShouldBeOnPage(String page) throws Throwable
    {
        shortWait();
        onPage(page);
    }

    @Then("^I should see \"([^\"]*)\" (element|button|link|image|text box|dropdown|check box|radio button|form|text block|table)$")
    public void iShouldSeeElement(String element, String type) throws Throwable {
        if (element.contains("TOC") || element.contains("sections")) {
            JavascriptExecutor js = (JavascriptExecutor) Browser.getDriver(By.cssSelector("story-preview"));
            js.executeScript("document.body.style.zoom='70%';");
        }
        shortWait();
        Assertions.assertTrue(isDisplayed(element), element + " " + type + " not displayed on " + getCurrentPageName());
    }

    @Then("^I should see \"([^\"]*)\" (element|button|link|image|text box|dropdown|check box|radio button|form|text block|table) on \"([^\"]*)\" page$")
    public void iShouldSeeElementOnPage(String element, String type, String page) throws Throwable {
        iShouldBeOnPage(page);
        iShouldSeeElement(element, type);
    }

    @Then("^I should see following (elements|buttons|links|images|text blocks):$")
    public void iShouldSeeFollowingElements(String type, List<String> elements) throws Throwable {
        List<String> notDisplayed = elements.stream().filter(e -> !isDisplayed(e)).toList();
        Assertions.assertEquals(0, notDisplayed.size(), type + " not displayed on " + getCurrentPageName() + ": " + notDisplayed);
    }


    @Then("^I click on \"([^\"]*)\" element on \"([^\"]*)\" page$")
    public void iClickOnElementOnPage(String element, String page) throws Throwable {
        iShouldBeOnPage(page);
        waitUntilElementPresent(getWebElement(element));
        iClickOnElement(element);
        log().info("Clicked on " + element + "successfully on " + page + "page");
    }

    @Then("^I click on \"([^\"]*)\" element$")
    public void iClickOnElement(String element) throws Throwable
    {
        jsClick(element);
        log().info("Clicked on " + element + "successfully");
    }
    @Then("^I should see \"([^\"]*)\" panel$")
    public void iShouldSeePanel(String panel) throws Throwable {
        Assertions.assertTrue(isPanelDisplayed(panel), panel + " not displayed on " + getCurrentPageName());
    }

    @Then("^I should see following panels:$")
    public void iShouldSeeFollowingPanels(List<String> panels) throws Throwable {
        List<String> notDisplayed = panels.stream().filter(p -> !isPanelDisplayed(p)).toList();
        Assertions.assertEquals(0, notDisplayed.size(), "Panels not displayed on " + getCurrentPageName() + ": " + notDisplayed);
    }

    @Then("^I should see \"([^\"]*)\" panel on \"([^\"]*)\" page$")
    public void iShouldSeePanelOnPage(String panel, String page) throws Throwable {
        iShouldBeOnPage(page);
        iShouldSeePanel(panel);
    }

    @Then("^I should see following panels on \"([^\"]*)\" page:$")
    public void iShouldSeeFollowingPanelsOnPage(String page, List<String> panels) throws Throwable {
        iShouldBeOnPage(page);
        iShouldSeeFollowingPanels(panels);
    }

    @Then("^I should see \"([^\"]*)\" (element|button|link|image|text box|dropdown|check box|radio button|form|text block|table) on \"([^\"]*)\" panel$")
    public void iShouldSeeElementOnPanel(String element, String type, String panel) throws Throwable {
        Assertions.assertTrue(isDisplayed(element, panel), element + " " + type + " not displayed on " + getCurrentPageName());
    }

    @Then("^I should see \"([^\"]*)\" (element|button|link|image|text box|dropdown|check box|radio button|form|text block|table) on \"([^\"]*)\" panel on \"([^\"]*)\" page$")
    public void iShouldSeeElementOnPanelOnPage(String element, String type, String panel, String page) throws Throwable {
        iShouldBeOnPage(page);
        iShouldSeeElementOnPanel(element, type, panel);
    }

    @Then("^I should see following (elements|buttons|links|images|text blocks) on \"([^\"]*)\" panel:$")
    public void iShouldSeeFollowingElementsOnPanel(String type, String panel, List<String> elements) throws Throwable {
        List<String> notDisplayed = elements.stream().filter(e -> !isDisplayed(e, panel)).toList();
        Assertions.assertEquals(0, notDisplayed.size(), type + " not displayed on " + getCurrentPageName() + ": " + elements);
    }

    @Then("^I should see following (elements|buttons|links|images|text blocks) on \"([^\"]*)\" panel on \"([^\"]*)\" page:$")
    public void iShouldSeeFollowingElementsOnPanelOnPage(String type, String panel, String page, List<String> elements) throws Throwable {
        iShouldBeOnPage(page);
        iShouldSeeFollowingElementsOnPanel(type, panel, elements);
    }

    @Then("^I click on \"([^\"]*)\" (element|button|link|image|text box|dropdown|check box|radio button|form|text block|table) on \"([^\"]*)\" panel$")
    public void iClickOnElementOnPanel(String element, String type, String panel) throws Throwable {
        click(element, panel);
    }

    @Then("^I click on \"([^\"]*)\" (element|button|link|image|text box|dropdown|check box|radio button|form|text block|table) on \"([^\"]*)\" panel on \"([^\"]*)\" page$")
    public void iClickOnElementOnPanelOnPage(String element, String type, String panel, String page) throws Throwable {
        iShouldBeOnPage(page);
        iClickOnElementOnPanel(element, type, panel);
    }

    @When("^I \"([^\"]*)\" on current page$")
    public void iInvokeMethodOnCurrentPage(String method) throws Throwable {
        performAction(method);
    }

    @When("^I \"([^\"]*)\" on \"([^\"]*)\" page$")
    public void iInvokeMethodOnPage(String method, String page) throws Throwable {
        iShouldBeOnPage(page);
        iInvokeMethodOnCurrentPage(method);
    }

    @When("^I \"([^\"]*)\" on \"([^\"]*)\" panel$")
    public void iInvokeMethodOnPanel(String method, String panel) throws Throwable {
        performPanelAction(method, panel);
    }

    @When("^I \"([^\"]*)\" on \"([^\"]*)\" panel on \"([^\"]*)\" page$")
    public void iInvokeMethodOnPanelOnPage(String method, String panel, String page) throws Throwable {
        iShouldBeOnPage(page);
        iInvokeMethodOnPanel(method, panel);
    }

    @When("^I \"([^\"]*)\" with \"([^\"]*)\" on current page$")
    public void iInvokeMethodWithArgOnCurrentPage(String method, String args) throws Throwable {
        performAction(method, (Object[]) args.split(", "));
    }

    @When("^I \"([^\"]*)\" with \"([^\"]*)\" on \"([^\"]*)\" page$")
    public void iInvokeMethodWithArgOnPage(String method, String args, String page) throws Throwable {
        iShouldBeOnPage(page);
        iInvokeMethodWithArgOnCurrentPage(method, args);
    }

    @When("^I \"([^\"]*)\" with \"([^\"]*)\" on \"([^\"]*)\" panel$")
    public void iInvokeMethodWithArgOnPanel(String method, String args, String panel) throws Throwable {
        performPanelAction(method, panel, (Object[]) args.split(", "));
    }

    @When("^I \"([^\"]*)\" with \"([^\"]*)\" on \"([^\"]*)\" panel on \"([^\"]*)\" page$")
    public void iInvokeMethodWithArgOnPanelOnPage(String method, String args, String panel, String page) throws Throwable {
        iShouldBeOnPage(page);
        iInvokeMethodWithArgOnPanel(method, panel, args);
    }

    @When("^I select random value in \"([^\"]*)\" dropdown$")
    public void iSelectRandomValueInDropdown(String element) throws Throwable {
        Select select = new Select(getWebElement(element));
        Assertions.assertTrue(isDisplayed(element), element + " not displayed on " + getCurrentPageName());
        int optionsSize = select.getOptions().size();
        Assertions.assertTrue(optionsSize > 0, element + " is empty");
        select.selectByIndex(new Random().nextInt(optionsSize));
    }

    @When("^I select random value in \"([^\"]*)\" dropdown on \"([^\"]*)\" page$")
    public void iSelectRandomValueInDropdownOnPage(String element, String page) throws Throwable {
        iShouldBeOnPage(page);
        iSelectRandomValueInDropdown(element);
    }

    @When("^I select random value in \"([^\"]*)\" dropdown on \"([^\"]*)\" panel$")
    public void iSelectRandomValueInDropdownOnPanel(String element, String panel) throws Throwable {
        Select select = new Select(getWebElement(element, panel));
        Assertions.assertTrue(isDisplayed(element, panel), element + " not displayed on " + panel + " panel");
        int optionsSize = select.getOptions().size();
        Assertions.assertTrue(optionsSize > 0, element + " is empty");
        select.selectByIndex(new Random().nextInt(optionsSize));
    }

    @When("^I select random value in \"([^\"]*)\" dropdown on \"([^\"]*)\" panel on \"([^\"]*)\" page$")
    public void iSelectRandomValueInDropdownOnPanelOnPage(String element, String panel, String page) throws Throwable {
        iShouldBeOnPage(page);
        iSelectRandomValueInDropdownOnPanel(element, panel);
    }

    @When("^I select \"([^\"]*)\" value in \"([^\"]*)\" dropdown$")
    public void iSelectValueInDropdown(String value, String element) throws Throwable {
        Select select = new Select(getWebElement(element));
        Assertions.assertTrue(isDisplayed(getWebElement(element)), element + " not displayed on " + getCurrentPageName());
        select.selectByValue(value);
    }

    @When("^I select \"([^\"]*)\" value in \"([^\"]*)\" dropdown on \"([^\"]*)\" page$")
    public void iSelectValueInDropdownOnPage(String value, String element, String page) throws Throwable {
        iShouldBeOnPage(page);
        iSelectValueInDropdown(value, element);
    }

    @When("^I select \"([^\"]*)\" value in \"([^\"]*)\" dropdown on \"([^\"]*)\" panel$")
    public void iSelectValueInDropdownOnPanel(String value, String element, String panel) throws Throwable {
        Select select = new Select(getWebElement(element, panel));
        Assertions.assertTrue(isDisplayed(element, panel), element + " not displayed on " + panel + " panel");
        select.selectByValue(value);
    }

    @When("^I select \"([^\"]*)\" value in \"([^\"]*)\" dropdown on \"([^\"]*)\" panel on \"([^\"]*)\" page$")
    public void iSelectValueInDropdownOnPanelOnPage(String value, String element, String panel, String page) throws Throwable {
        iShouldBeOnPage(page);
        iSelectValueInDropdownOnPanel(value, element, panel);
    }

    @When("^I select \"([^\"]*)\" text in \"([^\"]*)\" dropdown$")
    public void iSelectTextInDropdown(String text, String element) throws Throwable {
        Select select = new Select(getWebElement(element));
        Assertions.assertTrue(isDisplayed(getWebElement(element)), element + " not displayed on " + getCurrentPageName());
        select.selectByVisibleText(text);
    }

    @When("^I select \"([^\"]*)\" text in \"([^\"]*)\" dropdown on \"([^\"]*)\" page$")
    public void iSelectTextInDropdownOnPage(String text, String element, String page) throws Throwable {
        iShouldBeOnPage(page);
        iSelectTextInDropdown(text, element);
    }
    @And("I select template in {string} dropdown on {string} page")
    public void iSelectTemplateInDropdownOnPage(String element, String page) throws Throwable {
        iShouldBeOnPage(page);
        String currentProject = getCustomerEnv();
        String text;
        if (currentProject.equals("PG")) {
            text = "6 col Inside Left NEW FONTS";
        } else {
            text = "6 col Inside Left NEW FONTS   ";
        }
        iSelectTextInDropdown(text, element);

    }
    @And("I select template text in {string} dropdown on {string} page")
    public void iSelectTemplateTextInDropdownOnPage(String element, String page) throws Throwable {
        iShouldBeOnPage(page);
        String currentProject = getCustomerEnv();
        String text;
        if (currentProject.equals("PG")) {
            text = "6 col Inside Left NEW FONTS";
        } else {
            text = "6 col Inside Left - CLE";
        }
        iSelectTextInDropdown(text, element);
    }
    @And("I select text in {string} dropdown on {string} page")
    public void iSelectTextInDropdownOnPage(String element, String page) throws Throwable
    {
        iShouldBeOnPage(page);
        String currentProject = getCustomerEnv();
        String text;
        if (currentProject.equals("PG")) {
            text = "Done";
        } else {
            text = "Published   ";
        }
        iSelectTextInDropdown(text, element);
    }
    @Then("I select the customer-specific channel in {string} dropdown")
    public void iSelectTheCustomerSpecificChannelInDropdown(String element) throws Throwable {
        String currentProject = getCustomerEnv();
        String channelValue;
        if (currentProject.equals("PG")) {
            channelValue = "PG Print";
        } else {
            channelValue = "Print";
        }
        iSelectTextInDropdown(channelValue, element);
    }

    @When("^I select \"([^\"]*)\" text in \"([^\"]*)\" dropdown on \"([^\"]*)\" panel$")
    public void iSelectTextInDropdownOnPanel(String text, String element, String panel) throws Throwable {
        Select select = new Select(getWebElement(element, panel));
        Assertions.assertTrue(isDisplayed(element, panel), element + " not displayed on " + panel + " panel");
        select.selectByVisibleText(text);
    }

    @When("^I select \"([^\"]*)\" text in \"([^\"]*)\" dropdown on \"([^\"]*)\" panel on \"([^\"]*)\" page$")
    public void iSelectTextInDropdownOnPanelOnPage(String text, String element, String panel, String page) throws Throwable {
        iShouldBeOnPage(page);
        iSelectTextInDropdownOnPanel(text, element, panel);
    }

    @When("^I select random \"([^\"]*)\" radio button$")
    public void iSelectRandomRadioButton(String element) throws Throwable {
        List<WebElement> radio = getWebElements(element);
        Assertions.assertTrue(isDisplayed(element), element + " not displayed on " + getCurrentPageName());
        int optionsSize = radio.size();
        Assertions.assertTrue(optionsSize > 0, element + " is empty");
        radio.get(new Random().nextInt(optionsSize)).click();
    }

    @When("^I select random \"([^\"]*)\" radio button on \"([^\"]*)\" page$")
    public void iSelectRandomRadioButtonOnPage(String element, String page) throws Throwable {
        iShouldBeOnPage(page);
        iSelectRandomRadioButton(element);
    }

    @When("^I select random \"([^\"]*)\" radio button on \"([^\"]*)\" panel$")
    public void iSelectRandomRadioButtonOnPanel(String element, String panel) throws Throwable {
        List<WebElement> radio = getWebElements(element, panel);
        Assertions.assertTrue(isDisplayed(radio), element + " not displayed on " + panel + " panel");
        int optionsSize = radio.size();
        Assertions.assertTrue(optionsSize > 0, element + " is empty");
        radio.get(new Random().nextInt(optionsSize)).click();
    }

    @When("^I select random \"([^\"]*)\" radio button on \"([^\"]*)\" panel \"([^\"]*)\" on page$")
    public void iSelectRandomRadioButtonOnPanelOnPage(String element, String panel, String page) throws Throwable {
        iShouldBeOnPage(page);
        iSelectRandomRadioButtonOnPanel(element, panel);
    }

    @When("^I select \"([^\"]*)\" in \"([^\"]*)\" radio buttons$")
    public void iSelectInRadioButtons(String value, String element) throws Throwable {
        List<WebElement> radio = getWebElements(element);
        Assertions.assertTrue(isDisplayed(element), element + " not displayed on " + getCurrentPageName());
        WebElement webElement = radio.stream()
                .filter(b -> value.equals(b.getDomAttribute("value")))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(
                        String.format("Cannot locate radio button with value: %s", value)));
        selectRadio(webElement);
    }

    @When("^I select \"([^\"]*)\" in \"([^\"]*)\" radio buttons on \"([^\"]*)\" page$")
    public void iSelectInRadioButtonsOnPage(String value, String element, String page) throws Throwable {
        iShouldBeOnPage(page);
        iSelectInRadioButtons(value, element);
    }

    @When("^I select \"([^\"]*)\" in \"([^\"]*)\" radio buttons on \"([^\"]*)\" panel$")
    public void iSelectInRadioButtonsOnPanel(String value, String element, String panel) throws Throwable {
        List<WebElement> radio = getWebElements(element, panel);
        Assertions.assertTrue(isDisplayed(radio), element + " not displayed on " + panel + " panel");
        WebElement webElement = radio.stream()
                .filter(b -> value.equals(b.getDomAttribute("value")))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(
                        String.format("Cannot locate radio button with value: %s", value)));
        selectRadio(webElement);
    }

    @When("^I select \"([^\"]*)\" in \"([^\"]*)\" radio buttons on \"([^\"]*)\" panel on \"([^\"]*)\" page$")
    public void iSelectInRadioButtonsOnPanel(String value, String element, String panel, String page) throws Throwable {
        iShouldBeOnPage(page);
        iSelectInRadioButtonsOnPanel(value, element, panel);
    }

    @When("^I select \"([^\"]*)\" check box$")
    public void iSelectCheckBox(String element) throws Throwable
    {
        shortWait();
        WebElement checkBox = getWebElement(element);
        Assertions.assertTrue(isDisplayed(checkBox), element + " check box is not displayed on " + getCurrentPageName());
        jsClick(checkBox);
    }

    @When("^I select \"([^\"]*)\" check box on \"([^\"]*)\" page$")
    public void iSelectCheckBoxOnPage(String element, String page) throws Throwable {
        iShouldBeOnPage(page);
        iSelectCheckBox(element);
    }

    @When("^I select \"([^\"]*)\" check box on \"([^\"]*)\" panel$")
    public void iSelectCheckBoxOnPanel(String element, String panel) throws Throwable {
        WebElement checkBox = getWebElement(element, panel);
        Assertions.assertTrue(isDisplayed(checkBox), element + " check box is not displayed on " + panel + " panel");
        selectCheckbox(checkBox);
    }

    @When("^I select \"([^\"]*)\" check box on \"([^\"]*)\" panel on \"([^\"]*)\" Page$")
    public void iSelectCheckBoxOnPanelOnPage(String element, String panel, String page) throws Throwable {
        iShouldBeOnPage(page);
        iSelectCheckBoxOnPanel(element, panel);
    }

    @When("^I type \"([^\"]*)\" in \"([^\"]*)\" text box$")
    public void iTypeInTextBox(String text, String element) throws Throwable {
        WebElement textInput = getWebElement(element);
        Assertions.assertTrue(isDisplayed(element), element + " text box is not displayed on " + getCurrentPageName());
        textInput.sendKeys(text);
    }

    @When("^I type \"([^\"]*)\" in \"([^\"]*)\" text box on \"([^\"]*)\" page$")
    public void iTypeInTextBoxOnPage(String text, String element, String page) throws Throwable {
        iShouldBeOnPage(page);
        iTypeInTextBox(text, element);
    }

    @When("^I type \"([^\"]*)\" in \"([^\"]*)\" text box on \"([^\"]*)\" panel$")
    public void iTypeInTextBoxOnPanel(String text, String element, String panel) throws Throwable {
        WebElement textInput = getWebElement(element, panel);
        Assertions.assertTrue(isDisplayed(textInput), element + " text box is not displayed on " + panel + " panel");
        textInput.sendKeys(text);
    }

    @When("^I type \"([^\"]*)\" in \"([^\"]*)\" text box on \"([^\"]*)\" panel on \"([^\"]*)\" page$")
    public void iTypeInTextBoxOnPanelOnPage(String text, String element, String panel, String page) throws Throwable {
        iShouldBeOnPage(page);
        iTypeInTextBoxOnPanel(text, element, panel);
    }

    @Then("^I should see \"([^\"]*)\" in \"([^\"]*)\" (element|text block|pop-up)$")
    public void iShouldSeeInElement(String text, String element, String type) throws Throwable {
        WebElement webElement = getWebElement(element);
        Assertions.assertTrue(isDisplayed(element), element + " " + type + " not displayed on " + getCurrentPageName());
        String elementText = webElement.getText();
        Assertions.assertTrue(elementText.contains(text), text + " is not displayed in " + element + " " + type + " ( " + elementText + " )");
    }

    @Then("^I should see \"([^\"]*)\" in \"([^\"]*)\" (element|text block) on \"([^\"]*)\" page$")
    public void iShouldSeeInElementOnPage(String text, String element, String type, String page) throws Throwable {
        iShouldBeOnPage(page);
        iShouldSeeInElement(text, element, type);
    }

    @Then("^I should see \"([^\"]*)\" in \"([^\"]*)\" (element|text block) on \"([^\"]*)\" panel$")
    public void iShouldSeeInElementOnPanel(String text, String element, String type, String panel) throws Throwable {
        WebElement webElement = getWebElement(element, panel);
        Assertions.assertTrue(isDisplayed(webElement), element + " " + type + " not displayed on " + panel + " panel");
        String elementText = webElement.getText();
        Assertions.assertTrue(elementText.contains(text), text + " is not displayed in " + element + " " + type + " ( " + elementText + " )");
    }

    @Then("^I should see \"([^\"]*)\" in \"([^\"]*)\" (element|text block) on \"([^\"]*)\" panel on \"([^\"]*)\" page$")
    public void iShouldSeeInElementOnPanelOnPage(String text, String element, String type, String panel, String page) throws Throwable {
        iShouldBeOnPage(page);
        iShouldSeeInElementOnPanel(text, element, type, panel);
    }

    @Then("^I should see \"([^\"]*)\" in \"([^\"]*)\" list$")
    public void iShouldSeeInList(String text, String element) throws Throwable {
        Assertions.assertTrue(getWebElements(element).stream().anyMatch(e -> e.getText().contains(text)), text + " is not displayed in " + element);
    }

    @Then("^I should see \"([^\"]*)\" in \"([^\"]*)\" list on \"([^\"]*)\" page$")
    public void iShouldSeeInListOnPage(String text, String element, String page) throws Throwable {
        iShouldBeOnPage(page);
        iShouldSeeInList(text, element);
    }

    @Then("^I should see \"([^\"]*)\" in \"([^\"]*)\" list on \"([^\"]*)\" panel$")
    public void iShouldSeeInListOnPanel(String text, String element, String panel) throws Throwable {
        Assertions.assertTrue(getWebElements(element, panel).stream().anyMatch(e -> e.getText().contains(text)), text + " is not displayed in " + element);
    }

    @Then("^I should see \"([^\"]*)\" in \"([^\"]*)\" list on \"([^\"]*)\" panel on \"([^\"]*)\" page$")
    public void iShouldSeeInListOnPanel(String text, String element, String panel, String page) throws Throwable {
        iShouldBeOnPage(page);
        iShouldSeeInListOnPanel(text, element, panel);
    }

    @Then("^I should see \"([^\"]*)\" is enabled$")
    public void iShouldSeeIsEnabled(String element) throws Throwable {
        WebElement webElement = getWebElement(element);
        Assertions.assertTrue(isDisplayed(element), element + " not displayed on " + getCurrentPageName());
        Assertions.assertTrue(webElement.isEnabled(), element + " is not enabled on " + getCurrentPageName());
    }

    @Then("^I should see \"([^\"]*)\" is enabled on \"([^\"]*)\" page$")
    public void iShouldSeeIsEnabledOnPage(String element, String page) throws Throwable {
        iShouldBeOnPage(page);
        iShouldSeeIsEnabled(element);
    }

    @Then("^I should see \"([^\"]*)\" is enabled on \"([^\"]*)\" panel$")
    public void iShouldSeeIsEnabledOnPanel(String element, String panel) throws Throwable {
        WebElement webElement = getWebElement(element, panel);
        Assertions.assertTrue(isDisplayed(webElement), element + " not displayed on " + panel + " panel");
        Assertions.assertTrue(webElement.isEnabled(), element + " is not enabled on " + panel + " panel");
    }

    @Then("^I should see \"([^\"]*)\" is enabled on \"([^\"]*)\" panel on \"([^\"]*)\" page$")
    public void iShouldSeeIsEnabledOnPanelOnPage(String element, String panel, String page) throws Throwable {
        iShouldBeOnPage(page);
        iShouldSeeIsEnabledOnPanel(element, panel);
    }

    @Then("^I should see following in \"([^\"]*)\" list:$")
    public void iShouldSeeFollowingInList(String element, List<String> texts) throws Throwable {
        List<String> displayed = getWebElements(element).stream().map(WebElement::getText).toList();
        List<String> notDisplayed = texts.stream().filter(text -> displayed.stream().noneMatch(d -> d.contains(text))).toList();
        Assertions.assertEquals(0, notDisplayed.size(), "Not displayed in " + element + " on " + getCurrentPageName() + ": " + notDisplayed);
    }

    @Then("^I should see following in \"([^\"]*)\" list on \"([^\"]*)\" page:$")
    public void iShouldSeeFollowingInListOnPage(String element, String page, List<String> texts) throws Throwable {
        iShouldBeOnPage(page);
        iShouldSeeFollowingInList(element, texts);
    }

    @Then("^I should see following in \"([^\"]*)\" list on \"([^\"]*)\" panel:$")
    public void iShouldSeeFollowingInListOnPanel(String element, String panel, List<String> texts) throws Throwable {
        List<String> displayed = getWebElements(element, panel).stream().map(WebElement::getText).toList();
        List<String> notDisplayed = texts.stream().filter(text -> displayed.stream().noneMatch(d -> d.contains(text))).toList();
        Assertions.assertEquals(0, notDisplayed.size(), "Not displayed in " + element + " on " + panel + " panel: " + notDisplayed);
    }

    @Then("^I should see following in \"([^\"]*)\" list on \"([^\"]*)\" panel on \"([^\"]*)\" page:$")
    public void iShouldSeeFollowingInListOnPanelOnPage(String element, String panel, String page, List<String> texts) throws Throwable {
        iShouldBeOnPage(page);
        iShouldSeeFollowingInListOnPanel(element, panel, texts);
    }

    @Then("^the status code is (\\d+)$")
    public void theStatusCodeIs(int statusCode) {
        validatableResponse = response.then().log().all().statusCode(statusCode);
    }

    @And("^response includes the following:$")
    public void responseIncludeTheFollowing(Map<String, String> responseFields) {
        for (Map.Entry<String, String> field : responseFields.entrySet()) {
            if (StringUtils.isNumeric(field.getValue())) {
                validatableResponse.body(field.getKey(), equalTo(Long.parseLong(field.getValue())));
            } else {
                validatableResponse.body(field.getKey(), equalTo(field.getValue()));
            }
        }
    }

    @And("^response includes the following in any order:$")
    public void responseIncludeTheFollowingInAnyOrder(Map<String, String> responseFields) {
        for (Map.Entry<String, String> field : responseFields.entrySet()) {
            if (StringUtils.isNumeric(field.getValue())) {
                validatableResponse.body(field.getKey(), containsInAnyOrder(Integer.parseInt(field.getValue())));
            } else {
                validatableResponse.body(field.getKey(), containsInAnyOrder(field.getValue()));
            }
        }
    }

}

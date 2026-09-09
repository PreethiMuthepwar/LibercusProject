package test.automation.steps.Libercus;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import test.automation.framework.Actions;
import test.automation.pages.Libercus.ManageSite.LiveManifestEditionGeneration;
import test.automation.pages.Libercus.ManageSite.ThumbnailsGeneration;
import static test.automation.pages.Libercus.ManageSite.RecacheSite.*;
public class ManageSiteSteps {
    @Then("I verify Reache is sucessfull")
    public void iVerifyReacheIsSucessfull() throws InterruptedException {
        reacheIsSucessfull();
    }
    @And("I generate the edition for {string}")
    public void iGenerateTheEditionFor(String day) throws Exception {
        LiveManifestEditionGeneration editionGeneration = new LiveManifestEditionGeneration();
        if (day.equalsIgnoreCase("today")) {
            editionGeneration.AfterMidnight();
        } else {
            editionGeneration.BeforeMidnight();
        }
    }
    @Then("I verify that the edition is generated successfully")
    public void iVerifyThatTheEditionIsGeneratedSuccessfully() {
        Actions.performAction("VerifyEditionGeneration");
    }
    @And("I generate the edition thumbnails for {string}")
    public void iGenerateTheEditionThumbnailsFor(String day) throws Exception {
        ThumbnailsGeneration thumbnailsGeneration = new ThumbnailsGeneration();
        if (day.equalsIgnoreCase("today")) {
            thumbnailsGeneration.AfterMidnight();
        } else {
            thumbnailsGeneration.BeforeMidnight();
        }
    }
    @And("I tap on Submit button")
    public void iTapOnSubmitButton() {
        Actions.performAction("subitButton");
    }
}
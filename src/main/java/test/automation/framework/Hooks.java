package test.automation.framework;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

import java.io.File;
import java.util.Collection;

import static test.automation.framework.Browser.downloadFilePath;
import static test.automation.framework.Runner.log;

public final class Hooks {

    public static Scenario scenario;

    @Before
    public void beforeScenario(Scenario scenario)
    {
        Hooks.scenario = scenario;
        log().info("Scenario Started: " + scenario.getName() );
        File folder = new File(downloadFilePath);

        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();

            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        file.delete();
                    }
                }
            }
        }
    }

    @After
    public void afterScenario(Scenario scenario) {
        Collection<String> tags = scenario.getSourceTagNames();
        if (Browser.isStarted()) {
            if (scenario.isFailed() || tags.contains("@Screenshot")) {
                scenario.attach(Browser.getScreenShot(), "image/png", "");
            }
            if (!(Config.isKeepBrowser() || tags.contains("@KeepBrowser"))) {
                Browser.quit();
            }
        }
        DB.closeConnection();
        log().info("Scenario completed: " + scenario.getName());
        log().info("Scenario status: " + scenario.getStatus());
    }

}

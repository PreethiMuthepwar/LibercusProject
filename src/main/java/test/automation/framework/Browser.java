package test.automation.framework;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;
import java.time.Duration;
import java.util.*;
import java.util.logging.Level;
import static test.automation.framework.Config.*;
import static test.automation.framework.Runner.log;

public final class Browser {

    private static WebDriver driver;
    public static final String downloadFilePath = System.getProperty("user.dir") + "\\Downloads";

    private static void start() {
        try {
            if (getRemoteUrl() != null) {
                DesiredCapabilities capabilities = new DesiredCapabilities();
                if (getBrowser() != null) {
                    capabilities.setCapability(CapabilityType.BROWSER_NAME, getBrowser());
                }
                if (getBrowserVersion() != null) {
                    capabilities.setCapability(CapabilityType.BROWSER_VERSION, getBrowserVersion());
                }
                if (getPlatform() != null) {
                    capabilities.setCapability(CapabilityType.PLATFORM_NAME, getPlatform());
                }
                if (getPlatformVersion() != null) {
                    capabilities.setCapability("platformVersion", getPlatformVersion());
                }
                if (getDevice() != null) {
                    capabilities.setCapability("deviceName", getDevice());
                    if (getAppiumVersion() != null) {
                        capabilities.setCapability("appiumVersion", getAppiumVersion());
                    }
                    if (getPlatform() != null && getPlatform().toLowerCase().contains("ios")) {
                        driver = new IOSDriver(new URL(getRemoteUrl()), capabilities);
                    } else {
                        driver = new AndroidDriver(new URL(getRemoteUrl()), capabilities);
                    }
                    log().info(getDevice() + " started in cloud.");
                    return;
                }
                driver = new RemoteWebDriver(new URL(getRemoteUrl()), capabilities);
                log().info(getBrowser() + " started in cloud.");
                return;
            }

            if (getDevice() != null) {
                Map<String, String> emulationOptions = new HashMap<>();
                emulationOptions.put("deviceName", getDevice());
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.setExperimentalOption("mobileEmulation", emulationOptions);
                driver = new ChromeDriver(chromeOptions);
                log().info(getDevice() + "Chrome Emulation started.");
                return;
            }

            switch (getBrowser().toLowerCase())
            {
              case "chrome":
//                  ChromeDriverService service = new ChromeDriverService.Builder()
//                          .usingDriverExecutable(new File("D:\\Drivers\\chromedriver.exe"))
//                          .usingAnyFreePort()
//                          .build();
//
//                  Map<String, Object> prefs = new HashMap<>();
//                  prefs.put("plugins.always_open_pdf_externally", true); // Forces download instead of opening in viewer
//                  ChromeOptions options = new ChromeOptions();
//                  options.addArguments("--incognito");
//                  options.setExperimentalOption("prefs", prefs);
//                  options.addArguments("--disable-print-preview");
//                  options.addArguments("--disable-pdf");
//                  driver = new ChromeDriver(service,options);
//                  driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(300));
//
//                  DevTools devTools = ((HasDevTools) driver).getDevTools();
//                  devTools.createSession();
//                  devTools.send(org.openqa.selenium.devtools.v136.emulation.Emulation.setGeolocationOverride(
//                          Optional.of(37.7749),   // latitude
//                          Optional.of(-122.4194), // longitude
//                          Optional.of(1)          // accuracy
//                  ));
//
//                  devTools.send(new Command<>("Page.enable", Map.of()));
//                  Map<String, Object> downloadOptions = new HashMap<>();
//                  downloadOptions.put("behavior", "allow");
//                  downloadOptions.put("downloadPath", downloadFilePath);
//                  downloadOptions.put("eventsEnabled", true);

//                  break;
                    Map<String, Object> prefs = new HashMap<>();
                    prefs.put("plugins.always_open_pdf_externally", true);

                    ChromeOptions options = new ChromeOptions();
                    options.addArguments("--incognito");
                    options.addArguments("--disable-print-preview");
                    options.addArguments("--disable-pdf");
                    options.setExperimentalOption("prefs", prefs);

                    // ❗ Do NOT set any chromedriver.exe path
                    driver = new ChromeDriver(options);  // Selenium Manager picks correct driver

                    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(300));

                    // DevTools Setup
//                  DevTools devTools = ((HasDevTools) driver).getDevTools();
//                  devTools.createSession();
//
//                  Map<String, Object> geoParams = new HashMap<>();
//                  geoParams.put("latitude", 37.7749);
//                  geoParams.put("longitude", -122.4194);
//                  geoParams.put("accuracy", 1);
//
//                  devTools.send(new Command<>("Emulation.setGeolocationOverride", geoParams));

                    break;
                case "chrome_headless":
                    ChromeOptions headlessOptions = new ChromeOptions();
                    headlessOptions.addArguments("--headless");
                    driver = new ChromeDriver(headlessOptions);
                    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(300));
                    break;
                case "firefox":
                    driver = new FirefoxDriver();
                    break;
                case "safari":
                    driver = new SafariDriver();
                    break;
                case "edge":
                    driver = new EdgeDriver();
                    break;
                case "ie":
                case "internet explorer":
                    driver = new InternetExplorerDriver();
                    break;
            }
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(60));
            log().info(getBrowser() + " browser started.");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void maximize() {
        getDriver(By.cssSelector("story-preview")).manage().window().maximize();
    }

    public static boolean isStarted() {
        return driver != null;
    }

    public static void reStart() {
        if (isStarted()) {
            quit();
        }
        start();
        log().info(getBrowser() + " browser re-started.");
    }

    public static void quit() {
        if (isStarted()) {
            driver.quit();
            driver = null;
        }
        log().info(getBrowser() + " browser closed.");
    }

    public static WebDriver getDriver(By by) {
        if (!isStarted()) {
            start();
            maximize();
        }
        return driver;
    }

    public static AndroidDriver getAndroidDriver() {
        if (getDriver(By.cssSelector("story-preview")) instanceof AndroidDriver) {
            return (AndroidDriver) getDriver(By.cssSelector("story-preview"));
        }
        return null;
    }

    public static IOSDriver getIOSDriver() {
        if (getDriver(By.cssSelector("story-preview")) instanceof IOSDriver) {
            return (IOSDriver) getDriver(By.cssSelector("story-preview"));
        }
        return null;
    }

    public static byte[] getScreenShot() {
        try {
            return ((TakesScreenshot) getDriver(By.cssSelector("story-preview"))).getScreenshotAs(OutputType.BYTES);
        } catch (Exception e) {
            log().log(Level.WARNING, e.getMessage());
            return null;
        }
    }
}

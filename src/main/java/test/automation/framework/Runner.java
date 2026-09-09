package test.automation.framework;

import com.rajatthareja.reportbuilder.Color;
import com.rajatthareja.reportbuilder.ReportBuilder;
import io.cucumber.core.cli.Main;
import io.cucumber.core.eventbus.EventBus;
import io.cucumber.core.feature.FeatureParser;
import io.cucumber.core.gherkin.Feature;
import io.cucumber.core.options.CommandlineOptionsParser;
import io.cucumber.core.options.RuntimeOptions;
import io.cucumber.core.resource.ClassLoaders;
import io.cucumber.core.runtime.FeaturePathFeatureSupplier;
import io.cucumber.core.runtime.TimeServiceEventBus;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import static test.automation.framework.Config.*;

public final class Runner {

    private static int exitStatus = 0;
    private static int buildCount = 0;
    private final static Logger logger = Logger.getLogger("Runner");
    private static final List<Object> jsonReports = new LinkedList<>();

    public static void main(String ... args) throws IOException {
        prepareReportDir();
        setLogFormat();
        prepareScriptExecution(args);
        buildReport();
        Browser.quit();
        System.exit(exitStatus);
    }

    public static Logger log() {
        return logger;
    }

    private static void setLogFormat() {
        try {
            logger.setUseParentHandlers(false);
            FileHandler fileHandler = new FileHandler(getReportsDir() + "log.html");
            logger.addHandler(fileHandler);
            LogHtmlFormatter logHtmlFormatter = new LogHtmlFormatter();
            fileHandler.setFormatter(logHtmlFormatter);
        } catch (Exception e) {
            log().log(Level.WARNING, e.getMessage());
        }
    }

    private static String[] getCucumberArgs(String ... args) {
        List<String> cucumberArgs = new LinkedList<>();
        cucumberArgs.add("--glue");
        cucumberArgs.add(getGlue());
        cucumberArgs.add("--plugin");
        cucumberArgs.add("json:" + getReportsDir() + "report.json");
        cucumberArgs.add("--plugin");
        cucumberArgs.add("rerun:" + getReportsDir() + "rerun.txt");
//        cucumberArgs.add("--plugin");
//        cucumberArgs.add("html:" + getReportsDir() + "cucumber");
        cucumberArgs.add("--plugin");
        cucumberArgs.add("pretty");
        if (getTags() != null) {
            cucumberArgs.add("--tags");
            cucumberArgs.add(getTags());
        }
        if (getScenarios() != null) {
            cucumberArgs.add("--name");
            cucumberArgs.add(getScenarios());
        }
        if (isDryRun()) {
            cucumberArgs.add("--dry-run");
        }

        cucumberArgs.addAll(Arrays.asList(args));
        if (getFeatures() != null) {
            if (getFeatures().startsWith("@")) {
                cucumberArgs.add(getFeatures());
            } else {
                cucumberArgs.add("classpath:" + Config.getClassPath("features") + getFeatures());
            }
        }
        log().info("Cucumber Options: " + cucumberArgs);
        return cucumberArgs.toArray(new String[cucumberArgs.size()]);
    }

    private static String[] getCucumberArgs(int count, String ... args) {
        List<String> cucumberArgs = new LinkedList<>();
        cucumberArgs.add("--glue");
        cucumberArgs.add(getGlue());
        cucumberArgs.add("--plugin");
        cucumberArgs.add("json:" + getReportsDir() + "report"+count+".json");
        cucumberArgs.add("--plugin");
        cucumberArgs.add("rerun:" + getReportsDir() + "rerun.txt");
//        cucumberArgs.add("--plugin");
//        cucumberArgs.add("html:" + getReportsDir() + "cucumber");
        cucumberArgs.add("--plugin");
        cucumberArgs.add("pretty");
        if (getTags() != null) {
            cucumberArgs.add("--tags");
            cucumberArgs.add(getTags());
        }
        if (getScenarios() != null) {
            cucumberArgs.add("--name");
            cucumberArgs.add(getScenarios());
        }
        if (isDryRun()) {
            cucumberArgs.add("--dry-run");
        }

        cucumberArgs.addAll(Arrays.asList(args));
        if (getFeatures() != null) {
            if (getFeatures().startsWith("@")) {
                cucumberArgs.add(getFeatures());
            } else {
                cucumberArgs.add("classpath:" + Config.getClassPath("features") + getFeatures());
            }
        }
        log().info("Cucumber Options: " + cucumberArgs);
        return cucumberArgs.toArray(new String[cucumberArgs.size()]);
    }

    private static String[] getCucumberRerunArgs() {
        List<String> cucumberArgs = new LinkedList<>();
        cucumberArgs.add("--glue");
        cucumberArgs.add(getGlue());
        cucumberArgs.add("--plugin");
        cucumberArgs.add("json:" + getReportsDir() + "rerun.json");
//        cucumberArgs.add("--plugin");
//        cucumberArgs.add("html:" + getReportsDir() + "cucumber/rerun");
        cucumberArgs.add("--plugin");
        cucumberArgs.add("pretty");

        cucumberArgs.add("@" + getReportsDir() + "rerun.txt");
        log().info("Cucumber Options: " + cucumberArgs);
        return cucumberArgs.toArray(new String[cucumberArgs.size()]);
    }

    private static void runCucumber(String ... cucumberArgs) {
        try {
            exitStatus = Main.run(cucumberArgs, Thread.currentThread().getContextClassLoader());
        } catch (Throwable e) {
            e.printStackTrace();
            exitStatus = 1;
        }
    }

    private static void runCucumber(int count, String ... cucumberArgs) {
        try {
            for(int i=0; i<count; i++)
                exitStatus = Main.run(cucumberArgs, Thread.currentThread().getContextClassLoader());
        } catch (Throwable e) {
            e.printStackTrace();
            exitStatus = 1;
        }
    }

    private static void prepareScriptExecution(String...args){
        if (getPartitionSize() > 0) {
            String tagName = null;
            String browser = null;
            String[] listOfBrowsers = getBrowser().split(",");
            List<Process> builds = new LinkedList<>();
            for (int i = 0; i < getPartitionSize(); i++) {
                if (listOfBrowsers.length == 1) {
                    if (getTags() != null) {
                        String[] listOfTags = getTags().split(",");
                        tagName = listOfTags[i];
                    }
                    try {
                        builds.add(runPartition(getTestCases(tagName, getCucumberArgs(args)), tagName, builds.size()));
                    } catch (IOException e) {
                        e.printStackTrace();
                        exitStatus = 1;
                    }
                } else {
                    browser = listOfBrowsers[i];
                    try {
                        builds.add(runPartition(getTestCases(getTags(), getCucumberArgs(args)), getTags(), browser, builds.size()));
                    } catch (IOException e) {
                        e.printStackTrace();
                        exitStatus = 1;
                    }
                }
            }
            buildCount = builds.size();
            for (Process build : builds) {
                try {
                    build.waitFor();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    exitStatus = 1;
                }
                if (build.exitValue() != 0) {
                    exitStatus = 1;
                }
            }
        } else {
            runCucumber(getCucumberArgs(args));

            if (isRerun() && exitStatus != 0 && Files.exists(Paths.get(getReportsDir() + "rerun.txt"))) {
                runCucumber(getCucumberRerunArgs());
            }
        }
    }

    private static Process runPartition(int buildNumber, String tagsList, String browserName, String... cucumberArgs) throws IOException {
        StringBuilder runStr = new StringBuilder();
//        runStr.append(" ").append(feature.getUri()).append(":").append(feature.getPickles());
        new File(getReportsDir() + "build" + buildNumber).mkdirs();
        Files.write(Paths.get(getReportsDir() + "build" + buildNumber +"/run.txt"), runStr.toString().trim().getBytes());

        ProcessBuilder pb;
        if (isWindows()) {
            pb = new ProcessBuilder("cmd", "/C", "mvn exec:java");
        } else {
            pb = new ProcessBuilder("sh", "-c", "mvn exec:java");
        }
        pb.redirectErrorStream(true);
        pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        Map<String, String> env = pb.environment();
        env.put("partition_size", "");
        env.put("tags", tagsList);
        env.put("browser", browserName);
        env.put("reports_dir", getReportsDir() + "build" + buildNumber + "/");
        env.put("features", "@" + getReportsDir() + "build" + buildNumber +"/run.txt");
        return pb.start();
    }

    private static Process runPartition(int buildNumber, String browserName, String... cucumberArgs) throws IOException {
        StringBuilder runStr = new StringBuilder();
//        runStr.append(" ").append(feature.getUri()).append(":").append(feature.getPickles());
        new File(getReportsDir() + "build" + buildNumber).mkdirs();
        Files.write(Paths.get(getReportsDir() + "build" + buildNumber +"/run.txt"), runStr.toString().trim().getBytes());

        ProcessBuilder pb;
        if (isWindows()) {
            pb = new ProcessBuilder("cmd", "/C", "mvn exec:java");
        } else {
            pb = new ProcessBuilder("sh", "-c", "mvn exec:java");
        }
        pb.redirectErrorStream(true);
        pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        Map<String, String> env = pb.environment();
        env.put("partition_size", "");
        env.put("browser", browserName);
        env.put("reports_dir", getReportsDir() + "build" + buildNumber + "/");
        env.put("features", "@" + getReportsDir() + "build" + buildNumber +"/run.txt");
        return pb.start();
    }

    private static Process runPartition(Feature feature, String selectedTag, int buildNumber) throws IOException {
        StringBuilder runStr = new StringBuilder();
        runStr.append(" ").append(feature.getUri()).append(":").append(feature.getPickles());
        new File(getReportsDir() + "build" + buildNumber).mkdirs();
        Files.write(Paths.get(getReportsDir() + "build" + buildNumber +"/run.txt"), runStr.toString().trim().getBytes());

        ProcessBuilder pb;
        if (isWindows()) {
            pb = new ProcessBuilder("cmd", "/C", "mvn exec:java");
        } else {
            pb = new ProcessBuilder("sh", "-c", "mvn exec:java");
        }
        pb.redirectErrorStream(true);
        pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        Map<String, String> env = pb.environment();
        env.put("partition_size", "");
        env.put("tags", selectedTag);
        env.put("reports_dir", getReportsDir() + "build" + buildNumber + "/");
        env.put("features", "@" + getReportsDir() + "build" + buildNumber +"/run.txt");
        return pb.start();
    }

    private static Process runPartition(Feature feature, String selectedTag, String browserName, int buildNumber) throws IOException {
        StringBuilder runStr = new StringBuilder();
        runStr.append(" ").append(feature.getUri()).append(":").append(feature.getPickles());
        new File(getReportsDir() + "build" + buildNumber).mkdirs();
        Files.write(Paths.get(getReportsDir() + "build" + buildNumber +"/run.txt"), runStr.toString().trim().getBytes());

        ProcessBuilder pb;
        if (isWindows()) {
            pb = new ProcessBuilder("cmd", "/C", "mvn exec:java");
        } else {
            pb = new ProcessBuilder("sh", "-c", "mvn exec:java");
        }
        pb.redirectErrorStream(true);
        pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        Map<String, String> env = pb.environment();
        env.put("partition_size", "");
        env.put("tags", selectedTag);
        env.put("browser", browserName);
        env.put("reports_dir", getReportsDir() + "build" + buildNumber + "/");
        env.put("features", "@" + getReportsDir() + "build" + buildNumber +"/run.txt");
        return pb.start();
    }

    private static void buildReport() {
        ReportBuilder reportBuilder = new ReportBuilder();
        reportBuilder.setReportDirectory(getReportsDir());
        reportBuilder.setReportColor(Color.CYAN);
        reportBuilder.setAdditionalInfo("Date", LocalDateTime.now().toString());
        reportBuilder.setAdditionalInfo("URL", getUrl());
        reportBuilder.setAdditionalInfo("Browser", getBrowser());
        if (getBrowserVersion() != null) {
            reportBuilder.setAdditionalInfo("Browser Version", getBrowserVersion());
        }
        reportBuilder.setAdditionalInfo("Platform", getPlatform() != null ? getPlatform() : System.getProperty("os.name"));
        if (getPlatformVersion() != null) {
            reportBuilder.setAdditionalInfo("Platform Version", getPlatformVersion());
        }
        if (getDevice() != null) {
            reportBuilder.setAdditionalInfo("Device", getDevice());
        }

        if (buildCount > 0) {
            List<Object> jsonReports = new LinkedList<>();
            for (int i = 0; i < buildCount; i++) {
                jsonReports.add(new File(getReportsDir() + "build" + i + "/report.json"));
            }
            reportBuilder.build(jsonReports);

            if (isRerun()) {
                jsonReports.clear();
                reportBuilder.setReportFileName("rerun");
                for (int i = 0; i < buildCount; i++) {
                    if (Files.exists(Paths.get(getReportsDir() + "build" + i + "rerun.json"))) {
                        jsonReports.add(new File(getReportsDir() + "build" + i + "/rerun.json"));
                    }
                }
                reportBuilder.build(jsonReports);

                jsonReports.clear();
                reportBuilder.setReportFileName("final_report");
                for (int i = 0; i < buildCount; i++) {
                    jsonReports.add(new File(getReportsDir() + "build" + i + "/report.json"));
                    if (Files.exists(Paths.get(getReportsDir() + "build" + i + "rerun.json"))) {
                        jsonReports.add(new File(getReportsDir() + "build" + i + "/rerun.json"));
                    }
                }
                reportBuilder.build(jsonReports);
            }
        } else {
            reportBuilder.build(new File(getReportsDir() + "report.json"));

            if (isRerun() && Files.exists(Paths.get(getReportsDir() + "rerun.json"))) {
                reportBuilder.setReportFileName("rerun");
                reportBuilder.build(new File(getReportsDir() + "rerun.json"));

                reportBuilder.setReportFileName("final_report");
                reportBuilder.build(new File(getReportsDir() + "report.json"), new File(getReportsDir() + "rerun.json"));
            }
        }
    }

    private static void buildReport(int count) {
        BuildReport reportBuilder = new BuildReport();
        reportBuilder.setReportDirectory(getReportsDir());
        reportBuilder.setReportColor(Color.CYAN);
        reportBuilder.setAdditionalInfo("Date", LocalDateTime.now().toString());
        reportBuilder.setAdditionalInfo("URL", getUrl());
        reportBuilder.setAdditionalInfo("Browser", getBrowser());
        if (getBrowserVersion() != null) {
            reportBuilder.setAdditionalInfo("Browser Version", getBrowserVersion());
        }
        reportBuilder.setAdditionalInfo("Platform", getPlatform() != null ? getPlatform() : System.getProperty("os.name"));
        if (getPlatformVersion() != null) {
            reportBuilder.setAdditionalInfo("Platform Version", getPlatformVersion());
        }
        if (getDevice() != null) {
            reportBuilder.setAdditionalInfo("Device", getDevice());
        }

        for (int i = 1; i <= count; i++) {
            jsonReports.add(new File(getReportsDir() + "report" + i + ".json"));
            if (i == count)
                reportBuilder.build(jsonReports);
        }

    }

    private static void prepareReportDir() {
        new File(getReportsDir()).mkdirs();
        Arrays.asList("report.json", "report.html", "cucumber/report.js",
                "rerun.txt", "rerun.json", "rerun.html", "cucumber/rerun/report.js",
                "final_report.html").forEach(file -> new File(getReportsDir() + file).delete());
    }

    public static String getGlue() {
        return Runner.class.getName().replace(".framework.Runner", "");
    }

    private static Feature getTestCases(String tagName, String ... cucumberArgs) {
        CommandlineOptionsParser commandlineOptionsParser = new CommandlineOptionsParser(System.out);
        RuntimeOptions runtimeOptions = commandlineOptionsParser.parse(cucumberArgs).build();
        EventBus eventBus = new TimeServiceEventBus(Clock.systemUTC(), UUID::randomUUID);
        FeatureParser parser = new FeatureParser(eventBus::generateId);
        FeaturePathFeatureSupplier featurePathFeatureSupplier = new FeaturePathFeatureSupplier(ClassLoaders::getDefaultClassLoader, runtimeOptions, parser);
        return featurePathFeatureSupplier.get().stream().filter(f -> f.getSource().contains(tagName)).findFirst().get();
    }
}

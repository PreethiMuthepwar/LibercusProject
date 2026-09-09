package test.automation.utils;
import java.util.Map;
import static test.automation.framework.Config.getCustomerEnv;
import static test.automation.utils.yaml_util.loadData;
public class testDataUtils {
    public static Object loadStoryData(String module)
    {
        Map<String, Object> screenData = loadData("src/main/resources/test/automation/data/StoryCreation.yml");
        if (screenData.containsKey(module)) {
            Map<String, Object> moduleData = (Map<String, Object>) screenData.get(module);
            return moduleData;
        }
        return null;
    }
    public static Map<String, Object> loadInteractiveAdsData(String module) {
        Map<String, Object> screenData = loadData("src/main/resources/test/automation/data/InteractiveAds.yml");
        if (screenData.containsKey(module)) {
            Map<String, Object> moduleData = (Map<String, Object>) screenData.get(module);
            return moduleData;
        }
        return null;
    }
    public static Map<String, Object> loadRichMediaAdData(String module) {
        Map<String, Object> screenData = loadData("src/main/resources/test/automation/data/RichMediaAds.yml");
        if (screenData.containsKey(module)) {
            Map<String, Object> moduleData = (Map<String, Object>) screenData.get(module);
            return moduleData;
        }
        return null;
    }
    public static Object getLoginCredentials() {
        Map<String, Object> envData = loadData("src/main/resources/test/automation/data/LoginDetails.yml");
        return envData;
    }
    public static Map<String, Object> getCredentials() {
        Map<String, Object> loginData = (Map<String, Object>) getLoginCredentials();
        return loginData;
    }
    public static Map<String, Object> getStoryData(String stories) {
        Map<String, Object> loginData = (Map<String, Object>) loadStoryData(stories);
        return loginData;
    }

    public static String getShapeForCurrentEnv(String storyKey)
    {
        String currentProject = getCustomerEnv();
        Map<String, Object> storyData = (Map<String, Object>) loadStoryData(storyKey);
        if (storyData == null) {
            throw new RuntimeException("Story key not found in YAML: " + storyKey);
        }
        Map<String, Object> envData = null;
        for (String key : storyData.keySet()) {
            if (key.equalsIgnoreCase(currentProject.trim())) {
                envData = (Map<String, Object>) storyData.get(key);
                break;
            }
        }
//        Map<String, Object> envData = (Map<String, Object>) storyData.get(currentProject);
        if (envData == null)
        {
            System.out.println("Available environments in YAML for " + storyKey + ": " + storyData.keySet());
            throw new RuntimeException("No data found for project: " + currentProject);
        }
        return (String) envData.get("Shape");
    }

    public static Map<String, Object> getInteractiveAdsData(String interactiveads) {
        Map<String, Object> loginData = (Map<String, Object>) loadInteractiveAdsData(interactiveads);
        return loginData;
    }
    public static Map<String, Object> getRichMediaAdData(String richMediaads) {
        Map<String, Object> loginData = (Map<String, Object>) loadRichMediaAdData(richMediaads);
        return loginData;
    }
    public static Object LoadDates() {
        Map<String, Object> envData = loadData("src/main/resources/test/automation/data/Dates.yml");
        return envData;
    }
    public static Map<String, Object> getDates() {
        Map<String, Object> Dates = (Map<String, Object>) LoadDates();
        return Dates;
    }
    public static Object LoadHelpContent(String module) {
        Map<String, Object> envData = loadData("src/main/resources/test/automation/data/HelpContent.yml");
        if (envData.containsKey(module)) {
            Map<String, Object> moduleData = (Map<String, Object>) envData.get(module);
            return moduleData;
        }
        return null;
    }
    public static Map<String, Object> getHelpContent(String eachContentText) {
        Map<String, Object> HelpContent = (Map<String, Object>) LoadHelpContent(eachContentText);
        return HelpContent;
    }
    public static Object LoadImagedata(String module) {
        Map<String, Object> screenData = loadData("src/main/resources/test/automation/data/MediaFiles.yml");
        if (screenData.containsKey(module)) {
            Map<String, Object> moduleData = (Map<String, Object>) screenData.get(module);
            return moduleData;
        }
        return null;
    }
    public static Map<String, Object> getImageData(String image) {
        Map<String, Object> ImageData = (Map<String, Object>) LoadImagedata(image);
        return ImageData;
    }
    public static Map<String, Object> loadCongeroTypes(String module)
    {
        Map<String, Object> screenData = loadData("src/main/resources/test/automation/data/CongeroType.yml");
        if (screenData.containsKey(module)) {
            Map<String, Object> moduleData = (Map<String, Object>) screenData.get(module);
            return moduleData;
        }
        return null;
    }
    public static Map<String, Object> getCongeroData(String congeroType) {
        Map<String, Object> loginData = (Map<String, Object>) loadCongeroTypes(congeroType);
        return loginData;
    }
    public static Map<String, Object> loadUserData()
    {
        Map<String, Object> userData = loadData("src/main/resources/test/automation/data/Users.yml");
        return userData;
    }
    public static Map<String, Object> getUserData() {
        Map<String, Object> userData = (Map<String, Object>) loadUserData();
        return userData;
    }
    public static Map<String, Object> loadTagData()
    {
        Map<String, Object> tagData = loadData("src/main/resources/test/automation/data/Tags.yml");
        return tagData;
    }
    public static Map<String, Object> getTagData() {
        Map<String, Object> tagData = (Map<String, Object>) loadTagData();
        return tagData;
    }
}
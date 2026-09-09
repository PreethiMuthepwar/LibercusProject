package test.automation.framework;

import org.apache.commons.lang3.RandomStringUtils;
import java.util.Random;

public class Util {

    public static int getRandomIntBetween(int min, int max) {
        return new Random().nextInt(max + 1 - min) + min;
    }

    public static int getRandomIndex(int size) {
        return getRandomIntBetween(0, size - 1);
    }

    public static String getRandom(int size, String input) {
        StringBuilder sb = new StringBuilder();
        while (sb.length() < size)
            sb.append(input.charAt(getRandomIndex(input.length())));
        return sb.toString();
    }

    public static String generateRandomEmail(int length) {
        if (length == 0) {
            length = 16;
        }

        String allowedChars = "abcdefghijklmnopqrstuvwxyz" + "1234567890";
        String email = RandomStringUtils.random(length, allowedChars);
        if (length > 5) {
            String miliseconds = String.format("%d", System.currentTimeMillis());
            email = email.substring(0, email.length() - 5);
            email = email + miliseconds.substring(miliseconds.length() - 5);
        }
        email = email.substring(0, email.length()) + "@void.osius.com";
        return email;
    }

    public static String getCustomerName(int size) {
        String str = getRandomNumber(size);
        String name = "Automation_New_Name_";
        return name + str;
    }

    public static String getRandomWebsite(String websiteName) {
        String str = websiteName.toLowerCase().replace("_", "");
        return "http://www." + str + ".com";
    }


    public static String projectName(int size) {
        String str = getRandomNumber(size);
        String name = "Automation_Project_Name_";
        return name + str;
    }

    public static String projectDescription(int size) {
        String str = getRandomNumber(size);
        String name = "Project_Description_";
        return name + str;
    }

    public static String getRandomString(int size) {
        return getRandom(size, "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz");
    }

    public static String getRandomNumber(int size) {
        return getRandom(size, "0123456789");
    }

    public static String getRandomMobleNumber() {
        return getRandom(3, "0123456789") + "-" + getRandom(3, "0123456789") + "-" + getRandom(4, "0123456789");
    }

    public static String getRandomFirstName() {
        String[] firstNames = {"VENKATE", "PARAM", "DURG", "ABHILAS"};
        return firstNames[new Random().nextInt(firstNames.length)];
    }

    public static String getRandomActivity() {
        String[] firstNames = {"Developer", "ManualTester", "AutomationTester", "Support", "UIDeveloper", "Devops", "TechnicalSupport", "SystemAdmin"};
        return firstNames[new Random().nextInt(firstNames.length)];
    }

}

package test.automation.framework;

public class Random {

    public static int getRandomIntBetween(int min, int max) {
        return new java.util.Random().nextInt(max + 1 - min) + min;
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

    public static String getRandomString(int size) {
        return getRandom(size, "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz");
    }

    public static String getRandomNumber(int size) {
        return getRandom(size, "0123456789");
    }
}

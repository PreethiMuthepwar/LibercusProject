package test.automation.utils;

import test.automation.framework.Data;
import test.automation.framework.Util;
import test.automation.models.EnFinUser;
import test.automation.models.PIUserDetails;
import java.util.List;
import java.util.Map;
import static test.automation.framework.Data.getAsMap;
import static test.automation.framework.Data.getAsObject;
import static test.automation.framework.Random.*;
import static test.automation.framework.Runner.log;

public class UserUtils
{

    private static EnFinUser enFinUser;
    public static String email;
    public static String accountName;
    private static final PIUserDetails piUserDetails = new PIUserDetails();

    public static EnFinUser getEnFinUser() {
        return (EnFinUser) getAsObject("EnFinUser");
    }

    public static Map<String, Object> getUserMap() {
        return getAsMap("user_v1");
    }

    public static String generateEmail() {
        email = getRandomString(getRandomIntBetween(5, 10)) + "ts101sa" + "@yopmail.com";
        log().info("Email is: " + email);
        return email;
    }

    public static String generateEmail(String type) {
        if (type.equals("approved")) {
            email = getRandomString(getRandomIntBetween(5, 10)) + "ts101sa" + "@yopmail.com";
        } else if (type.equals("rejected")) {
            email = getRandomString(getRandomIntBetween(5, 10)) + "ts101s" + "@yopmail.com";
        } else if (type.equals("no-match")) {
            email = getRandomString(getRandomIntBetween(5, 10)) + "ts101sa" + "@yopmail.com";
        }

        log().info("Email is: " + email);
        return email;
    }

    public static PIUserDetails getNewPIUserDetails() {
        accountName = "Test-User-" + getRandomString(getRandomIntBetween(5, 10));
        log().info("Account Name is: " + accountName);
        piUserDetails.setAccountName(accountName);
        piUserDetails.setPhoneNumber("9" + getRandomNumber(9));
        return piUserDetails;
    }

    public static EnFinUser getNewEnFinUser(String type) {
        enFinUser = getUserDetails(type);
        enFinUser.setFirstName(enFinUser.getFirstName());
        enFinUser.setLastName(enFinUser.getLastName());
        enFinUser.setMobile("9" + getRandomNumber(9));
        email = generateEmail(type);
        enFinUser.setEmail(email);
        log().info("Email is: " + email);
        enFinUser.setPrimaryAddress(enFinUser.getPrimaryAddress());
        enFinUser.setCity(enFinUser.getCity());
        enFinUser.setState(enFinUser.getState());
        enFinUser.setZipCode(enFinUser.getZipCode());
        enFinUser.setSocialSecurityNumber(enFinUser.getSocialSecurityNumber());
        return enFinUser;
    }

    public static EnFinUser getValidEnFinUser(String type) {
        List<EnFinUser> enFinUsers = ((List<EnFinUser>) ((Object) Data.getAsObjects("Enfin_Login_Credentials", EnFinUser.class))).stream().filter(u -> u.getType().equals(type)).toList();
        enFinUser = enFinUsers.get(Util.getRandomIndex(enFinUsers.size()));
        return enFinUser;
    }

    public static EnFinUser getUserDetails(String type) {
        List<EnFinUser> enFinUsers = ((List<EnFinUser>) ((Object) Data.getAsObjects("EnFinUser", EnFinUser.class))).stream().filter(u -> u.getType().equals(type)).toList();
        enFinUser = enFinUsers.get(Util.getRandomIndex(enFinUsers.size()));
        return enFinUser;
    }
}

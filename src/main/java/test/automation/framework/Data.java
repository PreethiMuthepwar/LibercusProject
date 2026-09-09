package test.automation.framework;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.stream.Collectors;
import static test.automation.framework.Config.getClassPath;
import static test.automation.framework.Runner.log;

public final class Data {

    public static String getModel(String model) {
        return Data.class.getName().replace("framework.Data", "models." + model.replace(" ", "."));
    }

    public static String getAsString(String jsonFileName) {
        String filePath = getClassPath("data") + jsonFileName + ".json";
        InputStream inputStream = Data.class.getResourceAsStream("/" + filePath);
        if (inputStream == null) {
            throw new RuntimeException("Json data file not found: " + filePath);
        }
        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(inputStream))) {
            return buffer.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            log().log(Level.WARNING, e.getMessage());
            throw new RuntimeException("Unable to read: " + filePath);
        }
    }

    public static JSONObject getAsJSONObject(String jsonFileName) {
        try {
            return new JSONObject(getAsString(jsonFileName));
        } catch (JSONException e) {
            log().log(Level.WARNING, e.getMessage());
            throw new RuntimeException("Invalid json object" + jsonFileName);
        }
    }

    public static JSONArray getAsJSONArray(String jsonFileName) {
        try {
            return new JSONArray(getAsString(jsonFileName));
        } catch (JSONException e) {
            log().log(Level.WARNING, e.getMessage());
            throw new RuntimeException("Invalid json array" + jsonFileName);
        }
    }

    public static Map<String, Object> getAsMap(String jsonFileName) {
        try {
            return new Gson().fromJson(getAsString(jsonFileName), new TypeToken<Map<String, String>>(){}.getType());
        } catch (JsonSyntaxException e) {
            throw new RuntimeException("Invalid json " + jsonFileName + " for Map");
        }
    }

    public static List<Map<String, Object>> getAsMaps(String jsonFileName) {
        try {
            return new Gson().fromJson(getAsString(jsonFileName), new TypeToken<List<Map<String, String>>>(){}.getType());
        } catch (JsonSyntaxException e) {
            throw new RuntimeException("Invalid json " + jsonFileName + " for list of Map");
        }
    }

    public static Object getAsObject(String jsonFileName, Class objectClass) {
        try {
            return new Gson().fromJson(getAsString(jsonFileName), objectClass);
        } catch (JsonSyntaxException e) {
            log().log(Level.WARNING, e.getMessage());
            throw new RuntimeException("Invalid json " + jsonFileName + " for " + objectClass.getSimpleName());
        }
    }

    public static Object getAsObject(String jsonFileName, String objectClass) {
        try {
            return getAsObject(jsonFileName, Class.forName(getModel(objectClass)));
        } catch (ClassNotFoundException e) {
            log().log(Level.WARNING, e.getMessage());
            throw new RuntimeException("Modal not found for " + objectClass);
        }
    }

    public static Object getAsObject(String jsonAndClassFileName) {
        return getAsObject(jsonAndClassFileName, jsonAndClassFileName);
    }

    public static List<Object> getAsObjects(String jsonFileName, Class objectClass) {
        List<Object> list = new ArrayList<>();
        try {
            getAsJSONArray(jsonFileName).forEach(object -> list.add(new Gson().fromJson(object.toString(), objectClass)));
            return list;
        } catch (JsonSyntaxException e) {
            log().log(Level.WARNING, e.getMessage());
            throw new RuntimeException("Invalid json " + jsonFileName + "for list of " + objectClass.getSimpleName());
        }
    }

    public static List<Object> getAsObjects(String jsonFileName, String objectClass) {
        try {
            return getAsObjects(jsonFileName, Class.forName(getModel(objectClass)));
        } catch (ClassNotFoundException e) {
            log().log(Level.WARNING, e.getMessage());
            throw new RuntimeException("Modal not found for " + objectClass);
        }
    }

    public static List<Object> getAsObjects(String jsonAndClassFileName) {
        return getAsObjects(jsonAndClassFileName, jsonAndClassFileName);
    }
}

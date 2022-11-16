package utilities;

import com.google.gson.Gson;

public class JsonUtilities {
    public static String toJsonString(Object object) {
        return new Gson().toJson(object).toString();
    }
}

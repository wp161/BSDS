package utilities;

import java.nio.charset.Charset;
import java.util.Random;

public class StringUtilities {

    public static String generateRandomString(int size) {
        byte[] array = new byte[size]; // length is bounded by 7
        new Random().nextBytes(array);
        String generatedString = new String(array, Charset.forName("UTF-8"));
        return generatedString;
    }
}

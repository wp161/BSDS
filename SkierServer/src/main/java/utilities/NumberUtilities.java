package utilities;

public class NumberUtilities {
    public static boolean isAnInteger(String num) {
        try {
            Integer.valueOf(num);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isBetween(String num, int min, int max) {
        int s = Integer.valueOf(num);
        return min <= s && s <= max;
    }
}

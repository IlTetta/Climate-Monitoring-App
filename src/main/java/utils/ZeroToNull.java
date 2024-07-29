package utils;

public class ZeroToNull {
    public static Integer zeroToNull(Integer value) {
        if (value == 0) {
            return null;
        }
        return value;
    }
}

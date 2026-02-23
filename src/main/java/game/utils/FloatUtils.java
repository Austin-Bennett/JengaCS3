package game.utils;

public class FloatUtils {

    public static float epsilon = 1e-6f;

    public static boolean flteq(float a, float b) {
        return Math.abs(a - b) <= epsilon;
    }

    public static boolean fltneq(float a, float b) {
        return Math.abs(a-b) > epsilon;
    }

}

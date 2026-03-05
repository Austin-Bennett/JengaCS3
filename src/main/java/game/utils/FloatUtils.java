package game.utils;

public class FloatUtils {

    public static final float epsilon = 1e-6f;
    public static final float PI_2 = (float) Math.PI / 2f;

    public static boolean flteq(float a, float b) {
        return Math.abs(a - b) <= epsilon;
    }

    public static boolean fltneq(float a, float b) {
        return Math.abs(a-b) > epsilon;
    }

}

package app;

/**
 * @author Fabian Bissig, Yannik Inniger, Magdalena Marinkov
 */
public class Util {

    private Util() {}

    public static String addPaddingToBinary(String intermediateResult, int length) {
        return String.format("%" + length + "s", intermediateResult).replace(' ', '0');
    }

    public static String convertToBinaryWithPadding(int value, int length) {
        String binary = Integer.toBinaryString(value);
        return String.format("%" + length + "s", binary).replace(' ', '0');
    }


    public static String xOr(int a, int b) {
        return Integer.toBinaryString(a ^ b);
    }

    public static String xOr(String a, String b) {
        int intA = Integer.parseInt(a, 2);
        int intB = Integer.parseInt(b, 2);
        return Integer.toBinaryString(intA ^ intB);
    }

    public static String[] divideToBlocks(int size, String string) {
        return string.split("(?<=\\G.{" + size + "})"); // matches every n-th character
    }

}

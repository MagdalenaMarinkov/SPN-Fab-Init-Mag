package app;

/**
 * @author Yannik Inniger
 */
public class Util {

    private Util() {}

    public static String addPaddingToBinary(String intermediateResult, int length) {
        return String.format("%" + length + "s", intermediateResult).replace(' ', '0');
    }

    public static String xOr(int a, int b) {
        return Integer.toBinaryString(a ^ b);
    }

    public static String[] divideToBlocks(int size, String string) {
        return string.split("(?<=\\G.{" + size + "})"); // matches every n-th character
    }

}

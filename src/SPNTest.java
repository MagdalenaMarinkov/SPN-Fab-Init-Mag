import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;

/**
 * @author yannikinniger on 21.04.18.
 */
public class SPNTest {

    private static Map<String, String> sBox;
    private static Map<Integer, Integer> bitPermutation;
    private static KeyCalculation keyCalculation;

    @BeforeClass
    public static void init() {
        sBox = new HashMap<>();
        sBox.put("00", "00");
        sBox.put("01", "10");
        sBox.put("10", "01");
        sBox.put("11", "11");

        bitPermutation = new HashMap<>();
        bitPermutation.put(0, 1);
        bitPermutation.put(1, 0);
        bitPermutation.put(2, 3);
        bitPermutation.put(3, 2);

        keyCalculation = key -> {
            int[] keys = new int[3];
            for (int i = 0; i <= 2; ++i) {
                String roundKey = "10" + key.charAt(i) + key.charAt(i);
                keys[i] = Integer.parseInt(roundKey, 2);
            }
            return keys;
        };
    }

    @Test
    public void shouldDivideToBlocks() {
        String text = "1001001010001111";
        String key = "1011101010010100";
        SPN spn = new SPN(text, key, keyCalculation, sBox, bitPermutation);

        int expectedNumberOfBlocks = 4;

        int result = spn.divideToBlocks(4, text).length;
        assertEquals(expectedNumberOfBlocks, result);
    }

    @Test
    public void shouldXor() {
        String text = "0010";
        String key = "010";
        SPN spn = new SPN(text, key, keyCalculation, sBox, bitPermutation);

        String expected = "1010";

        spn.initialStep();
        String result = spn.getIntermediateResult();
        assertEquals(expected, result);
    }

    @Test
    public void doRoundCalculation() {
        String text = "0010";
        String key = "010";
        SPN spn = new SPN(text, key, keyCalculation, sBox, bitPermutation);

        String expected = "0001";

        spn.initialStep();
        spn.roundStep(1);
        String result = spn.getIntermediateResult();
        assertEquals(expected, result);
    }

    @Test
    public void shouldRunThroughSbox() {
        String text = "0010";
        String key = "010";
        SPN spn = new SPN(text, key, keyCalculation, sBox, bitPermutation);

        String expected = "0101";

        spn.initialStep();
        spn.runThroughSBox();
        String result = spn.getIntermediateResult();
        assertEquals(expected, result);
    }

    @Test
    public void shouldDoBitPermutation() {
        String text = "0010";
        String key = "010";
        SPN spn = new SPN(text, key, keyCalculation, sBox, bitPermutation);

        String expected = "1010";

        spn.initialStep();
        spn.runThroughSBox();
        spn.doBitPermitation();
        String result = spn.getIntermediateResult();
        assertEquals(expected, result);
    }

    @Test
    public void shouldCalculateCorrectCipherText() {
        String text = "0010";
        String key = "010";
        SPN spn = new SPN(text, key, keyCalculation, sBox, bitPermutation);

        String expected = "1010";

        spn.initialStep();
        spn.roundStep(1);
        spn.lastStep();

        String result = spn.getIntermediateResult();
        assertEquals(expected, result);
    }

}

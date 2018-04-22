package test;

import org.junit.BeforeClass;
import org.junit.Test;
import spn.KeyCalculation;
import spn.Spn;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;

/**
 * @author yannikinniger on 21.04.18.
 */
public class SpnTest {

    private static Map<String, String> sBox;
    private static Map<Integer, Integer> bitPermutation;
    private static KeyCalculation keyCalculation;
    private static final int NUMBER_OF_ROUNDS = 2;

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

        keyCalculation = (key, numberOfRounds) -> {
            int[] keys = new int[numberOfRounds + 1];
            for (int i = 0; i < numberOfRounds + 1; ++i) {
                String roundKey = "10" + key.charAt(i) + key.charAt(i);
                keys[i] = Integer.parseInt(roundKey, 2);
            }
            return keys;
        };
    }

    @Test
    public void shouldXor() {
        String text = "0010";
        String key = "010";
        int[] keys = keyCalculation.calculateKeys(key, NUMBER_OF_ROUNDS);
        Spn spn = new Spn(key, keyCalculation, NUMBER_OF_ROUNDS, sBox, bitPermutation);

        String expected = "1010";

        String result = spn.initialStep(text, keys[0]);
        assertEquals(expected, result);
    }

    @Test
    public void doRoundCalculation() {
        String text = "0010";
        String key = "010";
        int[] keys = keyCalculation.calculateKeys(key, NUMBER_OF_ROUNDS);
        Spn spn = new Spn(key, keyCalculation, NUMBER_OF_ROUNDS, sBox, bitPermutation);

        String expected = "0001";

        String intermediateResult = spn.initialStep(text, keys[0]);
        String result = spn.roundStep(intermediateResult, keys[1]);
        assertEquals(expected, result);
    }

    @Test
    public void shouldRunThroughSbox() {
        String text = "0010";
        String key = "010";
        int[] keys = keyCalculation.calculateKeys(key, NUMBER_OF_ROUNDS);
        Spn spn = new Spn(key, keyCalculation, NUMBER_OF_ROUNDS, sBox, bitPermutation);

        String expected = "0101";

        String intermediateResult = spn.initialStep(text, keys[0]);
        String result = spn.runThroughSBox(intermediateResult);
        assertEquals(expected, result);
    }

    @Test
    public void shouldDoBitPermutation() {
        String text = "0010";
        String key = "010";
        int[] keys = keyCalculation.calculateKeys(key, NUMBER_OF_ROUNDS);
        Spn spn = new Spn(key, keyCalculation, NUMBER_OF_ROUNDS, sBox, bitPermutation);

        String expected = "1010";

        String intermediateResult = spn.initialStep(text, keys[0]);
        intermediateResult = spn.runThroughSBox(intermediateResult);
        String result = spn.doBitPermutation(intermediateResult);
        assertEquals(expected, result);
    }

    @Test
    public void shouldCalculateCorrectCipherText() {
        String text = "0010";
        String key = "010";
        Spn spn = new Spn(key, keyCalculation, NUMBER_OF_ROUNDS, sBox, bitPermutation);

        String expected = "1010";

        String result = spn.encrypt(text);
        assertEquals(expected, result);
    }

    @Test
    public void shouldDecryptCipherText() {
        String cipherText = "1101";
        String key = "010";
        Spn spn = new Spn(key, keyCalculation, NUMBER_OF_ROUNDS, sBox, bitPermutation);

        String expected = "1001";

        String result = spn.decrypt(cipherText);
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnIncorrectNumberOfKeys() {
        String key = "010";
        KeyCalculation keyCalculation = (k, numberOfRounds) -> {
            return new int[0];
        };
        new Spn(key, keyCalculation, NUMBER_OF_ROUNDS, sBox, bitPermutation);
    }

}

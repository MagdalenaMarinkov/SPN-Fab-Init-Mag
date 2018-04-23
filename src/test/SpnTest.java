package test;

import org.junit.BeforeClass;
import org.junit.Test;
import app.spn.KeyCalculation;
import app.spn.Spn;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;

/**
 * @author Fabian Bissig, Yannik Inniger, Magdalena Marinkov
 */
public class SpnTest {

    private static Map<String, String> sBox;
    private static Map<Integer, Integer> bitPermutation;
    private static KeyCalculation keyCalculation;
    private static final int NUMBER_OF_ROUNDS = 4;

    @BeforeClass
    public static void init() {
        //Sbox initialisieren
        sBox = new HashMap<>();
        sBox.put("0000", "1110");//E
        sBox.put("0001", "0100");
        sBox.put("0010", "1101");//D
        sBox.put("0011", "0001");
        sBox.put("0100", "0010");
        sBox.put("0101", "1111");//F
        sBox.put("0110", "1011");//B
        sBox.put("0111", "1000");
        sBox.put("1000", "0011");
        sBox.put("1001", "1010");//A
        sBox.put("1010", "0110");
        sBox.put("1011", "1100");//B, C
        sBox.put("1100", "0101");//C
        sBox.put("1101", "1001");//D
        sBox.put("1110", "0000");//E
        sBox.put("1111", "0111");//F

        //Bitpermutation initialisieren
        bitPermutation = new HashMap<Integer, Integer>();
        bitPermutation.put(0, 0);
        bitPermutation.put(1, 4);
        bitPermutation.put(2, 8);
        bitPermutation.put(3, 12);//C
        bitPermutation.put(4, 1);
        bitPermutation.put(5, 5);
        bitPermutation.put(6, 9);
        bitPermutation.put(7, 13);//D
        bitPermutation.put(8, 2);
        bitPermutation.put(9, 6);
        bitPermutation.put(10, 10);//A
        bitPermutation.put(11, 14);//B, E
        bitPermutation.put(12, 3);//C
        bitPermutation.put(13, 7);//D
        bitPermutation.put(14, 11);//E, B
        bitPermutation.put(15, 15);//F, F

        keyCalculation = (key, numberOfRounds) -> {
            key = key.replace(" ", "");
            int[] keys = new int[numberOfRounds + 1];
            for (int i = 0; i < numberOfRounds + 1; i += 1) {
                String currentKey = key.substring(i*4,i*4+16);
                keys[i] = Integer.parseInt(currentKey, 2);
            }
            return keys;
        };
    }

    @Test
    public void shouldCalculateFirstRound() {
        String text = "0001001010001111";
        String key = "00010001001010001000110000000000";
        int[] keys = keyCalculation.calculateKeys(key, NUMBER_OF_ROUNDS);
        Spn spn = new Spn(key, keyCalculation, NUMBER_OF_ROUNDS, sBox, bitPermutation);

        String expected = "0000001110100111";

        String result = spn.initialStep(text, keys[0]);
        assertEquals(expected, result);
    }

    @Test
    public void shouldRunThroughSbox() {
        String text = "0001001010001111";
        String key = "00010001001010001000110000000000";
        Spn spn = new Spn(key, keyCalculation, NUMBER_OF_ROUNDS, sBox, bitPermutation);

        String expected = "0100110100110111";

        String result = spn.runThroughSBox(text, sBox);
        assertEquals(expected, result);
    }

    @Test
    public void shouldDoBitPermutation() {
        String text = "0001001010001111";
        String key = "00010001001010001000110000000000";
        Spn spn = new Spn(key, keyCalculation, NUMBER_OF_ROUNDS, sBox, bitPermutation);

        String expected = "0011000101011001";

        String result = spn.doBitPermutation(text);
        assertEquals(expected, result);
    }

    @Test
    public void shouldCalculateCorrectCipherText() {
        String text = "0001001010001111";
        String key = "00010001001010001000110000000000";
        Spn spn = new Spn(key, keyCalculation, NUMBER_OF_ROUNDS, sBox, bitPermutation);

        String expected = "1010111010110100";

        String result = spn.encrypt(text);
        assertEquals(expected, result);
    }

    @Test
    public void shouldDecryptCipherText() {
        String cipherText = "1010111010110100";
        String key = "00010001001010001000110000000000";
        Spn spn = new Spn(key, keyCalculation, NUMBER_OF_ROUNDS, sBox, bitPermutation);

        String expected = "0001001010001111";

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

package app;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import app.spn.KeyCalculation;
import app.spn.Spn;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
/**
 * @author Fabian Bissig, Yannik Inniger, Magdalena Marinkov
 */
public class Main {

    private static final int NUMBER_OF_ROUNDS = 4;

    private static Map<String, String> sBox;
    private static Map<Integer, Integer> bitPermutation;

    private static KeyCalculation keyCalc;

    private static void init() {
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

        keyCalc = (key, numberOfRounds) -> {
            key = key.replace(" ", "");
            int[] keys = new int[numberOfRounds + 1];
            for (int i = 0; i < numberOfRounds + 1; i += 1) {
                String currentKey = key.substring(i*4,i*4+16);
                keys[i] = Integer.parseInt(currentKey, 2);
            }
            return keys;
        };
    }

    public static void main(String[] args) {
        init();
        String text = readFile("res/chiffre.txt");
        String key = "00111010100101001101011000111111";
        CryptoAlgorithm spn = new Spn(key, keyCalc, NUMBER_OF_ROUNDS, sBox, bitPermutation);
        Ctr ctr= new Ctr(spn,16);
        System.out.println(ctr.decrypt(text));
        String encrypted = ctr.encrypt("Gut gemacht!");
        System.out.println(encrypted.endsWith(text));
    }

    private static String readFile(String fileName) {
        StringBuilder sb = new StringBuilder();
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            stream.forEach(sb::append);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

}


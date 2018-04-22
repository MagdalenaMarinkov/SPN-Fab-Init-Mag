import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class Main {

    private static int r = 4;
    private static int n = 4;
    private static int m = 4;
    private static int s = 32;
    private static Map<Integer, Integer> k;

    private static Map<String, String> sBox;
    private static Map<Integer, Integer> bitPer;

    private static KeyCalculation keyCalc;

    private static String x;


    private static void init() {
        //Sbox initialisieren
        sBox = new HashMap<>();
        sBox.put("0", "14");//E
        sBox.put("1", "4");
        sBox.put("2", "13");//D
        sBox.put("3", "1");
        sBox.put("4", "2");
        sBox.put("5", "15");//F
        sBox.put("6", "11");//B
        sBox.put("7", "8");
        sBox.put("8", "3");
        sBox.put("9", "10");//A
        sBox.put("10", "6");
        sBox.put("11", "12");//B, C
        sBox.put("12", "5");//C
        sBox.put("13", "9");//D
        sBox.put("14", "0");//E
        sBox.put("15", "7");//F

        //Bitpermutation initialisieren
        bitPer = new HashMap<Integer, Integer>();
        bitPer.put(0, 0);
        bitPer.put(1, 4);
        bitPer.put(2, 8);
        bitPer.put(3, 12);//C
        bitPer.put(4, 1);
        bitPer.put(5, 5);
        bitPer.put(6, 9);
        bitPer.put(7, 13);//D
        bitPer.put(8, 2);
        bitPer.put(9, 6);
        bitPer.put(10, 10);//A
        bitPer.put(11, 14);//B, E
        bitPer.put(12, 3);//C
        bitPer.put(13, 7);//D
        bitPer.put(14, 11);//E, B
        bitPer.put(15, 15);//F, F

        keyCalc = (key, numberOfRounds) -> {
            key = key.replace(" ", "");
            int[] keys = new int[numberOfRounds + 1];
            for (int i = 0; i < numberOfRounds + 1; i += 1) {
                String currentKey = key.substring(i*4,i*4+4);
                keys[i] = Integer.parseInt(currentKey, 2);
            }
            return keys;
        };
    }

    public static void main(String[] args) {
        init();
        String text = readFile("res/chiffre.txt");
        String key = "0011 1010 1001 0100 1101 0110 0011 1111";
        Spn spn = new Spn(key, keyCalc, r, sBox, bitPer);
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


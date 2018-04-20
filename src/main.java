import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class main {

    private static int r = 4;
    private static int n = 4;
    private static int m = 4;
    private static int s = 32;
    private static Map<Integer, Integer> k;

    private static Map<Integer, Integer> sBox;
    private static Map<Integer, Integer> bitPer;

    private static String x;


    private static void init() {
        //Sbox initialisieren
        sBox = new HashMap<Integer, Integer>();
        sBox.put(0, 14);//E
        sBox.put(1, 4);
        sBox.put(2, 13);//D
        sBox.put(3, 1);
        sBox.put(4, 2);
        sBox.put(5, 15);//F
        sBox.put(6, 11);//B
        sBox.put(7, 8);
        sBox.put(8, 3);
        sBox.put(9, 10);//A
        sBox.put(10, 6);
        sBox.put(11, 12);//B, C
        sBox.put(12, 5);//C
        sBox.put(13, 9);//D
        sBox.put(14, 0);//E
        sBox.put(15, 7);//F

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

        //Rundenschlüssel initialisieren 4i:k=0011 1010 1001 0100 1101 0110 0011 1111
        k = new HashMap<Integer, Integer>();
        k.put(0, 3);//0011
        k.put(1, 10);//1010
        k.put(2, 9);//1001
        k.put(3, 4);//0100
        k.put(4, 13);//1101
        k.put(5, 5);//0110
        k.put(6, 3);//0011
        k.put(7, 15);//1111
    }


    private static void chiffrieren(String x, int numberBlocks) {
        String []blockX =new String [x.length()/numberBlocks];
        byte []blockXInt = new byte[blockX.length];
        byte [] xInital = new byte [1];
        //teile chiffretext in String Blöcke
        for(int i=0; i<blockX.length; i++){
            blockX[i]=x.substring(i*4,i*4+4);
        }

        //teile String-Chiffretext-Blöcke in Byte Blöcke
        for(int i=0; i<blockX.length; i++){
            blockXInt[i] = Byte.parseByte((blockX[i]),2);
        }

        //initialer Weissschritt


        String val1="1100";
        String val2="0000";


      //  blockX[0]=x3[0]^x4[0];
//        for(int i=0; i<x.length; i++){
//            ;
//        }
       // System.out.println(Integer.toString(x1[0],2));
        //reguläreRunde();
    }

    private static void reguläreRunde() {
        abschliessendeRunde();
    }

    private static void abschliessendeRunde() {

    }

    public static void main(String[] args) {
        init();
        String line;
        //TODO mit read from file ersetzten
        x="00000100110100100000101110111000000000101000111110001110011111110110000001010001010000111010000000010011011001110010101110110000";//testdata
        chiffrieren(x, n);

        try {
            BufferedReader in = new BufferedReader(new FileReader("res/chiffre.txt"));
            //TODO: geht noch nicht, zeigt null
            while (((line = in.readLine()) != null)) {
                line = in.readLine();
            }
            System.out.println(line);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

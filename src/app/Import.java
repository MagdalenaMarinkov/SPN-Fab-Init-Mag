package app;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Import {

    private static final String FILENAME = "res/chiffre.txt";

    /*
     * reads first Line of .txt and returns it as a String.
     * @param bitString: content of .txt as a String
     */
    public static String importBitString() throws FileNotFoundException {

        BufferedReader bufferedReader = null;
        FileReader fileReader = null;
        String bitString;

        try {

            fileReader = new FileReader(FILENAME);
            bufferedReader = new BufferedReader(fileReader);

            bufferedReader = new BufferedReader(new FileReader(FILENAME));

            bitString = bufferedReader.readLine();

        } catch (IOException e) {
            e.printStackTrace();
            bitString = "noString";
        } finally {

            try {

                if (bufferedReader != null)
                    bufferedReader.close();

                if (fileReader != null)
                    fileReader.close();

            } catch (IOException ex) {

                ex.printStackTrace();

            }

        }

        return bitString;
    }
}

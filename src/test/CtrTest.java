package test;

import app.CryptoAlgorithm;
import app.Ctr;
import org.junit.BeforeClass;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;


/**
 * @author Fabian Bissig, Yannik Inniger, Magdalena Marinkov
 */
public class CtrTest {

    private static CryptoAlgorithm vernumAlgorithm;
    private static final int BLOCK_SIZE = 4;

    @BeforeClass
    public static void init() {
        vernumAlgorithm = new CryptoAlgorithm() {
            private int key = Integer.parseInt("1000", 2);

            @Override
            public String encrypt(String text) {
                int binary = Integer.parseInt(text, 2);
                return Integer.toBinaryString(binary ^ key);
            }

            @Override
            public String decrypt(String cipherText) {
                int binary = Integer.parseInt(cipherText, 2);
                return Integer.toBinaryString(binary ^ key);
            }
        };
    }

    @Test
    public void shouldEncryptBlocks() {
        String text = "asdf";
        Ctr ctr = new Ctr(vernumAlgorithm, BLOCK_SIZE);
        String encrypted = ctr.encrypt(text);

        String expected = "asdf";

        String result = ctr.decrypt(encrypted);
        assertEquals(expected, result);
    }

    @Test
    public void shouldDecryptString() {
        String cipherText = "0010110010101011111010001011011001111010";
        Ctr ctr = new Ctr(vernumAlgorithm, BLOCK_SIZE);

        String expected = "asdf";

        String result = ctr.decrypt(cipherText);
        assertEquals(expected, result);
    }

}

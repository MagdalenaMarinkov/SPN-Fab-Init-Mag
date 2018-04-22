package java;

import java.spn.Spn;

/**
 * @author Yannik Inniger
 */
public class Ctr {

    private final Spn spn;
    private final int blockSize;

    public Ctr(Spn spn, int blockSize) {
        this.spn = spn;
        this.blockSize = blockSize;
    }

    public String encrypt(String text) {
        String[] blocks = Util.divideToBlocks(blockSize, text);

        return "";
    }

}

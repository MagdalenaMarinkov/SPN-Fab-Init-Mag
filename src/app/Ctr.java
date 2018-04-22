package app;

/**
 * @author Yannik Inniger
 */
public class Ctr {

    private final CryptoAlgorithm algorithm;
    private final int blockSize;

    public Ctr(CryptoAlgorithm algorithm, int blockSize) {
        this.algorithm = algorithm;
        this.blockSize = blockSize;
    }

    public String encrypt(String text) {
        StringBuilder sb = new StringBuilder();
        String[] blocks = Util.divideToBlocks(blockSize, text);

        return sb.toString();
    }

}

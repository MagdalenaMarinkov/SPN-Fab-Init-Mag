package app;

/**
 * @author Yannik Inniger
 */
public class Ctr {

    private final CryptoAlgorithm algorithm;
    private final int blockSize;
    private final int yMinusOne = 10; //1010
    private final int twoRaisedL;
    private final int k = 8;//1000

    public Ctr(CryptoAlgorithm algorithm, int blockSize) {
        this.algorithm = algorithm;
        this.blockSize = blockSize;
        twoRaisedL = (int)Math.pow((double)2 , (double)blockSize);
    }

    public String encrypt(String text) {
        StringBuilder sb = new StringBuilder();
        String[] blocks = Util.divideToBlocks(blockSize, text);

        String result;
        for (int i = 0; i < blocks.length; i++) {
            result=(Util.xOr(((yMinusOne+1) % twoRaisedL) ^ k,Integer.parseInt(blocks[i],2)));
            sb.append(algorithm.encrypt(result));
        }

        return sb.toString();
    }

    public String decrypt(String text) {
        StringBuilder sb = new StringBuilder();
        String[] blocks = Util.divideToBlocks(blockSize, text);

        String result;
        for (int i = 0; i < blocks.length; i++) {
            result=(Util.xOr(((yMinusOne+1) % twoRaisedL) ^ k,Integer.parseInt(blocks[i],2)));
            sb.append(algorithm.decrypt(result));
        }

        return sb.toString();
    }

}

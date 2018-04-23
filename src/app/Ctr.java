package app;

/**
 * @author Fabian Bissig, Yannik Inniger, Magdalena Marinkov
 */
public class Ctr {

    private final CryptoAlgorithm algorithm;
    private final int blockSize;
    private final int yMinusOne = 65536; //1010 //TODO:random generate
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
        sb.append(Integer.toBinaryString(yMinusOne));
        for (int i = 0; i < blocks.length; i++) {
            String param = Integer.toBinaryString((yMinusOne+i) % twoRaisedL);
            result= algorithm.encrypt(param);
            String yi=Util.xOr(Integer.parseInt(result,2),Integer.parseInt(blocks[i],2));
            sb.append(Util.addPaddingToBinary(yi,blockSize));
        }

        return sb.toString();
    }

    public String decrypt(String text) {
        StringBuilder sb = new StringBuilder();
        String[] blocks = Util.divideToBlocks(blockSize, text);

        String result;
        for (int i = 0; i < blocks.length; i++) {

            String param = Util.addPaddingToBinary(Integer.toBinaryString((yMinusOne+i) % twoRaisedL), blockSize);
            result= algorithm.decrypt(param);
            String xi=Util.xOr(Integer.parseInt(result,2),Integer.parseInt(blocks[i],2));
            sb.append(Util.addPaddingToBinary(xi,blockSize));
        }

        return sb.toString();
    }

}

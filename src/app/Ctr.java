package app;

import java.util.Random;

/**
 * @author Fabian Bissig, Yannik Inniger, Magdalena Marinkov
 */
public class Ctr {

    private static final int BASE = 2;
    private static final Random RANDOM = new Random();

    private final CryptoAlgorithm algorithm;
    private final int blockSize;
    private final int twoRaisedL;

    public Ctr(CryptoAlgorithm algorithm, int blockSize) {
        this.algorithm = algorithm;
        this.blockSize = blockSize;
        twoRaisedL = (int) Math.pow(BASE, (double) blockSize);
    }

    public String encrypt(String text) {
        String binaryText = convertStringToBinary(text);

        int yMinusOne = RANDOM.nextInt((int) Math.pow(BASE, blockSize));
        //yMinusOne = Integer.parseInt("0000010011010010", 2);
        StringBuilder sb = new StringBuilder();
        String[] blocks = Util.divideToBlocks(blockSize, binaryText);
        sb.append(Util.convertToBinaryWithPadding(yMinusOne, blockSize));

        sb.append(iterateOverBlocks(blocks, yMinusOne));
        return sb.toString();
    }

    public String decrypt(String text) {
        int yMinusOne = Integer.parseInt(text.substring(0, blockSize), 2);
        text = text.substring(blockSize, text.length());
        String[] blocks = Util.divideToBlocks(blockSize, text);

        String result = iterateOverBlocks(blocks, yMinusOne);
        if (result.length() % 8 != 0) {
            result = removePadding(result);
        }
        return convertBinaryToString(result);
    }

    private String iterateOverBlocks(String[] blocks, int yMinusOne) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < blocks.length; i++) {
            String toEncrypt = Util.convertToBinaryWithPadding((yMinusOne + i) % twoRaisedL, blockSize);
            String toXor = algorithm.encrypt(toEncrypt);
            String result = Util.xOr(toXor, blocks[i]);
            sb.append(Util.addPaddingToBinary(result, blockSize));
        }
        return sb.toString();
    }

    private String convertStringToBinary(String text) {
        StringBuilder binarySb = new StringBuilder();
        for (char c : text.toCharArray()) {
            binarySb.append(Util.addPaddingToBinary(Integer.toBinaryString((int)c), 8));
        }

        binarySb.append("1");
        while (binarySb.length() % blockSize != 0) {
            binarySb.append("0");
        }
        return binarySb.toString();
    }

    private String convertBinaryToString(String text) {
        StringBuilder asciiSb = new StringBuilder();
        if (text.length() % 8 == 0) {
            String[] asciiText = Util.divideToBlocks(8, text);
            for (String asciiChar : asciiText) {
                asciiSb.append((char)Integer.parseInt(asciiChar, 2));
            }
            return asciiSb.toString();
        } else {
            throw new IllegalArgumentException("text not dividable through 8");
        }
    }

    private String removePadding(String text) {
        int paddingStart = text.lastIndexOf("1");
        return text.substring(0, paddingStart);
    }

}

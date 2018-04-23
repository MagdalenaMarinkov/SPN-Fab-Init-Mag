package app.spn;

import app.CryptoAlgorithm;
import app.Util;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Fabian Bissig, Yannik Inniger, Magdalena Marinkov
 */
public class Spn implements CryptoAlgorithm {

    private int[] keys;
    private int numberOfRounds;
    private Map<String, String> sBox;
    private Map<String, String> sBoxInv;
    private Map<Integer, Integer> bitPermutation;

    public Spn(String key, KeyCalculation keyCalc, int numberOfRounds, Map<String, String> sBox, Map<Integer, Integer> bitPermutation) {
        this.keys = keyCalc.calculateKeys(key, numberOfRounds);
        if (this.keys.length != numberOfRounds + 1) {
            throw new IllegalArgumentException("The amount of keys is incorrect");
        }
        this.numberOfRounds = numberOfRounds;
        this.sBox = sBox;
        this.bitPermutation = bitPermutation;
    }

    @Override
    public String encrypt(String text) {
        return doRounds(text, keys, sBox);
    }

    @Override
    public String decrypt(String cipherText) {
        this.sBoxInv = getSBoxInv();
        int[] decryptionKeys = getDecryptionKeys();
        return doRounds(cipherText, decryptionKeys, sBoxInv);
    }

    private String doRounds(String text, int[] keys, Map<String, String> sBox) {
        String intermediateResult = this.initialStep(text, keys[0]);
        for (int i = 1; i < numberOfRounds; i += 1) {
            intermediateResult = this.roundStep(intermediateResult, keys[i], sBox);
        }
        intermediateResult = this.lastStep(intermediateResult, keys[keys.length - 1], sBox);
        return intermediateResult;
    }

    public String initialStep(String text, int key) {
        int binaryText = Integer.parseInt(text, 2);
        String intermediateResult = Integer.toBinaryString(binaryText ^ key);
        return Util.addPaddingToBinary(intermediateResult, text.length());
    }

    public String lastStep(String intermediateResult, int key, Map<String, String> sBox) {
        int resultLength = intermediateResult.length();
        intermediateResult = runThroughSBox(intermediateResult, sBox);
        intermediateResult = Util.xOr(Integer.parseInt(intermediateResult, 2), key);

        return Util.addPaddingToBinary(intermediateResult, resultLength);
    }

    public String roundStep(String intermediateResult, int key, Map<String, String> sBox) {
        int resultLength = intermediateResult.length();
        intermediateResult = runThroughSBox(intermediateResult, sBox);
        intermediateResult = doBitPermutation(intermediateResult);
        intermediateResult = Util.xOr(Integer.parseInt(intermediateResult, 2), key);

        return Util.addPaddingToBinary(intermediateResult, resultLength);
    }

    public String runThroughSBox(String intermediateResult, Map<String, String> sBox) {
        int sBoxLength = sBox.keySet().iterator().next().length();

        String[] dividedBinary = Util.divideToBlocks(sBoxLength, intermediateResult);
        StringBuilder sb = new StringBuilder();
        for (String block : dividedBinary) {
            sb.append(sBox.get(block));
        }

        return sb.toString();
    }

    public String doBitPermutation(String intermediateResult) {
        char[] bitArray = intermediateResult.toCharArray();
        char[] permutedBitArray = new char[intermediateResult.length()];
        for (int i = 0; i < bitArray.length; i += 1) {
            int newPosition = bitPermutation.get(i);
            permutedBitArray[newPosition] = bitArray[i];
        }

        return new String(permutedBitArray);
    }

    private Map<String, String> getSBoxInv() {
        return sBox.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
    }

    private int[] getDecryptionKeys() {
        int[] decryptionKeys = new int[keys.length];
        int j = keys.length;
        for (int i = 0; i < keys.length; i += 1) {
            decryptionKeys[i] = keys[j -= 1];
        }
        for (int i = 1; i < decryptionKeys.length - 1; i += 1) {
            String key = Util.addPaddingToBinary(Integer.toBinaryString(decryptionKeys[i]), bitPermutation.keySet().size());
            decryptionKeys[i] = Integer.parseInt(doBitPermutation(key), 2);
        }
        return decryptionKeys;
    }
}

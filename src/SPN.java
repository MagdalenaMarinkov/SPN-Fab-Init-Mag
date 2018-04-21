import java.util.Map;

/**
 * @author yannikinniger on 21.04.18.
 */
public class SPN {

    private String text;
    private String intermediateResult;
    private int[] keys;
    private Map<String, String> sBox;
    private Map<Integer, Integer> bitPermutation;

    public SPN(String text, String key, KeyCalculation keyCalc, Map<String, String> sBox, Map<Integer, Integer> bitPermutation) {
        this.text = text;
        this.keys = keyCalc.calculateKeys(key);
        this.sBox = sBox;
        this.bitPermutation = bitPermutation;
    }

    public String[] divideToBlocks(int size, String string) {
        return string.split("(?<=\\G.{" + size + "})"); // matches every n-th character
    }

    public void initialStep() {
        int binaryText = Integer.parseInt(this.text, 2);
        setIntermediateResult(Integer.toBinaryString(binaryText ^ keys[0]));
    }

    public void lastStep() {
        runThroughSBox();
        setIntermediateResult(xOr(Integer.parseInt(intermediateResult, 2), keys[keys.length - 1]));
    }

    public void roundStep(int roundNumber) {
        runThroughSBox();
        doBitPermitation();
        setIntermediateResult(xOr(Integer.parseInt(intermediateResult, 2), keys[roundNumber]));
    }

    public void runThroughSBox() {
        int sBoxLength = sBox.keySet().iterator().next().length();

        String[] dividedBinary = divideToBlocks(sBoxLength, intermediateResult);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < dividedBinary.length; i += 1) {
            sb.append(sBox.get(dividedBinary[i]));
        }

        setIntermediateResult(sb.toString());
    }

    public void doBitPermitation() {
        char[] bitArray = intermediateResult.toCharArray();
        char[] permutedBitArray = new char[intermediateResult.length()];
        for(int i = 0; i < bitArray.length; i += 1) {
            int newPosition = bitPermutation.get(i);
            permutedBitArray[newPosition] = bitArray[i];
        }

        setIntermediateResult(new String(permutedBitArray));
    }

    private String xOr(int a, int b) {
        return Integer.toBinaryString(a ^ b);
    }

    public String getIntermediateResult() {
        return intermediateResult;
    }

    private void setIntermediateResult(String intermediateResult) {
        int length = text.length();
        this.intermediateResult = String.format("%" + length + "s", intermediateResult).replace(' ', '0');
    }
}

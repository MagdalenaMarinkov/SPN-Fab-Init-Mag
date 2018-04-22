package java;

/**
 * @author Yannik Inniger
 */
public interface CryptoAlgorithm {

    String encrypt(String text);

    String decrypt(String cipherText);

}

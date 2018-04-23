package app;

/**
 * @author Fabian Bissig, Yannik Inniger, Magdalena Marinkov
 */
public interface CryptoAlgorithm {

    String encrypt(String text);

    String decrypt(String cipherText);

}

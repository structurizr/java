package com.structurizr.encryption;

import org.junit.Test;

import javax.crypto.BadPaddingException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class AesEncryptionStrategyTests {

    private AesEncryptionStrategy strategy;

    @Test
    public void test_encrypt_EncryptsPlaintext() throws Exception {
        strategy = new AesEncryptionStrategy(128, 1000, "password");

        String ciphertext = strategy.encrypt("Hello world");
        assertNotEquals("Hello world", ciphertext);
    }

    @Test
    public void test_decrypt_decryptsTheCiphertext_WhenTheSameStrategyInstanceIsUsed() throws Exception {
        strategy = new AesEncryptionStrategy(128, 1000, "password");

        String ciphertext = strategy.encrypt("Hello world");
        assertEquals("Hello world", strategy.decrypt(ciphertext));
    }

    @Test
    public void test_decrypt_decryptsTheCiphertext_WhenTheSameConfigurationIsUsed() throws Exception {
        strategy = new AesEncryptionStrategy(128, 1000, "password");

        String ciphertext = strategy.encrypt("Hello world");

        strategy = new AesEncryptionStrategy(strategy.getKeySize(), strategy.getIterationCount(), strategy.getSalt(), strategy.getIv(), "password");
        assertEquals("Hello world", strategy.decrypt(ciphertext));
    }

    @Test(expected = BadPaddingException.class)
    public void test_decrypt_doesNotDecryptTheCiphertext_WhenTheIncorrectKeySizeIsUsed() throws Exception {
        strategy = new AesEncryptionStrategy(128, 1000, "password");

        String ciphertext = strategy.encrypt("Hello world");

        strategy = new AesEncryptionStrategy(256, strategy.getIterationCount(), strategy.getSalt(), strategy.getIv(), "password");
        strategy.decrypt(ciphertext);
    }

    @Test(expected = BadPaddingException.class)
    public void test_decrypt_doesNotDecryptTheCiphertext_WhenTheIncorrectIterationCountIsUsed() throws Exception {
        strategy = new AesEncryptionStrategy(128, 1000, "password");

        String ciphertext = strategy.encrypt("Hello world");

        strategy = new AesEncryptionStrategy(strategy.getKeySize(), 2000, strategy.getSalt(), strategy.getIv(), "password");
        strategy.decrypt(ciphertext);
    }

    @Test(expected = BadPaddingException.class)
    public void test_decrypt_doesNotDecryptTheCiphertext_WhenTheIncorrectSaltIsUsed() throws Exception {
        strategy = new AesEncryptionStrategy(128, 1000, "password");

        String ciphertext = strategy.encrypt("Hello world");

        strategy = new AesEncryptionStrategy(strategy.getKeySize(), strategy.getIterationCount(), "133D30C2A658B3081279A97FD3B1F7CDE10C4FB61D39EEA8", strategy.getIv(), "password");
        strategy.decrypt(ciphertext);
    }

    @Test(expected = BadPaddingException.class)
    public void test_decrypt_doesNotDecryptTheCiphertext_WhenTheIncorrectIvIsUsed() throws Exception {
        strategy = new AesEncryptionStrategy(128, 1000, "password");

        String ciphertext = strategy.encrypt("Hello world");

        strategy = new AesEncryptionStrategy(strategy.getKeySize(), strategy.getIterationCount(), strategy.getSalt(), "1DED89E4FB15F61DC6433E3BADA4A891", "password");
        strategy.decrypt(ciphertext);
    }

    @Test(expected = BadPaddingException.class)
    public void test_decrypt_doesNotDecryptTheCiphertext_WhenTheIncorrectPassphraseIsUsed() throws Exception {
        strategy = new AesEncryptionStrategy(128, 1000, "password");

        String ciphertext = strategy.encrypt("Hello world");

        strategy = new AesEncryptionStrategy(strategy.getKeySize(), strategy.getIterationCount(), strategy.getSalt(), strategy.getIv(), "The Wrong Password");
        strategy.decrypt(ciphertext);
    }

}

package com.structurizr.encryption;

import org.junit.jupiter.api.Test;

import javax.crypto.BadPaddingException;
import java.security.InvalidKeyException;

import static org.junit.jupiter.api.Assertions.*;

public class AesEncryptionStrategyTests {

    @Test
    void test_encrypt_EncryptsPlaintext() throws Exception {
        AesEncryptionStrategy strategy = new AesEncryptionStrategy(128, 1000, "06DC30A48ADEEE72D98E33C2CEAEAD3E", "ED124530AF64A5CAD8EF463CF5628434", "password");

        String ciphertext = strategy.encrypt("Hello world");
        assertEquals("A/DzjV17WVS6ZAKsLOaC/Q==", ciphertext);
    }

    @Test
    void test_decrypt_decryptsTheCiphertext_WhenTheSameStrategyInstanceIsUsed() throws Exception {
        AesEncryptionStrategy strategy = new AesEncryptionStrategy(128, 1000, "password");

        String ciphertext = strategy.encrypt("Hello world");
        assertEquals("Hello world", strategy.decrypt(ciphertext));
    }

    @Test
    void test_decrypt_decryptsTheCiphertext_WhenTheSameConfigurationIsUsed() throws Exception {
        AesEncryptionStrategy strategy = new AesEncryptionStrategy(128, 1000, "password");

        String ciphertext = strategy.encrypt("Hello world");

        strategy = new AesEncryptionStrategy(strategy.getKeySize(), strategy.getIterationCount(), strategy.getSalt(), strategy.getIv(), "password");
        assertEquals("Hello world", strategy.decrypt(ciphertext));
    }

    @Test
    void test_decrypt_doesNotDecryptTheCiphertext_WhenTheIncorrectKeySizeIsUsed() throws Exception {
        try {
            AesEncryptionStrategy strategy = new AesEncryptionStrategy(128, 1000, "password");

            String ciphertext = strategy.encrypt("Hello world");

            strategy = new AesEncryptionStrategy(256, strategy.getIterationCount(), strategy.getSalt(), strategy.getIv(), "password");
            strategy.decrypt(ciphertext);
        } catch (BadPaddingException | InvalidKeyException bpe) {
            // BadPaddingException is thrown on Mac and Linux
            // InvalidKeyException is thrown in Windows
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void test_decrypt_doesNotDecryptTheCiphertext_WhenTheIncorrectIterationCountIsUsed() throws Exception {
        assertThrows(BadPaddingException.class, () -> {
            AesEncryptionStrategy strategy = new AesEncryptionStrategy(128, 1000, "password");

            String ciphertext = strategy.encrypt("Hello world");

            strategy = new AesEncryptionStrategy(strategy.getKeySize(), 2000, strategy.getSalt(), strategy.getIv(), "password");
            strategy.decrypt(ciphertext);
        });
    }

    @Test
    void test_decrypt_doesNotDecryptTheCiphertext_WhenTheIncorrectSaltIsUsed() throws Exception {
        assertThrows(BadPaddingException.class, () -> {
            AesEncryptionStrategy strategy = new AesEncryptionStrategy(128, 1000, "password");

            String ciphertext = strategy.encrypt("Hello world");

            strategy = new AesEncryptionStrategy(strategy.getKeySize(), strategy.getIterationCount(), "133D30C2A658B3081279A97FD3B1F7CDE10C4FB61D39EEA8", strategy.getIv(), "password");
            strategy.decrypt(ciphertext);
        });
    }

    @Test
    void test_decrypt_doesNotDecryptTheCiphertext_WhenTheIncorrectIvIsUsed() throws Exception {
        assertThrows(BadPaddingException.class, () -> {
            AesEncryptionStrategy strategy = new AesEncryptionStrategy(128, 1000, "password");

            String ciphertext = strategy.encrypt("Hello world");

            strategy = new AesEncryptionStrategy(strategy.getKeySize(), strategy.getIterationCount(), strategy.getSalt(), "1DED89E4FB15F61DC6433E3BADA4A891", "password");
            strategy.decrypt(ciphertext);
        });
    }

    @Test
    void test_decrypt_doesNotDecryptTheCiphertext_WhenTheIncorrectPassphraseIsUsed() throws Exception {
        assertThrows(BadPaddingException.class, () -> {
            AesEncryptionStrategy strategy = new AesEncryptionStrategy(128, 1000, "password");

            String ciphertext = strategy.encrypt("Hello world");

            strategy = new AesEncryptionStrategy(strategy.getKeySize(), strategy.getIterationCount(), strategy.getSalt(), strategy.getIv(), "The Wrong Password");
            strategy.decrypt(ciphertext);
        });
    }

}

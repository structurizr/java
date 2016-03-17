package com.structurizr.encryption;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

/**
 * This is implementation of an AES encryption strategy, allowing you to specify the
 * key size, iteration count and passphrase.
 */
public class AesEncryptionStrategy extends EncryptionStrategy {

    private static final String CIPHER_SPECIFICATION = "AES/CBC/PKCS5PADDING";
    private static final int INITIALIZATION_VECTOR_SIZE_IN_BYTES = 16;

    private int keySize;
    private int iterationCount;
    private String salt;
    private String iv;

    AesEncryptionStrategy() {
    }

    public AesEncryptionStrategy(String passphrase) {
        this(128, 1000, passphrase);
    }

    public AesEncryptionStrategy(int keySize, int iterationCount, String passphrase) {
        super(passphrase);

        this.keySize = keySize;
        this.iterationCount = iterationCount;

        byte[] saltAsBytes = new byte[keySize / 8];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(saltAsBytes);
        this.salt = DatatypeConverter.printHexBinary(saltAsBytes);

        byte[] ivAsBytes = new byte[INITIALIZATION_VECTOR_SIZE_IN_BYTES];
        SecureRandom prng = new SecureRandom();
        prng.nextBytes(ivAsBytes);
        this.iv = DatatypeConverter.printHexBinary(ivAsBytes);
    }

    public AesEncryptionStrategy(int keySize, int iterationCount, String salt, String iv, String passphrase) {
        super(passphrase);

        this.keySize = keySize;
        this.iterationCount = iterationCount;
        this.salt = salt;
        this.iv = iv;
    }

    public String encrypt(String plaintext) throws Exception {
        SecretKey secretKey = createSecretKey();

        Cipher cipher = Cipher.getInstance(CIPHER_SPECIFICATION);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(DatatypeConverter.parseHexBinary(iv)));

        byte[] byteDataToEncrypt = plaintext.getBytes();
        byte[] byteCipherText = cipher.doFinal(byteDataToEncrypt);

        return Base64.getEncoder().encodeToString(byteCipherText);
    }

    public String decrypt(String ciphertext) throws Exception {
        SecretKey secretKey = createSecretKey();

        Cipher cipher = Cipher.getInstance(CIPHER_SPECIFICATION);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(DatatypeConverter.parseHexBinary(iv)));
        byte[] unencrypted = cipher.doFinal(Base64.getDecoder().decode(ciphertext));

        return new String(unencrypted, "UTF-8");
    }

    private SecretKey createSecretKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        KeySpec spec = new PBEKeySpec(getPassphrase().toCharArray(), DatatypeConverter.parseHexBinary(salt), iterationCount, keySize);
        return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
    }

    public int getKeySize() {
        return keySize;
    }

    public int getIterationCount() {
        return iterationCount;
    }

    public String getSalt() {
        return salt;
    }

    public String getIv() {
        return iv;
    }

}

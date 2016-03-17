package com.structurizr.encryption;

public class MockEncryptionStrategy extends EncryptionStrategy {

    @Override
    public String encrypt(String plaintext) throws Exception {
        return new StringBuilder(plaintext).reverse().toString();
    }

    @Override
    public String decrypt(String ciphertext) throws Exception {
        return new StringBuilder(ciphertext).reverse().toString();
    }

}

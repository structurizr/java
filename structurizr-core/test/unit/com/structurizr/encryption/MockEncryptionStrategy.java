package com.structurizr.encryption;

public class MockEncryptionStrategy implements EncryptionStrategy {

    @Override
    public String getPassphrase() {
        return null;
    }

    @Override
    public void setPassphrase(String passphrase) {
    }

    @Override
    public String encrypt(String plaintext) throws Exception {
        return new StringBuilder(plaintext).reverse().toString();
    }

    @Override
    public String decrypt(String ciphertext) throws Exception {
        return new StringBuilder(ciphertext).reverse().toString();
    }

    @Override
    public EncryptionLocation getLocation() {
        return EncryptionLocation.Client;
    }

    @Override
    public void setLocation(EncryptionLocation location) {
    }

}

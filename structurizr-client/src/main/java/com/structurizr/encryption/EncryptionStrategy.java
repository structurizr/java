package com.structurizr.encryption;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Superclass for all encryption strategies.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value=AesEncryptionStrategy.class, name="aes")
})
public abstract class EncryptionStrategy {

    private String passphrase = "";
    private EncryptionLocation location = EncryptionLocation.Client;

    protected EncryptionStrategy() {
    }

    protected EncryptionStrategy(String passphrase) {
        this.passphrase = passphrase;
    }

    @JsonIgnore // we definitely do not want this in the JSON!
    public String getPassphrase() {
        return passphrase;
    }

    public void setPassphrase(String passphrase) {
        this.passphrase = passphrase;
    }

    public EncryptionLocation getLocation() {
        return location;
    }

    public void setLocation(EncryptionLocation location) {
        this.location = location;
    }

    public abstract String encrypt(String plaintext) throws Exception;

    public abstract String decrypt(String ciphertext) throws Exception;

}
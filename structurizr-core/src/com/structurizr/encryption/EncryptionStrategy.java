package com.structurizr.encryption;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value=AesEncryptionStrategy.class, name="aes")
})
public interface EncryptionStrategy {

    public String getPassphrase();

    public void setPassphrase(String passphrase);

    public String encrypt(String plaintext) throws Exception;

    public String decrypt(String ciphertext) throws Exception;

    public EncryptionLocation getLocation();

    public void setLocation(EncryptionLocation location);

}


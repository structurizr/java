package com.structurizr.encryption;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.structurizr.AbstractWorkspace;
import com.structurizr.Workspace;
import com.structurizr.io.json.JsonReader;
import com.structurizr.io.json.JsonWriter;

import java.io.StringReader;
import java.io.StringWriter;

/**
 * This is a wrapper around an existing workspace that has been encrypted.
 */
public final class EncryptedWorkspace extends AbstractWorkspace {

    private Workspace workspace;
    private String ciphertext;
    private String plaintext;

    private EncryptionStrategy encryptionStrategy;

    EncryptedWorkspace() {
    }

    /**
     * Creates an encrypted version of the specified workspace.
     *
     * @param workspace             the Workspace to encrypt
     * @param encryptionStrategy    the encryption strategy
     * @throws Exception            if an error occurs while creating the encrypted workspace
     */
    public EncryptedWorkspace(Workspace workspace, EncryptionStrategy encryptionStrategy) throws Exception {
        JsonWriter jsonWriter = new JsonWriter(false);
        StringWriter stringWriter = new StringWriter();
        jsonWriter.write(workspace, stringWriter);

        init(workspace, stringWriter.toString(), encryptionStrategy);
    }

    public EncryptedWorkspace(Workspace workspace, String plaintext, EncryptionStrategy encryptionStrategy) throws Exception {
        init(workspace, plaintext, encryptionStrategy);
    }

    private void init(Workspace workspace, String plaintext, EncryptionStrategy encryptionStrategy) throws Exception {
        this.workspace = workspace;

        setId(workspace.getId());
        setName(workspace.getName());
        setDescription(workspace.getDescription());
        setVersion(workspace.getVersion());
        setThumbnail(workspace.getThumbnail());
        setApi(workspace.getApi());

        this.plaintext = plaintext;
        this.ciphertext = encryptionStrategy.encrypt(plaintext);
        this.encryptionStrategy = encryptionStrategy;
    }

    @JsonIgnore
    public Workspace getWorkspace() throws Exception {
        if (this.workspace != null) {
            return this.workspace;
        } else if (this.ciphertext != null) {
            this.plaintext = encryptionStrategy.decrypt(ciphertext);
            JsonReader jsonReader = new JsonReader();
            StringReader stringReader = new StringReader(plaintext);
            return jsonReader.read(stringReader);
        } else {
            return null;
        }
    }

    public String getCiphertext() {
        return ciphertext;
    }

    public void setCiphertext(String ciphertext) {
        this.ciphertext = ciphertext;
    }

    @JsonIgnore
    public String getPlaintext() throws Exception {
        if (this.plaintext != null) {
            return this.plaintext;
        } else {
            return encryptionStrategy.decrypt(ciphertext);
        }
    }

    public EncryptionStrategy getEncryptionStrategy() {
        return encryptionStrategy;
    }

    public void setEncryptionStrategy(EncryptionStrategy encryptionStrategy) {
        this.encryptionStrategy = encryptionStrategy;
    }

}

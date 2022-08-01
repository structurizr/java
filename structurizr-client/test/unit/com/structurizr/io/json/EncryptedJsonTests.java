package com.structurizr.io.json;

import com.structurizr.Workspace;
import com.structurizr.encryption.AesEncryptionStrategy;
import com.structurizr.encryption.EncryptedWorkspace;
import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EncryptedJsonTests {

    @Test
    void test_write_and_read() throws Exception {
        final Workspace workspace1 = new Workspace("Name", "Description");

        // output the model as JSON
        EncryptedJsonWriter jsonWriter = new EncryptedJsonWriter(true);
        AesEncryptionStrategy encryptionStrategy = new AesEncryptionStrategy("password");
        final EncryptedWorkspace encryptedWorkspace1 = new EncryptedWorkspace(workspace1, encryptionStrategy);
        StringWriter stringWriter = new StringWriter();
        jsonWriter.write(encryptedWorkspace1, stringWriter);

        // and read it back again
        EncryptedJsonReader jsonReader = new EncryptedJsonReader();
        StringReader stringReader = new StringReader(stringWriter.toString());
        final EncryptedWorkspace encryptedWorkspace2 = jsonReader.read(stringReader);
        assertEquals("Name", encryptedWorkspace2.getName());
        assertEquals("Description", encryptedWorkspace2.getDescription());

        encryptedWorkspace2.getEncryptionStrategy().setPassphrase(encryptionStrategy.getPassphrase());
        final Workspace workspace2 = encryptedWorkspace2.getWorkspace();
        assertEquals("Name", workspace2.getName());
        assertEquals("Description", workspace2.getDescription());
    }

}
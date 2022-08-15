package com.structurizr.io.json;

import com.structurizr.Workspace;
import com.structurizr.encryption.AesEncryptionStrategy;
import com.structurizr.encryption.EncryptedWorkspace;
import org.junit.jupiter.api.Test;

import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class EncryptedJsonWriterTests {

    @Test
    void write_ThrowsAnIllegalArgumentException_WhenANullEncryptedWorkspaceIsSpecified() throws Exception {
        try {
            EncryptedJsonWriter writer = new EncryptedJsonWriter(true);
            writer.write(null, new StringWriter());
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("EncryptedWorkspace cannot be null.", e.getMessage());
        }
    }

    @Test
    void write_ThrowsAnIllegalArgumentException_WhenANullWriterIsSpecified() throws Exception {
        try {
            EncryptedJsonWriter writer = new EncryptedJsonWriter(true);
            Workspace workspace = new Workspace("Name", "Description");
            EncryptedWorkspace encryptedWorkspace = new EncryptedWorkspace(workspace, new AesEncryptionStrategy("password"));
            writer.write(encryptedWorkspace, null);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Writer cannot be null.", e.getMessage());
        }
    }

}

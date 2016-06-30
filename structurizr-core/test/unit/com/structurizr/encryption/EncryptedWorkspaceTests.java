package com.structurizr.encryption;

import com.structurizr.Workspace;
import com.structurizr.io.json.JsonWriter;
import org.junit.Before;
import org.junit.Test;

import java.io.StringWriter;

import static junit.framework.TestCase.assertSame;
import static org.junit.Assert.assertEquals;

public class EncryptedWorkspaceTests {

    private EncryptedWorkspace encryptedWorkspace;
    private Workspace workspace;
    private EncryptionStrategy encryptionStrategy;

    @Before
    public void setUp() throws Exception {
        workspace = new Workspace("Name", "Description");
        workspace.setThumbnail("thumbnail data");
        workspace.setId(1234);

        encryptionStrategy = new MockEncryptionStrategy();
        encryptedWorkspace = new EncryptedWorkspace(workspace, encryptionStrategy);
    }

    @Test
    public void test_construction_WhenTwoParametersAreSpecified() throws Exception {
        encryptedWorkspace = new EncryptedWorkspace(workspace, encryptionStrategy);

        assertEquals("Name", encryptedWorkspace.getName());
        assertEquals("Description", encryptedWorkspace.getDescription());
        assertEquals("thumbnail data", encryptedWorkspace.getThumbnail());
        assertEquals(1234, encryptedWorkspace.getId());

        assertSame(workspace, encryptedWorkspace.getWorkspace());
        assertSame(encryptionStrategy, encryptedWorkspace.getEncryptionStrategy());

        JsonWriter jsonWriter = new JsonWriter(false);
        StringWriter stringWriter = new StringWriter();
        jsonWriter.write(workspace, stringWriter);

        assertEquals(stringWriter.toString(), encryptedWorkspace.getPlaintext());
        assertEquals(encryptionStrategy.encrypt(stringWriter.toString()), encryptedWorkspace.getCiphertext());
    }

    @Test
    public void test_construction_WhenThreeParametersAreSpecified() throws Exception {
        JsonWriter jsonWriter = new JsonWriter(false);
        StringWriter stringWriter = new StringWriter();
        jsonWriter.write(workspace, stringWriter);

        encryptedWorkspace = new EncryptedWorkspace(workspace, stringWriter.toString(), encryptionStrategy);

        assertEquals("Name", encryptedWorkspace.getName());
        assertEquals("Description", encryptedWorkspace.getDescription());
        assertEquals("thumbnail data", encryptedWorkspace.getThumbnail());
        assertEquals(1234, encryptedWorkspace.getId());

        assertSame(workspace, encryptedWorkspace.getWorkspace());
        assertSame(encryptionStrategy, encryptedWorkspace.getEncryptionStrategy());

        assertEquals(stringWriter.toString(), encryptedWorkspace.getPlaintext());
        assertEquals(encryptionStrategy.encrypt(stringWriter.toString()), encryptedWorkspace.getCiphertext());
    }

    @Test
    public void test_getPlaintext_ReturnsTheDecryptedVersionOfTheCiphertext() throws Exception {
        String cipherText = encryptedWorkspace.getCiphertext();

        encryptedWorkspace = new EncryptedWorkspace();
        encryptedWorkspace.setEncryptionStrategy(encryptionStrategy);
        encryptedWorkspace.setCiphertext(cipherText);

        assertEquals(new StringBuilder(cipherText).reverse().toString(), encryptedWorkspace.getPlaintext());
    }

    @Test
    public void test_getWorkspace_ReturnsTheWorkspace_WhenACipherextIsSpecified() throws Exception {
        JsonWriter jsonWriter = new JsonWriter(false);
        StringWriter stringWriter = new StringWriter();
        jsonWriter.write(workspace, stringWriter);
        String expected = stringWriter.toString();

        encryptedWorkspace = new EncryptedWorkspace();
        encryptedWorkspace.setEncryptionStrategy(encryptionStrategy);
        encryptedWorkspace.setCiphertext(encryptionStrategy.encrypt(expected));

        workspace = encryptedWorkspace.getWorkspace();
        assertEquals("Name", workspace.getName());
        stringWriter = new StringWriter();
        jsonWriter.write(workspace, stringWriter);
        assertEquals(expected, stringWriter.toString());
    }

}

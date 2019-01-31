package com.structurizr.encryption;

import com.structurizr.Workspace;
import com.structurizr.configuration.Role;
import com.structurizr.io.json.JsonWriter;
import org.junit.Before;
import org.junit.Test;

import java.io.StringWriter;

import static junit.framework.TestCase.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class EncryptedWorkspaceTests {

    private EncryptedWorkspace encryptedWorkspace;
    private Workspace workspace;
    private EncryptionStrategy encryptionStrategy;

    @Before
    public void setUp() throws Exception {
        workspace = new Workspace("Name", "Description");
        workspace.setVersion("1.2.3");
        workspace.setId(1234);
        workspace.getConfiguration().addUser("user@domain.com", Role.ReadOnly);
        workspace.setLastModifiedUser("simon");
        workspace.setLastModifiedAgent("structurizr-java");

        encryptionStrategy = new MockEncryptionStrategy();
    }

    @Test
    public void test_construction_WhenTwoParametersAreSpecified() throws Exception {
        encryptedWorkspace = new EncryptedWorkspace(workspace, encryptionStrategy);

        assertEquals("Name", encryptedWorkspace.getName());
        assertEquals("Description", encryptedWorkspace.getDescription());
        assertEquals("1.2.3", encryptedWorkspace.getVersion());
        assertEquals("simon", encryptedWorkspace.getLastModifiedUser());
        assertEquals("structurizr-java", encryptedWorkspace.getLastModifiedAgent());
        assertEquals(1234, encryptedWorkspace.getId());
        assertEquals("user@domain.com", encryptedWorkspace.getConfiguration().getUsers().iterator().next().getUsername());
        assertNull(workspace.getConfiguration());

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
        assertEquals("1.2.3", encryptedWorkspace.getVersion());
        assertEquals("simon", encryptedWorkspace.getLastModifiedUser());
        assertEquals("structurizr-java", encryptedWorkspace.getLastModifiedAgent());
        assertEquals(1234, encryptedWorkspace.getId());

        assertSame(workspace, encryptedWorkspace.getWorkspace());
        assertSame(encryptionStrategy, encryptedWorkspace.getEncryptionStrategy());

        assertEquals(stringWriter.toString(), encryptedWorkspace.getPlaintext());
        assertEquals(encryptionStrategy.encrypt(stringWriter.toString()), encryptedWorkspace.getCiphertext());
    }

    @Test
    public void test_getPlaintext_ReturnsTheDecryptedVersionOfTheCiphertext() throws Exception {
        encryptedWorkspace = new EncryptedWorkspace(workspace, encryptionStrategy);
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
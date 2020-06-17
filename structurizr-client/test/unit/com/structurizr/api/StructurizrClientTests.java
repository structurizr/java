package com.structurizr.api;

import com.structurizr.Workspace;
import org.junit.Test;

import static org.junit.Assert.*;

public class StructurizrClientTests {

    private StructurizrClient structurizrClient;

    @Test
    public void test_construction_WithTwoParameters() {
        structurizrClient = new StructurizrClient("key", "secret");
        assertEquals("https://api.structurizr.com", structurizrClient.getUrl());
        assertEquals("key", structurizrClient.getApiKey());
        assertEquals("secret", structurizrClient.getApiSecret());
    }

    @Test
    public void test_construction_WithThreeParameters() {
        structurizrClient = new StructurizrClient("https://localhost", "key", "secret");
        assertEquals("https://localhost", structurizrClient.getUrl());
        assertEquals("key", structurizrClient.getApiKey());
        assertEquals("secret", structurizrClient.getApiSecret());
    }

    @Test
    public void test_construction_WithThreeParameters_TruncatesTheApiUrl_WhenTheApiUrlHasATrailingSlashCharacter() {
        structurizrClient = new StructurizrClient("https://localhost/", "key", "secret");
        assertEquals("https://localhost", structurizrClient.getUrl());
        assertEquals("key", structurizrClient.getApiKey());
        assertEquals("secret", structurizrClient.getApiSecret());
    }

    @Test
    public void test_construction_ThrowsAnException_WhenANullApiKeyIsUsed() {
        try {
            structurizrClient = new StructurizrClient(null, "secret");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("The API key must not be null or empty.", iae.getMessage());
        }
    }

    @Test
    public void test_construction_ThrowsAnException_WhenAnEmptyApiKeyIsUsed() {
        try {
            structurizrClient = new StructurizrClient(" ", "secret");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("The API key must not be null or empty.", iae.getMessage());
        }
    }

    @Test
    public void test_construction_ThrowsAnException_WhenANullApiSecretIsUsed() {
        try {
            structurizrClient = new StructurizrClient("key", null);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("The API secret must not be null or empty.", iae.getMessage());
        }
    }

    @Test
    public void test_construction_ThrowsAnException_WhenAnEmptyApiSecretIsUsed() {
        try {
            structurizrClient = new StructurizrClient("key", " ");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("The API secret must not be null or empty.", iae.getMessage());
        }
    }

    @Test
    public void test_construction_ThrowsAnException_WhenANullApiUrlIsUsed() {
        try {
            structurizrClient = new StructurizrClient(null, "key", "secret");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("The API URL must not be null or empty.", iae.getMessage());
        }
    }

    @Test
    public void test_construction_ThrowsAnException_WhenAnEmptyApiUrlIsUsed() {
        try {
            structurizrClient = new StructurizrClient(" ", "key", "secret");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("The API URL must not be null or empty.", iae.getMessage());
        }
    }

    @Test
    public void test_getWorkspace_ThrowsAnException_WhenTheWorkspaceIdIsNotValid() throws Exception {
        try {
            structurizrClient = new StructurizrClient("key", "secret");
            structurizrClient.getWorkspace(0);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("The workspace ID must be a positive integer.", iae.getMessage());
        }
    }

    @Test
    public void test_putWorkspace_ThrowsAnException_WhenTheWorkspaceIdIsNotValid() throws Exception {
        try {
            structurizrClient = new StructurizrClient("key", "secret");
            structurizrClient.putWorkspace(0, new Workspace("Name", "Description"));
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("The workspace ID must be a positive integer.", iae.getMessage());
        }
    }

    @Test
    public void test_putWorkspace_ThrowsAnException_WhenANullWorkspaceIsSpecified() throws Exception {
        try {
            structurizrClient = new StructurizrClient("key", "secret");
            structurizrClient.putWorkspace(1234, null);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("The workspace must not be null.", iae.getMessage());
        }
    }

    @Test
    public void test_constructionWithAPropertiesFile_ThrowsAnException_WhenNoPropertiesAreFound() {
        try {
            structurizrClient = new StructurizrClient();
            fail();
        } catch (Exception e) {
            assertEquals("Could not find a structurizr.properties file on the classpath.", e.getMessage());
        }
    }

    @Test
    public void test_getAgent() {
        structurizrClient = new StructurizrClient("key", "secret");
        assertTrue(structurizrClient.getAgent().startsWith("structurizr-java/"));
    }

    @Test
    public void test_setAgent() {
        structurizrClient = new StructurizrClient("key", "secret");
        structurizrClient.setAgent("new_agent");
        assertEquals("new_agent", structurizrClient.getAgent());
    }

    @Test
    public void test_setAgent_ThrowsAnException_WhenPassedNull() {
        structurizrClient = new StructurizrClient("key", "secret");

        try {
            structurizrClient.setAgent(null);
            fail();
        } catch (Exception e) {
            assertEquals("An agent must be provided.", e.getMessage());
        }
    }

    @Test
    public void test_setAgent_ThrowsAnException_WhenPassedAnEmptyString() {
        structurizrClient = new StructurizrClient("key", "secret");

        try {
            structurizrClient.setAgent(" ");
            fail();
        } catch (Exception e) {
            assertEquals("An agent must be provided.", e.getMessage());
        }
    }

}
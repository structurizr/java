package com.structurizr.api;

import com.structurizr.Workspace;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WorkspaceApiClientTests {

    private WorkspaceApiClient client;

    @Test
    void construction_WithTwoParameters() {
        client = new WorkspaceApiClient("key", "secret");
        assertEquals("https://api.structurizr.com", client.getUrl());
        assertEquals("key", client.getApiKey());
        assertEquals("secret", client.getApiSecret());
    }

    @Test
    void construction_WithThreeParameters() {
        client = new WorkspaceApiClient("https://localhost", "key", "secret");
        assertEquals("https://localhost", client.getUrl());
        assertEquals("key", client.getApiKey());
        assertEquals("secret", client.getApiSecret());
    }

    @Test
    void construction_WithThreeParameters_TruncatesTheApiUrl_WhenTheApiUrlHasATrailingSlashCharacter() {
        client = new WorkspaceApiClient("https://localhost/", "key", "secret");
        assertEquals("https://localhost", client.getUrl());
        assertEquals("key", client.getApiKey());
        assertEquals("secret", client.getApiSecret());
    }

    @Test
    void construction_ThrowsAnException_WhenANullApiKeyIsUsed() {
        try {
            client = new WorkspaceApiClient(null, "secret");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("The API key must not be null or empty.", iae.getMessage());
        }
    }

    @Test
    void construction_ThrowsAnException_WhenAnEmptyApiKeyIsUsed() {
        try {
            client = new WorkspaceApiClient(" ", "secret");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("The API key must not be null or empty.", iae.getMessage());
        }
    }

    @Test
    void construction_ThrowsAnException_WhenANullApiSecretIsUsed() {
        try {
            client = new WorkspaceApiClient("key", null);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("The API secret must not be null or empty.", iae.getMessage());
        }
    }

    @Test
    void construction_ThrowsAnException_WhenAnEmptyApiSecretIsUsed() {
        try {
            client = new WorkspaceApiClient("key", " ");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("The API secret must not be null or empty.", iae.getMessage());
        }
    }

    @Test
    void construction_ThrowsAnException_WhenANullApiUrlIsUsed() {
        try {
            client = new WorkspaceApiClient(null, "key", "secret");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("The API URL must not be null or empty.", iae.getMessage());
        }
    }

    @Test
    void construction_ThrowsAnException_WhenAnEmptyApiUrlIsUsed() {
        try {
            client = new WorkspaceApiClient(" ", "key", "secret");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("The API URL must not be null or empty.", iae.getMessage());
        }
    }

    @Test
    void getWorkspace_ThrowsAnException_WhenTheWorkspaceIdIsNotValid() throws Exception {
        try {
            client = new WorkspaceApiClient("key", "secret");
            client.getWorkspace(0);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("The workspace ID must be a positive integer.", iae.getMessage());
        }
    }

    @Test
    void putWorkspace_ThrowsAnException_WhenTheWorkspaceIdIsNotValid() throws Exception {
        try {
            client = new WorkspaceApiClient("key", "secret");
            client.putWorkspace(0, new Workspace("Name", "Description"));
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("The workspace ID must be a positive integer.", iae.getMessage());
        }
    }

    @Test
    void putWorkspace_ThrowsAnException_WhenANullWorkspaceIsSpecified() throws Exception {
        try {
            client = new WorkspaceApiClient("key", "secret");
            client.putWorkspace(1234, null);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("The workspace must not be null.", iae.getMessage());
        }
    }

    @Test
    void getAgent() {
        client = new WorkspaceApiClient("key", "secret");
        assertTrue(client.getAgent().startsWith("structurizr-java/"));
    }

    @Test
    void setAgent() {
        client = new WorkspaceApiClient("key", "secret");
        client.setAgent("new_agent");
        assertEquals("new_agent", client.getAgent());
    }

    @Test
    void setAgent_ThrowsAnException_WhenPassedNull() {
        client = new WorkspaceApiClient("key", "secret");

        try {
            client.setAgent(null);
            fail();
        } catch (Exception e) {
            assertEquals("An agent must be provided.", e.getMessage());
        }
    }

    @Test
    void setAgent_ThrowsAnException_WhenPassedAnEmptyString() {
        client = new WorkspaceApiClient("key", "secret");

        try {
            client.setAgent(" ");
            fail();
        } catch (Exception e) {
            assertEquals("An agent must be provided.", e.getMessage());
        }
    }

}
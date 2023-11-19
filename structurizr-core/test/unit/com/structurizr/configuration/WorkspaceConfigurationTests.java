package com.structurizr.configuration;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WorkspaceConfigurationTests {

    @Test
    void addUser_ThrowsAnException_WhenANullUsernameIsSpecified() {
        try {
            WorkspaceConfiguration configuration = new WorkspaceConfiguration();
            configuration.addUser(null, Role.ReadWrite);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A username must be specified.", iae.getMessage());
        }
    }

    @Test
    void addUser_ThrowsAnException_WhenAnEmptyUsernameIsSpecified() {
        try {
            WorkspaceConfiguration configuration = new WorkspaceConfiguration();
            configuration.addUser(" ", Role.ReadWrite);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A username must be specified.", iae.getMessage());
        }
    }

    @Test
    void addUser_ThrowsAnException_WhenANullRoleIsSpecified() {
        try {
            WorkspaceConfiguration configuration = new WorkspaceConfiguration();
            configuration.addUser("user@example.com", null);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A role must be specified.", iae.getMessage());
        }
    }

    @Test
    void addUser_AddsAUser() {
        WorkspaceConfiguration configuration = new WorkspaceConfiguration();
        configuration.addUser("user1@example.com", Role.ReadOnly);

        assertEquals(1, configuration.getUsers().size());
        User user = configuration.getUsers().stream().filter(u -> u.getUsername().equals("user1@example.com")).findFirst().get();
        assertEquals(Role.ReadOnly, user.getRole());

        configuration.addUser("user2@example.com", Role.ReadWrite);

        assertEquals(2, configuration.getUsers().size());
        user = configuration.getUsers().stream().filter(u -> u.getUsername().equals("user2@example.com")).findFirst().get();
        assertEquals(Role.ReadWrite, user.getRole());
    }

    @Test
    void scope() {
        WorkspaceConfiguration configuration = new WorkspaceConfiguration();
        assertNull(configuration.getScope()); // default scope is undefined

        configuration.setScope(WorkspaceScope.SoftwareSystem);
        assertEquals(WorkspaceScope.SoftwareSystem, configuration.getScope());

        configuration.setScope(null);
        assertNull(configuration.getScope());
    }

    @Test
    void visibility() {
        WorkspaceConfiguration configuration = new WorkspaceConfiguration();
        assertNull(configuration.getVisibility());

        configuration.setVisibility(Visibility.Private);
        assertEquals(Visibility.Private, configuration.getVisibility());

        configuration.setVisibility(null);
        assertNull(configuration.getVisibility());
    }

    @Test
    void clearUsers() {
        WorkspaceConfiguration configuration = new WorkspaceConfiguration();
        configuration.addUser("user@domain.com", Role.ReadOnly);
        assertEquals(1, configuration.getUsers().size());

        configuration.clearUsers();
        assertEquals(0, configuration.getUsers().size());
    }

}
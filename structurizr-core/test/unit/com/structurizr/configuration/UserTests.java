package com.structurizr.configuration;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class UserTests {

    @Test
    void construct_ThrowsAnException_WhenANullUsernameIsSpecified() {
        try {
            new User(null, Role.ReadWrite);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A username must be specified.", iae.getMessage());
        }
    }

    @Test
    void construct_ThrowsAnException_WhenAnEmptyUsernameIsSpecified() {
        try {
            new User(" ", Role.ReadWrite);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A username must be specified.", iae.getMessage());
        }
    }

    @Test
    void construct_ThrowsAnException_WhenANullRoleIsSpecified() {
        try {
            new User("user@domain.com", null);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A role must be specified.", iae.getMessage());
        }
    }

    @Test
    void comstruct() {
        User user = new User("user@domain.com", Role.ReadOnly);

        assertEquals("user@domain.com", user.getUsername());
        assertEquals(Role.ReadOnly, user.getRole());
    }

}
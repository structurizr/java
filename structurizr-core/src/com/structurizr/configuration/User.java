package com.structurizr.configuration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * Represents a user, and the role-based access they have to a workspace.
 */
public final class User {

    @Nonnull
    private final String username;
    @Nonnull
    private final Role role;

    @JsonCreator
    User(@JsonProperty(value = "username", required = true) @CheckForNull String username,
         @JsonProperty(value = "role", required = true) @CheckForNull Role role) {
        this.username = Objects.requireNonNull(username);
        this.role = Objects.requireNonNull(role);
    }

    /**
     * Gets the username (e.g. e-mail address).
     *
     * @return the username, as a String
     */
    @Nonnull
    public String getUsername() {
        return username;
    }

    /**
     * Gets the role.
     *
     * @return a Role enum
     */
    @Nonnull
    public Role getRole() {
        return role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return username.equals(user.username);
    }

    @Override
    public int hashCode() {
        return username.hashCode();
    }

    @Override
    public String toString() {
        return "User {" +
                "username='" + username + '\'' +
                ", role=" + role +
                '}';
    }

}

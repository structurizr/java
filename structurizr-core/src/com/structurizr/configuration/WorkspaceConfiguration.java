package com.structurizr.configuration;

import com.structurizr.util.StringUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

/**
 * A wrapper for configuration options related to the workspace.
 */
public final class WorkspaceConfiguration {

    @Nonnull
    private Set<User> users = new HashSet<>();

    WorkspaceConfiguration() {
    }

    /**
     * Gets the set of users should have read-write or read-only access to the workspace.
     *
     * @return   a Set of User objects (could be empty)
     */
    @Nonnull
    public Set<User> getUsers() {
        return new HashSet<>(users);
    }

    void setUsers(@Nullable Set<User> users) {
        if (users != null) {
            this.users = new HashSet<>(users);
        }
    }

    /**
     * Adds a user, with the specified username and role.
     *
     * @param username      the username (e.g. an e-mail address)
     * @param role          the user's role
     */
    public void addUser(@Nonnull String username, @Nonnull Role role) {
        if (StringUtils.isNullOrEmpty(username)) {
            throw new IllegalArgumentException("A username must be specified.");
        }

        if (role == null) {
            throw new IllegalArgumentException("A role must be specified.");
        }

        users.add(new User(username, role));
    }

}

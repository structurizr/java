package com.structurizr.configuration;

/**
 * Represents a user, and the role-based access they have to a workspace.
 */
public final class User {

    private String username;
    private Role role;

    User() {
    }

    User(String username, Role role) {
        setUsername(username);
        setRole(role);
    }

    /**
     * Gets the username (e.g. e-mail address).
     *
     * @return  the username, as a String
     */
    public String getUsername() {
        return username;
    }

    void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the role.
     *
     * @return  a Role enum
     */
    public Role getRole() {
        return role;
    }

    void setRole(Role role) {
        this.role = role;
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
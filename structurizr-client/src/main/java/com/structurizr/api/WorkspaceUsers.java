package com.structurizr.api;

public class WorkspaceUsers {

    private String owner;
    private String[] admin;
    private String[] write;
    private String[] read;

    public String getOwner() {
        return owner;
    }

    void setOwner(String owner) {
        this.owner = owner;
    }

    public String[] getAdmin() {
        return admin;
    }

    void setAdmin(String[] admin) {
        this.admin = admin;
    }

    public String[] getWrite() {
        return write;
    }

    void setWrite(String[] write) {
        this.write = write;
    }

    public String[] getRead() {
        return read;
    }

    void setRead(String[] read) {
        this.read = read;
    }

}
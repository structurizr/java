package com.structurizr;

import com.structurizr.configuration.WorkspaceConfiguration;

import java.lang.reflect.Constructor;
import java.util.Date;

/**
 * The superclass for regular and encrypted workspaces.
 */
public abstract class AbstractWorkspace {

    private long id;
    private String name;
    private String description;
    private String version;
    private Date lastModifiedDate;
    private String thumbnail;

    private WorkspaceConfiguration configuration;

    protected AbstractWorkspace() {
        configuration = createWorkspaceConfiguration();
    }

    AbstractWorkspace(String name, String description) {
        this();

        this.name = name;
        this.description = description;
    }

    /**
     * Gets the ID of this workspace.
     *
     * @return  the ID (a positive integer)
     */
    public long getId() {
        return this.id;
    }

    /**
     * Sets the ID of this workspace.
     *
     * @param id    the ID (a positive integer)
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Gets the name of this workspace.
     *
     * @return  the name, as a String
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of this workspace.
     *
     * @param name      the name, as a String
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the description of this workspace.
     *
     * @return  the description, as a String
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of this workspace.
     *
     * @param description       the description, as a String
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the version of this workspace.
     *
     * @return  the version, as a String
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets the version of this workspace.
     *
     * @param version   the version, as a String (e.g. 1.0.1, a git hash, etc).
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * Gets the last modified date of this workspace.
     *
     * @return  a Date object
     */
    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    /**
     * Sets the last modified date of this workspace.
     *
     * @param lastModifiedDate  a Date object
     */
    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    /**
     * Gets the thumbnail associated with this workspace.
     *
     * @return  a Base64 encoded PNG file as a Data URI (data:image/png;base64)
     *          or null if there is no thumbnail
     */
    public String getThumbnail() {
        return thumbnail;
    }

    /**
     * Sets the thumbnail associated with this workspace.
     *
     * @param thumbnail     a Base64 encoded PNG file as a Data URI (data:image/png;base64)
     */
    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    /**
     * Gets the configuration associated with this workspace.
     *
     * @return  a Configuration object
     */
    public WorkspaceConfiguration getConfiguration() {
        return configuration;
    }

    protected void setConfiguration(WorkspaceConfiguration configuration) {
        this.configuration = configuration;
    }

    private WorkspaceConfiguration createWorkspaceConfiguration() {
        try {
            Constructor constructor = WorkspaceConfiguration.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            return (WorkspaceConfiguration)constructor.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Clears the configuration associated with this workspace.
     */
    public void clearConfiguration() {
        this.configuration = null;
    }

}
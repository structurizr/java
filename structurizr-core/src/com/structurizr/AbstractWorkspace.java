package com.structurizr;

public abstract class AbstractWorkspace {

    private long id;
    private String name;
    private String description;
    private String thumbnail;

    public AbstractWorkspace() {
    }

    public AbstractWorkspace(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * Gets the ID of this workspace.
     *
     * @return  the ID
     */
    public long getId() {
        return this.id;
    }

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

    public void setDescription(String description) {
        this.description = description;
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

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

}

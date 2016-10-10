package com.structurizr;

import java.net.MalformedURLException;
import java.net.URL;

public abstract class AbstractWorkspace {

    private long id;
    private String name;
    private String description;
    private String thumbnail;
    private String source;
    private String api;

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

    /**
     * Gets the source of this workspace.
     *
     * @return  a URL (as a String)
     */
    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        if (source != null && source.trim().length() > 0) {
            try {
                URL url = new URL(source);
                this.source = source;
            } catch (MalformedURLException murle) {
                throw new IllegalArgumentException(source + " is not a valid URL.");
            }
        }
    }

    public boolean hasSource() {
        return this.source != null && this.source.trim().length() > 0;
    }

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        if (api != null && api.trim().length() > 0) {
            try {
                URL url = new URL(api);
                this.api = api;
            } catch (MalformedURLException murle) {
                throw new IllegalArgumentException(api + " is not a valid URL.");
            }
        }
    }

    public boolean hasApi() {
        return this.api != null && this.api.trim().length() > 0;
    }

}

package com.structurizr.view;

/**
 * The configuration associated with a set of views.
 */
public class Configuration {

    private Metadata metadata = null;

    private Styles styles = new Styles();

    /**
     * Gets the styles associated with this set of views.
     *
     * @return  a Styles object
     */
    public Styles getStyles() {
        return styles;
    }

    /**
     * Gets the placement of the diagram metadata.
     *
     * @return  Top, Bottom or None (for no metadata)
     */
    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

}

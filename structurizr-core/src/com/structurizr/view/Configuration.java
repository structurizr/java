package com.structurizr.view;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

/**
 * Configuration associated with how information in the workspace is rendered.
 */
public final class Configuration {

    private Branding branding = new Branding();
    private Styles styles = new Styles();

    private String defaultView;
    private String lastSavedView;

    /**
     * Gets the styles associated with this set of views.
     *
     * @return  a Styles object
     */
    public Styles getStyles() {
        return styles;
    }

    /**
     * Gets the key of the view that should be shown by default.
     *
     * @return  the key, as a String (or null if not specified)
     */
    public String getDefaultView() {
        return defaultView;
    }

    @JsonSetter
    void setDefaultView(String defaultView) {
        this.defaultView = defaultView;
    }

    /**
     * Sets the view that should be shown by default.
     *
     * @param view  a View object
     */
    public void setDefaultView(View view) {
        if (view != null) {
            this.defaultView = view.getKey();
        }
    }

    @JsonGetter
    String getLastSavedView() {
        return lastSavedView;
    }

    @JsonSetter
    void setLastSavedView(String lastSavedView) {
        this.lastSavedView = lastSavedView;
    }

    public void copyConfigurationFrom(Configuration configuration) {
        setLastSavedView(configuration.getLastSavedView());
    }

    /**
     * Gets the Branding object associated with this workspace.
     *
     * @return  a Branding object
     */
    public Branding getBranding() {
        return branding;
    }

    /**
     * Sets the Branding object associated with this workspace.
     *
     * @param branding      a Branding object
     */
    void setBranding(Branding branding) {
        this.branding = branding;
    }

}

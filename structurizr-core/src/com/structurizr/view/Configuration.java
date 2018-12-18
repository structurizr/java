package com.structurizr.view;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

/**
 * Configuration associated with how information in the workspace is rendered.
 */
public final class Configuration {

    private Branding branding = new Branding();
    private Styles styles = new Styles();
    private Terminology terminology = new Terminology();

    private String defaultView;
    private String lastSavedView;
    private ViewSortOrder viewSortOrder;

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

    /**
     * Gets the Terminology object associated with this workspace.
     *
     * @return  a Terminology object
     */
    public Terminology getTerminology() {
        return terminology;
    }

    /**
     * Sets the Terminology object associated with this workspace.
     *
     * @param terminology       a Terminology object
     */
    void setTerminology(Terminology terminology) {
        this.terminology = terminology;
    }

    /**
     * Gets the sort order used when displaying the list of views.
     *
     * @return  a ViewSortOrder enum
     */
    public ViewSortOrder getViewSortOrder() {
        return viewSortOrder;
    }

    /**
     * Sets the sort order used when displaying the list of views.
     *
     * @param viewSortOrder     a ViewSortOrder enum
     */
    public void setViewSortOrder(ViewSortOrder viewSortOrder) {
        this.viewSortOrder = viewSortOrder;
    }

}
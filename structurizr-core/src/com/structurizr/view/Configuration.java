package com.structurizr.view;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.structurizr.util.Url;

import java.util.ArrayList;
import java.util.List;

/**
 * Configuration associated with how information in the workspace is rendered.
 */
public final class Configuration {

    private Branding branding = new Branding();
    private Styles styles = new Styles();
    private String[] themes;
    private Terminology terminology = new Terminology();

    private MetadataSymbols metadataSymbols;

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

    @JsonIgnore
    @Deprecated
    public String getTheme() {
        if (themes == null || themes.length == 0) {
            return null;
        }

        return themes[0];
    }

    /**
     * Sets the theme used to render views.
     *
     * @param url       the URL of theme
     */
    @JsonSetter
    public void setTheme(String url) {
        setThemes(url);
    }

    /**
     * Gets the URLs of the themes used to render views.
     *
     * @return  an array of URLs
     */
    public String[] getThemes() {
        return themes;
    }

    /**
     * Sets the themes used to render views.
     *
     * @param themes        an array of URLs
     */
    public void setThemes(String... themes) {
        List<String> list = new ArrayList<>();

        if (themes != null) {
            for (String url : themes) {
                if (url != null && url.trim().length() > 0) {
                    if (Url.isUrl(url)) {
                        list.add(url.trim());
                    } else {
                        throw new IllegalArgumentException(url + " is not a valid URL.");
                    }
                }
            }
        }

        this.themes = list.toArray(new String[0]);
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
     * Gets the type of symbols to use when rendering metadata.
     *
     * @return  a MetadataSymbols enum value
     */
    public MetadataSymbols getMetadataSymbols() {
        return metadataSymbols;
    }

    /**
     * Sets the type of symbols to use when rendering metadata.
     *
     * @param metadataSymbols   a MetadataSymbols enum value
     */
    public void setMetadataSymbols(MetadataSymbols metadataSymbols) {
        this.metadataSymbols = metadataSymbols;
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
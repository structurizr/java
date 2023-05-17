package com.structurizr.view;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.structurizr.PropertyHolder;
import com.structurizr.util.Url;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Configuration associated with how information in the workspace is rendered.
 */
public final class Configuration implements PropertyHolder {

    @Nonnull
    private Branding branding = new Branding();
    private Styles styles = new Styles();
    @Nonnull
    private final List<String> themes = new ArrayList<>();
    private Terminology terminology = new Terminology();

    private MetadataSymbols metadataSymbols;

    private String defaultView;
    private String lastSavedView;
    private ViewSortOrder viewSortOrder;

    private Map<String, String> properties = new HashMap<>();

    /**
     * Gets the styles associated with this set of views.
     *
     * @return  a Styles object
     */
    public Styles getStyles() {
        return styles;
    }

    @Nullable
    @JsonIgnore
    @Deprecated
    public String getTheme() {
        return themes.stream().findFirst().orElse(null);
    }

    /**
     * Sets the theme used to render views.
     *
     * @param url       the URL of theme
     */
    @JsonSetter
    void setTheme(@Nullable String url) {
        setThemes(url);
    }

    /**
     * Gets the URLs of the themes used to render views.
     *
     * @return  an array of URLs
     */
    @Nonnull
    public String[] getThemes() {
        return themes.toArray(new String[0]);
    }

    /**
     * Sets the themes used to render views.
     *
     * @param themes        an array of URLs
     */
    public void setThemes(@Nullable String... themes) {
        if (themes != null) {
            for (String url : themes) {
                addTheme(url);
            }
        }
    }

    /**
     * Adds a theme.
     *
     * @param url       the URL of the theme to be added
     */
    public void addTheme(@Nullable String url) {
        if (url != null && !url.trim().isEmpty()) {
            if (Url.isUrl(url)) {
                if (!themes.contains(url)) {
                    themes.add(url.trim());
                }
            } else {
                throw new IllegalArgumentException(url + " is not a valid URL.");
            }
        }
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
    @Nonnull
    public Branding getBranding() {
        return branding;
    }

    /**
     * Sets the Branding object associated with this workspace.
     *
     * @param branding      a Branding object
     */
    void setBranding(@Nonnull Branding branding) {
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

    /**
     * Gets the collection of name-value property pairs, as a Map.
     *
     * @return  a Map (String, String) (empty if there are no properties)
     */
    public Map<String, String> getProperties() {
        return new HashMap<>(properties);
    }

    /**
     * Adds a name-value pair property.
     *
     * @param name      the name of the property
     * @param value     the value of the property
     */
    public void addProperty(String name, String value) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("A property name must be specified.");
        }

        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("A property value must be specified.");
        }

        properties.put(name, value);
    }

    void setProperties(Map<String, String> properties) {
        if (properties != null) {
            this.properties = new HashMap<>(properties);
        }
    }

}

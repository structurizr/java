package com.structurizr.view;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a view on top of a view, which can be used to include or exclude specific elements.
 * For example, perhaps you define a single System Context view and then create two filtered views
 * on top; one showing current state and the other showing future state.
 */
public class FilteredView {

    private View view;
    private String baseViewKey;

    private String key;
    private String description = "";

    private FilterMode mode = FilterMode.Exclude;
    private Set<String> tags = new HashSet<>();

    FilteredView() {
    }

    FilteredView(View view, String key, String description, FilterMode mode, String... tags) {
        this.view = view;
        this.key = key;
        this.description = description;
        this.mode = mode;
        this.tags.addAll(Arrays.asList(tags));
    }

    @JsonIgnore
    public View getView() {
        return view;
    }

    void setView(View view) {
        this.view = view;
    }

    public String getBaseViewKey() {
        if (view != null) {
            return view.getKey();
        } else {
            return this.baseViewKey;
        }
    }

    void setBaseViewKey(String baseViewKey) {
        this.baseViewKey = baseViewKey;
    }

    public String getKey() {
        return key;
    }

    void setKey(String key) {
        this.key = key;
    }

    public String getDescription() {
        return description;
    }

    void setDescription(String description) {
        this.description = description;
    }

    public FilterMode getMode() {
        return mode;
    }

    void setMode(FilterMode mode) {
        this.mode = mode;
    }

    public Set<String> getTags() {
        return new HashSet<>(tags);
    }

}

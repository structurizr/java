package com.structurizr.view;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

/**
 * Represents a view on top of a view, which can be used to include or exclude specific elements.
 */
public final class FilteredView extends View {

    private ModelView view;
    private String baseViewKey;

    private FilterMode mode = FilterMode.Exclude;
    private final Set<String> tags = new TreeSet<>();

    FilteredView() {
    }

    FilteredView(ModelView view, String key, String description, FilterMode mode, String... tags) {
        this.view = view;
        setKey(key);
        setDescription(description);
        this.mode = mode;
        this.tags.addAll(Arrays.asList(tags));
    }

    @JsonIgnore
    public View getView() {
        return view;
    }

    void setView(ModelView view) {
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

    public FilterMode getMode() {
        return mode;
    }

    void setMode(FilterMode mode) {
        this.mode = mode;
    }

    public Set<String> getTags() {
        return new TreeSet<>(tags);
    }

    @Override
    public String getName() {
        return "Filtered: " + view.getName();
    }

}
package com.structurizr.view;

import javax.annotation.Nonnull;

/**
 * A pluggable strategy that can be used to copy layout information from one version of a view to another.
 */
public interface LayoutMergeStrategy {

    /**
     * Attempts to copy the visual layout information (e.g. x,y coordinates) of elements and relationships
     * from the specified source view into the specified destination view.
     *
     * @param sourceView        the source view (e.g. the version stored by the Structurizr service)
     * @param destinationView   the destination View (e.g. the new version, created locally with code)
     */
    void copyLayoutInformation(@Nonnull ModelView sourceView, @Nonnull ModelView destinationView);

}
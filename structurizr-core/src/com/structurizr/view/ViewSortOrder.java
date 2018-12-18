package com.structurizr.view;

/**
 * <p>
 * Allows the sort order of views to be customized as follows:
 * </p>
 * <ul>
 *     <li><b>Default</b>: Views are grouped by the software system they are associated with, and then sorted by type (System Landscape, System Context, Container, Component, Dynamic and Deployment) within these groups.</li>
 *     <li><b>Type</b>: Views are sorted by type (System Landscape, System Context, Container, Component, Dynamic and Deployment).</li>
 *     <li><b>Key</b>: Views are sorted by the view key (alphabetical, ascending).</li>
 * </ul>
 */
public enum ViewSortOrder {

    Default,
    Type,
    Key

}
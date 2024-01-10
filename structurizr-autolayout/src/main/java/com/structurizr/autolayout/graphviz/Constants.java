package com.structurizr.autolayout.graphviz;

/**
 * Some constants used when applying graphviz.
 */
class Constants {

    // diagrams created by the Structurizr cloud service/on-premises installation/Lite are sized for 300dpi
    static final double STRUCTURIZR_DPI = 300.0;

    // graphviz uses 72dpi by default
    private static final double GRAPHVIZ_DPI = 72.0;

    // this is needed to convert coordinates provided by graphviz, to those used by Structurizr
    static final double DPI_RATIO = STRUCTURIZR_DPI / GRAPHVIZ_DPI;

}
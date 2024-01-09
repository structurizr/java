package com.structurizr.model;

/**
 * Represents the location of an element with regards to a specific viewpoint.
 *
 * Diagram renderers may use this information in a different way, but generally it will be used to mark
 * an element as being outside of the enterprise boundary. For example, "our customers are external to our enterprise":
 * - https://github.com/structurizr/examples/blob/main/java/src/main/java/com/structurizr/example/bigbankplc/BigBankPlc.java#L36
 * - https://structurizr.com/share/28201/diagrams#SystemLandscape
 */
public enum Location {

    Internal,
    External,
    Unspecified

}
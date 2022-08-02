package com.structurizr.documentation;

/**
 * Marker interface for items that can have documentation attached (i.e. workspaces and software systems).
 */
public interface Documentable {

    Documentation getDocumentation();

}
package com.structurizr.documentation;

import com.structurizr.documentation.Documentation;

/**
 * Marker interface for items that can have documentation attached (i.e. workspaces and software systems).
 */
public interface Documentable {

    Documentation getDocumentation();

}
package com.structurizr.importer.documentation;

import com.structurizr.documentation.Documentable;

import java.io.File;

/**
 * An interface implemented by documentation importers.
 */
public interface DocumentationImporter {

    /**
     * Imports documentation from the specified path.
     *
     * @param documentable      the item that documentation should be associated with
     * @param path              the path to import documentation from
     */
    void importDocumentation(Documentable documentable, File path);

}
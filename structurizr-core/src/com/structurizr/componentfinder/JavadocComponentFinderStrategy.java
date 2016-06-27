package com.structurizr.componentfinder;

import java.io.File;

/**
 * This component finder strategy doesn't really find components, it instead extracts
 * the top-level Javadoc comment from the code so that this can be added to existing
 * component definitions.
 *
 * @deprecated use {@link com.structurizr.componentfinder.SourceCodeComponentFinderStrategy} instead.
 */
@Deprecated
public class JavadocComponentFinderStrategy extends SourceCodeComponentFinderStrategy {

    public JavadocComponentFinderStrategy(File sourcePath) {
        super(sourcePath);
    }

    public JavadocComponentFinderStrategy(File sourcePath, int maxDescriptionLength) {
        super(sourcePath, maxDescriptionLength);
    }

}
package com.structurizr.componentfinder;

import com.structurizr.model.Component;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.RootDoc;

import java.io.File;
import java.util.Collection;
import java.util.LinkedList;

/**
 * This component finder strategy doesn't really find components, it instead extracts
 * the top-level Javadoc comment from the code so that this can be added to existing
 * component definitions.
 */
public class JavadocComponentFinderStrategy extends AbstractComponentFinderStrategy {

    private static RootDoc ROOTDOC;

    private File sourcePath;
    private Integer maxDescriptionLength = null;

    public JavadocComponentFinderStrategy(File sourcePath) {
        this.sourcePath = sourcePath;
    }

    public JavadocComponentFinderStrategy(File sourcePath, int maxDescriptionLength) {
        this.sourcePath = sourcePath;
        this.maxDescriptionLength = maxDescriptionLength;
    }

    @Override
    public Collection<Component> findComponents() throws Exception {
        runJavaDoc();

        Collection<Component> componentsFound = new LinkedList<>();

        // interfaces first (use interface Javadoc over implementation Javadoc)
        for (ClassDoc classDoc : ROOTDOC.classes()) {
            String comment = filterAndTruncate(classDoc.commentText());
            String pathToSourceFile = classDoc.position().file().getCanonicalPath();
            if (classDoc.isInterface()) {
                Component component = getComponentFinder().enrichComponent(
                        classDoc.qualifiedTypeName(),
                        null, // implementation type
                        comment,
                        "", // technology
                        pathToSourceFile);
                if (component != null) {
                    componentsFound.add(component);
                }
            }
        }

        // then implementation classes
        for (ClassDoc classDoc : ROOTDOC.classes()) {
            String comment = filterAndTruncate(classDoc.commentText());
            String pathToSourceFile = classDoc.position().file().getCanonicalPath();
            if (!classDoc.isInterface()) {
                Component component = getComponentFinder().enrichComponent(
                        null, // interface type
                        classDoc.qualifiedTypeName(),
                        comment,
                        "", // technology
                        pathToSourceFile);
                if (component != null) {
                    componentsFound.add(component);
                }
            }
        }

        return componentsFound;
    }

    private void runJavaDoc() throws Exception {
        com.sun.tools.javadoc.Main.execute("StructurizrDoclet",
                this.getClass().getName(),
                new String[]{
                        "-sourcepath", sourcePath.getCanonicalPath(),
                        "-subpackages", getComponentFinder().getPackageToScan(),
                        "-private"
                });
    }

    public static boolean start(RootDoc rootDoc) {
        ROOTDOC = rootDoc;
        return true;
    }

    @Override
    public void findDependencies() throws Exception {
        // do nothing
    }

    private String filterAndTruncate(String s) {
        if (s == null) {
            return null;
        }

        s = s.replaceAll("\\n", "");
        s = s.replaceAll("(?s)<.*?>", "");

        if (maxDescriptionLength != null && s.length() > maxDescriptionLength) {
            return s.substring(0, maxDescriptionLength-3) + "...";
        } else {
            return s;
        }
    }

}
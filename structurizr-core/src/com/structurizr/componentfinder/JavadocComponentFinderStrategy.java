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
public class JavadocComponentFinderStrategy extends ComponentFinderStrategy {

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

        for (ClassDoc classDoc : ROOTDOC.classes()) {
            String comment = filterAndTruncate(classDoc.commentText());
            String pathToSourceFile = classDoc.position().file().getCanonicalPath();

            Component component = getComponentFinder().getContainer().getComponentOfType(classDoc.qualifiedTypeName());
            if (component != null)
            {
                component.setDescription(comment);
                component.setSourcePath(pathToSourceFile);
            }
        }

        return new LinkedList<>();
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
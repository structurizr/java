package com.structurizr.componentfinder;

import com.structurizr.model.Component;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.RootDoc;

import java.util.Collection;
import java.util.LinkedList;

public class JavadocComponentFinderStrategy extends AbstractComponentFinderStrategy {

    private static RootDoc ROOTDOC;

    private String sourcePath;
    private Integer maxDescriptionLength = null;

    public JavadocComponentFinderStrategy(String sourcePath) {
        this.sourcePath = sourcePath;
    }

    public JavadocComponentFinderStrategy(String sourcePath, int maxDescriptionLength) {
        this.sourcePath = sourcePath;
        this.maxDescriptionLength = maxDescriptionLength;
    }

    @Override
    public Collection<Component> findComponents() throws Exception {
        runJavaDoc();

        Collection<Component> componentsFound = new LinkedList<>();

        // interfaces first (use interface Javadoc over implementation Javadoc)
        for (ClassDoc classDoc : ROOTDOC.classes()) {
            String comment = classDoc.commentText();
            if (comment != null && !comment.isEmpty()) {
                comment = filterAndTruncate(comment);
                if (classDoc.isInterface()) {
                    Component component = getComponentFinder().enrichComponent(classDoc.qualifiedTypeName(), null, comment, "");
                    if (component != null) {
                        componentsFound.add(component);
                    }
                }
            }
        }

        // then implementation classes
        for (ClassDoc classDoc : ROOTDOC.classes()) {
            String comment = classDoc.commentText();
            if (comment != null && !comment.isEmpty()) {
                comment = filterAndTruncate(comment);
                if (!classDoc.isInterface()) {
                    Component component = getComponentFinder().enrichComponent(null, classDoc.qualifiedTypeName(), comment, "");
                    if (component != null) {
                        componentsFound.add(component);
                    }
                }
            }
        }

        return componentsFound;
    }

    private void runJavaDoc() {
        com.sun.tools.javadoc.Main.execute("StructurizrDoclet",
                this.getClass().getName(),
                new String[]{
                        "-sourcepath", sourcePath,
                        "-subpackages", getComponentFinder().getPackageToScan()
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
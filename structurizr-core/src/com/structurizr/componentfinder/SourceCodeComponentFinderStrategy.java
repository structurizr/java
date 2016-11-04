package com.structurizr.componentfinder;

import com.structurizr.model.CodeElement;
import com.structurizr.model.Component;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.RootDoc;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * This component finder strategy doesn't really find components, it instead:
 * <ul>
 * <li>Extracts the top-level Javadoc comment from the code so that this can be added to existing component definitions.</li>
 * <li>Calculates the size of components based upon the number of lines of source code.</li>
 * </ul>
 */
public class SourceCodeComponentFinderStrategy extends ComponentFinderStrategy {

    private static RootDoc ROOTDOC;

    private File sourcePath;
    private Integer maxDescriptionLength = null;

    private Map<String,File> typeToSourceFile = new HashMap<>();
    private Map<String,String> typeToDescription = new HashMap<>();

    public SourceCodeComponentFinderStrategy(File sourcePath) {
        this.sourcePath = sourcePath;
    }

    public SourceCodeComponentFinderStrategy(File sourcePath, int maxDescriptionLength) {
        this.sourcePath = sourcePath;
        this.maxDescriptionLength = maxDescriptionLength;
    }

    @Override
    public void findComponents() throws Exception {
        // do nothing
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
        runJavaDoc();

        JavadocCommentFilter filter = new JavadocCommentFilter(maxDescriptionLength);
        for (ClassDoc classDoc : ROOTDOC.classes()) {
            String type = classDoc.qualifiedTypeName();
            String comment = filter.filterAndTruncate(classDoc.commentText());
            String pathToSourceFile = classDoc.position().file().getCanonicalPath();

            typeToSourceFile.put(type, new File(pathToSourceFile));
            typeToDescription.put(type, comment);
        }

        for (Component component : getComponentFinder().getContainer().getComponents()) {
            long count = 0;
            File sourceFile = typeToSourceFile.get(component.getType());
            if (sourceFile != null) {
                count += Files.lines(Paths.get(sourceFile.toURI())).count();
            }

            component.setDescription(typeToDescription.getOrDefault(component.getType(), null));

            for (CodeElement codeElement : component.getCode()) {
                codeElement.setDescription(typeToDescription.getOrDefault(codeElement.getType(), null));

                sourceFile = typeToSourceFile.get(codeElement.getType());
                if (sourceFile != null) {
                    long numberOfLinesInFile = Files.lines(Paths.get(sourceFile.toURI())).count();
                    codeElement.setUrl(sourceFile.toURI().toString());
                    codeElement.setSize(numberOfLinesInFile);
                    count += numberOfLinesInFile;
                }
            }

            if (count > 0) {
                component.setSize(count);
            }
        }
    }

}
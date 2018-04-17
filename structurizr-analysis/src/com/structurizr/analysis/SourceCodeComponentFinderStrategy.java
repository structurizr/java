package com.structurizr.analysis;

import com.structurizr.model.CodeElement;
import com.structurizr.model.CodeElementRole;
import com.structurizr.model.Component;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.RootDoc;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * This component finder strategy doesn't really find components, it instead:
 * <ul>
 * <li>Extracts the top-level Javadoc comment from the code so that this can be added to existing component definitions.</li>
 * <li>Calculates the size of components based upon the number of lines of source code.</li>
 * </ul>
 */
public class SourceCodeComponentFinderStrategy implements ComponentFinderStrategy {

    private ComponentFinder componentFinder;
    private static RootDoc ROOTDOC;

    private File sourcePath;
    private Integer maxDescriptionLength = null;
    private String encoding = null;

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
    public void setComponentFinder(ComponentFinder componentFinder) {
        this.componentFinder = componentFinder;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    @Override
    public void beforeFindComponents() throws Exception {
    }

    @Override
    public Set<Component> findComponents() throws Exception {
        return new HashSet<>(); // this component finder doesn't find components
    }

    @Override
    public void afterFindComponents() throws Exception {
        runJavaDoc();

        JavadocCommentFilter filter = new JavadocCommentFilter(maxDescriptionLength);
        for (ClassDoc classDoc : ROOTDOC.classes()) {
            String type = classDoc.qualifiedTypeName();
            String comment = filter.filterAndTruncate(classDoc.commentText());
            String pathToSourceFile = classDoc.position().file().getCanonicalPath();

            typeToSourceFile.put(type, new File(pathToSourceFile));
            typeToDescription.put(type, comment);
        }

        for (Component component : componentFinder.getContainer().getComponents()) {
            long count = 0;

            for (CodeElement codeElement : component.getCode()) {
                if (typeToDescription.containsKey(codeElement.getType())) {
                    codeElement.setDescription(typeToDescription.get(codeElement.getType()));

                    // additionally set the description on the component, if it's not already been set
                    if (codeElement.getRole() == CodeElementRole.Primary) {
                        if (component.getDescription() == null || component.getDescription().trim().length() == 0) {
                            component.setDescription(typeToDescription.get(component.getType().getType()));
                        }
                    }
                }

                File sourceFile = typeToSourceFile.get(codeElement.getType());
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

    private void runJavaDoc() throws Exception {
        List<String> parameters = new LinkedList<>();
        parameters.add("-sourcepath");
        parameters.add(sourcePath.getCanonicalPath());
        parameters.add("-subpackages");
        parameters.add(String.join(":", componentFinder.getPackageNames()));

        if (encoding != null) {
            parameters.add("-encoding");
            parameters.add(encoding);
        }

        parameters.add("-private");

        final PrintStream outOriginal = System.out;
        final PrintStream errOriginal = System.err;
        final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        System.setOut(new PrintStream(bytes));
        System.setErr(System.out);
        try {
            com.sun.tools.javadoc.Main.execute(
                    "StructurizrDoclet",
                    this.getClass().getName(),
                    parameters.toArray(new String[parameters.size()])
            );
        }
        catch (Throwable t) {
            outOriginal.write(bytes.toByteArray());
            outOriginal.flush();
            throw t;
        }
        finally {
            System.setOut(outOriginal);
            System.setOut(errOriginal);
        }
    }

    public static boolean start(RootDoc rootDoc) {
        ROOTDOC = rootDoc;
        return true;
    }

}
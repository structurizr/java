package com.structurizr.component.provider;

import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.structurizr.component.Type;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * A type provider that uses JavaParser to read Javadoc comments from source code.
 */
public final class SourceDirectoryTypeProvider implements TypeProvider {

    private static final Log log = LogFactory.getLog(SourceDirectoryTypeProvider.class);
    private static final String JAVA_FILE_EXTENSION = ".java";
    private static final int DEFAULT_DESCRIPTION_LENGTH = 60;

    private final File directory;
    private final int maximumDescriptionLength;
    private final Set<Type> types = new LinkedHashSet<>();

    public SourceDirectoryTypeProvider(File directory) {
        this(directory, DEFAULT_DESCRIPTION_LENGTH);
    }

    public SourceDirectoryTypeProvider(File directory, int maximumDescriptionLength) {
        if (directory == null) {
            throw new IllegalArgumentException("A directory must be supplied");
        }

        if (!directory.exists()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " does not exist");
        }

        if (!directory.isDirectory()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not a directory");
        }

        this.directory = directory;
        this.maximumDescriptionLength = maximumDescriptionLength;
        StaticJavaParser.getParserConfiguration().setLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_21);
    }

    @Override
    public Set<Type> getTypes() {
        parse(directory, maximumDescriptionLength);

        return new LinkedHashSet<>(types);
    }

    private void parse(File path, int maximumDescriptionLength) {
        if (path.isDirectory()) {
            File[] files = path.listFiles();
            if (files != null) {
                for (File file : files) {
                    try {
                        parse(file, maximumDescriptionLength);
                    } catch (Exception e) {
                        log.warn("Error parsing " + file.getAbsolutePath(), e);
                    }
                }
            }
        } else {
            if (path.getName().endsWith(JAVA_FILE_EXTENSION)) {
                try {
                    new VoidVisitorAdapter<>() {
                        @Override
                        public void visit(ClassOrInterfaceDeclaration n, Object arg) {
                            if (n.getFullyQualifiedName().isPresent()) {
                                String fullyQualifiedName = n.getFullyQualifiedName().get();
                                Type type = new Type(fullyQualifiedName);
                                type.setSource(path.getAbsolutePath());

                                if (n.getComment().isPresent() && n.getComment().get() instanceof JavadocComment) {
                                    JavadocComment javadocComment = (JavadocComment) n.getComment().get();
                                    String description = javadocComment.parse().getDescription().toText();

                                    type.setDescription(new JavadocCommentFilter(maximumDescriptionLength).filterAndTruncate(description));
                                }
                                types.add(type);
                            }
                        }

                        @Override
                        public void visit(PackageDeclaration n, Object arg) {
                            String PACKAGE_INFO_JAVA_SOURCE = "package-info.java";
                            String PACKAGE_INFO_SUFFIX = ".package-info";

                            if (path.getName().endsWith(PACKAGE_INFO_JAVA_SOURCE)) {
                                String fullyQualifiedName = n.getName().asString() + PACKAGE_INFO_SUFFIX;

                                Type type = new Type(fullyQualifiedName);
                                type.setSource(path.getAbsolutePath());

                                Node rootNode = n.findRootNode();
                                if (rootNode != null && rootNode.getComment().isPresent() && rootNode.getComment().get() instanceof JavadocComment) {
                                    JavadocComment javadocComment = (JavadocComment)rootNode.getComment().get();
                                    String description = javadocComment.parse().getDescription().toText();

                                    type.setDescription(new JavadocCommentFilter(maximumDescriptionLength).filterAndTruncate(description));
                                }

                                types.add(type);
                            }
                        }
                    }.visit(StaticJavaParser.parse(path), null);
                } catch (IOException e) {
                    log.warn("Error parsing source code", e);
                }
            } else {
                log.debug("Ignoring " + path.getAbsolutePath());
            }
        }
    }

}
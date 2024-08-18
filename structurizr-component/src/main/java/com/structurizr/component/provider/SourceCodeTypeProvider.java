package com.structurizr.component.provider;

import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.structurizr.component.Type;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * A type provider that uses JavaParser to read Javadoc comments from source code.
 */
public final class SourceCodeTypeProvider implements TypeProvider {

    private static final Log log = LogFactory.getLog(SourceCodeTypeProvider.class);
    private static final String JAVA_FILE_EXTENSION = ".java";
    private static final int DEFAULT_DESCRIPTION_LENGTH = 60;

    private final Set<Type> types = new HashSet<>();

    public SourceCodeTypeProvider(File path) {
        this(path, DEFAULT_DESCRIPTION_LENGTH);
    }

    public SourceCodeTypeProvider(File path, int maximumDescriptionLength) {
        StaticJavaParser.getParserConfiguration().setLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_21);

        parse(path, maximumDescriptionLength);
    }

    @Override
    public Set<Type> getTypes() {
        return new HashSet<>(types);
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
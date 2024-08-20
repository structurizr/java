package com.structurizr.component.provider;

import com.structurizr.component.Type;
import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.JavaClass;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * A type repository that uses Apache Commons BCEL to load Java classes from a local directory.
 */
public final class ClassDirectoryTypeProvider implements TypeProvider {

    private static final Log log = LogFactory.getLog(ClassDirectoryTypeProvider.class);
    private static final String CLASS_FILE_EXTENSION = ".class";

    private final File directory;

    public ClassDirectoryTypeProvider(File directory) {
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
    }

    public Set<Type> getTypes() {
        Set<Type> types = new LinkedHashSet<>();

        Set<File> files = findClassFiles(directory);
        for (File file : files) {
            ClassParser parser = new ClassParser(file.getAbsolutePath());
            try {
                JavaClass javaClass = parser.parse();
                types.add(new Type(javaClass));
            } catch (IOException e) {
                log.warn(e);
            }
        }

        return types;
    }

    private Set<File> findClassFiles(File path) {
        Set<File> classFiles = new LinkedHashSet<>();
        if (path.isDirectory()) {
            File[] files = path.listFiles();
            if (files != null) {
                for (File file : files) {
                    classFiles.addAll(findClassFiles(file));
                }
            }
        } else {
            if (path.getName().endsWith(CLASS_FILE_EXTENSION)) {
                classFiles.add(path);
            }
        }

        return classFiles;
    }


}
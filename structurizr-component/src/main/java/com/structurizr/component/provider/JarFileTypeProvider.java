package com.structurizr.component.provider;

import com.structurizr.component.Type;
import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.JavaClass;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;

/**
 * A type repository that uses Apache Commons BCEL to load Java classes from a local JAR file.
 */
public final class JarFileTypeProvider implements TypeProvider {

    private static final Log log = LogFactory.getLog(JarFileTypeProvider.class);
    private static final String CLASS_FILE_EXTENSION = ".class";

    private final File jarFile;

    public JarFileTypeProvider(File file) {
        this.jarFile = file;
    }

    public Set<Type> getTypes() {
        Set<Type> types = new HashSet<>();
        java.util.jar.JarFile jar = null;
        try {
            jar = new java.util.jar.JarFile(jarFile);
            Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (!entry.getName().endsWith(CLASS_FILE_EXTENSION)) {
                    continue;
                }

                ClassParser parser = new ClassParser(jarFile.getAbsolutePath(), entry.getName());
                JavaClass javaClass = parser.parse();
                types.add(new Type(javaClass));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (jar != null) {
                try {
                    jar.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return types;
    }

}
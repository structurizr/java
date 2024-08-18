package com.structurizr.component.matcher;

import com.structurizr.component.Type;
import org.apache.bcel.classfile.JavaClass;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Matches types where the type extends the specified class.
 */
public class ExtendsTypeMatcher extends AbstractTypeMatcher {

    private static final Log log = LogFactory.getLog(ExtendsTypeMatcher.class);

    private final String className;

    public ExtendsTypeMatcher(String className, String technology) {
        super(technology);

        this.className = className;
    }

    @Override
    public boolean matches(Type type) {
        JavaClass javaClass = type.getJavaClass();
        try {
            Set<String> superClasses = Stream.of(javaClass.getSuperClasses()).map(JavaClass::getClassName).collect(Collectors.toSet());
            return superClasses.contains(className);
        } catch (ClassNotFoundException e) {
            log.warn("Cannot find super classes of " + type.getFullyQualifiedName(), e);
        }

        return false;
    }

}
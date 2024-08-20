package com.structurizr.component;

import com.structurizr.util.StringUtils;
import org.apache.bcel.classfile.JavaClass;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a Java type (e.g. class or interface) - it's a wrapper around a BCEL JavaClass.
 */
public class Type {

    private final JavaClass javaClass;
    private final String fullyQualifiedName;
    private String description;
    private String source;
    private final Set<Type> dependencies = new LinkedHashSet<>();

    public Type(JavaClass javaClass) {
        if (javaClass == null) {
            throw new IllegalArgumentException("A BCEL JavaClass must be supplied");
        }

        this.fullyQualifiedName = javaClass.getClassName();
        this.javaClass = javaClass;
    }

    public Type(String fullyQualifiedName) {
        if (StringUtils.isNullOrEmpty(fullyQualifiedName)) {
            throw new IllegalArgumentException("A fully qualified name must be supplied");
        }

        this.fullyQualifiedName = fullyQualifiedName;
        this.javaClass = null;
    }

    public String getFullyQualifiedName() {
        return fullyQualifiedName;
    }

    public String getName() {
        return fullyQualifiedName.substring(fullyQualifiedName.lastIndexOf(".")+1);
    }

    public String getPackageName() {
        return getFullyQualifiedName().substring(0, getFullyQualifiedName().lastIndexOf("."));
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public JavaClass getJavaClass() {
        return this.javaClass;
    }

    public void addDependency(Type type) {
        this.dependencies.add(type);
    }

    public Set<Type> getDependencies() {
        return new LinkedHashSet<>(dependencies);
    }

    public boolean isAbstractClass() {
        return javaClass.isAbstract() && javaClass.isClass();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Type type = (Type) o;
        return fullyQualifiedName.equals(type.fullyQualifiedName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fullyQualifiedName);
    }

    @Override
    public String toString() {
        return this.fullyQualifiedName;
    }

}
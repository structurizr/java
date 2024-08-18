package com.structurizr.component;

import org.apache.bcel.classfile.JavaClass;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a Java type (e.g. class or interface) - it's a wrapper around a BCEL JavaClass.
 */
public final class Type {

    private JavaClass javaClass;
    private String description;
    private String source;
    private final Set<Type> dependencies = new HashSet<>();

    private final String fullyQualifiedName;

    public Type(JavaClass javaClass) {
        this(javaClass.getClassName());

        this.javaClass = javaClass;
    }

    public Type(String fullyQualifiedName) {
        this.fullyQualifiedName = fullyQualifiedName;
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

    public JavaClass getJavaClass() {
        return this.javaClass;
    }

    public void addDependency(Type type) {
        this.dependencies.add(type);
    }

    public Set<Type> getDependencies() {
        return new HashSet<>(dependencies);
    }

    public boolean isAbstract() {
        return javaClass.isAbstract();
    }

    @Override
    public String toString() {
        return this.fullyQualifiedName;
    }

}
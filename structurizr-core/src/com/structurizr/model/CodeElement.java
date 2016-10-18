package com.structurizr.model;

/**
 * Represents a code element, such as a Java class or interface,
 * that is part of the implementation of a component.
 */
public class CodeElement {

    private CodeElementRole role = CodeElementRole.Supporting;
    private String name;
    private String type;
    private String source;
    private String language = "Java";
    private long size;

    CodeElement() {
    }

    public CodeElement(String fullyQualifiedTypeName) {
        if (fullyQualifiedTypeName == null || fullyQualifiedTypeName.trim().isEmpty()) {
            throw new IllegalArgumentException("A fully qualified name must be provided.");
        }

        int dot = fullyQualifiedTypeName.lastIndexOf('.');
        if (dot > -1) {
            this.name = fullyQualifiedTypeName.substring(dot+1, fullyQualifiedTypeName.length());
            this.type = fullyQualifiedTypeName;
        } else {
            this.name = fullyQualifiedTypeName;
            this.type = fullyQualifiedTypeName;
        }
    }

    public CodeElementRole getRole() {
        return role;
    }

    void setRole(CodeElementRole role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    void setType(String type) {
        this.type = type;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CodeElement that = (CodeElement) o;

        return type.equals(that.type);
    }

    @Override
    public int hashCode() {
        return type.hashCode();
    }
}

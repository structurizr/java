package com.structurizr.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.structurizr.util.Url;

/**
 * Represents a code element, such as a Java class or interface,
 * that is part of the implementation of a component.
 */
public final class CodeElement {

    /** the role of the code element ... Primary or Supporting */
    private CodeElementRole role = CodeElementRole.Supporting;

    /** the name of the code element ... typically the simple class/interface name */
    private String name;

    /** the fully qualified type of the code element **/
    private String type;

    /** a short description of the code element */
    private String description;

    /** a URL; e.g. a reference to the code element in source code control */
    private String url;

    /** the programming language used to create the code element */
    private String language = "Java";

    /** the category of code element; e.g. class, interface, etc */
    private String category;

    /** the visibility of the code element; e.g. public, package, private */
    private String visibility;

    /** the size of the code element; e.g. the number of lines */
    private long size;

    CodeElement() {
    }

    CodeElement(String fullyQualifiedTypeName) {
        if (fullyQualifiedTypeName == null || fullyQualifiedTypeName.trim().isEmpty()) {
            throw new IllegalArgumentException("A fully qualified name must be provided.");
        }

        int dot = fullyQualifiedTypeName.lastIndexOf('.');
        if (dot > -1) {
            setName(fullyQualifiedTypeName.substring(dot+1, fullyQualifiedTypeName.length()));
            setType(fullyQualifiedTypeName);
        } else {
            setName(fullyQualifiedTypeName);
            setType(fullyQualifiedTypeName);
        }
    }

    /**
     * Gets the role of this code element; Primary or Supporting.
     *
     * @return  a CodeElementRole enum
     */
    public CodeElementRole getRole() {
        return role;
    }

    void setRole(CodeElementRole role) {
        this.role = role;
    }

    /**
     * Gets the name of this code element.
     *
     * @return  the name, as a String
     */
    public String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the type (fully qualified type name) of this code element.
     *
     * @return  the type, as a String
     */
    public String getType() {
        return type;
    }

    void setType(String type) {
        this.type = type;
    }

    /**
     * Gets the Java package of this component (i.e. the package of the primary code element).
     *
     * @return  the package name, as a String
     */
    @JsonIgnore
    public String getPackage() {
        return type.substring(0, type.lastIndexOf('.'));
    }

    /**
     * Gets the description of this code element.
     *
     * @return  the description, as a String
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of this code element.
     *
     * @param description   the description, as a String
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the URL where more information about this code element can be found.
     *
     * @return  the URL as a String, or null if not set
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the URL where more information about this code element can be found.
     *
     * @param url   the URL as a String
     * @throws IllegalArgumentException     if the URL is not a well-formed URL
     */
    public void setUrl(String url) {
        if (url != null && url.trim().length() > 0) {
            if (Url.isUrl(url)) {
                this.url = url;
            } else {
                throw new IllegalArgumentException(url + " is not a valid URL.");
            }
        }
    }

    /**
     * Gets the programming language of this code element.
     *
     * @return  the programming language, as a String
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Sets the programming language of this code element.
     *
     * @param language  the programming language, as a String
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * Gets the category of this code element (interface, class, etc).
     *
     * @return  the category, as a String
     */
    public String getCategory() {
        return category;
    }

    /**
     * Sets the category of this code element.
     *
     * @param category  the category, as a String
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Gets the visibility of this code element (public, package, etc).
     *
     * @return  the visibility, as a String
     */
    public String getVisibility() {
        return visibility;
    }

    /**
     * Sets the visibility of this code element.
     *
     * @param visibility    the visibility, as a String
     */
    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    /**
     * Gets the size of this code element (e.g. the number of lines of code).
     *
     * @return  the size, as a long
     */
    public long getSize() {
        return size;
    }

    /**
     * Sets the size of this code element.
     *
     * @param size  the size, as a long
     */
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
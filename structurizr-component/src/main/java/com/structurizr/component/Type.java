package com.structurizr.component;

import com.structurizr.util.StringUtils;
import org.apache.bcel.classfile.*;

import java.util.*;

/**
 * Represents a Java type (e.g. class or interface) - it's a wrapper around a BCEL JavaClass.
 */
public class Type {

    private static final String STRUCTURIZR_TAG_ANNOTATION = "Lcom/structurizr/annotation/Tag;";
    private static final String STRUCTURIZR_TAGS_ANNOTATION = "Lcom/structurizr/annotation/Tags;";

    private static final String STRUCTURIZR_PROPERTY_ANNOTATION = "Lcom/structurizr/annotation/Property;";
    private static final String STRUCTURIZR_PROPERTIES_ANNOTATION = "Lcom/structurizr/annotation/Properties;";

    private JavaClass javaClass = null;
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

    void setJavaClass(JavaClass javaClass) {
        this.javaClass = javaClass;
    }

    public void addDependency(Type type) {
        this.dependencies.add(type);
    }

    public Set<Type> getDependencies() {
        return new LinkedHashSet<>(dependencies);
    }

    public boolean hasDependency(Type type) {
        return dependencies.contains(type);
    }

    public boolean isAbstractClass() {
        return javaClass.isAbstract() && javaClass.isClass();
    }

    public List<String> getTags() {
        List<String> tags = new ArrayList<>();

        AnnotationEntry[] annotationEntries = javaClass.getAnnotationEntries();
        for (AnnotationEntry annotationEntry : annotationEntries) {
            if (STRUCTURIZR_TAG_ANNOTATION.equals(annotationEntry.getAnnotationType())) {
                ElementValuePair elementValuePair = annotationEntry.getElementValuePairs()[0];
                String tag = elementValuePair.getValue().stringifyValue();
                tags.add(tag);
            } else if (STRUCTURIZR_TAGS_ANNOTATION.equals(annotationEntry.getAnnotationType())) {
                ElementValuePair elementValuePair = annotationEntry.getElementValuePairs()[0];
                ArrayElementValue elementValue = (ArrayElementValue)elementValuePair.getValue();
                for (ElementValue value : elementValue.getElementValuesArray()) {
                    AnnotationElementValue annotationElementValue = (AnnotationElementValue)value;
                    AnnotationEntry tagAannotationEntry = annotationElementValue.getAnnotationEntry();
                    String tag = tagAannotationEntry.getElementValuePairs()[0].getValue().stringifyValue();
                    tags.add(tag);
                }
            }
        }

        return tags;
    }

    public Map<String, String> getProperties() {
        Map<String, String> properties = new HashMap<>();

        AnnotationEntry[] annotationEntries = javaClass.getAnnotationEntries();
        for (AnnotationEntry annotationEntry : annotationEntries) {
            if (STRUCTURIZR_PROPERTY_ANNOTATION.equals(annotationEntry.getAnnotationType())) {
                String name = annotationEntry.getElementValuePairs()[0].getValue().stringifyValue();
                String value = annotationEntry.getElementValuePairs()[1].getValue().stringifyValue();
                properties.put(name, value);
            } else if (STRUCTURIZR_PROPERTIES_ANNOTATION.equals(annotationEntry.getAnnotationType())) {
                ArrayElementValue arrayElementValue = (ArrayElementValue)annotationEntry.getElementValuePairs()[0].getValue();
                for (ElementValue elementValue : arrayElementValue.getElementValuesArray()) {
                    AnnotationElementValue annotationElementValue = (AnnotationElementValue)elementValue;
                    AnnotationEntry tagAannotationEntry = annotationElementValue.getAnnotationEntry();

                    String name = tagAannotationEntry.getElementValuePairs()[0].getValue().stringifyValue();
                    String value = tagAannotationEntry.getElementValuePairs()[1].getValue().stringifyValue();
                    properties.put(name, value);
                }
            }
        }

        return properties;
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
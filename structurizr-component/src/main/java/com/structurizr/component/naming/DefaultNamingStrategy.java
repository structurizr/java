package com.structurizr.component.naming;

import com.structurizr.component.Type;

/**
 * Uses Apache commons-lang to split a camel/Pascal cased name into separate words
 * (e.g. "CustomerRepository" -> "Customer Repository").
 */
public class DefaultNamingStrategy implements NamingStrategy {

    @Override
    public String nameOf(Type type) {
        String[] parts = org.apache.commons.lang3.StringUtils.splitByCharacterTypeCamelCase(type.getName());
        return String.join(" ", parts);
    }

    @Override
    public String toString() {
        return "DefaultNamingStrategy{}";
    }

}
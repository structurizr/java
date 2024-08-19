package com.structurizr.component.naming;

import com.structurizr.component.Type;

/**
 * Uses Apache commons-lang to split a camel-cased package name into separate words
 * (e.g. "com.example.order.package-info" -> "Order").
 */
public class DefaultPackageNamingStrategy implements NamingStrategy {

    @Override
    public String nameOf(Type type) {
        String packageName = type.getPackageName();
        if (packageName.contains(".")) {
            packageName = packageName.substring(packageName.lastIndexOf(".") + 1);
        }

        String[] parts = org.apache.commons.lang3.StringUtils.splitByCharacterTypeCamelCase(packageName);
        String name = String.join(" ", parts);
        name = name.substring(0, 1).toUpperCase() + name.substring(1);

        return name;
    }

}
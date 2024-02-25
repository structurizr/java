package com.structurizr.dsl;

/**
 * @param uniqueConstantsDeclaration If set to true, then constants declared with the !constant keyword cannot be overridden.
 */
public record StructurizrDslParserOptions(
        boolean uniqueConstantsDeclaration
) {
}

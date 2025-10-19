package com.structurizr.dsl;

import java.io.File;

final class DslParserContext extends DslContext {

    private final StructurizrDslParser parser;
    private final File file;

    DslParserContext(StructurizrDslParser parser, File file) {
        this.parser = parser;
        this.file = file;
    }

    StructurizrDslParser getParser() {
        return parser;
    }

    File getFile() {
        return file;
    }

    void copyFrom(IdentifiersRegister identifersRegister) {
        for (String identifier : identifersRegister.getElementIdentifiers()) {
            this.identifiersRegister.register(identifier, identifersRegister.getElement(identifier));
        }

        for (String identifier : identifersRegister.getRelationshipIdentifiers()) {
            this.identifiersRegister.register(identifier, identifersRegister.getRelationship(identifier));
        }
    }

    @Override
    protected String[] getPermittedTokens() {
        return new String[0];
    }

}
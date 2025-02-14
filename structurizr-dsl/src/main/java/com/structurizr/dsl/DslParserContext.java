package com.structurizr.dsl;

import java.io.File;

final class DslParserContext extends DslContext {

    private final StructurizrDslParser parser;
    private final File file;
    private final boolean restricted;

    DslParserContext(StructurizrDslParser parser, File file, boolean restricted) {
        this.parser = parser;
        this.file = file;
        this.restricted = restricted;
    }

    StructurizrDslParser getParser() {
        return parser;
    }

    File getFile() {
        return file;
    }

    boolean isRestricted() {
        return restricted;
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
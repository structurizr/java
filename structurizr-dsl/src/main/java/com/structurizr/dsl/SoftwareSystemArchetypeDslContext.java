package com.structurizr.dsl;

final class SoftwareSystemArchetypeDslContext extends ElementArchetypeDslContext {

    SoftwareSystemArchetypeDslContext(Archetype archetype) {
        super(archetype);
    }

    @Override
    protected String[] getPermittedTokens() {
        return new String[] {
            StructurizrDslTokens.DESCRIPTION_TOKEN,
            StructurizrDslTokens.TAG_TOKEN,
            StructurizrDslTokens.TAGS_TOKEN,
            StructurizrDslTokens.PROPERTIES_TOKEN
        };
    }

}
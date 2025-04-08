package com.structurizr.dsl;

final class CustomElementArchetypeDslContext extends ElementArchetypeDslContext {

    CustomElementArchetypeDslContext(Archetype archetype) {
        super(archetype);
    }

    @Override
    protected String[] getPermittedTokens() {
        return new String[] {
            StructurizrDslTokens.METADATA_TOKEN,
            StructurizrDslTokens.DESCRIPTION_TOKEN,
            StructurizrDslTokens.TAG_TOKEN,
            StructurizrDslTokens.TAGS_TOKEN,
            StructurizrDslTokens.PROPERTIES_TOKEN,
            StructurizrDslTokens.PERSPECTIVES_TOKEN
        };
    }

}
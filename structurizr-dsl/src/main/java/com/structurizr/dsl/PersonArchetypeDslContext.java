package com.structurizr.dsl;

final class PersonArchetypeDslContext extends ElementArchetypeDslContext {

    PersonArchetypeDslContext(Archetype archetype) {
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
package com.structurizr.dsl;

final class ContainerArchetypeDslContext extends ArchetypeDslContext {

    ContainerArchetypeDslContext(Archetype archetype) {
        super(archetype);
    }

    @Override
    protected String[] getPermittedTokens() {
        return new String[] {
            StructurizrDslTokens.DESCRIPTION_TOKEN,
            StructurizrDslTokens.TECHNOLOGY_TOKEN,
            StructurizrDslTokens.TAG_TOKEN,
            StructurizrDslTokens.TAGS_TOKEN,
        };
    }

}
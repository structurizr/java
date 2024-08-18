package com.structurizr.dsl;

final class ModelDslContext extends GroupableDslContext {

    ModelDslContext() {
        super(null);
    }

    ModelDslContext(ElementGroup group) {
        super(group);
    }

    @Override
    protected String[] getPermittedTokens() {
        return new String[] {
                StructurizrDslTokens.IDENTIFIERS_TOKEN,
                StructurizrDslTokens.GROUP_TOKEN,
                StructurizrDslTokens.PERSON_TOKEN,
                StructurizrDslTokens.SOFTWARE_SYSTEM_TOKEN,
                StructurizrDslTokens.DEPLOYMENT_ENVIRONMENT_TOKEN,
                StructurizrDslTokens.CUSTOM_ELEMENT_TOKEN,
                StructurizrDslTokens.RELATIONSHIP_TOKEN
        };
    }

}
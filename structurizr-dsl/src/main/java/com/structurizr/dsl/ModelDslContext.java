package com.structurizr.dsl;

final class ModelDslContext extends DslContext implements GroupableDslContext {

    private ElementGroup group;

    ModelDslContext() {
    }

    ModelDslContext(ElementGroup group) {
        this.group = group;
    }

    @Override
    public boolean hasGroup() {
        return group != null;
    }

    @Override
    public ElementGroup getGroup() {
        return group;
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
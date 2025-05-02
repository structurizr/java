package com.structurizr.dsl;

final class DeploymentEnvironmentDslContext extends DslContext implements GroupableDslContext {

    private final DeploymentEnvironment environment;
    private final ElementGroup group;

    DeploymentEnvironmentDslContext(String environment) {
        this.environment = new DeploymentEnvironment(environment);
        this.group = null;
    }

    DeploymentEnvironmentDslContext(String environment, ElementGroup group) {
        this.environment = new DeploymentEnvironment(environment);
        this.group = group;
    }

    DeploymentEnvironment getEnvironment() {
        return environment;
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
                StructurizrDslTokens.GROUP_TOKEN,
                StructurizrDslTokens.DEPLOYMENT_GROUP_TOKEN,
                StructurizrDslTokens.DEPLOYMENT_NODE_TOKEN,
                StructurizrDslTokens.RELATIONSHIP_TOKEN
        };
    }

}
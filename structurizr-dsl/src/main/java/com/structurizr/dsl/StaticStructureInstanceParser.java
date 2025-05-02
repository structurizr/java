package com.structurizr.dsl;

import com.structurizr.model.Element;

import java.util.HashSet;
import java.util.Set;

abstract class StaticStructureInstanceParser extends AbstractParser {

    protected Set<String> getDeploymentGroups(DeploymentNodeDslContext context, String token) {
        Set<String> deploymentGroups = new HashSet<>();
        String[] deploymentGroupReferences = token.split(",");
        for (String deploymentGroupReference : deploymentGroupReferences) {
            Element e = context.getElement(deploymentGroupReference, DeploymentGroup.class);

            if (e == null) {
                // try to find deployment group via hierarchical identifier
                String deploymentEnvironmentName = context.getDeploymentNode().getEnvironment();
                String deploymentEnvironmentIdentifier = context.findIdentifier(new DeploymentEnvironment(deploymentEnvironmentName));

                e = context.getElement(deploymentEnvironmentIdentifier + "." + deploymentGroupReference, DeploymentGroup.class);
            }

            if (e instanceof DeploymentGroup) {
                deploymentGroups.add(e.getName());
            } else {
                // backwards compatibility - deployment environment name rather than identifier
            }
        }

        return deploymentGroups;
    }

}
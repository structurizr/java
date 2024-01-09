package com.structurizr.validation;

import com.structurizr.Workspace;
import com.structurizr.model.Model;
import com.structurizr.model.SoftwareSystem;

/**
 * Validates that the workspace does not define containers and software system level documentation.
 */
public class LandscapeWorkspaceScopeValidator implements WorkspaceScopeValidator {

    @Override
    public void validate(Workspace workspace) throws WorkspaceScopeValidationException {
        Model model = workspace.getModel();
        for (SoftwareSystem softwareSystem : model.getSoftwareSystems()) {
            if (softwareSystem.getContainers().size() > 0) {
                throw new WorkspaceScopeValidationException("Workspace is landscape scoped, but the software system named " + softwareSystem.getName() + " has containers.");
            }

            if (!softwareSystem.getDocumentation().isEmpty()) {
                throw new WorkspaceScopeValidationException("Workspace is landscape scoped, but the software system named " + softwareSystem.getName() + " has documentation.");
            }
        }
    }

}
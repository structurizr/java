package com.structurizr.validation;

import com.structurizr.Workspace;
import com.structurizr.model.Model;

/**
 * Validates that the workspace only defines detail (containers and documentation) for a single software system.
 */
public class SoftwareSystemWorkspaceScopeValidator implements WorkspaceScopeValidator {

    @Override
    public void validate(Workspace workspace) throws WorkspaceScopeValidationException {
        Model model = workspace.getModel();
        long softwareSystemsWithContainersOrDocumentation = model.getSoftwareSystems().stream().filter(ss -> ss.getContainers().size() > 0 || !ss.getDocumentation().isEmpty()).count();

        if (softwareSystemsWithContainersOrDocumentation > 1) {
            throw new WorkspaceScopeValidationException("Workspace is software system scoped, but multiple software systems have containers and/or documentation defined.");
        }
    }

}
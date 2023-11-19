package com.structurizr.validation;

import com.structurizr.Workspace;

public class UndefinedWorkspaceScopeValidator implements WorkspaceScopeValidator {

    @Override
    public void validate(Workspace workspace) throws WorkspaceScopeValidationException {
        // no-op
    }

}
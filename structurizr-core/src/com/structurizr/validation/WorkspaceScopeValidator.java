package com.structurizr.validation;

import com.structurizr.Workspace;

public interface WorkspaceScopeValidator {

    void validate(Workspace workspace) throws WorkspaceScopeValidationException;

}
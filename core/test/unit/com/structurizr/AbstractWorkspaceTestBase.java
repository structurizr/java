package com.structurizr;

import com.structurizr.model.Model;

public class AbstractWorkspaceTestBase {

    protected Workspace workspace = new Workspace("Name", "Description");
    protected Model model = workspace.getModel();

}

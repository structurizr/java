package com.structurizr;

import com.structurizr.model.Model;
import com.structurizr.view.ViewSet;

public class AbstractWorkspaceTestBase {

    protected Workspace workspace = new Workspace("Name", "Description");
    protected Model model = workspace.getModel();
    protected ViewSet views = workspace.getViews();

}

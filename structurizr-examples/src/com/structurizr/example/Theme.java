package com.structurizr.example;

import com.structurizr.Workspace;
import com.structurizr.api.StructurizrClient;
import com.structurizr.model.Model;
import com.structurizr.model.Person;
import com.structurizr.model.SoftwareSystem;
import com.structurizr.view.SystemContextView;
import com.structurizr.view.ViewSet;

/**
 * This is an example of how to use an external theme.
 *
 * The live workspace is available to view at https://structurizr.com/share/38898
 */
public class Theme {

    private static final long WORKSPACE_ID = 38898;
    private static final String API_KEY = "";
    private static final String API_SECRET = "";

    public static void main(String[] args) throws Exception {
        Workspace workspace = new Workspace("Theme", "This is a model of my software system.");
        Model model = workspace.getModel();

        Person user = model.addPerson("User", "A user of my software system.");
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "My software system.");
        user.uses(softwareSystem, "Uses");

        ViewSet views = workspace.getViews();
        SystemContextView contextView = views.createSystemContextView(softwareSystem, "SystemContext", "An example of a System Context diagram.");
        contextView.addAllSoftwareSystems();
        contextView.addAllPeople();

        // add a theme
        views.getConfiguration().setThemes("https://raw.githubusercontent.com/structurizr/java/master/structurizr-examples/src/com/structurizr/example/theme/theme.json");

        StructurizrClient structurizrClient = new StructurizrClient(API_KEY, API_SECRET);
        structurizrClient.putWorkspace(WORKSPACE_ID, workspace);
    }

}
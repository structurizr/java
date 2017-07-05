package com.structurizr.example.core;

import com.structurizr.Workspace;
import com.structurizr.api.StructurizrClient;
import com.structurizr.model.*;
import com.structurizr.view.ContainerView;
import com.structurizr.view.Styles;
import com.structurizr.view.ViewSet;

/**
 * An example of how to style relationships on diagrams.
 *
 * The live workspace is available to view at https://structurizr.com/share/36131
 */
public class StylingRelationships {

    private static final long WORKSPACE_ID = 36131;
    private static final String API_KEY = "";
    private static final String API_SECRET = "";

    public static void main(String[] args) throws Exception {
        Workspace workspace = new Workspace("Styling Relationships", "This is a model of my software system.");
        Model model = workspace.getModel();

        Person user = model.addPerson("User", "A user of my software system.");
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "My software system.");
        Container webApplication = softwareSystem.addContainer("Web Application", "My web application.", "Java and Spring MVC");
        Container database = softwareSystem.addContainer("Database", "My database.", "Relational database schema");
        user.uses(webApplication, "Uses", "HTTPS");
        webApplication.uses(database, "Reads from and writes to", "JDBC");

        ViewSet viewSet = workspace.getViews();
        ContainerView containerView = viewSet.createContainerView(softwareSystem, "containers", "An example of a container diagram.");
        containerView.addAllElements();

        Styles styles = workspace.getViews().getConfiguration().getStyles();

        // example 1
//        styles.addRelationshipStyle(Tags.RELATIONSHIP).color("#ff0000");

        // example 2
//        model.getRelationships().stream().filter(r -> "HTTPS".equals(r.getTechnology())).forEach(r -> r.addTags("HTTPS"));
//        model.getRelationships().stream().filter(r -> "JDBC".equals(r.getTechnology())).forEach(r -> r.addTags("JDBC"));
//        styles.addRelationshipStyle("HTTPS").color("#ff0000");
//        styles.addRelationshipStyle("JDBC").color("#0000ff");

        StructurizrClient structurizrClient = new StructurizrClient(API_KEY, API_SECRET);
        structurizrClient.putWorkspace(WORKSPACE_ID, workspace);
    }

}
package com.structurizr.example.core;

import com.structurizr.Workspace;
import com.structurizr.api.StructurizrClient;
import com.structurizr.model.*;
import com.structurizr.view.ContainerView;
import com.structurizr.view.Shape;
import com.structurizr.view.Styles;
import com.structurizr.view.ViewSet;

/**
 * An example of how to style elements on diagrams.
 *
 * The live workspace is available to view at https://structurizr.com/share/36111
 */
public class StylingElements {

    private static final long WORKSPACE_ID = 36111;
    private static final String API_KEY = "";
    private static final String API_SECRET = "";

    public static void main(String[] args) throws Exception {
        Workspace workspace = new Workspace("Styling Elements", "This is a model of my software system.");
        Model model = workspace.getModel();

        Person user = model.addPerson("User", "A user of my software system.");
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "My software system.");
        Container webApplication = softwareSystem.addContainer("Web Application", "My web application.", "Java and Spring MVC");
        Container database = softwareSystem.addContainer("Database", "My database.", "Relational database schema");
        user.uses(webApplication, "Uses", "HTTPS");
        webApplication.uses(database, "Reads from and writes to", "JDBC");

        ViewSet views = workspace.getViews();
        ContainerView containerView = views.createContainerView(softwareSystem, "containers", "An example of a container diagram.");
        containerView.addAllElements();

        Styles styles = workspace.getViews().getConfiguration().getStyles();

        // example 1
//        styles.addElementStyle(Tags.ELEMENT).background("#438dd5").color("#ffffff");

        // example 2
//        styles.addElementStyle(Tags.ELEMENT).color("#ffffff");
//        styles.addElementStyle(Tags.PERSON).background("#08427b");
//        styles.addElementStyle(Tags.CONTAINER).background("#438dd5");

        // example 3
//        styles.addElementStyle(Tags.ELEMENT).color("#ffffff");
//        styles.addElementStyle(Tags.PERSON).background("#08427b").shape(Shape.Person);
//        styles.addElementStyle(Tags.CONTAINER).background("#438dd5");
//        database.addTags("Database");
//        styles.addElementStyle("Database").shape(Shape.Cylinder);

        StructurizrClient structurizrClient = new StructurizrClient(API_KEY, API_SECRET);
        structurizrClient.putWorkspace(WORKSPACE_ID, workspace);
    }

}
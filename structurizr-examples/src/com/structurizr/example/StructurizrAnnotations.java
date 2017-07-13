package com.structurizr.example;

import com.structurizr.Workspace;
import com.structurizr.analysis.ComponentFinder;
import com.structurizr.analysis.StructurizrAnnotationsComponentFinderStrategy;
import com.structurizr.api.StructurizrClient;
import com.structurizr.model.*;
import com.structurizr.view.*;

/**
 * An small example that illustrates how to use the Structurizr annotations
 * in conjunction with the StructurizrAnnotationsComponentFinderStrategy.
 *
 * The live workspace is available to view at https://structurizr.com/share/36571
 */
public class StructurizrAnnotations {

    private static final String DATABASE_TAG = "Database";

    private static final long WORKSPACE_ID = 36571;
    private static final String API_KEY = "";
    private static final String API_SECRET = "";

    public static void main(String[] args) throws Exception {
        Workspace workspace = new Workspace("Structurizr for Java Annotations", "This is a model of my software system.");
        Model model = workspace.getModel();

        Person user = model.addPerson("User", "A user of my software system.");
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "My software system.");

        Container webApplication = softwareSystem.addContainer("Web Application", "Provides users with information.", "Java");
        Container database = softwareSystem.addContainer("Database", "Stores information.", "Relational database schema");
        database.addTags(DATABASE_TAG);

        ComponentFinder componentFinder = new ComponentFinder(
                webApplication,
                "com.structurizr.example.annotations",
                new StructurizrAnnotationsComponentFinderStrategy()
        );
        componentFinder.findComponents();
        model.addImplicitRelationships();

        ViewSet views = workspace.getViews();
        SystemContextView contextView = views.createSystemContextView(softwareSystem, "SystemContext", "An example of a System Context diagram.");
        contextView.addAllElements();

        ContainerView containerView = views.createContainerView(softwareSystem, "Containers", "The container diagram from my software system.");
        containerView.addAllElements();

        ComponentView componentView = views.createComponentView(webApplication, "Components", "The component diagram for the web application.");
        componentView.addAllElements();

        Styles styles = views.getConfiguration().getStyles();
        styles.addElementStyle(Tags.ELEMENT).color("#ffffff");
        styles.addElementStyle(Tags.SOFTWARE_SYSTEM).background("#1168bd");
        styles.addElementStyle(Tags.CONTAINER).background("#438dd5");
        styles.addElementStyle(Tags.COMPONENT).background("#85bbf0").color("#000000");
        styles.addElementStyle(Tags.PERSON).background("#08427b").shape(Shape.Person);
        styles.addElementStyle(DATABASE_TAG).shape(Shape.Cylinder);

        StructurizrClient structurizrClient = new StructurizrClient(API_KEY, API_SECRET);
        structurizrClient.putWorkspace(WORKSPACE_ID, workspace);
    }

}
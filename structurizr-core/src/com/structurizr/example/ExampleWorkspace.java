package com.structurizr.example;

import com.structurizr.Workspace;
import com.structurizr.model.*;
import com.structurizr.view.*;

/**
 * This is the example workspace created when a user adds a new workspace in Structurizr.
 */
public class ExampleWorkspace {

    private static final String DATABASE_TAG = "Database";

    public static Workspace create(boolean useShapes) {
        Workspace workspace = new Workspace("Example workspace", "This is an example workspace - drag the elements around to organise them on the diagram.");
        Model model = workspace.getModel();

        Person user = model.addPerson("User", "A user of your software system.");
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "Your software system.");
        user.uses(softwareSystem, "Uses");

        Container webApplication = softwareSystem.addContainer("Web Application", "Does interesting things.", "Apache Tomcat");
        Container database = softwareSystem.addContainer("Database", "Stores interesting data.", "MySQL");
        database.addTags(DATABASE_TAG);
        user.uses(webApplication, "Uses");
        webApplication.uses(database, "Reads from and writes to");

        // create some views
        ViewSet viewSet = workspace.getViews();
        SystemContextView contextView = viewSet.createContextView(softwareSystem);
        contextView.addAllElements();
        contextView.setPaperSize(PaperSize.A5_Landscape);

        ContainerView containerView = viewSet.createContainerView(softwareSystem);
        containerView.addAllElements();
        containerView.setPaperSize(PaperSize.A5_Landscape);

        // tag and style some elements
        Styles styles = workspace.getViews().getConfiguration().getStyles();
        styles.add(new ElementStyle(Tags.SOFTWARE_SYSTEM, null, null, "#A4B7C9", "#000000", null));
        styles.add(new ElementStyle(Tags.PERSON, null, null, "#728da5", "#ffffff", null, useShapes ? Shape.Person : null));
        styles.add(new ElementStyle(Tags.CONTAINER, null, null, "#2A4E6E", "#ffffff", null));
        if (useShapes) {
            styles.add(new ElementStyle(DATABASE_TAG, null, null, null, null, null, Shape.Cylinder));
        }

        return workspace;
    }

}
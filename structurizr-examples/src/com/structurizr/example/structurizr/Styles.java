package com.structurizr.example.structurizr;

import com.structurizr.Workspace;
import com.structurizr.model.Tags;
import com.structurizr.view.*;

/**
 * Adds some styling to the views.
 */
public class Styles extends AbstractStructurizrWorkspace {

    public static void main(String[] args) throws Exception {
        new Styles().run();
    }

    void run() throws Exception {
        Workspace workspace = readFromFile();
        ViewSet views = workspace.getViews();

        views.getConfiguration().getStyles().addElementStyle(Tags.ELEMENT).color("#ffffff");
        views.getConfiguration().getStyles().addElementStyle(Tags.SOFTWARE_SYSTEM).background("#08427b").shape(Shape.RoundedBox);
        views.getConfiguration().getStyles().addElementStyle(STRUCTURIZR).background("#1168bd");
        views.getConfiguration().getStyles().addElementStyle(Tags.PERSON).background("#02172c").shape(Shape.Person);
        views.getConfiguration().getStyles().addElementStyle(Tags.CONTAINER).background("#438dd5");
        views.getConfiguration().getStyles().addElementStyle(DATABASE_TAG).shape(Shape.Cylinder);
        views.getConfiguration().getStyles().addElementStyle(Tags.COMPONENT).background("#85bbf0").color("#000000").fontSize(16);

        writeToFile(workspace);
    }

}
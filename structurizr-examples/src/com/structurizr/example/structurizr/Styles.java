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
        views.getConfiguration().getStyles().addElementStyle(Tags.SOFTWARE_SYSTEM).background("#728DA5").shape(Shape.RoundedBox);
        views.getConfiguration().getStyles().addElementStyle(STRUCTURIZR).background("#011221");
        views.getConfiguration().getStyles().addElementStyle(Tags.PERSON).background("#2a4e6e").shape(Shape.Person);
        views.getConfiguration().getStyles().addElementStyle(Tags.CONTAINER).background("#041f37");
        views.getConfiguration().getStyles().addElementStyle(DATABASE_TAG).shape(Shape.Cylinder);
        views.getConfiguration().getStyles().addElementStyle(Tags.COMPONENT).background("#d4dee7").color("#000000").fontSize(16);

        writeToFile(workspace);
    }

}
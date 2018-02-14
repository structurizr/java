package com.structurizr.example;

import com.structurizr.Workspace;
import com.structurizr.api.StructurizrClient;
import com.structurizr.model.Model;
import com.structurizr.model.Tags;
import com.structurizr.view.*;

/**
 * An example illustrating all of the shapes available in Structurizr.
 *
 * The live workspace is available to view at https://structurizr.com/share/12541
 */
public class Shapes {

    private static final long WORKSPACE_ID = 12541;
    private static final String API_KEY = "";
    private static final String API_SECRET = "";

    public static void main(String[] args) throws Exception {
        Workspace workspace = new Workspace("Shapes", "An example of all shapes available in Structurizr.");
        Model model = workspace.getModel();

        model.addSoftwareSystem("Box", "Description").addTags("Box");
        model.addSoftwareSystem("RoundedBox", "Description").addTags("RoundedBox");
        model.addSoftwareSystem("Ellipse", "Description").addTags("Ellipse");
        model.addSoftwareSystem("Circle", "Description").addTags("Circle");
        model.addSoftwareSystem("Hexagon", "Description").addTags("Hexagon");
        model.addSoftwareSystem("Cylinder", "Description").addTags("Cylinder");
        model.addSoftwareSystem("Pipe", "Description").addTags("Pipe");
        model.addSoftwareSystem("Folder", "Description").addTags("Folder");
        model.addPerson("Person", "Description").addTags("Person");

        ViewSet views = workspace.getViews();
        SystemLandscapeView view = views.createSystemLandscapeView("shapes", "An example of all shapes available in Structurizr.");
        view.addAllElements();
        view.setPaperSize(PaperSize.A5_Landscape);

        Styles styles = views.getConfiguration().getStyles();

        styles.addElementStyle(Tags.ELEMENT).color("#ffffff").background("#438dd5").fontSize(34).width(650).height(400);
        styles.addElementStyle("Box").shape(Shape.Box);
        styles.addElementStyle("RoundedBox").shape(Shape.RoundedBox);
        styles.addElementStyle("Ellipse").shape(Shape.Ellipse);
        styles.addElementStyle("Circle").shape(Shape.Circle);
        styles.addElementStyle("Cylinder").shape(Shape.Cylinder);
        styles.addElementStyle("Pipe").shape(Shape.Pipe);
        styles.addElementStyle("Folder").shape(Shape.Folder);
        styles.addElementStyle("Hexagon").shape(Shape.Hexagon);
        styles.addElementStyle("Person").shape(Shape.Person).width(550);

        StructurizrClient structurizrClient = new StructurizrClient(API_KEY, API_SECRET);
        structurizrClient.putWorkspace(WORKSPACE_ID, workspace);
    }

}
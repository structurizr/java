package com.structurizr.example.core;

import com.structurizr.Workspace;
import com.structurizr.api.StructurizrClient;
import com.structurizr.model.Model;
import com.structurizr.model.Tags;
import com.structurizr.view.Shape;
import com.structurizr.view.Styles;
import com.structurizr.view.ViewSet;

/**
 * A simple example of the shapes available in Structurizr.
 * See the live diagram at https://structurizr.com/workspace/12541
 */
public class ShapesExample {

    public static void main(String[] args) throws Exception {
        Workspace workspace = new Workspace("Shapes", "An example of all shapes available in Structurizr.");
        Model model = workspace.getModel();

        model.addSoftwareSystem("Box", "Description").addTags("Box");
        model.addSoftwareSystem("RoundedBox", "Description").addTags("RoundedBox");
        model.addSoftwareSystem("Ellipse", "Description").addTags("Ellipse");
        model.addSoftwareSystem("Circle", "Description").addTags("Circle");
        model.addSoftwareSystem("Hexagon", "Description").addTags("Hexagon");
        model.addSoftwareSystem("Cylinder", "Description").addTags("Cylinder");
        model.addPerson("Person", "Description").addTags("Person");

        // tag and style some elements
        ViewSet viewSet = workspace.getViews();
        Styles styles = viewSet.getConfiguration().getStyles();

        styles.addElementStyle(Tags.ELEMENT).color("#ffffff").background("#438dd5").fontSize(34).width(650).height(400);
        styles.addElementStyle("Box").shape(Shape.Box);
        styles.addElementStyle("RoundedBox").shape(Shape.RoundedBox);
        styles.addElementStyle("Ellipse").shape(Shape.Ellipse);
        styles.addElementStyle("Circle").shape(Shape.Circle);
        styles.addElementStyle("Cylinder").shape(Shape.Cylinder);
        styles.addElementStyle("Hexagon").shape(Shape.Hexagon);
        styles.addElementStyle("Person").shape(Shape.Person).width(550);

        // create some views
        viewSet.createEnterpriseContextView("Diagram", "An example diagram.").addAllElements();

        StructurizrClient structurizrClient = new StructurizrClient("key", "secret");
        structurizrClient.mergeWorkspace(12541, workspace);

    }

}

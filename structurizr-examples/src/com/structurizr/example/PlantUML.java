package com.structurizr.example;

import com.structurizr.Workspace;
import com.structurizr.io.plantuml.PlantUMLWriter;
import com.structurizr.model.Model;
import com.structurizr.model.Person;
import com.structurizr.model.SoftwareSystem;
import com.structurizr.model.Tags;
import com.structurizr.view.PaperSize;
import com.structurizr.view.Shape;
import com.structurizr.view.Styles;
import com.structurizr.view.SystemContextView;
import com.structurizr.view.ViewSet;

import java.io.StringWriter;

/**
 * An example of how to use the PlantUML writer. Run this program and copy/paste
 * the output into http://www.plantuml.com/plantuml/
 */
public class PlantUML {

    public static void main(String[] args) throws Exception {
        Workspace workspace = new Workspace("Getting Started", "This is a model of my software system.");
        Model model = workspace.getModel();

        Person user = model.addPerson("User", "A user of my software system.");
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "My software system.");
        user.uses(softwareSystem, "Uses");

        ViewSet views = workspace.getViews();
        SystemContextView contextView = views.createSystemContextView(softwareSystem, "SystemContext", "An example of a System Context diagram.");
        contextView.addAllSoftwareSystems();
        contextView.addAllPeople();
        contextView.setPaperSize(PaperSize.Slide_16_9);

//        Styles styles = views.getConfiguration().getStyles();
//        styles.addElementStyle(Tags.SOFTWARE_SYSTEM).background("#1168bd").color("#ffffff");
//        styles.addElementStyle(Tags.PERSON).background("#08427b").color("#ffffff").shape(Shape.Person);

        StringWriter stringWriter = new StringWriter();
        PlantUMLWriter plantUMLWriter = new PlantUMLWriter();

        // if you're using dark background colours, you might need to explicitly set the foreground colour using skin params
        // e.g. rectangleFontColor, rectangleFontColor<<Software System>>, etc
//        plantUMLWriter.addSkinParam("rectangleFontColor", "#ffffff");
//        plantUMLWriter.addSkinParam("rectangleStereotypeFontColor", "#ffffff");

        plantUMLWriter.write(workspace, stringWriter);
        System.out.println(stringWriter.toString());
    }

}

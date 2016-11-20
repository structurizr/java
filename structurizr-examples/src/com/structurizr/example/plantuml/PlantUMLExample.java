package com.structurizr.example.plantuml;

import com.structurizr.Workspace;
import com.structurizr.io.plantuml.PlantUMLWriter;
import com.structurizr.model.Model;
import com.structurizr.model.Person;
import com.structurizr.model.SoftwareSystem;
import com.structurizr.view.SystemContextView;
import com.structurizr.view.ViewSet;

import java.io.StringWriter;

/**
 * A simple example of how to use the PlantUML writer. Run this program and copy/paste
 * the output into http://www.plantuml.com/plantuml/
 */
public class PlantUMLExample {

    public static void main(String[] args) throws Exception {
        Workspace workspace = new Workspace("PlantUML", "An example workspace that demonstrates the PlantUML writer.");
        Model model = workspace.getModel();
        ViewSet views = workspace.getViews();

        SoftwareSystem softwareSystem = model.addSoftwareSystem("My Software System", "");
        Person user = model.addPerson("User", "");
        user.uses(softwareSystem, "Uses");

        SystemContextView view = views.createSystemContextView(softwareSystem, "context", "A simple system context diagram.");
        view.addAllElements();

        StringWriter stringWriter = new StringWriter();
        PlantUMLWriter plantUMLWriter = new PlantUMLWriter();
        plantUMLWriter.write(workspace, stringWriter);
        System.out.println(stringWriter.toString());
    }

}

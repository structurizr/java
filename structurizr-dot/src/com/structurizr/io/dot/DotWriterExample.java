package com.structurizr.io.dot;

import com.structurizr.Workspace;
import com.structurizr.model.Model;
import com.structurizr.model.Person;
import com.structurizr.model.SoftwareSystem;
import com.structurizr.view.SystemContextView;
import com.structurizr.view.ViewSet;

import java.io.StringWriter;

/**
 * Demonstrates the export to DOT. Paste graphs into https://stamm-wilbrandt.de/GraphvizFiddle/
 * to visualise them.
 */
public class DotWriterExample {

    public static void main(String[] args) {
        Workspace workspace = new Workspace("Getting Started", "This is a model of my software system.");
        Model model = workspace.getModel();

        Person user = model.addPerson("User", "A user of my software system.");
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "My software system.");
        user.uses(softwareSystem, "Uses");

        ViewSet views = workspace.getViews();
        SystemContextView contextView = views.createSystemContextView(softwareSystem, "SystemContext", "An example of a System Context diagram.");
        contextView.addAllSoftwareSystems();
        contextView.addAllPeople();

        StringWriter stringWriter = new StringWriter();
        DotWriter dotWriter = new DotWriter();
        dotWriter.write(workspace, stringWriter);
        System.out.println(stringWriter);
    }

}

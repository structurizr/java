package com.structurizr.example.annotations;

import com.structurizr.Workspace;
import com.structurizr.componentfinder.ComponentFinder;
import com.structurizr.componentfinder.JavadocComponentFinderStrategy;
import com.structurizr.componentfinder.StructurizrAnnotationsComponentFinderStrategy;
import com.structurizr.io.json.JsonWriter;
import com.structurizr.model.*;
import com.structurizr.view.ComponentView;
import com.structurizr.view.ContainerView;
import com.structurizr.view.SystemContextView;
import com.structurizr.view.ViewSet;

import java.io.StringWriter;

public class StructurizrAnnotations {

    public static void main(String[] args) throws Exception  {
        Workspace workspace = new Workspace("Structurizr annotations example", "A simple example of how to use the automated component finders");
        Model model = workspace.getModel();

        SoftwareSystem softwareSystem = model.addSoftwareSystem(Location.Internal, "Example software system", "An example software system");
        Person user = model.addPerson(Location.External, "A user", "An example user");
        user.uses(softwareSystem, "Uses");

        Container webApplication = softwareSystem.addContainer("Web Application", "An example web application", "Apache Tomcat 7.x");
        user.uses(webApplication, "Uses");

        ComponentFinder componentFinder = new ComponentFinder(
                webApplication,
                "com.structurizr.example.annotations",
                new StructurizrAnnotationsComponentFinderStrategy(),
                new JavadocComponentFinderStrategy("/Users/simon/sandbox/structurizr-java/structurizr-examples/src/")
        );
        componentFinder.findComponents();

        // create some views
        ViewSet viewSet = workspace.getViews();
        SystemContextView contextView = viewSet.createContextView(softwareSystem);
        contextView.addAllSoftwareSystems();
        contextView.addAllPeople();

        ContainerView containerView = viewSet.createContainerView(softwareSystem);
        containerView.addAllPeople();
        containerView.addAllSoftwareSystems();
        containerView.addAllContainers();

        ComponentView componentView = viewSet.createComponentView(webApplication);
        componentView.addAllContainers();
        componentView.addAllComponents();

        // and output the model as JSON
        JsonWriter jsonWriter = new JsonWriter(true);
        StringWriter stringWriter = new StringWriter();
        jsonWriter.write(workspace, stringWriter);
        System.out.println(stringWriter.toString());
    }

}

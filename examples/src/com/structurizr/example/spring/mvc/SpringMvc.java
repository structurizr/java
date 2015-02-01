package com.structurizr.example.spring.mvc;

import com.structurizr.Workspace;
import com.structurizr.componentfinder.ComponentFinder;
import com.structurizr.componentfinder.SpringComponentFinderStrategy;
import com.structurizr.io.json.JsonWriter;
import com.structurizr.model.*;
import com.structurizr.view.ComponentView;
import com.structurizr.view.ContainerView;
import com.structurizr.view.SystemContextView;
import com.structurizr.view.ViewSet;

import java.io.StringWriter;

/**
 * A simple Spring MVC example.
 */
public class SpringMvc {

    public static void main(String[] args) throws Exception {
        Workspace workspace = new Workspace("Spring MVC app", "This is a C4 representation of a simple Spring MVC web application");
        Model model = workspace.getModel();

        // create the basic model (the stuff we can't get from the code)
        SoftwareSystem springPetClinic = model.addSoftwareSystem(Location.Internal, "My Spring MVC webapp", "");
        Person user = model.addPerson(Location.External, "User", "");
        user.uses(springPetClinic, "Uses");

        Container webApplication = springPetClinic.addContainer("Web Application", "", "Apache Tomcat 7.x");
        Container relationalDatabase = springPetClinic.addContainer("Relational Database", "", "MySQL");
        user.uses(webApplication, "Uses");
        webApplication.uses(relationalDatabase, "Reads from and writes to");

        // and now automatically find all Spring @Controller, @Component, @Service and @Repository components
        ComponentFinder componentFinder = new ComponentFinder(
                webApplication,
                SpringMvc.class.getPackage().getName(),
                new SpringComponentFinderStrategy());
        componentFinder.findComponents();

        // connect the user to all of the Spring MVC controllers
        webApplication.getComponents().stream().filter(c -> c.getTechnology().equals("Spring Controller")).forEach(c -> user.uses(c, "Uses"));

        // connect all of the repository components to the relational database
        webApplication.getComponents().stream().filter(c -> c.getTechnology().equals("Spring Repository")).forEach(c -> c.uses(relationalDatabase, "Reads from and writes to"));

        // finally create some views
        ViewSet viewSet = workspace.getViews();
        SystemContextView contextView = viewSet.createContextView(springPetClinic);
        contextView.addAllSoftwareSystems();
        contextView.addAllPeople();

        ContainerView containerView = viewSet.createContainerView(springPetClinic);
        containerView.addAllPeople();
        containerView.addAllSoftwareSystems();
        containerView.addAllContainers();

        ComponentView componentView = viewSet.createComponentView(springPetClinic, webApplication);
        componentView.addAllComponents();
        componentView.addAllPeople();
        componentView.add(relationalDatabase);

        JsonWriter jsonWriter = new JsonWriter(true);
        StringWriter stringWriter = new StringWriter();
        jsonWriter.write(workspace, stringWriter);

        System.out.println(stringWriter.toString());
    }

}
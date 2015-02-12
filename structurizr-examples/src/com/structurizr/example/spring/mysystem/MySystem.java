package com.structurizr.example.spring.mysystem;

import com.structurizr.Workspace;
import com.structurizr.api.StructurizrClient;
import com.structurizr.componentfinder.ComponentFinder;
import com.structurizr.componentfinder.SpringComponentFinderStrategy;
import com.structurizr.io.json.JsonWriter;
import com.structurizr.model.*;
import com.structurizr.view.ComponentView;
import com.structurizr.view.ContainerView;
import com.structurizr.view.SystemContextView;
import com.structurizr.view.ViewSet;

import java.io.StringWriter;

public class MySystem {

    public static void main(String[] args) throws Exception {
        // create the workspace
        Workspace workspace = new Workspace("My system", "This is a model of my software system.");
        Model model = workspace.getModel();

        SoftwareSystem mySystem = model.addSoftwareSystem(Location.Internal, "My system",
                "Do something cool");
        Person user = model.addPerson(Location.External, "User", "Somebody on the Internet");
        user.uses(mySystem, "Does something cool with");

        Container webApplication = mySystem.addContainer("Web Application", "My web application",
                "Apache Tomcat 7");
        Container database = mySystem.addContainer("Database", "Stores something", "MySQL");

        webApplication.uses(database, "Reads from and writes to");
        user.uses(webApplication, "Does something using");

        // and now automatically find all Spring @Controller, @Component, @Service and @Repository components
        // - this will ignore the domain and util classes as they're not considered to be "architecturally significant" building blocks
        ComponentFinder componentFinder = new ComponentFinder(
            webApplication,
            MySystem.class.getPackage().getName(),
            new SpringComponentFinderStrategy());
        componentFinder.findComponents();

        // connect the user to all of the Spring MVC controllers
        webApplication.getComponents().stream().filter(c -> c.getTechnology().equals("Spring Controller")).forEach(c -> user.uses(c, "Uses"));

        // connect all of the repository components to the relational database
        webApplication.getComponents().stream().filter(c -> c.getTechnology().equals("Spring Repository")).forEach(c -> c.uses(database, "Reads from and writes to"));


        // create some views
        ViewSet viewSet = workspace.getViews();
        SystemContextView contextView = viewSet.createContextView(mySystem);
        contextView.addAllSoftwareSystems();
        contextView.addAllPeople();

        ContainerView containerView = viewSet.createContainerView(mySystem);
        containerView.addAllPeople();
        containerView.addAllSoftwareSystems();
        containerView.addAllContainers();

        ComponentView componentView = viewSet.createComponentView(mySystem, webApplication);
        componentView.addAllPeople();
        componentView.addAllSoftwareSystems();
        componentView.addAllContainers();
        componentView.addAllComponents();

        // output the model as JSON
        JsonWriter jsonWriter = new JsonWriter(true);
        StringWriter stringWriter = new StringWriter();
        jsonWriter.write(workspace, stringWriter);
        System.out.println(stringWriter.toString());
    }

}
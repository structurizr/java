package com.structurizr.example.spring.petclinic;

import com.structurizr.Workspace;
import com.structurizr.api.StructurizrClient;
import com.structurizr.componentfinder.ComponentFinder;
import com.structurizr.componentfinder.SpringComponentFinderStrategy;
import com.structurizr.io.json.JsonWriter;
import com.structurizr.model.*;
import com.structurizr.view.*;

import java.io.StringWriter;

/**
 * This is a C4 representation of the Spring PetClinic sample app
 * (https://github.com/spring-projects/spring-petclinic/).
 *
 * Use the examples/springpetclinic.sh file to run this example -
 * you'll need a compiled version of the app on the CLASSPATH.
 */
public class SpringPetClinic {

    public static void main(String[] args) throws Exception {
        Workspace workspace = new Workspace("Spring PetClinic", "This is a C4 representation of the Spring PetClinic sample app (https://github.com/spring-projects/spring-petclinic/)");
        Model model = workspace.getModel();

        // create the basic model (the stuff we can't get from the code)
        SoftwareSystem springPetClinic = model.addSoftwareSystem(Location.Internal, "Spring PetClinic", "");
        Person user = model.addPerson(Location.External, "User", "");
        user.uses(springPetClinic, "Uses");

        Container webApplication = springPetClinic.addContainer("Web Application", "", "Apache Tomcat 7.x");
        Container relationalDatabase = springPetClinic.addContainer("Relational Database", "", "HSQLDB");
        user.uses(webApplication, "Uses");
        webApplication.uses(relationalDatabase, "Reads from and writes to");

        // and now automatically find all Spring @Controller, @Component, @Service and @Repository components
        ComponentFinder componentFinder = new ComponentFinder(webApplication, "org.springframework.samples.petclinic",
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

        ComponentView componentView = viewSet.createComponentView(webApplication);
        componentView.addAllComponents();
        componentView.addAllPeople();
        componentView.add(relationalDatabase);

        // tag and style some elements
        springPetClinic.addTags("Spring PetClinic");
        webApplication.getComponents().stream().filter(c -> c.getTechnology().equals("Spring Controller")).forEach(c -> c.addTags("Spring Controller"));
        webApplication.getComponents().stream().filter(c -> c.getTechnology().equals("Spring Service")).forEach(c -> c.addTags("Spring Service"));
        webApplication.getComponents().stream().filter(c -> c.getTechnology().equals("Spring Repository")).forEach(c -> c.addTags("Spring Repository"));

        viewSet.getStyles().add(new ElementStyle("Spring PetClinic", null, null, "#6CB33E", "white", null));
        viewSet.getStyles().add(new ElementStyle(Tags.SOFTWARE_SYSTEM, null, null, "#35760A", "white", null));
        viewSet.getStyles().add(new ElementStyle(Tags.PERSON, null, null, "#519823", "white", null));
        viewSet.getStyles().add(new ElementStyle(Tags.CONTAINER, null, null, "#91D366", "white", null));
        viewSet.getStyles().add(new ElementStyle(Tags.COMPONENT, null, null, "#6CB33E", "black", null));
        viewSet.getStyles().add(new ElementStyle("Spring Controller", null, null, "#D4F3C0", "black", null));
        viewSet.getStyles().add(new ElementStyle("Spring Service", null, null, "#6CB33E", "black", null));
        viewSet.getStyles().add(new ElementStyle("Spring Repository", null, null, "#95D46C", "black", null));

        JsonWriter jsonWriter = new JsonWriter(true);
        StringWriter stringWriter = new StringWriter();
        jsonWriter.write(workspace, stringWriter);

        System.out.println(stringWriter.toString());

        StructurizrClient structurizrClient = new StructurizrClient("http://localhost:9090", "eab0b844-3e63-4961-b04e-c5a0b48411b6", "f9b7a3fe-1f39-4956-84ee-a5651cb1160b");
        Workspace oldWorkspace = structurizrClient.getWorkspace(1);
        workspace.getViews().copyLayoutInformationFrom(oldWorkspace.getViews());
        workspace.setId(1);
        structurizrClient.putWorkspace(workspace);
    }

}
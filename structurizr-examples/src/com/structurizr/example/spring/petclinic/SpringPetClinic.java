package com.structurizr.example.spring.petclinic;

import com.structurizr.Workspace;
import com.structurizr.api.StructurizrClient;
import com.structurizr.componentfinder.ComponentFinder;
import com.structurizr.componentfinder.JavadocComponentFinderStrategy;
import com.structurizr.componentfinder.SpringComponentFinderStrategy;
import com.structurizr.io.json.JsonWriter;
import com.structurizr.model.*;
import com.structurizr.view.*;

import java.io.File;
import java.io.StringWriter;

/**
 * This is a C4 representation of the Spring PetClinic sample app
 * (https://github.com/spring-projects/spring-petclinic/).
 *
 * Use the examples/springpetclinic.sh file to run this example -
 * you'll need a compiled version of the app on the CLASSPATH.
 *
 * You can see the resulting diagrams at https://www.structurizr.com/public/1
 */
public class SpringPetClinic {

    public static void main(String[] args) throws Exception {
        Workspace workspace = new Workspace("Spring PetClinic",
                "This is a C4 representation of the Spring PetClinic sample app (https://github.com/spring-projects/spring-petclinic/)");
        Model model = workspace.getModel();

        // create the basic model (the stuff we can't get from the code)
        SoftwareSystem springPetClinic = model.addSoftwareSystem("Spring PetClinic", "Allows employees to view and manage information regarding the veterinarians, the clients, and their pets.");
        Person clinicEmployee = model.addPerson("Clinic Employee", "An employee of the clinic");
        clinicEmployee.uses(springPetClinic, "Uses");

        Container webApplication = springPetClinic.addContainer(
               "Web Application", "Allows employees to view and manage information regarding the veterinarians, the clients, and their pets.", "Apache Tomcat 7.x");
        Container relationalDatabase = springPetClinic.addContainer(
               "Relational Database", "Stores information regarding the veterinarians, the clients, and their pets.", "HSQLDB");
        clinicEmployee.uses(webApplication, "Uses", "HTTP");
        webApplication.uses(relationalDatabase, "Reads from and writes to", "JDBC, port 9001");

        // and now automatically find all Spring @Controller, @Component, @Service and @Repository components
        ComponentFinder componentFinder = new ComponentFinder(
                webApplication, "org.springframework.samples.petclinic",
                new SpringComponentFinderStrategy(),
                new JavadocComponentFinderStrategy(new File("/Users/simon/Documents/sandbox/spring/spring-petclinic/src/main/java/"), 150));
        componentFinder.findComponents();

        // connect the user to all of the Spring MVC controllers
        webApplication.getComponents().stream()
                .filter(c -> c.getTechnology().equals("Spring MVC Controller"))
                .forEach(c -> clinicEmployee.uses(c, "Uses"));

        // connect all of the repository components to the relational database
        webApplication.getComponents().stream()
                .filter(c -> c.getTechnology().equals("Spring Repository"))
                .forEach(c -> c.uses(relationalDatabase, "Reads from and writes to"));

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

        // link the architecture model with the code
        for (Component component : webApplication.getComponents()) {
            if (component.getSourcePath() != null) {
                component.setSourcePath(component.getSourcePath().replace(
                        "/Users/simon/Documents/sandbox/spring/spring-petclinic/",
                        "https://github.com/spring-projects/spring-petclinic/tree/master/"));
            }
        }

        // tag and style some elements
        springPetClinic.addTags("Spring PetClinic");
        webApplication.getComponents().stream().filter(c -> c.getTechnology().equals("Spring MVC Controller")).forEach(c -> c.addTags("Spring MVC Controller"));
        webApplication.getComponents().stream().filter(c -> c.getTechnology().equals("Spring Service")).forEach(c -> c.addTags("Spring Service"));
        webApplication.getComponents().stream().filter(c -> c.getTechnology().equals("Spring Repository")).forEach(c -> c.addTags("Spring Repository"));
        relationalDatabase.addTags("Database");

        viewSet.getConfiguration().getStyles().add(new ElementStyle("Spring PetClinic", null, null, "#6CB33E", "white", null));
        viewSet.getConfiguration().getStyles().add(new ElementStyle(Tags.PERSON, 400, null, "#519823", "white", null, Shape.Person));
        viewSet.getConfiguration().getStyles().add(new ElementStyle(Tags.CONTAINER, null, null, "#91D366", "white", null));
        viewSet.getConfiguration().getStyles().add(new ElementStyle("Database", null, null, null, null, null, Shape.Cylinder));
        viewSet.getConfiguration().getStyles().add(new ElementStyle("Spring MVC Controller", null, null, "#D4F3C0", "black", null));
        viewSet.getConfiguration().getStyles().add(new ElementStyle("Spring Service", null, null, "#6CB33E", "black", null));
        viewSet.getConfiguration().getStyles().add(new ElementStyle("Spring Repository", null, null, "#95D46C", "black", null));

        StructurizrClient structurizrClient = new StructurizrClient("https://api.structurizr.com", "key", "secret");
        structurizrClient.mergeWorkspace(1, workspace);
    }

}
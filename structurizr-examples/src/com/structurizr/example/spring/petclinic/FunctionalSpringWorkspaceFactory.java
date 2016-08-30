package com.structurizr.example.spring.petclinic;

import com.structurizr.Workspace;
import com.structurizr.componentfinder.*;
import com.structurizr.model.*;
import com.structurizr.view.*;

import java.io.File;

public class FunctionalSpringWorkspaceFactory {


    static Workspace buildWorkspace(String sourceRoot) throws Exception {
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
                webApplication,
                "org.springframework.samples.petclinic",
                new SpringComponentFinderStrategy(
                        new FirstImplementationOfInterfaceSupportingTypesStrategy(),
                        new ReferencedTypesSupportingTypesStrategy()
                ),
                new SourceCodeComponentFinderStrategy(new File(sourceRoot, "/src/main/java/"), 150));
        componentFinder.findComponents();

        // connect the user to all of the Spring MVC controllers
        webApplication.getComponents().stream()
                .filter(c -> c.getTechnology().equals(SpringComponentFinderStrategy.SPRING_MVC_CONTROLLER))
                .forEach(c -> clinicEmployee.uses(c, "Uses", "HTTP"));

        // connect all of the repository components to the relational database
        webApplication.getComponents().stream()
                .filter(c -> c.getTechnology().equals(SpringComponentFinderStrategy.SPRING_REPOSITORY))
                .forEach(c -> c.uses(relationalDatabase, "Reads from and writes to", "JDBC"));

        // finally create some views
        ViewSet viewSet = workspace.getViews();
        SystemContextView contextView = viewSet.createSystemContextView(springPetClinic, "context", "The System Context diagram for the Spring PetClinic system.");
        contextView.addAllSoftwareSystems();
        contextView.addAllPeople();

        ContainerView containerView = viewSet.createContainerView(springPetClinic, "containers", "The Containers diagram for the Spring PetClinic system.");
        containerView.addAllPeople();
        containerView.addAllSoftwareSystems();
        containerView.addAllContainers();

        ComponentView componentView = viewSet.createComponentView(webApplication, "components", "The Components diagram for the Spring PetClinic web application.");
        componentView.addAllComponents();
        componentView.addAllPeople();
        componentView.add(relationalDatabase);

        // link the architecture model with the code
        for (Component component : webApplication.getComponents()) {
            String sourcePath = component.getSourcePath();
            if (sourcePath != null) {
                component.setSourcePath(sourcePath.replace(
                        sourceRoot,
                        "https://github.com/spring-projects/spring-petclinic/tree/master"));
            }
        }

        // tag and style some elements
        springPetClinic.addTags("Spring PetClinic");
        webApplication.getComponents().stream().filter(c -> c.getTechnology().equals(SpringComponentFinderStrategy.SPRING_MVC_CONTROLLER)).forEach(c -> c.addTags("Spring MVC Controller"));
        webApplication.getComponents().stream().filter(c -> c.getTechnology().equals(SpringComponentFinderStrategy.SPRING_SERVICE)).forEach(c -> c.addTags("Spring Service"));
        webApplication.getComponents().stream().filter(c -> c.getTechnology().equals(SpringComponentFinderStrategy.SPRING_REPOSITORY)).forEach(c -> c.addTags("Spring Repository"));
        relationalDatabase.addTags("Database");

        Component vetController = webApplication.getComponentWithName("VetController");
        Component clinicService = webApplication.getComponentWithName("ClinicService");
        Component vetRepository = webApplication.getComponentWithName("VetRepository");

//        DynamicView dynamicView = viewSet.createDynamicView(springPetClinic, "viewListOfVets", "Shows how the \"view list of vets\" feature works.");
//        dynamicView.add(clinicEmployee, "Requests list of vets from /vets", vetController);
//        dynamicView.add(vetController, "Calls findVets", clinicService);
//        dynamicView.add(clinicService, "Calls findAll", vetRepository);
//        dynamicView.add(vetRepository, "select * from vets", relationalDatabase);

        Styles styles = viewSet.getConfiguration().getStyles();
        styles.addElementStyle("Spring PetClinic").background("#6CB33E").color("#ffffff");
        styles.addElementStyle(Tags.PERSON).background("#519823").color("#ffffff").shape(Shape.Person);
        styles.addElementStyle(Tags.CONTAINER).background("#91D366").color("#ffffff");
        styles.addElementStyle("Database").shape(Shape.Cylinder);
        styles.addElementStyle("Spring MVC Controller").background("#D4F3C0").color("#000000");
        styles.addElementStyle("Spring Service").background("#6CB33E").color("#000000");
        styles.addElementStyle("Spring Repository").background("#95D46C").color("#000000");
        return workspace;
    }


}

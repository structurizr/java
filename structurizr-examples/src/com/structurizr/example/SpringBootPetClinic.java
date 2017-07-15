package com.structurizr.example;

import com.structurizr.Workspace;
import com.structurizr.analysis.ComponentFinder;
import com.structurizr.analysis.ReferencedTypesSupportingTypesStrategy;
import com.structurizr.analysis.SourceCodeComponentFinderStrategy;
import com.structurizr.analysis.SpringComponentFinderStrategy;
import com.structurizr.api.StructurizrClient;
import com.structurizr.model.*;
import com.structurizr.view.*;

import java.io.File;

/**
 * This is a C4 representation of the Spring Boot version of the PetClinic sample app
 * (https://github.com/spring-projects/spring-petclinic/tree/ffa967c94b65a70ea6d3b44275632821838d9fd3) and you can see
 * the resulting diagrams at https://www.structurizr.com/public/6031
 *
 * Please note, you will need to modify the paths specified in the structurizr-examples/build.gradle file.
 */
public class SpringBootPetClinic {

    private static final String VERSION = "ffa967c94b65a70ea6d3b44275632821838d9fd3";

    private static final long WORKSPACE_ID = 6031;
    private static final String API_KEY = "";
    private static final String API_SECRET = "";

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.out.println("The path to the Spring Boot PetClinic source code must be provided.");
            System.exit(-1);
        }
        File sourceRoot = new File(args[0]);

        try {
            ClassLoader.getSystemClassLoader().loadClass("org.springframework.samples.petclinic.vet.Vet");
        } catch (ClassNotFoundException e) {
            System.err.println("Please check that the compiled version of the Spring Boot PetClinic application is on the classpath");
            System.exit(-1);
        }

        Workspace workspace = new Workspace("Spring Boot PetClinic",
                "This is a C4 representation of the Spring Boot PetClinic sample app (https://github.com/spring-projects/spring-petclinic/)");
        workspace.setVersion(VERSION);
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

        SpringComponentFinderStrategy springComponentFinderStrategy = new SpringComponentFinderStrategy(new ReferencedTypesSupportingTypesStrategy(false));
        springComponentFinderStrategy.setIncludePublicTypesOnly(false);

        // and now automatically find all Spring @Controller, @Component, @Service and @Repository components
        ComponentFinder componentFinder = new ComponentFinder(
                webApplication,
                "org.springframework.samples.petclinic",
                springComponentFinderStrategy,
                new SourceCodeComponentFinderStrategy(new File(sourceRoot, "/src/main/java/"), 150));

        componentFinder.exclude(".*Formatter.*");
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
        ViewSet views = workspace.getViews();
        SystemContextView contextView = views.createSystemContextView(springPetClinic, "context", "The System Context diagram for the Spring PetClinic system.");
        contextView.addAllSoftwareSystems();
        contextView.addAllPeople();

        ContainerView containerView = views.createContainerView(springPetClinic, "containers", "The Containers diagram for the Spring PetClinic system.");
        containerView.addAllPeople();
        containerView.addAllSoftwareSystems();
        containerView.addAllContainers();

        ComponentView componentView = views.createComponentView(webApplication, "components", "The Components diagram for the Spring PetClinic web application.");
        componentView.addAllComponents();
        componentView.addAllPeople();
        componentView.add(relationalDatabase);

        // link the architecture model with the code
        for (Component component : webApplication.getComponents()) {
            for (CodeElement codeElement : component.getCode()) {
                String sourcePath = codeElement.getUrl();
                if (sourcePath != null) {
                    codeElement.setUrl(sourcePath.replace(
                            sourceRoot.toURI().toString(),
                            "https://github.com/spring-projects/spring-petclinic/tree/" + VERSION + "/"));
                }
            }
        }

        // rather than creating a component model for the database, let's simply link to the DDL
        // (this is really just an example of linking an arbitrary element in the model to an external resource)
        relationalDatabase.setUrl("https://github.com/spring-projects/spring-petclinic/tree/" + VERSION + "/src/main/resources/db/hsqldb");

        // tag and style some elements
        springPetClinic.addTags("Spring PetClinic");
        webApplication.getComponents().stream().filter(c -> c.getTechnology().equals(SpringComponentFinderStrategy.SPRING_MVC_CONTROLLER)).forEach(c -> c.addTags("Spring MVC Controller"));
        webApplication.getComponents().stream().filter(c -> c.getTechnology().equals(SpringComponentFinderStrategy.SPRING_SERVICE)).forEach(c -> c.addTags("Spring Service"));
        webApplication.getComponents().stream().filter(c -> c.getTechnology().equals(SpringComponentFinderStrategy.SPRING_REPOSITORY)).forEach(c -> c.addTags("Spring Repository"));
        relationalDatabase.addTags("Database");

        Component vetController = webApplication.getComponentWithName("VetController");
        Component vetRepository = webApplication.getComponentWithName("VetRepository");

        DynamicView dynamicView = views.createDynamicView(webApplication, "viewListOfVets", "Shows how the \"view list of vets\" feature works.");
        dynamicView.add(clinicEmployee, "Requests list of vets from /vets", vetController);
        dynamicView.add(vetController, "Calls findAll", vetRepository);
        dynamicView.add(vetRepository, "select * from vets", relationalDatabase);

        Styles styles = views.getConfiguration().getStyles();
        styles.addElementStyle("Spring PetClinic").background("#6CB33E").color("#ffffff");
        styles.addElementStyle(Tags.PERSON).background("#519823").color("#ffffff").shape(Shape.Person);
        styles.addElementStyle(Tags.CONTAINER).background("#91D366").color("#ffffff");
        styles.addElementStyle("Database").shape(Shape.Cylinder);
        styles.addElementStyle("Spring MVC Controller").background("#D4F3C0").color("#000000");
        styles.addElementStyle("Spring Service").background("#6CB33E").color("#000000");
        styles.addElementStyle("Spring Repository").background("#95D46C").color("#000000");

        StructurizrClient structurizrClient = new StructurizrClient(API_KEY, API_SECRET);
        structurizrClient.putWorkspace(WORKSPACE_ID, workspace);
    }

}
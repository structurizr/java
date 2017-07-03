package com.structurizr.example.spring.petclinic;

import com.structurizr.Workspace;
import com.structurizr.api.StructurizrClient;
import com.structurizr.componentfinder.ComponentFinder;
import com.structurizr.componentfinder.ReferencedTypesSupportingTypesStrategy;
import com.structurizr.componentfinder.SourceCodeComponentFinderStrategy;
import com.structurizr.componentfinder.SpringComponentFinderStrategy;
import com.structurizr.documentation.Format;
import com.structurizr.documentation.StructurizrDocumentation;
import com.structurizr.model.*;
import com.structurizr.util.MapUtils;
import com.structurizr.view.*;

import java.io.File;

/**
 * This is a C4 representation of the original Spring PetClinic sample app
 * (https://github.com/spring-projects/spring-petclinic/tree/95de1d9f8bf63560915331664b27a4a75ce1f1f6) and you can see
 * the resulting diagrams at https://www.structurizr.com/public/1
 *
 * Use the following command to run this example: ./gradlew :structurizr-example:springPetClinic
 *
 * Please note, you will need to modify the paths specified in the structurizr-examples/build.gradle file.
 */
public class SpringPetClinic {

    private static final String VERSION = "95de1d9f8bf63560915331664b27a4a75ce1f1f6";

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.out.println("The path to the Spring PetClinic source code must be provided.");
            System.exit(-1);
        }
        File sourceRoot = new File(args[0]);

        try {
            Class.forName("org.springframework.samples.petclinic.model.Vet");
        } catch (ClassNotFoundException e) {
            System.err.println("Please check that the compiled version of the Spring PetClinic application is on the classpath");
            System.exit(-1);
        }

        Workspace workspace = new Workspace("Spring PetClinic",
                "This is a C4 representation of the Spring PetClinic sample app (https://github.com/spring-projects/spring-petclinic/)");
        workspace.setVersion(VERSION);
        Model model = workspace.getModel();

        // create the basic model (the stuff we can't get from the code)
        SoftwareSystem springPetClinic = model.addSoftwareSystem("Spring PetClinic", "Allows employees to view and manage information regarding the veterinarians, the clients, and their pets.");
        Person clinicEmployee = model.addPerson("Clinic Employee", "An employee of the clinic");
        clinicEmployee.uses(springPetClinic, "Uses");

        Container webApplication = springPetClinic.addContainer(
               "Web Application", "Allows employees to view and manage information regarding the veterinarians, the clients, and their pets.", "Java and Spring");
        webApplication.addProperty("Deployable artifact name", "petclinic.war");
        Container relationalDatabase = springPetClinic.addContainer(
               "Database", "Stores information regarding the veterinarians, the clients, and their pets.", "Relational Database Schema");
        clinicEmployee.uses(webApplication, "Uses", "HTTPS");
        webApplication.uses(relationalDatabase, "Reads from and writes to", "JDBC");

        // and now automatically find all Spring @Controller, @Component, @Service and @Repository components
        ComponentFinder componentFinder = new ComponentFinder(
                webApplication,
                "org.springframework.samples.petclinic",
                new SpringComponentFinderStrategy(
                        new ReferencedTypesSupportingTypesStrategy(false)
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
        relationalDatabase.addProperty("Schema name", "petclinic");

        // tag and style some elements
        springPetClinic.addTags("Spring PetClinic");
        webApplication.getComponents().stream().filter(c -> c.getTechnology().equals(SpringComponentFinderStrategy.SPRING_MVC_CONTROLLER)).forEach(c -> c.addTags("Spring MVC Controller"));
        webApplication.getComponents().stream().filter(c -> c.getTechnology().equals(SpringComponentFinderStrategy.SPRING_SERVICE)).forEach(c -> c.addTags("Spring Service"));
        webApplication.getComponents().stream().filter(c -> c.getTechnology().equals(SpringComponentFinderStrategy.SPRING_REPOSITORY)).forEach(c -> c.addTags("Spring Repository"));
        relationalDatabase.addTags("Database");

        Component vetController = webApplication.getComponentWithName("VetController");
        Component clinicService = webApplication.getComponentWithName("ClinicService");
        Component vetRepository = webApplication.getComponentWithName("VetRepository");

        DynamicView dynamicView = viewSet.createDynamicView(webApplication, "viewListOfVets", "Shows how the \"view list of vets\" feature works.");
        dynamicView.add(clinicEmployee, "Requests list of vets from /vets", vetController);
        dynamicView.add(vetController, "Calls findVets", clinicService);
        dynamicView.add(clinicService, "Calls findAll", vetRepository);
        dynamicView.add(vetRepository, "select * from vets", relationalDatabase);

        DeploymentNode developerLaptop = model.addDeploymentNode("Developer Laptop", "A developer laptop.", "Windows 7+ or macOS");
        developerLaptop.addDeploymentNode("Docker Container - Web Server", "A Docker container.", "Docker")
                .addDeploymentNode("Apache Tomcat", "An open source Java EE web server.", "Apache Tomcat 7.x", 1, MapUtils.create("Xmx=256M", "Xms=512M", "Java Version=8"))
                .add(webApplication);

        developerLaptop.addDeploymentNode("Docker Container - Database Server", "A Docker container.", "Docker")
                .addDeploymentNode("Database Server", "A development database.", "HSQLDB")
                .add(relationalDatabase);

        DeploymentNode stagingServer = model.addDeploymentNode("Staging Server", "A server hosted at Amazon AWS EC2", "Ubuntu 12.04 LTS", 1, MapUtils.create("AWS instance type=t2.medium", "AWS region=us-west-1"));
        stagingServer.addDeploymentNode("Apache Tomcat", "An open source Java EE web server.", "Apache Tomcat 7.x", 1, MapUtils.create("Xmx=512M", "Xms=1024M", "Java Version=8"))
                .add(webApplication);
        stagingServer.addDeploymentNode("MySQL", "The staging database server.", "MySQL 5.5.x", 1)
                .add(relationalDatabase);;

        DeploymentNode liveWebServer = model.addDeploymentNode("Web Server", "A server hosted at Amazon AWS EC2, accessed via Elastic Load Balancing.", "Ubuntu 12.04 LTS", 2, MapUtils.create("AWS instance type=t2.small", "AWS region=us-west-1"));
        liveWebServer.addDeploymentNode("Apache Tomcat", "An open source Java EE web server.", "Apache Tomcat 7.x", 1, MapUtils.create("Xmx=512M", "Xms=1024M", "Java Version=8"))
                .add(webApplication);

        DeploymentNode primaryDatabaseServer = model.addDeploymentNode("Database Server - Primary", "A server hosted at Amazon AWS EC2.", "Ubuntu 12.04 LTS", 1, MapUtils.create("AWS instance type=t2.medium", "AWS region=us-west-1"))
                .addDeploymentNode("MySQL - Primary", "The primary, live database server.", "MySQL 5.5.x");
        primaryDatabaseServer.add(relationalDatabase);

        DeploymentNode secondaryDatabaseServer = model.addDeploymentNode("Database Server - Secondary", "A server hosted at Amazon AWS EC2.", "Ubuntu 12.04 LTS", 1, MapUtils.create("AWS instance type=t2.small", "AWS region=us-east-1"))
                .addDeploymentNode("MySQL - Secondary", "A secondary database server, used for failover purposes.", "MySQL 5.5.x");
        ContainerInstance secondaryDatabase = secondaryDatabaseServer.add(relationalDatabase);

        model.getRelationships().stream().filter(r -> r.getDestination().equals(secondaryDatabase)).forEach(r -> r.addTags("Failover"));
        Relationship dataReplicationRelationship = primaryDatabaseServer.uses(secondaryDatabaseServer, "Replicates data to", "");
        secondaryDatabase.addTags("Failover");

        DeploymentView developmentDeploymentView = viewSet.createDeploymentView(springPetClinic, "developmentDeployment", "An example development deployment scenario for the Spring PetClinic software system.");
        developmentDeploymentView.add(developerLaptop);

        DeploymentView stagingDeploymentView = viewSet.createDeploymentView(springPetClinic, "stagingDeployment", "An example staging deployment scenario for the Spring PetClinic software system.");
        stagingDeploymentView.add(stagingServer);

        DeploymentView liveDeploymentView = viewSet.createDeploymentView(springPetClinic, "liveDeployment", "An example live deployment scenario for the Spring PetClinic software system.");
        liveDeploymentView.add(liveWebServer);
        liveDeploymentView.add(primaryDatabaseServer);
        liveDeploymentView.add(secondaryDatabaseServer);
        liveDeploymentView.add(dataReplicationRelationship);

        StructurizrDocumentation documentation = new StructurizrDocumentation(workspace);
        documentation.addContextSection(springPetClinic, Format.Markdown, "This is the context section for the Spring PetClinic System...\n![](embed:context)");
        documentation.addContainersSection(springPetClinic, Format.Markdown, "This is the containers section for the Spring PetClinic System...\n![](embed:containers)");
        documentation.addComponentsSection(webApplication, Format.Markdown, "This is the components section for the Spring PetClinic web application...\n![](embed:components)");
        documentation.addDeploymentSection(springPetClinic, Format.Markdown, "This is the deployment section for the Spring PetClinic web application...\n### Staging environment\n![](embed:stagingDeployment)\n### Live environment\n![](embed:liveDeployment)");
        documentation.addDevelopmentEnvironmentSection(springPetClinic, Format.Markdown, "This is the development environment section for the Spring PetClinic web application...\n![](embed:developmentDeployment)");

        Styles styles = viewSet.getConfiguration().getStyles();
        styles.addElementStyle("Spring PetClinic").background("#6CB33E").color("#ffffff");
        styles.addElementStyle(Tags.PERSON).background("#519823").color("#ffffff").shape(Shape.Person);
        styles.addElementStyle(Tags.CONTAINER).background("#91D366").color("#ffffff");
        styles.addElementStyle("Database").shape(Shape.Cylinder);
        styles.addElementStyle("Spring MVC Controller").background("#D4F3C0").color("#000000");
        styles.addElementStyle("Spring Service").background("#6CB33E").color("#000000");
        styles.addElementStyle("Spring Repository").background("#95D46C").color("#000000");
        styles.addElementStyle("Failover").opacity(25);
        styles.addRelationshipStyle("Failover").opacity(25).position(70);

        StructurizrClient structurizrClient = new StructurizrClient("key", "secret");
        structurizrClient.putWorkspace(1, workspace);
    }

}
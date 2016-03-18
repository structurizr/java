package com.structurizr.example.jhipster;

import com.structurizr.Workspace;
import com.structurizr.api.StructurizrClient;
import com.structurizr.componentfinder.ComponentFinder;
import com.structurizr.componentfinder.JavadocComponentFinderStrategy;
import com.structurizr.componentfinder.SpringComponentFinderStrategy;
import com.structurizr.model.*;
import com.structurizr.view.*;

import java.io.File;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This is a C4 representation of the JHipster sample application
 * (https://github.com/jhipster/jhipster-sample-app).
 */
public class JHipsterSampleApplication {

    public static void main(String[] args) throws Exception {
        Workspace workspace = new Workspace("JHipster Sample Application",
                "This is a C4 representation of the JHipster Sample Application (https://github.com/jhipster/jhipster-sample-app)");
        Model model = workspace.getModel();

        // create the basic model (the stuff we can't get from the code)
        SoftwareSystem jhipsterSampleApplication = model.addSoftwareSystem("JHipster Sample Application", "");
        Person anonymousUser = model.addPerson("Anonymous User", "");
        anonymousUser.uses(jhipsterSampleApplication, "Uses");

        Container browser = jhipsterSampleApplication.addContainer("Web Browser", "", "HTML, CSS and Angular");
        Container webApplication = jhipsterSampleApplication.addContainer("Web Application", "", "Spring Boot");
        Container relationalDatabase = jhipsterSampleApplication.addContainer("Database", "", "");

        anonymousUser.uses(browser, "Uses");
        browser.uses(webApplication, "Uses");
        webApplication.uses(relationalDatabase, "Reads from and writes to", "JDBC");

        // and now automatically find all components in the Spring Boot part of the application
        ComponentFinder componentFinder = new ComponentFinder(
                webApplication, "com.mycompany.myapp",
                new SpringComponentFinderStrategy(),
                new JavadocComponentFinderStrategy(new File("/Users/simon/Desktop/jhipster-sample-app/src/main/java/"), 150));
        componentFinder.findComponents();

        // connect the web browser to all REST controllers
        webApplication.getComponents().stream()
                .filter(c -> c.getTechnology().equals(SpringComponentFinderStrategy.SPRING_REST_CONTROLLER))
                .forEach(c -> browser.uses(c, "Uses", "JSON/HTTP(S)"));

        // connect all of the repository components to the relational database
        webApplication.getComponents().stream()
                .filter(c -> c.getTechnology().equals(SpringComponentFinderStrategy.SPRING_REPOSITORY))
                .forEach(c -> c.uses(relationalDatabase, "Reads from and writes to", "JDBC"));

        // create some views
        ViewSet viewSet = workspace.getViews();
        SystemContextView contextView = viewSet.createContextView(jhipsterSampleApplication);
        contextView.setKey("context");
        contextView.addAllSoftwareSystems();
        contextView.addAllPeople();

        ContainerView containerView = viewSet.createContainerView(jhipsterSampleApplication);
        containerView.setKey("containers");
        containerView.addAllPeople();
        containerView.addAllSoftwareSystems();
        containerView.addAllContainers();

        ComponentView componentView = viewSet.createComponentView(webApplication);
        componentView.setKey("components");
        componentView.addAllComponents();
        componentView.addAllPeople();
        componentView.addAllContainers();

        Set<Component> restControllers = webApplication.getComponents().stream()
                .filter(c -> c.getTechnology().equals(SpringComponentFinderStrategy.SPRING_REST_CONTROLLER)).collect(Collectors.toSet());
        for (Component restController : restControllers) {
            ComponentView view = viewSet.createComponentView(webApplication, restController.getName());
            view.addAllElements();
            view.removeElementsThatCantBeReachedFrom(restController);
            view.addAllContainers();
            view.removeElementsWithNoRelationships();
        }

        // link the architecture model with the code
        for (Component component : webApplication.getComponents()) {
            if (component.getSourcePath() != null) {
                component.setSourcePath(component.getSourcePath().replace(
                        "/Users/simon/Desktop/jhipster-sample-app",
                        "https://github.com/jhipster/jhipster-sample-app/tree/master/"));
            }
        }

        // tag and style some elements
        relationalDatabase.addTags("Database");

        viewSet.getConfiguration().getStyles().addElementStyle(Tags.PERSON).shape(Shape.Person);
        viewSet.getConfiguration().getStyles().addElementStyle("Database").shape(Shape.Cylinder);

        StructurizrClient structurizrClient = new StructurizrClient("key", "secret");
        structurizrClient.mergeWorkspace(5651, workspace);
    }

}
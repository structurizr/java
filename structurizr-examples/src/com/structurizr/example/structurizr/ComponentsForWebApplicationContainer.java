package com.structurizr.example.structurizr;

import com.structurizr.Workspace;
import com.structurizr.componentfinder.*;
import com.structurizr.componentfinder.ComponentPackageSupportingTypesStrategy;
import com.structurizr.model.*;
import com.structurizr.view.ComponentView;

import java.io.File;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Creates the component model and views for the web application container.
 */
public class ComponentsForWebApplicationContainer extends AbstractStructurizrWorkspace {

    private String remoteSourcePath;

    public static void main(String[] args) throws Exception {
        new ComponentsForWebApplicationContainer(args[0]).run();
    }

    public ComponentsForWebApplicationContainer(String remoteSourcePath) {
        this.remoteSourcePath = remoteSourcePath;
    }

    void run() throws Exception {
        Workspace workspace = readFromFile();
        Model model = workspace.getModel();

        Person anonymousUser = model.getPersonWithName(ANONYMOUS_USER);
        Person authenticatedUser = model.getPersonWithName(AUTHENTICATED_USER);

        SoftwareSystem structurizr = model.getSoftwareSystemWithName(STRUCTURIZR);
        Container webBrowser = structurizr.getContainerWithName(WEB_BROWSER);
        Container container = structurizr.getContainerWithName(WWW);

        // find all Spring @Controller components,
        // plus those marked using the Structurizr @Component annotation
        ComponentFinder componentFinder = new ComponentFinder(
                container,
                "com.structurizr",
                new SpringComponentFinderStrategy(),
                new StructurizrAnnotationsComponentFinderStrategy(
                        new ComponentPackageSupportingTypesStrategy()
                ),
                new SourceCodeComponentFinderStrategy(new File("structurizr-core/src/")),
                new SourceCodeComponentFinderStrategy(new File("structurizr-web/src/"))
        );
        componentFinder.findComponents();

        replaceLocalSourcePathWithRemoteUrl(container, remoteSourcePath);

        Set<Component> controllers = container.getComponents().stream()
                .filter(c -> c.getTechnology().equals(SpringComponentFinderStrategy.SPRING_MVC_CONTROLLER))
                .collect(Collectors.toSet());

        controllers.stream()
                .filter(c -> !c.getType().startsWith("com.structurizr.web.controller.webhook"))
                .forEach(c -> webBrowser.uses(c, "Uses"));

        // default all components to be "Spring Beans" if no technology is specified
        container.getComponents().stream()
                .filter(c -> c.getTechnology().isEmpty())
                .forEach(c -> c.setTechnology("Spring Bean"));

        // create one component view per Spring controller
        for (Component controller : controllers) {
            ComponentView view = workspace.getViews().createComponentView(container, "This diagram shows the components for a slice through the web application, focussed around the " + controller.getName() + " component");
            view.setSubtitle(controller.getName());
            view.setKey(controller.getName());

            view.addAllElements();
            view.remove(container.getComponentWithName(LOGGING_COMPONENT));
            controllers.stream().filter(c -> c != controller).forEach(view::remove);
            view.removeElementsThatCantBeReachedFrom(controller);

            if (controller.getType().startsWith("com.structurizr.web.controller.secured")) {
                view.add(authenticatedUser);
                view.remove(anonymousUser);
                view.add(webBrowser);
            } else if (controller.getType().startsWith("com.structurizr.web.controller.webhook")) {
                view.add(authenticatedUser);
                view.remove(anonymousUser);
                view.remove(webBrowser);
                view.add(model.getSoftwareSystemWithName(BRAINTREE));
            } else {
                view.remove(authenticatedUser);
                view.add(anonymousUser);
                view.add(webBrowser);
            }

            view.removeElementsWithNoRelationships();
        }


        writeToFile(workspace);
    }

}

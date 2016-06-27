package com.structurizr.example.structurizr;

import com.structurizr.Workspace;
import com.structurizr.componentfinder.*;
import com.structurizr.componentfinder.ComponentPackageSupportingTypesStrategy;
import com.structurizr.model.*;
import com.structurizr.view.ComponentView;

import java.io.File;

/**
 * Creates the component model and view for the API container.
 */
public class ComponentsForApiContainer extends AbstractStructurizrWorkspace {

    private String remoteSourcePath;

    public static void main(String[] args) throws Exception {
        new ComponentsForApiContainer(args[0]).run();
    }

    public ComponentsForApiContainer(String remoteSourcePath) {
        this.remoteSourcePath = remoteSourcePath;
    }

    void run() throws Exception {
        Workspace workspace = readFromFile();
        Model model = workspace.getModel();

        SoftwareSystem structurizr = model.getSoftwareSystemWithName(STRUCTURIZR);
        SoftwareSystem structurizrClient = workspace.getModel().getSoftwareSystemWithName(STRUCTURIZR_CLIENT);
        Container container = structurizr.getContainerWithName(API);
        Container webBrowser = structurizr.getContainerWithName(WEB_BROWSER);

        // find all Spring @Controller and @RestController components,
        // plus those marked using the Structurizr @Component annotation
        ComponentFinder componentFinder = new ComponentFinder(
                container,
                "com.structurizr",
                new SpringComponentFinderStrategy(),
                new StructurizrAnnotationsComponentFinderStrategy(
                        new ComponentPackageSupportingTypesStrategy()
                ),
                new SourceCodeComponentFinderStrategy(new File("structurizr-core/src/")),
                new SourceCodeComponentFinderStrategy(new File("structurizr-api/src/"))
        );
        componentFinder.findComponents();

        container.getComponents().stream()
                .filter(c -> c.getType().startsWith("com.structurizr.web.controller"))
                .forEach(controller -> webBrowser.uses(controller, "uses"));

        container.getComponents().stream()
                .filter(c -> c.getType().startsWith("com.structurizr.api.controller"))
                .forEach(controller -> structurizrClient.uses(controller, "uses"));

        replaceLocalSourcePathWithRemoteUrl(container, remoteSourcePath);

        // default all components to be "Spring Beans" if no technology is specified
        container.getComponents().stream()
                .filter(c -> c.getTechnology().isEmpty())
                .forEach(c -> c.setTechnology("Spring Bean"));

        // and, since the API container is small, create a single component view for it
        ComponentView componentView = workspace.getViews().createComponentView(container);
        componentView.add(structurizrClient);
        componentView.add(webBrowser);
        componentView.add(model.getSoftwareSystemWithName(PINGDOM));
        componentView.add(structurizr.getContainerWithName(DATABASE));

        componentView.addAllComponents();

        // remove any unused components from the view
        for (Component component : container.getComponents()) {
            if (!component.hasAfferentRelationships()) {
                componentView.remove(component);
            }
        }
        componentView.remove(container.getComponentWithName(LOGGING_COMPONENT));

        writeToFile(workspace);
    }

}

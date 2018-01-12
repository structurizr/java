package com.structurizr;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.structurizr.documentation.Documentation;
import com.structurizr.model.*;
import com.structurizr.view.ViewSet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.LinkedList;
import java.util.List;

/**
 * Represents a Structurizr workspace, which is a wrapper for a
 * software architecture model, views and documentation.
 */
public final class Workspace extends AbstractWorkspace {

    private static final Log log = LogFactory.getLog(Workspace.class);

    private Model model = new Model();
    private ViewSet viewSet = new ViewSet(model);
    private Documentation documentation = new Documentation(model);

    Workspace() {
    }

    /**
     * Creates a new workspace.
     *
     * @param name          the name of the workspace
     * @param description   a short description
     */
    public Workspace(String name, String description) {
        super(name, description);
    }

    /**
     * Gets the software architecture model.
     *
     * @return  a Model instance
     */
    public Model getModel() {
        return model;
    }

    void setModel(Model model) {
        this.model = model;
    }

    /**
     * Gets the set of views onto a software architecture model.
     *
     * @return  a ViewSet instance
     */
    public ViewSet getViews() {
        return viewSet;
    }

    void setViews(ViewSet viewSet) {
        this.viewSet = viewSet;
    }

    /**
     * Called when deserialising JSON, to re-create the object graph
     * based upon element/relationship IDs.
     */
    public void hydrate() {
        this.viewSet.setModel(model);
        this.documentation.setModel(model);

        this.model.hydrate();
        this.viewSet.hydrate();
        this.documentation.hydrate();
    }

    /**
     * Gets the documentation associated with this workspace.
     *
     * @return  a Documentation object
     */
    public Documentation getDocumentation() {
        return documentation;
    }

    /**
     * Sets the documentation associated with this workspace.
     *
     * @param documentation a Documentation object
     */
    public void setDocumentation(Documentation documentation) {
        this.documentation = documentation;
        documentation.setModel(getModel());
    }

    @JsonIgnore
    public boolean isEmpty() {
        return model.isEmpty() && viewSet.isEmpty() && documentation.isEmpty();
    }

    /**
     * Counts and logs any warnings within the workspace (e.g. missing element descriptions).
     *
     * @return  the number of warnings
     */
    public int countAndLogWarnings() {
        final List<String> warnings = new LinkedList<>();

        // find elements with a missing description
        getModel().getElements().stream()
                .filter(e -> !(e instanceof ContainerInstance))
                .filter(e -> e.getDescription() == null || e.getDescription().trim().length() == 0)
                .forEach(e -> warnings.add("The " + typeof(e) + " \"" + e.getCanonicalName().substring(1) + "\" is missing a description."));

        // find containers with a missing technology
        getModel().getElements().stream()
                .filter(e -> e instanceof Container)
                .map(e -> (Container)e)
                .filter(c -> c.getTechnology() == null || c.getTechnology().trim().length() == 0)
                .forEach(c -> warnings.add("The container \"" + c.getCanonicalName().substring(1) + "\" is missing a technology."));

        // find components with a missing technology
        getModel().getElements().stream()
                .filter(e -> e instanceof Component)
                .map(e -> (Component)e)
                .filter(c -> c.getTechnology() == null || c.getTechnology().trim().length() == 0)
                .forEach(c -> warnings.add("The component \"" + c.getCanonicalName().substring(1) + "\" is missing a technology."));

        // find component relationships with a missing description
        for (Relationship relationship : getModel().getRelationships()) {
            if (relationship.getSource() instanceof Component && relationship.getDestination() instanceof Component &&
                    relationship.getSource().getParent().equals(relationship.getDestination().getParent())) {
                // ignore component-component relationships inside the same container because these are
                // often identified using reflection and won't have a description
                // (i.e. let's not flood the user with warnings)
            } else {
                if (relationship.getDescription() == null || relationship.getDescription().trim().length() == 0) {
                    warnings.add("The relationship between " + typeof(relationship.getSource()) + " \"" + relationship.getSource().getCanonicalName().substring(1) + "\" and " + typeof(relationship.getDestination()) + " \"" + relationship.getDestination().getCanonicalName().substring(1) + "\" is missing a description.");
                }
            }
        }

        // diagram keys have not been specified - this is only applicable to
        // workspaces created with the early versions of Structurizr for Java
        getViews().getSystemLandscapeViews().stream()
                .filter(v -> v.getKey() == null)
                .forEach(v -> warnings.add("Enterprise Context view \"" + v.getName() + "\": Missing key"));
        getViews().getSystemContextViews().stream()
                .filter(v -> v.getKey() == null)
                .forEach(v -> warnings.add("System Context view \"" + v.getName() + "\": Missing key"));
        getViews().getContainerViews().stream()
                .filter(v -> v.getKey() == null)
                .forEach(v -> warnings.add("Container view \"" + v.getName() + "\": Missing key"));
        getViews().getComponentViews().stream()
                .filter(v -> v.getKey() == null)
                .forEach(v -> warnings.add("Component view \"" + v.getName() + "\": Missing key"));
        getViews().getDynamicViews().stream()
                .filter(v -> v.getKey() == null)
                .forEach(v -> warnings.add("Dynamic view \"" + v.getName() + "\": Missing key"));
        getViews().getDeploymentViews().stream()
                .filter(v -> v.getKey() == null)
                .forEach(v -> warnings.add("Deployment view \"" + v.getName() + "\": Missing key"));

        warnings.forEach(log::warn);

        return warnings.size();
    }

    private String typeof(Element element) {
        if (element instanceof SoftwareSystem) {
            return "software system";
        } else {
            return element.getClass().getSimpleName().toLowerCase();
        }
    }

}

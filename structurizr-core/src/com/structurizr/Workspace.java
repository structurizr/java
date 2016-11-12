package com.structurizr;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.structurizr.documentation.Documentation;
import com.structurizr.model.Model;
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

    public void setModel(Model model) {
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

    void setDocumentation(Documentation documentation) {
        this.documentation = documentation;
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
                .filter(e -> e.getDescription() == null || e.getDescription().trim().length() == 0)
                .forEach(e -> warnings.add(e.getClass().getSimpleName() + " " + e.getCanonicalName() + ": Missing description"));

        // find containers with a missing technology
        getModel().getSoftwareSystems()
                .forEach(s -> s.getContainers().stream()
                        .filter(c -> c.getTechnology() == null || c.getTechnology().trim().length() == 0)
                        .forEach(c -> warnings.add("Container " + c.getCanonicalName() + ": Missing technology")));

        // find relationships with a missing description
        getModel().getRelationships().stream()
                .filter(e -> e.getDescription() == null || e.getDescription().trim().length() == 0)
                .forEach(e -> warnings.add(e.getClass().getSimpleName() + " " + e.toString() + ": Missing description"));

        // find relationships with a missing technology
        getModel().getRelationships().stream()
                .filter(e -> e.getTechnology() == null || e.getTechnology().trim().length() == 0)
                .forEach(e -> warnings.add(e.getClass().getSimpleName() + " " + e.toString() + ": Missing technology"));

        // diagram keys have not been specified
        getViews().getEnterpriseContextViews().stream()
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

        warnings.forEach(log::warn);

        return warnings.size();
    }

}

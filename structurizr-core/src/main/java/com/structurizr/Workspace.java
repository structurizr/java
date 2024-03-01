package com.structurizr;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.structurizr.documentation.Documentable;
import com.structurizr.documentation.Documentation;
import com.structurizr.model.*;
import com.structurizr.view.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.Nonnull;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Represents a Structurizr workspace, which is a wrapper for a
 * software architecture model, views and documentation.
 */
public final class Workspace extends AbstractWorkspace implements Documentable {

    private static final Log log = LogFactory.getLog(Workspace.class);

    private Model model = createModel();
    private ViewSet viewSet;
    private Documentation documentation;

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

        model = createModel();
        viewSet = createViewSet();
        documentation = new Documentation();
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

    private Model createModel() {
        try {
            Constructor constructor = Model.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            return (Model)constructor.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private ViewSet createViewSet() {
        try {
            Constructor constructor = ViewSet.class.getDeclaredConstructor(Model.class);
            constructor.setAccessible(true);
            return (ViewSet)constructor.newInstance(model);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Called when deserialising JSON, to re-create the object graph
     * based upon element/relationship IDs.
     */
    public void hydrate() {
        if (viewSet == null) {
            viewSet = createViewSet();
        }

        hydrateModel();
        hydrateViewSet();
    }

    private void hydrateModel() {
        try {
            Method hydrateMethod = Model.class.getDeclaredMethod("hydrate");
            hydrateMethod.setAccessible(true);
            hydrateMethod.invoke(model);
        } catch (InvocationTargetException ite) {
            if (ite.getCause() != null && ite.getCause() instanceof WorkspaceValidationException) {
                throw (WorkspaceValidationException)ite.getCause();
            } else {
                throw new RuntimeException(ite.getCause());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void hydrateViewSet() {
        try {
            Method hydrateMethod = ViewSet.class.getDeclaredMethod("hydrate", Model.class);
            hydrateMethod.setAccessible(true);
            hydrateMethod.invoke(viewSet, model);
        } catch (InvocationTargetException ite) {
            if (ite.getCause() != null && ite.getCause() instanceof WorkspaceValidationException) {
                throw (WorkspaceValidationException)ite.getCause();
            } else {
                throw new RuntimeException(ite.getCause());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
    void setDocumentation(@Nonnull Documentation documentation) {
        this.documentation = documentation;
    }

    /**
     * Determines whether this model is empty.
     *
     * @return  true if the model has no elements, views or documentation; false otherwise
     */
    @JsonIgnore
    public boolean isEmpty() {
        return model.isEmpty() && viewSet.isEmpty() && documentation.isEmpty();
    }

    /**
     * Trims the workspace by removing all unused elements.
     */
    public void trim() {
        for (CustomElement element : model.getCustomElements()) {
            remove(element);
        }

        for (Person person : model.getPeople()) {
            remove(person);
        }

        for (SoftwareSystem softwareSystem : model.getSoftwareSystems()) {
            remove(softwareSystem);
        }

        for (DeploymentNode deploymentNode : model.getDeploymentNodes()) {
            remove(deploymentNode);
        }
    }

    void remove(CustomElement element) {
        if (!isElementAssociatedWithAnyViews(element)) {
            try {
                Method method = Model.class.getDeclaredMethod("remove", CustomElement.class);
                method.setAccessible(true);
                method.invoke(model, element);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    void remove(Person person) {
        if (!isElementAssociatedWithAnyViews(person)) {
            try {
                Method method = Model.class.getDeclaredMethod("remove", Person.class);
                method.setAccessible(true);
                method.invoke(model, person);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    void remove(SoftwareSystem softwareSystem) {
        Set<SoftwareSystemInstance> softwareSystemInstances = model.getElements().stream().filter(e -> e instanceof SoftwareSystemInstance && ((SoftwareSystemInstance)e).getSoftwareSystem() == softwareSystem).map(e -> (SoftwareSystemInstance)e).collect(Collectors.toSet());
        for (SoftwareSystemInstance softwareSystemInstance : softwareSystemInstances) {
            remove(softwareSystemInstance);
        }

        for (Container container : softwareSystem.getContainers()) {
            remove(container);
        }

        boolean hasContainers = softwareSystem.hasContainers();
        boolean hasSoftwareSystemInstances = model.getElements().stream().anyMatch(e -> e instanceof SoftwareSystemInstance && ((SoftwareSystemInstance)e).getSoftwareSystem() == softwareSystem);
        if (!hasContainers && !hasSoftwareSystemInstances && !isElementAssociatedWithAnyViews(softwareSystem)) {
            try {
                Method method = Model.class.getDeclaredMethod("remove", SoftwareSystem.class);
                method.setAccessible(true);
                method.invoke(model, softwareSystem);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    void remove(Container container) {
        for (Component component : container.getComponents()) {
            remove(component);
        }

        if (!isElementAssociatedWithAnyViews(container)) {
            Set<ContainerInstance> containerInstances = model.getElements().stream().filter(e -> e instanceof ContainerInstance && ((ContainerInstance)e).getContainer() == container).map(e -> (ContainerInstance)e).collect(Collectors.toSet());
            for (ContainerInstance containerInstance : containerInstances) {
                remove(containerInstance);
            }

            boolean hasComponents = container.hasComponents();
            boolean hasContainerInstances = model.getElements().stream().anyMatch(e -> e instanceof ContainerInstance && ((ContainerInstance)e).getContainer() == container);
            if (!hasComponents && !hasContainerInstances && !isElementAssociatedWithAnyViews(container)) {
                try {
                    Method method = Model.class.getDeclaredMethod("remove", Container.class);
                    method.setAccessible(true);
                    method.invoke(model, container);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    void remove(Component component) {
        if (!isElementAssociatedWithAnyViews(component)) {
            try {
                Method method = Model.class.getDeclaredMethod("remove", Component.class);
                method.setAccessible(true);
                method.invoke(model, component);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    void remove(SoftwareSystemInstance softwareSystemInstance) {
        if (!isElementAssociatedWithAnyViews(softwareSystemInstance)) {
            try {
                Method method = Model.class.getDeclaredMethod("remove", SoftwareSystemInstance.class);
                method.setAccessible(true);
                method.invoke(model, softwareSystemInstance);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    void remove(ContainerInstance containerInstance) {
        if (!isElementAssociatedWithAnyViews(containerInstance)) {
            try {
                Method method = Model.class.getDeclaredMethod("remove", ContainerInstance.class);
                method.setAccessible(true);
                method.invoke(model, containerInstance);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    void remove(DeploymentNode deploymentNode) {
        if (deploymentNode.hasChildren()) {
            for (DeploymentNode child : deploymentNode.getChildren()) {
                remove(child);
            }
        }

        if (!deploymentNode.hasChildren() && !deploymentNode.hasSoftwareSystemInstances() && !deploymentNode.hasContainerInstances()) {
            try {
                Method method = Model.class.getDeclaredMethod("remove", DeploymentNode.class);
                method.setAccessible(true);
                method.invoke(model, deploymentNode);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private boolean isElementAssociatedWithAnyViews(Element element) {
        boolean result = false;

        // is the element used in any views
        for (View view : viewSet.getViews()) {
            if (view instanceof ModelView) {
                ModelView modelView = (ModelView)view;
                result = result | modelView.isElementInView(element);
            }
        }

        // is the element the scope of any views?
        for (SystemContextView view : viewSet.getSystemContextViews()) {
            result = result | view.getSoftwareSystem() == element;
        }

        for (ContainerView view : viewSet.getContainerViews()) {
            result = result | view.getSoftwareSystem() == element;
        }

        for (ComponentView view : viewSet.getComponentViews()) {
            result = result | view.getContainer() == element;
        }

        for (DynamicView view : viewSet.getDynamicViews()) {
            result = result | view.getElement() == element;
        }

        for (DeploymentView view : viewSet.getDeploymentViews()) {
            result = result | view.getSoftwareSystem() == element;
        }

        for (ImageView view : viewSet.getImageViews()) {
            result = result | view.getElement() == element;
        }

        return result;
    }

}
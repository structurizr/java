package com.structurizr.view;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.structurizr.model.Container;
import com.structurizr.model.Model;
import com.structurizr.model.SoftwareSystem;

import java.util.Collection;
import java.util.TreeSet;

/**
 * A set of views onto a software architecture model.
 */
public class ViewSet {

    private Model model;

    private Collection<SystemContextView> systemContextViews = new TreeSet<>();
    private Collection<ContainerView> containerViews = new TreeSet<>();
    private Collection<ComponentView> componentViews = new TreeSet<>();
    private Collection<DynamicView> dynamicViews = new TreeSet<>();

    private Styles styles = new Styles();
    private Configuration configuration = new Configuration();

    ViewSet() {
    }

    public ViewSet(Model model) {
        this.model = model;
    }

    @JsonIgnore
    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public SystemContextView createContextView(SoftwareSystem softwareSystem) {
        return createContextView(softwareSystem, null);
    }

    public SystemContextView createContextView(SoftwareSystem softwareSystem, String description) {
        SystemContextView view = new SystemContextView(softwareSystem, description);
        systemContextViews.add(view);

        return view;
    }

    public ContainerView createContainerView(SoftwareSystem softwareSystem) {
        return createContainerView(softwareSystem, null);
    }

    public ContainerView createContainerView(SoftwareSystem softwareSystem, String description) {
        ContainerView view = new ContainerView(softwareSystem, description);
        containerViews.add(view);

        return view;
    }

    public ComponentView createComponentView(Container container) {
        return createComponentView(container, null);
    }

    public ComponentView createComponentView(Container container, String description) {
        ComponentView view = new ComponentView(container, description);
        componentViews.add(view);

        return view;
    }

    public DynamicView createDynamicView(SoftwareSystem softwareSystem, String description) {
        DynamicView view = new DynamicView(softwareSystem, description);
        dynamicViews.add(view);

        return view;
    }

    public Collection<SystemContextView> getSystemContextViews() {
        return new TreeSet<>(systemContextViews);
    }

    public Collection<ContainerView> getContainerViews() {
        return new TreeSet<>(containerViews);
    }

    public Collection<ComponentView> getComponentViews() {
        return new TreeSet<>(componentViews);
    }

    public Collection<DynamicView> getDynamicViews() {
        return new TreeSet<>(dynamicViews);
    }

    public void hydrate() {
        systemContextViews.forEach(this::hydrateView);
        containerViews.forEach(this::hydrateView);
        componentViews.forEach(this::hydrateView);
        for (ComponentView view : componentViews) {
            hydrateView(view);
            view.setContainer(view.getSoftwareSystem().getContainerWithId(view.getContainerId()));
        }

        dynamicViews.forEach(this::hydrateView);
    }

    private void hydrateView(View view) {
        view.setSoftwareSystem(model.getSoftwareSystemWithId(view.getSoftwareSystemId()));

        for (ElementView elementView : view.getElements()) {
            elementView.setElement(model.getElement(elementView.getId()));
        }
        for (RelationshipView relationshipView : view.getRelationships()) {
            relationshipView.setRelationship(model.getRelationship(relationshipView.getId()));
        }
    }

    /**
     * @deprecated use {@link ViewSet#getConfiguration()} and call {@link Configuration#getStyles()} there
     */
    @Deprecated
    public Styles getStyles() {
        return styles;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void copyLayoutInformationFrom(ViewSet source) {
        for (SystemContextView sourceView : source.getSystemContextViews()) {
            SystemContextView destinationView = findSystemContextView(sourceView);
            if (destinationView != null) {
                destinationView.copyLayoutInformationFrom(sourceView);
            }
        }

        for (ContainerView sourceView : source.getContainerViews()) {
            ContainerView destinationView = findContainerView(sourceView);
            if (destinationView != null) {
                destinationView.copyLayoutInformationFrom(sourceView);
            }
        }

        for (ComponentView sourceView : source.getComponentViews()) {
            ComponentView destinationView = findComponentView(sourceView);
            if (destinationView != null) {
                destinationView.copyLayoutInformationFrom(sourceView);
            }
        }

        for (DynamicView sourceView : source.getDynamicViews()) {
            DynamicView destinationView = findDynamicView(sourceView);
            if (destinationView != null) {
                destinationView.copyLayoutInformationFrom(sourceView);
            }
        }
    }

    private SystemContextView findSystemContextView(SystemContextView systemContextView) {
        for (SystemContextView view : systemContextViews) {
            if (view.getTitle().equals(systemContextView.getTitle())) {
                return view;
            }
        }

        return null;
    }

    private ContainerView findContainerView(ContainerView containerView) {
        for (ContainerView view : containerViews) {
            if (view.getTitle().equals(containerView.getTitle())) {
                return view;
            }
        }

        return null;
    }

    private ComponentView findComponentView(ComponentView componentView) {
        for (ComponentView view : componentViews) {
            if (view.getTitle().equals(componentView.getTitle())) {
                return view;
            }
        }

        return null;
    }

    private DynamicView findDynamicView(DynamicView dynamicView) {
        for (DynamicView view : dynamicViews) {
            if (view.getTitle().equals(dynamicView.getTitle())) {
                return view;
            }
        }

        return null;
    }

}

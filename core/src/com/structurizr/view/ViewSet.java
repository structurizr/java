package com.structurizr.view;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.structurizr.model.Container;
import com.structurizr.model.Element;
import com.structurizr.model.Model;
import com.structurizr.model.SoftwareSystem;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Represents a set of views onto a model.
 */
public class ViewSet {

    private Model model;

    private Collection<SystemContextView> systemContextViews = new LinkedList<>();
    private Collection<ContainerView> containerViews = new LinkedList<>();
    private Collection<ComponentView> componentViews = new LinkedList<>();

    private Styles styles = new Styles();

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
        SystemContextView view = new SystemContextView(softwareSystem);
        systemContextViews.add(view);

        return view;
    }

    public ContainerView createContainerView(SoftwareSystem softwareSystem) {
        ContainerView view = new ContainerView(softwareSystem);
        containerViews.add(view);

        return view;
    }

    public ComponentView createComponentView(SoftwareSystem softwareSystem, Container container) {
        ComponentView view = new ComponentView(softwareSystem, container);
        componentViews.add(view);

        return view;
    }

    public Collection<SystemContextView> getSystemContextViews() {
        return new LinkedList<>(systemContextViews);
    }

    public Collection<ContainerView> getContainerViews() {
        return new LinkedList<>(containerViews);
    }

    public Collection<ComponentView> getComponentViews() {
        return new LinkedList<>(componentViews);
    }

    public void hydrate() {
        systemContextViews.forEach(this::hydrateView);
        containerViews.forEach(this::hydrateView);
        componentViews.forEach(this::hydrateView);
        for (ComponentView view : componentViews) {
            hydrateView(view);
            view.setContainer(view.getSoftwareSystem().getContainerWithId(view.getContainerId()));
        }
    }

    private void hydrateView(View view) {
        view.setSoftwareSystem(model.getSoftwareSystemWithId(view.getSoftwareSystemId()));

        for (ElementView elementView : view.getElements()) {
            elementView.setElement(model.getElement(elementView.getId()));
        }
    }

    public Styles getStyles() {
        return styles;
    }

    public void copyLayoutInformationFrom(ViewSet source) {
        for (SystemContextView sourceView : source.getSystemContextViews()) {
            SystemContextView destinationView = findSystemContextViewWithName(sourceView.getName());
            if (destinationView != null) {
                destinationView.copyLayoutInformationFrom(sourceView);
            }
        }

        for (ContainerView sourceView : source.getContainerViews()) {
            ContainerView destinationView = findContainerViewWithName(sourceView.getName());
            if (destinationView != null) {
                destinationView.copyLayoutInformationFrom(sourceView);
            }
        }

        for (ComponentView sourceView : source.getComponentViews()) {
            ComponentView destinationView = findComponentViewWithName(sourceView.getName());
            if (destinationView != null) {
                destinationView.copyLayoutInformationFrom(sourceView);
            }
        }
    }

    private SystemContextView findSystemContextViewWithName(String name) {
        for (SystemContextView view : systemContextViews) {
            if (view.getName().equals(name)) {
                return view;
            }
        }

        return null;
    }

    private ContainerView findContainerViewWithName(String name) {
        for (ContainerView view : containerViews) {
            if (view.getName().equals(name)) {
                return view;
            }
        }

        return null;
    }

    private ComponentView findComponentViewWithName(String name) {
        for (ComponentView view : componentViews) {
            if (view.getName().equals(name)) {
                return view;
            }
        }

        return null;
    }

}

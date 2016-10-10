package com.structurizr.view;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.structurizr.model.Container;
import com.structurizr.model.Model;
import com.structurizr.model.SoftwareSystem;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * A set of views onto a software architecture model.
 */
public class ViewSet {

    private static final Log log = LogFactory.getLog(ViewSet.class);

    private Model model;

    private Collection<EnterpriseContextView> enterpriseContextViews = new HashSet<>();
    private Collection<SystemContextView> systemContextViews = new HashSet<>();
    private Collection<ContainerView> containerViews = new HashSet<>();
    private Collection<ComponentView> componentViews = new HashSet<>();
    private Collection<DynamicView> dynamicViews = new HashSet<>();

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

    public EnterpriseContextView createEnterpriseContextView(String key, String description) {
        EnterpriseContextView view = new EnterpriseContextView(model, key, description);

        if (getViewWithKey(key) != null) {
            throw new IllegalArgumentException("A view with the key " + key + " already exists.");
        } else {
            enterpriseContextViews.add(view);
            return view;
        }
    }

    /**
     * Please use {@link #createSystemContextView(com.structurizr.model.SoftwareSystem, String, String)} instead.
     * @deprecated
     */
    @Deprecated
    public SystemContextView createContextView(SoftwareSystem softwareSystem) {
        return createSystemContextView(softwareSystem, null, null);
    }

    /**
     * Please use {@link #createSystemContextView(com.structurizr.model.SoftwareSystem, String, String)} instead.
     * @deprecated
     */
    @Deprecated
    public SystemContextView createContextView(SoftwareSystem softwareSystem, String description) {
        return createSystemContextView(softwareSystem, null, description);
    }

    public SystemContextView createSystemContextView(SoftwareSystem softwareSystem, String key, String description) {
        SystemContextView view = new SystemContextView(softwareSystem, key, description);

        if (getViewWithKey(key) != null) {
            throw new IllegalArgumentException("A view with the key " + key + " already exists.");
        } else {
            systemContextViews.add(view);
            return view;
        }
    }

    /**
     * Finds the view with the specified key, or null if the view does not exist.
     *
     * @param key   the key
     * @return  a View object, or null if a view with the specified key could not be found
     */
    public View getViewWithKey(String key) {
        View view = null;

        if (key != null) {
            Set<View> views = new HashSet<>();
            views.addAll(systemContextViews);
            views.addAll(containerViews);
            views.addAll(componentViews);
            views.addAll(dynamicViews);

            view = views.stream().filter(v -> key.equals(v.getKey())).findFirst().orElse(null);
        }

        return view;
    }

    /**
     * Please use {@link #createContainerView(com.structurizr.model.SoftwareSystem, String, String)} instead.
     * @deprecated
     */
    public ContainerView createContainerView(SoftwareSystem softwareSystem) {
        return createContainerView(softwareSystem, null, null);
    }

    /**
     * Please use {@link #createContainerView(com.structurizr.model.SoftwareSystem, String, String)} instead.
     * @deprecated
     */
    @Deprecated
    public ContainerView createContainerView(SoftwareSystem softwareSystem, String description) {
        return createContainerView(softwareSystem, null, description);
    }

    public ContainerView createContainerView(SoftwareSystem softwareSystem, String key, String description) {
        ContainerView view = new ContainerView(softwareSystem, key, description);

        if (getViewWithKey(key) != null) {
            throw new IllegalArgumentException("A view with the key " + key + " already exists.");
        } else {
            containerViews.add(view);
            return view;
        }
    }

    /**
     * Please use {@link #createComponentView(com.structurizr.model.Container, String, String)} instead.
     * @deprecated
     */
    @Deprecated
    public ComponentView createComponentView(Container container) {
        return createComponentView(container, null);
    }

    /**
     * Please use {@link #createComponentView(com.structurizr.model.Container, String, String)} instead.
     * @deprecated
     */
    @Deprecated
    public ComponentView createComponentView(Container container, String description) {
        return createComponentView(container, null, description);
    }

    public ComponentView createComponentView(Container container, String key, String description) {
        ComponentView view = new ComponentView(container, key, description);

        if (getViewWithKey(key) != null) {
            throw new IllegalArgumentException("A view with the key " + key + " already exists.");
        } else {
            componentViews.add(view);
            return view;
        }
    }

    /**
     * Please use {@link #createDynamicView(com.structurizr.model.SoftwareSystem, String, String)} instead.
     * @deprecated
     */
    @Deprecated
    public DynamicView createDynamicView(SoftwareSystem softwareSystem, String description) {
        return createDynamicView(softwareSystem, null, description);
    }

    public DynamicView createDynamicView(SoftwareSystem softwareSystem, String key, String description) {
        DynamicView view = new DynamicView(softwareSystem, key, description);

        if (getViewWithKey(key) != null) {
            throw new IllegalArgumentException("A view with the key " + key + " already exists.");
        } else {
            dynamicViews.add(view);
            return view;
        }
    }

    /**
     * Gets the set of enterprise context views.
     *
     * @return  a Collection of EnterpriseContextView objects
     */
    public Collection<EnterpriseContextView> getEnterpriseContextViews() {
        return new HashSet<>(enterpriseContextViews);
    }

    /**
     * Gets the set of system context views.
     *
     * @return  a Collection of SystemContextView objects
     */
    public Collection<SystemContextView> getSystemContextViews() {
        return new HashSet<>(systemContextViews);
    }

    /**
     * Gets the set of container views.
     *
     * @return  a Collection of ContainerView objects
     */
    public Collection<ContainerView> getContainerViews() {
        return new HashSet<>(containerViews);
    }

    /**
     * Gets the set of component views.
     *
     * @return  a Collection of ComponentView objects
     */
    public Collection<ComponentView> getComponentViews() {
        return new HashSet<>(componentViews);
    }

    /**
     * Gets the set of dynamic views.
     *
     * @return  a Collection of DynamicView objects
     */
    public Collection<DynamicView> getDynamicViews() {
        return new HashSet<>(dynamicViews);
    }

    public void hydrate() {
        enterpriseContextViews.forEach(this::hydrateView);

        systemContextViews.forEach(this::hydrateView);

        containerViews.forEach(this::hydrateView);

        for (ComponentView view : componentViews) {
            hydrateView(view);
            view.setContainer(view.getSoftwareSystem().getContainerWithId(view.getContainerId()));
        }

        dynamicViews.forEach(this::hydrateView);
    }

    private void hydrateView(View view) {
        if (view instanceof EnterpriseContextView) {
            EnterpriseContextView enterpriseContextView = (EnterpriseContextView)view;
            enterpriseContextView.setModel(model);
        } else {
            view.setSoftwareSystem(model.getSoftwareSystemWithId(view.getSoftwareSystemId()));
        }

        for (ElementView elementView : view.getElements()) {
            elementView.setElement(model.getElement(elementView.getId()));
        }
        for (RelationshipView relationshipView : view.getRelationships()) {
            relationshipView.setRelationship(model.getRelationship(relationshipView.getId()));
        }
    }

    /**
     * Gets the configuration object associated with this set of views.
     *
     * @return  a Configuration object
     */
    public Configuration getConfiguration() {
        return configuration;
    }

    public void copyLayoutInformationFrom(ViewSet source) {
        for (EnterpriseContextView view : enterpriseContextViews) {
            EnterpriseContextView sourceView = findView(source.getEnterpriseContextViews(), view);
            if (sourceView != null) {
                view.copyLayoutInformationFrom(sourceView);
            } else {
                log.warn("Could not find a matching view for \"" + view.getName() + "\" ... diagram layout information may be lost.");
            }
        }

        for (SystemContextView view : systemContextViews) {
            SystemContextView sourceView = findView(source.getSystemContextViews(), view);
            if (sourceView != null) {
                view.copyLayoutInformationFrom(sourceView);
            } else {
                log.warn("Could not find a matching view for \"" + view.getName() + "\" ... diagram layout information may be lost.");
            }
        }

        for (ContainerView view : containerViews) {
            ContainerView sourceView = findView(source.getContainerViews(), view);
            if (sourceView != null) {
                view.copyLayoutInformationFrom(sourceView);
            } else {
                log.warn("Could not find a matching view for \"" + view.getName() + "\" ... diagram layout information may be lost.");
            }
        }

        for (ComponentView view : componentViews) {
            ComponentView sourceView = findView(source.getComponentViews(), view);
            if (sourceView != null) {
                view.copyLayoutInformationFrom(sourceView);
            } else {
                log.warn("Could not find a matching view for \"" + view.getName() + "\" ... diagram layout information may be lost.");
            }
        }

        for (DynamicView view : dynamicViews) {
            DynamicView sourceView = findView(source.getDynamicViews(), view);
            if (sourceView != null) {
                view.copyLayoutInformationFrom(sourceView);
            } else {
                log.warn("Could not find a matching view for \"" + view.getName() + "\" ... diagram layout information may be lost.");
            }
        }
    }

    private <T extends View> T findView(Collection<T> views, T sourceView) {
        for (T view : views) {
            if (view.getKey() != null && view.getKey().equals(sourceView.getKey())) {
                return view;
            }
        }

        for (T view : views) {
            if (view.getName().equals(sourceView.getName())) {
                if (view.getDescription() != null) {
                    if (view.getDescription().equals(sourceView.getDescription())) {
                        return view;
                    }
                } else {
                    return view;
                }
            }
        }

        return null;
    }

    @JsonIgnore
    public boolean isEmpty() {
        return enterpriseContextViews.isEmpty() && systemContextViews.isEmpty() && containerViews.isEmpty() && componentViews.isEmpty() && filteredViews.isEmpty();
    }

}

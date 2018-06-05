package com.structurizr.view;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.structurizr.model.Container;
import com.structurizr.model.Model;
import com.structurizr.model.SoftwareSystem;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static com.structurizr.util.StringUtils.isNullOrEmpty;

/**
 * A set of views onto a software architecture model.
 */
public final class ViewSet {

    private static final Log log = LogFactory.getLog(ViewSet.class);

    private Model model;

    private Collection<SystemLandscapeView> systemLandscapeViews = new HashSet<>();
    private Collection<SystemContextView> systemContextViews = new HashSet<>();
    private Collection<ContainerView> containerViews = new HashSet<>();
    private Collection<ComponentView> componentViews = new HashSet<>();
    private Collection<DynamicView> dynamicViews = new HashSet<>();
    private Collection<DeploymentView> deploymentViews = new HashSet<>();

    private Collection<FilteredView> filteredViews = new HashSet<>();

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

    /**
     * Creates a system landscape view.
     *
     * @param key           the key for the view (must be unique)
     * @param description   a description of the view
     * @return              an SystemLandscapeView object
     * @throws              IllegalArgumentException if the key is not unique
     */
    public SystemLandscapeView createSystemLandscapeView(String key, String description) {
        assertThatTheViewKeyIsUnique(key);

        SystemLandscapeView view = new SystemLandscapeView(model, key, description);
        view.setViewSet(this);
        systemLandscapeViews.add(view);
        return view;
    }

    /**
     * Creates a system context view, where the scope of the view is the specified software system.
     *
     * @param softwareSystem    the SoftwareSystem object representing the scope of the view
     * @param key               the key for the view (must be unique)
     * @param description       a description of the view
     * @return                  a SystemContextView object
     * @throws                  IllegalArgumentException if the software system is null or the key is not unique
     */
    public SystemContextView createSystemContextView(SoftwareSystem softwareSystem, String key, String description) {
        assertThatTheSoftwareSystemIsNotNull(softwareSystem);
        assertThatTheViewKeyIsUnique(key);

        SystemContextView view = new SystemContextView(softwareSystem, key, description);
        view.setViewSet(this);
        systemContextViews.add(view);
        return view;
    }

    /**
     * Creates a container view, where the scope of the view is the specified software system.
     *
     * @param softwareSystem    the SoftwareSystem object representing the scope of the view
     * @param key               the key for the view (must be unique)
     * @param description       a description of the view
     * @return                  a ContainerView object
     * @throws                  IllegalArgumentException if the software system is null or the key is not unique
     */
    public ContainerView createContainerView(SoftwareSystem softwareSystem, String key, String description) {
        assertThatTheSoftwareSystemIsNotNull(softwareSystem);
        assertThatTheViewKeyIsUnique(key);

        ContainerView view = new ContainerView(softwareSystem, key, description);
        view.setViewSet(this);
        containerViews.add(view);
        return view;
    }

    /**
     * Creates a component view, where the scope of the view is the specified container.
     *
     * @param container         the Container object representing the scope of the view
     * @param key               the key for the view (must be unique)
     * @param description       a description of the view
     * @return                  a ContainerView object
     * @throws                  IllegalArgumentException if the container is null or the key is not unique
     */
    public ComponentView createComponentView(Container container, String key, String description) {
        assertThatTheContainerIsNotNull(container);
        assertThatTheViewKeyIsUnique(key);

        ComponentView view = new ComponentView(container, key, description);
        view.setViewSet(this);
        componentViews.add(view);
        return view;
    }

    /**
     * Creates a dynamic view.
     *
     * @param key           the key for the view (must be unique)
     * @param description   a description of the view
     * @return              a DynamicView object
     * @throws              IllegalArgumentException if the key is not unique
     */
    public DynamicView createDynamicView(String key, String description) {
        assertThatTheViewKeyIsUnique(key);

        DynamicView view = new DynamicView(getModel(), key, description);
        view.setViewSet(this);
        dynamicViews.add(view);
        return view;
    }

    /**
     * Creates a dynamic view, where the scope is the specified software system. The following
     * elements can be added to the resulting view:
     *
     * <ul>
     * <li>People</li>
     * <li>Software systems</li>
     * <li>Containers that reside inside the specified software system</li>
     * </ul>
     *
     * @param softwareSystem    the SoftwareSystem object representing the scope of the view
     * @param key               the key for the view (must be unique)
     * @param description       a description of the view
     * @return                  a DynamicView object
     * @throws                  IllegalArgumentException if the software system is null or the key is not unique
     */
    public DynamicView createDynamicView(SoftwareSystem softwareSystem, String key, String description) {
        assertThatTheSoftwareSystemIsNotNull(softwareSystem);
        assertThatTheViewKeyIsUnique(key);

        DynamicView view = new DynamicView(softwareSystem, key, description);
        view.setViewSet(this);
        dynamicViews.add(view);
        return view;
    }

    /**
     * Creates a dynamic view, where the scope is the specified container. The following
     * elements can be added to the resulting view:
     *
     * <ul>
     * <li>People</li>
     * <li>Software systems</li>
     * <li>Containers with the same parent software system as the specified container</li>
     * <li>Components within the specified container</li>
     * </ul>
     *
     * @param container         the Container object representing the scope of the view
     * @param key               the key for the view (must be unique)
     * @param description       a description of the view
     * @return                  a DynamicView object
     * @throws                  IllegalArgumentException if the container is null or the key is not unique
     */
    public DynamicView createDynamicView(Container container, String key, String description) {
        assertThatTheContainerIsNotNull(container);
        assertThatTheViewKeyIsUnique(key);

        DynamicView view = new DynamicView(container, key, description);
        view.setViewSet(this);
        dynamicViews.add(view);
        return view;
    }

    /**
     * Creates a deployment view.
     *
     * @param key           the key for the deployment view (must be unique)
     * @param description   a description of the  view
     * @return              a DeploymentView object
     * @throws              IllegalArgumentException if the key is not unique
     */
    public DeploymentView createDeploymentView(String key, String description) {
        assertThatTheViewKeyIsUnique(key);

        DeploymentView view = new DeploymentView(getModel(), key, description);
        view.setViewSet(this);
        deploymentViews.add(view);
        return view;
    }

    /**
     * Creates a deployment view, where the scope of the view is the specified software system.
     *
     * @param softwareSystem    the SoftwareSystem object representing the scope of the view
     * @param key               the key for the deployment view (must be unique)
     * @param description       a description of the view
     * @return                  a DeploymentView object
     * @throws                  IllegalArgumentException if the software system is null or the key is not unique
     */
    public DeploymentView createDeploymentView(SoftwareSystem softwareSystem, String key, String description) {
        assertThatTheSoftwareSystemIsNotNull(softwareSystem);
        assertThatTheViewKeyIsUnique(key);

        DeploymentView view = new DeploymentView(softwareSystem, key, description);
        view.setViewSet(this);
        deploymentViews.add(view);
        return view;
    }

    /**
     * Creates a FilteredView on top of an existing static view.
     *
     * @param view          the static view to base the FilteredView upon
     * @param key           the key for the filtered view (must be unique)
     * @param description   a description
     * @param mode          whether to Include or Exclude elements/relationships based upon their tag
     * @param tags          the tags to include or exclude
     * @return              a FilteredView object
     */
    public FilteredView createFilteredView(StaticView view, String key, String description, FilterMode mode, String... tags) {
        assertThatTheViewKeyIsUnique(key);

        FilteredView filteredView = new FilteredView(view, key, description, mode, tags);
        filteredViews.add(filteredView);
        return filteredView;
    }

    private void assertThatTheViewKeyIsUnique(String key) {
        if (getViewWithKey(key) != null || getFilteredViewWithKey(key) != null) {
            throw new IllegalArgumentException("A view with the key " + key + " already exists.");
        }
    }

    private void assertThatTheSoftwareSystemIsNotNull(SoftwareSystem softwareSystem) {
        if (softwareSystem == null) {
            throw new IllegalArgumentException("Software system must not be null.");
        }
    }

    private void assertThatTheContainerIsNotNull(Container container) {
        if (container == null) {
            throw new IllegalArgumentException("Container must not be null.");
        }
    }

    /**
     * Finds the view with the specified key, or null if the view does not exist.
     *
     * @param key   the key
     * @return  a View object, or null if a view with the specified key could not be found
     */
    public View getViewWithKey(String key) {
        if (key == null) {
            throw new IllegalArgumentException("A key must be specified.");
        }

        Set<View> views = new HashSet<>();
        views.addAll(systemLandscapeViews);
        views.addAll(systemContextViews);
        views.addAll(containerViews);
        views.addAll(componentViews);
        views.addAll(dynamicViews);
        views.addAll(deploymentViews);

        return views.stream().filter(v -> key.equals(v.getKey())).findFirst().orElse(null);
    }

    /**
     * Finds the filtered view with the specified key, or null if the view does not exist.
     *
     * @param key   the key
     * @return  a FilteredView object, or null if a view with the specified key could not be found
     */
    public FilteredView getFilteredViewWithKey(String key) {
        if (key == null) {
            throw new IllegalArgumentException("A key must be specified.");
        }

        return filteredViews.stream().filter(v -> key.equals(v.getKey())).findFirst().orElse(null);
    }

    /**
     * Gets the set of system landscape views.
     *
     * @return  a Collection of SystemLandscapeView objects
     */
    public Collection<SystemLandscapeView> getSystemLandscapeViews() {
        return new HashSet<>(systemLandscapeViews);
    }

    @JsonSetter("enterpriseContextViews")
    private void setEnterpriseContextViews(Collection<SystemLandscapeView> enterpriseContextViews) {
        systemLandscapeViews.addAll(enterpriseContextViews);
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

    public Collection<FilteredView> getFilteredViews() {
        return new HashSet<>(filteredViews);
    }

    /**
     * Gets the set of dynamic views.
     *
     * @return  a Collection of DynamicView objects
     */
    public Collection<DeploymentView> getDeploymentViews() {
        return new HashSet<>(deploymentViews);
    }

    public void hydrate() {
        for (SystemLandscapeView view : getSystemLandscapeViews()) {
            view.setModel(model);
            hydrateView(view);
        }

        for (SystemContextView view : systemContextViews) {
            view.setSoftwareSystem(model.getSoftwareSystemWithId(view.getSoftwareSystemId()));
            hydrateView(view);
        }

        for (ContainerView view : containerViews) {
            view.setSoftwareSystem(model.getSoftwareSystemWithId(view.getSoftwareSystemId()));
            hydrateView(view);
        }

        for (ComponentView view : componentViews) {
            view.setSoftwareSystem(model.getSoftwareSystemWithId(view.getSoftwareSystemId()));
            view.setContainer(view.getSoftwareSystem().getContainerWithId(view.getContainerId()));
            hydrateView(view);
        }

        for (DynamicView view : dynamicViews) {
            view.setModel(model);
            hydrateView(view);
        }

        for (DeploymentView view : deploymentViews) {
            if (!isNullOrEmpty(view.getSoftwareSystemId())) {
                view.setSoftwareSystem(model.getSoftwareSystemWithId(view.getSoftwareSystemId()));
            }

            view.setModel(model);
            hydrateView(view);
        }

        for (FilteredView filteredView : filteredViews) {
            filteredView.setView(getViewWithKey(filteredView.getBaseViewKey()));
        }
    }

    private void hydrateView(View view) {
        view.setViewSet(this);

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
        for (SystemLandscapeView view : getSystemLandscapeViews()) {
            SystemLandscapeView sourceView = findView(source.getSystemLandscapeViews(), view);
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

        for (DeploymentView view : deploymentViews) {
            DeploymentView sourceView = findView(source.getDeploymentViews(), view);
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
        return getSystemLandscapeViews().isEmpty() && systemContextViews.isEmpty() && containerViews.isEmpty() && componentViews.isEmpty() && filteredViews.isEmpty();
    }

}

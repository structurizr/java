package com.structurizr.view;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.structurizr.WorkspaceValidationException;
import com.structurizr.model.Container;
import com.structurizr.model.Element;
import com.structurizr.model.Model;
import com.structurizr.model.SoftwareSystem;
import com.structurizr.util.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
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

    ViewSet(Model model) {
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
        assertThatTheViewKeyIsSpecifiedAndUnique(key);

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
        assertThatTheViewKeyIsSpecifiedAndUnique(key);

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
        assertThatTheViewKeyIsSpecifiedAndUnique(key);

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
        assertThatTheViewKeyIsSpecifiedAndUnique(key);

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
        assertThatTheViewKeyIsSpecifiedAndUnique(key);

        DynamicView view = new DynamicView(model, key, description);
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
        assertThatTheViewKeyIsSpecifiedAndUnique(key);

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
        assertThatTheViewKeyIsSpecifiedAndUnique(key);

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
        assertThatTheViewKeyIsSpecifiedAndUnique(key);

        DeploymentView view = new DeploymentView(model, key, description);
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
        assertThatTheViewKeyIsSpecifiedAndUnique(key);

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
        assertThatTheViewIsNotNull(view);
        assertThatTheViewKeyIsSpecifiedAndUnique(key);

        FilteredView filteredView = new FilteredView(view, key, description, mode, tags);
        filteredViews.add(filteredView);
        return filteredView;
    }

    private void assertThatTheViewKeyIsSpecifiedAndUnique(String key) {
        if (StringUtils.isNullOrEmpty(key)) {
            throw new IllegalArgumentException("A key must be specified.");
        }

        if (getViewWithKey(key) != null || getFilteredViewWithKey(key) != null) {
            throw new IllegalArgumentException("A view with the key " + key + " already exists.");
        }
    }

    private void assertThatTheSoftwareSystemIsNotNull(SoftwareSystem softwareSystem) {
        if (softwareSystem == null) {
            throw new IllegalArgumentException("A software system must be specified.");
        }
    }

    private void assertThatTheContainerIsNotNull(Container container) {
        if (container == null) {
            throw new IllegalArgumentException("A container must be specified.");
        }
    }

    private void assertThatTheViewIsNotNull(View view) {
        if (view == null) {
            throw new IllegalArgumentException("A view must be specified.");
        }
    }

    /**
     * Finds the view with the specified key, or null if the view does not exist.
     *
     * @param key   the key
     * @return  a View object, or null if a view with the specified key could not be found
     */
    View getViewWithKey(String key) {
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
    FilteredView getFilteredViewWithKey(String key) {
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

    void setSystemLandscapeViews(Set<SystemLandscapeView> systemLandscapeViews) {
        if (systemLandscapeViews != null) {
            this.systemLandscapeViews = new HashSet<>(systemLandscapeViews);
        }
    }

    /**
     * (this is for backwards compatibility)
     */
    @JsonSetter("enterpriseContextViews")
    void setEnterpriseContextViews(Collection<SystemLandscapeView> enterpriseContextViews) {
        if (enterpriseContextViews != null) {
            this.systemLandscapeViews = new HashSet<>(enterpriseContextViews);
        }
    }

    /**
     * Gets the set of system context views.
     *
     * @return  a Collection of SystemContextView objects
     */
    public Collection<SystemContextView> getSystemContextViews() {
        return new HashSet<>(systemContextViews);
    }

    void setSystemContextViews(Set<SystemContextView> systemContextViews) {
        if (systemContextViews != null) {
            this.systemContextViews = new HashSet<>(systemContextViews);
        }
    }

    /**
     * Gets the set of container views.
     *
     * @return  a Collection of ContainerView objects
     */
    public Collection<ContainerView> getContainerViews() {
        return new HashSet<>(containerViews);
    }

    void setContainerViews(Set<ContainerView> containerViews) {
        if (containerViews != null) {
            this.containerViews = new HashSet<>(containerViews);
        }
    }

    /**
     * Gets the set of component views.
     *
     * @return  a Collection of ComponentView objects
     */
    public Collection<ComponentView> getComponentViews() {
        return new HashSet<>(componentViews);
    }

    void setComponentViews(Set<ComponentView> componentViews) {
        if (componentViews != null) {
            this.componentViews = new HashSet<>(componentViews);
        }
    }

    /**
     * Gets the set of dynamic views.
     *
     * @return  a Collection of DynamicView objects
     */
    public Collection<DynamicView> getDynamicViews() {
        return new HashSet<>(dynamicViews);
    }

    void setDynamicViews(Set<DynamicView> dynamicViews) {
        if (dynamicViews != null) {
            this.dynamicViews = new HashSet<>(dynamicViews);
        }
    }

    public Collection<FilteredView> getFilteredViews() {
        return new HashSet<>(filteredViews);
    }

    void setFilteredViews(Set<FilteredView> filteredViews) {
        if (filteredViews != null) {
            this.filteredViews = new HashSet<>(filteredViews);
        }
    }

    /**
     * Gets the set of dynamic views.
     *
     * @return  a Collection of DynamicView objects
     */
    public Collection<DeploymentView> getDeploymentViews() {
        return new HashSet<>(deploymentViews);
    }

    void setDeploymentViews(Set<DeploymentView> deploymentViews) {
        if (deploymentViews != null) {
            this.deploymentViews = new HashSet<>(deploymentViews);
        }
    }

    /**
     * Gets the set of all views (except filtered views).
     *
     * @return      a Collection of View objects
     */
    @JsonIgnore
    public Collection<View> getViews() {
        HashSet<View> views = new HashSet<>();

        views.addAll(getSystemLandscapeViews());
        views.addAll(getSystemContextViews());
        views.addAll(getContainerViews());
        views.addAll(getComponentViews());
        views.addAll(getDynamicViews());
        views.addAll(getDeploymentViews());

        return views;
    }

    void hydrate(Model model) {
        this.model = model;

        checkViewKeysAreUnique();

        for (SystemLandscapeView view : systemLandscapeViews) {
            view.setModel(model);
            hydrateView(view);
        }

        for (SystemContextView view : systemContextViews) {
            SoftwareSystem softwareSystem = model.getSoftwareSystemWithId(view.getSoftwareSystemId());
            if (softwareSystem == null) {
                throw new WorkspaceValidationException(
                        String.format("The system context view with key %s is associated with a software system (id=%s), but that element does not exist in the model.",
                                view.getKey(), view.getSoftwareSystemId())
                );
            }

            view.setSoftwareSystem(softwareSystem);
            hydrateView(view);
        }

        for (ContainerView view : containerViews) {
            SoftwareSystem softwareSystem = model.getSoftwareSystemWithId(view.getSoftwareSystemId());
            if (softwareSystem == null) {
                throw new WorkspaceValidationException(
                        String.format("The container view with key %s is associated with a software system (id=%s), but that element does not exist in the model.",
                                view.getKey(), view.getSoftwareSystemId())
                );
            }

            view.setSoftwareSystem(softwareSystem);
            hydrateView(view);
        }

        for (ComponentView view : componentViews) {
            SoftwareSystem softwareSystem = model.getSoftwareSystemWithId(view.getSoftwareSystemId());
            if (softwareSystem == null) {
                throw new WorkspaceValidationException(
                        String.format("The component view with key %s is associated with a software system (id=%s), but that element does not exist in the model.",
                                view.getKey(), view.getSoftwareSystemId())
                );
            }

            view.setSoftwareSystem(softwareSystem);

            Container container = softwareSystem.getContainerWithId(view.getContainerId());
            if (container == null) {
                throw new WorkspaceValidationException(
                        String.format("The component view with key %s is associated with a container (id=%s), but that element does not exist in the model.",
                                view.getKey(), view.getContainerId())
                );
            }

            view.setContainer(container);
            hydrateView(view);
        }

        for (DynamicView view : dynamicViews) {
            if (!isNullOrEmpty(view.getElementId())) {
                Element element = model.getElement(view.getElementId());
                if (element == null) {
                    throw new WorkspaceValidationException(
                            String.format("The dynamic view with key %s is associated with an element (id=%s), but that element does not exist in the model.",
                                    view.getKey(), view.getElementId())
                    );
                }

                view.setElement(element);
            }

            view.setModel(model);
            hydrateView(view);
        }

        for (DeploymentView view : deploymentViews) {
            if (!isNullOrEmpty(view.getSoftwareSystemId())) {
                SoftwareSystem softwareSystem = model.getSoftwareSystemWithId(view.getSoftwareSystemId());
                if (softwareSystem == null) {
                    throw new WorkspaceValidationException(
                            String.format("The deployment view with key %s is associated with a software system (id=%s), but that element does not exist in the model.",
                                    view.getKey(), view.getSoftwareSystemId())
                    );
                }

                view.setSoftwareSystem(softwareSystem);
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

    private void checkViewKeysAreUnique() {
        Set<String> keys = new HashSet<>();
        Collection<View> views = new ArrayList<>();
        views.addAll(systemLandscapeViews);
        views.addAll(systemContextViews);
        views.addAll(containerViews);
        views.addAll(componentViews);
        views.addAll(dynamicViews);
        views.addAll(deploymentViews);

        for (View view : views) {
            if (keys.contains(view.getKey())) {
                throw new WorkspaceValidationException("A view with the key " + view.getKey() + " already exists.");
            } else {
                keys.add(view.getKey());
            }
        }

        for (FilteredView filteredView : filteredViews) {
            if (keys.contains(filteredView.getKey())) {
                throw new WorkspaceValidationException("A view with the key " + filteredView.getKey() + " already exists.");
            } else {
                keys.add(filteredView.getKey());
            }
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
        for (SystemLandscapeView view : systemLandscapeViews) {
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
        return systemLandscapeViews.isEmpty() && systemContextViews.isEmpty() && containerViews.isEmpty() && componentViews.isEmpty() && dynamicViews.isEmpty() && deploymentViews.isEmpty() && filteredViews.isEmpty();
    }

}
package com.structurizr.view;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.structurizr.WorkspaceValidationException;
import com.structurizr.model.*;
import com.structurizr.util.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

import static com.structurizr.util.StringUtils.isNullOrEmpty;

/**
 * A set of views onto a software architecture model.
 */
public final class ViewSet {

    private static final Log log = LogFactory.getLog(ViewSet.class);

    public static final String SYSTEM_LANDSCAPE_VIEW_TYPE = "SystemLandscape";
    public static final String SYSTEM_CONTEXT_VIEW_TYPE = "SystemContext";
    public static final String CONTAINER_VIEW_TYPE = "Container";
    public static final String COMPONENT_VIEW_TYPE = "Component";
    public static final String DYNAMIC_VIEW_TYPE = "Dynamic";
    public static final String DEPLOYMENT_VIEW_TYPE = "Deployment";
    public static final String FILTERED_VIEW_TYPE = "Filtered";
    public static final String IMAGE_VIEW_TYPE = "Image";
    public static final String CUSTOM_VIEW_TYPE = "Custom";

    private Model model;

    private Collection<CustomView> customViews = new TreeSet<>();
    private Collection<SystemLandscapeView> systemLandscapeViews = new TreeSet<>();
    private Collection<SystemContextView> systemContextViews = new TreeSet<>();
    private Collection<ContainerView> containerViews = new TreeSet<>();
    private Collection<ComponentView> componentViews = new TreeSet<>();
    private Collection<DynamicView> dynamicViews = new TreeSet<>();
    private Collection<DeploymentView> deploymentViews = new TreeSet<>();
    private Collection<ImageView> imageViews = new TreeSet<>();

    private Collection<FilteredView> filteredViews = new TreeSet<>();

    private Configuration configuration = new Configuration();

    ViewSet() {
    }

    ViewSet(Model model) {
        this.model = model;
    }

    /**
     * Creates a custom view view.
     *
     * @param key           the key for the view (must be unique)
     * @param title         a title of the view
     * @param description   a description of the view
     * @return              a CustomView object
     * @throws              IllegalArgumentException if the key is not unique
     */
    public CustomView createCustomView(String key, String title, String description) {
        boolean keyIsAutomaticallyGenerated = false;

        if (StringUtils.isNullOrEmpty(key)) {
            key = generateViewKey(CUSTOM_VIEW_TYPE);
            keyIsAutomaticallyGenerated = true;
        }

        assertThatTheViewKeyIsSpecifiedAndUnique(key);

        CustomView view = new CustomView(model, key, title, description);
        view.setGeneratedKey(keyIsAutomaticallyGenerated);
        view.setOrder(getNextOrder());
        view.setViewSet(this);
        customViews.add(view);
        return view;
    }

    /**
     * Creates a system landscape view.
     *
     * @param key           the key for the view (must be unique)
     * @param description   a description of the view
     * @return              a SystemLandscapeView object
     * @throws              IllegalArgumentException if the key is not unique
     */
    public SystemLandscapeView createSystemLandscapeView(String key, String description) {
        boolean keyIsAutomaticallyGenerated = false;

        if (StringUtils.isNullOrEmpty(key)) {
            key = generateViewKey(SYSTEM_LANDSCAPE_VIEW_TYPE);
            keyIsAutomaticallyGenerated = true;
        }

        assertThatTheViewKeyIsSpecifiedAndUnique(key);

        SystemLandscapeView view = new SystemLandscapeView(model, key, description);
        view.setGeneratedKey(keyIsAutomaticallyGenerated);
        view.setOrder(getNextOrder());
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
        boolean keyIsAutomaticallyGenerated = false;

        if (StringUtils.isNullOrEmpty(key)) {
            key = generateViewKey(SYSTEM_CONTEXT_VIEW_TYPE);
            keyIsAutomaticallyGenerated = true;
        }

        assertThatTheSoftwareSystemIsNotNull(softwareSystem);
        assertThatTheViewKeyIsSpecifiedAndUnique(key);

        SystemContextView view = new SystemContextView(softwareSystem, key, description);
        view.setGeneratedKey(keyIsAutomaticallyGenerated);
        view.setOrder(getNextOrder());
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
        boolean keyIsAutomaticallyGenerated = false;

        if (StringUtils.isNullOrEmpty(key)) {
            key = generateViewKey(CONTAINER_VIEW_TYPE);
            keyIsAutomaticallyGenerated = true;
        }

        assertThatTheSoftwareSystemIsNotNull(softwareSystem);
        assertThatTheViewKeyIsSpecifiedAndUnique(key);

        ContainerView view = new ContainerView(softwareSystem, key, description);
        view.setGeneratedKey(keyIsAutomaticallyGenerated);
        view.setOrder(getNextOrder());
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
        boolean keyIsAutomaticallyGenerated = false;

        if (StringUtils.isNullOrEmpty(key)) {
            key = generateViewKey(COMPONENT_VIEW_TYPE);
            keyIsAutomaticallyGenerated = true;
        }

        assertThatTheContainerIsNotNull(container);
        assertThatTheViewKeyIsSpecifiedAndUnique(key);

        ComponentView view = new ComponentView(container, key, description);
        view.setGeneratedKey(keyIsAutomaticallyGenerated);
        view.setOrder(getNextOrder());
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
        boolean keyIsAutomaticallyGenerated = false;

        if (StringUtils.isNullOrEmpty(key)) {
            key = generateViewKey(DYNAMIC_VIEW_TYPE);
            keyIsAutomaticallyGenerated = true;
        }

        assertThatTheViewKeyIsSpecifiedAndUnique(key);

        DynamicView view = new DynamicView(model, key, description);
        view.setGeneratedKey(keyIsAutomaticallyGenerated);
        view.setOrder(getNextOrder());
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
        boolean keyIsAutomaticallyGenerated = false;

        if (StringUtils.isNullOrEmpty(key)) {
            key = generateViewKey(DYNAMIC_VIEW_TYPE);
            keyIsAutomaticallyGenerated = true;
        }

        assertThatTheSoftwareSystemIsNotNull(softwareSystem);
        assertThatTheViewKeyIsSpecifiedAndUnique(key);

        DynamicView view = new DynamicView(softwareSystem, key, description);
        view.setGeneratedKey(keyIsAutomaticallyGenerated);
        view.setOrder(getNextOrder());
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
        boolean keyIsAutomaticallyGenerated = false;

        if (StringUtils.isNullOrEmpty(key)) {
            key = generateViewKey(DYNAMIC_VIEW_TYPE);
            keyIsAutomaticallyGenerated = true;
        }

        assertThatTheContainerIsNotNull(container);
        assertThatTheViewKeyIsSpecifiedAndUnique(key);

        DynamicView view = new DynamicView(container, key, description);
        view.setGeneratedKey(keyIsAutomaticallyGenerated);
        view.setOrder(getNextOrder());
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
        boolean keyIsAutomaticallyGenerated = false;

        if (StringUtils.isNullOrEmpty(key)) {
            key = generateViewKey(DEPLOYMENT_VIEW_TYPE);
            keyIsAutomaticallyGenerated = true;
        }

        assertThatTheViewKeyIsSpecifiedAndUnique(key);

        DeploymentView view = new DeploymentView(model, key, description);
        view.setGeneratedKey(keyIsAutomaticallyGenerated);
        view.setOrder(getNextOrder());
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
        boolean keyIsAutomaticallyGenerated = false;

        if (StringUtils.isNullOrEmpty(key)) {
            key = generateViewKey(DEPLOYMENT_VIEW_TYPE);
            keyIsAutomaticallyGenerated = true;
        }

        assertThatTheSoftwareSystemIsNotNull(softwareSystem);
        assertThatTheViewKeyIsSpecifiedAndUnique(key);

        DeploymentView view = new DeploymentView(softwareSystem, key, description);
        view.setGeneratedKey(keyIsAutomaticallyGenerated);
        view.setOrder(getNextOrder());
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
        boolean keyIsAutomaticallyGenerated = false;

        if (StringUtils.isNullOrEmpty(key)) {
            key = generateViewKey(FILTERED_VIEW_TYPE);
            keyIsAutomaticallyGenerated = true;
        }

        assertThatTheViewIsNotNull(view);
        assertThatTheViewKeyIsSpecifiedAndUnique(key);

        FilteredView filteredView = new FilteredView(view, key, description, mode, tags);
        filteredView.setGeneratedKey(keyIsAutomaticallyGenerated);
        filteredView.setOrder(getNextOrder());
        filteredView.setViewSet(this);
        filteredViews.add(filteredView);
        return filteredView;
    }

    /**
     * Creates an image view.
     *
     * @param key               the key for the view (must be unique)
     * @return                  an ImageView object
     * @throws                  IllegalArgumentException if the key is not unique
     */
    public ImageView createImageView(String key) {
        boolean keyIsAutomaticallyGenerated = false;

        if (StringUtils.isNullOrEmpty(key)) {
            key = generateViewKey(IMAGE_VIEW_TYPE);
            keyIsAutomaticallyGenerated = true;
        }

        assertThatTheViewKeyIsSpecifiedAndUnique(key);

        ImageView view = new ImageView(key);
        view.setGeneratedKey(keyIsAutomaticallyGenerated);
        view.setOrder(getNextOrder());
        view.setViewSet(this);
        imageViews.add(view);
        return view;
    }

    /**
     * Creates an image view, where the scope is the specified element.
     *
     * @param element           the Element object representing the scope of the view
     * @param key               the key for the view (must be unique)
     * @return                  an ImageView object
     * @throws                  IllegalArgumentException if the element is null or the key is not unique
     */
    public ImageView createImageView(Element element, String key) {
        boolean keyIsAutomaticallyGenerated = false;

        if (StringUtils.isNullOrEmpty(key)) {
            key = generateViewKey(IMAGE_VIEW_TYPE);
            keyIsAutomaticallyGenerated = true;
        }

        if (element == null) {
            throw new IllegalArgumentException("An element must be specified.");
        }
        assertThatTheViewKeyIsSpecifiedAndUnique(key);

        ImageView view = new ImageView(element, key);
        view.setGeneratedKey(keyIsAutomaticallyGenerated);
        view.setOrder(getNextOrder());
        view.setViewSet(this);
        imageViews.add(view);
        return view;
    }

    private void assertThatTheViewKeyIsSpecifiedAndUnique(String key) {
        if (StringUtils.isNullOrEmpty(key)) {
            throw new IllegalArgumentException("A key must be specified.");
        }

        if (getViewWithKey(key) != null || getFilteredViewWithKey(key) != null || getImageViewWithKey(key) != null) {
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
    public View getViewWithKey(String key) {
        if (key == null) {
            throw new IllegalArgumentException("A key must be specified.");
        }

        return getViews().stream().filter(v -> key.equals(v.getKey())).findFirst().orElse(null);
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
     * Finds the image view with the specified key, or null if the view does not exist.
     *
     * @param key   the key
     * @return  a ImageView object, or null if a view with the specified key could not be found
     */
    ImageView getImageViewWithKey(String key) {
        if (key == null) {
            throw new IllegalArgumentException("A key must be specified.");
        }

        return imageViews.stream().filter(v -> key.equals(v.getKey())).findFirst().orElse(null);
    }

    /**
     * Gets the set of custom views.
     *
     * @return  a Collection of CustomView objects
     */
    public Collection<CustomView> getCustomViews() {
        return new TreeSet<>(customViews);
    }

    void setCustomViews(Set<CustomView> customViews) {
        if (customViews != null) {
            this.customViews = new TreeSet<>(customViews);
        }
    }

    /**
     * Gets the set of system landscape views.
     *
     * @return  a Collection of SystemLandscapeView objects
     */
    public Collection<SystemLandscapeView> getSystemLandscapeViews() {
        return new TreeSet<>(systemLandscapeViews);
    }

    void setSystemLandscapeViews(Set<SystemLandscapeView> systemLandscapeViews) {
        if (systemLandscapeViews != null) {
            this.systemLandscapeViews = new TreeSet<>(systemLandscapeViews);
        }
    }

    /**
     * (this is for backwards compatibility)
     */
    @JsonSetter("enterpriseContextViews")
    void setEnterpriseContextViews(Collection<SystemLandscapeView> enterpriseContextViews) {
        if (enterpriseContextViews != null) {
            this.systemLandscapeViews = new TreeSet<>(enterpriseContextViews);
        }
    }

    /**
     * Gets the set of system context views.
     *
     * @return  a Collection of SystemContextView objects
     */
    public Collection<SystemContextView> getSystemContextViews() {
        return new TreeSet<>(systemContextViews);
    }

    void setSystemContextViews(Set<SystemContextView> systemContextViews) {
        if (systemContextViews != null) {
            this.systemContextViews = new TreeSet<>(systemContextViews);
        }
    }

    /**
     * Gets the set of container views.
     *
     * @return  a Collection of ContainerView objects
     */
    public Collection<ContainerView> getContainerViews() {
        return new TreeSet<>(containerViews);
    }

    void setContainerViews(Set<ContainerView> containerViews) {
        if (containerViews != null) {
            this.containerViews = new TreeSet<>(containerViews);
        }
    }

    /**
     * Gets the set of component views.
     *
     * @return  a Collection of ComponentView objects
     */
    public Collection<ComponentView> getComponentViews() {
        return new TreeSet<>(componentViews);
    }

    void setComponentViews(Set<ComponentView> componentViews) {
        if (componentViews != null) {
            this.componentViews = new TreeSet<>(componentViews);
        }
    }

    /**
     * Gets the set of dynamic views.
     *
     * @return  a Collection of DynamicView objects
     */
    public Collection<DynamicView> getDynamicViews() {
        return new TreeSet<>(dynamicViews);
    }

    void setDynamicViews(Set<DynamicView> dynamicViews) {
        if (dynamicViews != null) {
            this.dynamicViews = new TreeSet<>(dynamicViews);
        }
    }

    public Collection<FilteredView> getFilteredViews() {
        return new TreeSet<>(filteredViews);
    }

    void setFilteredViews(Set<FilteredView> filteredViews) {
        if (filteredViews != null) {
            this.filteredViews = new TreeSet<>(filteredViews);
        }
    }

    /**
     * Gets the set of deployment views.
     *
     * @return  a Collection of DeploymentView objects
     */
    public Collection<DeploymentView> getDeploymentViews() {
        return new TreeSet<>(deploymentViews);
    }

    void setDeploymentViews(Set<DeploymentView> deploymentViews) {
        if (deploymentViews != null) {
            this.deploymentViews = new TreeSet<>(deploymentViews);
        }
    }

    /**
     * Gets the set of image views.
     *
     * @return  a Collection of ImageView objects
     */
    public Collection<ImageView> getImageViews() {
        return new TreeSet<>(imageViews);
    }

    void setImageView(Set<ImageView> imageViews) {
        if (imageViews != null) {
            this.imageViews = new TreeSet<>(imageViews);
        }
    }

    /**
     * Gets the set of all views.
     *
     * @return      a Collection of View objects
     */
    @JsonIgnore
    public Collection<View> getViews() {
        Set<View> views = new TreeSet<>();

        views.addAll(getCustomViews());
        views.addAll(getSystemLandscapeViews());
        views.addAll(getSystemContextViews());
        views.addAll(getContainerViews());
        views.addAll(getComponentViews());
        views.addAll(getDynamicViews());
        views.addAll(getDeploymentViews());
        views.addAll(getFilteredViews());
        views.addAll(getImageViews());

        return views;
    }

    void hydrate(Model model) {
        this.model = model;

        checkViewKeysAreUnique();

        for (CustomView view : customViews) {
            view.setModel(model);
            hydrateView(view);
        }

        for (SystemLandscapeView view : systemLandscapeViews) {
            view.setModel(model);
            hydrateView(view);
        }

        for (SystemContextView view : systemContextViews) {
            SoftwareSystem softwareSystem = model.getSoftwareSystemWithId(view.getSoftwareSystemId());
            if (softwareSystem == null) {
                throw new WorkspaceValidationException(
                        String.format("The system context view with key \"%s\" is associated with a software system (id=%s), but that element does not exist in the model.",
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
                        String.format("The container view with key \"%s\" is associated with a software system (id=%s), but that element does not exist in the model.",
                                view.getKey(), view.getSoftwareSystemId())
                );
            }

            view.setSoftwareSystem(softwareSystem);
            hydrateView(view);
        }

        for (ComponentView view : componentViews) {
            Container container = (Container)model.getElement(view.getContainerId());
            if (container == null) {
                throw new WorkspaceValidationException(
                        String.format("The component view with key \"%s\" is associated with a container (id=%s), but that element does not exist in the model.",
                                view.getKey(), view.getContainerId())
                );
            }

            view.setContainer(container);
            view.setSoftwareSystem(container.getSoftwareSystem());
            hydrateView(view);
        }

        for (DynamicView view : dynamicViews) {
            if (!isNullOrEmpty(view.getElementId())) {
                Element element = model.getElement(view.getElementId());
                if (element == null) {
                    throw new WorkspaceValidationException(
                            String.format("The dynamic view with key \"%s\" is associated with an element (id=%s), but that element does not exist in the model.",
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
                            String.format("The deployment view with key \"%s\" is associated with a software system (id=%s), but that element does not exist in the model.",
                                    view.getKey(), view.getSoftwareSystemId())
                    );
                }

                view.setSoftwareSystem(softwareSystem);
            }

            view.setModel(model);
            hydrateView(view);
        }

        for (FilteredView filteredView : filteredViews) {
            View view = getViewWithKey(filteredView.getBaseViewKey());
            if (view == null) {
                throw new WorkspaceValidationException(
                        String.format("The filtered view with key \"%s\" is based upon a view (key=%s), but that view does not exist in the workspace.",
                                filteredView.getKey(), filteredView.getBaseViewKey())
                );
            }

            if (view instanceof StaticView) {
                filteredView.setView((StaticView)view);
            } else {
                throw new WorkspaceValidationException(
                        String.format("The filtered view with key \"%s\" is based upon a view (key=%s), but that view is not a static view.",
                                filteredView.getKey(), filteredView.getBaseViewKey())
                );
            }
        }

        for (ImageView view : imageViews) {
            if (!isNullOrEmpty(view.getElementId())) {
                Element element = model.getElement(view.getElementId());
                if (element == null) {
                    throw new WorkspaceValidationException(
                            String.format("The image view with key \"%s\" is associated with an element (id=%s), but that element does not exist in the model.",
                                    view.getKey(), view.getElementId())
                    );
                }

                view.setElement(element);
            }
        }
    }

    private void hydrateView(ModelView view) {
        view.setViewSet(this);

        for (ElementView elementView : view.getElements()) {
            Element element = model.getElement(elementView.getId());
            if (element == null) {
                throw new WorkspaceValidationException(
                        String.format("The view with key \"%s\" references an element (id=%s), but that element does not exist in the model.",
                                view.getKey(), elementView.getId())
                );
            }

            elementView.setElement(element);
        }

        for (RelationshipView relationshipView : view.getRelationships()) {
            Relationship relationship = model.getRelationship(relationshipView.getId());
            if (relationship == null) {
                throw new WorkspaceValidationException(
                        String.format("The view with key \"%s\" references a relationship (id=%s), but that relationship does not exist in the model.",
                                view.getKey(), relationshipView.getId())
                );
            }

            relationshipView.setRelationship(relationship);
        }
    }

    private void checkViewKeysAreUnique() {
        Set<String> keys = new HashSet<>();

        for (View view : getViews()) {
            if (keys.contains(view.getKey())) {
                throw new WorkspaceValidationException("A view with the key " + view.getKey() + " already exists.");
            } else {
                keys.add(view.getKey());
            }
        }
    }

    private synchronized int getNextOrder() {
        return getViews().stream().max(Comparator.comparingInt(View::getOrder)).map(View::getOrder).orElse(0) + 1;
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
        for (CustomView view : customViews) {
            if (view.getAutomaticLayout() == null && view.getMergeFromRemote() == true) {
                CustomView sourceView = findView(source.getCustomViews(), view);
                if (sourceView != null) {
                    view.copyLayoutInformationFrom(sourceView);
                } else {
                    log.warn("Could not find a matching view for \"" + view.getName() + "\" ... diagram layout information may be lost.");
                }
            }
        }

        for (SystemLandscapeView view : systemLandscapeViews) {
            if (view.getAutomaticLayout() == null && view.getMergeFromRemote() == true) {
                SystemLandscapeView sourceView = findView(source.getSystemLandscapeViews(), view);
                if (sourceView != null) {
                    view.copyLayoutInformationFrom(sourceView);
                } else {
                    log.warn("Could not find a matching view for \"" + view.getName() + "\" ... diagram layout information may be lost.");
                }
            }
        }

        for (SystemContextView view : systemContextViews) {
            if (view.getAutomaticLayout() == null && view.getMergeFromRemote() == true) {
                SystemContextView sourceView = findView(source.getSystemContextViews(), view);
                if (sourceView != null) {
                    view.copyLayoutInformationFrom(sourceView);
                } else {
                    log.warn("Could not find a matching view for \"" + view.getName() + "\" ... diagram layout information may be lost.");
                }
            }
        }

        for (ContainerView view : containerViews) {
            if (view.getAutomaticLayout() == null && view.getMergeFromRemote() == true) {
                ContainerView sourceView = findView(source.getContainerViews(), view);
                if (sourceView != null) {
                    view.copyLayoutInformationFrom(sourceView);
                } else {
                    log.warn("Could not find a matching view for \"" + view.getName() + "\" ... diagram layout information may be lost.");
                }
            }
        }

        for (ComponentView view : componentViews) {
            if (view.getAutomaticLayout() == null && view.getMergeFromRemote() == true) {
                ComponentView sourceView = findView(source.getComponentViews(), view);
                if (sourceView != null) {
                    view.copyLayoutInformationFrom(sourceView);
                } else {
                    log.warn("Could not find a matching view for \"" + view.getName() + "\" ... diagram layout information may be lost.");
                }
            }
        }

        for (DynamicView view : dynamicViews) {
            if (view.getAutomaticLayout() == null && view.getMergeFromRemote() == true) {
                DynamicView sourceView = findView(source.getDynamicViews(), view);
                if (sourceView != null) {
                    view.copyLayoutInformationFrom(sourceView);
                } else {
                    log.warn("Could not find a matching view for \"" + view.getName() + "\" ... diagram layout information may be lost.");
                }
            }
        }

        for (DeploymentView view : deploymentViews) {
            if (view.getAutomaticLayout() == null && view.getMergeFromRemote() == true) {
                DeploymentView sourceView = findView(source.getDeploymentViews(), view);
                if (sourceView != null) {
                    view.copyLayoutInformationFrom(sourceView);
                } else {
                    log.warn("Could not find a matching view for \"" + view.getName() + "\" ... diagram layout information may be lost.");
                }
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
        return customViews.isEmpty() && systemLandscapeViews.isEmpty() && systemContextViews.isEmpty() && containerViews.isEmpty() && componentViews.isEmpty() && dynamicViews.isEmpty() && deploymentViews.isEmpty() && filteredViews.isEmpty() && imageViews.isEmpty();
    }

    private String generateViewKey(String prefix) {
        NumberFormat format = new DecimalFormat("000");
        int counter = 1;
        String key = prefix + "-" + format.format(counter);

        while (hasViewWithKey(key)) {
            counter++;
            key = prefix + "-" + format.format(counter);
        }

        return key;
    }

    private boolean hasViewWithKey(String key) {
        return getViews().stream().anyMatch(view -> view.getKey().equals(key));
    }

    public void createDefaultViews() {
        // create a single System Landscape diagram containing all people and software systems
        SystemLandscapeView systemLandscapeView = createSystemLandscapeView("", "");
        systemLandscapeView.addDefaultElements();
        systemLandscapeView.enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300);

        if (!model.getSoftwareSystems().isEmpty()) {
            List<SoftwareSystem> softwareSystems = new ArrayList<>(model.getSoftwareSystems());
            softwareSystems.sort(Comparator.comparing(Element::getName));

            // and a system context view plus container view for each software system
            for (SoftwareSystem softwareSystem : softwareSystems) {
                SystemContextView systemContextView = createSystemContextView(softwareSystem, "", "");
                systemContextView.addDefaultElements();
                systemContextView.enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300);

                if (softwareSystem.getContainers().size() > 0) {
                    List<Container> containers = new ArrayList<>(softwareSystem.getContainers());
                    containers.sort(Comparator.comparing(Element::getName));

                    ContainerView containerView = createContainerView(softwareSystem, "", "");
                    containerView.addDefaultElements();
                    containerView.enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300);

                    for (Container container : containers) {
                        if (container.getComponents().size() > 0) {
                            ComponentView componentView = createComponentView(container, "", "");
                            componentView.addDefaultElements();
                            componentView.enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300);
                        }
                    }
                }
            }
        }

        // and deployment views for each environment and software system pair
        List<String> deploymentEnvironments = new ArrayList<>();
        for (DeploymentNode deploymentNode : model.getDeploymentNodes()) {
            String environment = deploymentNode.getEnvironment();
            if (!deploymentEnvironments.contains(environment)) {
                deploymentEnvironments.add(environment);
            }
        }
        deploymentEnvironments.sort(String::compareTo);

        for (String deploymentEnvironment : deploymentEnvironments) {
            List<SoftwareSystem> softwareSystems = new ArrayList<>();
            for (DeploymentNode deploymentNode : model.getDeploymentNodes()) {
                if (deploymentNode.getEnvironment().equals(deploymentEnvironment)) {
                    Set<SoftwareSystemInstance> softwareSystemInstances = getSoftwareSystemInstances(deploymentNode);
                    for (SoftwareSystemInstance softwareSystemInstance : softwareSystemInstances) {
                        SoftwareSystem softwareSystem = softwareSystemInstance.getSoftwareSystem();
                        if (!softwareSystems.contains(softwareSystem)) {
                            softwareSystems.add(softwareSystem);
                        }
                    }

                    Set<ContainerInstance> containerInstances = getContainerInstances(deploymentNode);
                    for (ContainerInstance containerInstance : containerInstances) {
                        SoftwareSystem softwareSystem = containerInstance.getContainer().getSoftwareSystem();
                        if (!softwareSystems.contains(softwareSystem)) {
                            softwareSystems.add(softwareSystem);
                        }
                    }
                }
            }

            if (softwareSystems.isEmpty()) {
                // there are no container instances, but perhaps there are infrastructure nodes in this environment
                if (model.getElements().stream().anyMatch(e -> e instanceof InfrastructureNode && ((InfrastructureNode)e).getEnvironment().equals(deploymentEnvironment))) {
                    DeploymentView deploymentView = createDeploymentView("", "");
                    deploymentView.setEnvironment(deploymentEnvironment);
                    deploymentView.addDefaultElements();
                    deploymentView.enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300);
                }
            } else {
                softwareSystems.sort(Comparator.comparing(Element::getName));

                for (SoftwareSystem softwareSystem : softwareSystems) {
                    DeploymentView deploymentView = createDeploymentView(softwareSystem, "", "");
                    deploymentView.setEnvironment(deploymentEnvironment);
                    deploymentView.addDefaultElements();
                    deploymentView.enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300);
                }
            }
        }
    }

    private Set<SoftwareSystemInstance> getSoftwareSystemInstances(DeploymentNode deploymentNode) {
        Set<SoftwareSystemInstance> softwareSystemInstances = new TreeSet<>(deploymentNode.getSoftwareSystemInstances());

        for (DeploymentNode child : deploymentNode.getChildren()) {
            softwareSystemInstances.addAll(getSoftwareSystemInstances(child));
        }

        return softwareSystemInstances;
    }

    private Set<ContainerInstance> getContainerInstances(DeploymentNode deploymentNode) {
        Set<ContainerInstance> containerInstances = new TreeSet<>(deploymentNode.getContainerInstances());

        for (DeploymentNode child : deploymentNode.getChildren()) {
            containerInstances.addAll(getContainerInstances(child));
        }

        return containerInstances;
    }

    /**
     * Removes all views and configuration.
     */
    public void clear() {
        customViews = new TreeSet<>();
        systemLandscapeViews = new TreeSet<>();
        systemContextViews = new TreeSet<>();
        containerViews = new TreeSet<>();
        componentViews = new TreeSet<>();
        dynamicViews = new TreeSet<>();
        deploymentViews = new TreeSet<>();
        filteredViews = new TreeSet<>();
        configuration = new Configuration();
    }

}
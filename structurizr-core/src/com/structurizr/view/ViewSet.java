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

    public EnterpriseContextView createEnterpriseContextView(String key, String description) {
        if (getViewWithKey(key) != null) {
            throw new IllegalArgumentException("A view with the key " + key + " already exists.");
        } else {
            EnterpriseContextView view = new EnterpriseContextView(model, key, description);
            enterpriseContextViews.add(view);
            return view;
        }
    }

    public SystemContextView createSystemContextView(SoftwareSystem softwareSystem, String key, String description) {
        if (getViewWithKey(key) != null) {
            throw new IllegalArgumentException("A view with the key " + key + " already exists.");
        } else {
            SystemContextView view = new SystemContextView(softwareSystem, key, description);
            systemContextViews.add(view);
            return view;
        }
    }

    public ContainerView createContainerView(SoftwareSystem softwareSystem, String key, String description) {
        if (getViewWithKey(key) != null) {
            throw new IllegalArgumentException("A view with the key " + key + " already exists.");
        } else {
            ContainerView view = new ContainerView(softwareSystem, key, description);
            containerViews.add(view);
            return view;
        }
    }

    public ComponentView createComponentView(Container container, String key, String description) {
        if (getViewWithKey(key) != null) {
            throw new IllegalArgumentException("A view with the key " + key + " already exists.");
        } else {
            ComponentView view = new ComponentView(container, key, description);
            componentViews.add(view);
            return view;
        }
    }

    /**
     * Creates a dynamic view.
     *
     * @param key           the key for the dynamic view (must be unique)
     * @param description   a description of the dynamic view
     * @return              a DynamicView object
     */
    public DynamicView createDynamicView(String key, String description) {
        if (getViewWithKey(key) != null) {
            throw new IllegalArgumentException("A view with the key " + key + " already exists.");
        } else {
            DynamicView view = new DynamicView(getModel(), key, description);
            dynamicViews.add(view);
            return view;
        }
    }

    /**
     * Creates a dynamic view, where the scope is the specified software system. The following
     * elements can be added to the resulting view:
     * <ul>
     * <li>People</li>
     * <li>Software systems</li>
     * <li>Containers that reside inside the specified software system</li>
     * </ul>
     *
     * @param softwareSystem    the SoftwareSystem object representing the scope of the view
     * @param key               the key for the dynamic view (must be unique)
     * @param description       a description of the dynamic view
     * @return                  a DynamicView object
     */
    public DynamicView createDynamicView(SoftwareSystem softwareSystem, String key, String description) {
        if (softwareSystem == null) {
            throw new IllegalArgumentException("Software system must not be null.");
        }

        if (getViewWithKey(key) != null) {
            throw new IllegalArgumentException("A view with the key " + key + " already exists.");
        } else {
            DynamicView view = new DynamicView(softwareSystem, key, description);
            dynamicViews.add(view);
            return view;
        }
    }

    /**
     * Creates a dynamic view, where the scope is the specified container. The following
     * elements can be added to the resulting view:
     * <ul>
     * <li>People</li>
     * <li>Software systems</li>
     * <li>Containers with the same parent software system as the specified container</li>
     * <li>Components within the specified container</li>
     * </ul>
     *
     * @param container         the Container object representing the scope of the view
     * @param key               the key for the dynamic view (must be unique)
     * @param description       a description of the dynamic view
     * @return                  a DynamicView object
     */
    public DynamicView createDynamicView(Container container, String key, String description) {
        if (container == null) {
            throw new IllegalArgumentException("Container must not be null.");
        }

        if (getViewWithKey(key) != null) {
            throw new IllegalArgumentException("A view with the key " + key + " already exists.");
        } else {
            DynamicView view = new DynamicView(container, key, description);
            dynamicViews.add(view);
            return view;
        }
    }

    /**
     * Creates a FilteredView on top of an existing view.
     *
     * @param view          the view to base the FilteredView upon
     * @param key           the key for the filtered view (must be unique)
     * @param description   a description
     * @param mode          whether to Include or Exclude elements/relationships based upon their tag
     * @param tags          the tags to include or exclude
     * @return              a FilteredView object
     */
    public FilteredView createFilteredView(View view, String key, String description, FilterMode mode, String... tags) {
        if (getViewWithKey(key) != null) {
            throw new IllegalArgumentException("A view with the key " + key + " already exists.");
        } else {
            FilteredView filteredView = new FilteredView(view, key, description, mode, tags);
            filteredViews.add(filteredView);
            return filteredView;
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

    public Collection<FilteredView> getFilteredViews() {
        return new HashSet<>(filteredViews);
    }

    public void hydrate() {
        for (EnterpriseContextView view : enterpriseContextViews) {
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

        for (FilteredView filteredView : filteredViews) {
            filteredView.setView(getViewWithKey(filteredView.getBaseViewKey()));
        }
    }

    private void hydrateView(View view) {
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

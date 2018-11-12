package com.structurizr.analysis;

import com.structurizr.model.Component;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * <p>
 *     This component finder strategy knows how to find the following Spring components:
 * </p>
 *
 * <ul>
 *     <li>Spring MVC controllers (classes annotated @Controller)</li>
 *     <li>Spring REST controllers (classes annotated @RestController)</li>
 *     <li>Spring services (classes annotated @Service)</li>
 *     <li>Spring components (classes annotated @Component)</li>
 *     <li>Spring repositories (classes annotated @Repository, plus those that extend JpaRepository or CrudRepository)</li>
 * </ul>
 *
 * <p>
 *     By default, non-public types will be ignored so that, for example, you can
 *     hide repository implementations behind services, as described at
 *     <a href="http://olivergierke.de/2013/01/whoops-where-did-my-architecture-go/">Whoops! Where did my architecture go</a>.
 *     You can change this behaviour by passing false to {@link #setIncludePublicTypesOnly(boolean)}.
 * </p>
 */
public class SpringComponentFinderStrategy extends AbstractSpringComponentFinderStrategy {

    private List<AbstractSpringComponentFinderStrategy> componentFinderStrategies = new LinkedList<>();

    public SpringComponentFinderStrategy(SupportingTypesStrategy... strategies) {
        super(strategies);
    }

    @Override
    public void beforeFindComponents() {
        super.beforeFindComponents();

        componentFinderStrategies.add(new SpringRestControllerComponentFinderStrategy());
        componentFinderStrategies.add(new SpringMvcControllerComponentFinderStrategy());
        componentFinderStrategies.add(new SpringServiceComponentFinderStrategy());
        componentFinderStrategies.add(new SpringComponentComponentFinderStrategy());
        componentFinderStrategies.add(new SpringRepositoryComponentFinderStrategy());

        for (AbstractSpringComponentFinderStrategy componentFinderStrategy : componentFinderStrategies) {
            componentFinderStrategy.setIncludePublicTypesOnly(includePublicTypesOnly);
            componentFinderStrategy.setComponentFinder(getComponentFinder());
            supportingTypesStrategies.forEach(componentFinderStrategy::addSupportingTypesStrategy);
            componentFinderStrategy.setDuplicateComponentStrategy(getDuplicateComponentStrategy());
            componentFinderStrategy.beforeFindComponents();
        }
    }

    @Override
    protected Set<Component> doFindComponents() {
        Set<Component> components = new HashSet<>();

        for (AbstractComponentFinderStrategy componentFinderStrategy : componentFinderStrategies) {
            components.addAll(componentFinderStrategy.findComponents());
        }

        return components;
    }

}
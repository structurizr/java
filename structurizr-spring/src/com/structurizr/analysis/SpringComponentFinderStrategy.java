package com.structurizr.analysis;

import com.structurizr.model.Component;

import java.util.HashSet;
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
 * </p>

 * <p>
 *     You can change this behaviour by passing false to {@link #setIncludePublicTypesOnly(boolean)}.
 * </p>
 *
 * @author Simon Brown
 */
public class SpringComponentFinderStrategy extends AbstractSpringComponentFinderStrategy {

    private SpringRestControllerComponentFinderStrategy springRestControllerComponentFinderStrategy;
    private SpringMvcControllerComponentFinderStrategy springMvcControllerComponentFinderStrategy;
    private SpringServiceComponentFinderStrategy springServiceComponentFinderStrategy;
    private SpringComponentComponentFinderStrategy springComponentComponentFinderStrategy;
    private SpringRepositoryComponentFinderStrategy springRepositoryComponentFinderStrategy;

    public SpringComponentFinderStrategy(SupportingTypesStrategy... strategies) {
        super(strategies);
    }

    @Override
    protected Set<Component> doFindComponents() throws Exception {
        Set<Component> components = new HashSet<>();

        springRestControllerComponentFinderStrategy = new SpringRestControllerComponentFinderStrategy();
        springRestControllerComponentFinderStrategy.setIncludePublicTypesOnly(includePublicTypesOnly);
        springRestControllerComponentFinderStrategy.setComponentFinder(getComponentFinder());
        supportingTypesStrategies.forEach(springRestControllerComponentFinderStrategy::addSupportingTypesStrategy);
        components.addAll(springRestControllerComponentFinderStrategy.findComponents());

        springMvcControllerComponentFinderStrategy = new SpringMvcControllerComponentFinderStrategy();
        springMvcControllerComponentFinderStrategy.setIncludePublicTypesOnly(includePublicTypesOnly);
        springMvcControllerComponentFinderStrategy.setComponentFinder(getComponentFinder());
        supportingTypesStrategies.forEach(springMvcControllerComponentFinderStrategy::addSupportingTypesStrategy);
        components.addAll(springMvcControllerComponentFinderStrategy.findComponents());

        springServiceComponentFinderStrategy = new SpringServiceComponentFinderStrategy();
        springServiceComponentFinderStrategy.setIncludePublicTypesOnly(includePublicTypesOnly);
        springServiceComponentFinderStrategy.setComponentFinder(getComponentFinder());
        supportingTypesStrategies.forEach(springServiceComponentFinderStrategy::addSupportingTypesStrategy);
        components.addAll(springServiceComponentFinderStrategy.findComponents());

        springComponentComponentFinderStrategy = new SpringComponentComponentFinderStrategy();
        springComponentComponentFinderStrategy.setIncludePublicTypesOnly(includePublicTypesOnly);
        springComponentComponentFinderStrategy.setComponentFinder(getComponentFinder());
        supportingTypesStrategies.forEach(springComponentComponentFinderStrategy::addSupportingTypesStrategy);
        components.addAll(springComponentComponentFinderStrategy.findComponents());

        springRepositoryComponentFinderStrategy = new SpringRepositoryComponentFinderStrategy();
        springRepositoryComponentFinderStrategy.setIncludePublicTypesOnly(includePublicTypesOnly);
        springRepositoryComponentFinderStrategy.setComponentFinder(getComponentFinder());
        supportingTypesStrategies.forEach(springRepositoryComponentFinderStrategy::addSupportingTypesStrategy);
        components.addAll(springRepositoryComponentFinderStrategy.findComponents());

        return components;
    }

    @Override
    public void postFindComponents() throws Exception {
        springRestControllerComponentFinderStrategy.postFindComponents();
        springMvcControllerComponentFinderStrategy.postFindComponents();
        springServiceComponentFinderStrategy.postFindComponents();
        springComponentComponentFinderStrategy.postFindComponents();
        springRepositoryComponentFinderStrategy.postFindComponents();
    }

}
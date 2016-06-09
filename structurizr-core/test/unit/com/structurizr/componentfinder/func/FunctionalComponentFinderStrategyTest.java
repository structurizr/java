package com.structurizr.componentfinder.func;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.structurizr.componentfinder.myapp.MyController;
import com.structurizr.componentfinder.myapp.MyRepository;
import com.structurizr.componentfinder.myapp.MyRepositoryImpl;
import com.structurizr.model.Component;
import com.structurizr.model.Container;
import com.structurizr.model.Element;
import com.structurizr.model.Relationship;
import com.structurizr.testapp.paperboy.PaperDeliveryApplicationService;
import com.structurizr.testapp.paperboy.model.*;
import com.structurizr.testapp.paperboy.service.DeliverPapersCommand;
import com.structurizr.testapp.paperboy.service.DeliveryStrategy;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.structurizr.componentfinder.ComponentFinderTestConstants.*;
import static com.structurizr.componentfinder.func.TagValidator.validateTag;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FunctionalComponentFinderStrategyTest {

    private static final String JAVA_INTERFACE = "JAVA_INTERFACE";
    private static final String JAVA_CLASS = "JAVA_CLASS";
    private static final String MYAPP_TAG = "MYAPP";
    private static final String PAPERBOY_TAG = "PAPERBOY";
    private static final String DELIVERY_PAPER_SERVICE = "com.structurizr.testapp.paperboy.DeliverPapersService";
    private Container webApplication;


    @Before
    public void setUp() {
        webApplication = createDefaultContainer();
    }

    @Test
    public void findComponentsInMyApp() throws Exception {
        final FunctionalComponentFinder componentFinder = createScanAllComponentFinder(".*");
        final Collection<Component> components = componentFinder.findComponents(MY_APP_TEST_PACKAGE_TO_SCAN);
        assertThat(components).hasSize(3);
        validateComponentsAreAllContainerComponent(components);

        final Component myController = loadExpectedComponent(MyController.class);
        final Component myRepository = loadExpectedComponent(MyRepository.class);
        final Component myRepositoryImpl = loadExpectedComponent(MyRepositoryImpl.class);

        validateRelations(myController, myRepository, myRepositoryImpl);
        validateRelations(myRepository);
        validateRelations(myRepositoryImpl, myRepository);

        validateTag(myRepository, JAVA_INTERFACE, MYAPP_TAG);
        validateTag(myController, JAVA_CLASS, MYAPP_TAG);
        validateTag(myRepositoryImpl, JAVA_CLASS, MYAPP_TAG);


    }

    @Test
    public void findComponentsInPaperboy() throws Exception {
        final FunctionalComponentFinder componentFinder = createScanAllComponentFinder(".*");
        final Collection<Component> components = componentFinder.findComponents(PAPERBOY_APP_PACKAGE_TO_SCAN);

        assertThat(components).hasSize(19);

        validateComponentsAreAllContainerComponent(components);

        final Component appServiceInterface = loadExpectedComponent(PaperDeliveryApplicationService.class);
        final Component appService = loadExpectedComponent(Class.forName(DELIVERY_PAPER_SERVICE));
        final Component customer = loadExpectedComponent(Customer.class);
        final Component wallet = loadExpectedComponent(Wallet.class);
        final Component address = loadExpectedComponent(Address.class);
        final Component paper = loadExpectedComponent(Paper.class);
        final Component paperBoy = loadExpectedComponent(PaperBoy.class);
        final Component paperBoyRepo = loadExpectedComponent(PaperBoyRepository.class);
        final Component strategy = loadExpectedComponent(DeliveryStrategy.class);
        final Component deliverCommand = loadExpectedComponent(DeliverPapersCommand.class);
        final Component money = loadExpectedComponent(MonetaryAmount.class);

        validateRelations(appServiceInterface);
        validateRelations(appService, appServiceInterface,paperBoyRepo,strategy,deliverCommand);
        validateRelations(customer, wallet, address,paper, paperBoy,money);

        validateTag(appServiceInterface,JAVA_INTERFACE,PAPERBOY_TAG);
        validateTag(appService,JAVA_CLASS,PAPERBOY_TAG);
        validateTag(customer,JAVA_CLASS,PAPERBOY_TAG);
        validateTag(wallet,JAVA_CLASS,PAPERBOY_TAG);
        validateTag(address,JAVA_CLASS,PAPERBOY_TAG);
        validateTag(paper,JAVA_CLASS,PAPERBOY_TAG);
        validateTag(paperBoy,JAVA_CLASS,PAPERBOY_TAG);
        validateTag(paperBoyRepo,JAVA_INTERFACE,PAPERBOY_TAG);
        validateTag(strategy,JAVA_INTERFACE,PAPERBOY_TAG);
        validateTag(deliverCommand,JAVA_CLASS,PAPERBOY_TAG);
        validateTag(money,JAVA_CLASS,PAPERBOY_TAG);


    }


    private void validateComponentsAreAllContainerComponent(Collection<Component> components) {
        final Set<Component> containerComponents = webApplication.getComponents();
        assertThat(containerComponents).hasSameSizeAs(components);
        assertThat(containerComponents).containsAll(components);
    }

    private void validateRelations(Component source, Element... destinationElements) {
        final List<Element> destinations = Lists.newArrayList(destinationElements);
        assertEquals(destinations.size(), source.getRelationships().size());
        final List<Element> elements = source.getRelationships().stream().map(Relationship::getDestination).collect(Collectors.toList());
        assertTrue(elements.containsAll(destinations));
    }

    private Component loadExpectedComponent(Class<?> expectClassComponent) {
        final Component component = webApplication.getComponentWithName(expectClassComponent.getSimpleName());
        validateComponent(expectClassComponent, component);
        return component;
    }

    private void validateComponent(Class<?> expectClassComponent, Component component) {
        assertThat(component).isNotNull();
        assertThat(expectClassComponent.getSimpleName()).isEqualTo(component.getName());
        assertThat(expectClassComponent.getName()).isEqualTo(component.getType());
        assertThat(component.getDescription()).isEmpty();
        assertThat(component.getTechnology()).isEmpty();
    }


    private FunctionalComponentFinder createScanAllComponentFinder(String typeRegex) {
        return FunctionalComponentFinder.builder()
                .addComponentFactory(createComponentFactory(typeRegex))
                .withDirectDependencyScanner()
                .build();
    }

    private ComponentFactory createComponentFactory(String typeRegex) {
        return ComponentFactory.builder()
                .withTypeMatcher(
                        RegexClassNameMatcher.create(typeRegex)
                                .and(InnerClassMatcher.INSTANCE).negate())
                .withFactoryFromTypeForContainer(webApplication)
                .withDecorator(createDecoratorThroughBuilder())
                .build();
    }

    private Consumer<CreatedComponent> createDecoratorThroughBuilder() {
        final Predicate<Class<?>> isInterface = Class::isInterface;
        return TagDecorator.builder()
                .addTag(RegexClassNameMatcher.create(".*\\.paperboy\\..*"), PAPERBOY_TAG)
                .addTag(RegexClassNameMatcher.create(".*\\.myapp\\..*"), MYAPP_TAG)
                .addTag(isInterface, JAVA_INTERFACE)
                .addTag(isInterface.negate(), JAVA_CLASS)
                .build();
    }

    private Consumer<CreatedComponent> createComponentDecorator() {
        return TagDecorator.create(this::buildTags);
    }

    private Collection<String> buildTags(Class<?> aClass) {
        final ImmutableList.Builder<String> builder = ImmutableList.builder();
        if (aClass.getName().contains(".paperboy."))
            builder.add(PAPERBOY_TAG);
        if (aClass.getName().contains(".myapp."))
            builder.add(MYAPP_TAG);
        if (aClass.isInterface())
            builder.add(JAVA_INTERFACE);
        else
            builder.add(JAVA_CLASS);
        return builder.build();
    }


}

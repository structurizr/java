package com.structurizr.componentfinder.func;

import com.structurizr.componentfinder.myapp.MyController;
import com.structurizr.componentfinder.myapp.MyRepository;
import com.structurizr.componentfinder.myapp.MyRepositoryImpl;
import com.structurizr.model.Component;
import com.structurizr.model.Container;
import com.structurizr.testapp.paperboy.PaperDeliveryApplicationService;
import com.structurizr.testapp.paperboy.model.*;
import com.structurizr.testapp.paperboy.service.DeliverPapersCommand;
import com.structurizr.testapp.paperboy.service.DeliveryStrategy;

import java.util.Collection;

import static com.structurizr.componentfinder.ComponentFinderTestConstants.*;
import static com.structurizr.componentfinder.func.TagValidator.validateTag;
import static org.assertj.core.api.Assertions.assertThat;

class DirectDependenciesValidator extends DependenciesValidator {
    private static final String DELIVERY_PAPER_SERVICE = "com.structurizr.testapp.paperboy.DeliverPapersService";


    public DirectDependenciesValidator(Container container) {
        super(container);
    }

    public void validatePaperBoyComponentDependencies(Collection<Component> components) throws Exception {
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
        validateRelations(appService, appServiceInterface, paperBoyRepo, strategy, deliverCommand);
        validateRelations(customer, wallet, address, paper, paperBoy, money);

        validateTag(appServiceInterface, JAVA_INTERFACE, PAPERBOY_TAG);
        validateTag(appService, JAVA_CLASS, PAPERBOY_TAG);
        validateTag(customer, JAVA_CLASS, PAPERBOY_TAG);
        validateTag(wallet, JAVA_CLASS, PAPERBOY_TAG);
        validateTag(address, JAVA_CLASS, PAPERBOY_TAG);
        validateTag(paper, JAVA_CLASS, PAPERBOY_TAG);
        validateTag(paperBoy, JAVA_CLASS, PAPERBOY_TAG);
        validateTag(paperBoyRepo, JAVA_INTERFACE, PAPERBOY_TAG);
        validateTag(strategy, JAVA_INTERFACE, PAPERBOY_TAG);
        validateTag(deliverCommand, JAVA_CLASS, PAPERBOY_TAG);
        validateTag(money, JAVA_CLASS, PAPERBOY_TAG);
    }

    public void validateMyAppComponentDependencies(Collection<Component> components) {
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
}

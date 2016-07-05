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

import static com.structurizr.componentfinder.TestConstants.*;
import static com.structurizr.componentfinder.func.TagValidator.validateTags;
import static org.assertj.core.api.Assertions.assertThat;

class StructurizrDependenciesValidator extends DependenciesValidator {
    private static final String DELIVERY_PAPER_SERVICE = "com.structurizr.testapp.paperboy.DeliverPapersService";


    public StructurizrDependenciesValidator(Container container) {
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

        //Interfaces have a dependency on their implementation and on all the dependencies of the first implementation
        validateRelations(appServiceInterface, appService, paperBoyRepo, strategy, deliverCommand);
        validateRelations(appService, appServiceInterface, paperBoyRepo, strategy, deliverCommand);
        validateRelations(customer, wallet, address, paper, paperBoy, money);

        validateTags(appServiceInterface, JAVA_INTERFACE, PAPERBOY_TAG);
        validateTags(appService, JAVA_CLASS, PAPERBOY_TAG);
        validateTags(customer, JAVA_CLASS, PAPERBOY_TAG);
        validateTags(wallet, JAVA_CLASS, PAPERBOY_TAG);
        validateTags(address, JAVA_CLASS, PAPERBOY_TAG);
        validateTags(paper, JAVA_CLASS, PAPERBOY_TAG);
        validateTags(paperBoy, JAVA_CLASS, PAPERBOY_TAG);
        validateTags(paperBoyRepo, JAVA_INTERFACE, PAPERBOY_TAG);
        validateTags(strategy, JAVA_INTERFACE, PAPERBOY_TAG);
        validateTags(deliverCommand, JAVA_CLASS, PAPERBOY_TAG);
        validateTags(money, JAVA_CLASS, PAPERBOY_TAG);
    }

    public void validateMyAppComponentDependencies(Collection<Component> components) {
        assertThat(components).hasSize(3);
        validateComponentsAreAllContainerComponent(components);

        final Component myController = loadExpectedComponent(MyController.class);
        final Component myRepository = loadExpectedComponent(MyRepository.class);
        final Component myRepositoryImpl = loadExpectedComponent(MyRepositoryImpl.class);

        validateRelations(myController, myRepository, myRepositoryImpl);
        validateRelations(myRepository, myRepositoryImpl);//Interfaces have a dependency on their implementation
        validateRelations(myRepositoryImpl, myRepository);

        validateTags(myRepository, JAVA_INTERFACE, MYAPP_TAG);
        validateTags(myController, JAVA_CLASS, MYAPP_TAG);
        validateTags(myRepositoryImpl, JAVA_CLASS, MYAPP_TAG);
    }
}

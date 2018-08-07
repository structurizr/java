package com.structurizr.example;

import com.structurizr.Workspace;
import com.structurizr.api.StructurizrClient;
import com.structurizr.model.*;
import com.structurizr.view.*;

/**
 * A simple example of what a microservices architecture might look like. This workspace also
 * includes a dynamic view that demonstrates parallel sequences of events.
 *
 * The live version of the diagrams can be found at https://structurizr.com/public/4241
 */
public class MicroservicesExample {

    private static final String MICROSERVICE_TAG = "Microservice";
    private static final String MESSAGE_BUS_TAG = "Message Bus";
    private static final String DATASTORE_TAG = "Database";

    public static void main(String[] args) throws Exception {
        Workspace workspace = new Workspace("Microservices example", "An example of a microservices architecture, which includes asynchronous and parallel behaviour.");
        Model model = workspace.getModel();

        SoftwareSystem mySoftwareSystem = model.addSoftwareSystem("Customer Information System", "Stores information ");
        Person customer = model.addPerson("Customer", "A customer");
        Container customerApplication = mySoftwareSystem.addContainer("Customer Application", "Allows customers to manage their profile.", "Angular");

        Container customerService = mySoftwareSystem.addContainer("Customer Service", "The point of access for customer information.", "Java and Spring Boot");
        customerService.addTags(MICROSERVICE_TAG);
        Container customerDatabase = mySoftwareSystem.addContainer("Customer Database", "Stores customer information.", "Oracle 12c");
        customerDatabase.addTags(DATASTORE_TAG);

        Container reportingService = mySoftwareSystem.addContainer("Reporting Service", "Creates normalised data for reporting purposes.", "Ruby");
        reportingService.addTags(MICROSERVICE_TAG);
        Container reportingDatabase = mySoftwareSystem.addContainer("Reporting Database", "Stores a normalised version of all business data for ad hoc reporting purposes.", "MySQL");
        reportingDatabase.addTags(DATASTORE_TAG);

        Container auditService = mySoftwareSystem.addContainer("Audit Service", "Provides organisation-wide auditing facilities.", "C# .NET");
        auditService.addTags(MICROSERVICE_TAG);
        Container auditStore = mySoftwareSystem.addContainer("Audit Store", "Stores information about events that have happened.", "Event Store");
        auditStore.addTags(DATASTORE_TAG);

        Container messageBus = mySoftwareSystem.addContainer("Message Bus", "Transport for business events.", "RabbitMQ");
        messageBus.addTags(MESSAGE_BUS_TAG);

        customer.uses(customerApplication, "Uses");
        customerApplication.uses(customerService, "Updates customer information using", "JSON/HTTPS", InteractionStyle.Synchronous);
        customerService.uses(messageBus, "Sends customer update events to", "", InteractionStyle.Asynchronous);
        customerService.uses(customerDatabase, "Stores data in", "JDBC", InteractionStyle.Synchronous);
        customerService.uses(customerApplication, "Sends events to", "WebSocket", InteractionStyle.Asynchronous);
        messageBus.uses(reportingService, "Sends customer update events to", "", InteractionStyle.Asynchronous);
        messageBus.uses(auditService, "Sends customer update events to", "", InteractionStyle.Asynchronous);
        reportingService.uses(reportingDatabase, "Stores data in", "", InteractionStyle.Synchronous);
        auditService.uses(auditStore, "Stores events in", "", InteractionStyle.Synchronous);

        ViewSet views = workspace.getViews();

        ContainerView containerView = views.createContainerView(mySoftwareSystem, "Containers", null);
        containerView.addAllElements();

        DynamicView dynamicView = views.createDynamicView(mySoftwareSystem, "CustomerUpdateEvent", "This diagram shows what happens when a customer updates their details.");
        dynamicView.add(customer, customerApplication);
        dynamicView.add(customerApplication, customerService);

        dynamicView.add(customerService, customerDatabase);
        dynamicView.add(customerService, messageBus);

        dynamicView.startParallelSequence();
        dynamicView.add(messageBus, reportingService);
        dynamicView.add(reportingService, reportingDatabase);
        dynamicView.endParallelSequence();

        dynamicView.startParallelSequence();
        dynamicView.add(messageBus, auditService);
        dynamicView.add(auditService, auditStore);
        dynamicView.endParallelSequence();

        dynamicView.startParallelSequence();
        dynamicView.add(customerService, "Confirms update to", customerApplication);
        dynamicView.endParallelSequence();

        Styles styles = views.getConfiguration().getStyles();
        styles.addElementStyle(Tags.ELEMENT).color("#000000");
        styles.addElementStyle(Tags.PERSON).background("#ffbf00").shape(Shape.Person);
        styles.addElementStyle(Tags.CONTAINER).background("#facc2E");
        styles.addElementStyle(MESSAGE_BUS_TAG).width(1600).shape(Shape.Pipe);
        styles.addElementStyle(MICROSERVICE_TAG).shape(Shape.Hexagon);
        styles.addElementStyle(DATASTORE_TAG).background("#f5da81").shape(Shape.Cylinder);
        styles.addRelationshipStyle(Tags.RELATIONSHIP).routing(Routing.Orthogonal);

        styles.addRelationshipStyle(Tags.ASYNCHRONOUS).dashed(true);
        styles.addRelationshipStyle(Tags.SYNCHRONOUS).dashed(false);

        StructurizrClient client = new StructurizrClient("key", "secret");
        client.putWorkspace(4241, workspace);
    }

}

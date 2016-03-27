package com.structurizr.example.core;

import com.structurizr.Workspace;
import com.structurizr.api.StructurizrClient;
import com.structurizr.model.*;
import com.structurizr.view.ContainerView;
import com.structurizr.view.DynamicView;
import com.structurizr.view.Shape;
import com.structurizr.view.ViewSet;

public class MessageBusExample {

    private static final String MICROSERVICE_TAG = "Microservice";
    private static final String MESSAGE_BUS_TAG = "Message Bus";
    private static final String DATABASE_TAG = "Database";
    private static final String EVENT_STORE_TAG = "Event Store";

    public static void main(String[] args) throws Exception {
        Workspace workspace = new Workspace("Asynchronous example", "An example of an asynchronous system");
        Model model = workspace.getModel();

        SoftwareSystem mySoftwareSystem = model.addSoftwareSystem("My software system", "A description of my software system.");

        Container microserviceA = mySoftwareSystem.addContainer("Microservice A", "", "Java and Spring Boot");
        microserviceA.addTags(MICROSERVICE_TAG);
        Container microserviceB = mySoftwareSystem.addContainer("Microservice B", "", "Ruby");
        microserviceB.addTags(MICROSERVICE_TAG);
        Container microserviceC = mySoftwareSystem.addContainer("Microservice C", "", "C# .NET");
        microserviceC.addTags(MICROSERVICE_TAG);

        Container messageBus = mySoftwareSystem.addContainer("Message Bus", "", "RabbitMQ");
        messageBus.addTags(MESSAGE_BUS_TAG);

        Container databaseB = mySoftwareSystem.addContainer("Database B", "", "MySQL");
        databaseB.addTags(DATABASE_TAG);
        Container eventStoreC = mySoftwareSystem.addContainer("Event Store C", "", "Event Store");
        eventStoreC.addTags(EVENT_STORE_TAG);

        microserviceA.uses(messageBus, "Sends business event X to", "", InteractionStyle.Asynchronous);
        messageBus.uses(microserviceB, "Sends business event X  to", "", InteractionStyle.Asynchronous);
        messageBus.uses(microserviceC, "Sends business event X  to", "", InteractionStyle.Asynchronous);
        microserviceB.uses(databaseB, "Stores data in", "", InteractionStyle.Synchronous);
        microserviceB.uses(messageBus, "Sends confirmations and events to", "", InteractionStyle.Asynchronous);
        microserviceC.uses(eventStoreC, "Stores event in", "", InteractionStyle.Synchronous);
        microserviceC.uses(messageBus, "Sends confirmation to", "", InteractionStyle.Asynchronous);
        messageBus.uses(microserviceA, "Sends confirmation to", "", InteractionStyle.Asynchronous);

        ViewSet views = workspace.getViews();

        ContainerView containerView = views.createContainerView(mySoftwareSystem);
        containerView.addAllElements();

        DynamicView dynamicView = views.createDynamicView(mySoftwareSystem, "Business event X");
        dynamicView.add(microserviceA, messageBus);
        dynamicView.startChildSequence();
        dynamicView.add(messageBus, microserviceB);
        dynamicView.add(microserviceB, databaseB);
        dynamicView.add(microserviceB, "Sends business event Y to", messageBus);
        dynamicView.add(microserviceB, "Sends confirmation to", messageBus);
        dynamicView.add(messageBus, microserviceA);
        dynamicView.endChildSequence();
        dynamicView.startChildSequence();
        dynamicView.add(messageBus, microserviceC);
        dynamicView.add(microserviceC, eventStoreC);
        dynamicView.add(microserviceC, messageBus);
        dynamicView.add(messageBus, microserviceA);
        dynamicView.endChildSequence();

        views.getConfiguration().getStyles().addElementStyle(MESSAGE_BUS_TAG).width(1600).background("#ffbf00").color("#000000").shape(Shape.RoundedBox);
        views.getConfiguration().getStyles().addElementStyle(MICROSERVICE_TAG).background("#facc2E").color("#000000").shape(Shape.Ellipse);
        views.getConfiguration().getStyles().addElementStyle(DATABASE_TAG).background("#f5da81").color("#000000").shape(Shape.Cylinder);
        views.getConfiguration().getStyles().addElementStyle(EVENT_STORE_TAG).background("#f5da81").color("#000000").shape(Shape.Cylinder);

        views.getConfiguration().getStyles().addRelationshipStyle(Tags.ASYNCHRONOUS).dashed(true);
        views.getConfiguration().getStyles().addRelationshipStyle(Tags.SYNCHRONOUS).dashed(false);

        StructurizrClient client = new StructurizrClient("key", "secret");
        client.mergeWorkspace(4241, workspace);
    }

}

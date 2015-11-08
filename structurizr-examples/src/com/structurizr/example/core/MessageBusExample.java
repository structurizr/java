package com.structurizr.example.core;

import com.structurizr.Workspace;
import com.structurizr.api.StructurizrClient;
import com.structurizr.io.json.JsonWriter;
import com.structurizr.model.*;
import com.structurizr.view.*;

import java.io.StringWriter;

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

        views.getConfiguration().getStyles().add(new ElementStyle(Tags.ELEMENT, null, null, null, null, null, Shape.RoundedBox));
        views.getConfiguration().getStyles().add(new ElementStyle(MESSAGE_BUS_TAG, 1600, null, "#FFBF00", "#000000", null));
        views.getConfiguration().getStyles().add(new ElementStyle(MICROSERVICE_TAG, null, null, "#FACC2E", "#000000", null, null));
        views.getConfiguration().getStyles().add(new ElementStyle(DATABASE_TAG, null, null, "#F5DA81", "#000000", null, Shape.Cylinder));
        views.getConfiguration().getStyles().add(new ElementStyle(EVENT_STORE_TAG, null, null, "#F5DA81", "#000000", null, Shape.Cylinder));

        views.getConfiguration().getStyles().add(new RelationshipStyle(Tags.ASYNCHRONOUS, null, null, true, null, null, null, null));
        views.getConfiguration().getStyles().add(new RelationshipStyle(Tags.SYNCHRONOUS, null, null, false, null, null, null, null));

        JsonWriter jsonWriter = new JsonWriter(true);
        StringWriter stringWriter = new StringWriter();
        jsonWriter.write(workspace, stringWriter);
        System.out.println(stringWriter.toString());

        StructurizrClient client = new StructurizrClient("https://api.structurizr.com", "key", "secret");
        client.mergeWorkspace(4241, workspace);
    }

}

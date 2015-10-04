package com.structurizr.io.json;

import com.structurizr.Workspace;
import com.structurizr.model.*;
import com.structurizr.view.*;
import org.junit.Test;

import java.io.StringReader;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

public class JsonTests {

    @Test
    public void test_write_and_read() throws Exception {
        Workspace workspace1 = createWorkspace();

        // output the model as JSON
        JsonWriter jsonWriter = new JsonWriter(true);
        StringWriter stringWriter = new StringWriter();
        jsonWriter.write(workspace1, stringWriter);

        JsonReader jsonReader = new JsonReader();
        StringReader stringReader = new StringReader(stringWriter.toString());
        Workspace workspace2 = jsonReader.read(stringReader);
    }

    protected Workspace createWorkspace() {
        Workspace workspace = new Workspace("My workspace", "Description");
        Model model = workspace.getModel();

        SoftwareSystem mySoftwareSystem = model.addSoftwareSystem(Location.Internal, "My Software System", "Description");
        mySoftwareSystem.addTags("Internal");

        Person person = model.addPerson(Location.External, "A User", "Description");
        person.addTags("External");
        person.uses(mySoftwareSystem, "Uses");

        Container webApplication = mySoftwareSystem.addContainer("Web Application", "Description", "Apache Tomcat");
        Container database = mySoftwareSystem.addContainer("Database", "Description", "MySQL");
        person.uses(webApplication, "Uses");
        Relationship webApplicationToDatabase = webApplication.uses(database, "Reads from and writes to");
        webApplicationToDatabase.addTags("JDBC");

        Component componentA = webApplication.addComponent("ComponentA", "Description", "Technology A");
        Component componentB = webApplication.addComponentOfType("com.somecompany.system.ComponentB", "com.somecompany.system.ComponentBImpl", "Description", "Technology B");
        person.uses(componentA, "Uses");
        componentA.uses(componentB, "Uses");
        componentB.uses(database, "Reads from and writes to");

        ViewSet views = workspace.getViews();
        SystemContextView systemContextView = views.createContextView(mySoftwareSystem);
        systemContextView.addAllSoftwareSystems();
        systemContextView.addAllPeople();

        ContainerView containerView = views.createContainerView(mySoftwareSystem);
        containerView.addAllSoftwareSystems();
        containerView.addAllPeople();
        containerView.addAllContainers();

        ComponentView componentView = views.createComponentView(webApplication);
        componentView.addAllSoftwareSystems();
        componentView.addAllPeople();
        componentView.addAllContainers();
        componentView.addAllComponents();

        views.getConfiguration().getStyles().add(new ElementStyle(Tags.ELEMENT, 600, 450, "#dddddd", "#000000", 30));
        views.getConfiguration().getStyles().add(new ElementStyle("Internal", null, null, "#041F37", "#ffffff", null));
        views.getConfiguration().getStyles().add(new RelationshipStyle(Tags.RELATIONSHIP, 4, "#dddddd", true, null, 25, 300, null));
        views.getConfiguration().getStyles().add(new RelationshipStyle("JDBC", 4, "#ff0000", true, null, 25, 300, null));

        return workspace;
    }

}

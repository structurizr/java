package com.structurizr.model;

import com.structurizr.Workspace;
import com.structurizr.io.WorkspaceReaderException;
import com.structurizr.io.WorkspaceWriterException;
import com.structurizr.io.json.JsonReader;
import com.structurizr.io.json.JsonWriter;
import com.structurizr.view.ComponentView;
import com.structurizr.view.ContainerView;
import com.structurizr.view.ElementStyle;
import com.structurizr.view.RelationshipStyle;
import com.structurizr.view.Routing;
import com.structurizr.view.SystemContextView;
import com.structurizr.view.ViewSet;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

import java.io.StringReader;
import java.io.StringWriter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

public class MessageDigestIdGeneratorTests {

    @Test
    public void createWorkspaceWithMD5IdsHaveStableIds() {
        final MessageDigestIdGenerator ids = MessageDigestIdGenerator.getInstance("MD5");
        final String webappId = "4091a0564c0058b74bdd067373d02578";
        final String databaseId = "7f51a432eb63bedebd49fe41a41f9767";
        final String relationshipId = "c0a5a5ffcd3e87aba4f96e31bd90bef6";

        createWorkspaceWithExpectedIds(ids, webappId, databaseId, relationshipId);
    }

    @Test
    public void createWorkspaceWithSHA1IdsHaveStableIds() {
        final MessageDigestIdGenerator ids = MessageDigestIdGenerator.getInstance("SHA-1");
        final String webappId = "26f55141b08fa3e27467d2dd6ee0c313de7d82ff";
        final String databaseId = "f056f580ae4ffc36261086037f6ef7d5a735ef48";
        final String relationshipId = "63a3048b98311af9b25610284c21afedcb38dd3d";
        createWorkspaceWithExpectedIds(ids, webappId, databaseId, relationshipId);
    }

    @Test
    public void createWorkspaceWithShortSHA1IdsHaveStableIds() {
        final MessageDigestIdGenerator ids = MessageDigestIdGenerator.getInstance("SHA-1", 7);
        final String webappId = "26f5514";
        final String databaseId = "f056f58";
        final String relationshipId = "63a3048";
        createWorkspaceWithExpectedIds(ids, webappId, databaseId, relationshipId);
    }

    @Test
    public void writeAndRead() throws WorkspaceWriterException, WorkspaceReaderException {
        final MessageDigestIdGenerator ids = MessageDigestIdGenerator.getInstance("SHA-512");

        final String specificId = "a5341244d7a6fa9cde9edb2794587877704edeae985ca56bd9b3c71404ecb37d148a61eb69be4c113f516f04f5ecff7f936487880277b03d79e8689daae3c262";

        Workspace workspace1 = createWorkspace(ids);

        // output the model as JSON
        JsonWriter jsonWriter = new JsonWriter(true);
        StringWriter stringWriter = new StringWriter();
        jsonWriter.write(workspace1, stringWriter);

        final String json = stringWriter.toString();
        assertThat(json, CoreMatchers.containsString(specificId));

        JsonReader jsonReader = new JsonReader();
        StringReader stringReader = new StringReader(json);
        Workspace workspace2 = jsonReader.read(stringReader);

        assertEquals(
                "/My Software System/Web Application/ComponentB",
                workspace2.getModel().getElement(specificId).getCanonicalName());
    }

    private void createWorkspaceWithExpectedIds(MessageDigestIdGenerator ids, String webappId, String databaseId, String relationshipId) {
        final Workspace ws = createWorkspace(ids);

        final Element webapp = ws.getModel().getElement(webappId);
        final Element database = ws.getModel().getElement(databaseId);
        final Relationship relationship = ws.getModel().getRelationship(relationshipId);

        assertNotNull(webapp);
        assertNotNull(database);
        assertNotNull(relationship);
        assertEquals("Web Application", webapp.getName());
        assertEquals("Database", database.getName());
        assertEquals("Reads from and writes to", relationship.getDescription());
        assertSame(webapp, relationship.getSource());
        assertSame(database, relationship.getDestination());
    }

    private Workspace createWorkspace(IdGenerator ids) {
        Workspace workspace = new Workspace("My workspace", "Description");
        Model model = workspace.getModel();
        model.setIdGenerator(ids);

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
        Component componentB = webApplication.addComponentAndCode(
                "ComponentB",
                "com.somecompany.system.ComponentBImpl",
                "com.somecompany.system",
                "Description", "Technology B");
        person.uses(componentA, "Uses");
        componentA.uses(componentB, "Uses");
        componentB.uses(database, "Reads from and writes to");

        ViewSet views = workspace.getViews();
        SystemContextView systemContextView = views.createSystemContextView(mySoftwareSystem, "context", "Description");
        systemContextView.addAllSoftwareSystems();
        systemContextView.addAllPeople();

        ContainerView containerView = views.createContainerView(mySoftwareSystem, "containers", "Description");
        containerView.addAllSoftwareSystems();
        containerView.addAllPeople();
        containerView.addAllContainers();

        ComponentView componentView = views.createComponentView(webApplication, "components", "Description");
        componentView.addAllSoftwareSystems();
        componentView.addAllPeople();
        componentView.addAllContainers();
        componentView.addAllComponents();

        views.getConfiguration().getStyles().add(new ElementStyle(Tags.ELEMENT, 600, 450, "#dddddd", "#000000", 30));
        views.getConfiguration().getStyles().add(new ElementStyle("Internal", null, null, "#041F37", "#ffffff", null));
        views.getConfiguration().getStyles().add(new RelationshipStyle(Tags.RELATIONSHIP, 4, "#dddddd", true, Routing.Direct, 25, 300, null));
        views.getConfiguration().getStyles().add(new RelationshipStyle("JDBC", 4, "#ff0000", true, Routing.Direct, 25, 300, null));

        return workspace;
    }

}

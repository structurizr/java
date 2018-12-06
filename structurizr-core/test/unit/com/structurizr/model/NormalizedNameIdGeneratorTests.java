package com.structurizr.model;

import com.structurizr.Workspace;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class NormalizedNameIdGeneratorTests {

    private Model model;

    @Before
    public void setUp() {
        Workspace workspace = new Workspace("My workspace", "Description");
        model = workspace.getModel();
    }

    @Test
    public void test_generateId_WhenUsingCanonicalName() {
        model.setIdGenerator(new NormalizedNameIdGenerator(true));

        SoftwareSystem mySoftwareSystem = model.addSoftwareSystem("My Software System", "Description");

        assertEquals("AUser", model.addPerson("A User", "Description").getId());

        Container webApplication = mySoftwareSystem.addContainer("Web-Application", "Description", "Technology"); // Dash to be removed
        assertEquals("MySoftwareSystemWebApplication", webApplication.getId());

        Container database = mySoftwareSystem.addContainer("Database", "Description", "Technology");
        assertEquals("MySoftwareSystemWebApplicationReadsfromandwritestoMySoftwareSystemDatabase", 
            webApplication.uses(database, "Reads from and writes to").getId());

        DeploymentNode deploymentNode = model.addDeploymentNode("Deployment Node", "Description", "Technology");
        assertEquals("MySoftwareSystemWebApplication1", deploymentNode.add(webApplication).getId());

        assertEquals("MySoftwareSystemWebApplicationComponentA", 
            webApplication.addComponent("ComponentA", "Description", "Technology A").getId());

        DeploymentNode awsVm = model.addDeploymentNode("VM @ AWS", "Description", "Technology");
        assertEquals("DeploymentDefaultVMAWS", awsVm.getId());
        assertEquals("DeploymentDefaultVMAWSJBossAS711", awsVm.addDeploymentNode("JBoss AS 7.1.1", "Description", "Technology").getId());
    }
    
    @Test
    public void test_generateId_WhenUsingName() {
        model.setIdGenerator(new NormalizedNameIdGenerator(false));

        SoftwareSystem mySoftwareSystem = model.addSoftwareSystem("My Software System", "Description");

        assertEquals("AUser", model.addPerson("A User", "Description").getId());

        Container webApplication = mySoftwareSystem.addContainer("Web-Application", "Description", "Technology"); // Dash to be removed
        assertEquals("WebApplication", webApplication.getId());
        Container database = mySoftwareSystem.addContainer("Database", "Description", "Technology");
        assertEquals("WebApplicationReadsfromandwritestoDatabase", 
            webApplication.uses(database, "Reads from and writes to").getId());

        DeploymentNode deploymentNode = model.addDeploymentNode("Deployment Node", "Description", "Technology");
        assertEquals("WebApplication1", deploymentNode.add(webApplication).getId());

        assertEquals("ComponentA", 
            webApplication.addComponent("ComponentA", "Description", "Technology A").getId());

        DeploymentNode awsVm = model.addDeploymentNode("VM @ AWS", "Description", "Technology");
        assertEquals("VMAWS", awsVm.getId());
        assertEquals("JBossAS711", awsVm.addDeploymentNode("JBoss AS 7.1.1", "Description", "Technology").getId());
    }
    
    @Test
    public void test_generateId_ThrowsAnException_WhenElementCanonicalNamesAreNotUnique() {
        model.setIdGenerator(new NormalizedNameIdGenerator(true));

        SoftwareSystem mySoftwareSystem = model.addSoftwareSystem("My Software System", "Description");
        mySoftwareSystem.addContainer("My-Container 1.1", "Description", "Technology");

        try {
            mySoftwareSystem.addContainer("My Container/1,1", "Description", "Technology");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("Non-unique ID generated: MySoftwareSystemMyContainer11", iae.getMessage());
        }
    }

    @Test
    public void test_generateId_ThrowsAnException_WhenElementNamesAreNotUnique() {
        model.setIdGenerator(new NormalizedNameIdGenerator(false));

        SoftwareSystem mySoftwareSystem = model.addSoftwareSystem("My Software System", "Description");
        mySoftwareSystem.addContainer("ContainerA", "Description", "Technology")
            .addComponent("Component A", "Description");

        try {
            mySoftwareSystem.addContainer("ContainerB", "Description", "Technology")
                .addComponent("Component-A", "Description");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("Non-unique ID generated: ComponentA", iae.getMessage());
        }
    }
}
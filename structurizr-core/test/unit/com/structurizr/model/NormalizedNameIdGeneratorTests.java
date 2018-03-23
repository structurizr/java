package com.structurizr.model;

import com.structurizr.Workspace;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NormalizedNameIdGeneratorTests {

    @Test
    public void canonicalName() {
        Workspace workspace = new Workspace("My workspace", "Description");
        Model model = workspace.getModel();
        model.setIdGenerator(new NormalizedNameIdGenerator(true));

        SoftwareSystem mySoftwareSystem = model.addSoftwareSystem("My Software System", "Description");

        assertEquals("AUser", model.addPerson("A User", "Description").getId());

        Container webApplication = mySoftwareSystem.addContainer("Web-Application", "Description", "Technology"); // Dash to be removed
        assertEquals("MySoftwareSystemWebApplication", webApplication.getId());
        Container database = mySoftwareSystem.addContainer("Database", "Description", "Technology");
        assertEquals("MySoftwareSystemWebApplicationReadsfromandwritestoMySoftwareSystemDatabase", 
            webApplication.uses(database, "Reads from and writes to").getId());

        assertEquals("MySoftwareSystemWebApplication1", model.addContainerInstance(webApplication).getId());

        assertEquals("MySoftwareSystemWebApplicationComponentA", 
            webApplication.addComponent("ComponentA", "Description", "Technology A").getId());

        final DeploymentNode awsVm = model.addDeploymentNode("VM @ AWS", "Description", "Technology");
        assertEquals("VMAWS", awsVm.getId());
        assertEquals("VMAWSJBossAS711", awsVm.addDeploymentNode("JBoss AS 7.1.1", "Description", "Technology").getId());
    }
    
    /** Use the non-canonical name */
    @Test
    public void name() {
        Workspace workspace = new Workspace("My workspace", "Description");
        Model model = workspace.getModel();
        model.setIdGenerator(new NormalizedNameIdGenerator(false));

        SoftwareSystem mySoftwareSystem = model.addSoftwareSystem("My Software System", "Description");

        assertEquals("AUser", model.addPerson("A User", "Description").getId());

        Container webApplication = mySoftwareSystem.addContainer("Web-Application", "Description", "Technology"); // Dash to be removed
        assertEquals("WebApplication", webApplication.getId());
        Container database = mySoftwareSystem.addContainer("Database", "Description", "Technology");
        assertEquals("WebApplicationReadsfromandwritestoDatabase", 
            webApplication.uses(database, "Reads from and writes to").getId());

        assertEquals("WebApplication1", model.addContainerInstance(webApplication).getId());

        assertEquals("ComponentA", 
            webApplication.addComponent("ComponentA", "Description", "Technology A").getId());

        final DeploymentNode awsVm = model.addDeploymentNode("VM @ AWS", "Description", "Technology");
        assertEquals("VMAWS", awsVm.getId());
        assertEquals("JBossAS711", awsVm.addDeploymentNode("JBoss AS 7.1.1", "Description", "Technology").getId());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void canonicalNameClash() {
        Workspace workspace = new Workspace("My workspace", "Description");
        Model model = workspace.getModel();
        model.setIdGenerator(new NormalizedNameIdGenerator());

        SoftwareSystem mySoftwareSystem = model.addSoftwareSystem("My Software System", "Description");
        mySoftwareSystem.addContainer("My-Container 1.1", "Description", "Technology");
        mySoftwareSystem.addContainer("My Container/1,1", "Description", "Technology");
    }

    /** Component names clash since they are not canonical */
    @Test(expected = IllegalArgumentException.class)
    public void nonCanonicalNameClash() {
        Workspace workspace = new Workspace("My workspace", "Description");
        Model model = workspace.getModel();
        model.setIdGenerator(new NormalizedNameIdGenerator(false));

        SoftwareSystem mySoftwareSystem = model.addSoftwareSystem("My Software System", "Description");
        mySoftwareSystem.addContainer("ContainerA", "Description", "Technology")
            .addComponent("Component A", "Description");
        mySoftwareSystem.addContainer("ContainerB", "Description", "Technology")
            .addComponent("Component-A", "Description");
    }
}
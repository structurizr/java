package com.structurizr.view;

import com.structurizr.AbstractWorkspaceTestBase;
import com.structurizr.Workspace;
import com.structurizr.model.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DeploymentViewTests extends AbstractWorkspaceTestBase {

    private DeploymentView deploymentView;

    @Before
    public void setup() {
    }

    @Test
    public void test_getName_WithNoSoftwareSystemAndNoEnvironment() {
        deploymentView = views.createDeploymentView("deployment", "Description");
        assertEquals("Deployment", deploymentView.getName());
    }

    @Test
    public void test_getName_WithNoSoftwareSystemAndAnEnvironment() {
        deploymentView = views.createDeploymentView("deployment", "Description");
        deploymentView.setEnvironment("Live");
        assertEquals("Deployment - Live", deploymentView.getName());
    }

    @Test
    public void test_getName_WithASoftwareSystemAndNoEnvironment() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "");
        deploymentView = views.createDeploymentView(softwareSystem, "deployment", "Description");
        assertEquals("Software System - Deployment", deploymentView.getName());
    }

    @Test
    public void test_getName_WithASoftwareSystemAndAnEnvironment() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "");
        deploymentView = views.createDeploymentView(softwareSystem, "deployment", "Description");
        deploymentView.setEnvironment("Live");
        assertEquals("Software System - Deployment - Live", deploymentView.getName());
    }

}
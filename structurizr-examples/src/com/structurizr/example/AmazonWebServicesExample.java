package com.structurizr.example;

import com.structurizr.Workspace;
import com.structurizr.api.StructurizrClient;
import com.structurizr.model.*;
import com.structurizr.view.*;

/**
 * An example of how to document an AWS deployment.
 *
 * The live workspace is available to view at https://structurizr.com/share/54915
 */
public class AmazonWebServicesExample {

    private static final long WORKSPACE_ID = 54915;
    private static final String API_KEY = "";
    private static final String API_SECRET = "";

    private static final String SPRING_BOOT_TAG = "Spring Boot Application";
    private static final String DATABASE_TAG = "Database";

    public static void main(String[] args) throws Exception {
        Workspace workspace = new Workspace("Amazon Web Services Example", "An example AWS deployment architecture.");
        Model model = workspace.getModel();

        SoftwareSystem softwareSystem = model.addSoftwareSystem("Spring PetClinic", "Allows employees to view and manage information regarding the veterinarians, the clients, and their pets.");
        Container webApplication = softwareSystem.addContainer("Web Application", "Allows employees to view and manage information regarding the veterinarians, the clients, and their pets.", "Java and Spring Boot");
        webApplication.addTags(SPRING_BOOT_TAG);
        Container database = softwareSystem.addContainer("Database", "Stores information regarding the veterinarians, the clients, and their pets.", "Relational database schema");
        database.addTags(DATABASE_TAG);

        webApplication.uses(database, "Reads from and writes to", "JDBC/SSL");

        DeploymentNode amazonWebServices = model.addDeploymentNode("Amazon Web Services");
        amazonWebServices.addTags("Amazon Web Services - Cloud");
        DeploymentNode amazonRegion = amazonWebServices.addDeploymentNode("US-East-1");
        amazonRegion.addTags("Amazon Web Services - Region");
        DeploymentNode ec2 = amazonRegion.addDeploymentNode("Amazon EC2");
        ec2.setInstances(10);
        ec2.addTags("Amazon Web Services - EC2");
        ContainerInstance webApplicationInstance = ec2.add(webApplication);

        InfrastructureNode route53 = amazonRegion.addInfrastructureNode("Route 53");
        route53.addTags("Amazon Web Services - Route 53");

        InfrastructureNode elb = amazonRegion.addInfrastructureNode("Elastic Load Balancer");
        elb.addTags("Amazon Web Services - Elastic Load Balancing");

        route53.uses(elb, "Forwards requests to", "HTTPS");
        elb.uses(webApplicationInstance, "Forwards requests to", "HTTPS");

        DeploymentNode rds = amazonRegion.addDeploymentNode("Amazon RDS");
        rds.addTags("Amazon Web Services - RDS");
        DeploymentNode mySql = rds.addDeploymentNode("MySQL");
        mySql.addTags("Amazon Web Services - RDS_MySQL_instance");
        ContainerInstance databaseInstance = mySql.add(database);

        ViewSet views = workspace.getViews();
        DeploymentView deploymentView = views.createDeploymentView(softwareSystem, "AmazonWebServicesDeployment", "An example deployment diagram.");
        deploymentView.addAllDeploymentNodes();

        deploymentView.addAnimation(route53);
        deploymentView.addAnimation(elb);
        deploymentView.addAnimation(webApplicationInstance);
        deploymentView.addAnimation(databaseInstance);

        Styles styles = views.getConfiguration().getStyles();
        styles.addElementStyle(SPRING_BOOT_TAG).shape(Shape.RoundedBox).background("#ffffff");
        styles.addElementStyle(DATABASE_TAG).shape(Shape.Cylinder).background("#ffffff");
        styles.addElementStyle(Tags.INFRASTRUCTURE_NODE).shape(Shape.RoundedBox).background("#ffffff");

        views.getConfiguration().setThemes("https://raw.githubusercontent.com/structurizr/themes/master/amazon-web-services/theme.json");

        StructurizrClient structurizrClient = new StructurizrClient(API_KEY, API_SECRET);
        structurizrClient.putWorkspace(WORKSPACE_ID, workspace);
    }

}
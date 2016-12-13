package com.structurizr.example.core;

import com.structurizr.Workspace;
import com.structurizr.api.StructurizrClient;
import com.structurizr.model.*;
import com.structurizr.view.*;

/**
 * A simple example of how to use dynamic views to show runtime behaviour.
 *
 * You can see the live diagrams at https://structurizr.com/public/10021
 */
public class DynamicViews {

    public static void main(String[] args) throws Exception {
        Workspace workspace = new Workspace("Dynamic Views", "A simple example of using dynamic views.");
        Model model = workspace.getModel();
        ViewSet views = workspace.getViews();
        Styles styles = views.getConfiguration().getStyles();

        Person customer = model.addPerson("Customer", "A customer of the store.");
        SoftwareSystem softwareSystem = model.addSoftwareSystem("E-commerce System", "Allows customers to buy things online.");
        Container webApplication = softwareSystem.addContainer("Web Application", "Does many things.", "Spring Boot");
        Container database = softwareSystem.addContainer("Database", "Stores many things.", "MySQL");

        customer.uses(webApplication, "Uses", "HTTPS");
        webApplication.uses(database, "Reads from and writes to", "JDBC");

        ContainerView containerView = views.createContainerView(softwareSystem, "containers", "This diagram shows the static structure of the e-commerce system, from a containers perspective.");
        containerView.addAllElements();

        DynamicView dynamicView1 = views.createDynamicView(softwareSystem, "signin", "This diagram shows the basic interactions between containers when the customer signs in.");
        dynamicView1.add(customer, "Signs in using", webApplication);
        dynamicView1.add(webApplication, "select hashed_password from users where username = ?", database);

        DynamicView dynamicView2 = views.createDynamicView(softwareSystem, "showPastOrders", "This diagram shows the basic interactions between containers when the customer requests a list of their past orders.");
        dynamicView2.add(customer, "Requests a list of past orders from", webApplication);
        dynamicView2.add(webApplication, "select * from orders where user_id = ?", database);

        styles.addElementStyle(Tags.ELEMENT).color("#ffffff");
        styles.addElementStyle(Tags.PERSON).shape(Shape.Person).background("#a20000");
        styles.addElementStyle(Tags.CONTAINER).shape(Shape.RoundedBox).background("#c60000");
        styles.addRelationshipStyle(Tags.RELATIONSHIP).width(400);

        StructurizrClient client = new StructurizrClient("key", "secret");
        client.putWorkspace(10021, workspace);
    }

}

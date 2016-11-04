package com.structurizr.example.core;

import com.structurizr.Workspace;
import com.structurizr.api.StructurizrClient;
import com.structurizr.model.*;
import com.structurizr.view.*;

public class WidgetsLimited {

    private static final String EXTERNAL_PERSON_TAG = "External Person";
    private static final String EXTERNAL_SOFTWARE_SYSTEM_TAG = "External Software System";

    private static final String INTERNAL_PERSON_TAG = "Internal Person";
    private static final String INTERNAL_SOFTWARE_SYSTEM_TAG = "Internal Software System";

    public static void main(String[] args) throws Exception {
        Workspace workspace = new Workspace("Widgets Limited", "Sells widgets to customers online.");
        Model model = workspace.getModel();
        ViewSet views = workspace.getViews();
        Styles styles = views.getConfiguration().getStyles();

        model.setEnterprise(new Enterprise("Widgets Limited"));

        Person customer = model.addPerson(Location.External, "Customer", "A customer of Widgets Limited.");
        Person customerServiceUser = model.addPerson(Location.Internal, "Customer Service Agent", "Deals with customer enquiries.");
        SoftwareSystem ecommerceSystem = model.addSoftwareSystem(Location.Internal, "E-commerce System", "Allows customers to buy widgets online via the widgets.com website.");
        SoftwareSystem fulfilmentSystem = model.addSoftwareSystem(Location.Internal, "Fulfilment System", "Responsible for processing and shipping of customer orders.");
        SoftwareSystem taxamo = model.addSoftwareSystem(Location.External, "Taxamo", "Calculates local tax (for EU B2B customers) and acts as a front-end for Braintree Payments.");
        taxamo.setUrl("https://www.taxamo.com");
        SoftwareSystem braintreePayments = model.addSoftwareSystem(Location.External, "Braintree Payments", "Processes credit card payments on behalf of Widgets Limited.");
        braintreePayments.setUrl("https://www.braintreepayments.com");
        SoftwareSystem jerseyPost = model.addSoftwareSystem(Location.External, "Jersey Post", "Calculates worldwide shipping costs for packages.");

        model.getPeople().stream().filter(p -> p.getLocation() == Location.External).forEach(p -> p.addTags(EXTERNAL_PERSON_TAG));
        model.getPeople().stream().filter(p -> p.getLocation() == Location.Internal).forEach(p -> p.addTags(INTERNAL_PERSON_TAG));

        model.getSoftwareSystems().stream().filter(ss -> ss.getLocation() == Location.External).forEach(ss -> ss.addTags(EXTERNAL_SOFTWARE_SYSTEM_TAG));
        model.getSoftwareSystems().stream().filter(ss -> ss.getLocation() == Location.Internal).forEach(ss -> ss.addTags(INTERNAL_SOFTWARE_SYSTEM_TAG));

        customer.interactsWith(customerServiceUser, "Asks questions to", "Telephone");
        customerServiceUser.uses(ecommerceSystem, "Looks up order information using");
        customer.uses(ecommerceSystem, "Places orders for widgets using");
        ecommerceSystem.uses(fulfilmentSystem, "Sends order information to");
        fulfilmentSystem.uses(jerseyPost, "Gets shipping charges from");
        ecommerceSystem.uses(taxamo, "Delegates credit card processing to");
        taxamo.uses(braintreePayments, "Uses for credit card processing");

        EnterpriseContextView enterpriseContextView = views.createEnterpriseContextView("EnterpriseContext", "The enterprise context for Widgets Limited.");
        enterpriseContextView.addAllElements();

        SystemContextView ecommerceSystemContext = views.createSystemContextView(ecommerceSystem, "EcommerceSystemContext", "The system context diagram for the Widgets Limited e-commerce system.");
        ecommerceSystemContext.addNearestNeighbours(ecommerceSystem);
        ecommerceSystemContext.remove(customer.getEfferentRelationshipWith(customerServiceUser));

        SystemContextView fulfilmentSystemContext = views.createSystemContextView(fulfilmentSystem, "FulfilmentSystemContext", "The system context diagram for the Widgets Limited fulfilment system.");
        fulfilmentSystemContext.addNearestNeighbours(fulfilmentSystem);

        DynamicView dynamicView = views.createDynamicView("CustomerSupportCall", "A high-level overview of the customer support call process.");
        dynamicView.add(customer, customerServiceUser);
        dynamicView.add(customerServiceUser, ecommerceSystem);

        styles.addElementStyle(Tags.SOFTWARE_SYSTEM).shape(Shape.RoundedBox);
        styles.addElementStyle(Tags.PERSON).shape(Shape.Person);

        styles.addElementStyle(Tags.ELEMENT).color("#ffffff");
        styles.addElementStyle(EXTERNAL_PERSON_TAG).background("#EC5381");
        styles.addElementStyle(EXTERNAL_SOFTWARE_SYSTEM_TAG).background("#EC5381");

        styles.addElementStyle(INTERNAL_PERSON_TAG).background("#B60037");
        styles.addElementStyle(INTERNAL_SOFTWARE_SYSTEM_TAG).background("#B60037");

        StructurizrClient structurizrClient = new StructurizrClient("key", "secret");
        structurizrClient.mergeWorkspace(14471, workspace);
    }

}

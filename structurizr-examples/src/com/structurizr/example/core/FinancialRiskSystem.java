package com.structurizr.example.core;

import com.structurizr.Workspace;
import com.structurizr.api.StructurizrClient;
import com.structurizr.io.json.JsonWriter;
import com.structurizr.model.*;
import com.structurizr.view.*;

import java.io.StringWriter;

/**
 * This is a simple (incomplete) example C4 model based upon the financial risk system
 * architecture kata, which can be found at http://bit.ly/sa4d-risksystem
 */
public class FinancialRiskSystem {

    private static final String TAG_ALERT = "Alert";

    public static void main(String[] args) throws Exception {
        Workspace workspace = new Workspace("Financial Risk System", "This is a simple (incomplete) example C4 model based upon the financial risk system architecture kata, which can be found at http://bit.ly/sa4d-risksystem");
        Model model = workspace.getModel();

        // create the basic model
        SoftwareSystem financialRiskSystem = model.addSoftwareSystem(Location.Internal, "Financial Risk System", "Calculates the bank's exposure to risk for product X");

        Person businessUser = model.addPerson(Location.Internal, "Business User", "A regular business user");
        businessUser.uses(financialRiskSystem, "Views reports using");

        Person configurationUser = model.addPerson(Location.Internal, "Configuration User", "A regular business user who can also configure the parameters used in the risk calculations");
        configurationUser.uses(financialRiskSystem, "Configures parameters using");

        SoftwareSystem tradeDataSystem = model.addSoftwareSystem(Location.Internal, "Trade Data System", "The system of record for trades of type X");
        financialRiskSystem.uses(tradeDataSystem, "Gets trade data from");

        SoftwareSystem referenceDataSystem = model.addSoftwareSystem(Location.Internal, "Reference Data System", "Manages reference data for all counterparties the bank interacts with");
        financialRiskSystem.uses(referenceDataSystem, "Gets counterparty data from");

        SoftwareSystem emailSystem = model.addSoftwareSystem(Location.Internal, "E-mail system", "Microsoft Exchange");
        financialRiskSystem.uses(emailSystem, "Sends a notification that a report is ready to");
        emailSystem.delivers(businessUser, "Sends a notification that a report is ready to", "E-mail message", InteractionStyle.Asynchronous);

        SoftwareSystem centralMonitoringService = model.addSoftwareSystem(Location.Internal, "Central Monitoring Service", "The bank-wide monitoring and alerting dashboard");
        financialRiskSystem.uses(centralMonitoringService, "Sends critical failure alerts to").addTags(TAG_ALERT);

        SoftwareSystem activeDirectory = model.addSoftwareSystem(Location.Internal, "Active Directory", "Manages users and security roles across the bank");
        financialRiskSystem.uses(activeDirectory, "Uses for authentication and authorisation");

        // create some views
        ViewSet viewSet = workspace.getViews();
        SystemContextView contextView = viewSet.createContextView(financialRiskSystem);
        contextView.addAllSoftwareSystems();
        contextView.addAllPeople();

        // tag and style some elements
        financialRiskSystem.addTags("Risk System");
        viewSet.getConfiguration().getStyles().add(new ElementStyle("Risk System", null, null, "#550000", "#ffffff", 40));
        viewSet.getConfiguration().getStyles().add(new ElementStyle(Tags.SOFTWARE_SYSTEM, 650, 300, "#801515", "#ffffff", 36, Shape.RoundedBox));
        viewSet.getConfiguration().getStyles().add(new ElementStyle(Tags.PERSON, 550, null, "#d46a6a", "#ffffff", 32, Shape.Person));
        viewSet.getConfiguration().getStyles().add(new RelationshipStyle(Tags.RELATIONSHIP, 4, null, false, null, 32, 400, null));
        viewSet.getConfiguration().getStyles().add(new RelationshipStyle(Tags.SYNCHRONOUS, null, null, false, null, 32, 400, null));
        viewSet.getConfiguration().getStyles().add(new RelationshipStyle(Tags.ASYNCHRONOUS, null, null, true, null, null, null, null));
        viewSet.getConfiguration().getStyles().add(new RelationshipStyle(TAG_ALERT, null, "#ff0000", true, null, null, null, null));

        // and upload the model to structurizr.com
        StructurizrClient structurizrClient = new StructurizrClient("https://api.structurizr.com", "key", "secret");
        structurizrClient.mergeWorkspace(31, workspace);
    }

}
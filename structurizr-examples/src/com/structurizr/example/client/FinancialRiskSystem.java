package com.structurizr.example.client;

import com.structurizr.Workspace;
import com.structurizr.api.StructurizrClient;
import com.structurizr.model.*;
import com.structurizr.view.ElementStyle;
import com.structurizr.view.SystemContextView;
import com.structurizr.view.ViewSet;

/**
 * Create a simple model and upload it to structurizr.com
 */
public class FinancialRiskSystem {

    public static void main(String[] args) throws Exception {
        Workspace workspace = new Workspace("Financial Risk System", "This is a simple (incomplete) example C4 model based upon the financial risk system architecture kata, which can be found at http://bit.ly/sa4d-risksystem");
        Model model = workspace.getModel();

        // create the basic model
        SoftwareSystem financialRiskSystem = model.addSoftwareSystem(Location.Internal, "Financial Risk System", "Calculates the bank's exposure to risk for product X");

        Person businessUser = model.addPerson(Location.Internal, "Business User", "A regular business user");
        businessUser.uses(financialRiskSystem, "Views reports");

        Person configurationUser = model.addPerson(Location.Internal, "Configuration User", "A regular business user who can also configure the parameters used in the risk calculations");
        configurationUser.uses(financialRiskSystem, "Configures parameters");

        SoftwareSystem tradeDataSystem = model.addSoftwareSystem(Location.Internal, "Trade Data System", "The system of record for trades of type X");
        financialRiskSystem.uses(tradeDataSystem, "Gets trade data from");

        SoftwareSystem referenceDataSystem = model.addSoftwareSystem(Location.Internal, "Reference Data System", "Manages reference data for all counterparties the bank interacts with");
        financialRiskSystem.uses(referenceDataSystem, "Gets counterparty data from");

        SoftwareSystem emailSystem = model.addSoftwareSystem(Location.Internal, "E-mail system", "Microsoft Exchange");
        financialRiskSystem.uses(emailSystem, "Sends a notification that a report is ready via e-mail to");
        emailSystem.delivers(businessUser, "Sends a notification that a report is ready via e-mail to");

        SoftwareSystem centralMonitoringService = model.addSoftwareSystem(Location.Internal, "Central Monitoring Service", "The bank-wide monitoring and alerting dashboard");
        financialRiskSystem.uses(centralMonitoringService, "Sends critical failure alerts to");

        SoftwareSystem activeDirectory = model.addSoftwareSystem(Location.Internal, "Active Directory", "Manages users and security roles across the bank");
        financialRiskSystem.uses(activeDirectory, "Uses for authentication and authorisation");

        // create some views
        ViewSet viewSet = workspace.getViews();
        SystemContextView contextView = viewSet.createContextView(financialRiskSystem);
        contextView.addAllSoftwareSystems();
        contextView.addAllPeople();

        // tag and style some elements
        financialRiskSystem.addTags("Risk System");
        viewSet.getStyles().add(new ElementStyle("Risk System", null, null, "#041F37", "white", null));
        viewSet.getStyles().add(new ElementStyle(Tags.SOFTWARE_SYSTEM, null, null, "#2A4E6E", "white", null));
        viewSet.getStyles().add(new ElementStyle(Tags.PERSON, null, null, "#728da5", "white", null));

        // and upload the model to structurizr.com
        StructurizrClient structurizrClient = new StructurizrClient("https://api.structurizr.com", "key", "secret");
        Workspace currentWorkspace = structurizrClient.getWorkspace(31);
        workspace.getViews().copyLayoutInformationFrom(currentWorkspace.getViews());
        workspace.setId(31); // this would be your workspace ID
        structurizrClient.putWorkspace(workspace);
    }

}

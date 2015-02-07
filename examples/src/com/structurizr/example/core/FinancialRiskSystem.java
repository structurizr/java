package com.structurizr.example.core;

import com.structurizr.Workspace;
import com.structurizr.api.StructurizrClient;
import com.structurizr.io.json.JsonReader;
import com.structurizr.io.json.JsonWriter;
import com.structurizr.model.Location;
import com.structurizr.model.Model;
import com.structurizr.model.Person;
import com.structurizr.model.SoftwareSystem;
import com.structurizr.view.ElementStyle;
import com.structurizr.view.SystemContextView;
import com.structurizr.view.ViewSet;

import java.io.StringReader;
import java.io.StringWriter;

/**
 * This is a simple (incomplete) example C4 model based upon the financial risk system
 * architecture kata, which can be found at http://bit.ly/sa4d-risksystem
 */
public class FinancialRiskSystem {

    public static void main(String[] args) throws Exception {
        Workspace workspace = new Workspace("Financial Risk System", "This is a simple (incomplete) example C4 model based upon the financial risk system architecture kata, which can be found at http://bit.ly/sa4d-risksystem");
        Model model = workspace.getModel();

        // create the basic model
        SoftwareSystem financialRiskSystem = model.addSoftwareSystem(Location.Internal, "Financial Risk System", "Calculates the bank's exposure to risk for product X");
        financialRiskSystem.addTags("systemUnderConstruction");

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

        viewSet.getStyles().add(new ElementStyle("systemUnderConstruction", null, null, "#041F37", "white"));
        viewSet.getStyles().add(new ElementStyle("softwareSystem", null, null, "#2A4E6E", "white"));
        viewSet.getStyles().add(new ElementStyle("person", null, null, "#728da5", "white"));

        // and output the model as JSON
        JsonWriter jsonWriter = new JsonWriter(true);
        StringWriter stringWriter = new StringWriter();
        jsonWriter.write(workspace, stringWriter);
        System.out.println(stringWriter.toString());

//        JsonReader jsonReader = new JsonReader();
//        StringReader stringReader = new StringReader(stringWriter.toString());
//        Workspace newWorkspace = jsonReader.read(stringReader);

        StructurizrClient structurizrClient = new StructurizrClient("http://localhost:9090", "eab0b844-3e63-4961-b04e-c5a0b48411b6", "f9b7a3fe-1f39-4956-84ee-a5651cb1160b");
        workspace.setId(1);
        structurizrClient.putWorkspace(workspace);
    }

}

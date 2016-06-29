package com.structurizr.example.core.financialrisksystem;

import com.structurizr.Workspace;
import com.structurizr.api.StructurizrClient;
import com.structurizr.documentation.Documentation;
import com.structurizr.documentation.Format;
import com.structurizr.documentation.Type;
import com.structurizr.model.*;
import com.structurizr.view.Shape;
import com.structurizr.view.Styles;
import com.structurizr.view.SystemContextView;
import com.structurizr.view.ViewSet;

import java.io.File;

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
        SoftwareSystem financialRiskSystem = model.addSoftwareSystem("Financial Risk System", "Calculates the bank's exposure to risk for product X");

        Person businessUser = model.addPerson("Business User", "A regular business user");
        businessUser.uses(financialRiskSystem, "Views reports using");

        Person configurationUser = model.addPerson("Configuration User", "A regular business user who can also configure the parameters used in the risk calculations");
        configurationUser.uses(financialRiskSystem, "Configures parameters using");

        SoftwareSystem tradeDataSystem = model.addSoftwareSystem("Trade Data System", "The system of record for trades of type X");
        financialRiskSystem.uses(tradeDataSystem, "Gets trade data from");

        SoftwareSystem referenceDataSystem = model.addSoftwareSystem("Reference Data System", "Manages reference data for all counterparties the bank interacts with");
        financialRiskSystem.uses(referenceDataSystem, "Gets counterparty data from");

        SoftwareSystem emailSystem = model.addSoftwareSystem("E-mail system", "Microsoft Exchange");
        financialRiskSystem.uses(emailSystem, "Sends a notification that a report is ready to");
        emailSystem.delivers(businessUser, "Sends a notification that a report is ready to", "E-mail message", InteractionStyle.Asynchronous);

        SoftwareSystem centralMonitoringService = model.addSoftwareSystem("Central Monitoring Service", "The bank-wide monitoring and alerting dashboard");
        financialRiskSystem.uses(centralMonitoringService, "Sends critical failure alerts to", "SNMP", InteractionStyle.Asynchronous).addTags(TAG_ALERT);

        SoftwareSystem activeDirectory = model.addSoftwareSystem("Active Directory", "Manages users and security roles across the bank");
        financialRiskSystem.uses(activeDirectory, "Uses for authentication and authorisation");

        // create some views
        ViewSet viewSet = workspace.getViews();
        SystemContextView contextView = viewSet.createContextView(financialRiskSystem);
        contextView.addAllSoftwareSystems();
        contextView.addAllPeople();

        // tag and style some elements
        Styles styles = viewSet.getConfiguration().getStyles();
        financialRiskSystem.addTags("Risk System");

        styles.addElementStyle(Tags.ELEMENT).color("#ffffff").fontSize(34);
        styles.addElementStyle("Risk System").background("#550000").color("#ffffff");
        styles.addElementStyle(Tags.SOFTWARE_SYSTEM).width(650).height(400).background("#801515").shape(Shape.RoundedBox);
        styles.addElementStyle(Tags.PERSON).width(550).background("#d46a6a").shape(Shape.Person);

        styles.addRelationshipStyle(Tags.RELATIONSHIP).thickness(4).dashed(false).fontSize(32).width(400);
        styles.addRelationshipStyle(Tags.SYNCHRONOUS).dashed(false);
        styles.addRelationshipStyle(Tags.ASYNCHRONOUS).dashed(true);
        styles.addRelationshipStyle(TAG_ALERT).color("#ff0000");

        Documentation documentation = workspace.getDocumentation();
        File documentationRoot = new File("./structurizr-examples/src/com/structurizr/example/core/financialrisksystem");
        documentation.add(financialRiskSystem, Type.Context, Format.Markdown, new File(documentationRoot, "context.md"));
        documentation.add(financialRiskSystem, Type.FunctionalOverview, Format.Markdown, new File(documentationRoot, "functional-overview.md"));
        documentation.add(financialRiskSystem, Type.QualityAttributes, Format.Markdown, new File(documentationRoot, "quality-attributes.md"));
        documentation.addImages(documentationRoot);

        // and upload the model to structurizr.com
        StructurizrClient structurizrClient = new StructurizrClient("c9532520-06da-4579-b640-496d8fb3e0f9", "b7856871-8934-431c-8f71-28a6e3fad939");
        structurizrClient.mergeWorkspace(31, workspace);
    }

}
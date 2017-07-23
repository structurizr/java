package com.structurizr.example;

import com.structurizr.Workspace;
import com.structurizr.api.StructurizrClient;
import com.structurizr.documentation.Format;
import com.structurizr.documentation.StructurizrDocumentationTemplate;
import com.structurizr.model.*;
import com.structurizr.view.*;

import java.io.File;

/**
 * This is a simple (incomplete) example C4 model based upon the financial risk system
 * architecture kata, which can be found at http://bit.ly/sa4d-risksystem
 *
 * You can see the workspace online at https://structurizr.com/public/31
 */
public class FinancialRiskSystem {

    private static final long WORKSPACE_ID = 31;
    private static final String API_KEY = "";
    private static final String API_SECRET = "";

    private static final String TAG_ALERT = "Alert";

    public static void main(String[] args) throws Exception {
        Workspace workspace = new Workspace("Financial Risk System", "This is a simple (incomplete) example C4 model based upon the financial risk system architecture kata, which can be found at http://bit.ly/sa4d-risksystem");
        Model model = workspace.getModel();

        SoftwareSystem financialRiskSystem = model.addSoftwareSystem("Financial Risk System", "Calculates the bank's exposure to risk for product X.");

        Person businessUser = model.addPerson("Business User", "A regular business user.");
        businessUser.uses(financialRiskSystem, "Views reports using");

        Person configurationUser = model.addPerson("Configuration User", "A regular business user who can also configure the parameters used in the risk calculations.");
        configurationUser.uses(financialRiskSystem, "Configures parameters using");

        SoftwareSystem tradeDataSystem = model.addSoftwareSystem("Trade Data System", "The system of record for trades of type X.");
        financialRiskSystem.uses(tradeDataSystem, "Gets trade data from");

        SoftwareSystem referenceDataSystem = model.addSoftwareSystem("Reference Data System", "Manages reference data for all counterparties the bank interacts with.");
        financialRiskSystem.uses(referenceDataSystem, "Gets counterparty data from");

        SoftwareSystem referenceDataSystemV2 = model.addSoftwareSystem("Reference Data System v2.0", "Manages reference data for all counterparties the bank interacts with.");
        referenceDataSystemV2.addTags("Future State");
        financialRiskSystem.uses(referenceDataSystemV2, "Gets counterparty data from").addTags("Future State");

        SoftwareSystem emailSystem = model.addSoftwareSystem("E-mail system", "The bank's Microsoft Exchange system.");
        financialRiskSystem.uses(emailSystem, "Sends a notification that a report is ready to");
        emailSystem.delivers(businessUser, "Sends a notification that a report is ready to", "E-mail message", InteractionStyle.Asynchronous);

        SoftwareSystem centralMonitoringService = model.addSoftwareSystem("Central Monitoring Service", "The bank's central monitoring and alerting dashboard.");
        financialRiskSystem.uses(centralMonitoringService, "Sends critical failure alerts to", "SNMP", InteractionStyle.Asynchronous).addTags(TAG_ALERT);

        SoftwareSystem activeDirectory = model.addSoftwareSystem("Active Directory", "The bank's authentication and authorisation system.");
        financialRiskSystem.uses(activeDirectory, "Uses for user authentication and authorisation");

        ViewSet views = workspace.getViews();
        SystemContextView contextView = views.createSystemContextView(financialRiskSystem, "Context", "An example System Context diagram for the Financial Risk System architecture kata.");
        contextView.addAllSoftwareSystems();
        contextView.addAllPeople();

        Styles styles = views.getConfiguration().getStyles();
        financialRiskSystem.addTags("Risk System");

        styles.addElementStyle(Tags.ELEMENT).color("#ffffff").fontSize(34);
        styles.addElementStyle("Risk System").background("#550000").color("#ffffff");
        styles.addElementStyle(Tags.SOFTWARE_SYSTEM).width(650).height(400).background("#801515").shape(Shape.RoundedBox);
        styles.addElementStyle(Tags.PERSON).width(550).background("#d46a6a").shape(Shape.Person);

        styles.addRelationshipStyle(Tags.RELATIONSHIP).thickness(4).dashed(false).fontSize(32).width(400);
        styles.addRelationshipStyle(Tags.SYNCHRONOUS).dashed(false);
        styles.addRelationshipStyle(Tags.ASYNCHRONOUS).dashed(true);
        styles.addRelationshipStyle(TAG_ALERT).color("#ff0000");

        styles.addElementStyle("Future State").opacity(30).border(Border.Dashed);
        styles.addRelationshipStyle("Future State").opacity(30).dashed(true);

        StructurizrDocumentationTemplate template = new StructurizrDocumentationTemplate(workspace);
        File documentationRoot = new File("./structurizr-examples/src/com/structurizr/example/financialrisksystem");
        template.addContextSection(financialRiskSystem, Format.AsciiDoc, new File(documentationRoot, "context.adoc"));
        template.addFunctionalOverviewSection(financialRiskSystem, Format.Markdown, new File(documentationRoot, "functional-overview.md"));
        template.addQualityAttributesSection(financialRiskSystem, Format.Markdown, new File(documentationRoot, "quality-attributes.md"));
        template.addImages(documentationRoot);

        StructurizrClient structurizrClient = new StructurizrClient(API_KEY, API_SECRET);
        structurizrClient.putWorkspace(WORKSPACE_ID, workspace);
    }

}
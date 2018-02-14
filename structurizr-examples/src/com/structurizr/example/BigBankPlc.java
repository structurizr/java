package com.structurizr.example;

import com.structurizr.Workspace;
import com.structurizr.api.StructurizrClient;
import com.structurizr.documentation.Format;
import com.structurizr.documentation.StructurizrDocumentationTemplate;
import com.structurizr.model.*;
import com.structurizr.util.MapUtils;
import com.structurizr.view.*;

/**
 * This is an example workspace to illustrate the key features of Structurizr,
 * based around a fictional Internet Banking System for Big Bank plc.
 *
 * The live workspace is available to view at https://structurizr.com/share/36141
 */
public class BigBankPlc {

    private static final long WORKSPACE_ID = 36141;
    private static final String API_KEY = "key";
    private static final String API_SECRET = "secret";

    private static final String DATABASE_TAG = "Database";

    private static Workspace create(boolean usePaidFeatures) {
        Workspace workspace = new Workspace("Big Bank plc", "This is an example workspace to illustrate the key features of Structurizr, based around a fictional online banking system.");
        Model model = workspace.getModel();
        ViewSet views = workspace.getViews();

        model.setEnterprise(new Enterprise("Big Bank plc"));

        // people and software systems
        Person customer = model.addPerson(Location.External, "Customer", "A customer of the bank.");

        SoftwareSystem internetBankingSystem = model.addSoftwareSystem(Location.Internal, "Internet Banking System", "Allows customers to view information about their bank accounts and make payments.");
        customer.uses(internetBankingSystem, "Uses");

        SoftwareSystem mainframeBankingSystem = model.addSoftwareSystem(Location.Internal, "Mainframe Banking System", "Stores all of the core banking information about customers, accounts, transactions, etc.");
        internetBankingSystem.uses(mainframeBankingSystem, "Uses");

        SoftwareSystem atm = model.addSoftwareSystem(Location.Internal, "ATM", "Allows customers to withdraw cash.");
        atm.uses(mainframeBankingSystem, "Uses");
        customer.uses(atm, "Withdraws cash using");

        Person bankStaff = model.addPerson(Location.Internal, "Bank Staff", "Staff within the bank.");
        bankStaff.uses(mainframeBankingSystem, "Uses");

        // containers
        Container webApplication = internetBankingSystem.addContainer("Web Application", "Provides all of the Internet banking functionality to customers.", "Java and Spring MVC");
        Container database = internetBankingSystem.addContainer("Database", "Stores interesting data.", "Relational Database Schema");
        database.addTags(DATABASE_TAG);

        customer.uses(webApplication, "Uses", "HTTPS");
        webApplication.uses(database, "Reads from and writes to", "JDBC");
        webApplication.uses(mainframeBankingSystem, "Uses", "XML/HTTPS");

        // components
        // - for a real-world software system, you would probably want to extract the components using
        // - static analysis/reflection rather than manually specifying them all
        Component homePageController = webApplication.addComponent("Home Page Controller", "Serves up the home page.", "Spring MVC Controller");
        Component signinController = webApplication.addComponent("Sign In Controller", "Allows users to sign in to the Internet Banking System.", "Spring MVC Controller");
        Component accountsSummaryController = webApplication.addComponent("Accounts Summary Controller", "Provides customers with an summary of their bank accounts.", "Spring MVC Controller");
        Component securityComponent = webApplication.addComponent("Security Component", "Provides functionality related to signing in, changing passwords, etc.", "Spring Bean");
        Component mainframeBankingSystemFacade = webApplication.addComponent("Mainframe Banking System Facade", "A facade onto the mainframe banking system.", "Spring Bean");

        webApplication.getComponents().stream().filter(c -> "Spring MVC Controller".equals(c.getTechnology())).forEach(c -> customer.uses(c, "Uses", "HTTPS"));
        signinController.uses(securityComponent, "Uses");
        accountsSummaryController.uses(mainframeBankingSystemFacade, "Uses");
        securityComponent.uses(database, "Reads from and writes to", "JDBC");
        mainframeBankingSystemFacade.uses(mainframeBankingSystem, "Uses", "XML/HTTPS");

        // deployment nodes and container instances
        DeploymentNode developerLaptop = model.addDeploymentNode("Developer Laptop", "A developer laptop.", "Windows 7 or 10");
        developerLaptop.addDeploymentNode("Docker Container - Web Server", "A Docker container.", "Docker")
            .addDeploymentNode("Apache Tomcat", "An open source Java EE web server.", "Apache Tomcat 8.x", 1, MapUtils.create("Xmx=512M", "Xms=1024M", "Java Version=8"))
            .add(webApplication);

        developerLaptop.addDeploymentNode("Docker Container - Database Server", "A Docker container.", "Docker")
            .addDeploymentNode("Database Server", "A development database.", "Oracle 12c")
            .add(database);

        DeploymentNode liveWebServer = model.addDeploymentNode("bigbank-web***", "A web server residing in the web server farm, accessed via F5 BIG-IP LTMs.", "Ubuntu 16.04 LTS", 8, MapUtils.create("Location=London"));
        liveWebServer.addDeploymentNode("Apache Tomcat", "An open source Java EE web server.", "Apache Tomcat 8.x", 1, MapUtils.create("Xmx=512M", "Xms=1024M", "Java Version=8"))
                .add(webApplication);

        DeploymentNode primaryDatabaseServer = model.addDeploymentNode("bigbank-db01", "The primary database server.", "Ubuntu 16.04 LTS", 1, MapUtils.create("Location=London"))
                .addDeploymentNode("Oracle - Primary", "The primary, live database server.", "Oracle 12c");
        primaryDatabaseServer.add(database);

        DeploymentNode secondaryDatabaseServer = model.addDeploymentNode("bigbank-db02", "The secondary database server.", "Ubuntu 16.04 LTS", 1, MapUtils.create("Location=Reading"))
                .addDeploymentNode("Oracle - Secondary", "A secondary, standby database server, used for failover purposes only.", "Oracle 12c");
        ContainerInstance secondaryDatabase = secondaryDatabaseServer.add(database);

        model.getRelationships().stream().filter(r -> r.getDestination().equals(secondaryDatabase)).forEach(r -> r.addTags("Failover"));
        Relationship dataReplicationRelationship = primaryDatabaseServer.uses(secondaryDatabaseServer, "Replicates data to", "");
        secondaryDatabase.addTags("Failover");

        // views/diagrams
        SystemLandscapeView systemLandscapeView = views.createSystemLandscapeView("SystemLandscape", "The system landscape diagram for Big Bank plc.");
        systemLandscapeView.addAllElements();
        systemLandscapeView.setPaperSize(PaperSize.A5_Landscape);

        SystemContextView systemContextView = views.createSystemContextView(internetBankingSystem, "SystemContext", "The system context diagram for the Internet Banking System.");
        systemContextView.addNearestNeighbours(internetBankingSystem);
        systemContextView.setPaperSize(PaperSize.A5_Landscape);

        ContainerView containerView = views.createContainerView(internetBankingSystem, "Containers", "The container diagram for the Internet Banking System.");
        containerView.add(customer);
        containerView.addAllContainers();
        containerView.add(mainframeBankingSystem);
        containerView.setPaperSize(PaperSize.A5_Landscape);

        ComponentView componentView = views.createComponentView(webApplication, "Components", "The component diagram for the Web Application.");
        componentView.addAllContainers();
        componentView.addAllComponents();
        componentView.add(customer);
        componentView.add(mainframeBankingSystem);
        componentView.setPaperSize(PaperSize.A5_Landscape);

        if (usePaidFeatures) {
            // dynamic diagrams, deployment diagrams and corporate branding are not available with the Free Plan
            DynamicView dynamicView = views.createDynamicView(webApplication, "SignIn", "Summarises how the sign in feature works.");
            dynamicView.add(customer, "Requests /signin from", signinController);
            dynamicView.add(customer, "Submits credentials to", signinController);
            dynamicView.add(signinController, "Calls isAuthenticated() on", securityComponent);
            dynamicView.add(securityComponent, "select * from users where username = ?", database);
            dynamicView.setPaperSize(PaperSize.A5_Landscape);

            DeploymentView developmentDeploymentView = views.createDeploymentView(internetBankingSystem, "DevelopmentDeployment", "An example development deployment scenario for the Internet Banking System.");
            developmentDeploymentView.setEnvironment("Development");
            developmentDeploymentView.add(developerLaptop);
            developmentDeploymentView.setPaperSize(PaperSize.A5_Landscape);

            DeploymentView liveDeploymentView = views.createDeploymentView(internetBankingSystem, "LiveDeployment", "An example live deployment scenario for the Internet Banking System.");
            liveDeploymentView.setEnvironment("Live");
            liveDeploymentView.add(liveWebServer);
            liveDeploymentView.add(primaryDatabaseServer);
            liveDeploymentView.add(secondaryDatabaseServer);
            liveDeploymentView.add(dataReplicationRelationship);
            liveDeploymentView.setPaperSize(PaperSize.A5_Landscape);
        }

        // colours, shapes and other diagram styling
        Styles styles = views.getConfiguration().getStyles();
        styles.addElementStyle(Tags.ELEMENT).color("#ffffff");
        styles.addElementStyle(Tags.SOFTWARE_SYSTEM).background("#1168bd");
        styles.addElementStyle(Tags.CONTAINER).background("#438dd5");
        styles.addElementStyle(Tags.COMPONENT).background("#85bbf0").color("#000000");
        styles.addElementStyle(Tags.PERSON).background("#08427b").shape(Shape.Person);
        styles.addElementStyle(DATABASE_TAG).shape(Shape.Cylinder);
        styles.addElementStyle("Failover").opacity(25);
        styles.addRelationshipStyle("Failover").opacity(25).position(70);

        // documentation
        // - usually the documentation would be included from separate Markdown/AsciiDoc files, but this is just an example
        StructurizrDocumentationTemplate template = new StructurizrDocumentationTemplate(workspace);
        template.addContextSection(internetBankingSystem, Format.Markdown,
                "Here is some context about the Internet Banking System...\n" +
                "![](embed:SystemLandscape)\n" +
                "![](embed:SystemContext)\n" +
                "### Internet Banking System\n...\n" +
                "### Mainframe Banking System\n...\n");
        template.addContainersSection(internetBankingSystem, Format.Markdown,
                "Here is some information about the containers within the Internet Banking System...\n" +
                "![](embed:Containers)\n" +
                "### Web Application\n...\n" +
                "### Database\n...\n");
        template.addComponentsSection(webApplication, Format.Markdown,
                "Here is some information about the Web Application...\n" +
                "![](embed:Components)\n" +
                "### Sign in process\n" +
                "Here is some information about the Sign In Controller, including how the sign in process works...\n" +
                "![](embed:SignIn)");
        template.addDevelopmentEnvironmentSection(internetBankingSystem, Format.AsciiDoc,
                "Here is some information about how to set up a development environment for the Internet Banking System...\n" +
                "image::embed:DevelopmentDeployment[]");
        template.addDeploymentSection(internetBankingSystem, Format.AsciiDoc,
                "Here is some information about the live deployment environment for the Internet Banking System...\n" +
                "image::embed:LiveDeployment[]");

        return workspace;
    }

    public static void main(String[] args) throws Exception {
        StructurizrClient structurizrClient = new StructurizrClient(API_KEY, API_SECRET);
        structurizrClient.putWorkspace(WORKSPACE_ID, create(true));
    }

}
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

    private static final String EXISTING_SYSTEM_TAG = "Existing System";
    private static final String BANK_STAFF_TAG = "Bank Staff";
    private static final String WEB_BROWSER_TAG = "Web Browser";
    private static final String MOBILE_APP_TAG = "Mobile App";
    private static final String DATABASE_TAG = "Database";
    private static final String FAILOVER_TAG = "Failover";

    public static void main(String[] args) throws Exception {
        Workspace workspace = new Workspace("Big Bank plc", "This is an example workspace to illustrate the key features of Structurizr, based around a fictional online banking system.");
        Model model = workspace.getModel();
        ViewSet views = workspace.getViews();

        model.setEnterprise(new Enterprise("Big Bank plc"));

        // people and software systems
        Person customer = model.addPerson(Location.External, "Personal Banking Customer", "A customer of the bank, with personal bank accounts.");

        SoftwareSystem internetBankingSystem = model.addSoftwareSystem(Location.Internal, "Internet Banking System", "Allows customers to view information about their bank accounts, and make payments.");
        customer.uses(internetBankingSystem, "Views account balances, and makes payments using");

        SoftwareSystem mainframeBankingSystem = model.addSoftwareSystem(Location.Internal, "Mainframe Banking System", "Stores all of the core banking information about customers, accounts, transactions, etc.");
        mainframeBankingSystem.addTags(EXISTING_SYSTEM_TAG);
        internetBankingSystem.uses(mainframeBankingSystem, "Gets account information from, and makes payments using");

        SoftwareSystem emailSystem = model.addSoftwareSystem(Location.Internal, "E-mail System", "The internal Microsoft Exchange e-mail system.");
        internetBankingSystem.uses(emailSystem, "Sends e-mail using");
        emailSystem.addTags(EXISTING_SYSTEM_TAG);
        emailSystem.delivers(customer, "Sends e-mails to");

        SoftwareSystem atm = model.addSoftwareSystem(Location.Internal, "ATM", "Allows customers to withdraw cash.");
        atm.addTags(EXISTING_SYSTEM_TAG);
        atm.uses(mainframeBankingSystem, "Uses");
        customer.uses(atm, "Withdraws cash using");

        Person customerServiceStaff = model.addPerson(Location.Internal, "Customer Service Staff", "Customer service staff within the bank.");
        customerServiceStaff.addTags(BANK_STAFF_TAG);
        customerServiceStaff.uses(mainframeBankingSystem, "Uses");
        customer.interactsWith(customerServiceStaff, "Asks questions to", "Telephone");

        Person backOfficeStaff = model.addPerson(Location.Internal, "Back Office Staff", "Administration and support staff within the bank.");
        backOfficeStaff.addTags(BANK_STAFF_TAG);
        backOfficeStaff.uses(mainframeBankingSystem, "Uses");

        // containers
        Container singlePageApplication = internetBankingSystem.addContainer("Single-Page Application", "Provides all of the Internet banking functionality to customers via their web browser.", "JavaScript and Angular");
        singlePageApplication.addTags(WEB_BROWSER_TAG);
        Container mobileApp = internetBankingSystem.addContainer("Mobile App", "Provides a limited subset of the Internet banking functionality to customers via their mobile device.", "Xamarin");
        mobileApp.addTags(MOBILE_APP_TAG);
        Container webApplication = internetBankingSystem.addContainer("Web Application", "Delivers the static content and the Internet banking single page application.", "Java and Spring MVC");
        Container apiApplication = internetBankingSystem.addContainer("API Application", "Provides Internet banking functionality via a JSON/HTTPS API.", "Java and Spring MVC");
        Container database = internetBankingSystem.addContainer("Database", "Stores user registration information, hashed authentication credentials, access logs, etc.", "Oracle Database Schema");
        database.addTags(DATABASE_TAG);

        customer.uses(webApplication, "Visits bigbank.com/ib using", "HTTPS");
        customer.uses(singlePageApplication, "Views account balances, and makes payments using", "");
        customer.uses(mobileApp, "Views account balances, and makes payments using", "");
        webApplication.uses(singlePageApplication, "Delivers to the customer's web browser", "");
        apiApplication.uses(database, "Reads from and writes to", "JDBC");
        apiApplication.uses(mainframeBankingSystem, "Makes API calls to", "XML/HTTPS");
        apiApplication.uses(emailSystem, "Sends e-mail using", "SMTP");

        // components
        // - for a real-world software system, you would probably want to extract the components using
        // - static analysis/reflection rather than manually specifying them all
        Component signinController = apiApplication.addComponent("Sign In Controller", "Allows users to sign in to the Internet Banking System.", "Spring MVC Rest Controller");
        Component accountsSummaryController = apiApplication.addComponent("Accounts Summary Controller", "Provides customers with a summary of their bank accounts.", "Spring MVC Rest Controller");
        Component resetPasswordController = apiApplication.addComponent("Reset Password Controller", "Allows users to reset their passwords with a single use URL.", "Spring MVC Rest Controller");
        Component securityComponent = apiApplication.addComponent("Security Component", "Provides functionality related to signing in, changing passwords, etc.", "Spring Bean");
        Component mainframeBankingSystemFacade = apiApplication.addComponent("Mainframe Banking System Facade", "A facade onto the mainframe banking system.", "Spring Bean");
        Component emailComponent = apiApplication.addComponent("E-mail Component", "Sends e-mails to users.", "Spring Bean");

        apiApplication.getComponents().stream().filter(c -> "Spring MVC Rest Controller".equals(c.getTechnology())).forEach(c -> singlePageApplication.uses(c, "Makes API calls to", "JSON/HTTPS"));
        apiApplication.getComponents().stream().filter(c -> "Spring MVC Rest Controller".equals(c.getTechnology())).forEach(c -> mobileApp.uses(c, "Makes API calls to", "JSON/HTTPS"));
        signinController.uses(securityComponent, "Uses");
        accountsSummaryController.uses(mainframeBankingSystemFacade, "Uses");
        resetPasswordController.uses(securityComponent, "Uses");
        resetPasswordController.uses(emailComponent, "Uses");
        securityComponent.uses(database, "Reads from and writes to", "JDBC");
        mainframeBankingSystemFacade.uses(mainframeBankingSystem, "Uses", "XML/HTTPS");
        emailComponent.uses(emailSystem, "Sends e-mail using");

        model.addImplicitRelationships();

        // deployment nodes and container instances
        DeploymentNode developerLaptop = model.addDeploymentNode("Development", "Developer Laptop", "A developer laptop.", "Microsoft Windows 10 or Apple macOS");
        DeploymentNode apacheTomcat = developerLaptop.addDeploymentNode("Docker Container - Web Server", "A Docker container.", "Docker")
                .addDeploymentNode("Apache Tomcat", "An open source Java EE web server.", "Apache Tomcat 8.x", 1, MapUtils.create("Xmx=512M", "Xms=1024M", "Java Version=8"));
        apacheTomcat.add(webApplication);
        apacheTomcat.add(apiApplication);

        developerLaptop.addDeploymentNode("Docker Container - Database Server", "A Docker container.", "Docker")
                .addDeploymentNode("Database Server", "A development database.", "Oracle 12c")
                .add(database);

        developerLaptop.addDeploymentNode("Web Browser", "", "Google Chrome, Mozilla Firefox, Apple Safari or Microsoft Edge").add(singlePageApplication);

        DeploymentNode customerMobileDevice = model.addDeploymentNode("Live", "Customer's mobile device", "", "Apple iOS or Android");
        customerMobileDevice.add(mobileApp);

        DeploymentNode customerComputer = model.addDeploymentNode("Live", "Customer's computer", "", "Microsoft Windows or Apple macOS");
        customerComputer.addDeploymentNode("Web Browser", "", "Google Chrome, Mozilla Firefox, Apple Safari or Microsoft Edge").add(singlePageApplication);

        DeploymentNode bigBankDataCenter = model.addDeploymentNode("Live", "Big Bank plc", "", "Big Bank plc data center");

        DeploymentNode liveWebServer = bigBankDataCenter.addDeploymentNode("bigbank-web***", "A web server residing in the web server farm, accessed via F5 BIG-IP LTMs.", "Ubuntu 16.04 LTS", 4, MapUtils.create("Location=London and Reading"));
        liveWebServer.addDeploymentNode("Apache Tomcat", "An open source Java EE web server.", "Apache Tomcat 8.x", 1, MapUtils.create("Xmx=512M", "Xms=1024M", "Java Version=8"))
                .add(webApplication);

        DeploymentNode liveApiServer = bigBankDataCenter.addDeploymentNode("bigbank-api***", "A web server residing in the web server farm, accessed via F5 BIG-IP LTMs.", "Ubuntu 16.04 LTS", 8, MapUtils.create("Location=London and Reading"));
        liveApiServer.addDeploymentNode("Apache Tomcat", "An open source Java EE web server.", "Apache Tomcat 8.x", 1, MapUtils.create("Xmx=512M", "Xms=1024M", "Java Version=8"))
                .add(apiApplication);

        DeploymentNode primaryDatabaseServer = bigBankDataCenter.addDeploymentNode("bigbank-db01", "The primary database server.", "Ubuntu 16.04 LTS", 1, MapUtils.create("Location=London"))
                .addDeploymentNode("Oracle - Primary", "The primary, live database server.", "Oracle 12c");
        primaryDatabaseServer.add(database);

        DeploymentNode bigBankdb02 = bigBankDataCenter.addDeploymentNode("bigbank-db02", "The secondary database server.", "Ubuntu 16.04 LTS", 1, MapUtils.create("Location=Reading"));
        bigBankdb02.addTags(FAILOVER_TAG);
        DeploymentNode secondaryDatabaseServer = bigBankdb02.addDeploymentNode("Oracle - Secondary", "A secondary, standby database server, used for failover purposes only.", "Oracle 12c");
        secondaryDatabaseServer.addTags(FAILOVER_TAG);
        ContainerInstance secondaryDatabase = secondaryDatabaseServer.add(database);

        model.getRelationships().stream().filter(r -> r.getDestination().equals(secondaryDatabase)).forEach(r -> r.addTags(FAILOVER_TAG));
        Relationship dataReplicationRelationship = primaryDatabaseServer.uses(secondaryDatabaseServer, "Replicates data to", "");
        secondaryDatabase.addTags(FAILOVER_TAG);

        // views/diagrams
        SystemLandscapeView systemLandscapeView = views.createSystemLandscapeView("SystemLandscape", "The system landscape diagram for Big Bank plc.");
        systemLandscapeView.addAllElements();
        systemLandscapeView.setPaperSize(PaperSize.A5_Landscape);

        SystemContextView systemContextView = views.createSystemContextView(internetBankingSystem, "SystemContext", "The system context diagram for the Internet Banking System.");
        systemContextView.setEnterpriseBoundaryVisible(false);
        systemContextView.addNearestNeighbours(internetBankingSystem);
        systemContextView.setPaperSize(PaperSize.A5_Landscape);

        ContainerView containerView = views.createContainerView(internetBankingSystem, "Containers", "The container diagram for the Internet Banking System.");
        containerView.add(customer);
        containerView.addAllContainers();
        containerView.add(mainframeBankingSystem);
        containerView.add(emailSystem);
        containerView.setPaperSize(PaperSize.A5_Landscape);

        ComponentView componentView = views.createComponentView(apiApplication, "Components", "The component diagram for the API Application.");
        componentView.add(mobileApp);
        componentView.add(singlePageApplication);
        componentView.add(database);
        componentView.addAllComponents();
        componentView.add(mainframeBankingSystem);
        componentView.add(emailSystem);
        componentView.setPaperSize(PaperSize.A5_Landscape);

        systemLandscapeView.addAnimation(internetBankingSystem, customer, mainframeBankingSystem, emailSystem);
        systemLandscapeView.addAnimation(atm);
        systemLandscapeView.addAnimation(customerServiceStaff, backOfficeStaff);

        systemContextView.addAnimation(internetBankingSystem);
        systemContextView.addAnimation(customer);
        systemContextView.addAnimation(mainframeBankingSystem);
        systemContextView.addAnimation(emailSystem);

        containerView.addAnimation(customer, mainframeBankingSystem, emailSystem);
        containerView.addAnimation(webApplication);
        containerView.addAnimation(singlePageApplication);
        containerView.addAnimation(mobileApp);
        containerView.addAnimation(apiApplication);
        containerView.addAnimation(database);

        componentView.addAnimation(singlePageApplication, mobileApp, database, emailSystem, mainframeBankingSystem);
        componentView.addAnimation(signinController, securityComponent);
        componentView.addAnimation(accountsSummaryController, mainframeBankingSystemFacade);
        componentView.addAnimation(resetPasswordController, emailComponent);

        // dynamic diagrams and deployment diagrams are not available with the Free Plan
        DynamicView dynamicView = views.createDynamicView(apiApplication, "SignIn", "Summarises how the sign in feature works in the single-page application.");
        dynamicView.add(singlePageApplication, "Submits credentials to", signinController);
        dynamicView.add(signinController, "Calls isAuthenticated() on", securityComponent);
        dynamicView.add(securityComponent, "select * from users where username = ?", database);
        dynamicView.setPaperSize(PaperSize.A5_Landscape);

        DeploymentView developmentDeploymentView = views.createDeploymentView(internetBankingSystem, "DevelopmentDeployment", "An example development deployment scenario for the Internet Banking System.");
        developmentDeploymentView.setEnvironment("Development");
        developmentDeploymentView.add(developerLaptop);
        developmentDeploymentView.setPaperSize(PaperSize.A5_Landscape);

        DeploymentView liveDeploymentView = views.createDeploymentView(internetBankingSystem, "LiveDeployment", "An example live deployment scenario for the Internet Banking System.");
        liveDeploymentView.setEnvironment("Live");
        liveDeploymentView.add(bigBankDataCenter);
        liveDeploymentView.add(customerMobileDevice);
        liveDeploymentView.add(customerComputer);
        liveDeploymentView.add(dataReplicationRelationship);
        liveDeploymentView.setPaperSize(PaperSize.A5_Landscape);

        // colours, shapes and other diagram styling
        Styles styles = views.getConfiguration().getStyles();
        styles.addElementStyle(Tags.ELEMENT).color("#ffffff");
        styles.addElementStyle(Tags.SOFTWARE_SYSTEM).background("#1168bd");
        styles.addElementStyle(Tags.CONTAINER).background("#438dd5");
        styles.addElementStyle(Tags.COMPONENT).background("#85bbf0").color("#000000");
        styles.addElementStyle(Tags.PERSON).background("#08427b").shape(Shape.Person).fontSize(22);
        styles.addElementStyle(EXISTING_SYSTEM_TAG).background("#999999");
        styles.addElementStyle(BANK_STAFF_TAG).background("#999999");
        styles.addElementStyle(WEB_BROWSER_TAG).shape(Shape.WebBrowser);
        styles.addElementStyle(MOBILE_APP_TAG).shape(Shape.MobileDeviceLandscape);
        styles.addElementStyle(DATABASE_TAG).shape(Shape.Cylinder);
        styles.addElementStyle(FAILOVER_TAG).opacity(25);
        styles.addRelationshipStyle(FAILOVER_TAG).opacity(25).position(70);

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
                "Here is some information about the API Application...\n" +
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

        StructurizrClient structurizrClient = new StructurizrClient(API_KEY, API_SECRET);
        structurizrClient.putWorkspace(WORKSPACE_ID, workspace);
    }

}
package com.structurizr.example.structurizr;

import com.structurizr.Workspace;
import com.structurizr.model.Container;
import com.structurizr.model.Model;
import com.structurizr.model.Person;
import com.structurizr.model.SoftwareSystem;
import com.structurizr.view.ContainerView;
import com.structurizr.view.ViewSet;

/**
 * Creates the container model and view.
 */
public class Containers extends AbstractStructurizrWorkspace {

    public static void main(String[] args) throws Exception {
        new Containers().run();
    }

    void run() throws Exception {
        Workspace workspace = readFromFile();
        Model model = workspace.getModel();

        SoftwareSystem structurizr = model.getSoftwareSystemWithName(STRUCTURIZR);
        Person anonymousUser = model.getPersonWithName(ANONYMOUS_USER);
        Person authenticatedUser = model.getPersonWithName(AUTHENTICATED_USER);
        SoftwareSystem structurizrClient = model.getSoftwareSystemWithName(STRUCTURIZR_CLIENT);
        SoftwareSystem pingdom = model.getSoftwareSystemWithName(PINGDOM);
        SoftwareSystem sendGrid = model.getSoftwareSystemWithName(SENDGRID);
        SoftwareSystem taxamo = model.getSoftwareSystemWithName(TAXAMO);

        Container webBrowser = structurizr.addContainer(WEB_BROWSER, "Allows users to view and manage workspaces.", "Safari, Firefox, Chrome, IE11");
        anonymousUser.uses(webBrowser, "Uses");
        authenticatedUser.uses(webBrowser, "Uses");

        Container webApplication = structurizr.addContainer(WWW, "Allows users to view and manage workspaces.", "Apache Tomcat 7.x");
        webBrowser.uses(webApplication, "Makes requests to");
        webApplication.uses(taxamo, "Looks up payment transactions using");

        Container api = structurizr.addContainer(API, "Allows authenticated clients to get/put workspaces.", "Apache Tomcat 7.x");
        webBrowser.uses(api, "Gets workspace data via");
        structurizrClient.uses(api, "Uploads software architecture models using");

        Container database = structurizr.addContainer(DATABASE, "Stores information about users, workspaces, etc.", "MySQL");
        database.addTags(DATABASE_TAG);
        webApplication.uses(database, "Reads from and writes to");
        api.uses(database, "Reads from and writes to");

        pingdom.uses(webApplication, "Monitors");
        webApplication.uses(sendGrid, "Sends e-mails using");

        ViewSet views = workspace.getViews();
        ContainerView containerView = views.createContainerView(structurizr);
        containerView.addAllElements();
        containerView.setKey("Containers");

        writeToFile(workspace);
    }

}
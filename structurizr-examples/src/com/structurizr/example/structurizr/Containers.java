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
        SoftwareSystem remoteApi = model.getSoftwareSystemWithName(ON_PREMISES_API);

        Container webBrowser = structurizr.addContainer(WEB_BROWSER, "Allows users to view and manage workspaces.", "Safari, Firefox, Chrome, IE11");
        anonymousUser.uses(webBrowser, "Uses");
        authenticatedUser.uses(webBrowser, "Uses");

        Container webApplication = structurizr.addContainer(WWW, "Allows users to view and manage workspaces.", "Apache Tomcat 7.x");
        webBrowser.uses(webApplication, "Makes requests to");
        webApplication.uses(taxamo, "Looks up payment transactions using");

        Container api = structurizr.addContainer(API, "Allows authenticated clients to get/put workspaces.", "Apache Tomcat 7.x");
        webBrowser.uses(api, "Reads and writes workspace data using");
        structurizrClient.uses(api, "Uploads software architecture models using");

        Container database = structurizr.addContainer(DATABASE, "Stores information about users, workspaces, etc.", "MySQL");
        database.addTags(DATABASE_TAG);
        webApplication.uses(database, "Reads and writes user and workspace information using");
        api.uses(database, "Reads and writes workspace information using");

        Container httpSessionStore = structurizr.addContainer(HTTP_SESSION_STORE, "Stores HTTP session information.", "Redis 3.0.x");
        httpSessionStore.addTags(DATABASE_TAG);
        webApplication.uses(httpSessionStore, "Reads and writes HTTP session information using");

        pingdom.uses(webApplication, "Monitors");
        webApplication.uses(sendGrid, "Sends e-mails using");

        webBrowser.uses(remoteApi, "Reads and writes workspace information using");

        ViewSet views = workspace.getViews();
        ContainerView containerView = views.createContainerView(structurizr, "Containers", "The container diagram for Structurizr.");
        containerView.addAllElements();
        containerView.remove(remoteApi);

        writeToFile(workspace);
    }

}
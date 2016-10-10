package com.structurizr.example.structurizr;

import com.structurizr.Workspace;
import com.structurizr.model.Model;
import com.structurizr.model.Person;
import com.structurizr.model.SoftwareSystem;
import com.structurizr.view.SystemContextView;
import com.structurizr.view.ViewSet;

/**
 * Creates the system context model and view (people and software systems).
 */
public class SystemContext extends AbstractStructurizrWorkspace {

    public static void main(String[] args) throws Exception {
        new SystemContext().run();
    }

    void run() throws Exception {
        Workspace workspace = new Workspace("Structurizr", "This is the software architecture model for Structurizr.");
        Model model = workspace.getModel();

        SoftwareSystem structurizr = model.addSoftwareSystem(STRUCTURIZR, "Simple, versionable, up-to-date, scalable software architecture models.");
        structurizr.addTags(STRUCTURIZR);

        Person anonymousUser = model.addPerson(ANONYMOUS_USER, "An anonymous user on the Internet.");
        anonymousUser.uses(structurizr, "Uses via web");

        Person authenticatedUser = model.addPerson(AUTHENTICATED_USER, "An authenticated user.");
        authenticatedUser.uses(structurizr, "Uses via web");

        SoftwareSystem structurizrClient = model.addSoftwareSystem(STRUCTURIZR_CLIENT, "A program (or collection of programs) that create a software architecture model using code.");
        structurizrClient.uses(structurizr, "Uses the API");

        SoftwareSystem pingdom = model.addSoftwareSystem(PINGDOM, "Uptime and performance monitoring service.");
        pingdom.uses(structurizr, "Monitors");

        SoftwareSystem sendGrid = model.addSoftwareSystem(SENDGRID, "Email delivery as a service.");
        structurizr.uses(sendGrid, "Sends e-mails using");
        sendGrid.delivers(authenticatedUser, "Sends e-mails to");

        SoftwareSystem taxamo = model.addSoftwareSystem(TAXAMO, "Payment and tax services.");
        structurizr.uses(taxamo, "Takes payments using");

        SoftwareSystem braintree = model.addSoftwareSystem(BRAINTREE, "Online payment provider");
        taxamo.uses(braintree, "Delegates payment processing to");

        SoftwareSystem onPremisesAPi = model.addSoftwareSystem(ON_PREMISES_API, "An on-premises Structurizr API server, hosted outside of the Structurizr cloud environment.");

        ViewSet views = workspace.getViews();
        SystemContextView systemContextView = views.createSystemContextView(structurizr, "Context", "The System Context view for Structurizr.");
        systemContextView.addAllElements();
        systemContextView.remove(onPremisesAPi);

        writeToFile(workspace);
    }

}
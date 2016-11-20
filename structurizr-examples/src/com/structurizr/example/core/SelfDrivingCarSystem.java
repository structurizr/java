package com.structurizr.example.core;

import com.structurizr.Workspace;
import com.structurizr.api.StructurizrClient;
import com.structurizr.model.*;
import com.structurizr.view.ContainerView;
import com.structurizr.view.PaperSize;
import com.structurizr.view.SystemContextView;
import com.structurizr.view.ViewSet;

/**
 * An example of a software architecture model to describe an initial solution to Nate Schutta's
 * case study in his "Modeling for Architects" workshop at the O'Reilly Software Architecture
 * conference in Boston, United States during March 2015.
 *
 *  - http://softwarearchitecturecon.com/sa2015/public/schedule/detail/40246
 *  - https://www.structurizr.com/public/271
 */
public class SelfDrivingCarSystem {

    public static void main(String[] args) throws Exception {
        Workspace workspace = new Workspace("Self-Driving Car System", "Case study for Nate Schutta's modeling workshop at the O'Reilly Software Architecture Conference 2015");
        Model model = workspace.getModel();

        // create the model
        SoftwareSystem selfDrivingCarSystem = model.addSoftwareSystem("Self-Driving Car System", "Central system for storing information about self-driving cars.");

        SoftwareSystem selfDrivingCar = model.addSoftwareSystem("Self-Driving Car", "Our own self-driving cars");
        selfDrivingCar.uses(selfDrivingCarSystem, "Sends VIN and status information (battery level, health of engine, location, etc) to");

        Person carOwner = model.addPerson("Car Owner", "The owner of a self-driving car");
        carOwner.uses(selfDrivingCarSystem, "Gets information from and makes requests (e.g. summon car) to");
        carOwner.uses(selfDrivingCar, "Owns and drives");

        Person auditor = model.addPerson("Auditor", "Audits access to customer data");
        auditor.uses(selfDrivingCarSystem, "Views audit information about customer data access from");

        Person dataScientist = model.addPerson("Data Scientist", "Mines self-driving car data");
        dataScientist.uses(selfDrivingCarSystem, "Mines and produces reports about self-driving cars and their usage with");

        Person developer = model.addPerson("Developer", "Builds and maintains the software used in the self-driving cars");
        developer.uses(selfDrivingCarSystem, "Send software updates for cars to");
        selfDrivingCarSystem.uses(selfDrivingCar, "Sends software updates and requests (e.g. drive to location) to");

        SoftwareSystem activeDirectory = model.addSoftwareSystem("Active Directory", "Provides enterprise-wide authorisationa and authentication services");
        selfDrivingCarSystem.uses(activeDirectory, "uses");

        Container mobileApp = selfDrivingCarSystem.addContainer("Mobile App", "Allows car owners to view information about and make requests to their car", "Apple iOS, Android and Windows Phone");
        carOwner.uses(mobileApp, "Views information from and makes requests to");
        Container browser = selfDrivingCarSystem.addContainer("Web Browser", "Allows car owners to view information about and make requests to their car", "Apple iOS, Android and Windows Phone");
        carOwner.uses(browser, "Views information from and makes requests to");
        Container webApp = selfDrivingCarSystem.addContainer("Web Application", "Hosts the browser-based web application", "Apache Tomcat");
        browser.uses(webApp, "uses");

        Container apiServer = selfDrivingCarSystem.addContainer("API Server", "Provides an API to get information from and make requests to cars", "Node.js");
        mobileApp.uses(apiServer, "uses [JSON/HTTPS]");
        browser.uses(apiServer, "uses [JSON/HTTPS]");

        Container customerService = selfDrivingCarSystem.addContainer("Customer Service", "Provides information and behaviour related to customers", "Ruby");
        Container selfDrivingCarService = selfDrivingCarSystem.addContainer("Self-Driving Service", "Provides information and behaviour related to self-driving cars", "Scala");

        Container coreDataStore = selfDrivingCarSystem.addContainer("Core Data Store", "Stores all data relating to self-driving cars and their owners", "MySQL");
        Container dataReporting = selfDrivingCarSystem.addContainer("Data Reporting", "Allows users to report and mine data", "Hadoop");
        dataScientist.uses(dataReporting, "Does science with data from");
        auditor.uses(dataReporting, "Looks at audit information from");
        developer.uses(selfDrivingCarService, "Pushes updates to");
        selfDrivingCarService.uses(selfDrivingCar, "Pushes updates to");

        selfDrivingCarService.uses(dataReporting, "Sends events to");
        customerService.uses(dataReporting, "Sends events to");

        selfDrivingCarService.uses(coreDataStore, "Reads from and writes to");
        customerService.uses(coreDataStore, "Reads from and writes to");

        apiServer.uses(selfDrivingCarService, "uses [JSON/HTTPS]");
        apiServer.uses(customerService, "uses [JSON/HTTPS]");

        selfDrivingCar.uses(selfDrivingCarService, "Sends VIN and status information (battery level, health of engine, location, etc) to");
        dataReporting.uses(activeDirectory, "uses");

        // create some views
        ViewSet viewSet = workspace.getViews();
        SystemContextView contextView = viewSet.createSystemContextView(selfDrivingCarSystem, "context", null);
        contextView.setPaperSize(PaperSize.Slide_4_3);
        contextView.addAllElements();

        ContainerView containerView = viewSet.createContainerView(selfDrivingCarSystem, "containers", null);
        containerView.setPaperSize(PaperSize.A3_Landscape);
        containerView.addAllElements();

        // tag and style some elements
        selfDrivingCarSystem.addTags("System Under Construction");
        viewSet.getConfiguration().getStyles().addElementStyle(Tags.ELEMENT).width(600).height(450).fontSize(40);
        viewSet.getConfiguration().getStyles().addElementStyle("System Under Construction").background("#041f37").color("#ffffff");
        viewSet.getConfiguration().getStyles().addElementStyle(Tags.SOFTWARE_SYSTEM).background("#2a4e6e").color("#ffffff");
        viewSet.getConfiguration().getStyles().addElementStyle(Tags.PERSON).background("#728da5").color("#ffffff");
        viewSet.getConfiguration().getStyles().addRelationshipStyle(Tags.RELATIONSHIP).thickness(5).fontSize(40).width(500);
        contextView.setPaperSize(PaperSize.Slide_4_3);

        // upload it to structurizr.com
        StructurizrClient structurizrClient = new StructurizrClient("key", "secret");
        structurizrClient.putWorkspace(271, workspace);
    }

}
package com.structurizr.example;

import com.structurizr.Workspace;
import com.structurizr.api.StructurizrClient;
import com.structurizr.model.*;
import com.structurizr.view.*;

/**
 * A simple example of how to use filtered views to show "before" and "after" views of a software system.
 *
 * You can see the live diagrams at https://structurizr.com/public/19911
 */
public class FilteredViews {

    public static void main(String[] args) throws Exception {
        Workspace workspace = new Workspace("Filtered Views", "A simple example of using filtered views.");
        Model model = workspace.getModel();

        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "A description of the software system.");
        softwareSystem.addTags("Software System under construction");
        Person user = model.addPerson("User", "A description of the user.");
        user.uses(softwareSystem, "Uses");

        SoftwareSystem softwareSystemA = model.addSoftwareSystem("Software System A", "A description of software system A.");
        softwareSystemA.addTags("CurrentState");

        SoftwareSystem softwareSystemB = model.addSoftwareSystem("Software System B", "A description of software system B.");
        softwareSystemB.addTags("FutureState");

        SoftwareSystem softwareSystemC = model.addSoftwareSystem("Software System C", "A description of software system B.");
        softwareSystemC.uses(softwareSystem, "Sends data to").addTags("CurrentState");
        softwareSystemC.uses(softwareSystem, "Sends even more data to").addTags("FutureState");

        softwareSystem.uses(softwareSystemA, "Gets data from");
        softwareSystem.uses(softwareSystemB, "Gets even more data from");

        // tag and style some elements
        ViewSet views = workspace.getViews();
        Styles styles = views.getConfiguration().getStyles();

        styles.addElementStyle(Tags.ELEMENT).color("#ffffff").fontSize(34);
        styles.addElementStyle("Software System under construction").background("#445200");
        styles.addElementStyle(Tags.SOFTWARE_SYSTEM).width(650).height(400).background("#91a437").shape(Shape.RoundedBox);
        styles.addElementStyle(Tags.PERSON).width(550).background("#6a7b15").shape(Shape.Person);

        // create some views
        SystemContextView contextView = views.createSystemContextView(softwareSystem, "Context", "An example System Context diagram.");
        contextView.addAllElements();

        views.createFilteredView(contextView, "CurrentStateSystemContext", "The current system context", FilterMode.Exclude, "FutureState");
        views.createFilteredView(contextView, "FutureStateSystemContext", "The future state system context after Software System B is live.", FilterMode.Exclude, "CurrentState");

        StructurizrClient structurizrClient = new StructurizrClient("key", "secret");
        structurizrClient.putWorkspace(19911, workspace);

    }

}

package com.structurizr.example.core;

import com.structurizr.Workspace;
import com.structurizr.api.StructurizrClient;
import com.structurizr.model.Model;
import com.structurizr.model.Person;
import com.structurizr.model.SoftwareSystem;
import com.structurizr.model.Tags;
import com.structurizr.view.Styles;
import com.structurizr.view.SystemContextView;
import com.structurizr.view.ViewSet;

public class GettingStarted {

    public static void main(String[] args) throws Exception {
        Workspace workspace = new Workspace("My model", "This is a model of my software system.");
        Model model = workspace.getModel();

        Person user = model.addPerson("User", "A user of my software system.");
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "My software system.");
        user.uses(softwareSystem, "Uses");

        ViewSet viewSet = workspace.getViews();
        SystemContextView contextView = viewSet.createContextView(softwareSystem);
        contextView.addAllSoftwareSystems();
        contextView.addAllPeople();

        Styles styles = viewSet.getConfiguration().getStyles();
        styles.addElementStyle(Tags.SOFTWARE_SYSTEM).background("#a4b7c9").color("#000000");
        styles.addElementStyle(Tags.PERSON).background("#728da5").color("#ffffff");

        StructurizrClient structurizrClient = new StructurizrClient("key", "secret");
        structurizrClient.putWorkspace(1234, workspace);
    }

}

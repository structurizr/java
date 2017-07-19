package com.structurizr.example;

import com.structurizr.Workspace;
import com.structurizr.api.StructurizrClient;
import com.structurizr.encryption.AesEncryptionStrategy;
import com.structurizr.model.Model;
import com.structurizr.model.Person;
import com.structurizr.model.SoftwareSystem;
import com.structurizr.model.Tags;
import com.structurizr.view.Shape;
import com.structurizr.view.Styles;
import com.structurizr.view.SystemContextView;
import com.structurizr.view.ViewSet;

/**
 * This is an example of how to use client-side encryption.
 *
 * You can see the workspace online at https://structurizr.com/share/41
 * (the passphrase is "password")
 */
public class ClientSideEncryption {

    private static final long WORKSPACE_ID = 41;
    private static final String API_KEY = "key";
    private static final String API_SECRET = "secret";

    public static void main(String[] args) throws Exception {
        Workspace workspace = new Workspace("Client-side encrypted workspace", "This is a client-side encrypted workspace. The passphrase is 'password'.");
        Model model = workspace.getModel();

        Person user = model.addPerson("User", "A user of my software system.");
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "My software system.");
        user.uses(softwareSystem, "Uses");

        ViewSet views = workspace.getViews();
        SystemContextView contextView = views.createSystemContextView(softwareSystem, "SystemContext", "An example of a System Context diagram.");
        contextView.addAllSoftwareSystems();
        contextView.addAllPeople();

        Styles styles = views.getConfiguration().getStyles();
        styles.addElementStyle(Tags.SOFTWARE_SYSTEM).background("#d34407").color("#ffffff");
        styles.addElementStyle(Tags.PERSON).background("#f86628").color("#ffffff").shape(Shape.Person);

        StructurizrClient structurizrClient = new StructurizrClient(API_KEY, API_SECRET);
        structurizrClient.setEncryptionStrategy(new AesEncryptionStrategy("password"));
        structurizrClient.putWorkspace(WORKSPACE_ID, workspace);
    }

}
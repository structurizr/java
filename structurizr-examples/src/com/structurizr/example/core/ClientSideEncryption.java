package com.structurizr.example.core;

import com.structurizr.Workspace;
import com.structurizr.api.StructurizrClient;
import com.structurizr.documentation.Format;
import com.structurizr.documentation.StructurizrDocumentation;
import com.structurizr.encryption.AesEncryptionStrategy;
import com.structurizr.encryption.EncryptionStrategy;
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
 * (the passphrase is "password").
 */
public class ClientSideEncryption {

    private static final long WORKSPACE_ID = 41;
    private static final String API_KEY = "0ea99dfa-95f2-4adf-a78d-ea14c1949293";
    private static final String API_SECRET = "4c23f518-721f-462e-9087-d98efb95c8d5";

    public static void main(String[] args) throws Exception {
        Workspace workspace = new Workspace("Client-side encrypted workspace", "This is a client-side encrypted workspace. The passphrase is 'password'.");
        Model model = workspace.getModel();

        Person user = model.addPerson("User", "A user of my software system.");
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "My software system.");
        user.uses(softwareSystem, "Uses");

        ViewSet viewSet = workspace.getViews();
        SystemContextView contextView = viewSet.createSystemContextView(softwareSystem, "SystemContext", "An example of a System Context diagram.");
        contextView.addAllSoftwareSystems();
        contextView.addAllPeople();

        StructurizrDocumentation documentation = new StructurizrDocumentation(workspace);
        documentation.addContextSection(softwareSystem, Format.Markdown, "Here is some context about the software system...\n\n![](embed:SystemContext)");
        documentation.addQualityAttributesSection(softwareSystem, Format.Markdown, "Here is some information about the quality attributes...");
        documentation.addConstraintsSection(softwareSystem, Format.Markdown, "Here is some information about the constraints...");
        documentation.addPrinciplesSection(softwareSystem, Format.Markdown, "Here is some information about the principles...");

        Styles styles = viewSet.getConfiguration().getStyles();
        styles.addElementStyle(Tags.SOFTWARE_SYSTEM).background("#d34407").color("#ffffff");
        styles.addElementStyle(Tags.PERSON).background("#f86628").color("#ffffff").shape(Shape.Person);

        EncryptionStrategy encryptionStrategy = new AesEncryptionStrategy("password");

        StructurizrClient structurizrClient = new StructurizrClient(API_KEY, API_SECRET);
        structurizrClient.setEncryptionStrategy(encryptionStrategy);
        structurizrClient.putWorkspace(WORKSPACE_ID, workspace);
    }

}
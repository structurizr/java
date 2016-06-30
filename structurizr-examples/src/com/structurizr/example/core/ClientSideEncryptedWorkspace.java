package com.structurizr.example.core;

import com.structurizr.Workspace;
import com.structurizr.api.StructurizrClient;
import com.structurizr.encryption.AesEncryptionStrategy;
import com.structurizr.encryption.EncryptionStrategy;
import com.structurizr.model.Model;
import com.structurizr.model.Person;
import com.structurizr.model.SoftwareSystem;
import com.structurizr.model.Tags;
import com.structurizr.view.Styles;
import com.structurizr.view.SystemContextView;
import com.structurizr.view.ViewSet;

/**
 * This is an example client-side encrypted workspace. You can see it in your web browser
 * at https://structurizr.com/public/41 (the passphrase is "password").
 */
public class ClientSideEncryptedWorkspace {

    public static void main(String[] args) throws Exception {
        Workspace workspace = new Workspace("Client-side encrypted workspace", "This is a client-side encrypted workspace. The passphrase is 'password'.");
        Model model = workspace.getModel();

        Person user = model.addPerson("User", "A user of my software system.");
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "My software system.");
        user.uses(softwareSystem, "Uses");

        ViewSet viewSet = workspace.getViews();
        SystemContextView contextView = viewSet.createSystemContextView(softwareSystem, "context", "A simple example of a client-side encrypted workspace.");
        contextView.addAllSoftwareSystems();
        contextView.addAllPeople();

        Styles styles = viewSet.getConfiguration().getStyles();
        styles.addElementStyle(Tags.SOFTWARE_SYSTEM).background("#d34407").color("#ffffff");
        styles.addElementStyle(Tags.PERSON).background("#f86628").color("#ffffff");

        EncryptionStrategy encryptionStrategy = new AesEncryptionStrategy("password");

        StructurizrClient structurizrClient = new StructurizrClient("key", "secret");
        structurizrClient.setEncryptionStrategy(encryptionStrategy);
        structurizrClient.mergeWorkspace(41, workspace);
    }

}

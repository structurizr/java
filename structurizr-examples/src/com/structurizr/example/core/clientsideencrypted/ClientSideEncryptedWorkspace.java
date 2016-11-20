package com.structurizr.example.core.clientsideencrypted;

import com.structurizr.Workspace;
import com.structurizr.api.StructurizrClient;
import com.structurizr.documentation.Documentation;
import com.structurizr.documentation.Format;
import com.structurizr.documentation.Type;
import com.structurizr.encryption.AesEncryptionStrategy;
import com.structurizr.encryption.EncryptionStrategy;
import com.structurizr.model.Model;
import com.structurizr.model.Person;
import com.structurizr.model.SoftwareSystem;
import com.structurizr.model.Tags;
import com.structurizr.view.Styles;
import com.structurizr.view.SystemContextView;
import com.structurizr.view.ViewSet;

import java.io.File;

/**
 * This is an example client-side encrypted workspace, which includes diagrams
 * and documentation.
 *
 * You can see the workspace online at https://structurizr.com/public/41
 * (the passphrase is "password").
 */
public class ClientSideEncryptedWorkspace {

    public static void main(String[] args) throws Exception {
        Workspace workspace = new Workspace("Client-side encrypted workspace", "This is a client-side encrypted workspace. The passphrase is 'password'.");
        Model model = workspace.getModel();

        Person user = model.addPerson("User", "A user of my software system.");
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "My software system.");
        user.uses(softwareSystem, "Uses");

        ViewSet viewSet = workspace.getViews();
        SystemContextView contextView = viewSet.createSystemContextView(softwareSystem, "Context", "A simple example of a client-side encrypted workspace.");
        contextView.addAllSoftwareSystems();
        contextView.addAllPeople();

        Documentation documentation = workspace.getDocumentation();
        File documentationRoot = new File("./structurizr-examples/src/com/structurizr/example/core/clientsideencrypted");
        documentation.add(softwareSystem, Type.Context, Format.Markdown, new File(documentationRoot, "context.md"));
        documentation.add(softwareSystem, Type.QualityAttributes, Format.Markdown, new File(documentationRoot, "quality-attributes.md"));
        documentation.add(softwareSystem, Type.Constraints, Format.Markdown, new File(documentationRoot, "constraints.md"));
        documentation.add(softwareSystem, Type.Principles, Format.Markdown, new File(documentationRoot, "principles.md"));

        Styles styles = viewSet.getConfiguration().getStyles();
        styles.addElementStyle(Tags.SOFTWARE_SYSTEM).background("#d34407").color("#ffffff");
        styles.addElementStyle(Tags.PERSON).background("#f86628").color("#ffffff");

        EncryptionStrategy encryptionStrategy = new AesEncryptionStrategy("password");

        StructurizrClient structurizrClient = new StructurizrClient("key", "secret");
        structurizrClient.setEncryptionStrategy(encryptionStrategy);
        structurizrClient.putWorkspace(41, workspace);
    }

}

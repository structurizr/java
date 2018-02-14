package com.structurizr.example;

import com.structurizr.Workspace;
import com.structurizr.api.StructurizrClient;
import com.structurizr.documentation.AutomaticDocumentationTemplate;
import com.structurizr.model.Model;
import com.structurizr.model.Person;
import com.structurizr.model.SoftwareSystem;
import com.structurizr.model.Tags;
import com.structurizr.view.Shape;
import com.structurizr.view.Styles;
import com.structurizr.view.SystemContextView;
import com.structurizr.view.ViewSet;

import java.io.File;

/**
 * An empty software architecture document based upon the Structurizr template,
 * created with the automatic document template.
 *
 * See https://structurizr.com/share/35971/documentation for the live version.
 */
public class AutomaticDocumentationTemplateExample {

    private static final long WORKSPACE_ID = 35971;
    private static final String API_KEY = "key";
    private static final String API_SECRET = "secret";

    public static void main(String[] args) throws Exception {
        Workspace workspace = new Workspace("Documentation - Automatic", "An empty software architecture document using the Structurizr template.");
        Model model = workspace.getModel();
        ViewSet views = workspace.getViews();

        Person user = model.addPerson("User", "A user of my software system.");
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "My software system.");
        user.uses(softwareSystem, "Uses");

        SystemContextView contextView = views.createSystemContextView(softwareSystem, "SystemContext", "An example of a System Context diagram.");
        contextView.addAllSoftwareSystems();
        contextView.addAllPeople();

        Styles styles = views.getConfiguration().getStyles();
        styles.addElementStyle(Tags.PERSON).shape(Shape.Person);

        // this directory includes a mix of Markdown and AsciiDoc files
        File documentationRoot = new File("./structurizr-examples/src/com/structurizr/example/documentation/automatic");

        AutomaticDocumentationTemplate template = new AutomaticDocumentationTemplate(workspace);
        template.addSections(softwareSystem, documentationRoot);

        StructurizrClient structurizrClient = new StructurizrClient(API_KEY, API_SECRET);
        structurizrClient.putWorkspace(WORKSPACE_ID, workspace);
    }

}
package com.structurizr.example.core;

import com.structurizr.Workspace;
import com.structurizr.api.StructurizrClient;
import com.structurizr.documentation.Format;
import com.structurizr.documentation.StructurizrDocumentation;
import com.structurizr.model.Model;
import com.structurizr.model.Person;
import com.structurizr.model.SoftwareSystem;
import com.structurizr.model.Tags;
import com.structurizr.util.ImageUtils;
import com.structurizr.view.*;

import java.io.File;

/**
 * <p>
 *   This is a simple example that illustrates the corporate branding features of Structurizr, including:
 * </p>
 *
 * <ul>
 *   <li>A colour scheme, which is applied to model elements and documentation navigation.</li>
 *   <li>A logo, which is included in diagrams and documentation.</li>
 * </ul>
 *
 * <p>
 *   You can see the live workspace at https://structurizr.com/share/35031
 * </p>
 */
public class CorporateBranding {

    public static void main(String[] args) throws Exception {
        Workspace workspace = new Workspace("Corporate branding", "An example of the corporate branding features.");
        Model model = workspace.getModel();
        ViewSet views = workspace.getViews();

        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "A software system.");
        Person user = model.addPerson("User", "A user.");
        user.uses(softwareSystem, "uses");

        SystemContextView systemContextView = views.createSystemContextView(softwareSystem, "systemContext", "This is an example system context diagram.");
        systemContextView.addAllElements();

        StructurizrDocumentation documentation = new StructurizrDocumentation(workspace);
        documentation.addContextSection(softwareSystem, Format.Markdown, "Here is a diagram... ![](embed:systemContext)");

        Branding branding = views.getConfiguration().getBranding();
        branding.setColor1(new ColorPair("#02172C", "#ffffff"));
        branding.setColor2(new ColorPair("#08427B", "#ffffff"));
        branding.setColor3(new ColorPair("#1168BD", "#ffffff"));
        branding.setColor4(new ColorPair("#438DD5", "#ffffff"));
        branding.setColor5(new ColorPair("#85BBF0", "#ffffff"));
        branding.setLogo(ImageUtils.getImageAsDataUri(new File("./structurizr-examples/src/com/structurizr/example/core/structurizr.png")));

        views.getConfiguration().getStyles().addElementStyle(Tags.PERSON).shape(Shape.Person);

        StructurizrClient structurizrClient = new StructurizrClient("key", "secret");
        structurizrClient.putWorkspace(35031, workspace);
    }

}

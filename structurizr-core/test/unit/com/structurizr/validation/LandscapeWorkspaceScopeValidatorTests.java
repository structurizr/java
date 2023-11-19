package com.structurizr.validation;

import com.structurizr.Workspace;
import com.structurizr.documentation.Format;
import com.structurizr.documentation.Section;
import com.structurizr.validation.LandscapeWorkspaceScopeValidator;
import com.structurizr.validation.WorkspaceScopeValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class LandscapeWorkspaceScopeValidatorTests {

    private final LandscapeWorkspaceScopeValidator validator = new LandscapeWorkspaceScopeValidator();

    @Test
    void validate() throws Exception {
        Workspace workspace = new Workspace("Name", "Description");
        validator.validate(workspace);

        workspace.getModel().addPerson("User");
        validator.validate(workspace);

        workspace.getModel().addSoftwareSystem("A");
        validator.validate(workspace);

        workspace.getModel().addSoftwareSystem("B");
        validator.validate(workspace);
    }

    @Test
    void validate_ThrowsAnException_WhenContainersAreDefined() {
        Workspace workspace = new Workspace("Name", "Description");
        workspace.getModel().addSoftwareSystem("A").addContainer("AA");
        try {
            validator.validate(workspace);
            fail();
        } catch (WorkspaceScopeValidationException e) {
            assertEquals("Workspace is landscape scoped, but the software system named A has containers.", e.getMessage());
        }
    }

    @Test
    void validate_ThrowsAnException_WhenSoftwareSystemDocumentationIsDefined() {
        Workspace workspace = new Workspace("Name", "Description");
        workspace.getModel().addSoftwareSystem("A").getDocumentation().addSection(new Section(Format.Markdown, "## Heading 1"));
        try {
            validator.validate(workspace);
            fail();
        } catch (WorkspaceScopeValidationException e) {
            assertEquals("Workspace is landscape scoped, but the software system named A has documentation.", e.getMessage());
        }
    }

}

package com.structurizr.validation;

import com.structurizr.Workspace;
import com.structurizr.documentation.Format;
import com.structurizr.documentation.Section;
import com.structurizr.model.SoftwareSystem;
import com.structurizr.validation.SoftwareSystemWorkspaceScopeValidator;
import com.structurizr.validation.WorkspaceScopeValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class SoftwareSystemWorkspaceScopeValidatorTests {

    private final SoftwareSystemWorkspaceScopeValidator validator = new SoftwareSystemWorkspaceScopeValidator();

    @Test
    void validate() throws Exception {
        Workspace workspace = new Workspace("Name", "Description");
        validator.validate(workspace);

        workspace.getModel().addPerson("User");
        validator.validate(workspace);

        SoftwareSystem a = workspace.getModel().addSoftwareSystem("A");
        a.addContainer("AA");
        a.getDocumentation().addSection(new Section(Format.Markdown, "## Heading 1"));
        validator.validate(workspace);

        workspace.getModel().addSoftwareSystem("B");
        validator.validate(workspace);
    }

    @Test
    void validate_ThrowsAnException_WhenMultipleSoftwareSystemsDefineContainers() {
        Workspace workspace = new Workspace("Name", "Description");
        workspace.getModel().addSoftwareSystem("A").addContainer("AA");
        workspace.getModel().addSoftwareSystem("B").addContainer("BB");
        try {
            validator.validate(workspace);
            fail();
        } catch (WorkspaceScopeValidationException e) {
            assertEquals("Workspace is software system scoped, but multiple software systems have containers and/or documentation defined.", e.getMessage());
        }
    }

    @Test
    void validate_ThrowsAnException_WhenMultipleSoftwareSystemsDefineDocumentation() {
        Workspace workspace = new Workspace("Name", "Description");
        workspace.getModel().addSoftwareSystem("A").getDocumentation().addSection(new Section(Format.Markdown, "## Heading 1"));
        workspace.getModel().addSoftwareSystem("B").getDocumentation().addSection(new Section(Format.Markdown, "## Heading 1"));
        try {
            validator.validate(workspace);
            fail();
        } catch (WorkspaceScopeValidationException e) {
            assertEquals("Workspace is software system scoped, but multiple software systems have containers and/or documentation defined.", e.getMessage());
        }
    }

}
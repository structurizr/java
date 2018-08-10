package com.structurizr.documentation;

import com.structurizr.AbstractWorkspaceTestBase;
import com.structurizr.Workspace;
import com.structurizr.model.SoftwareSystem;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class DocumentationTests extends AbstractWorkspaceTestBase {

    private SoftwareSystem softwareSystem;

    @Before
    public void setUp() {
        Workspace workspace = new Workspace("Name", "Description");
        softwareSystem = workspace.getModel().addSoftwareSystem("Software System", "Description");
    }

    @Test
    public void test_addSection_ThrowsAnException_WhenTheRelatedElementIsNotPresentInTheAssociatedModel() {
        try {
            new Workspace("", "").getDocumentation().addSection(softwareSystem, "Type", Format.Markdown, "Content");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("The element named Software System does not exist in the model associated with this documentation.", iae.getMessage());
        }
    }

}
package com.structurizr.view;

import com.structurizr.AbstractWorkspaceTestBase;
import com.structurizr.model.Element;
import com.structurizr.model.Location;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ElementViewTests extends AbstractWorkspaceTestBase {

    @Test
    void test_copyLayoutInformationFrom_DoesNothing_WhenNullIsPassed() {
        Element element = model.addSoftwareSystem(Location.External, "SystemA", "");
        ElementView elementView = new ElementView(element);
        elementView.copyLayoutInformationFrom(null);
    }

    @Test
    void test_copyLayoutInformationFrom_CopiesXAndY_WhenANonNullElementViewIsPassed() {
        Element element = model.addSoftwareSystem(Location.External, "SystemA", "");
        ElementView elementView1 = new ElementView(element);
        assertEquals(0, elementView1.getX());
        assertEquals(0, elementView1.getY());

        ElementView elementView2 = new ElementView(element);
        elementView2.setX(123);
        elementView2.setY(456);

        elementView1.copyLayoutInformationFrom(elementView2);
        assertEquals(123, elementView1.getX());
        assertEquals(456, elementView1.getY());
    }

}

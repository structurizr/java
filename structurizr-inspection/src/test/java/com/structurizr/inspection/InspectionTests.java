package com.structurizr.inspection;

import com.structurizr.Workspace;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InspectionTests {

    @Test
    void generateTypes() {
        Workspace workspace = new Workspace("Name", "Description");
        List<String> types = new Inspection(workspace) {
            @Override
            protected String getType() {
                return "model.component.description";
            }
        }.generateTypes();

        assertEquals("structurizr.inspection.model.component.description", types.get(0));
        assertEquals("structurizr.inspection.model.component.*", types.get(1));
        assertEquals("structurizr.inspection.model.*", types.get(2));
        assertEquals("structurizr.inspection.*", types.get(3));
    }

}
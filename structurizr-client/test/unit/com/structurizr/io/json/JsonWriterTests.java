package com.structurizr.io.json;

import com.structurizr.Workspace;
import org.junit.Test;

import java.io.StringWriter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class JsonWriterTests {

    @Test
    public void test_write_ThrowsAnIllegalArgumentException_WhenANullWorkspaceIsSpecified() throws Exception {
        try {
            JsonWriter writer = new JsonWriter(true);
            writer.write(null, new StringWriter());
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Workspace cannot be null.", e.getMessage());
        }
    }

    @Test
    public void test_write_ThrowsAnIllegalArgumentException_WhenANullWriterIsSpecified() throws Exception {
        try {
            JsonWriter writer = new JsonWriter(true);
            Workspace workspace = new Workspace("Name", "Description");
            writer.write(workspace, null);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Writer cannot be null.", e.getMessage());
        }
    }

}

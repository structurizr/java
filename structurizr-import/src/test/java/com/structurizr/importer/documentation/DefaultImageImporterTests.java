package com.structurizr.importer.documentation;

import com.structurizr.Workspace;
import com.structurizr.documentation.Documentation;
import com.structurizr.documentation.Image;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class DefaultImageImporterTests {

    private Workspace workspace;
    private DefaultImageImporter imageImporter;

    @BeforeEach
    public void setUp() {
        workspace = new Workspace("Name", "Description");
        imageImporter = new DefaultImageImporter();
    }

    @Test
    public void test_importDocumentation_ThrowsAnException_WhenTheDocumentableIsNull() {
        try {
            imageImporter.importDocumentation(null, null);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("A workspace or software system must be specified.", e.getMessage());
        }
    }

    @Test
    public void test_importDocumentation_ThrowsAnException_WhenThePathIsNull() {
        try {
            imageImporter.importDocumentation(workspace, null);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A path must be specified.", iae.getMessage());
        }
    }

    @Test
    public void test_importDocumentation_ThrowsAnException_WhenThePathDoesNotExist() {
        try {
            imageImporter.importDocumentation(workspace, new File("./src/test/resources/java/com/structurizr/documentation/foo"));
            fail();
        } catch (IllegalArgumentException iae) {
            assertTrue(iae.getMessage().endsWith("foo does not exist."));
        }
    }

    @Test
    public void test_importDocumentation_DoesNothing_WhenThereAreNoImageFilesInThePath() {
        File directory = new File("./src/test/resources/docs/images/noimages");
        assertTrue(directory.exists());
        imageImporter.importDocumentation(workspace, directory);
        assertTrue(workspace.getDocumentation().getImages().isEmpty());
    }

    @Test
    public void test_importDocumentation_ThrowsAnException_WhenTheSpecifiedDirectoryIsNotAnImage() throws IOException {
        try {
            imageImporter.importDocumentation(workspace, new File("README.md"));
            fail();
        } catch (Exception e) {
            assertTrue(e.getMessage().endsWith("README.md is not a supported image file."));
        }
    }

    @Test
    public void test_importDocumentation_AddsAllImages_NonRecursively() {
        Documentation documentation = workspace.getDocumentation();
        assertTrue(documentation.getImages().isEmpty());

        imageImporter.importDocumentation(workspace, new File("./src/test/resources/docs/images/images"));

        Set<Image> images = documentation.getImages();
        assertEquals(4, documentation.getImages().size());

        Image png = documentation.getImages().stream().filter(i -> i.getName().equals("image.png")).findFirst().get();
        assertEquals("image/png", png.getType());
        assertTrue(png.getContent().startsWith("iVBORw0KGgoAAAANSUhEUgAAACAAAAAaCAYAAADWm14/AAAD"));
        assertTrue(images.contains(png));

        Image jpg = documentation.getImages().stream().filter(i -> i.getName().equals("image.jpg")).findFirst().get();
        assertEquals("image/jpeg", jpg.getType());
        assertEquals("/9j/4AAQSkZJRgABAgAAAQABAAD/2wBDAAgGBgcGBQgHBwcJCQgKDBQNDAsLDBkSEw8UHRofHh0aHBwgJC4nICIsIxwcKDcpLDAxNDQ0Hyc5PTgyPC4zNDL/2wBDAQkJCQwLDBgNDRgyIRwhMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjL/wAARCAAaACADASIAAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQAAAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEAAwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSExBhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElKU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3uLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3+Pn6/9oADAMBAAIRAxEAPwDxzUdRu9Vv5r69nea4mcu7uxJyTnv2GeBT49I1CWNXS1cqwyDkDP5mqLfdP0NeueF9Pt9Q1AR3Kb40h3begJ4HNerhqEal+boebiK0qdra3PNP7D1L/nzf/vpf8afbnVvD17BqEPnWs0UgZJVbHIOccHvjoete8/8ACP6R/wA+EP5V598R9Ot9PttttHsSRUbaOgIcDitZ4anytpszhXqcyTS1PNHVl3KykMMgg8c16n4b1q20+6W5dt8MkWwlCCR05x+Fc78UraC1+IGqx28EcKeYW2xoFGSTk4Hc1x5RSCSoz9KwoV/Zpu17m9ah7RpXtY99/wCEy0j+9N/3x/8AXrhfH+swavEotwf4I0U43Md2ScCvPPLT+4v5V2XwttLa5+IOlRz28UqeaG2yIGGQRg4NVPFrlaUfxFHC2km5bH//2Q==", jpg.getContent());
        assertTrue(images.contains(jpg));

        Image jpeg = documentation.getImages().stream().filter(i -> i.getName().equals("image.jpeg")).findFirst().get();
        assertEquals("image/jpeg", jpeg.getType());
        assertEquals("/9j/4AAQSkZJRgABAgAAAQABAAD/2wBDAAgGBgcGBQgHBwcJCQgKDBQNDAsLDBkSEw8UHRofHh0aHBwgJC4nICIsIxwcKDcpLDAxNDQ0Hyc5PTgyPC4zNDL/2wBDAQkJCQwLDBgNDRgyIRwhMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjL/wAARCAAaACADASIAAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQAAAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEAAwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSExBhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElKU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3uLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3+Pn6/9oADAMBAAIRAxEAPwDxzUdRu9Vv5r69nea4mcu7uxJyTnv2GeBT49I1CWNXS1cqwyDkDP5mqLfdP0NeueF9Pt9Q1AR3Kb40h3begJ4HNerhqEal+boebiK0qdra3PNP7D1L/nzf/vpf8afbnVvD17BqEPnWs0UgZJVbHIOccHvjoete8/8ACP6R/wA+EP5V598R9Ot9PttttHsSRUbaOgIcDitZ4anytpszhXqcyTS1PNHVl3KykMMgg8c16n4b1q20+6W5dt8MkWwlCCR05x+Fc78UraC1+IGqx28EcKeYW2xoFGSTk4Hc1x5RSCSoz9KwoV/Zpu17m9ah7RpXtY99/wCEy0j+9N/3x/8AXrhfH+swavEotwf4I0U43Md2ScCvPPLT+4v5V2XwttLa5+IOlRz28UqeaG2yIGGQRg4NVPFrlaUfxFHC2km5bH//2Q==", jpeg.getContent());
        assertTrue(images.contains(jpeg));

        Image gif = documentation.getImages().stream().filter(i -> i.getName().equals("image.gif")).findFirst().get();
        assertEquals("image/gif", gif.getType());
        assertEquals("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", gif.getContent());
        assertTrue(images.contains(gif));
    }

    @Test
    public void test_importDocumentation_AddsAllImages_Recursively() {
        Documentation documentation = workspace.getDocumentation();
        assertTrue(documentation.getImages().isEmpty());

        imageImporter.importDocumentation(workspace, new File("./src/test/resources/docs/images"));
        assertEquals(9, documentation.getImages().size());

        Image pngInDirectory = documentation.getImages().stream().filter(i -> i.getName().equals("image.png")).findFirst().get();
        assertEquals("image/png", pngInDirectory.getType());
        assertTrue(pngInDirectory.getContent().startsWith("iVBORw0KGgoAAAANSUhEUgAAACAAAAAaCAYAAADWm14/AAAD"));

        Image jpgInDirectory = documentation.getImages().stream().filter(i -> i.getName().equals("image.jpg")).findFirst().get();
        assertEquals("image/jpeg", jpgInDirectory.getType());
        assertEquals("/9j/4AAQSkZJRgABAgAAAQABAAD/2wBDAAgGBgcGBQgHBwcJCQgKDBQNDAsLDBkSEw8UHRofHh0aHBwgJC4nICIsIxwcKDcpLDAxNDQ0Hyc5PTgyPC4zNDL/2wBDAQkJCQwLDBgNDRgyIRwhMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjL/wAARCAAaACADASIAAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQAAAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEAAwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSExBhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElKU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3uLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3+Pn6/9oADAMBAAIRAxEAPwDxzUdRu9Vv5r69nea4mcu7uxJyTnv2GeBT49I1CWNXS1cqwyDkDP5mqLfdP0NeueF9Pt9Q1AR3Kb40h3begJ4HNerhqEal+boebiK0qdra3PNP7D1L/nzf/vpf8afbnVvD17BqEPnWs0UgZJVbHIOccHvjoete8/8ACP6R/wA+EP5V598R9Ot9PttttHsSRUbaOgIcDitZ4anytpszhXqcyTS1PNHVl3KykMMgg8c16n4b1q20+6W5dt8MkWwlCCR05x+Fc78UraC1+IGqx28EcKeYW2xoFGSTk4Hc1x5RSCSoz9KwoV/Zpu17m9ah7RpXtY99/wCEy0j+9N/3x/8AXrhfH+swavEotwf4I0U43Md2ScCvPPLT+4v5V2XwttLa5+IOlRz28UqeaG2yIGGQRg4NVPFrlaUfxFHC2km5bH//2Q==", jpgInDirectory.getContent());

        Image jpegInDirectory = documentation.getImages().stream().filter(i -> i.getName().equals("image.jpeg")).findFirst().get();
        assertEquals("image/jpeg", jpegInDirectory.getType());
        assertEquals("/9j/4AAQSkZJRgABAgAAAQABAAD/2wBDAAgGBgcGBQgHBwcJCQgKDBQNDAsLDBkSEw8UHRofHh0aHBwgJC4nICIsIxwcKDcpLDAxNDQ0Hyc5PTgyPC4zNDL/2wBDAQkJCQwLDBgNDRgyIRwhMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjL/wAARCAAaACADASIAAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQAAAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEAAwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSExBhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElKU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3uLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3+Pn6/9oADAMBAAIRAxEAPwDxzUdRu9Vv5r69nea4mcu7uxJyTnv2GeBT49I1CWNXS1cqwyDkDP5mqLfdP0NeueF9Pt9Q1AR3Kb40h3begJ4HNerhqEal+boebiK0qdra3PNP7D1L/nzf/vpf8afbnVvD17BqEPnWs0UgZJVbHIOccHvjoete8/8ACP6R/wA+EP5V598R9Ot9PttttHsSRUbaOgIcDitZ4anytpszhXqcyTS1PNHVl3KykMMgg8c16n4b1q20+6W5dt8MkWwlCCR05x+Fc78UraC1+IGqx28EcKeYW2xoFGSTk4Hc1x5RSCSoz9KwoV/Zpu17m9ah7RpXtY99/wCEy0j+9N/3x/8AXrhfH+swavEotwf4I0U43Md2ScCvPPLT+4v5V2XwttLa5+IOlRz28UqeaG2yIGGQRg4NVPFrlaUfxFHC2km5bH//2Q==", jpegInDirectory.getContent());

        Image gifInDirectory = documentation.getImages().stream().filter(i -> i.getName().equals("image.gif")).findFirst().get();
        assertEquals("image/gif", gifInDirectory.getType());
        assertEquals("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", gifInDirectory.getContent());

        Image svgInDirectory = documentation.getImages().stream().filter(i -> i.getName().equals("image.svg")).findFirst().get();
        assertEquals("image/svg+xml", svgInDirectory.getType());
        assertEquals("PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSIxMDAiIGhlaWdodD0iMTAwIj4KICA8Y2lyY2xlIGN4PSI1MCIgY3k9IjUwIiByPSI0MCIgc3Ryb2tlPSJibGFjayIgc3Ryb2tlLXdpZHRoPSIzIiBmaWxsPSJyZWQiIC8+Cjwvc3ZnPiA=", svgInDirectory.getContent());

        Image pngInSubDirectory = documentation.getImages().stream().filter(i -> i.getName().equals("images/image.png")).findFirst().get();
        assertEquals("image/png", pngInSubDirectory.getType());
        assertTrue(pngInSubDirectory.getContent().startsWith("iVBORw0KGgoAAAANSUhEUgAAACAAAAAaCAYAAADWm14/AAAD"));

        Image jpgInSubDirectory = documentation.getImages().stream().filter(i -> i.getName().equals("images/image.jpg")).findFirst().get();
        assertEquals("image/jpeg", jpgInSubDirectory.getType());
        assertEquals("/9j/4AAQSkZJRgABAgAAAQABAAD/2wBDAAgGBgcGBQgHBwcJCQgKDBQNDAsLDBkSEw8UHRofHh0aHBwgJC4nICIsIxwcKDcpLDAxNDQ0Hyc5PTgyPC4zNDL/2wBDAQkJCQwLDBgNDRgyIRwhMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjL/wAARCAAaACADASIAAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQAAAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEAAwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSExBhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElKU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3uLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3+Pn6/9oADAMBAAIRAxEAPwDxzUdRu9Vv5r69nea4mcu7uxJyTnv2GeBT49I1CWNXS1cqwyDkDP5mqLfdP0NeueF9Pt9Q1AR3Kb40h3begJ4HNerhqEal+boebiK0qdra3PNP7D1L/nzf/vpf8afbnVvD17BqEPnWs0UgZJVbHIOccHvjoete8/8ACP6R/wA+EP5V598R9Ot9PttttHsSRUbaOgIcDitZ4anytpszhXqcyTS1PNHVl3KykMMgg8c16n4b1q20+6W5dt8MkWwlCCR05x+Fc78UraC1+IGqx28EcKeYW2xoFGSTk4Hc1x5RSCSoz9KwoV/Zpu17m9ah7RpXtY99/wCEy0j+9N/3x/8AXrhfH+swavEotwf4I0U43Md2ScCvPPLT+4v5V2XwttLa5+IOlRz28UqeaG2yIGGQRg4NVPFrlaUfxFHC2km5bH//2Q==", jpgInSubDirectory.getContent());

        Image jpegInSubDirectory = documentation.getImages().stream().filter(i -> i.getName().equals("images/image.jpeg")).findFirst().get();
        assertEquals("image/jpeg", jpegInSubDirectory.getType());
        assertEquals("/9j/4AAQSkZJRgABAgAAAQABAAD/2wBDAAgGBgcGBQgHBwcJCQgKDBQNDAsLDBkSEw8UHRofHh0aHBwgJC4nICIsIxwcKDcpLDAxNDQ0Hyc5PTgyPC4zNDL/2wBDAQkJCQwLDBgNDRgyIRwhMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjL/wAARCAAaACADASIAAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQAAAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEAAwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSExBhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElKU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3uLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3+Pn6/9oADAMBAAIRAxEAPwDxzUdRu9Vv5r69nea4mcu7uxJyTnv2GeBT49I1CWNXS1cqwyDkDP5mqLfdP0NeueF9Pt9Q1AR3Kb40h3begJ4HNerhqEal+boebiK0qdra3PNP7D1L/nzf/vpf8afbnVvD17BqEPnWs0UgZJVbHIOccHvjoete8/8ACP6R/wA+EP5V598R9Ot9PttttHsSRUbaOgIcDitZ4anytpszhXqcyTS1PNHVl3KykMMgg8c16n4b1q20+6W5dt8MkWwlCCR05x+Fc78UraC1+IGqx28EcKeYW2xoFGSTk4Hc1x5RSCSoz9KwoV/Zpu17m9ah7RpXtY99/wCEy0j+9N/3x/8AXrhfH+swavEotwf4I0U43Md2ScCvPPLT+4v5V2XwttLa5+IOlRz28UqeaG2yIGGQRg4NVPFrlaUfxFHC2km5bH//2Q==", jpegInSubDirectory.getContent());

        Image gifInSubDirectory = documentation.getImages().stream().filter(i -> i.getName().equals("images/image.gif")).findFirst().get();
        assertEquals("image/gif", gifInSubDirectory.getType());
        assertEquals("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", gifInSubDirectory.getContent());
    }

    @Test
    public void test_importDocumentation_AddsASingleImage() {
        Documentation documentation = workspace.getDocumentation();
        assertTrue(documentation.getImages().isEmpty());

        imageImporter.importDocumentation(workspace, new File("./src/test/resources/docs/images/image.png"));
        assertEquals(1, documentation.getImages().size());

        Image png = documentation.getImages().stream().filter(i -> i.getName().equals("image.png")).findFirst().get();
        assertEquals("image/png", png.getType());
        assertTrue(png.getContent().startsWith("iVBORw0KGgoAAAANSUhEUgAAACAAAAAaCAYAAADWm14/AAAD"));
    }

    @Test
    public void test_importDocumentation_IgnoresHiddenFolders() throws Exception {
        Documentation documentation = workspace.getDocumentation();

        File tempDirectory = Files.createTempDirectory("test").toFile();
        File hiddenFolder = new File(tempDirectory, ".structurizr");
        hiddenFolder.mkdir();

        File source = new File("./src/test/resources/docs/images/images/image.png");
        File destination = new File(hiddenFolder, "image.png");
        Files.copy(source.toPath(), destination.toPath());
        assertTrue(destination.exists());

        imageImporter.importDocumentation(workspace, tempDirectory);
        assertEquals(0, documentation.getImages().size());
    }

}
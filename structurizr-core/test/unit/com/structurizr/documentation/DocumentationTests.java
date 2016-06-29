package com.structurizr.documentation;

import com.structurizr.Workspace;
import com.structurizr.model.Container;
import com.structurizr.model.SoftwareSystem;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

public class DocumentationTests {

    private SoftwareSystem softwareSystem;
    private Container container;
    private Documentation documentation;

    @Before
    public void setUp() {
        Workspace workspace = new Workspace("Name", "Description");
        softwareSystem = workspace.getModel().addSoftwareSystem("Name", "Description");
        container = softwareSystem.addContainer("Name", "Description", "Technology");

        documentation = workspace.getDocumentation();
    }

    @Test
    public void test_construction() {
        assertTrue(documentation.getSections().isEmpty());
        assertTrue(documentation.getImages().isEmpty());
    }

    @Test
    public void test_addWithContentForSoftwareSystem_AddsASectionWithTheSpecifiedContent_WhenThatSectionDoesNotExist() {
        documentation.add(softwareSystem, Type.Context, Format.Markdown, "Some Markdown content");
        Section section = documentation.add(softwareSystem, Type.FunctionalOverview, Format.Markdown, "Some more Markdown content");

        assertEquals(softwareSystem, section.getElement());
        assertEquals(softwareSystem.getId(), section.getElementId());
        assertEquals(Type.FunctionalOverview, section.getType());
        assertEquals(Format.Markdown, section.getFormat());
        assertEquals("Some more Markdown content", section.getContent());

        assertEquals(2, documentation.getSections().size());
        assertTrue(documentation.getSections().contains(section));
    }

    @Test
    public void test_addWithContentForSoftwareSystem_ThrowsAnException_WhenThatSectionAlreadyExists() {
        documentation.add(softwareSystem, Type.Context, Format.Markdown, "Some Markdown content");
        assertEquals(1, documentation.getSections().size());

        try {
            documentation.add(softwareSystem, Type.Context, Format.Markdown, "Some Markdown content");
            fail();
        } catch (IllegalArgumentException iae) {
            // this is the expected exception
            assertEquals(1, documentation.getSections().size());
        }
    }

    @Test
    public void test_addFromFileForSoftwareSystem_AddsASectionWithTheSpecifiedContent_WhenThatSectionDoesNotExist() throws IOException {
        documentation.add(softwareSystem, Type.Context, Format.Markdown, "Some Markdown content");

        File file = new File(".//test/unit/com/structurizr/documentation/example.md");
        Section section = documentation.add(softwareSystem, Type.FunctionalOverview, Format.Markdown, file);

        assertEquals(softwareSystem, section.getElement());
        assertEquals(softwareSystem.getId(), section.getElementId());
        assertEquals(Type.FunctionalOverview, section.getType());
        assertEquals(Format.Markdown, section.getFormat());
        assertEquals("## Heading\n" +
                "\n" +
                "Here is a paragraph.", section.getContent());

        assertEquals(2, documentation.getSections().size());
        assertTrue(documentation.getSections().contains(section));
    }

    @Test
    public void test_addFromFileForSoftwareSystem_ThrowsAnException_WhenThatSectionAlreadyExists() throws IOException {
        documentation.add(softwareSystem, Type.Context, Format.Markdown, "Some Markdown content");
        assertEquals(1, documentation.getSections().size());

        try {
            File file = new File(".//test/unit/com/structurizr/documentation/example.md");
            documentation.add(softwareSystem, Type.Context, Format.Markdown, file);
            fail();
        } catch (IllegalArgumentException iae) {
            // this is the expected exception
            assertEquals(1, documentation.getSections().size());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_addWithContentForSoftwareSystem_ThrowsAnException_WhenAContainerIsNotSpecifiedForTheComponentType() {
        documentation.add(softwareSystem, Type.Components, Format.Markdown, "Some Markdown content");
    }

    @Test
    public void test_addWithContentForContainer_AddsASectionWithTheSpecifiedContent_WhenThatSectionDoesNotExist() {
        documentation.add(softwareSystem, Type.Context, Format.Markdown, "Some Markdown content");
        Section section = documentation.add(container, Format.Markdown, "Some more Markdown content");

        assertEquals(container, section.getElement());
        assertEquals(container.getId(), section.getElementId());
        assertEquals(Type.Components, section.getType());
        assertEquals(Format.Markdown, section.getFormat());
        assertEquals("Some more Markdown content", section.getContent());

        assertEquals(2, documentation.getSections().size());
        assertTrue(documentation.getSections().contains(section));
    }

    @Test
    public void test_addWithContentForContainer_ThrowsAnException_WhenThatSectionAlreadyExists() {
        documentation.add(container, Format.Markdown, "Some Markdown content");
        assertEquals(1, documentation.getSections().size());

        try {
            documentation.add(container, Format.Markdown, "Some Markdown content");
            fail();
        } catch (IllegalArgumentException iae) {
            // this is the expected exception
            assertEquals(1, documentation.getSections().size());
        }
    }

    @Test
    public void test_addFromFileForContainer_AddsASectionWithTheSpecifiedContent_WhenThatSectionDoesNotExist() throws IOException {
        documentation.add(softwareSystem, Type.Context, Format.Markdown, "Some Markdown content");

        File file = new File(".//test/unit/com/structurizr/documentation/example.md");
        Section section = documentation.add(container, Format.Markdown, file);

        assertEquals(container, section.getElement());
        assertEquals(container.getId(), section.getElementId());
        assertEquals(Type.Components, section.getType());
        assertEquals(Format.Markdown, section.getFormat());
        assertEquals("## Heading\n" +
                "\n" +
                "Here is a paragraph.", section.getContent());

        assertEquals(2, documentation.getSections().size());
        assertTrue(documentation.getSections().contains(section));
    }

    @Test
    public void test_addFromFileForContainer_ThrowsAnException_WhenThatSectionAlreadyExists() throws IOException {
        documentation.add(container, Format.Markdown, "Some Markdown content");
        assertEquals(1, documentation.getSections().size());

        try {
            File file = new File(".//test/unit/com/structurizr/documentation/example.md");
            documentation.add(container, Format.Markdown, file);
            fail();
        } catch (IllegalArgumentException iae) {
            // this is the expected exception
            assertEquals(1, documentation.getSections().size());
        }
    }

    @Test
    public void test_addImages_DoesNothing_WhenThereAreNoImageFilesInTheSpecifiedDirectory() throws IOException {
        documentation.addImages(new File(".//test/unit/com/structurizr/"));
        assertTrue(documentation.getImages().isEmpty());
    }

    @Test
    public void test_addImages_ThrowsAnException_WhenTheSpecifiedDirectoryIsNull() throws IOException {
        try {
            documentation.addImages(null);
        } catch (IllegalArgumentException iae) {
            assertEquals("Directory path must not be null", iae.getMessage());
        }
    }

    @Test
    public void test_addImages_ThrowsAnException_WhenTheSpecifiedDirectoryIsNotADirectory() throws IOException {
        try {
            documentation.addImages(new File(".//test/unit/com/structurizr/documentation/example.md"));
        } catch (IllegalArgumentException iae) {
            assertTrue(iae.getMessage().endsWith("/structurizr-core/test/unit/com/structurizr/documentation/example.md is not a directory."));
        }
    }

    @Test
    public void test_addImages_ThrowsAnException_WhenTheSpecifiedDirectoryDoesNotExist() throws IOException {
        try {
            documentation.addImages(new File(".//test/unit/com/structurizr/document"));
        } catch (IllegalArgumentException iae) {
            assertTrue(iae.getMessage().endsWith("/structurizr-core/test/unit/com/structurizr/document does not exist."));
        }
    }

    @Test
    public void test_addImages_AddsAllImagesFromTheSpecifiedDirectory_WhenThereAreImageFilesInTheSpecifiedDirectory() throws IOException {
        assertTrue(documentation.getImages().isEmpty());
        documentation.addImages(new File(".//test/unit/com/structurizr/documentation"));
        assertEquals(4, documentation.getImages().size());

        Image png = documentation.getImages().stream().filter(i -> i.getName().equals("image.png")).findFirst().get();
        assertEquals("image/png", png.getType());
        assertEquals("iVBORw0KGgoAAAANSUhEUgAAACAAAAAaCAYAAADWm14/AAADx0lEQVR42sVXWU8TURS+g7YsbaEtpVAUNEhEA4iA8UkfNMFALFhRFo1xZVdcYqI+uKGyCGI0PrhL4gbqgy8gUgq0QNxiXP6CMTERDag88fJ57p0ZQqJ0Q+LDl5l2zj3fd8+5c84ZNjExgfaOx8grKAaLSUN4fDpCYtNmBWHkm8WlIze/CA8ePgLnZu0dT8AYA5MWCQPJmjZrArhvrY1EaJIFp+Be7yilH0lISFlJEUidNXIVzJqKxCXExRbA7thC19h0hHJVfpGnzxCKCOIK45z0H/Mr7LblCInPQkhcBmGZcg0Eyhrug/tS0iHOhPcdp4oFkt4CiXImhUdAiogMAlGQwsJlHwarIkKOOPO1c+5Ak7UD+k3XYNhyl3BPwf0p9/7gLvSFV6HJ3AZJZ56MBPNKboiFZvlWmCqcMO8bhrnGA/PeQZgIFkLcvkHYajmGpkUcPbeSnVhLPowVPdBklEKKtAmO6QXEZ4uQ6RxXiHRIiDBVumCs7EVMlQvacifYjudg27t9gGz2OMUao9jIEHQbLgvf/Ex4EZAJSSvBUHxHqDcRsbGqDzHVfcLhov1u5NW/haP5PTa2cHz4A/yZvekdMo8Mg+12IppE8Ajqi25BmksCbJneUkACQjUwlLTRIlmAVSFfdfwlTnaOoKn/p0809v3EOecoNrd+BNv5HLGUDj1tStIwHyn4Q4BL3j3t5PDjz2h2j+Nsz6hw7gtcSF33dyw+MIA5VW6YSoIQwMMXXuHCwtoBnOj8igbXmHBe3zvmE9yO26899QqsbACW0rZABdCpJwGachdSDrpxqmuEHP7wWwBHI9mvO/OaUtg/AwEVqoBvgQvo4wLe/CsBI/9bQKARGJ2pAM+kgMUHgotAw8zOgPwWRJCABQG+BeqryO3XBP8WTClEVAcOdXxCi2fcbwHnB37h9LNvSKrth7bKA6NfdUCUYkal+PZkKRaFqKwX2UeHcezpF7ErHloVjX+9HxPk9sZ3YLuUSqiWYuKYfiBRm1HBJdFAeDNSGxHb3SPuVx9/gZy61yK3f0MOgYed75yXYUul3FV1+RfFTBiWsMLLSMb7tXE+5qYWwljWJbdjpRVbSVBUzSBYuVvk1CvIJrTaQzsfktvxnk5ISx0IjV4IxlMw/VCqTkPRJGIjIuytNJRch37zDegIkUU3EF18ExYf4DbGopvQ0UCjs18g8gIkJKUQZzINpaW+xnJZREjUfHmc4pCmgPkJxZbzaM2JlPcl8lj+iLjVD5Ncbx8mXMS8bHmopIMTHLLknNO9/GHSIT5MfgMs9yb27QgdSwAAAABJRU5ErkJggg==", png.getContent());

        Image jpg = documentation.getImages().stream().filter(i -> i.getName().equals("image.jpg")).findFirst().get();
        assertEquals("image/jpeg", jpg.getType());
        assertEquals("/9j/4AAQSkZJRgABAgAAAQABAAD/2wBDAAgGBgcGBQgHBwcJCQgKDBQNDAsLDBkSEw8UHRofHh0aHBwgJC4nICIsIxwcKDcpLDAxNDQ0Hyc5PTgyPC4zNDL/2wBDAQkJCQwLDBgNDRgyIRwhMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjL/wAARCAAaACADASIAAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQAAAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEAAwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSExBhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElKU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3uLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3+Pn6/9oADAMBAAIRAxEAPwDxzUdRu9Vv5r69nea4mcu7uxJyTnv2GeBT49I1CWNXS1cqwyDkDP5mqLfdP0NeueF9Pt9Q1AR3Kb40h3begJ4HNerhqEal+boebiK0qdra3PNP7D1L/nzf/vpf8afbnVvD17BqEPnWs0UgZJVbHIOccHvjoete8/8ACP6R/wA+EP5V598R9Ot9PttttHsSRUbaOgIcDitZ4anytpszhXqcyTS1PNHVl3KykMMgg8c16n4b1q20+6W5dt8MkWwlCCR05x+Fc78UraC1+IGqx28EcKeYW2xoFGSTk4Hc1x5RSCSoz9KwoV/Zpu17m9ah7RpXtY99/wCEy0j+9N/3x/8AXrhfH+swavEotwf4I0U43Md2ScCvPPLT+4v5V2XwttLa5+IOlRz28UqeaG2yIGGQRg4NVPFrlaUfxFHC2km5bH//2Q==", jpg.getContent());

        Image jpeg = documentation.getImages().stream().filter(i -> i.getName().equals("image.jpeg")).findFirst().get();
        assertEquals("image/jpeg", jpeg.getType());
        assertEquals("/9j/4AAQSkZJRgABAgAAAQABAAD/2wBDAAgGBgcGBQgHBwcJCQgKDBQNDAsLDBkSEw8UHRofHh0aHBwgJC4nICIsIxwcKDcpLDAxNDQ0Hyc5PTgyPC4zNDL/2wBDAQkJCQwLDBgNDRgyIRwhMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjL/wAARCAAaACADASIAAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQAAAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEAAwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSExBhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElKU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3uLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3+Pn6/9oADAMBAAIRAxEAPwDxzUdRu9Vv5r69nea4mcu7uxJyTnv2GeBT49I1CWNXS1cqwyDkDP5mqLfdP0NeueF9Pt9Q1AR3Kb40h3begJ4HNerhqEal+boebiK0qdra3PNP7D1L/nzf/vpf8afbnVvD17BqEPnWs0UgZJVbHIOccHvjoete8/8ACP6R/wA+EP5V598R9Ot9PttttHsSRUbaOgIcDitZ4anytpszhXqcyTS1PNHVl3KykMMgg8c16n4b1q20+6W5dt8MkWwlCCR05x+Fc78UraC1+IGqx28EcKeYW2xoFGSTk4Hc1x5RSCSoz9KwoV/Zpu17m9ah7RpXtY99/wCEy0j+9N/3x/8AXrhfH+swavEotwf4I0U43Md2ScCvPPLT+4v5V2XwttLa5+IOlRz28UqeaG2yIGGQRg4NVPFrlaUfxFHC2km5bH//2Q==", jpeg.getContent());

        Image gif = documentation.getImages().stream().filter(i -> i.getName().equals("image.gif")).findFirst().get();
        assertEquals("image/gif", gif.getType());
        assertEquals("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", gif.getContent());
    }

    @Test
    public void test_addImage_AddsTheSpecifiedImage_WhenTheSpecifiedFileExists() throws IOException {
        assertTrue(documentation.getImages().isEmpty());
        documentation.addImage(new File(".//test/unit/com/structurizr/documentation/image.png"));
        assertEquals(1, documentation.getImages().size());

        Image png = documentation.getImages().stream().filter(i -> i.getName().equals("image.png")).findFirst().get();
        assertEquals("image/png", png.getType());
        assertEquals("iVBORw0KGgoAAAANSUhEUgAAACAAAAAaCAYAAADWm14/AAADx0lEQVR42sVXWU8TURS+g7YsbaEtpVAUNEhEA4iA8UkfNMFALFhRFo1xZVdcYqI+uKGyCGI0PrhL4gbqgy8gUgq0QNxiXP6CMTERDag88fJ57p0ZQqJ0Q+LDl5l2zj3fd8+5c84ZNjExgfaOx8grKAaLSUN4fDpCYtNmBWHkm8WlIze/CA8ePgLnZu0dT8AYA5MWCQPJmjZrArhvrY1EaJIFp+Be7yilH0lISFlJEUidNXIVzJqKxCXExRbA7thC19h0hHJVfpGnzxCKCOIK45z0H/Mr7LblCInPQkhcBmGZcg0Eyhrug/tS0iHOhPcdp4oFkt4CiXImhUdAiogMAlGQwsJlHwarIkKOOPO1c+5Ak7UD+k3XYNhyl3BPwf0p9/7gLvSFV6HJ3AZJZ56MBPNKboiFZvlWmCqcMO8bhrnGA/PeQZgIFkLcvkHYajmGpkUcPbeSnVhLPowVPdBklEKKtAmO6QXEZ4uQ6RxXiHRIiDBVumCs7EVMlQvacifYjudg27t9gGz2OMUao9jIEHQbLgvf/Ex4EZAJSSvBUHxHqDcRsbGqDzHVfcLhov1u5NW/haP5PTa2cHz4A/yZvekdMo8Mg+12IppE8Ajqi25BmksCbJneUkACQjUwlLTRIlmAVSFfdfwlTnaOoKn/p0809v3EOecoNrd+BNv5HLGUDj1tStIwHyn4Q4BL3j3t5PDjz2h2j+Nsz6hw7gtcSF33dyw+MIA5VW6YSoIQwMMXXuHCwtoBnOj8igbXmHBe3zvmE9yO26899QqsbACW0rZABdCpJwGachdSDrpxqmuEHP7wWwBHI9mvO/OaUtg/AwEVqoBvgQvo4wLe/CsBI/9bQKARGJ2pAM+kgMUHgotAw8zOgPwWRJCABQG+BeqryO3XBP8WTClEVAcOdXxCi2fcbwHnB37h9LNvSKrth7bKA6NfdUCUYkal+PZkKRaFqKwX2UeHcezpF7ErHloVjX+9HxPk9sZ3YLuUSqiWYuKYfiBRm1HBJdFAeDNSGxHb3SPuVx9/gZy61yK3f0MOgYed75yXYUul3FV1+RfFTBiWsMLLSMb7tXE+5qYWwljWJbdjpRVbSVBUzSBYuVvk1CvIJrTaQzsfktvxnk5ISx0IjV4IxlMw/VCqTkPRJGIjIuytNJRch37zDegIkUU3EF18ExYf4DbGopvQ0UCjs18g8gIkJKUQZzINpaW+xnJZREjUfHmc4pCmgPkJxZbzaM2JlPcl8lj+iLjVD5Ncbx8mXMS8bHmopIMTHLLknNO9/GHSIT5MfgMs9yb27QgdSwAAAABJRU5ErkJggg==", png.getContent());
    }

    @Test
    public void test_addImage_ThrowsAnException_WhenTheSpecifiedFileIsNull() throws IOException {
        try {
            documentation.addImage(null);
        } catch (IllegalArgumentException iae) {
            assertEquals("File must not be null", iae.getMessage());
        }
    }

    @Test
    public void test_addImage_ThrowsAnException_WhenTheSpecifiedFileIsNotAFile() throws IOException {
        try {
            documentation.addImage(new File(".//test/unit/com/structurizr/documentation/"));
        } catch (IllegalArgumentException iae) {
            assertTrue(iae.getMessage().endsWith("/structurizr-core/test/unit/com/structurizr/documentation is not a file."));
        }
    }

    @Test
    public void test_addImage_ThrowsAnException_WhenTheSpecifiedFileDoesNotExist() throws IOException {
        try {
            documentation.addImage(new File(".//test/unit/com/structurizr/documentation/some-other-image.png"));
        } catch (IllegalArgumentException iae) {
            assertTrue(iae.getMessage().endsWith("/structurizr-core/test/unit/com/structurizr/documentation/some-other-image.png does not exist."));
        }
    }

    @Test
    public void test_addImage_ThrowsAnException_WhenTheSpecifiedFileIsNotAnImage() throws IOException {
        try {
            documentation.addImage(new File(".//test/unit/com/structurizr/documentation/example.md"));
        } catch (IllegalArgumentException iae) {
            assertTrue(iae.getMessage().endsWith("/structurizr-core/test/unit/com/structurizr/documentation/example.md is not a supported image file."));
        }
    }

}
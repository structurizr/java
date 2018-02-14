package com.structurizr.util;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ImageUtilsTests {

    @Test
    public void test_getContentType_ThrowsAnException_WhenANullFileIsSpecified() throws Exception {
        try {
            ImageUtils.getContentType(null);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A file must be specified.", iae.getMessage());
        }
    }

    @Test
    public void test_getContentType_ThrowsAnException_WhenAFileIsSpecifiedButItIsNotAFile() throws Exception {
        try {
            ImageUtils.getContentType(new File("../structurizr-core"));
            fail();
        } catch (IllegalArgumentException iae) {
            iae.printStackTrace();
            assertTrue(iae.getMessage().endsWith("structurizr-core is not a file."));
        }
    }

    @Test
    public void test_getContentType_ThrowsAnException_WhenAFileIsSpecifiedButItIsNotAnImage() throws Exception {
        try {
            ImageUtils.getContentType(new File("../build.gradle"));
            fail();
        } catch (IllegalArgumentException iae) {
            iae.printStackTrace();
            assertTrue(iae.getMessage().endsWith("build.gradle is not a supported image file."));
        }
    }

    @Test
    public void test_getContentType_ThrowsAnException_WhenAFileIsSpecifiedButItDoesNotExist() throws Exception {
        try {
            ImageUtils.getContentType(new File("./foo.xml"));
            fail();
        } catch (IllegalArgumentException iae) {
            assertTrue(iae.getMessage().endsWith("foo.xml does not exist."));
        }
    }

    @Test
    public void test_getContentType_ReturnsTheContentType_WhenAFileIsSpecified() throws Exception {
        String contentType = ImageUtils.getContentType(new File("../structurizr-core/test/unit/com/structurizr/util/structurizr-logo.png"));
        assertEquals("image/png", contentType);
    }

    @Test
    public void test_getImageAsBase64_ThrowsAnException_WhenANullFileIsSpecified() throws Exception {
        try {
            ImageUtils.getImageAsBase64(null);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A file must be specified.", iae.getMessage());
        }
    }

    @Test
    public void test_getImageAsBase64_ThrowsAnException_WhenAFileIsSpecifiedButItIsNotAFile() throws Exception {
        try {
            ImageUtils.getImageAsBase64(new File("../structurizr-core"));
            fail();
        } catch (IllegalArgumentException iae) {
            iae.printStackTrace();
            assertTrue(iae.getMessage().endsWith("structurizr-core is not a file."));
        }
    }

    @Test
    public void test_getImageAsBase64_ThrowsAnException_WhenAFileIsSpecifiedButItIsNotAnImage() throws Exception {
        try {
            ImageUtils.getImageAsBase64(new File("../build.gradle"));
            fail();
        } catch (IllegalArgumentException iae) {
            iae.printStackTrace();
            assertTrue(iae.getMessage().endsWith("build.gradle is not a supported image file."));
        }
    }

    @Test
    public void test_getImageAsBase64_ThrowsAnException_WhenAFileIsSpecifiedButItDoesNotExist() throws Exception {
        try {
            ImageUtils.getImageAsBase64(new File("./foo.xml"));
            fail();
        } catch (IllegalArgumentException iae) {
            assertTrue(iae.getMessage().endsWith("foo.xml does not exist."));
        }
    }

    @Test
    public void test_getImageAsBase64_ReturnsTheImageAsABase64EncodedString_WhenAFileIsSpecified() throws Exception {
        String imageAsBase64 = ImageUtils.getImageAsBase64(new File("../structurizr-core/test/unit/com/structurizr/util/structurizr-logo.png"));
        assertTrue(imageAsBase64.startsWith("iVBORw0KGgoAAAANSUhEUgAAAMQAAADECAYAAADApo5rAAA")); // the actual base64 encoded string varies between Java 8 and 9
    }

    @Test
    public void test_getImageAsDataUri_ThrowsAnException_WhenANullFileIsSpecified() throws Exception {
        try {
            ImageUtils.getImageAsDataUri(null);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A file must be specified.", iae.getMessage());
        }
    }

    @Test
    public void test_getImageAsDataUri_ThrowsAnException_WhenAFileIsSpecifiedButItIsNotAFile() throws Exception {
        try {
            ImageUtils.getImageAsDataUri(new File("../structurizr-core"));
            fail();
        } catch (IllegalArgumentException iae) {
            iae.printStackTrace();
            assertTrue(iae.getMessage().endsWith("structurizr-core is not a file."));
        }
    }

    @Test
    public void test_getImageAsDataUri_ThrowsAnException_WhenAFileIsSpecifiedButItIsNotAnImage() throws Exception {
        try {
            ImageUtils.getImageAsDataUri(new File("../build.gradle"));
            fail();
        } catch (IllegalArgumentException iae) {
            iae.printStackTrace();
            assertTrue(iae.getMessage().endsWith("build.gradle is not a supported image file."));
        }
    }

    @Test
    public void test_getImageAsDataUri_ThrowsAnException_WhenAFileIsSpecifiedButItDoesNotExist() throws Exception {
        try {
            ImageUtils.getImageAsDataUri(new File("./foo.xml"));
            fail();
        } catch (IllegalArgumentException iae) {
            assertTrue(iae.getMessage().endsWith("foo.xml does not exist."));
        }
    }

    @Test
    public void test_getImageAsDataUri_ReturnsTheImageAsADataUri_WhenAFileIsSpecified() throws Exception {
        String imageAsDataUri = ImageUtils.getImageAsDataUri(new File("../structurizr-core/test/unit/com/structurizr/util/structurizr-logo.png"));
        System.out.println(imageAsDataUri);
        assertTrue(imageAsDataUri.startsWith("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAMQAAADECAYAAADA"));  // the actual base64 encoded string varies between Java 8 and 9
    }

}

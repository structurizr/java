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
    public void test_getImageAsBase64_ReturnsTheContentType_WhenAFileIsSpecified() throws Exception {
        String contentType = ImageUtils.getImageAsBase64(new File("../structurizr-core/test/unit/com/structurizr/util/structurizr-logo.png"));
        assertEquals("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", contentType);
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
    public void test_getImageAsDataUri_ReturnsTheContentType_WhenAFileIsSpecified() throws Exception {
        String contentType = ImageUtils.getImageAsDataUri(new File("../structurizr-core/test/unit/com/structurizr/util/structurizr-logo.png"));
        assertEquals("data:image/png;base64,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", contentType);
    }

}

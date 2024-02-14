package com.structurizr.util;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class ImageUtilsTests {

    @Test
    void getContentType_ThrowsAnException_WhenANullFileIsSpecified() throws Exception {
        try {
            ImageUtils.getContentType((File)null);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A file must be specified.", iae.getMessage());
        }
    }

    @Test
    void getContentType_ThrowsAnException_WhenAFileIsSpecifiedButItIsNotAFile() throws Exception {
        try {
            ImageUtils.getContentType(new File("../structurizr-core"));
            fail();
        } catch (IllegalArgumentException iae) {
            iae.printStackTrace();
            assertTrue(iae.getMessage().endsWith("structurizr-core is not a file."));
        }
    }

    @Test
    void getContentType_ThrowsAnException_WhenAFileIsSpecifiedButItIsNotAnImage() throws Exception {
        try {
            ImageUtils.getContentType(new File("../build.gradle"));
            fail();
        } catch (IllegalArgumentException iae) {
            iae.printStackTrace();
            assertTrue(iae.getMessage().endsWith("build.gradle is not a supported image file."));
        }
    }

    @Test
    void getContentType_ThrowsAnException_WhenAFileIsSpecifiedButItDoesNotExist() throws Exception {
        try {
            ImageUtils.getContentType(new File("./foo.xml"));
            fail();
        } catch (IllegalArgumentException iae) {
            assertTrue(iae.getMessage().endsWith("foo.xml does not exist."));
        }
    }

    @Test
    void getContentType_ReturnsTheContentType_WhenAPNGFileIsSpecified() throws Exception {
        String contentType = ImageUtils.getContentType(new File("./src/test/resources/image.png"));
        assertEquals("image/png", contentType);
    }

    @Test
    void getContentType_ReturnsTheContentType_WhenASVGFileIsSpecified() throws Exception {
        String contentType = ImageUtils.getContentType(new File("./src/test/resources/image.svg"));
        assertEquals("image/svg+xml", contentType);
    }

    @Test
    void getContentType_ThrowsAnException_WhenANullUrlIsSpecified() throws Exception {
        try {
            ImageUtils.getContentType((String)null);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A URL must be specified.", iae.getMessage());
        }
    }

    @Test
    void getContentType_ReturnsTheContentType_WhenAPNGUrlIsSpecified() throws Exception {
        String contentType = ImageUtils.getContentType(new File("../structurizr-core/test/unit/com/structurizr/util/image.png").toURI().toURL().toExternalForm());
        assertEquals("image/png", contentType);
    }

    @Test
    void getContentType_ReturnsTheContentType_WhenASVGUrlIsSpecified() throws Exception {
        String contentType = ImageUtils.getContentType(new File("../structurizr-core/test/unit/com/structurizr/util/image.svg").toURI().toURL().toExternalForm());
        assertEquals("image/svg+xml", contentType);
    }

    @Test
    void getContentTypeFromDataUri_ThrowsAnException_WhenANullDataUriIsSpecified() throws Exception {
        try {
            ImageUtils.getContentTypeFromDataUri(null);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A data URI must be specified.", iae.getMessage());
        }
    }

    @Test
    void getContentTypeFromDataUri_ReturnsTheContentType_WhenAUrlIsSpecified() throws Exception {
        assertEquals("image/png", ImageUtils.getContentTypeFromDataUri("data:image/png;base64,..."));
        assertEquals("image/jpeg", ImageUtils.getContentTypeFromDataUri("data:image/jpeg;base64,..."));
        assertEquals("image/svg+xml", ImageUtils.getContentTypeFromDataUri("data:image/svg+xml;utf8,..."));
        assertNull(ImageUtils.getContentTypeFromDataUri("data:..."));
    }

    @Test
    void getImageAsBase64_ThrowsAnException_WhenANullFileIsSpecified() throws Exception {
        try {
            ImageUtils.getImageAsBase64(null);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A file must be specified.", iae.getMessage());
        }
    }

    @Test
    void getImageAsBase64_ThrowsAnException_WhenAFileIsSpecifiedButItIsNotAFile() throws Exception {
        try {
            ImageUtils.getImageAsBase64(new File("../structurizr-core"));
            fail();
        } catch (IllegalArgumentException iae) {
            iae.printStackTrace();
            assertTrue(iae.getMessage().endsWith("structurizr-core is not a file."));
        }
    }

    @Test
    void getImageAsBase64_ThrowsAnException_WhenAFileIsSpecifiedButItIsNotAnImage() throws Exception {
        try {
            ImageUtils.getImageAsBase64(new File("../build.gradle"));
            fail();
        } catch (IllegalArgumentException iae) {
            iae.printStackTrace();
            assertTrue(iae.getMessage().endsWith("build.gradle is not a supported image file."));
        }
    }

    @Test
    void getImageAsBase64_ThrowsAnException_WhenAFileIsSpecifiedButItDoesNotExist() throws Exception {
        try {
            ImageUtils.getImageAsBase64(new File("./foo.xml"));
            fail();
        } catch (IllegalArgumentException iae) {
            assertTrue(iae.getMessage().endsWith("foo.xml does not exist."));
        }
    }

    @Test
    void getImageAsBase64_ReturnsTheImageAsABase64EncodedString_WhenAPNGFileIsSpecified() throws Exception {
        String imageAsBase64 = ImageUtils.getImageAsBase64(new File("./src/test/resources/image.png"));
        assertEquals("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", imageAsBase64);
    }

    @Test
    void getImageAsBase64_ReturnsTheImageAsABase64EncodedString_WhenASVGFileIsSpecified() throws Exception {
        String imageAsBase64 = ImageUtils.getImageAsBase64(new File("./src/test/resources/image.svg"));
        assertEquals("PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0idXMtYXNjaWkiIHN0YW5kYWxvbmU9Im5vIj8+PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHhtbG5zOnhsaW5rPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5L3hsaW5rIiBjb250ZW50U3R5bGVUeXBlPSJ0ZXh0L2NzcyIgaGVpZ2h0PSIxMjBweCIgcHJlc2VydmVBc3BlY3RSYXRpbz0ibm9uZSIgc3R5bGU9IndpZHRoOjExM3B4O2hlaWdodDoxMjBweDtiYWNrZ3JvdW5kOiNGRkZGRkY7IiB2ZXJzaW9uPSIxLjEiIHZpZXdCb3g9IjAgMCAxMTMgMTIwIiB3aWR0aD0iMTEzcHgiIHpvb21BbmRQYW49Im1hZ25pZnkiPjxkZWZzLz48Zz48bGluZSBzdHlsZT0ic3Ryb2tlOiMxODE4MTg7c3Ryb2tlLXdpZHRoOjAuNTtzdHJva2UtZGFzaGFycmF5OjUuMCw1LjA7IiB4MT0iMjYiIHgyPSIyNiIgeTE9IjM2LjI5NjkiIHkyPSI4NS40Mjk3Ii8+PGxpbmUgc3R5bGU9InN0cm9rZTojMTgxODE4O3N0cm9rZS13aWR0aDowLjU7c3Ryb2tlLWRhc2hhcnJheTo1LjAsNS4wOyIgeDE9IjgyIiB4Mj0iODIiIHkxPSIzNi4yOTY5IiB5Mj0iODUuNDI5NyIvPjxyZWN0IGZpbGw9IiNFMkUyRjAiIGhlaWdodD0iMzAuMjk2OSIgcng9IjIuNSIgcnk9IjIuNSIgc3R5bGU9InN0cm9rZTojMTgxODE4O3N0cm9rZS13aWR0aDowLjU7IiB3aWR0aD0iNDMiIHg9IjUiIHk9IjUiLz48dGV4dCBmaWxsPSIjMDAwMDAwIiBmb250LWZhbWlseT0ic2Fucy1zZXJpZiIgZm9udC1zaXplPSIxNCIgbGVuZ3RoQWRqdXN0PSJzcGFjaW5nIiB0ZXh0TGVuZ3RoPSIyOSIgeD0iMTIiIHk9IjI0Ljk5NTEiPkJvYjwvdGV4dD48cmVjdCBmaWxsPSIjRTJFMkYwIiBoZWlnaHQ9IjMwLjI5NjkiIHJ4PSIyLjUiIHJ5PSIyLjUiIHN0eWxlPSJzdHJva2U6IzE4MTgxODtzdHJva2Utd2lkdGg6MC41OyIgd2lkdGg9IjQzIiB4PSI1IiB5PSI4NC40Mjk3Ii8+PHRleHQgZmlsbD0iIzAwMDAwMCIgZm9udC1mYW1pbHk9InNhbnMtc2VyaWYiIGZvbnQtc2l6ZT0iMTQiIGxlbmd0aEFkanVzdD0ic3BhY2luZyIgdGV4dExlbmd0aD0iMjkiIHg9IjEyIiB5PSIxMDQuNDI0OCI+Qm9iPC90ZXh0PjxyZWN0IGZpbGw9IiNFMkUyRjAiIGhlaWdodD0iMzAuMjk2OSIgcng9IjIuNSIgcnk9IjIuNSIgc3R5bGU9InN0cm9rZTojMTgxODE4O3N0cm9rZS13aWR0aDowLjU7IiB3aWR0aD0iNDkiIHg9IjU4IiB5PSI1Ii8+PHRleHQgZmlsbD0iIzAwMDAwMCIgZm9udC1mYW1pbHk9InNhbnMtc2VyaWYiIGZvbnQtc2l6ZT0iMTQiIGxlbmd0aEFkanVzdD0ic3BhY2luZyIgdGV4dExlbmd0aD0iMzUiIHg9IjY1IiB5PSIyNC45OTUxIj5BbGljZTwvdGV4dD48cmVjdCBmaWxsPSIjRTJFMkYwIiBoZWlnaHQ9IjMwLjI5NjkiIHJ4PSIyLjUiIHJ5PSIyLjUiIHN0eWxlPSJzdHJva2U6IzE4MTgxODtzdHJva2Utd2lkdGg6MC41OyIgd2lkdGg9IjQ5IiB4PSI1OCIgeT0iODQuNDI5NyIvPjx0ZXh0IGZpbGw9IiMwMDAwMDAiIGZvbnQtZmFtaWx5PSJzYW5zLXNlcmlmIiBmb250LXNpemU9IjE0IiBsZW5ndGhBZGp1c3Q9InNwYWNpbmciIHRleHRMZW5ndGg9IjM1IiB4PSI2NSIgeT0iMTA0LjQyNDgiPkFsaWNlPC90ZXh0Pjxwb2x5Z29uIGZpbGw9IiMxODE4MTgiIHBvaW50cz0iNzAuNSw2My40Mjk3LDgwLjUsNjcuNDI5Nyw3MC41LDcxLjQyOTcsNzQuNSw2Ny40Mjk3IiBzdHlsZT0ic3Ryb2tlOiMxODE4MTg7c3Ryb2tlLXdpZHRoOjEuMDsiLz48bGluZSBzdHlsZT0ic3Ryb2tlOiMxODE4MTg7c3Ryb2tlLXdpZHRoOjEuMDsiIHgxPSIyNi41IiB4Mj0iNzYuNSIgeTE9IjY3LjQyOTciIHkyPSI2Ny40Mjk3Ii8+PHRleHQgZmlsbD0iIzAwMDAwMCIgZm9udC1mYW1pbHk9InNhbnMtc2VyaWYiIGZvbnQtc2l6ZT0iMTMiIGxlbmd0aEFkanVzdD0ic3BhY2luZyIgdGV4dExlbmd0aD0iMzAiIHg9IjMzLjUiIHk9IjYyLjM2MzgiPmhlbGxvPC90ZXh0PjwhLS1TUkM9W1N5ZkZLajJyS3QzQ29LbkVMUjFJbzRaRG9TYTcwMDAwXS0tPjwvZz48L3N2Zz4=", imageAsBase64);
    }

    @Test
    void getImageAsDataUri_ThrowsAnException_WhenANullFileIsSpecified() throws Exception {
        try {
            ImageUtils.getImageAsDataUri(null);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A file must be specified.", iae.getMessage());
        }
    }

    @Test
    void getImageAsDataUri_ThrowsAnException_WhenAFileIsSpecifiedButItIsNotAFile() throws Exception {
        try {
            ImageUtils.getImageAsDataUri(new File("../structurizr-core"));
            fail();
        } catch (IllegalArgumentException iae) {
            iae.printStackTrace();
            assertTrue(iae.getMessage().endsWith("structurizr-core is not a file."));
        }
    }

    @Test
    void getImageAsDataUri_ThrowsAnException_WhenAFileIsSpecifiedButItIsNotAnImage() throws Exception {
        try {
            ImageUtils.getImageAsDataUri(new File("../build.gradle"));
            fail();
        } catch (IllegalArgumentException iae) {
            iae.printStackTrace();
            assertTrue(iae.getMessage().endsWith("build.gradle is not a supported image file."));
        }
    }

    @Test
    void getImageAsDataUri_ThrowsAnException_WhenAFileIsSpecifiedButItDoesNotExist() throws Exception {
        try {
            ImageUtils.getImageAsDataUri(new File("./foo.xml"));
            fail();
        } catch (IllegalArgumentException iae) {
            assertTrue(iae.getMessage().endsWith("foo.xml does not exist."));
        }
    }

    @Test
    void getImageAsDataUri_ReturnsTheImageAsADataUri_WhenAFileIsSpecified() throws Exception {
        String imageAsDataUri = ImageUtils.getImageAsDataUri(new File("./src/test/resources/image.png"));
        assertTrue(imageAsDataUri.startsWith("data:image/png;base64,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"));
    }

    @Test
    void validateImage() {
        // allowed
        ImageUtils.validateImage("https://structurizr.com/image.png");
        ImageUtils.validateImage("data:image/png;base64,iVBORw0KGg");
        ImageUtils.validateImage("data:image/jpeg;base64,iVBORw0KGg");
        ImageUtils.validateImage("image.png");
        ImageUtils.validateImage("image.jpg");
        ImageUtils.validateImage("image.jpeg");
        ImageUtils.validateImage("image.gif");
        ImageUtils.validateImage("data:image/svg+xml;utf8,iVBORw0KGg");

        //disallowed
        try {
            ImageUtils.validateImage("data:image/other");
            fail();
        } catch (Exception e) {
            assertEquals("Only PNG and JPG data URIs are supported: data:image/other", e.getMessage());
        }
    }

    @Test
    void isSupportedDataUri() {
        assertTrue(ImageUtils.isSupportedDataUri("data:image/png;base64,iVBORw0KGg"));
        assertTrue(ImageUtils.isSupportedDataUri("data:image/jpeg;base64,iVBORw0KGg"));
        assertTrue(ImageUtils.isSupportedDataUri("data:image/svg+xml;utf8,<svg..."));
        assertFalse(ImageUtils.isSupportedDataUri("hello world"));
    }

}

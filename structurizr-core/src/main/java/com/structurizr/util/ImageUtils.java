package com.structurizr.util;

import javax.annotation.Nonnull;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.Base64;

/**
 * Some utility methods for dealing with images.
 */
public class ImageUtils {

    public static final String DATA_URI_PREFIX = "data:";
    public static final String DATA_URI_IMAGE_PNG = "data:image/png;base64,";
    public static final String DATA_URI_IMAGE_JPG = "data:image/jpeg;base64,";
    public static final String DATA_URI_IMAGE_SVG = "data:image/svg+xml;";

    public static final String CONTENT_TYPE_IMAGE_PNG = "image/png";
    public static final String CONTENT_TYPE_IMAGE_JPG = "image/jpeg";
    public static final String CONTENT_TYPE_IMAGE_SVG = "image/svg+xml";

    /**
     * Gets the content type of the specified file representing an image.
     *
     * @param   file            a File pointing to an image
     * @return  a content type (e.g. "image/png")
     * @throws IOException      if there is an error reading the file
     */
    public static String getContentType(@Nonnull File file) throws IOException {
        if (file == null) {
            throw new IllegalArgumentException("A file must be specified.");
        } else if (!file.exists()) {
            throw new IllegalArgumentException(file.getCanonicalPath() + " does not exist.");
        } else if (!file.isFile()) {
            throw new IllegalArgumentException(file.getCanonicalPath() + " is not a file.");
        }

        String contentType = URLConnection.guessContentTypeFromName(file.getName());
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException(file.getCanonicalPath() + " is not a supported image file.");
        }

        return contentType;
    }

    /**
     * Gets the content type of the specified URL representing an image.
     *
     * @param   url             a URL pointing to an image
     * @return  a content type (e.g. "image/png")
     * @throws IOException      if there is an error reading the file
     */
    public static String getContentType(String url) throws IOException {
        if (StringUtils.isNullOrEmpty(url)) {
            throw new IllegalArgumentException("A URL must be specified.");
        }

        URLConnection connection = new URL(url).openConnection();
        connection.setConnectTimeout(1000 * 30);
        return connection.getContentType();
    }

    /**
     * Gets the content type of the specified data URI representing an image.
     *
     * @param   dataUri             a data URI representing an image
     * @return  a content type (e.g. "image/png")
     */
    public static String getContentTypeFromDataUri(String dataUri) {
        if (StringUtils.isNullOrEmpty(dataUri)) {
            throw new IllegalArgumentException("A data URI must be specified.");
        }

        if (dataUri.startsWith(DATA_URI_IMAGE_PNG)) {
            return CONTENT_TYPE_IMAGE_PNG;
        } else if (dataUri.startsWith(DATA_URI_IMAGE_JPG)) {
            return CONTENT_TYPE_IMAGE_JPG;
        } else if (dataUri.startsWith(DATA_URI_IMAGE_SVG)) {
            return CONTENT_TYPE_IMAGE_SVG;
        }

        return null;
    }

    /**
     * Gets the content of an image as a Base64 encoded string.
     *
     * @param   file            a File pointing to an image
     * @return  a Base64 encoded version of that image
     * @throws IOException      if there is an error reading the file
     */
    public static String getImageAsBase64(@Nonnull File file) throws IOException {
        String contentType = getContentType(file);

        if (ImageUtils.CONTENT_TYPE_IMAGE_SVG.equalsIgnoreCase(contentType)) {
            return Base64.getEncoder().encodeToString(Files.readAllBytes(file.toPath()));
        }

        BufferedImage bufferedImage = ImageIO.read(file);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, contentType.split("/")[1], bos);
        byte[] imageBytes = bos.toByteArray();

        return Base64.getEncoder().encodeToString(imageBytes);
    }

    /**
     * Gets the content of an image as a data URI; e.g. "data:image/png;base64,iVBORw0KGgoAA..."
     *
     * @param   file        a File pointing to an image
     * @return              a data URI
     * @throws IOException  if there is an error reading the file
     */
    public static String getImageAsDataUri(File file) throws IOException {
        String contentType = getContentType(file);
        String base64Content = getImageAsBase64(file);

        return DATA_URI_PREFIX + contentType + ";base64," + base64Content;
    }

    public static void validateImage(String imageDescriptor) {
        if (StringUtils.isNullOrEmpty(imageDescriptor)) {
            return;
        }

        imageDescriptor = imageDescriptor.trim();

        if (Url.isUrl(imageDescriptor)) {
            // all good
            return;
        }

        if (imageDescriptor.toLowerCase().endsWith(".png") || imageDescriptor.toLowerCase().endsWith(".jpg") || imageDescriptor.toLowerCase().endsWith(".jpeg") || imageDescriptor.toLowerCase().endsWith(".gif") || imageDescriptor.toLowerCase().endsWith(".svg")) {
            // it's just a filename
            return;
        }

        if (imageDescriptor.startsWith(DATA_URI_PREFIX)) {
            if (ImageUtils.isSupportedDataUri(imageDescriptor)) {
                // it's a PNG/JPG/SVG data URI
                return;
            } else {
                // it's a data URI, but not supported
                throw new IllegalArgumentException("Only PNG, JPG, and SVG data URIs are supported: " + imageDescriptor);
            }
        }

        throw new IllegalArgumentException("Expected a URL or data URI");
    }

    public static boolean isSupportedDataUri(String uri) {
        return  uri.startsWith(DATA_URI_IMAGE_PNG) ||
                uri.startsWith(DATA_URI_IMAGE_JPG) ||
                uri.startsWith(DATA_URI_IMAGE_SVG);
    }

}
package com.structurizr.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.util.Base64;

/**
 * Some utility methods for dealing with images.
 */
public class ImageUtils {

    /**
     * Gets the content type of the specified file representing an image.
     *
     * @param   file        a File pointing to an image
     * @return  a content type (e.g.
     */
    public static String getContentType(File file) throws IOException {
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
     * Gets the content of an image as a Base64 encoded string.
     *
     * @param   file    a File pointing to an image
     * @return          a Base64 encoded version of that image
     */
    public static String getImageAsBase64(File file) throws IOException {
        String contentType = getContentType(file);
        BufferedImage bufferedImage = ImageIO.read(file);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, contentType.split("/")[1], bos);
        byte[] imageBytes = bos.toByteArray();

        return Base64.getEncoder().encodeToString(imageBytes);
    }

    /**
     * Gets the content of an image as a data URI; e.g. "data:image/png;base64,iVBORw0KGgoAA..."
     *
     * @param   file    a File pointing to an image
     * @return          a data URI
     */
    public static String getImageAsDataUri(File file) throws IOException {
        String contentType = ImageUtils.getContentType(file);
        String base64Content = ImageUtils.getImageAsBase64(file);

        return "data:" + contentType + ";base64," + base64Content;
    }

}
package com.structurizr.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.util.Base64;

public class ImageUtils {

    public static String getContentType(File file) throws IOException {
        String contentType = URLConnection.guessContentTypeFromName(file.getName());
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException(file.getCanonicalPath() + " is not a supported image file.");
        }

        return contentType;
    }

    public static String getImageAsBase64(File file) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(file);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, getContentType(file).split("/")[1], bos);
        byte[] imageBytes = bos.toByteArray();

        return Base64.getEncoder().encodeToString(imageBytes);
    }

    public static String getImageAsDataUri(File file) throws IOException {
        if (file == null) {
            throw new IllegalArgumentException("File must not be null.");
        } else if (!file.exists()) {
            throw new IllegalArgumentException("The file " + file.getCanonicalPath() + " does not exist.");
        } else if (!file.isFile()) {
            throw new IllegalArgumentException(file.getCanonicalPath() + " is not a file.");
        }

        String contentType = ImageUtils.getContentType(file);
        String base64Content = ImageUtils.getImageAsBase64(file);

        return "data:" + contentType + ";base64," + base64Content;
    }

}

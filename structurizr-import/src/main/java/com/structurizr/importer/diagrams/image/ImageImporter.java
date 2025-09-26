package com.structurizr.importer.diagrams.image;

import com.structurizr.importer.diagrams.AbstractDiagramImporter;
import com.structurizr.util.ImageUtils;
import com.structurizr.view.ColorScheme;
import com.structurizr.view.ImageView;

import java.io.File;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class ImageImporter extends AbstractDiagramImporter {

    public static final String IMAGE_INLINE_PROPERTY = "image.inline";

    public void importDiagram(ImageView view, File file) throws Exception {
        importDiagram(view, file, null);
    }

    public void importDiagram(ImageView view, File file, ColorScheme colorScheme) throws Exception {
        view.setContent(ImageUtils.getImageAsDataUri(file));
        view.setContentType(ImageUtils.getContentType(file));
        view.setTitle(file.getName());
    }

    public void importDiagram(ImageView view, String url) throws Exception {
        importDiagram(view, url, null);
    }

    public void importDiagram(ImageView view, String url, ColorScheme colorScheme) throws Exception {
        String inline = getViewOrViewSetProperty(view, IMAGE_INLINE_PROPERTY);
        if ("true".equals(inline)) {
            String imageFormat = ImageUtils.getContentType(url);
            if (!imageFormat.equals(CONTENT_TYPE_IMAGE_PNG) && !imageFormat.equals(CONTENT_TYPE_IMAGE_SVG)) {
                throw new IllegalArgumentException(String.format("Found %s - expected a format of %s or %s", imageFormat, PNG_FORMAT, SVG_FORMAT));
            }

            if (imageFormat.equals(CONTENT_TYPE_IMAGE_SVG)) {
                view.setContent(ImageUtils.getSvgAsDataUri(new URL(url), false), colorScheme);
            } else {
                view.setContent(ImageUtils.getPngAsDataUri(new URL(url), false), colorScheme);
            }

            view.setContentType(imageFormat);
        } else {
            view.setContent(url, colorScheme);
            view.setContentType(ImageUtils.getContentType(url));
        }

        view.setTitle(url.substring(url.lastIndexOf("/")+1));
    }

}
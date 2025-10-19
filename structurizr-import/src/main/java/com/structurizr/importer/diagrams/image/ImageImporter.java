package com.structurizr.importer.diagrams.image;

import com.structurizr.http.HttpClient;
import com.structurizr.http.RemoteContent;
import com.structurizr.importer.diagrams.AbstractDiagramImporter;
import com.structurizr.util.ImageUtils;
import com.structurizr.view.ColorScheme;
import com.structurizr.view.ImageView;

import java.io.File;

public class ImageImporter extends AbstractDiagramImporter {

    public static final String IMAGE_INLINE_PROPERTY = "image.inline";

    public ImageImporter() {
    }

    public ImageImporter(HttpClient httpClient) {
        super(httpClient);
    }

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
        RemoteContent remoteContent = httpClient.get(url, false);

        String inline = getViewOrViewSetProperty(view, IMAGE_INLINE_PROPERTY);
        if ("true".equals(inline)) {

            String contentType = remoteContent.getContentType();

            if (!contentType.equals(CONTENT_TYPE_IMAGE_PNG) && !contentType.equals(CONTENT_TYPE_IMAGE_SVG)) {
                throw new IllegalArgumentException(String.format("Found %s - expected a format of %s or %s", contentType, PNG_FORMAT, SVG_FORMAT));
            }

            if (contentType.equals(CONTENT_TYPE_IMAGE_SVG)) {
                view.setContent(ImageUtils.getSvgAsDataUri(remoteContent.getContentAsString()), colorScheme);
            } else {
                view.setContent(ImageUtils.getPngAsDataUri(remoteContent.getContentAsBytes()), colorScheme);
            }

            view.setContentType(contentType);
        } else {
            view.setContent(url, colorScheme);
            view.setContentType(remoteContent.getContentType());
        }

        view.setTitle(url.substring(url.lastIndexOf("/")+1));
    }

}
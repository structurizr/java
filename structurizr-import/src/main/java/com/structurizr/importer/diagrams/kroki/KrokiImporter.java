package com.structurizr.importer.diagrams.kroki;

import com.structurizr.http.HttpClient;
import com.structurizr.http.RemoteContent;
import com.structurizr.importer.diagrams.AbstractDiagramImporter;
import com.structurizr.util.ImageUtils;
import com.structurizr.util.StringUtils;
import com.structurizr.view.ColorScheme;
import com.structurizr.view.ImageView;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class KrokiImporter extends AbstractDiagramImporter {

    public static final String KROKI_URL_PROPERTY = "kroki.url";
    public static final String KROKI_FORMAT_PROPERTY = "kroki.format";
    public static final String KROKI_INLINE_PROPERTY = "kroki.inline";

    public KrokiImporter() {
    }

    public KrokiImporter(HttpClient httpClient) {
        super(httpClient);
    }

    public void importDiagram(ImageView view, String format, File file) throws Exception {
        importDiagram(view, format, file, null);
    }

    public void importDiagram(ImageView view, String format, File file, ColorScheme colorScheme) throws Exception {
        String content = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
        view.setTitle(file.getName());

        importDiagram(view, format, content, colorScheme);
    }

    public void importDiagram(ImageView view, String format, String content) throws Exception {
        importDiagram(view, format, content, null);
    }

    public void importDiagram(ImageView view, String format, String content, ColorScheme colorScheme) throws Exception {
        String krokiServer = getViewOrViewSetProperty(view, KROKI_URL_PROPERTY);
        if (StringUtils.isNullOrEmpty(krokiServer)) {
            throw new IllegalArgumentException("Please define a view/viewset property named " + KROKI_URL_PROPERTY + " to specify your Kroki server");
        }

        String imageFormat = getViewOrViewSetProperty(view, KROKI_FORMAT_PROPERTY);
        if (StringUtils.isNullOrEmpty(imageFormat)) {
            imageFormat = PNG_FORMAT;
        }

        if (!imageFormat.equals(PNG_FORMAT) && !imageFormat.equals(SVG_FORMAT)) {
            throw new IllegalArgumentException(String.format("Expected a format of %s or %s", PNG_FORMAT, SVG_FORMAT));
        }

        String encodedDiagram = new KrokiEncoder().encode(content);
        String url = String.format("%s/%s/%s/%s", krokiServer, format, imageFormat, encodedDiagram);

        String inline = getViewOrViewSetProperty(view, KROKI_INLINE_PROPERTY);
        if ("true".equals(inline)) {
            RemoteContent remoteContent = httpClient.get(url, true);

            if (imageFormat.equals(SVG_FORMAT)) {
                view.setContent(ImageUtils.getSvgAsDataUri(remoteContent.getContentAsString()), colorScheme);
            } else {
                view.setContent(ImageUtils.getPngAsDataUri(remoteContent.getContentAsBytes()), colorScheme);
            }
        } else {
            view.setContent(url, colorScheme);
        }
        view.setContentType(CONTENT_TYPES_BY_FORMAT.get(imageFormat));
    }

}
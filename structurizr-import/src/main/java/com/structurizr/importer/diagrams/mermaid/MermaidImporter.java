package com.structurizr.importer.diagrams.mermaid;

import com.structurizr.importer.diagrams.AbstractDiagramImporter;
import com.structurizr.util.StringUtils;
import com.structurizr.view.ImageView;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class MermaidImporter extends AbstractDiagramImporter {

    public static final String MERMAID_URL_PROPERTY = "mermaid.url";
    public static final String MERMAID_FORMAT_PROPERTY = "mermaid.format";
    public static final String MERMAID_COMPRESS_PROPERTY = "mermaid.compress";

    public void importDiagram(ImageView view, File file) throws Exception {
        String content = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
        view.setTitle(file.getName());

        importDiagram(view, content);
    }

    public void importDiagram(ImageView view, String content) {
        String mermaidServer = getViewOrViewSetProperty(view, MERMAID_URL_PROPERTY);
        if (StringUtils.isNullOrEmpty(mermaidServer)) {
            throw new IllegalArgumentException("Please define a view/viewset property named " + MERMAID_URL_PROPERTY + " to specify your Mermaid server");
        }

        String format = getViewOrViewSetProperty(view, MERMAID_FORMAT_PROPERTY);
        if (StringUtils.isNullOrEmpty(format)) {
            format = SVG_FORMAT;
        }

        if (!format.equals(PNG_FORMAT) && !format.equals(SVG_FORMAT)) {
            throw new IllegalArgumentException(String.format("Expected a format of %s or %s", PNG_FORMAT, SVG_FORMAT));
        }

        String compress = getViewOrViewSetProperty(view, MERMAID_COMPRESS_PROPERTY);
        if (StringUtils.isNullOrEmpty(compress)) {
            compress = "true";
        }

        String encodedMermaid = new MermaidEncoder().encode(content, compress.equalsIgnoreCase("true"));
        String url;
        if (format.equals(PNG_FORMAT)) {
            url = String.format("%s/img/%s?type=png", mermaidServer, encodedMermaid);
        } else {
            url = String.format("%s/svg/%s", mermaidServer, encodedMermaid);
        }

        view.setContent(url);
        view.setContentType(CONTENT_TYPES_BY_FORMAT.get(format));
    }

}
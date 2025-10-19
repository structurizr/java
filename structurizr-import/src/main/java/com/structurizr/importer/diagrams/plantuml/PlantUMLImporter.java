package com.structurizr.importer.diagrams.plantuml;

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

public class PlantUMLImporter extends AbstractDiagramImporter {

    public static final String PLANTUML_URL_PROPERTY = "plantuml.url";
    public static final String PLANTUML_FORMAT_PROPERTY = "plantuml.format";
    public static final String PLANTUML_INLINE_PROPERTY = "plantuml.inline";
    private static final String TITLE_STRING = "title ";
    private static final String NEWLINE = "\n";

    public PlantUMLImporter() {
    }

    public PlantUMLImporter(HttpClient httpClient) {
        super(httpClient);
    }

    public void importDiagram(ImageView view, File file) throws Exception {
        importDiagram(view, file, null);
    }

    public void importDiagram(ImageView view, File file, ColorScheme colorScheme) throws Exception {
        String content = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
        view.setTitle(file.getName());

        importDiagram(view, content, colorScheme);
    }

    public void importDiagram(ImageView view, String content) throws Exception {
        importDiagram(view, content, null);
    }

    public void importDiagram(ImageView view, String content, ColorScheme colorScheme) throws Exception {
        String plantUMLServer = getViewOrViewSetProperty(view, PLANTUML_URL_PROPERTY);
        if (StringUtils.isNullOrEmpty(plantUMLServer)) {
            throw new IllegalArgumentException("Please define a view/viewset property named " + PLANTUML_URL_PROPERTY + " to specify your PlantUML server");
        }

        String format = getViewOrViewSetProperty(view, PLANTUML_FORMAT_PROPERTY);
        if (StringUtils.isNullOrEmpty(format)) {
            format = SVG_FORMAT;
        }

        if (!format.equals(PNG_FORMAT) && !format.equals(SVG_FORMAT)) {
            throw new IllegalArgumentException(String.format("Expected a format of %s or %s", PNG_FORMAT, SVG_FORMAT));
        }

        String encodedPlantUML = new PlantUMLEncoder().encode(content);
        String url = String.format("%s/%s/%s", plantUMLServer, format, encodedPlantUML);

        String inline = getViewOrViewSetProperty(view, PLANTUML_INLINE_PROPERTY);
        if ("true".equals(inline)) {
            RemoteContent remoteContent = httpClient.get(url, true);

            if (format.equals(SVG_FORMAT)) {
                view.setContent(ImageUtils.getSvgAsDataUri(remoteContent.getContentAsString()), colorScheme);
            } else {
                view.setContent(ImageUtils.getPngAsDataUri(remoteContent.getContentAsBytes()), colorScheme);
            }
        } else {
            view.setContent(url, colorScheme);
        }
        view.setContentType(CONTENT_TYPES_BY_FORMAT.get(format));

        String[] lines = content.split(NEWLINE);
        for (String line : lines) {
            if (line.startsWith(TITLE_STRING)) {
                view.setTitle(extractTitle(line));
            }
        }
    }

    private String extractTitle(String line) {
        String title = line.substring(TITLE_STRING.length());

        if (title.contains(NEWLINE)) {
            title = title.split(NEWLINE)[0];
        }

        if (title.startsWith("<size:")) {
            title = title.substring(title.indexOf(">") + 1);

            if (title.endsWith("</size>")) {
                title = title.substring(0, title.indexOf("</size>"));
            }
        }

        return title;
    }

}
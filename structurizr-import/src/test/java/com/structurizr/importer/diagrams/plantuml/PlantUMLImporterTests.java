package com.structurizr.importer.diagrams.plantuml;

import com.structurizr.Workspace;
import com.structurizr.http.HttpClient;
import com.structurizr.view.ImageView;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class PlantUMLImporterTests {

    @Test
    public void importDiagram_WhenATitleIsDefined() throws Exception {
        Workspace workspace = new Workspace("Name", "Description");
        workspace.getViews().getConfiguration().addProperty(PlantUMLImporter.PLANTUML_URL_PROPERTY, "https://plantuml.com/plantuml");
        ImageView view = workspace.getViews().createImageView("key");

        new PlantUMLImporter().importDiagram(view, new File("./src/test/resources/diagrams/plantuml/with-title.puml"));
        assertEquals("key", view.getKey());
        assertNull(view.getElement());
        assertNull(view.getElementId());
        assertEquals("Sequence diagram example", view.getTitle());
        assertEquals("https://plantuml.com/plantuml/svg/SoWkIImgAStDuIh9BCb9LGXEBInDpKjELKZ9J4mlIinLIAr8p2t8IULooazIqBLJSCp914fQAMIavkJaSpcavgK0zG80", view.getContent());
        assertEquals("image/svg+xml", view.getContentType());
    }

    @Test
    public void importDiagram_WhenATitleIsNotDefined() throws Exception {
        Workspace workspace = new Workspace("Name", "Description");
        workspace.getViews().getConfiguration().addProperty(PlantUMLImporter.PLANTUML_URL_PROPERTY, "https://plantuml.com/plantuml");
        ImageView view = workspace.getViews().createImageView("key");

        new PlantUMLImporter().importDiagram(view, new File("./src/test/resources/diagrams/plantuml/without-title.puml"));
        assertEquals("key", view.getKey());
        assertNull(view.getElement());
        assertNull(view.getElementId());
        assertEquals("without-title.puml", view.getTitle());
        assertEquals("https://plantuml.com/plantuml/svg/SoWkIImgAStDuNBAJrBGjLDmpCbCJbMmKiX8pSd9vt98pKi1IW80", view.getContent());
        assertEquals("image/svg+xml", view.getContentType());
    }

    @Test
    public void importDiagram_AsPNG() throws Exception {
        Workspace workspace = new Workspace("Name", "Description");
        workspace.getViews().getConfiguration().addProperty(PlantUMLImporter.PLANTUML_URL_PROPERTY, "https://plantuml.com/plantuml");
        workspace.getViews().getConfiguration().addProperty(PlantUMLImporter.PLANTUML_FORMAT_PROPERTY, "png");
        ImageView view = workspace.getViews().createImageView("key");

        new PlantUMLImporter().importDiagram(view, new File("./src/test/resources/diagrams/plantuml/with-title.puml"));
        assertEquals("https://plantuml.com/plantuml/png/SoWkIImgAStDuIh9BCb9LGXEBInDpKjELKZ9J4mlIinLIAr8p2t8IULooazIqBLJSCp914fQAMIavkJaSpcavgK0zG80", view.getContent());
        assertEquals("image/png", view.getContentType());
    }

    @Test
    @Tag("IntegrationTest")
    public void importDiagram_AsInlinePNG() throws Exception {
        Workspace workspace = new Workspace("Name", "Description");
        workspace.getViews().getConfiguration().addProperty(PlantUMLImporter.PLANTUML_URL_PROPERTY, "https://plantuml.com/plantuml");
        workspace.getViews().getConfiguration().addProperty(PlantUMLImporter.PLANTUML_FORMAT_PROPERTY, "png");
        workspace.getViews().getConfiguration().addProperty(PlantUMLImporter.PLANTUML_INLINE_PROPERTY, "true");
        ImageView view = workspace.getViews().createImageView("key");

        HttpClient httpClient = new HttpClient();
        httpClient.allow(".*");

        new PlantUMLImporter(httpClient).importDiagram(view, new File("./src/test/resources/diagrams/plantuml/with-title.puml"));
        assertEquals("Sequence diagram example", view.getTitle());
        assertEquals("data:image/png;base64,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", view.getContent());
        assertEquals("image/png", view.getContentType());
    }

    @Test
    public void importDiagram_AsSVG() throws Exception {
        Workspace workspace = new Workspace("Name", "Description");
        workspace.getViews().getConfiguration().addProperty(PlantUMLImporter.PLANTUML_URL_PROPERTY, "https://plantuml.com/plantuml");
        workspace.getViews().getConfiguration().addProperty(PlantUMLImporter.PLANTUML_FORMAT_PROPERTY, "svg");
        ImageView view = workspace.getViews().createImageView("key");

        new PlantUMLImporter().importDiagram(view, new File("./src/test/resources/diagrams/plantuml/with-title.puml"));
        assertEquals("https://plantuml.com/plantuml/svg/SoWkIImgAStDuIh9BCb9LGXEBInDpKjELKZ9J4mlIinLIAr8p2t8IULooazIqBLJSCp914fQAMIavkJaSpcavgK0zG80", view.getContent());
        assertEquals("image/svg+xml", view.getContentType());
    }

    @Test
    @Tag("IntegrationTest")
    public void importDiagram_AsInlineSVG() throws Exception {
        Workspace workspace = new Workspace("Name", "Description");
        workspace.getViews().getConfiguration().addProperty(PlantUMLImporter.PLANTUML_URL_PROPERTY, "https://plantuml.com/plantuml");
        workspace.getViews().getConfiguration().addProperty(PlantUMLImporter.PLANTUML_FORMAT_PROPERTY, "svg");
        workspace.getViews().getConfiguration().addProperty(PlantUMLImporter.PLANTUML_INLINE_PROPERTY, "true");
        ImageView view = workspace.getViews().createImageView("key");

        HttpClient httpClient = new HttpClient();
        httpClient.allow(".*");

        new PlantUMLImporter(httpClient).importDiagram(view, new File("./src/test/resources/diagrams/plantuml/with-title.puml"));
        assertEquals("data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHhtbG5zOnhsaW5rPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5L3hsaW5rIiBjb250ZW50U3R5bGVUeXBlPSJ0ZXh0L2NzcyIgZGF0YS1kaWFncmFtLXR5cGU9IlNFUVVFTkNFIiBoZWlnaHQ9IjE1OHB4IiBwcmVzZXJ2ZUFzcGVjdFJhdGlvPSJub25lIiBzdHlsZT0id2lkdGg6MjQ2cHg7aGVpZ2h0OjE1OHB4O2JhY2tncm91bmQ6I0ZGRkZGRjsiIHZlcnNpb249IjEuMSIgdmlld0JveD0iMCAwIDI0NiAxNTgiIHdpZHRoPSIyNDZweCIgem9vbUFuZFBhbj0ibWFnbmlmeSI+PHRpdGxlPlNlcXVlbmNlIGRpYWdyYW0gZXhhbXBsZTwvdGl0bGU+PGRlZnMvPjxnPjx0ZXh0IGZpbGw9IiMwMDAwMDAiIGZvbnQtZmFtaWx5PSJzYW5zLXNlcmlmIiBmb250LXNpemU9IjE0IiBmb250LXdlaWdodD0iYm9sZCIgbGVuZ3RoQWRqdXN0PSJzcGFjaW5nIiB0ZXh0TGVuZ3RoPSIyMTguNjc0OCIgeD0iMTAiIHk9IjI3Ljk5NTEiPlNlcXVlbmNlIGRpYWdyYW0gZXhhbXBsZTwvdGV4dD48Zz48dGl0bGU+Qm9iPC90aXRsZT48cmVjdCBmaWxsPSIjMDAwMDAwIiBmaWxsLW9wYWNpdHk9IjAuMDAwMDAiIGhlaWdodD0iNDkuMTMyOCIgd2lkdGg9IjgiIHg9Ijg2LjQ3NzUiIHk9IjczLjU5MzgiLz48bGluZSBzdHlsZT0ic3Ryb2tlOiMxODE4MTg7c3Ryb2tlLXdpZHRoOjAuNTtzdHJva2UtZGFzaGFycmF5OjUsNTsiIHgxPSI4OS45NDkyIiB4Mj0iODkuOTQ5MiIgeTE9IjczLjU5MzgiIHkyPSIxMjIuNzI2NiIvPjwvZz48Zz48dGl0bGU+QWxpY2U8L3RpdGxlPjxyZWN0IGZpbGw9IiMwMDAwMDAiIGZpbGwtb3BhY2l0eT0iMC4wMDAwMCIgaGVpZ2h0PSI0OS4xMzI4IiB3aWR0aD0iOCIgeD0iMTQxLjg5MjEiIHk9IjczLjU5MzgiLz48bGluZSBzdHlsZT0ic3Ryb2tlOiMxODE4MTg7c3Ryb2tlLXdpZHRoOjAuNTtzdHJva2UtZGFzaGFycmF5OjUsNTsiIHgxPSIxNDUuMDU4NiIgeDI9IjE0NS4wNTg2IiB5MT0iNzMuNTkzOCIgeTI9IjEyMi43MjY2Ii8+PC9nPjxnIGNsYXNzPSJwYXJ0aWNpcGFudCBwYXJ0aWNpcGFudC1oZWFkIiBkYXRhLXBhcnRpY2lwYW50PSJCb2IiPjxyZWN0IGZpbGw9IiNFMkUyRjAiIGhlaWdodD0iMzAuMjk2OSIgcng9IjIuNSIgcnk9IjIuNSIgc3R5bGU9InN0cm9rZTojMTgxODE4O3N0cm9rZS13aWR0aDowLjU7IiB3aWR0aD0iNDEuMDU2NiIgeD0iNjkuOTQ5MiIgeT0iNDIuMjk2OSIvPjx0ZXh0IGZpbGw9IiMwMDAwMDAiIGZvbnQtZmFtaWx5PSJzYW5zLXNlcmlmIiBmb250LXNpemU9IjE0IiBsZW5ndGhBZGp1c3Q9InNwYWNpbmciIHRleHRMZW5ndGg9IjI3LjA1NjYiIHg9Ijc2Ljk0OTIiIHk9IjYyLjI5MiI+Qm9iPC90ZXh0PjwvZz48ZyBjbGFzcz0icGFydGljaXBhbnQgcGFydGljaXBhbnQtdGFpbCIgZGF0YS1wYXJ0aWNpcGFudD0iQm9iIj48cmVjdCBmaWxsPSIjRTJFMkYwIiBoZWlnaHQ9IjMwLjI5NjkiIHJ4PSIyLjUiIHJ5PSIyLjUiIHN0eWxlPSJzdHJva2U6IzE4MTgxODtzdHJva2Utd2lkdGg6MC41OyIgd2lkdGg9IjQxLjA1NjYiIHg9IjY5Ljk0OTIiIHk9IjEyMS43MjY2Ii8+PHRleHQgZmlsbD0iIzAwMDAwMCIgZm9udC1mYW1pbHk9InNhbnMtc2VyaWYiIGZvbnQtc2l6ZT0iMTQiIGxlbmd0aEFkanVzdD0ic3BhY2luZyIgdGV4dExlbmd0aD0iMjcuMDU2NiIgeD0iNzYuOTQ5MiIgeT0iMTQxLjcyMTciPkJvYjwvdGV4dD48L2c+PGcgY2xhc3M9InBhcnRpY2lwYW50IHBhcnRpY2lwYW50LWhlYWQiIGRhdGEtcGFydGljaXBhbnQ9IkFsaWNlIj48cmVjdCBmaWxsPSIjRTJFMkYwIiBoZWlnaHQ9IjMwLjI5NjkiIHJ4PSIyLjUiIHJ5PSIyLjUiIHN0eWxlPSJzdHJva2U6IzE4MTgxODtzdHJva2Utd2lkdGg6MC41OyIgd2lkdGg9IjQ3LjY2NyIgeD0iMTIyLjA1ODYiIHk9IjQyLjI5NjkiLz48dGV4dCBmaWxsPSIjMDAwMDAwIiBmb250LWZhbWlseT0ic2Fucy1zZXJpZiIgZm9udC1zaXplPSIxNCIgbGVuZ3RoQWRqdXN0PSJzcGFjaW5nIiB0ZXh0TGVuZ3RoPSIzMy42NjciIHg9IjEyOS4wNTg2IiB5PSI2Mi4yOTIiPkFsaWNlPC90ZXh0PjwvZz48ZyBjbGFzcz0icGFydGljaXBhbnQgcGFydGljaXBhbnQtdGFpbCIgZGF0YS1wYXJ0aWNpcGFudD0iQWxpY2UiPjxyZWN0IGZpbGw9IiNFMkUyRjAiIGhlaWdodD0iMzAuMjk2OSIgcng9IjIuNSIgcnk9IjIuNSIgc3R5bGU9InN0cm9rZTojMTgxODE4O3N0cm9rZS13aWR0aDowLjU7IiB3aWR0aD0iNDcuNjY3IiB4PSIxMjIuMDU4NiIgeT0iMTIxLjcyNjYiLz48dGV4dCBmaWxsPSIjMDAwMDAwIiBmb250LWZhbWlseT0ic2Fucy1zZXJpZiIgZm9udC1zaXplPSIxNCIgbGVuZ3RoQWRqdXN0PSJzcGFjaW5nIiB0ZXh0TGVuZ3RoPSIzMy42NjciIHg9IjEyOS4wNTg2IiB5PSIxNDEuNzIxNyI+QWxpY2U8L3RleHQ+PC9nPjxnIGNsYXNzPSJtZXNzYWdlIiBkYXRhLXBhcnRpY2lwYW50LTE9IkJvYiIgZGF0YS1wYXJ0aWNpcGFudC0yPSJBbGljZSI+PHBvbHlnb24gZmlsbD0iIzE4MTgxOCIgcG9pbnRzPSIxMzMuODkyMSwxMDAuNzI2NiwxNDMuODkyMSwxMDQuNzI2NiwxMzMuODkyMSwxMDguNzI2NiwxMzcuODkyMSwxMDQuNzI2NiIgc3R5bGU9InN0cm9rZTojMTgxODE4O3N0cm9rZS13aWR0aDoxOyIvPjxsaW5lIHN0eWxlPSJzdHJva2U6IzE4MTgxODtzdHJva2Utd2lkdGg6MTsiIHgxPSI5MC40Nzc1IiB4Mj0iMTM5Ljg5MjEiIHkxPSIxMDQuNzI2NiIgeTI9IjEwNC43MjY2Ii8+PHRleHQgZmlsbD0iIzAwMDAwMCIgZm9udC1mYW1pbHk9InNhbnMtc2VyaWYiIGZvbnQtc2l6ZT0iMTMiIGxlbmd0aEFkanVzdD0ic3BhY2luZyIgdGV4dExlbmd0aD0iMzEuNDE0NiIgeD0iOTcuNDc3NSIgeT0iOTkuNjYwNiI+aGVsbG88L3RleHQ+PC9nPjwhLS1TUkM9W0F5YWlvS2JMMjR1akI0dERJcXZMSUNiQ0oyekFwNUw4aEtaQ0JTWDl2TkJBSnJCR2pMRG1wQ2E0SWJlZlBBSmN2RUczMDAwMF0tLT48L2c+PC9zdmc+", view.getContent());
        assertEquals("image/svg+xml", view.getContentType());
    }

    @Test
    public void importDiagram_WhenThePlantUMLURLIsNotSpecified() throws Exception {
        Workspace workspace = new Workspace("Name", "Description");
        ImageView view = workspace.getViews().createImageView("key");

        try {
        new PlantUMLImporter().importDiagram(view, new File("./src/test/resources/diagrams/plantuml/with-title.puml"));
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Please define a view/viewset property named plantuml.url to specify your PlantUML server", e.getMessage());
        }
    }

    @Test
    public void importDiagram_WhenAnInvalidFormatIsSpecified() throws Exception {
        Workspace workspace = new Workspace("Name", "Description");
        workspace.getViews().getConfiguration().addProperty(PlantUMLImporter.PLANTUML_URL_PROPERTY, "https://plantuml.com/plantuml");
        workspace.getViews().getConfiguration().addProperty(PlantUMLImporter.PLANTUML_FORMAT_PROPERTY, "jpg");
        ImageView view = workspace.getViews().createImageView("key");

        try {
            new PlantUMLImporter().importDiagram(view, "...");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Expected a format of png or svg", e.getMessage());
        }
    }

}

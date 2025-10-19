package com.structurizr.importer.diagrams.plantuml;

import com.structurizr.Workspace;
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

        new PlantUMLImporter().importDiagram(view, new File("./src/test/resources/diagrams/plantuml/with-title.puml"));
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

        new PlantUMLImporter().importDiagram(view, new File("./src/test/resources/diagrams/plantuml/with-title.puml"));
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

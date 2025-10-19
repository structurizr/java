package com.structurizr.importer.diagrams;

import com.structurizr.http.HttpClient;
import com.structurizr.view.View;
import com.structurizr.view.ViewSet;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractDiagramImporter {

    protected static final Map<String, String> CONTENT_TYPES_BY_FORMAT = new HashMap<>();

    protected static final String CONTENT_TYPE_IMAGE_PNG = "image/png";
    protected static final String CONTENT_TYPE_IMAGE_SVG = "image/svg+xml";

    protected static final String PNG_FORMAT = "png";
    protected static final String SVG_FORMAT = "svg";

    static {
        CONTENT_TYPES_BY_FORMAT.put(PNG_FORMAT, CONTENT_TYPE_IMAGE_PNG);
        CONTENT_TYPES_BY_FORMAT.put(SVG_FORMAT, CONTENT_TYPE_IMAGE_SVG);
    }

    protected HttpClient httpClient;

    public AbstractDiagramImporter() {
        this.httpClient = new HttpClient();
    }

    public AbstractDiagramImporter(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    protected String getViewOrViewSetProperty(View view, String name) {
        ViewSet views = view.getViewSet();

        return
                view.getProperties().getOrDefault(name,
                        views.getConfiguration().getProperties().get(name)
                );
    }

}
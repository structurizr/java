package com.structurizr.export.websequencediagrams;

import com.structurizr.export.Diagram;
import com.structurizr.view.ModelView;

public class WebSequenceDiagramsDiagram extends Diagram {

    public WebSequenceDiagramsDiagram(ModelView view, String definition) {
        super(view, definition);
    }

    @Override
    public String getFileExtension() {
        return "wsd";
    }

}
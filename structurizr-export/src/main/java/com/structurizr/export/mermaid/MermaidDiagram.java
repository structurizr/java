package com.structurizr.export.mermaid;

import com.structurizr.export.Diagram;
import com.structurizr.view.ModelView;

public class MermaidDiagram extends Diagram {

    public MermaidDiagram(ModelView view, String definition) {
        super(view, definition);
    }

    @Override
    public String getFileExtension() {
        return "mmd";
    }

}

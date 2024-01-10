package com.structurizr.autolayout.graphviz;

import com.structurizr.export.Diagram;
import com.structurizr.view.ModelView;

class DOTDiagram extends Diagram {

    DOTDiagram(ModelView view, String definition) {
        super(view, definition);
    }

    @Override
    public String getFileExtension() {
        return "dot";
    }

}
package com.structurizr.export.dot;

import com.structurizr.export.Diagram;
import com.structurizr.view.ModelView;

public class DOTDiagram extends Diagram {

    public DOTDiagram(ModelView view, String definition) {
        super(view, definition);
    }

    @Override
    public String getFileExtension() {
        return "dot";
    }

}
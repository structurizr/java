package com.structurizr.export.plantuml;

import com.structurizr.export.Diagram;
import com.structurizr.view.ModelView;

public class PlantUMLDiagram extends Diagram {

    public PlantUMLDiagram(ModelView view, String definition) {
        super(view, definition);
    }

    @Override
    public String getFileExtension() {
        return "puml";
    }

}
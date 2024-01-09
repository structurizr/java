package com.structurizr.export;

import com.structurizr.view.View;

import java.util.ArrayList;
import java.util.List;

public abstract class Diagram {

    private View view;
    private String definition;

    private List<Diagram> frames = new ArrayList<>();
    private Legend legend;

    public Diagram(View view, String definition) {
        this.view = view;
        this.definition = definition;
    }

    public String getKey() {
        return view.getKey();
    }

    public View getView() {
        return view;
    }

    public String getDefinition() {
        return definition;
    }

    public void addFrame(Diagram frame) {
        frames.add(frame);
    }

    public List<Diagram> getFrames() {
        return new ArrayList<>(frames);
    }

    public Legend getLegend() {
        return legend;
    }

    public void setLegend(Legend legend) {
        this.legend = legend;
    }

    public abstract String getFileExtension();

}
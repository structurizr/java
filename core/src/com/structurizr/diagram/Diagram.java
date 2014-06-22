package com.structurizr.diagram;

import com.structurizr.view.View;

import java.util.HashSet;
import java.util.Set;

public class Diagram {

    private String title;

    private View view;

    private Set<Box> boxes = new HashSet<>();
    private Set<Line> lines = new HashSet<>();

    public Diagram(View view) {
        this.view = view;
    }

    public String getTitle() {
        return view.getSoftwareSystem().getName() + " - " + view.getType();
    }

}

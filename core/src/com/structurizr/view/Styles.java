package com.structurizr.view;

import java.util.Collection;
import java.util.LinkedList;

public class Styles {

    private Collection<ElementStyle> elements = new LinkedList<>();

    public Collection<ElementStyle> getElements() {
        return elements;
    }

    public void add(ElementStyle elementStyle) {
        if (elementStyle != null) {
            this.elements.add(elementStyle);
        }
    }

}

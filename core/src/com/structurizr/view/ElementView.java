package com.structurizr.view;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.structurizr.model.Element;

public class ElementView {

    private Element element;

    public ElementView(Element element) {
        this.element = element;
    }

    @JsonIgnore
    public Element getElement() {
        return element;
    }

    public long getId() {
        return element.getId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ElementView that = (ElementView)o;
        return this.getId() == that.getId();
    }

    @Override
    public int hashCode() {
        return element.hashCode();
    }

    @Override
    public String toString() {
        return getElement().toString();
    }

}

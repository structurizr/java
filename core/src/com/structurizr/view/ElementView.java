package com.structurizr.view;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.structurizr.model.Element;

public class ElementView {

    private Element element;
    private int id;

    public ElementView() {
    }

    public ElementView(Element element) {
        this.element = element;
    }

    @JsonIgnore
    public Element getElement() {
        return element;
    }

    public void setElement(Element element) {
        this.element = element;
    }

    public int getId() {
        if (element != null) {
            return element.getId();
        } else {
            return this.id;
        }
    }

    public void setId(int id) {
        this.id = id;
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
        return getId();
    }

    @Override
    public String toString() {
        return getElement().toString();
    }

}

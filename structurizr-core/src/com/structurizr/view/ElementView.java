package com.structurizr.view;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.structurizr.model.Element;

/**
 * Represents an instance of an Element in a View.
 */
public class ElementView {

    private Element element;
    private String id;
    private int x;
    private int y;

    ElementView() {
    }

    ElementView(Element element) {
        this.element = element;
    }

    @JsonIgnore
    public Element getElement() {
        return element;
    }

    public void setElement(Element element) {
        this.element = element;
    }

    /**
     * Gets the ID of the Element.
     *
     * @return  the ID of the Element, as a String
     */
    public String getId() {
        if (element != null) {
            return element.getId();
        } else {
            return this.id;
        }
    }

    void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the horizontal position of the element when rendered.
     *
     * @return  the X coordinate, as an int
     */
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    /**
     * Gets the vertical position of the element when rendered.
     *
     * @return  the Y coordinate, as an int
     */
    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ElementView that = (ElementView)o;

        return getId().equals(that.getId());

    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    @Override
    public String toString() {
        return getElement().toString();
    }

    void copyLayoutInformationFrom(ElementView source) {
        if (source != null) {
            setX(source.getX());
            setY(source.getY());
        }
    }

}

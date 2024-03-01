package com.structurizr.view;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.structurizr.model.Element;

/**
 * Represents an instance of an Element in a View.
 */
public final class ElementView implements Comparable<ElementView> {

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

    void setElement(Element element) {
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

    @Override
    public int compareTo(ElementView elementView) {
        try {
            int id1 = Integer.parseInt(getId());
            int id2 = Integer.parseInt(elementView.getId());

            return id1 - id2;
        } catch (NumberFormatException nfe) {
            return getId().compareTo(elementView.getId());
        }
    }

}

package com.structurizr.diagram;

import com.structurizr.model.Element;

public class Box {

    private String elementId;
    private Position position = new Position(0, 0);

    public Box(Element element) {
        this.elementId = element.getId();
    }

    public String getElementId() {
        return elementId;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "Box{" +
                "elementId=" + elementId +
                ", position=" + position +
                '}';
    }

}

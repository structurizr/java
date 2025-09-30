package com.structurizr.autolayout.graphviz;

import com.structurizr.model.Element;
import com.structurizr.view.ElementStyle;
import com.structurizr.view.Shape;
import com.structurizr.view.View;

class StyleUtils {

    private static final int DEFAULT_WIDTH = 450;
    private static final int DEFAULT_HEIGHT = 300;

    private static final int DEFAULT_WIDTH_PERSON = 400;
    private static final int DEFAULT_HEIGHT_PERSON = 400;

    static ElementStyle findElementStyle(View view, Element element) {
        ElementStyle style = view.getViewSet().getConfiguration().getStyles().findElementStyle(element);

        if (style.getWidth() == null) {
            if (style.getShape() == Shape.Person || style.getShape() == Shape.Robot) {
                style.setWidth(DEFAULT_WIDTH_PERSON);
            } else {
                style.setWidth(DEFAULT_WIDTH);
            }
        }

        if (style.getHeight() == null) {
            if (style.getShape() == Shape.Person || style.getShape() == Shape.Robot) {
                style.setHeight(DEFAULT_HEIGHT_PERSON);
            } else {
                style.setHeight(DEFAULT_HEIGHT);
            }
        }

        return style;
    }

}

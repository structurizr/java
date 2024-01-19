package com.structurizr.inspection.model;

import com.structurizr.inspection.Inspector;
import com.structurizr.inspection.Violation;
import com.structurizr.model.Element;
import com.structurizr.view.ElementView;
import com.structurizr.view.ModelView;
import com.structurizr.view.View;

import java.util.HashSet;
import java.util.Set;

public class ElementNotIncludedInAnyViewsInspection extends AbstractElementInspection {

    private final Set<String> elementsInViews = new HashSet<>();

    public ElementNotIncludedInAnyViewsInspection(Inspector inspector) {
        super(inspector);

        for (View view : getWorkspace().getViews().getViews()) {
            if (view instanceof ModelView) {
                ModelView modelView = (ModelView)view;
                for (ElementView elementView : modelView.getElements()) {
                    elementsInViews.add(elementView.getId());
                }
            }
        }
    }

    @Override
    protected Violation inspect(Element element) {
        if (!elementsInViews.contains(element.getId())) {
            return violation("The " + terminologyFor(element) + " named \"" + element.getName() + "\" is not included on any views - add it to a view.");
        }

        return noViolation();
    }

    @Override
    protected String getType() {
        return "model.element.noview";
    }

}
package com.structurizr.assistant.model;

import com.structurizr.Workspace;
import com.structurizr.assistant.Recommendation;
import com.structurizr.model.Element;
import com.structurizr.view.ElementView;
import com.structurizr.view.ModelView;
import com.structurizr.view.View;

import java.util.HashSet;
import java.util.Set;

public class ElementNotIncludedInAnyViewsInspection extends ElementInspection {

    private final Set<String> elementsInViews = new HashSet<>();

    public ElementNotIncludedInAnyViewsInspection(Workspace workspace) {
        super(workspace);

        for (View view : workspace.getViews().getViews()) {
            if (view instanceof ModelView) {
                ModelView modelView = (ModelView)view;
                for (ElementView elementView : modelView.getElements()) {
                    elementsInViews.add(elementView.getId());
                }
            }
        }
    }

    @Override
    protected Recommendation inspect(Element element) {
        if (!elementsInViews.contains(element.getId())) {
            return lowPriorityRecommendation("The " + terminologyFor(element) + " named \"" + element.getName() + "\" is not included on any views - add it to a view.");
        }

        return noRecommendation();
    }

    @Override
    protected String getType() {
        return "model.element.noview";
    }

}
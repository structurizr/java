package com.structurizr.assistant.model;

import com.structurizr.Workspace;
import com.structurizr.assistant.Inspection;
import com.structurizr.assistant.Recommendation;
import com.structurizr.model.Element;

abstract class ElementInspection extends Inspection {

    public ElementInspection(Workspace workspace) {
        super(workspace);
    }

    public final Recommendation run(Element element) {
        Element parentElement = element.getParent();

        if (isEnabled(getType(), getWorkspace(), getWorkspace().getModel(), parentElement, element)) {
            return inspect(element);
        }

        return noRecommendation();
    }

    protected String terminologyFor(Element element) {
        return getWorkspace().getViews().getConfiguration().getTerminology().findTerminology(element).toLowerCase();
    }

    protected abstract Recommendation inspect(Element element);

}
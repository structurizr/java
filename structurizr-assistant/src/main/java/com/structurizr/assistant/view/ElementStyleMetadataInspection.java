package com.structurizr.assistant.view;

import com.structurizr.Workspace;
import com.structurizr.assistant.Inspection;
import com.structurizr.assistant.Recommendation;
import com.structurizr.model.Element;
import com.structurizr.view.ElementStyle;
import com.structurizr.view.SystemContextView;

import java.util.HashSet;
import java.util.Set;

public class ElementStyleMetadataInspection extends Inspection {

    public ElementStyleMetadataInspection(Workspace workspace) {
        super(workspace);
    }

    public final Recommendation run(ElementStyle elementStyle) {
        if (isEnabled(getType(), getWorkspace(), getWorkspace().getViews().getConfiguration())) {
            return inspect(elementStyle);
        }

        return noRecommendation();
    }

    public Recommendation inspect(ElementStyle elementStyle) {
        if (elementStyle.getMetadata() != null && !elementStyle.getMetadata()) {
            return lowPriorityRecommendation("The element style for tag \"" + elementStyle.getTag() + "\" has metadata hidden, which may introduce ambiguity on rendered diagrams.");
        }

        return noRecommendation();
    }

    @Override
    protected String getType() {
        return "views.styles.element.metadata";
    }

}
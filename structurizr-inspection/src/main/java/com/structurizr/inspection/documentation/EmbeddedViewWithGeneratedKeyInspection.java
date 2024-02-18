package com.structurizr.inspection.documentation;

import com.structurizr.Workspace;
import com.structurizr.documentation.Documentable;
import com.structurizr.inspection.Inspector;
import com.structurizr.inspection.Violation;
import com.structurizr.model.Element;
import com.structurizr.view.View;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmbeddedViewWithGeneratedKeyInspection extends AbstractDocumentableInspection {

    public EmbeddedViewWithGeneratedKeyInspection(Inspector inspector) {
        super(inspector);
    }

    protected Violation inspect(Documentable documentable) {
        Set<String> keys = findEmbeddedViewKeys(documentable);
        Set<String> viewsWithGeneratedKeys = new LinkedHashSet<>();

        for (String key : keys) {
            View view = getWorkspace().getViews().getViewWithKey(key);
            if (view != null && view.isGeneratedKey()) {
                viewsWithGeneratedKeys.add(key);
            }
        }

        if (!viewsWithGeneratedKeys.isEmpty()) {
            if (documentable instanceof Workspace) {
                return violation("The following views are embedded into documentation for the workspace via an automatically generated view key: " + String.join(", ", viewsWithGeneratedKeys));
            } else if (documentable instanceof Element) {
                Element element = (Element)documentable;
                return violation("The following views are embedded into documentation for the " + terminologyFor(element).toLowerCase() + " named \"" + element.getName() + "\" via an automatically generated view key: " + String.join(", ", viewsWithGeneratedKeys));
            }
        }

        return noViolation();
    }

    @Override
    protected String getType() {
        return "documentation.embeddedView";
    }

}
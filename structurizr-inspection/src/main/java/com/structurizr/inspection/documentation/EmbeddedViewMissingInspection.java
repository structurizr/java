package com.structurizr.inspection.documentation;

import com.structurizr.Workspace;
import com.structurizr.documentation.Documentable;
import com.structurizr.documentation.Format;
import com.structurizr.documentation.Section;
import com.structurizr.inspection.Inspector;
import com.structurizr.inspection.Violation;
import com.structurizr.model.Element;
import com.structurizr.view.View;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmbeddedViewMissingInspection extends AbstractDocumentableInspection {

    public EmbeddedViewMissingInspection(Inspector inspector) {
        super(inspector);
    }

    protected Violation inspect(Documentable documentable) {
        Set<String> keys = findEmbeddedViewKeys(documentable);
        Set<String> missingViews = new LinkedHashSet<>();

        for (String key : keys) {
            View view = getWorkspace().getViews().getViewWithKey(key);
            if (view == null) {
                missingViews.add(key);
            }
        }

        if (!missingViews.isEmpty()) {
            if (documentable instanceof Workspace) {
                return violation("The following views are embedded into documentation for the workspace but do not exist in the workspace: " + String.join(", ", missingViews));
            } else if (documentable instanceof Element) {
                Element element = (Element)documentable;
                return violation("The following views are embedded into documentation for the " + terminologyFor(element).toLowerCase() + " \"" + nameOf(element) + "\" but do not exist in the workspace: " + String.join(", ", missingViews));
            }
        }

        return noViolation();
    }

    @Override
    protected String getType() {
        return "documentation.embeddedview";
    }

}
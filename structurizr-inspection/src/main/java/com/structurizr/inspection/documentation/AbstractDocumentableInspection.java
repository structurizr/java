package com.structurizr.inspection.documentation;

import com.structurizr.Workspace;
import com.structurizr.documentation.*;
import com.structurizr.inspection.Inspection;
import com.structurizr.inspection.Inspector;
import com.structurizr.inspection.Severity;
import com.structurizr.inspection.Violation;
import com.structurizr.model.Element;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractDocumentableInspection extends Inspection {

    private static final Pattern MARKDOWN_EMBED = Pattern.compile("!\\[.*?]\\(embed:(.+?)\\)");
    private static final Pattern ASCIIDOC_EMBED = Pattern.compile("image::embed:(.+?)\\[]");

    public AbstractDocumentableInspection(Inspector inspector) {
        super(inspector);
    }

    public final Violation run(Documentable documentable) {
        Severity severity;
        if (documentable instanceof Workspace) {
            severity = getInspector().getSeverityStrategy().getSeverity(this, (Workspace)documentable);
        } else {
            Element element = (Element)documentable;
            severity = getInspector().getSeverityStrategy().getSeverity(this, element);
        }
        Violation violation = inspect(documentable);

        return violation == null ? null : violation.withSeverity(severity);
    }

    protected abstract Violation inspect(Documentable documentable);

    protected Set<String> findEmbeddedViewKeys(Documentable documentable) {
        Set<String> keys = new LinkedHashSet<>();

        for (Section section : documentable.getDocumentation().getSections()) {
            keys.addAll(findEmbeddedViewKeys(section));
        }

        for (Decision decision : documentable.getDocumentation().getDecisions()) {
            keys.addAll(findEmbeddedViewKeys(decision));
        }

        return keys;
    }

    private Set<String> findEmbeddedViewKeys(DocumentationContent content) {
        Set<String> keys = new LinkedHashSet<>();

        String[] lines = content.getContent().split("\n");
        for (String line : lines) {
            if (content.getFormat() == Format.Markdown) {
                // ![](embed:MyDiagramKey)
                Matcher matcher = MARKDOWN_EMBED.matcher(line);
                if (matcher.matches()) {
                    String key = matcher.group(1);
                    keys.add(key);
                }
            } else if (content.getFormat() == Format.AsciiDoc) {
                // image::embed:MyDiagramKey[]
                Matcher matcher = ASCIIDOC_EMBED.matcher(line);
                if (matcher.matches()) {
                    String key = matcher.group(1);
                    keys.add(key);
                }
            }
        }

        return keys;
    }

    protected String terminologyFor(Element element) {
        return getWorkspace().getViews().getConfiguration().getTerminology().findTerminology(element).toLowerCase();
    }

}
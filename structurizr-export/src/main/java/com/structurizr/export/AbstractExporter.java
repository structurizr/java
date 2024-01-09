package com.structurizr.export;

import com.structurizr.Workspace;
import com.structurizr.model.*;
import com.structurizr.util.StringUtils;
import com.structurizr.view.*;

public abstract class AbstractExporter {

    protected String breakText(int maxWidth, int fontSize, String s) {
        if (StringUtils.isNullOrEmpty(s)) {
            return "";
        }

        StringBuilder buf = new StringBuilder();

        double characterWidth = fontSize * 0.6;
        int maxCharacters = (int)(maxWidth / characterWidth);

        if (s.length() < maxCharacters) {
            return s;
        }

        String[] words = s.split(" ");
        String line = null;
        for (String word : words) {
            if (line == null) {
                line = word;
            } else {
                if ((line.length() + word.length() + 1) < maxCharacters) {
                    line += " ";
                    line += word;
                } else {
                    buf.append(line);
                    buf.append("<br />");
                    line = word;
                }
            }
        }

        if (line != null) {
            buf.append(line);
        }

        return buf.toString();
    }

    protected String typeOf(Workspace workspace, Element e, boolean includeMetadataSymbols) {
        return typeOf(workspace.getViews().getConfiguration(), e, includeMetadataSymbols);
    }

    protected String typeOf(ModelView view, Element e, boolean includeMetadataSymbols) {
        return typeOf(view.getViewSet().getConfiguration(), e, includeMetadataSymbols);
    }

    private String typeOf(Configuration configuration, Element e, boolean includeMetadataSymbols) {
        String type = "";

        if (e instanceof Person) {
            type = configuration.getTerminology().findTerminology(e);
        } else if (e instanceof SoftwareSystem) {
            type = configuration.getTerminology().findTerminology(e);
        } else if (e instanceof Container) {
            Container container = (Container)e;
            type = configuration.getTerminology().findTerminology(e) + (hasValue(container.getTechnology()) ? ": " + container.getTechnology() : "");
        } else if (e instanceof Component) {
            Component component = (Component)e;
            type = configuration.getTerminology().findTerminology(e) + (hasValue(component.getTechnology()) ? ": " + component.getTechnology() : "");
        } else if (e instanceof DeploymentNode) {
            DeploymentNode deploymentNode = (DeploymentNode)e;
            type = configuration.getTerminology().findTerminology(e) + (hasValue(deploymentNode.getTechnology()) ? ": " + deploymentNode.getTechnology() : "");
        } else if (e instanceof InfrastructureNode) {
            InfrastructureNode infrastructureNode = (InfrastructureNode)e;
            type = configuration.getTerminology().findTerminology(e) + (hasValue(infrastructureNode.getTechnology()) ? ": " + infrastructureNode.getTechnology() : "");
        } else if (e instanceof CustomElement) {
            type = ((CustomElement)e).getMetadata();
        }

        if (StringUtils.isNullOrEmpty(type)) {
            return type;
        }

        if (includeMetadataSymbols) {
            if (configuration.getMetadataSymbols() == null) {
                configuration.setMetadataSymbols(MetadataSymbols.SquareBrackets);
            }

            switch (configuration.getMetadataSymbols()) {
                case RoundBrackets:
                    return "(" + type + ")";
                case CurlyBrackets:
                    return "{" + type + "}";
                case AngleBrackets:
                    return "<" + type + ">";
                case DoubleAngleBrackets:
                    return "<<" + type + ">>";
                case None:
                    return type;
                default:
                    return "[" + type + "]";
            }
        } else {
            return type;
        }
    }

    protected boolean hasValue(String s) {
        return !StringUtils.isNullOrEmpty(s);
    }

    protected ElementStyle findElementStyle(ModelView view, Element element) {
        return view.getViewSet().getConfiguration().getStyles().findElementStyle(element);
    }

    protected RelationshipStyle findRelationshipStyle(ModelView view, Relationship relationship) {
        return view.getViewSet().getConfiguration().getStyles().findRelationshipStyle(relationship);
    }

}
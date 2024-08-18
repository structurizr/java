package com.structurizr.dsl;

import com.structurizr.model.*;
import com.structurizr.util.StringUtils;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static com.structurizr.dsl.StructurizrDslExpressions.*;

class ExpressionParser {

    private static final String WILDCARD = "*";

    static boolean isExpression(String token) {
        token = token.toLowerCase();

        return
                token.startsWith(ELEMENT_TYPE_EQUALS_EXPRESSION.toLowerCase()) ||
                        token.startsWith(ELEMENT_TAG_EQUALS_EXPRESSION.toLowerCase()) ||
                        token.startsWith(ELEMENT_TAG_NOT_EQUALS_EXPRESSION.toLowerCase()) ||
                        token.startsWith(ELEMENT_TECHNOLOGY_EQUALS_EXPRESSION.toLowerCase()) ||
                        token.startsWith(ELEMENT_TECHNOLOGY_NOT_EQUALS_EXPRESSION.toLowerCase()) ||
                        token.matches(ELEMENT_PROPERTY_EQUALS_EXPRESSION) ||
                        token.startsWith(ELEMENT_PARENT_EQUALS_EXPRESSION.toLowerCase()) ||
                        token.startsWith(RELATIONSHIP) || token.endsWith(RELATIONSHIP) || token.contains(RELATIONSHIP) ||
                        token.startsWith(ELEMENT_EQUALS_EXPRESSION) ||
                        token.startsWith(RELATIONSHIP_TAG_EQUALS_EXPRESSION.toLowerCase()) ||
                        token.startsWith(RELATIONSHIP_TAG_NOT_EQUALS_EXPRESSION.toLowerCase()) ||
                        token.matches(RELATIONSHIP_PROPERTY_EQUALS_EXPRESSION) ||
                        token.startsWith(RELATIONSHIP_SOURCE_EQUALS_EXPRESSION.toLowerCase()) ||
                        token.startsWith(RELATIONSHIP_DESTINATION_EQUALS_EXPRESSION.toLowerCase()) ||
                        token.startsWith(RELATIONSHIP_EQUALS_EXPRESSION);
    }


    final Set<ModelItem> parseExpression(String expr, DslContext context) {
        if (expr.contains(" && ")) {
            String[] expressions = expr.split(" && ");
            Set<ModelItem> modelItems1 = evaluateExpression(expressions[0], context);
            Set<ModelItem> modelItems2 = evaluateExpression(expressions[1], context);

            Set<ModelItem> modelItems = new HashSet<>(modelItems1);
            modelItems.retainAll(modelItems2);

            return modelItems;
        } else if (expr.contains(" || ")) {
            String[] expressions = expr.split(" \\|\\| ");
            Set<ModelItem> modelItems1 = evaluateExpression(expressions[0], context);
            Set<ModelItem> modelItems2 = evaluateExpression(expressions[1], context);

            Set<ModelItem> modelItems = new HashSet<>(modelItems1);
            modelItems.addAll(modelItems2);

            return modelItems;
        } else {
            return evaluateExpression(expr, context);
        }
    }

    private Set<ModelItem> evaluateExpression(String expr, DslContext context) {
        Set<ModelItem> modelItems = new LinkedHashSet<>();

        if (expr.startsWith(ELEMENT_EQUALS_EXPRESSION)) {
            expr = expr.substring(ELEMENT_EQUALS_EXPRESSION.length());

            if (isExpression(expr)) {
                modelItems.addAll(evaluateExpression(expr, context));
            } else {
                modelItems.addAll(parseIdentifier(expr, context));
            }
        } else if (expr.startsWith(RELATIONSHIP_EQUALS_EXPRESSION)) {
            expr = expr.substring(RELATIONSHIP_EQUALS_EXPRESSION.length());

            if (WILDCARD.equals(expr)) {
                expr = WILDCARD + RELATIONSHIP + WILDCARD;
            }

            if (isExpression(expr)) {
                modelItems.addAll(evaluateExpression(expr, context));
            } else {
                modelItems.addAll(parseIdentifier(expr, context));
            }
        } else if (RELATIONSHIP.equals(expr)) {
            throw new RuntimeException("Unexpected identifier \"->\"");
        } else if (expr.startsWith(RELATIONSHIP) || expr.endsWith(RELATIONSHIP)) {
            // this is an element expression: ->identifier identifier-> ->identifier->
            boolean includeAfferentCouplings = false;
            boolean includeEfferentCouplings = false;

            String identifier = expr;

            if (identifier.startsWith(RELATIONSHIP)) {
                includeAfferentCouplings = true;
                identifier = identifier.substring(RELATIONSHIP.length());
            }
            if (identifier.endsWith(RELATIONSHIP)) {
                includeEfferentCouplings = true;
                identifier = identifier.substring(0, identifier.length() - RELATIONSHIP.length());
            }

            identifier = identifier.trim();
            Set<Element> elements;

            if (isExpression(identifier)) {
                elements = parseExpression(identifier, context).stream().filter(mi -> mi instanceof Element).map(mi -> (Element)mi).collect(Collectors.toSet());
            } else {
                elements = getElements(identifier, context);
            }

            if (elements.isEmpty()) {
                throw new RuntimeException("The element \"" + identifier + "\" does not exist");
            }

            for (Element element : elements) {
                modelItems.add(element);

                if (includeAfferentCouplings) {
                    modelItems.addAll(findAfferentCouplings(element));
                }

                if (includeEfferentCouplings) {
                    modelItems.addAll(findEfferentCouplings(element));
                }
            }
        } else if (expr.contains(RELATIONSHIP)) {
            String[] identifiers = expr.split(RELATIONSHIP);
            String sourceIdentifier = identifiers[0].trim();
            String destinationIdentifier = identifiers[1].trim();

            String sourceExpression = RELATIONSHIP_SOURCE_EQUALS_EXPRESSION + sourceIdentifier;
            String destinationExpression = RELATIONSHIP_DESTINATION_EQUALS_EXPRESSION + destinationIdentifier;

            if (WILDCARD.equals(sourceIdentifier) && WILDCARD.equals(destinationIdentifier)) {
                modelItems.addAll(context.getWorkspace().getModel().getRelationships());
            } else if (WILDCARD.equals(destinationIdentifier)) {
                modelItems.addAll(parseExpression(sourceExpression, context));
            } else if (WILDCARD.equals(sourceIdentifier)) {
                modelItems.addAll(parseExpression(destinationExpression, context));
            } else {
                modelItems.addAll(parseExpression(sourceExpression + " && " + destinationExpression, context));
            }
        } else if (expr.toLowerCase().startsWith(ELEMENT_PARENT_EQUALS_EXPRESSION)) {
            String parentIdentifier = expr.substring(ELEMENT_PARENT_EQUALS_EXPRESSION.length());
            Element parentElement = context.getElement(parentIdentifier);
            if (parentElement == null) {
                throw new RuntimeException("The parent element \"" + parentIdentifier + "\" does not exist");
            } else {
                context.getWorkspace().getModel().getElements().forEach(element -> {
                    if (element.getParent() == parentElement) {
                        modelItems.add(element);
                    }
                });
            }
        } else if (expr.toLowerCase().startsWith(ELEMENT_TYPE_EQUALS_EXPRESSION)) {
            modelItems.addAll(evaluateElementTypeExpression(expr, context));
        } else if (expr.toLowerCase().startsWith(ELEMENT_TAG_EQUALS_EXPRESSION.toLowerCase())) {
            String[] tags = expr.substring(ELEMENT_TAG_EQUALS_EXPRESSION.length()).split(",");
            context.getWorkspace().getModel().getElements().forEach(element -> {
                if (hasAllTags(element, tags)) {
                    modelItems.add(element);
                }
            });
        } else if (expr.toLowerCase().startsWith(ELEMENT_TAG_NOT_EQUALS_EXPRESSION)) {
            String[] tags = expr.substring(ELEMENT_TAG_NOT_EQUALS_EXPRESSION.length()).split(",");
            context.getWorkspace().getModel().getElements().forEach(element -> {
                if (!hasAllTags(element, tags)) {
                    modelItems.add(element);
                }
            });
        } else if (expr.toLowerCase().startsWith(ELEMENT_TECHNOLOGY_EQUALS_EXPRESSION.toLowerCase())) {
            String technology = expr.substring(ELEMENT_TECHNOLOGY_EQUALS_EXPRESSION.length());
            modelItems.addAll(context.getWorkspace().getModel().getElements().stream().filter(e -> e instanceof Container).map(e -> (Container)e).filter(c -> technology.equals(c.getTechnology())).collect(Collectors.toSet()));
            modelItems.addAll(context.getWorkspace().getModel().getElements().stream().filter(e -> e instanceof Component).map(e -> (Component)e).filter(c -> technology.equals(c.getTechnology())).collect(Collectors.toSet()));
            modelItems.addAll(context.getWorkspace().getModel().getElements().stream().filter(e -> e instanceof DeploymentNode).map(e -> (DeploymentNode)e).filter(dn -> technology.equals(dn.getTechnology())).collect(Collectors.toSet()));
            modelItems.addAll(context.getWorkspace().getModel().getElements().stream().filter(e -> e instanceof InfrastructureNode).map(e -> (InfrastructureNode)e).filter(in -> technology.equals(in.getTechnology())).collect(Collectors.toSet()));
            modelItems.addAll(context.getWorkspace().getModel().getElements().stream().filter(e -> e instanceof ContainerInstance).map(e -> (ContainerInstance)e).filter(c -> technology.equals(c.getContainer().getTechnology())).collect(Collectors.toSet()));
        } else if (expr.toLowerCase().startsWith(ELEMENT_TECHNOLOGY_NOT_EQUALS_EXPRESSION)) {
            String technology = expr.substring(ELEMENT_TECHNOLOGY_NOT_EQUALS_EXPRESSION.length());
            modelItems.addAll(context.getWorkspace().getModel().getElements().stream().filter(e -> e instanceof Container).map(e -> (Container)e).filter(c -> !technology.equals(c.getTechnology())).collect(Collectors.toSet()));
            modelItems.addAll(context.getWorkspace().getModel().getElements().stream().filter(e -> e instanceof Component).map(e -> (Component)e).filter(c -> !technology.equals(c.getTechnology())).collect(Collectors.toSet()));
            modelItems.addAll(context.getWorkspace().getModel().getElements().stream().filter(e -> e instanceof DeploymentNode).map(e -> (DeploymentNode)e).filter(dn -> !technology.equals(dn.getTechnology())).collect(Collectors.toSet()));
            modelItems.addAll(context.getWorkspace().getModel().getElements().stream().filter(e -> e instanceof InfrastructureNode).map(e -> (InfrastructureNode)e).filter(in -> !technology.equals(in.getTechnology())).collect(Collectors.toSet()));
            modelItems.addAll(context.getWorkspace().getModel().getElements().stream().filter(e -> e instanceof ContainerInstance).map(e -> (ContainerInstance)e).filter(c -> !technology.equals(c.getContainer().getTechnology())).collect(Collectors.toSet()));
        } else if (expr.matches(ELEMENT_PROPERTY_EQUALS_EXPRESSION)) {
            String propertyName = expr.substring(expr.indexOf("[")+1, expr.indexOf("]"));
            String propertyValue = expr.substring(expr.indexOf("==")+2);

            context.getWorkspace().getModel().getElements().forEach(element -> {
                if (hasProperty(element, propertyName, propertyValue)) {
                    modelItems.add(element);
                }
            });
        } else if (expr.startsWith(RELATIONSHIP_TAG_EQUALS_EXPRESSION)) {
            String[] tags = expr.substring(RELATIONSHIP_TAG_EQUALS_EXPRESSION.length()).split(",");
            context.getWorkspace().getModel().getRelationships().forEach(relationship -> {
                if (hasAllTags(relationship, tags)) {
                    modelItems.add(relationship);
                }
            });
        } else if (expr.startsWith(RELATIONSHIP_TAG_NOT_EQUALS_EXPRESSION)) {
            String[] tags = expr.substring(RELATIONSHIP_TAG_NOT_EQUALS_EXPRESSION.length()).split(",");
            context.getWorkspace().getModel().getRelationships().forEach(relationship -> {
                if (!hasAllTags(relationship, tags)) {
                    modelItems.add(relationship);
                }
            });
        } else if (expr.matches(RELATIONSHIP_PROPERTY_EQUALS_EXPRESSION)) {
            String propertyName = expr.substring(expr.indexOf("[")+1, expr.indexOf("]"));
            String propertyValue = expr.substring(expr.indexOf("==")+2);

            context.getWorkspace().getModel().getRelationships().forEach(relationship -> {
                if (hasProperty(relationship, propertyName, propertyValue)) {
                    modelItems.add(relationship);
                }
            });
        } else if (expr.startsWith(RELATIONSHIP_SOURCE_EQUALS_EXPRESSION)) {
            String identifier = expr.substring(RELATIONSHIP_SOURCE_EQUALS_EXPRESSION.length());
            Set<Element> sourceElements = new HashSet<>();

            if (isExpression(identifier)) {
                Set<ModelItem> set = parseExpression(identifier, context);
                for (ModelItem modelItem : set) {
                    if (modelItem instanceof Element) {
                        sourceElements.add((Element)modelItem);
                    }
                }
            } else {
                Element source = context.getElement(identifier);
                if (source == null) {
                    throw new RuntimeException("The element \"" + identifier + "\" does not exist");
                }

                if (source instanceof ElementGroup) {
                    sourceElements.addAll(((ElementGroup) source).getElements());
                } else {
                    sourceElements.add(source);
                }
            }

            context.getWorkspace().getModel().getRelationships().forEach(relationship -> {
                if (sourceElements.contains(relationship.getSource())) {
                    modelItems.add(relationship);
                }
            });
        } else if (expr.startsWith(RELATIONSHIP_DESTINATION_EQUALS_EXPRESSION)) {
            String identifier = expr.substring(RELATIONSHIP_DESTINATION_EQUALS_EXPRESSION.length());
            Set<Element> destinationElements = new HashSet<>();

            if (isExpression(identifier)) {
                Set<ModelItem> set = parseExpression(identifier, context);
                for (ModelItem modelItem : set) {
                    if (modelItem instanceof Element) {
                        destinationElements.add((Element)modelItem);
                    }
                }
            } else {
                Element destination = context.getElement(identifier);
                if (destination == null) {
                    throw new RuntimeException("The element \"" + identifier + "\" does not exist");
                }

                if (destination instanceof ElementGroup) {
                    destinationElements.addAll(((ElementGroup) destination).getElements());
                } else {
                    destinationElements.add(destination);
                }
            }

            context.getWorkspace().getModel().getRelationships().forEach(relationship -> {
                if (destinationElements.contains(relationship.getDestination())) {
                    modelItems.add(relationship);
                }
            });
        }

        return modelItems;
    }

    protected Set<Element> evaluateElementTypeExpression(String expr, DslContext context) {
        Set<Element> elements = new LinkedHashSet<>();

        String type = expr.substring(ELEMENT_TYPE_EQUALS_EXPRESSION.length());
        switch (type.toLowerCase()) {
            case "custom":
                context.getWorkspace().getModel().getElements().stream().filter(e -> e instanceof CustomElement).forEach(elements::add);
                break;
            case "person":
                context.getWorkspace().getModel().getElements().stream().filter(e -> e instanceof Person).forEach(elements::add);
                break;
            case "softwaresystem":
                context.getWorkspace().getModel().getElements().stream().filter(e -> e instanceof SoftwareSystem).forEach(elements::add);
                break;
            case "container":
                context.getWorkspace().getModel().getElements().stream().filter(e -> e instanceof Container).forEach(elements::add);
                break;
            case "component":
                context.getWorkspace().getModel().getElements().stream().filter(e -> e instanceof Component).forEach(elements::add);
                break;
            case "deploymentnode":
                context.getWorkspace().getModel().getElements().stream().filter(e -> e instanceof DeploymentNode).forEach(elements::add);
                break;
            case "infrastructurenode":
                context.getWorkspace().getModel().getElements().stream().filter(e -> e instanceof InfrastructureNode).forEach(elements::add);
                break;
            case "softwaresysteminstance":
                context.getWorkspace().getModel().getElements().stream().filter(e -> e instanceof SoftwareSystemInstance).forEach(elements::add);
                break;
            case "containerinstance":
                context.getWorkspace().getModel().getElements().stream().filter(e -> e instanceof ContainerInstance).forEach(elements::add);
                break;
        }

        return elements;
    }

    private boolean hasAllTags(ModelItem modelItem, String[] tags) {
        boolean result = true;

        for (String tag : tags) {
            boolean hasTag = modelItem.hasTag(tag.trim());

            if (!hasTag) {
                // perhaps the tag is instead on a related model item?
                if (modelItem instanceof StaticStructureElementInstance) {
                    StaticStructureElementInstance elementInstance = (StaticStructureElementInstance)modelItem;
                    hasTag = elementInstance.getElement().hasTag(tag.trim());
                } else if (modelItem instanceof Relationship) {
                    Relationship relationship = (Relationship)modelItem;
                    if (!StringUtils.isNullOrEmpty(relationship.getLinkedRelationshipId())) {
                        Relationship linkedRelationship = relationship.getModel().getRelationship(relationship.getLinkedRelationshipId());
                        if (linkedRelationship != null) {
                            hasTag = linkedRelationship.hasTag(tag.trim());
                        }
                    }
                }
            }

            result = result && hasTag;
        }

        return result;
    }

    private boolean hasProperty(ModelItem modelItem, String name, String value) {
        boolean result = modelItem.hasProperty(name, value);

        if (!result) {
            // perhaps the property is instead on a related model item?
            if (modelItem instanceof StaticStructureElementInstance) {
                StaticStructureElementInstance elementInstance = (StaticStructureElementInstance)modelItem;
                result = elementInstance.getElement().hasProperty(name, value);
            } else if (modelItem instanceof Relationship) {
                Relationship relationship = (Relationship)modelItem;
                if (!StringUtils.isNullOrEmpty(relationship.getLinkedRelationshipId())) {
                    Relationship linkedRelationship = relationship.getModel().getRelationship(relationship.getLinkedRelationshipId());
                    if (linkedRelationship != null) {
                        result = linkedRelationship.hasProperty(name, value);
                    }
                }
            }
        }

        return result;
    }

    protected Set<Element> findAfferentCouplings(Element element) {
        return new LinkedHashSet<>(findAfferentCouplings(element, Element.class));
    }

    protected <T extends Element> Set<Element> findAfferentCouplings(Element element, Class<T> typeOfElement) {
        Set<Element> elements = new LinkedHashSet<>();

        Set<Relationship> relationships = element.getModel().getRelationships();
        relationships.stream().filter(r -> r.getDestination().equals(element) && typeOfElement.isInstance(r.getSource()))
                .map(Relationship::getSource)
                .forEach(elements::add);

        return elements;
    }

    protected Set<Element> findEfferentCouplings(Element element) {
        return new LinkedHashSet<>(findEfferentCouplings(element, Element.class));
    }

    protected <T extends Element> Set<Element> findEfferentCouplings(Element element, Class<T> typeOfElement) {
        Set<Element> elements = new LinkedHashSet<>();

        Set<Relationship> relationships = element.getModel().getRelationships();
        relationships.stream().filter(r -> r.getSource().equals(element) && typeOfElement.isInstance(r.getDestination()))
                .map(Relationship::getDestination)
                .forEach(elements::add);

        return elements;
    }

    protected Set<ModelItem> parseIdentifier(String identifier, DslContext context) {
        Set<ModelItem> modelItems = new LinkedHashSet<>();

        Element element = context.getElement(identifier);
        if (element != null) {
            modelItems.addAll(getElements(identifier, context));
        }

        Relationship relationship = context.getRelationship(identifier);
        if (relationship != null) {
            modelItems.add(relationship);

            // and also find all relationships linked to it (i.e. implied and replicated relationships)
            relationship.getModel().getRelationships().stream().filter(r -> relationship.getId().equals(r.getLinkedRelationshipId())).forEach(modelItems::add);
        }

        if (modelItems.isEmpty()) {
            throw new RuntimeException("The element/relationship \"" + identifier + "\" does not exist");
        } else {
            return modelItems;
        }
    }

    private Set<Element> getElements(String identifier, DslContext context) {
        Set<Element> elements = new HashSet<>();

        Element element = context.getElement(identifier);
        if (element != null) {
            if (element instanceof ElementGroup) {
                ElementGroup group = (ElementGroup) element;
                elements.addAll(group.getElements());
            } else {
                elements.add(element);
            }
        }

        return elements;
    }

}
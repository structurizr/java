package com.structurizr.dsl;

import com.structurizr.model.Element;
import com.structurizr.model.ModelItem;
import com.structurizr.model.StaticStructureElement;
import com.structurizr.view.StaticView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

final class StaticViewAnimationStepParser extends AbstractParser {

    private static final String GRAMMAR = "<identifier|element expression> [identifier|element expression...]";

    void parse(StaticViewDslContext context, Tokens tokens) {
        // animationStep <identifier|element expression> [identifier|element expression...]

        if (!tokens.includes(1)) {
            throw new RuntimeException("Expected: animationStep " + GRAMMAR);
        }

        parse(context, context.getView(), tokens, 1);
    }

    void parse(StaticViewAnimationDslContext context, Tokens tokens) {
        // <identifier|element expression> [identifier|element expression...]

        if (!tokens.includes(0)) {
            throw new RuntimeException("Expected: " + GRAMMAR);
        }

        parse(context, context.getView(), tokens, 0);
    }

    private void parse(DslContext context, StaticView view, Tokens tokens, int startIndex) {
        List<StaticStructureElement> staticStructureElements = new ArrayList<>();

        for (int i = startIndex; i < tokens.size(); i++) {
            String token = tokens.get(i);

            if (ExpressionParser.isExpression(token.toLowerCase())) {
                Set<ModelItem> elements = new StaticViewExpressionParser().parseExpression(token, context);

                for (ModelItem element : elements) {
                    if (element instanceof StaticStructureElement) {
                        staticStructureElements.add((StaticStructureElement)element);
                    }
                }
            } else {
                Set<ModelItem> elements = new StaticViewExpressionParser().parseIdentifier(token, context);

                if (elements.isEmpty()) {
                    throw new RuntimeException("The element \"" + token + "\" does not exist");
                }

                for (ModelItem element : elements) {
                    if (element instanceof StaticStructureElement) {
                        staticStructureElements.add((StaticStructureElement)element);
                    }
                }
            }
        }

        if (!staticStructureElements.isEmpty()) {
            view.addAnimation(staticStructureElements.toArray(new Element[0]));
        } else {
            throw new RuntimeException("No elements were found");
        }
    }

}
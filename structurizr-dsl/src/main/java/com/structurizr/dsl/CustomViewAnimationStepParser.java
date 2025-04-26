package com.structurizr.dsl;

import com.structurizr.model.*;
import com.structurizr.view.CustomView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

final class CustomViewAnimationStepParser extends AbstractParser {

    private static final String GRAMMAR = "<identifier|element expression> [identifier|element expression...]";

    void parse(CustomViewDslContext context, Tokens tokens) {
        // animationStep <identifier|element expression> [identifier|element expression...]

        if (!tokens.includes(1)) {
            throw new RuntimeException("Expected: animationStep " + GRAMMAR);
        }

        parse(context, context.getCustomView(), tokens, 1);
    }

    void parse(CustomViewAnimationDslContext context, Tokens tokens) {
        // <identifier|element expression> [identifier|element expression...]

        if (!tokens.includes(0)) {
            throw new RuntimeException("Expected: " + GRAMMAR);
        }

        parse(context, context.getView(), tokens, 0);
    }

    void parse(DslContext context, CustomView view, Tokens tokens, int startIndex) {
        List<CustomElement> customElements = new ArrayList<>();

        for (int i = startIndex; i < tokens.size(); i++) {
            String token = tokens.get(i);

            if (ExpressionParser.isExpression(token.toLowerCase())) {
                Set<ModelItem> elements = new CustomViewExpressionParser().parseExpression(token, context);

                for (ModelItem element : elements) {
                    if (element instanceof CustomElement) {
                        customElements.add((CustomElement)element);
                    }
                }
            } else {
                Set<ModelItem> elements = new CustomViewExpressionParser().parseIdentifier(token, context);

                if (elements.isEmpty()) {
                    throw new RuntimeException("The element \"" + token + "\" does not exist");
                }

                for (ModelItem element : elements) {
                    if (element instanceof CustomElement) {
                        customElements.add((CustomElement)element);
                    }
                }
            }
        }

        if (!customElements.isEmpty()) {
            view.addAnimation(customElements.toArray(new CustomElement[0]));
        } else {
            throw new RuntimeException("No custom elements were found");
        }
    }

}
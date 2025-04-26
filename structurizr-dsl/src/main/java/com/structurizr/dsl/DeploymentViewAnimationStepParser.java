package com.structurizr.dsl;

import com.structurizr.model.*;
import com.structurizr.view.DeploymentView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

final class DeploymentViewAnimationStepParser extends AbstractParser {

    private static final String GRAMMAR = "<identifier|element expression> [identifier|element expression...]";

    void parse(DeploymentViewDslContext context, Tokens tokens) {
        // animationStep <identifier|element expression> [identifier|element expression...]

        if (!tokens.includes(1)) {
            throw new RuntimeException("Expected: animationStep " + GRAMMAR);
        }

        parse(context, context.getView(), tokens, 1);
    }

    void parse(DeploymentViewAnimationDslContext context, Tokens tokens) {
        // <identifier|element expression> [identifier|element expression...]

        if (!tokens.includes(0)) {
            throw new RuntimeException("Expected: " + GRAMMAR);
        }

        parse(context, context.getView(), tokens, 0);
    }

    private void parse(DslContext context, DeploymentView view, Tokens tokens, int startIndex) {
        List<StaticStructureElementInstance> staticStructureElementInstances = new ArrayList<>();
        List<InfrastructureNode> infrastructureNodes = new ArrayList<>();

        for (int i = startIndex; i < tokens.size(); i++) {
            String token = tokens.get(i);

            if (ExpressionParser.isExpression(token.toLowerCase())) {
                Set<ModelItem> elements = new DeploymentViewExpressionParser().parseExpression(token, context);

                for (ModelItem element : elements) {
                    if (element instanceof StaticStructureElementInstance) {
                        staticStructureElementInstances.add((StaticStructureElementInstance)element);
                    }

                    if (element instanceof InfrastructureNode) {
                        infrastructureNodes.add((InfrastructureNode)element);
                    }
                }
            } else {
                Set<ModelItem> elements = new DeploymentViewExpressionParser().parseIdentifier(token, context);

                if (elements.isEmpty()) {
                    throw new RuntimeException("The element \"" + token + "\" does not exist");
                }

                for (ModelItem element : elements) {
                    if (element instanceof StaticStructureElementInstance) {
                        staticStructureElementInstances.add((StaticStructureElementInstance)element);
                    }

                    if (element instanceof InfrastructureNode) {
                        infrastructureNodes.add((InfrastructureNode)element);
                    }
                }
            }
        }

        if (!(staticStructureElementInstances.isEmpty() && infrastructureNodes.isEmpty())) {
            view.addAnimation(staticStructureElementInstances.toArray(new StaticStructureElementInstance[0]), infrastructureNodes.toArray(new InfrastructureNode[0]));
        } else {
            throw new RuntimeException("No software system instances, container instances, or infrastructure nodes were found");
        }
    }

}
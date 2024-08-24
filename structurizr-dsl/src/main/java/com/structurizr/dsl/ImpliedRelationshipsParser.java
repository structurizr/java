package com.structurizr.dsl;

import com.structurizr.model.CreateImpliedRelationshipsUnlessAnyRelationshipExistsStrategy;
import com.structurizr.model.DefaultImpliedRelationshipsStrategy;
import com.structurizr.model.ImpliedRelationshipsStrategy;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.Set;

final class ImpliedRelationshipsParser extends AbstractParser {

    private static final String GRAMMAR = "!impliedRelationships <true|false|fqcn>";

    private static final Set<String> BUILT_IN_IMPLIED_RELATIONSHIPS_STRATEGIES = Set.of(
            "com.structurizr.model.DefaultImpliedRelationshipsStrategy",
            "com.structurizr.model.CreateImpliedRelationshipsUnlessAnyRelationshipExistsStrategy",
            "com.structurizr.model.CreateImpliedRelationshipsUnlessSameRelationshipExistsStrategy"
    );

    private static final int OPTION_INDEX = 1;
    private static final String TRUE = "true";
    private static final String FALSE = "false";

    void parse(DslContext context, Tokens tokens, File dslFile, boolean restricted) {
        // impliedRelationships <true|false|fqcn>

        if (tokens.hasMoreThan(OPTION_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: " + GRAMMAR);
        }

        if (!tokens.includes(OPTION_INDEX)) {
            throw new RuntimeException("Expected: " + GRAMMAR);
        }

        String option = tokens.get(OPTION_INDEX);

        if (option.equalsIgnoreCase(FALSE)) {
            context.getWorkspace().getModel().setImpliedRelationshipsStrategy(new DefaultImpliedRelationshipsStrategy());
        } else if (option.equalsIgnoreCase(TRUE)) {
            context.getWorkspace().getModel().setImpliedRelationshipsStrategy(new CreateImpliedRelationshipsUnlessAnyRelationshipExistsStrategy());
        } else {
            if (restricted) {
                if (!BUILT_IN_IMPLIED_RELATIONSHIPS_STRATEGIES.contains(option)) {
                    throw new RuntimeException("The implied relationships strategy " + option + " is not available when the DSL parser is running in restricted mode");
                }
            }

            try {
                Class<? extends ImpliedRelationshipsStrategy> impliedRelationshipsStrategyClass = context.loadClass(option, dslFile);
                Constructor<? extends ImpliedRelationshipsStrategy> constructor = impliedRelationshipsStrategyClass.getDeclaredConstructor();
                ImpliedRelationshipsStrategy impliedRelationshipsStrategy = (ImpliedRelationshipsStrategy)constructor.newInstance();
                context.getWorkspace().getModel().setImpliedRelationshipsStrategy(impliedRelationshipsStrategy);
            } catch (ClassNotFoundException cnfe) {
                throw new RuntimeException("Error loading implied relationships strategy: " + option + " was not found");
            } catch (Exception e) {
                throw new RuntimeException("Error loading implied relationships strategy: " + e.getMessage());
            }
        }
    }

}
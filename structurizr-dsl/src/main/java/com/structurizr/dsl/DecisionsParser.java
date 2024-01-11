package com.structurizr.dsl;

import com.structurizr.documentation.Documentable;
import com.structurizr.importer.documentation.DefaultImageImporter;
import com.structurizr.importer.documentation.DocumentationImporter;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

final class DecisionsParser extends AbstractParser {

    private static final Map<String,String> DECISION_IMPORTERS = new HashMap<>();

    private static final String ADRTOOLS_DECISION_IMPORTER = "adrtools";
    private static final String LOG4BRAINS_DECISION_IMPORTER = "log4brains";
    private static final String MADR_DECISION_IMPORTER = "madr";

    static {
        DECISION_IMPORTERS.put(ADRTOOLS_DECISION_IMPORTER, "com.structurizr.importer.documentation.AdrToolsDecisionImporter");
        DECISION_IMPORTERS.put(MADR_DECISION_IMPORTER, "com.structurizr.importer.documentation.MadrDecisionImporter");
        DECISION_IMPORTERS.put(LOG4BRAINS_DECISION_IMPORTER, "com.structurizr.importer.documentation.Log4brainsDecisionImporter");
    }

    private static final String GRAMMAR = "!decisions <path> <type|fqn>";

    private static final int PATH_INDEX = 1;
    private static final int TYPE_OR_FQN_INDEX = 2;

    void parse(WorkspaceDslContext context, File dslFile, Tokens tokens) {
        parse(context, context.getWorkspace(), dslFile, tokens);
    }

    void parse(SoftwareSystemDslContext context, File dslFile, Tokens tokens) {
        parse(context, context.getSoftwareSystem(), dslFile, tokens);
    }

    void parse(ContainerDslContext context, File dslFile, Tokens tokens) {
        parse(context, context.getContainer(), dslFile, tokens);
    }

    void parse(ComponentDslContext context, File dslFile, Tokens tokens) {
        parse(context, context.getComponent(), dslFile, tokens);
    }

    private void parse(DslContext context, Documentable documentable, File dslFile, Tokens tokens) {
        // !adrs <path>

        if (tokens.hasMoreThan(TYPE_OR_FQN_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: " + GRAMMAR);
        }

        if (!tokens.includes(PATH_INDEX)) {
            throw new RuntimeException("Expected: " + GRAMMAR);
        }

        String fullyQualifiedClassName = DECISION_IMPORTERS.get(ADRTOOLS_DECISION_IMPORTER);
        if (tokens.includes(TYPE_OR_FQN_INDEX)) {
            String typeOrFullyQualifiedName = tokens.get(TYPE_OR_FQN_INDEX);
            fullyQualifiedClassName = DECISION_IMPORTERS.getOrDefault(typeOrFullyQualifiedName, typeOrFullyQualifiedName);
        }

        if (dslFile != null) {
            File path = new File(dslFile.getParentFile(), tokens.get(PATH_INDEX));

            if (!path.exists()) {
                throw new RuntimeException("Documentation path " + path + " does not exist");
            }

            if (!path.isDirectory()) {
                throw new RuntimeException("Documentation path " + path + " is not a directory");
            }

            try {
                Class decisionImporterClass = context.loadClass(fullyQualifiedClassName, dslFile);
                Constructor constructor = decisionImporterClass.getDeclaredConstructor();
                DocumentationImporter decisionImporter = (DocumentationImporter)constructor.newInstance();
                decisionImporter.importDocumentation(documentable, path);

                if (!tokens.includes(TYPE_OR_FQN_INDEX)) {
                    DefaultImageImporter imageImporter = new DefaultImageImporter();
                    imageImporter.importDocumentation(documentable, path);
                }
            } catch (ClassNotFoundException cnfe) {
                throw new RuntimeException("Error importing decisions from " + path.getAbsolutePath() + ": " + fullyQualifiedClassName + " was not found");
            } catch (Exception e) {
                throw new RuntimeException("Error importing decisions from " + path.getAbsolutePath() + ": " + e.getMessage());
            }
        }
    }

}
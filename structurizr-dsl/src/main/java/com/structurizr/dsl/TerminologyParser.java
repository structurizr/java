package com.structurizr.dsl;

import com.structurizr.view.MetadataSymbols;

import java.util.*;

final class TerminologyParser extends AbstractParser {

    private final static int TERM_INDEX = 1;
    private final static int SYMBOL_TYPE_INDEX = 1;

    void parsePerson(DslContext context, Tokens tokens) {
        // person <term>
        if (!tokens.includes(TERM_INDEX)) {
            throw new RuntimeException("Expected: person <term>");
        }

        context.getWorkspace().getViews().getConfiguration().getTerminology().setPerson(tokens.get(TERM_INDEX));
    }

    void parseSoftwareSystem(DslContext context, Tokens tokens) {
        // softwareSystem <term>
        if (!tokens.includes(TERM_INDEX)) {
            throw new RuntimeException("Expected: softwareSystem <term>");
        }

        context.getWorkspace().getViews().getConfiguration().getTerminology().setSoftwareSystem(tokens.get(TERM_INDEX));
    }

    void parseContainer(DslContext context, Tokens tokens) {
        // container <term>
        if (!tokens.includes(TERM_INDEX)) {
            throw new RuntimeException("Expected: container <term>");
        }

        context.getWorkspace().getViews().getConfiguration().getTerminology().setContainer(tokens.get(TERM_INDEX));
    }

    void parseComponent(DslContext context, Tokens tokens) {
        // component <term>
        if (!tokens.includes(TERM_INDEX)) {
            throw new RuntimeException("Expected: component <term>");
        }

        context.getWorkspace().getViews().getConfiguration().getTerminology().setComponent(tokens.get(TERM_INDEX));
    }

    void parseDeploymentNode(DslContext context, Tokens tokens) {
        // deploymentNode <term>
        if (!tokens.includes(TERM_INDEX)) {
            throw new RuntimeException("Expected: deploymentNode <term>");
        }

        context.getWorkspace().getViews().getConfiguration().getTerminology().setDeploymentNode(tokens.get(TERM_INDEX));
    }

    void parseInfrastructureNode(DslContext context, Tokens tokens) {
        // infrastructureNode <term>
        if (!tokens.includes(TERM_INDEX)) {
            throw new RuntimeException("Expected: infrastructureNode <term>");
        }

        context.getWorkspace().getViews().getConfiguration().getTerminology().setInfrastructureNode(tokens.get(TERM_INDEX));
    }

    void parseRelationship(DslContext context, Tokens tokens) {
        // relationship <term>
        if (!tokens.includes(TERM_INDEX)) {
            throw new RuntimeException("Expected: relationship <term>");
        }

        context.getWorkspace().getViews().getConfiguration().getTerminology().setRelationship(tokens.get(TERM_INDEX));
    }

    void parseMetadataSymbols(DslContext context, Tokens tokens) {
        Map<String, MetadataSymbols> symbols = new LinkedHashMap<>();
        symbols.put("square", MetadataSymbols.SquareBrackets);
        symbols.put("round", MetadataSymbols.RoundBrackets);
        symbols.put("curly", MetadataSymbols.CurlyBrackets);
        symbols.put("angle", MetadataSymbols.AngleBrackets);
        symbols.put("double-angle", MetadataSymbols.DoubleAngleBrackets);
        symbols.put("none", MetadataSymbols.None);

        String symbolsAsString = String.join("|", symbols.keySet());

        // metadata <symbol-type>
        if (!tokens.includes(SYMBOL_TYPE_INDEX)) {
            throw new RuntimeException("Expected: metadata <" + symbolsAsString + ">");
        }

        String symbolAsString = tokens.get(SYMBOL_TYPE_INDEX).toLowerCase();
        MetadataSymbols symbol = symbols.get(symbolAsString);
        if (symbol != null) {
            context.getWorkspace().getViews().getConfiguration().setMetadataSymbols(symbol);
        } else {
            throw new RuntimeException("The symbol type \"" + symbolAsString + "\" is not valid");
        }

    }

}
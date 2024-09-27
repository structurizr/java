package com.structurizr.dsl;

import java.io.File;

class ExternalScriptDslContext extends ScriptDslContext {

    private final String filename;

    ExternalScriptDslContext(DslContext parentContext, File dslFile, StructurizrDslParser dslParser, String filename) {
        super(parentContext, dslFile, dslParser);

        this.filename = filename;
    }

    @Override
    void end() {
        try {
            File scriptFile = new File(dslFile.getParent(), filename);
            if (!scriptFile.exists()) {
                throw new RuntimeException("Script file " + scriptFile.getCanonicalPath() + " does not exist");
            }

            run(this, scriptFile);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error running script at " + filename + ", caused by " + e.getClass().getName() + ": " + e.getMessage());
        }
    }

    @Override
    protected String[] getPermittedTokens() {
        return new String[0];
    }

}
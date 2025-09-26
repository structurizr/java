package com.structurizr.dsl;

import com.structurizr.model.Element;
import com.structurizr.model.Relationship;

import javax.script.*;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

abstract class ScriptDslContext extends DslContext {

    private static final String CONTEXT_VARIABLE_NAME = "context";
    private static final String WORKSPACE_VARIABLE_NAME = "workspace";
    private static final String VIEW_VARIABLE_NAME = "view";
    private static final String ELEMENT_VARIABLE_NAME = "element";
    private static final String RELATIONSHIP_VARIABLE_NAME = "relationship";

    private final DslContext parentContext;

    protected final File dslFile;
    private final StructurizrDslParser dslParser;

    private final Map<String,String> parameters = new HashMap<>();

    ScriptDslContext(DslContext parentContext, File dslFile, StructurizrDslParser dslParser) {
        this.parentContext = parentContext;
        this.dslFile = dslFile;
        this.dslParser = dslParser;
        setDslPortable(false);
    }

    void addParameter(String name, String value) {
        parameters.put(name, value);
    }

    void run(DslContext context, String extension, List<String> lines) throws Exception {
        StringBuilder script = new StringBuilder();
        for (String line : lines) {
            script.append(line);
            script.append('\n');
        }

        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByExtension(extension);

        if (engine != null) {
            Bindings bindings = engine.createBindings();
            populateBindings(bindings, context);

            engine.eval(script.toString(), bindings);
        } else {
            throw new RuntimeException("Could not load a scripting engine for extension \"" + extension + "\"");
        }
    }

    void run(DslContext context, File scriptFile) throws Exception {
        String extension = scriptFile.getName().substring(scriptFile.getName().lastIndexOf('.') + 1);
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByExtension(extension);

        if (engine != null) {
            Bindings bindings = engine.createBindings();
            populateBindings(bindings, context);

            ScriptContext scriptContext = new SimpleScriptContext();
            scriptContext.setBindings(bindings, ScriptContext.ENGINE_SCOPE);
            scriptContext.setAttribute(ScriptEngine.FILENAME, scriptFile.getAbsolutePath(), ScriptContext.ENGINE_SCOPE);
            engine.eval(new FileReader(scriptFile), scriptContext);
        } else {
            throw new RuntimeException("Could not load a scripting engine for extension \"" + extension + "\"");
        }
    }

    private void populateBindings(Bindings bindings, DslContext context) {
        bindings.put(WORKSPACE_VARIABLE_NAME, context.getWorkspace());

        if (parentContext instanceof ViewDslContext) {
            bindings.put(VIEW_VARIABLE_NAME, ((ViewDslContext)parentContext).getView());
        } else if (parentContext instanceof ModelItemDslContext) {
            ModelItemDslContext modelItemDslContext = (ModelItemDslContext)parentContext;
            if (modelItemDslContext.getModelItem() instanceof Element) {
                bindings.put(ELEMENT_VARIABLE_NAME, modelItemDslContext.getModelItem());
            } else if (modelItemDslContext.getModelItem() instanceof Relationship) {
                bindings.put(RELATIONSHIP_VARIABLE_NAME, modelItemDslContext.getModelItem());
            }
        }

        // bind a context object
        StructurizrDslScriptContext scriptContext = new StructurizrDslScriptContext(dslParser, dslFile, getWorkspace(), parameters);
        bindings.put(CONTEXT_VARIABLE_NAME, scriptContext);

        // and any custom parameters
        for (String name : parameters.keySet()) {
            bindings.put(name, parameters.get(name));
        }
    }

}
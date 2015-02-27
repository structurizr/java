package com.structurizr.io.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.structurizr.Workspace;
import com.structurizr.io.StructurizrWriter;
import com.structurizr.io.StructurizrWriterException;

import java.io.IOException;
import java.io.Writer;

public class JsonWriter implements StructurizrWriter {

    private boolean indentOutput = true;

    public JsonWriter(boolean indentOutput) {
        this.indentOutput = indentOutput;
    }

    public void write(Workspace workspace, Writer writer) throws StructurizrWriterException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            if (indentOutput) {
                objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            }
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);

            writer.write(objectMapper.writeValueAsString(workspace));
        } catch (IOException ioe) {
            throw new StructurizrWriterException("Could not write as JSON", ioe);
        }
    }

}

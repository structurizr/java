package com.structurizr.io.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.structurizr.io.StructurizrWriter;
import com.structurizr.io.StructurizrWriterException;
import com.structurizr.view.ViewSet;

import java.io.IOException;
import java.io.Writer;

public class JsonWriter implements StructurizrWriter {

    private boolean indentOutput = true;

    public JsonWriter(boolean indentOutput) {
        this.indentOutput = indentOutput;
    }

    public void write(ViewSet viewSet, Writer writer) throws StructurizrWriterException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            if (indentOutput) {
                objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            }

            ModelAndViewSet modelAndViewSet = new ModelAndViewSet(viewSet.getModel(), viewSet);

            writer.write(objectMapper.writeValueAsString(modelAndViewSet));
        } catch (IOException ioe) {
            throw new StructurizrWriterException("Could not write as JSON", ioe);
        }
    }

}

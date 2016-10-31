package com.structurizr.encryption;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.structurizr.io.WorkspaceWriterException;

import java.io.Writer;

public final class EncryptedJsonWriter {

    private boolean indentOutput = true;

    public EncryptedJsonWriter(boolean indentOutput) {
        this.indentOutput = indentOutput;
    }

    public void write(EncryptedWorkspace encryptedWorkspace, Writer writer) throws WorkspaceWriterException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            if (indentOutput) {
                objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            }
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);

            writer.write(objectMapper.writeValueAsString(encryptedWorkspace));
        } catch (Exception e) {
            throw new WorkspaceWriterException("Could not write as JSON", e);
        }
    }

}

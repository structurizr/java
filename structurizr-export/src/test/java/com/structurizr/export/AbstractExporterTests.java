package com.structurizr.export;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

public abstract class AbstractExporterTests {

    protected String readFile(File file) throws Exception {
        StringBuilder buf = new StringBuilder();

        Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);

        List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);

        for (String line : lines) {
            buf.append(line);
            buf.append("\n");
        }

        if (buf.length() > 1) {
            return buf.toString().substring(0, buf.length() - 1);
        } else {
            return buf.toString();
        }
    }

}

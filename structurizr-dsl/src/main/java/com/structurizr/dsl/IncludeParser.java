package com.structurizr.dsl;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

final class IncludeParser extends AbstractParser {

    private static final String GRAMMAR = "!include <file|directory|url>";

    private static final int SOURCE_INDEX = 1;

    List<IncludedFile> parse(DslContext context, File dslFile, Tokens tokens) {
        // !include <file|directory|url>

        List<IncludedFile> includedFiles = new ArrayList<>();

        if (tokens.hasMoreThan(SOURCE_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: " + GRAMMAR);
        }

        if (!tokens.includes(SOURCE_INDEX)) {
            throw new RuntimeException("Expected: " + GRAMMAR);
        }

        String source = tokens.get(SOURCE_INDEX);
        if (source.startsWith("https://") || source.startsWith("http://")) {
            RemoteContent content = readFromUrl(source);
            List<String> lines = Arrays.asList(content.getContent().split("\n"));
            includedFiles.add(new IncludedFile(dslFile, lines));
        } else {
            if (dslFile != null) {
                File path = new File(dslFile.getParent(), source);

                try {
                    if (!path.exists()) {
                        throw new RuntimeException(path.getCanonicalPath() + " could not be found");
                    }

                    includedFiles.addAll(readFiles(path));
                    context.setDslPortable(false);
                } catch (IOException e) {
                    throw new RuntimeException("Error including " + path.getAbsolutePath() + ": " + e.getMessage());
                }
            }
        }

        return includedFiles;
    }

    private List<IncludedFile> readFiles(File path) throws IOException {
        List<IncludedFile> includedFiles = new ArrayList<>();

        if (path.isHidden() || path.getName().startsWith(".")) {
            // ignore
            return includedFiles;
        }

        if (path.isDirectory()) {
            File[] files = path.listFiles();
            if (files != null) {
                Arrays.sort(files);

                for (File file : files) {
                    includedFiles.addAll(readFiles(file));
                }
            }
        } else {
            try {
                includedFiles.add(new IncludedFile(path, Files.readAllLines(path.toPath(), StandardCharsets.UTF_8)));
            } catch (IOException e) {
                throw new RuntimeException("Error reading file at " + path.getAbsolutePath() + ": " + e.getMessage());
            }
        }

        return includedFiles;
    }

}
package com.structurizr.documentation;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

class FormatFinder {

    private static Set<String> MARKDOWN_EXTENSIONS = new HashSet<>(Arrays.asList(".md", ".markdown", ".text"));

    private static Set<String> ASCIIDOC_EXTENSIONS = new HashSet<>(Arrays.asList(".asciidoc", ".adoc", ".asc"));

    static boolean isMarkdownOrAsciiDoc(File file) {
        String extension = file.getName().substring(file.getName().lastIndexOf("."));

        return MARKDOWN_EXTENSIONS.contains(extension) || ASCIIDOC_EXTENSIONS.contains(extension);
    }

    static Format findFormat(File file) {
        if (file == null) {
            throw new IllegalArgumentException("A file must be specified.");
        }

        String extension = file.getName().substring(file.getName().lastIndexOf("."));
        if (MARKDOWN_EXTENSIONS.contains(extension)) {
            return Format.Markdown;
        } else if (ASCIIDOC_EXTENSIONS.contains(extension)) {
            return Format.AsciiDoc;
        } else {
            // just assume Markdown
            return Format.Markdown;
        }

    }

}
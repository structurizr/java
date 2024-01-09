package com.structurizr.importer.documentation;

import com.structurizr.documentation.Format;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class FormatFinder {

    private static final Set<String> MARKDOWN_EXTENSIONS = new HashSet<>(Arrays.asList(".md", ".markdown", ".text"));

    private static final Set<String> ASCIIDOC_EXTENSIONS = new HashSet<>(Arrays.asList(".asciidoc", ".adoc", ".asc"));

    public static boolean isMarkdownOrAsciiDoc(File file) {
        String extension = file.getName().substring(file.getName().lastIndexOf("."));

        return MARKDOWN_EXTENSIONS.contains(extension) || ASCIIDOC_EXTENSIONS.contains(extension);
    }

    public static Format findFormat(File file) {
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
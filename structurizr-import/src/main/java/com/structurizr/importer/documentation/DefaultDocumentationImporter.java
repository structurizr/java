package com.structurizr.importer.documentation;

import com.structurizr.documentation.Documentable;
import com.structurizr.documentation.Format;
import com.structurizr.documentation.Section;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;

/**
 * This implementation scans a given directory and automatically imports all Markdown or AsciiDoc
 * files in that directory.
 *
 * See https://structurizr.com/help/documentation/headings for details of how section headings and numbering are handled.
 */
public class DefaultDocumentationImporter implements DocumentationImporter {

    /**
     * Imports Markdown/AsciiDoc files from the specified path, each in its own section.
     *
     * @param documentable      the item that documentation should be associated with
     * @param path              the path to import documentation from
     */
    @Override
    public void importDocumentation(Documentable documentable, File path) {
        if (documentable == null) {
            throw new IllegalArgumentException("A workspace, software system, container, or component must be specified.");
        }

        if (path == null) {
            throw new IllegalArgumentException("A path must be specified.");
        } else if (!path.exists()) {
            throw new IllegalArgumentException(path.getAbsolutePath() + " does not exist.");
        }

        try {
            if (path.isDirectory()) {
                File[] filesInDirectory = path.listFiles();
                if (filesInDirectory != null) {
                    Arrays.sort(filesInDirectory);

                    for (File file : filesInDirectory) {
                        if (!file.isDirectory() && !file.getName().startsWith(".")) {
                            importFile(documentable, file);
                        }
                    }
                }
            } else {
                importFile(documentable, path);
            }

            // now trim the filenames
            for (Section section : documentable.getDocumentation().getSections()) {
                String filename = section.getFilename();

                filename = filename.replace(path.getCanonicalPath(), "");
                if (filename.startsWith("/")) {
                    filename = filename.substring(1);
                }

                section.setFilename(filename);
            }
        } catch (Exception e) {
            throw new DocumentationImportException(e);
        }
    }

    protected void importFile(Documentable documentable, File file) throws Exception {
        if (FormatFinder.isMarkdownOrAsciiDoc(file)) {
            Format format = FormatFinder.findFormat(file);
            String content = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);

            Section section = new Section(format, content);
            section.setFilename(file.getCanonicalPath());
            documentable.getDocumentation().addSection(section);
        }
    }

}
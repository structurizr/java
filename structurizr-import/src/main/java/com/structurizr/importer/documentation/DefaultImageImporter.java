package com.structurizr.importer.documentation;

import com.structurizr.documentation.Documentable;
import com.structurizr.documentation.Image;
import com.structurizr.util.ImageUtils;
import com.structurizr.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

/**
 * This implementation scans a given directory and automatically imports all Markdown or AsciiDoc
 * files in that directory.
 *
 * - Each file must represent a separate documentation section.
 * - The second level heading ("## Section Title" in Markdown and "== Section Title" in AsciiDoc) will be used as the section title.
 */
public class DefaultImageImporter implements DocumentationImporter {

    /**
     * Imports one or more png/jpg/jpeg/gif images from the specified path.
     *
     * @param documentable      the item that documentation should be associated with
     * @param path              the path to import images from
     */
    public void importDocumentation(Documentable documentable, File path) {
        if (documentable == null) {
            throw new IllegalArgumentException("A workspace or software system must be specified.");
        }

        if (path == null) {
            throw new IllegalArgumentException("A path must be specified.");
        } else if (!path.exists()) {
            throw new IllegalArgumentException(path.getAbsolutePath() + " does not exist.");
        }

        try {
            if (path.isDirectory()) {
                importImages(documentable, "", path);
            } else {
                importImage(documentable, "", path);
            }
        } catch (Exception e) {
            throw new DocumentationImportException(e.getMessage(), e);
        }
    }

    private void importImages(Documentable documentable, String root, File path) throws IOException {
        File[] files = path.listFiles();
        if (files != null) {
            for (File file : files) {
                String name = file.getName().toLowerCase();
                if (file.isDirectory() && !file.isHidden()) {
                    if (StringUtils.isNullOrEmpty(root)) {
                        importImages(documentable, file.getName(), file);
                    } else {
                        importImages(documentable, root + "/" + file.getName(), file);
                    }
                } else {
                    if (name.endsWith(".png") || name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".gif") || name.endsWith(".svg")) {
                        importImage(documentable, root, file);
                    }
                }
            }
        }
    }

    private void importImage(Documentable documentable, String path, File file) throws IOException {
        String contentType = ImageUtils.getContentType(file);
        String base64Content;

        String name;
        if (StringUtils.isNullOrEmpty(path)) {
            name = file.getName();
        } else {
            name = path + "/" + file.getName();
        }

        if (ImageUtils.CONTENT_TYPE_IMAGE_SVG.equalsIgnoreCase(contentType)) {
            base64Content = Base64.getEncoder().encodeToString(Files.readAllBytes(file.toPath()));
        } else {
            contentType = ImageUtils.getContentType(file);
            base64Content = ImageUtils.getImageAsBase64(file);
        }

        documentable.getDocumentation().addImage(new Image(name, contentType, base64Content));
    }

}
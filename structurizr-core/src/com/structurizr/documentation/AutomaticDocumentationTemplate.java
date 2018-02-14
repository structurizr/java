package com.structurizr.documentation;

import com.structurizr.Workspace;
import com.structurizr.model.Element;
import com.structurizr.model.SoftwareSystem;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This template allows you to scan a given directory and automatically add all Markdown or AsciiDoc
 * files in that directory. Each file must represent a separate section, and the second level heading
 * ("## Section Title" in Markdown and "== Section Title" in AsciiDoc) will be used as the section name.
 */
public class AutomaticDocumentationTemplate extends DocumentationTemplate {

    public AutomaticDocumentationTemplate(Workspace workspace) {
        super(workspace);
    }

    /**
     * Adds all files in the specified directory, each in its own section.
     *
     * @param directory     the directory to scan
     * @return              a List of Section objects
     * @throws IOException  if there is an error reading the files in the directory
     */
    public List<Section> addSections(File directory) throws IOException {
        return add(null, directory);
    }

    /**
     * Adds all files in the specified directory, each in its own section, related to a software system.
     *
     * @param directory         the directory to scan
     * @param softwareSystem    the SoftwareSystem to associate the documentation with
     * @return                  a List of Section objects
     * @throws IOException      if there is an error reading the files in the directory
     */
    public List<Section> addSections(SoftwareSystem softwareSystem, File directory) throws IOException {
        if (softwareSystem == null) {
            throw new IllegalArgumentException("A software system must be specified.");
        }

        return add(softwareSystem, directory);
    }

    private List<Section> add(SoftwareSystem softwareSystem, File directory) throws IOException {
        List<Section> sections = new ArrayList<>();

        if (!directory.exists()) {
            throw new IllegalArgumentException(directory.getCanonicalPath() + " does not exist.");
        }

        if (!directory.isDirectory()) {
            throw new IllegalArgumentException(directory.getCanonicalPath() + " is not a directory.");
        }

        File[] filesInDirectory = directory.listFiles();
        if (filesInDirectory != null) {
            Arrays.sort(filesInDirectory);

            for (File file : filesInDirectory) {
                Format format = FormatFinder.findFormat(file);
                String sectionDefinition = "";

                if (format == Format.Markdown) {
                    sectionDefinition = "##";
                } else if (format == Format.AsciiDoc) {
                    sectionDefinition = "==";
                }

                String content = new String(Files.readAllBytes(file.toPath()), "UTF-8");
                String sectionName = file.getName();
                Matcher matcher = Pattern.compile("^" + sectionDefinition + " (.*?)$", Pattern.MULTILINE).matcher(content);
                if (matcher.find()) {
                    sectionName = matcher.group(1);
                }

                Section section = addSection(softwareSystem, sectionName, GROUP1, format, content);
                sections.add(section);
            }
        }

        return sections;
    }

    @Override
    protected TemplateMetadata getMetadata() {
        return null;
    }

}
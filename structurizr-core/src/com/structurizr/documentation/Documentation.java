package com.structurizr.documentation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.structurizr.Workspace;
import com.structurizr.model.Element;
import com.structurizr.model.Model;
import com.structurizr.model.SoftwareSystem;
import com.structurizr.util.ImageUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents the documentation within a workspace - a collection of
 * content in Markdown or AsciiDoc format, optionally with attached images.
 *
 * See <a href="https://structurizr.com/help/documentation">Documentation</a>
 * on the Structurizr website for more details.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type", defaultImpl = StructurizrDocumentation.class)
@JsonSubTypes({
        @JsonSubTypes.Type(value=StructurizrDocumentation.class, name="structurizr"),
        @JsonSubTypes.Type(value=Arc42Documentation.class, name="arc42")
})
public abstract class Documentation {

    private Model model;
    private Set<Section> sections = new HashSet<>();
    private Set<Image> images = new HashSet<>();

    Documentation() {
    }

    public Documentation(Workspace workspace) {
        if (workspace == null) {
            throw new IllegalArgumentException("A workspace must be specified.");
        }

        this.model = workspace.getModel();
        workspace.setDocumentation(this);
    }

    @JsonIgnore
    Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    protected String readFiles(File... files) throws IOException {
        StringBuilder content = new StringBuilder();
        if (files != null) {
            for (File file : files) {
                if (file != null) {
                    if (content.length() > 0) {
                        content.append(System.lineSeparator());
                    }

                    if (file.isFile()) {
                        content.append(new String(Files.readAllBytes(file.toPath()), "UTF-8"));
                    } else if (file.isDirectory()) {
                        content.append(readFiles(file.listFiles()));
                    }
                }
            }
        }

        return content.toString();
    }

    /**
     * Adds a custom section from a file, that isn't related to any element in the model.
     *
     * @param name              the name of the section
     * @param group             the group of the section (an integer between 1 and 5)
     * @param format    the {@link Format} of the documentation content
     * @param files  one or more File objects that point to the documentation content
     * @return  a documentation {@link Section}
     * @throws IOException  if the file can't be read
     */
    public Section addCustomSection(String name, int group, Format format, File... files) throws IOException {
        return addCustomSection(name, group, format, readFiles(files));
    }

    /**
     * Adds a custom section, that isn't related to any element in the model.
     *
     * @param name              the name of the section
     * @param group             the group of the section (an integer between 1 and 5)
     * @param format    the {@link Format} of the documentation content
     * @param content   a String containing the documentation content
     * @return  a documentation {@link Section}
     */
    public Section addCustomSection(String name, int group, Format format, String content) {
        return addSection(null, name, group, format, content);
    }

    /**
     * Adds a custom section relating to a {@link SoftwareSystem} from a file.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param name              the name of the section
     * @param group             the group of the section (an integer between 1 and 5)
     * @param format    the {@link Format} of the documentation content
     * @param files  one or more File objects that point to the documentation content
     * @return  a documentation {@link Section}
     * @throws IOException  if the file can't be read
     */
    public Section addCustomSection(SoftwareSystem softwareSystem, String name, int group, Format format, File... files) throws IOException {
        return addCustomSection(softwareSystem, name, group, format, readFiles(files));
    }

    /**
     * Adds a custom section relating to a {@link SoftwareSystem}.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param name              the name of the section
     * @param group             the group of the section (an integer between 1 and 5)
     * @param format    the {@link Format} of the documentation content
     * @param content   a String containing the documentation content
     * @return  a documentation {@link Section}
     */
    public Section addCustomSection(SoftwareSystem softwareSystem, String name, int group, Format format, String content) {
        return addSection(softwareSystem, name, group, format, content);
    }

    protected final Section addSection(Element element, String type, int group, Format format, String content) {
        if (group < 1) {
            group = 1;
        } else if (group > 5) {
            group = 5;
        }

        Section section = new Section(element, type, calculateOrder(), group, format, content);
        if (!sections.contains(section)) {
            sections.add(section);
            return section;
        } else {
            throw new IllegalArgumentException("A section of type " + type +
                    (element != null ? " for " + element.getName() : "")
                    + " already exists.");
        }
    }

    private int calculateOrder() {
        return sections.size()+1;
    }

    /**
     * Gets the set of {@link Section}s.
     *
     * @return  a Set of {@link Section} objects
     */
    public Set<Section> getSections() {
        return new HashSet<>(sections);
    }

    void setSections(Set<Section> sections) {
        this.sections = sections;
    }

    /**
     * Adds png/jpg/jpeg/gif images in the given directory to the workspace.
     *
     * @param path  a File descriptor representing a directory on disk
     * @throws IOException  if the path can't be accessed
     */
    public void addImages(File path) throws IOException {
        if (path == null) {
            throw new IllegalArgumentException("Directory path must not be null.");
        } else if (!path.exists()) {
            throw new IllegalArgumentException("The directory " + path.getCanonicalPath() + " does not exist.");
        } else if (!path.isDirectory()) {
            throw new IllegalArgumentException(path.getCanonicalPath() + " is not a directory.");
        }

        addImagesFromPath("", path);
    }

    private void addImagesFromPath(String root, File path) throws IOException {
        File[] files = path.listFiles();
        if (files != null) {
            for (File file : files) {
                String name = file.getName().toLowerCase();
                if (file.isDirectory()) {
                    addImagesFromPath(file.getName() + "/", file);
                } else {
                    if (name.endsWith(".png") || name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".gif")) {
                        Image image = addImage(file);

                        if (!root.isEmpty()) {
                            image.setName(root + image.getName());
                        }
                    }
                }
            }
        }
    }

    /**
     * Adds an image from the given file to the workspace.
     *
     * @param file  a File descriptor representing an image file on disk
     * @return  an Image object representing the image added
     * @throws IOException  if the file can't be read
     */
    public Image addImage(File file) throws IOException {
        if (file == null) {
            throw new IllegalArgumentException("File must not be null.");
        } else if (!file.exists()) {
            throw new IllegalArgumentException("The file " + file.getCanonicalPath() + " does not exist.");
        } else if (!file.isFile()) {
            throw new IllegalArgumentException(file.getCanonicalPath() + " is not a file.");
        }

        String contentType = ImageUtils.getContentType(file);
        String base64Content = ImageUtils.getImageAsBase64(file);

        Image image = new Image(file.getName(), contentType, base64Content);
        images.add(image);

        return image;
    }

    /**
     * Gets the set of {@link Image}s in this workspace.
     *
     * @return  a Set of {@link Image} objects
     */
    public Set<Image> getImages() {
        return new HashSet<>(images);
    }

    void setImages(Set<Image> images) {
        this.images = images;
    }

    public void hydrate() {
        for (Section section : sections) {
            section.setElement(model.getElement(section.getElementId()));
        }
    }

    @JsonIgnore
    public boolean isEmpty() {
        return sections.isEmpty() && images.isEmpty();
    }

}

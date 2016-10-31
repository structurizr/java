package com.structurizr.documentation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.structurizr.model.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents the documentation within a workspace.
 */
public class Documentation {

    private Model model;
    private Set<Section> sections = new HashSet<>();
    private Set<Image> images = new HashSet<>();

    Documentation() {
    }

    public Documentation(Model model) {
        this.model = model;
    }

    @JsonIgnore
    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    /**
     * Adds documentation content relating to a {@link SoftwareSystem} from a file.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param type  the {@link Type} of the documentation content
     * @param format    the {@link Format} of the documentation content
     * @param file  a File that points to the documentation content
     * @return  a documentation {@link Section}
     * @throws IOException  if the file can't be read
     */
    public Section add(SoftwareSystem softwareSystem, Type type, Format format, File file) throws IOException {
        String content = new String(Files.readAllBytes(file.toPath()), "UTF-8");
        return add(softwareSystem, type, format, content);
    }

    /**
     * Adds documentation content related to a {@link SoftwareSystem} from a String.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param type  the {@link Type} of the documentation content
     * @param format    the {@link Format} of the documentation content
     * @param content   a String containing the documentation content
     * @return  a documentation {@link Section}
     */
    public Section add(SoftwareSystem softwareSystem, Type type, Format format, String content) {
        return addSection(softwareSystem, type, format, content);
    }

    /**
     * Adds documentation content related to a {@link Container} from a file.
     *
     * @param container the {@link Container} the documentation content relates to
     * @param format    the {@link Format} of the documentation content
     * @param file  a File that points to the documentation content
     * @return  a documentation {@link Section}
     * @throws IOException  if the file can't be read
     */
    public Section add(Container container, Format format, File file) throws IOException {
        String content = new String(Files.readAllBytes(file.toPath()), "UTF-8");
        return add(container, format, content);
    }

    /**
     * Adds documentation content related to a {@link Container} from a String.
     *
     * @param container the {@link Container} the documentation content relates to
     * @param format    the {@link Format} of the documentation content
     * @param content   a String containing the documentation content
     * @return  a documentation {@link Section}
     */
    public Section add(Container container, Format format, String content) {
        return addSection(container, Type.Components, format, content);
    }

    /**
     * Adds documentation content related to a {@link Component} from a file.
     *
     * @param component the {@link Component} the documentation content relates to
     * @param format    the {@link Format} of the documentation content
     * @param file  a File that points to the documentation content
     * @return  a documentation {@link Section}
     * @throws IOException  if the file can't be read
     */
    public Section add(Component component, Format format, File file) throws IOException {
        String content = new String(Files.readAllBytes(file.toPath()), "UTF-8");
        return add(component, format, content);
    }

    /**
     * Adds documentation content related to a {@link Component} from a String.
     *
     * @param component the {@link Component} the documentation content relates to
     * @param format    the {@link Format} of the documentation content
     * @param content   a String containing the documentation content
     * @return  a documentation {@link Section}
     */
    public Section add(Component component, Format format, String content) {
        return addSection(component, Type.Code, format, content);
    }

    private Section addSection(Element element, Type type, Format format, String content) {
        if (!(element instanceof Container) && type == Type.Components) {
            throw new IllegalArgumentException("Sections of type Components must be related to a container rather than a software system.");
        }

        Section section = new Section(element, type, format, content);
        if (!sections.contains(section)) {
            sections.add(section);
            return section;
        } else {
            throw new IllegalArgumentException("A section of type " + type + " for " + element.getName() + " already exists.");
        }
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

        File[] imageFiles = path.listFiles((dir, name) -> {
            name = name.toLowerCase();
            return name.endsWith(".png") || name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".gif");
        });

        for (File file : imageFiles) {
            addImage(file);
        }
    }

    /**
     * Adds an image from the given file to the workspace.
     *
     * @param file  a File descriptor representing an image file on disk
     * @throws IOException
     */
    public void addImage(File file) throws IOException {
        if (file == null) {
            throw new IllegalArgumentException("File must not be null.");
        } else if (!file.exists()) {
            throw new IllegalArgumentException("The file " + file.getCanonicalPath() + " does not exist.");
        } else if (!file.isFile()) {
            throw new IllegalArgumentException(file.getCanonicalPath() + " is not a file.");
        }

        String contentType = URLConnection.guessContentTypeFromName(file.getName());
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException(file.getCanonicalPath() + " is not a supported image file.");
        }

        BufferedImage bufferedImage = ImageIO.read(file);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, contentType.split("/")[1], bos);
        byte[] imageBytes = bos.toByteArray();

        String base64Content = Base64.getEncoder().encodeToString(imageBytes);
        Image image = new Image(file.getName(), contentType, base64Content);
        images.add(image);
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

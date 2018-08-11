package com.structurizr.documentation;

import com.structurizr.Workspace;
import com.structurizr.model.Component;
import com.structurizr.model.Container;
import com.structurizr.model.Element;
import com.structurizr.model.SoftwareSystem;
import com.structurizr.util.ImageUtils;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

/**
 * The superclass for all documentation templates.
 */
public abstract class DocumentationTemplate {

    private Documentation documentation;

    /**
     * Creates a new documentation template for the given workspace.
     *
     * @param workspace     the Workspace instance to create documentation for
     */
    public DocumentationTemplate(@Nonnull Workspace workspace) {
        if (workspace == null) {
            throw new IllegalArgumentException("A workspace must be specified.");
        }

        this.documentation = workspace.getDocumentation();
        documentation.setTemplate(getMetadata());
    }

    /**
     * Adds a custom section from one or more files, that isn't related to any element in the model.
     *
     * @param title             the section title
     * @param files             one or more File objects that point to the documentation content
     * @return                  a documentation {@link Section}
     * @throws IOException      if there is an error reading the files
     */
    public Section addSection(String title, File... files) throws IOException {
        return add(null, title, files);
    }

    /**
     * Adds a custom section, that isn't related to any element in the model.
     *
     * @param title             the section title
     * @param format    the {@link Format} of the documentation content
     * @param content   a String containing the documentation content
     * @return           a documentation {@link Section}
     */
    @Nonnull
    public Section addSection(String title, Format format, String content) {
        return add(null, title, format, content);
    }

    /**
     * Adds a section relating to a {@link SoftwareSystem} from one or more files.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param title             the section title
     * @param files             one or more File objects that point to the documentation content
     * @return                  a documentation {@link Section}
     * @throws IOException      if there is an error reading the files
     */
    @Nonnull
    public Section addSection(SoftwareSystem softwareSystem, String title, File... files) throws IOException {
        return add(softwareSystem, title, files);
    }

    /**
     * Adds a section relating to a {@link SoftwareSystem}.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param title             the section title
     * @param format            the {@link Format} of the documentation content
     * @param content           a String containing the documentation content
     * @return                  a documentation {@link Section}
     */
    public Section addSection(SoftwareSystem softwareSystem, String title, Format format, String content) {
        return add(softwareSystem, title, format, content);
    }

    /**
     * Adds a section relating to a {@link Container} from one or more files.
     *
     * @param container     the {@link Container} the documentation content relates to
     * @param title             the section title
     * @param files         one or more File objects that point to the documentation content
     * @return              a documentation {@link Section}
     * @throws IOException      if there is an error reading the files
     */
    public Section addSection(Container container, String title, File... files) throws IOException {
        return add(container, title, files);
    }

    /**
     * Adds a section relating to a {@link Container}.
     *
     * @param container     the {@link Container} the documentation content relates to
     * @param title             the section title
     * @param format        the {@link Format} of the documentation content
     * @param content       a String containing the documentation content
     * @return              a documentation {@link Section}
     */
    public Section addSection(Container container, String tite, Format format, String content) {
        return add(container, tite, format, content);
    }

    /**
     * Adds a section relating to a {@link Component} from one or more files.
     *
     * @param component     the {@link Component} the documentation content relates to
     * @param title             the section title
     * @param files         one or more File objects that point to the documentation content
     * @return              a documentation {@link Section}
     * @throws IOException      if there is an error reading the files
     */
    public Section addSection(Component component, String title, File... files) throws IOException {
        return add(component, title, files);
    }

    /**
     * Adds a section relating to a {@link Component}.
     *
     * @param component     the {@link Component} the documentation content relates to
     * @param title             the section title
     * @param format        the {@link Format} of the documentation content
     * @param content       a String containing the documentation content
     * @return              a documentation {@link Section}
     */
    public Section addSection(Component component, String title, Format format, String content) {
        return add(component, title, format, content);
    }

    private Section add(Element element, @Nonnull String title, @Nonnull Format format, @Nonnull String content) {
        return documentation.addSection(element, title, format, content);
    }

    @Nonnull
    private Section add(Element element, @Nonnull String title, File... files) throws IOException {
        FormattedContent content = readFiles(files);
        return documentation.addSection(element, title, content.getFormat(), content.getContent());
    }

    private FormattedContent readFiles(File... files) throws IOException {
        if (files == null || files.length == 0) {
            throw new IllegalArgumentException("One or more files must be specified.");
        }

        Format format = Format.Markdown;
        StringBuilder content = new StringBuilder();
        for (File file : files) {
            if (file == null) {
                throw new IllegalArgumentException("One or more files must be specified.");
            }

            if (!file.exists()) {
                throw new IllegalArgumentException(file.getCanonicalPath() + " does not exist.");
            }

            if (content.length() > 0) {
                content.append(System.lineSeparator());
            }

            if (file.isFile()) {
                format = FormatFinder.findFormat(file);
                content.append(new String(Files.readAllBytes(file.toPath()), "UTF-8"));
            } else if (file.isDirectory()) {
                File[] filesInDirectory = file.listFiles();
                if (filesInDirectory != null) {
                    Arrays.sort(filesInDirectory);
                    content.append(readFiles(filesInDirectory).getContent());
                }
            }
        }

        return new FormattedContent(content.toString(), format);
    }

    /**
     * Adds png/jpg/jpeg/gif images in the given directory to the workspace.
     *
     * @param path          a File descriptor representing a directory on disk
     * @return  a Collection of Image objects
     * @throws IOException  if the path can't be accessed
     */
    public Collection<Image> addImages(File path) throws IOException {
        if (path == null) {
            throw new IllegalArgumentException("Directory path must not be null.");
        } else if (!path.exists()) {
            throw new IllegalArgumentException("The directory " + path.getCanonicalPath() + " does not exist.");
        } else if (!path.isDirectory()) {
            throw new IllegalArgumentException(path.getCanonicalPath() + " is not a directory.");
        }

        return addImagesFromPath("", path);
    }

    private Collection<Image> addImagesFromPath(String root, File path) throws IOException {
        Collection<Image> images = new HashSet<>();

        File[] files = path.listFiles();
        if (files != null) {
            for (File file : files) {
                String name = file.getName().toLowerCase();
                if (file.isDirectory()) {
                    images.addAll(addImagesFromPath(file.getName() + "/", file));
                } else {
                    if (name.endsWith(".png") || name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".gif")) {
                        Image image = addImage(file);

                        if (!root.isEmpty()) {
                            image.setName(root + image.getName());
                        }

                        images.add(image);
                    }
                }
            }
        }

        return images;
    }

    /**
     * Adds an image from the given file to the workspace.
     *
     * @param file              a File descriptor representing an image file on disk
     * @return                  an Image object representing the image added
     * @throws IOException      if there is an error reading the image
     */
    public Image addImage(File file) throws IOException {
        String contentType = ImageUtils.getContentType(file);
        String base64Content = ImageUtils.getImageAsBase64(file);

        Image image = new Image(file.getName(), contentType, base64Content);
        documentation.addImage(image);

        return image;
    }

    /**
     * Gets the metadata associated with this template.
     *
     * @return  a TemplateMetadata object, or null if there is none
     */
    protected abstract TemplateMetadata getMetadata();

}
package com.structurizr.documentation;

import com.structurizr.Workspace;
import com.structurizr.model.Element;
import com.structurizr.model.SoftwareSystem;
import com.structurizr.util.ImageUtils;

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

    public static final int GROUP1 = 1;
    public static final int GROUP2 = 2;
    public static final int GROUP3 = 3;
    public static final int GROUP4 = 4;
    public static final int GROUP5 = 5;

    protected Documentation documentation;

    /**
     * Creates a new documentation template for the given workspace.
     *
     * @param workspace     the Workspace instance to create documentation for
     */
    public DocumentationTemplate(Workspace workspace) {
        if (workspace == null) {
            throw new IllegalArgumentException("A workspace must be specified.");
        }

        this.documentation = workspace.getDocumentation();
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
     * Adds a custom section relating to a {@link SoftwareSystem} from one or more files.
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
        return documentation.addSection(element, type, group, format, content);
    }

    protected String readFiles(File... files) throws IOException {
        if (files == null || files.length == 0) {
            throw new IllegalArgumentException("One or more files must be specified.");
        }

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
                content.append(new String(Files.readAllBytes(file.toPath()), "UTF-8"));
            } else if (file.isDirectory()) {
                File[] filesInDirectory = file.listFiles();
                if (filesInDirectory != null) {
                    Arrays.sort(filesInDirectory);
                    content.append(readFiles(filesInDirectory));
                }
            }
        }

        return content.toString();
    }

    /**
     * Adds png/jpg/jpeg/gif images in the given directory to the workspace.
     *
     * @param path  a File descriptor representing a directory on disk
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
     * @param file  a File descriptor representing an image file on disk
     * @return  an Image object representing the image added
     * @throws IOException  if the file can't be read
     */
    public Image addImage(File file) throws IOException {
        String contentType = ImageUtils.getContentType(file);
        String base64Content = ImageUtils.getImageAsBase64(file);

        Image image = new Image(file.getName(), contentType, base64Content);
        documentation.addImage(image);

        return image;
    }

}
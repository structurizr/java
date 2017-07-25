package com.structurizr.documentation;

import com.structurizr.Workspace;
import com.structurizr.model.SoftwareSystem;

import java.io.File;
import java.io.IOException;

/**
 * <p>
 * An implementation of the <a href="http://www.viewpoints-and-perspectives.info">"Viewpoints and Perspectives" documentation template</a>,
 * from the "Software Systems Architecture" book by Nick Rozanski and Eoin Woods, consisting of the following sections:
 * </p>
 *
 * <ol>
 *     <li>Introduction (1)</li>
 *     <li>Glossary (1)</li>
 *     <li>System Stakeholders and Requirements (2)</li>
 *     <li>Architectural Forces (2)</li>
 *     <li>Architectural Views (3)</li>
 *     <li>System Qualities (4)</li>
 *     <li>Appendices (5)</li>
 * </ol>
 *
 * <p>
 * The number in parentheses () represents the grouping, which is simply used to colour code
 * section navigation buttons when rendered.
 * </p>
 */
public class ViewpointsAndPerspectivesDocumentationTemplate extends DocumentationTemplate {

    public ViewpointsAndPerspectivesDocumentationTemplate(Workspace workspace) {
        super(workspace);
    }

    /**
     * Adds a "Introduction" section relating to a {@link SoftwareSystem} from a file.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param files             one or more File objects that point to the documentation content
     * @return                  a documentation {@link Section}
     */
    public Section addIntroductionSection(SoftwareSystem softwareSystem, File... files) throws IOException {
        return addSection(softwareSystem, "Introduction", GROUP1, files);
    }

    /**
     * Adds a "Introduction" section relating to a {@link SoftwareSystem}.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param format            the {@link Format} of the documentation content
     * @param content           a String containing the documentation content
     * @return                  a documentation {@link Section}
     */
    public Section addIntroductionSection(SoftwareSystem softwareSystem, Format format, String content) {
        return addSection(softwareSystem, "Introduction", GROUP1, format, content);
    }

    /**
     * Adds a "Glossary" section relating to a {@link SoftwareSystem} from a file.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param files             one or more File objects that point to the documentation content
     * @return                  a documentation {@link Section}
     */
    public Section addGlossarySection(SoftwareSystem softwareSystem, File... files) throws IOException {
        return addSection(softwareSystem, "Glossary", GROUP1, files);
    }

    /**
     * Adds a "Glossary" section relating to a {@link SoftwareSystem}.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param format            the {@link Format} of the documentation content
     * @param content           a String containing the documentation content
     * @return                  a documentation {@link Section}
     */
    public Section addGlossarySection(SoftwareSystem softwareSystem, Format format, String content) {
        return addSection(softwareSystem, "Glossary", GROUP1, format, content);
    }

    /**
     * Adds a "System Stakeholders and Requirements" section relating to a {@link SoftwareSystem} from a file.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param files             one or more File objects that point to the documentation content
     * @return                  a documentation {@link Section}
     */
    public Section addSystemStakeholdersAndRequirementsSection(SoftwareSystem softwareSystem, File... files) throws IOException {
        return addSection(softwareSystem, "System Stakeholders and Requirements", GROUP2, files);
    }

    /**
     * Adds a "System Stakeholders and Requirements" section relating to a {@link SoftwareSystem}.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param format            the {@link Format} of the documentation content
     * @param content           a String containing the documentation content
     * @return                  a documentation {@link Section}
     */
    public Section addSystemStakeholdersAndRequirementsSection(SoftwareSystem softwareSystem, Format format, String content) {
        return addSection(softwareSystem, "System Stakeholders and Requirements", GROUP2, format, content);
    }

    /**
     * Adds an "Architectural Forces" section relating to a {@link SoftwareSystem} from a file.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param files             one or more File objects that point to the documentation content
     * @return                  a documentation {@link Section}
     */
    public Section addArchitecturalForcesSection(SoftwareSystem softwareSystem, File... files) throws IOException {
        return addSection(softwareSystem, "Architectural Forces", GROUP2, files);
    }

    /**
     * Adds an "Architectural Forces" section relating to a {@link SoftwareSystem}.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param format            the {@link Format} of the documentation content
     * @param content           a String containing the documentation content
     * @return                  a documentation {@link Section}
     */
    public Section addArchitecturalForcesSection(SoftwareSystem softwareSystem, Format format, String content) {
        return addSection(softwareSystem, "Architectural Forces", GROUP2, format, content);
    }

    /**
     * Adds an "Architectural Views" section relating to a {@link SoftwareSystem} from a file.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param files             one or more File objects that point to the documentation content
     * @return                  a documentation {@link Section}
     */
    public Section addArchitecturalViewsSection(SoftwareSystem softwareSystem, File... files) throws IOException {
        return addSection(softwareSystem, "Architectural Views", GROUP3, files);
    }

    /**
     * Adds an "Architectural Views" section relating to a {@link SoftwareSystem}.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param format            the {@link Format} of the documentation content
     * @param content           a String containing the documentation content
     * @return                  a documentation {@link Section}
     */
    public Section addArchitecturalViewsSection(SoftwareSystem softwareSystem, Format format, String content) {
        return addSection(softwareSystem, "Architectural Views", GROUP3, format, content);
    }

    /**
     * Adds a "System Qualities" section relating to a {@link SoftwareSystem} from a file.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param files             one or more File objects that point to the documentation content
     * @return                  a documentation {@link Section}
     */
    public Section addSystemQualitiesSection(SoftwareSystem softwareSystem, File... files) throws IOException {
        return addSection(softwareSystem, "System Qualities", GROUP4, files);
    }

    /**
     * Adds a "System Qualities" section relating to a {@link SoftwareSystem}.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param format            the {@link Format} of the documentation content
     * @param content           a String containing the documentation content
     * @return                  a documentation {@link Section}
     */
    public Section addSystemQualitiesSection(SoftwareSystem softwareSystem, Format format, String content) {
        return addSection(softwareSystem, "System Qualities", GROUP4, format, content);
    }

    /**
     * Adds an "Appendices" section relating to a {@link SoftwareSystem} from a file.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param files             one or more File objects that point to the documentation content
     * @return                  a documentation {@link Section}
     */
    public Section addAppendicesSection(SoftwareSystem softwareSystem, File... files) throws IOException {
        return addSection(softwareSystem, "Appendices", GROUP5, files);
    }

    /**
     * Adds an "Appendices" section relating to a {@link SoftwareSystem}.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param format            the {@link Format} of the documentation content
     * @param content           a String containing the documentation content
     * @return                  a documentation {@link Section}
     */
    public Section addAppendicesSection(SoftwareSystem softwareSystem, Format format, String content) {
        return addSection(softwareSystem, "Appendices", GROUP5, format, content);
    }

    @Override
    protected TemplateMetadata getMetadata() {
        return new TemplateMetadata("Viewpoints and Perspectives", "Nick Rozanski and Eoin Woods", "https://www.viewpoints-and-perspectives.info");
    }

}
package com.structurizr.documentation;

import com.structurizr.Workspace;
import com.structurizr.model.Component;
import com.structurizr.model.Container;
import com.structurizr.model.SoftwareSystem;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;

/**
 * <p>
 * A simple documentation template, based upon the "software guidebook" concept in Simon Brown's
 * <a href="https://leanpub.com/visualising-software-architecture">Software Architecture for Developers</a>
 * book, with the following sections:
 * </p>
 *
 * <ul>
 *     <li>Context (1)</li>
 *     <li>Functional Overview (2)</li>
 *     <li>Quality Attributes (2)</li>
 *     <li>Constraints (2)</li>
 *     <li>Principles (2)</li>
 *     <li>Software Architecture (3)</li>
 *     <li>Containers (3)</li>
 *     <li>Components (3)</li>
 *     <li>Code (3)</li>
 *     <li>Data (3)</li>
 *     <li>Infrastructure Architecture (4)</li>
 *     <li>Deployment (4)</li>
 *     <li>Development Environment (4)</li>
 *     <li>Operation and Support (4)</li>
 *     <li>Decision Log (5)</li>
 * </ul>
 *
 * <p>
 * The number in parentheses () represents the grouping, which is simply used to colour code
 * section navigation buttons when rendered.
 * </p>
 */
public class StructurizrDocumentationTemplate extends DocumentationTemplate {

    public StructurizrDocumentationTemplate(Workspace workspace) {
        super(workspace);
    }

    /**
     * Adds a "Context" section relating to a {@link SoftwareSystem} from one or more files.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to, or null if it relates to the whole workspace
     * @param files             one or more File objects that point to the documentation content
     * @return                  a documentation {@link Section}
     * @throws IOException      if there is an error reading the files
     */
    @Nonnull
    public Section addContextSection(@Nullable SoftwareSystem softwareSystem, File... files) throws IOException {
        return addSection(softwareSystem, "Context", files);
    }

    /**
     * Adds a "Context" section relating to a {@link SoftwareSystem}.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param format            the {@link Format} of the documentation content
     * @param content           a String containing the documentation content
     * @return                  a documentation {@link Section}
     */
    @Nonnull
    public Section addContextSection(@Nullable SoftwareSystem softwareSystem, @Nonnull Format format, @Nonnull String content) {
        return addSection(softwareSystem, "Context", format, content);
    }

    /**
     * Adds a "Functional Overview" section relating to a {@link SoftwareSystem} from one or more files.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param files             one or more File objects that point to the documentation content
     * @return                  a documentation {@link Section}
     * @throws IOException      if there is an error reading the files
     */
    @Nonnull
    public Section addFunctionalOverviewSection(@Nullable SoftwareSystem softwareSystem, File... files) throws IOException {
        return addSection(softwareSystem, "Functional Overview", files);
    }

    /**
     * Adds a "Functional Overview" section relating to a {@link SoftwareSystem}.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param format            the {@link Format} of the documentation content
     * @param content           a String containing the documentation content
     * @return                  a documentation {@link Section}
     */
    @Nonnull
    public Section addFunctionalOverviewSection(@Nullable SoftwareSystem softwareSystem, @Nonnull Format format, @Nonnull String content) {
        return addSection(softwareSystem, "Functional Overview", format, content);
    }

    /**
     * Adds a "Quality Attributes" section relating to a {@link SoftwareSystem} from one or more files.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param files             one or more File objects that point to the documentation content
     * @return                  a documentation {@link Section}
     * @throws IOException      if there is an error reading the files
     */
    @Nonnull
    public Section addQualityAttributesSection(@Nullable SoftwareSystem softwareSystem, File... files) throws IOException {
        return addSection(softwareSystem, "Quality Attributes", files);
    }

    /**
     * Adds a "Quality Attributes" section relating to a {@link SoftwareSystem}.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param format            the {@link Format} of the documentation content
     * @param content           a String containing the documentation content
     * @return                  a documentation {@link Section}
     */
    @Nonnull
    public Section addQualityAttributesSection(@Nullable SoftwareSystem softwareSystem, @Nonnull Format format, @Nonnull String content) {
        return addSection(softwareSystem, "Quality Attributes", format, content);
    }

    /**
     * Adds a "Constraints" section relating to a {@link SoftwareSystem} from one or more files.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param files             one or more File objects that point to the documentation content
     * @return                  a documentation {@link Section}
     * @throws IOException      if there is an error reading the files
     */
    @Nonnull
    public Section addConstraintsSection(@Nullable SoftwareSystem softwareSystem, File... files) throws IOException {
        return addSection(softwareSystem, "Constraints", files);
    }

    /**
     * Adds a "Constraints" section relating to a {@link SoftwareSystem}.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param format            the {@link Format} of the documentation content
     * @param content           a String containing the documentation content
     * @return                  a documentation {@link Section}
     */
    @Nonnull
    public Section addConstraintsSection(@Nullable SoftwareSystem softwareSystem, @Nonnull Format format, @Nonnull String content) {
        return addSection(softwareSystem, "Constraints", format, content);
    }

    /**
     * Adds a "Principles" section relating to a {@link SoftwareSystem} from one or more files.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param files             one or more File objects that point to the documentation content
     * @return                  a documentation {@link Section}
     * @throws IOException      if there is an error reading the files
     */
    @Nonnull
    public Section addPrinciplesSection(@Nullable SoftwareSystem softwareSystem, File... files) throws IOException {
        return addSection(softwareSystem, "Principles", files);
    }

    /**
     * Adds a "Principles" section relating to a {@link SoftwareSystem}.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param format            the {@link Format} of the documentation content
     * @param content           a String containing the documentation content
     * @return                  a documentation {@link Section}
     */
    @Nonnull
    public Section addPrinciplesSection(@Nullable SoftwareSystem softwareSystem, @Nonnull Format format, @Nonnull String content) {
        return addSection(softwareSystem, "Principles", format, content);
    }

    /**
     * Adds a "Software Architecture" section relating to a {@link SoftwareSystem} from one or more files.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param files             one or more File objects that point to the documentation content
     * @return                  a documentation {@link Section}
     * @throws IOException      if there is an error reading the files
     */
    @Nonnull
    public Section addSoftwareArchitectureSection(@Nullable SoftwareSystem softwareSystem, File... files) throws IOException {
        return addSection(softwareSystem, "Software Architecture", files);
    }

    /**
     * Adds a "Software Architecture" section relating to a {@link SoftwareSystem}.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param format            the {@link Format} of the documentation content
     * @param content           a String containing the documentation content
     * @return                  a documentation {@link Section}
     */
    @Nonnull
    public Section addSoftwareArchitectureSection(@Nullable SoftwareSystem softwareSystem, @Nonnull Format format, @Nonnull String content) {
        return addSection(softwareSystem, "Software Architecture", format, content);
    }

    /**
     * Adds a "Containers" section relating to a {@link SoftwareSystem} from one or more files.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param files             one or more File objects that point to the documentation content
     * @return                  a documentation {@link Section}
     * @throws IOException      if there is an error reading the files
     */
    @Nonnull
    public Section addContainersSection(@Nullable SoftwareSystem softwareSystem, File... files) throws IOException {
        return addSection(softwareSystem, "Containers", files);
    }

    /**
     * Adds a "Containers" section relating to a {@link SoftwareSystem}.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param format            the {@link Format} of the documentation content
     * @param content           a String containing the documentation content
     * @return                  a documentation {@link Section}
     */
    @Nonnull
    public Section addContainersSection(@Nullable SoftwareSystem softwareSystem, @Nonnull Format format, @Nonnull String content) {
        return addSection(softwareSystem, "Containers", format, content);
    }

    /**
     * Adds a "Components" section relating to a {@link Container} from one or more files.
     *
     * @param container     the {@link Container} the documentation content relates to
     * @param files         one or more File objects that point to the documentation content
     * @return              a documentation {@link Section}
     * @throws IOException      if there is an error reading the files
     */
    @Nonnull
    public Section addComponentsSection(@Nullable Container container, File... files) throws IOException {
        return addSection(container, "Components", files);
    }

    /**
     * Adds a "Components" section relating to a {@link Container}.
     *
     * @param container     the {@link Container} the documentation content relates to
     * @param format        the {@link Format} of the documentation content
     * @param content       a String containing the documentation content
     * @return              a documentation {@link Section}
     */
    @Nonnull
    public Section addComponentsSection(@Nullable Container container, @Nonnull Format format, @Nonnull String content) {
        return addSection(container, "Components", format, content);
    }

    /**
     * Adds a "Code" section relating to a {@link Component} from one or more files.
     *
     * @param component     the {@link Component} the documentation content relates to
     * @param files         one or more File objects that point to the documentation content
     * @return              a documentation {@link Section}
     * @throws IOException      if there is an error reading the files
     */
    @Nonnull
    public Section addCodeSection(@Nullable Component component, File... files) throws IOException {
        return addSection(component, "Code", files);
    }

    /**
     * Adds a "Code" section relating to a {@link Component}.
     *
     * @param component     the {@link Component} the documentation content relates to
     * @param format        the {@link Format} of the documentation content
     * @param content       a String containing the documentation content
     * @return              a documentation {@link Section}
     */
    @Nonnull
    public Section addCodeSection(@Nullable Component component, @Nonnull Format format, @Nonnull String content) {
        return addSection(component, "Code", format, content);
    }

    /**
     * Adds a "Data" section relating to a {@link SoftwareSystem} from one or more files.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param files             one or more File objects that point to the documentation content
     * @return                  a documentation {@link Section}
     * @throws IOException      if there is an error reading the files
     */
    @Nonnull
    public Section addDataSection(@Nullable SoftwareSystem softwareSystem, File... files) throws IOException {
        return addSection(softwareSystem, "Data", files);
    }

    /**
     * Adds a "Data" section relating to a {@link SoftwareSystem}.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param format            the {@link Format} of the documentation content
     * @param content           a String containing the documentation content
     * @return                  a documentation {@link Section}
     */
    @Nonnull
    public Section addDataSection(@Nullable SoftwareSystem softwareSystem, @Nonnull Format format, @Nonnull String content) {
        return addSection(softwareSystem, "Data", format, content);
    }

    /**
     * Adds an "Infrastructure Architecture" section relating to a {@link SoftwareSystem} from one or more files.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param files             one or more File objects that point to the documentation content
     * @return                  a documentation {@link Section}
     * @throws IOException      if there is an error reading the files
     */
    @Nonnull
    public Section addInfrastructureArchitectureSection(@Nullable SoftwareSystem softwareSystem, File... files) throws IOException {
        return addSection(softwareSystem, "Infrastructure Architecture", files);
    }

    /**
     * Adds a "Infrastructure Architecture" section relating to a {@link SoftwareSystem}.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param format            the {@link Format} of the documentation content
     * @param content           a String containing the documentation content
     * @return                  a documentation {@link Section}
     */
    @Nonnull
    public Section addInfrastructureArchitectureSection(@Nullable SoftwareSystem softwareSystem, @Nonnull Format format, @Nonnull String content) {
        return addSection(softwareSystem, "Infrastructure Architecture", format, content);
    }

    /**
     * Adds a "Deployment" section relating to a {@link SoftwareSystem} from one or more files.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param files             one or more File objects that point to the documentation content
     * @return                  a documentation {@link Section}
     * @throws IOException      if there is an error reading the files
     */
    @Nonnull
    public Section addDeploymentSection(@Nullable SoftwareSystem softwareSystem, File... files) throws IOException {
        return addSection(softwareSystem, "Deployment", files);
    }

    /**
     * Adds a "Deployment" section relating to a {@link SoftwareSystem}.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param format            the {@link Format} of the documentation content
     * @param content           a String containing the documentation content
     * @return                  a documentation {@link Section}
     */
    @Nonnull
    public Section addDeploymentSection(@Nullable SoftwareSystem softwareSystem, @Nonnull Format format, @Nonnull String content) {
        return addSection(softwareSystem, "Deployment", format, content);
    }

    /**
     * Adds a "Development Environment" section relating to a {@link SoftwareSystem} from one or more files.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param files             one or more File objects that point to the documentation content
     * @return                  a documentation {@link Section}
     * @throws IOException      if there is an error reading the files
     */
    @Nonnull
    public Section addDevelopmentEnvironmentSection(@Nullable SoftwareSystem softwareSystem, File... files) throws IOException {
        return addSection(softwareSystem, "Development Environment", files);
    }

    /**
     * Adds a "Development Environment" section relating to a {@link SoftwareSystem}.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param format            the {@link Format} of the documentation content
     * @param content           a String containing the documentation content
     * @return                  a documentation {@link Section}
     */
    @Nonnull
    public Section addDevelopmentEnvironmentSection(@Nullable SoftwareSystem softwareSystem, @Nonnull Format format, @Nonnull String content) {
        return addSection(softwareSystem, "Development Environment", format, content);
    }

    /**
     * Adds an "Operation and Support" section relating to a {@link SoftwareSystem} from one or more files.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param files             one or more File objects that point to the documentation content
     * @return                  a documentation {@link Section}
     * @throws IOException      if there is an error reading the files
     */
    @Nonnull
    public Section addOperationAndSupportSection(@Nullable SoftwareSystem softwareSystem, File... files) throws IOException {
        return addSection(softwareSystem, "Operation and Support", files);
    }

    /**
     * Adds a "Operation and Support" section relating to a {@link SoftwareSystem}.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param format            the {@link Format} of the documentation content
     * @param content           a String containing the documentation content
     * @return                  a documentation {@link Section}
     */
    @Nonnull
    public Section addOperationAndSupportSection(@Nullable SoftwareSystem softwareSystem, @Nonnull Format format, @Nonnull String content) {
        return addSection(softwareSystem, "Operation and Support", format, content);
    }

    /**
     * Adds a "Decision Log" section relating to a {@link SoftwareSystem} from one or more files.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param files             one or more File objects that point to the documentation content
     * @return                  a documentation {@link Section}
     * @throws IOException      if there is an error reading the files
     */
    @Nonnull
    public Section addDecisionLogSection(@Nullable SoftwareSystem softwareSystem, File... files) throws IOException {
        return addSection(softwareSystem, "Decision Log", files);
    }

    /**
     * Adds a "Decision Log" section relating to a {@link SoftwareSystem}.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param format            the {@link Format} of the documentation content
     * @param content           a String containing the documentation content
     * @return                  a documentation {@link Section}
     */
    @Nonnull
    public Section addDecisionLogSection(@Nullable SoftwareSystem softwareSystem, @Nonnull Format format, @Nonnull String content) {
        return addSection(softwareSystem, "Decision Log", format, content);
    }

    @Override
    protected TemplateMetadata getMetadata() {
        return new TemplateMetadata("Software Guidebook", "Simon Brown", "https://leanpub.com/visualising-software-architecture");
    }

}
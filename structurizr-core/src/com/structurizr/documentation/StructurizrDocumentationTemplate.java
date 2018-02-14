package com.structurizr.documentation;

import com.structurizr.Workspace;
import com.structurizr.model.Component;
import com.structurizr.model.Container;
import com.structurizr.model.SoftwareSystem;

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
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param files             one or more File objects that point to the documentation content
     * @return                  a documentation {@link Section}
     * @throws IOException      if there is an error reading the files
     */
    public Section addContextSection(SoftwareSystem softwareSystem, File... files) throws IOException {
        return addSection(softwareSystem, "Context", GROUP1, files);
    }

    /**
     * Adds a "Context" section relating to a {@link SoftwareSystem}.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param format            the {@link Format} of the documentation content
     * @param content           a String containing the documentation content
     * @return                  a documentation {@link Section}
     */
    public Section addContextSection(SoftwareSystem softwareSystem, Format format, String content) {
        return addSection(softwareSystem, "Context", GROUP1, format, content);
    }

    /**
     * Adds a "Functional Overview" section relating to a {@link SoftwareSystem} from one or more files.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param files             one or more File objects that point to the documentation content
     * @return                  a documentation {@link Section}
     * @throws IOException      if there is an error reading the files
     */
    public Section addFunctionalOverviewSection(SoftwareSystem softwareSystem, File... files) throws IOException {
        return addSection(softwareSystem, "Functional Overview", GROUP2, files);
    }

    /**
     * Adds a "Functional Overview" section relating to a {@link SoftwareSystem}.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param format            the {@link Format} of the documentation content
     * @param content           a String containing the documentation content
     * @return                  a documentation {@link Section}
     */
    public Section addFunctionalOverviewSection(SoftwareSystem softwareSystem, Format format, String content) {
        return addSection(softwareSystem, "Functional Overview", GROUP2, format, content);
    }

    /**
     * Adds a "Quality Attributes" section relating to a {@link SoftwareSystem} from one or more files.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param files             one or more File objects that point to the documentation content
     * @return                  a documentation {@link Section}
     * @throws IOException      if there is an error reading the files
     */
    public Section addQualityAttributesSection(SoftwareSystem softwareSystem, File... files) throws IOException {
        return addSection(softwareSystem, "Quality Attributes", GROUP2, files);
    }

    /**
     * Adds a "Quality Attributes" section relating to a {@link SoftwareSystem}.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param format            the {@link Format} of the documentation content
     * @param content           a String containing the documentation content
     * @return                  a documentation {@link Section}
     */
    public Section addQualityAttributesSection(SoftwareSystem softwareSystem, Format format, String content) {
        return addSection(softwareSystem, "Quality Attributes", GROUP2, format, content);
    }

    /**
     * Adds a "Constraints" section relating to a {@link SoftwareSystem} from one or more files.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param files             one or more File objects that point to the documentation content
     * @return                  a documentation {@link Section}
     * @throws IOException      if there is an error reading the files
     */
    public Section addConstraintsSection(SoftwareSystem softwareSystem, File... files) throws IOException {
        return addSection(softwareSystem, "Constraints", GROUP2, files);
    }

    /**
     * Adds a "Constraints" section relating to a {@link SoftwareSystem}.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param format            the {@link Format} of the documentation content
     * @param content           a String containing the documentation content
     * @return                  a documentation {@link Section}
     */
    public Section addConstraintsSection(SoftwareSystem softwareSystem, Format format, String content) {
        return addSection(softwareSystem, "Constraints", GROUP2, format, content);
    }

    /**
     * Adds a "Principles" section relating to a {@link SoftwareSystem} from one or more files.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param files             one or more File objects that point to the documentation content
     * @return                  a documentation {@link Section}
     * @throws IOException      if there is an error reading the files
     */
    public Section addPrinciplesSection(SoftwareSystem softwareSystem, File... files) throws IOException {
        return addSection(softwareSystem, "Principles", GROUP2, files);
    }

    /**
     * Adds a "Principles" section relating to a {@link SoftwareSystem}.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param format            the {@link Format} of the documentation content
     * @param content           a String containing the documentation content
     * @return                  a documentation {@link Section}
     */
    public Section addPrinciplesSection(SoftwareSystem softwareSystem, Format format, String content) {
        return addSection(softwareSystem, "Principles", GROUP2, format, content);
    }

    /**
     * Adds a "Software Architecture" section relating to a {@link SoftwareSystem} from one or more files.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param files             one or more File objects that point to the documentation content
     * @return                  a documentation {@link Section}
     * @throws IOException      if there is an error reading the files
     */
    public Section addSoftwareArchitectureSection(SoftwareSystem softwareSystem, File... files) throws IOException {
        return addSection(softwareSystem, "Software Architecture", GROUP3, files);
    }

    /**
     * Adds a "Software Architecture" section relating to a {@link SoftwareSystem}.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param format            the {@link Format} of the documentation content
     * @param content           a String containing the documentation content
     * @return                  a documentation {@link Section}
     */
    public Section addSoftwareArchitectureSection(SoftwareSystem softwareSystem, Format format, String content) {
        return addSection(softwareSystem, "Software Architecture", GROUP3, format, content);
    }

    /**
     * Adds a "Containers" section relating to a {@link SoftwareSystem} from one or more files.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param files             one or more File objects that point to the documentation content
     * @return                  a documentation {@link Section}
     * @throws IOException      if there is an error reading the files
     */
    public Section addContainersSection(SoftwareSystem softwareSystem, File... files) throws IOException {
        return addSection(softwareSystem, "Containers", GROUP3, files);
    }

    /**
     * Adds a "Containers" section relating to a {@link SoftwareSystem}.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param format            the {@link Format} of the documentation content
     * @param content           a String containing the documentation content
     * @return                  a documentation {@link Section}
     */
    public Section addContainersSection(SoftwareSystem softwareSystem, Format format, String content) {
        return addSection(softwareSystem, "Containers", GROUP3, format, content);
    }

    /**
     * Adds a "Components" section relating to a {@link Container} from one or more files.
     *
     * @param container     the {@link Container} the documentation content relates to
     * @param files         one or more File objects that point to the documentation content
     * @return              a documentation {@link Section}
     * @throws IOException      if there is an error reading the files
     */
    public Section addComponentsSection(Container container, File... files) throws IOException {
        return addSection(container, "Components", GROUP3, files);
    }

    /**
     * Adds a "Components" section relating to a {@link Container}.
     *
     * @param container     the {@link Container} the documentation content relates to
     * @param format        the {@link Format} of the documentation content
     * @param content       a String containing the documentation content
     * @return              a documentation {@link Section}
     */
    public Section addComponentsSection(Container container, Format format, String content) {
        return addSection(container, "Components", GROUP3, format, content);
    }

    /**
     * Adds a "Code" section relating to a {@link Component} from one or more files.
     *
     * @param component     the {@link Component} the documentation content relates to
     * @param files         one or more File objects that point to the documentation content
     * @return              a documentation {@link Section}
     * @throws IOException      if there is an error reading the files
     */
    public Section addCodeSection(Component component, File... files) throws IOException {
        return addSection(component, "Code", GROUP3, files);
    }

    /**
     * Adds a "Code" section relating to a {@link Component}.
     *
     * @param component     the {@link Component} the documentation content relates to
     * @param format        the {@link Format} of the documentation content
     * @param content       a String containing the documentation content
     * @return              a documentation {@link Section}
     */
    public Section addCodeSection(Component component, Format format, String content) {
        return addSection(component, "Code", GROUP3, format, content);
    }

    /**
     * Adds a "Data" section relating to a {@link SoftwareSystem} from one or more files.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param files             one or more File objects that point to the documentation content
     * @return                  a documentation {@link Section}
     * @throws IOException      if there is an error reading the files
     */
    public Section addDataSection(SoftwareSystem softwareSystem, File... files) throws IOException {
        return addSection(softwareSystem, "Data", GROUP3, files);
    }

    /**
     * Adds a "Data" section relating to a {@link SoftwareSystem}.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param format            the {@link Format} of the documentation content
     * @param content           a String containing the documentation content
     * @return                  a documentation {@link Section}
     */
    public Section addDataSection(SoftwareSystem softwareSystem, Format format, String content) {
        return addSection(softwareSystem, "Data", GROUP3, format, content);
    }

    /**
     * Adds an "Infrastructure Architecture" section relating to a {@link SoftwareSystem} from one or more files.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param files             one or more File objects that point to the documentation content
     * @return                  a documentation {@link Section}
     * @throws IOException      if there is an error reading the files
     */
    public Section addInfrastructureArchitectureSection(SoftwareSystem softwareSystem, File... files) throws IOException {
        return addSection(softwareSystem, "Infrastructure Architecture", GROUP4, files);
    }

    /**
     * Adds a "Infrastructure Architecture" section relating to a {@link SoftwareSystem}.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param format            the {@link Format} of the documentation content
     * @param content           a String containing the documentation content
     * @return                  a documentation {@link Section}
     */
    public Section addInfrastructureArchitectureSection(SoftwareSystem softwareSystem, Format format, String content) {
        return addSection(softwareSystem, "Infrastructure Architecture", GROUP4, format, content);
    }

    /**
     * Adds a "Deployment" section relating to a {@link SoftwareSystem} from one or more files.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param files             one or more File objects that point to the documentation content
     * @return                  a documentation {@link Section}
     * @throws IOException      if there is an error reading the files
     */
    public Section addDeploymentSection(SoftwareSystem softwareSystem, File... files) throws IOException {
        return addSection(softwareSystem, "Deployment", GROUP4, files);
    }

    /**
     * Adds a "Deployment" section relating to a {@link SoftwareSystem}.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param format            the {@link Format} of the documentation content
     * @param content           a String containing the documentation content
     * @return                  a documentation {@link Section}
     */
    public Section addDeploymentSection(SoftwareSystem softwareSystem, Format format, String content) {
        return addSection(softwareSystem, "Deployment", GROUP4, format, content);
    }

    /**
     * Adds a "Development Environment" section relating to a {@link SoftwareSystem} from one or more files.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param files             one or more File objects that point to the documentation content
     * @return                  a documentation {@link Section}
     * @throws IOException      if there is an error reading the files
     */
    public Section addDevelopmentEnvironmentSection(SoftwareSystem softwareSystem, File... files) throws IOException {
        return addSection(softwareSystem, "Development Environment", GROUP4, files);
    }

    /**
     * Adds a "Development Environment" section relating to a {@link SoftwareSystem}.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param format            the {@link Format} of the documentation content
     * @param content           a String containing the documentation content
     * @return                  a documentation {@link Section}
     */
    public Section addDevelopmentEnvironmentSection(SoftwareSystem softwareSystem, Format format, String content) {
        return addSection(softwareSystem, "Development Environment", GROUP4, format, content);
    }

    /**
     * Adds an "Operation and Support" section relating to a {@link SoftwareSystem} from one or more files.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param files             one or more File objects that point to the documentation content
     * @return                  a documentation {@link Section}
     * @throws IOException      if there is an error reading the files
     */
    public Section addOperationAndSupportSection(SoftwareSystem softwareSystem, File... files) throws IOException {
        return addSection(softwareSystem, "Operation and Support", GROUP4, files);
    }

    /**
     * Adds a "Operation and Support" section relating to a {@link SoftwareSystem}.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param format            the {@link Format} of the documentation content
     * @param content           a String containing the documentation content
     * @return                  a documentation {@link Section}
     */
    public Section addOperationAndSupportSection(SoftwareSystem softwareSystem, Format format, String content) {
        return addSection(softwareSystem, "Operation and Support", GROUP4, format, content);
    }

    /**
     * Adds a "Decision Log" section relating to a {@link SoftwareSystem} from one or more files.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param files             one or more File objects that point to the documentation content
     * @return                  a documentation {@link Section}
     * @throws IOException      if there is an error reading the files
     */
    public Section addDecisionLogSection(SoftwareSystem softwareSystem, File... files) throws IOException {
        return addSection(softwareSystem, "Decision Log", GROUP5, files);
    }

    /**
     * Adds a "Decision Log" section relating to a {@link SoftwareSystem}.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param format            the {@link Format} of the documentation content
     * @param content           a String containing the documentation content
     * @return                  a documentation {@link Section}
     */
    public Section addDecisionLogSection(SoftwareSystem softwareSystem, Format format, String content) {
        return addSection(softwareSystem, "Decision Log", GROUP5, format, content);
    }

    @Override
    protected TemplateMetadata getMetadata() {
        return new TemplateMetadata("Software Guidebook", "Simon Brown", "https://leanpub.com/visualising-software-architecture");
    }

}
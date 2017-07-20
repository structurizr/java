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
public final class StructurizrDocumentation extends Documentation {

    StructurizrDocumentation() {
    }

    public StructurizrDocumentation(Workspace workspace) {
        super(workspace);
    }

    /**
     * Adds a "Context" section relating to a {@link SoftwareSystem} from a file.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param format    the {@link Format} of the documentation content
     * @param files  one or more File objects that point to the documentation content
     * @return  a documentation {@link Section}
     * @throws IOException  if the file can't be read
     */
    public Section addContextSection(SoftwareSystem softwareSystem, Format format, File... files) throws IOException {
        return addContextSection(softwareSystem, format, readFiles(files));
    }

    /**
     * Adds a "Context" section relating to a {@link SoftwareSystem}.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param format    the {@link Format} of the documentation content
     * @param content   a String containing the documentation content
     * @return  a documentation {@link Section}
     */
    public Section addContextSection(SoftwareSystem softwareSystem, Format format, String content) {
        return addSection(softwareSystem, "Context", GROUP1, format, content);
    }

    /**
     * Adds a "Functional Overview" section relating to a {@link SoftwareSystem} from a file.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param format    the {@link Format} of the documentation content
     * @param files  one or more File objects that point to the documentation content
     * @return  a documentation {@link Section}
     * @throws IOException  if the file can't be read
     */
    public Section addFunctionalOverviewSection(SoftwareSystem softwareSystem, Format format, File... files) throws IOException {
        return addFunctionalOverviewSection(softwareSystem, format, readFiles(files));
    }

    /**
     * Adds a "Functional Overview" section relating to a {@link SoftwareSystem}.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param format    the {@link Format} of the documentation content
     * @param content   a String containing the documentation content
     * @return  a documentation {@link Section}
     */
    public Section addFunctionalOverviewSection(SoftwareSystem softwareSystem, Format format, String content) {
        return addSection(softwareSystem, "Functional Overview", GROUP2, format, content);
    }

    /**
     * Adds a "Quality Attributes" section relating to a {@link SoftwareSystem} from a file.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param format    the {@link Format} of the documentation content
     * @param files  one or more File objects that point to the documentation content
     * @return  a documentation {@link Section}
     * @throws IOException  if the file can't be read
     */
    public Section addQualityAttributesSection(SoftwareSystem softwareSystem, Format format, File... files) throws IOException {
        return addQualityAttributesSection(softwareSystem, format, readFiles(files));
    }

    /**
     * Adds a "Quality Attributes" section relating to a {@link SoftwareSystem}.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param format    the {@link Format} of the documentation content
     * @param content   a String containing the documentation content
     * @return  a documentation {@link Section}
     */
    public Section addQualityAttributesSection(SoftwareSystem softwareSystem, Format format, String content) {
        return addSection(softwareSystem, "Quality Attributes", GROUP2, format, content);
    }

    /**
     * Adds a "Constraints" section relating to a {@link SoftwareSystem} from a file.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param format    the {@link Format} of the documentation content
     * @param files  one or more File objects that point to the documentation content
     * @return  a documentation {@link Section}
     * @throws IOException  if the file can't be read
     */
    public Section addConstraintsSection(SoftwareSystem softwareSystem, Format format, File... files) throws IOException {
        return addConstraintsSection(softwareSystem, format, readFiles(files));
    }

    /**
     * Adds a "Constraints" section relating to a {@link SoftwareSystem}.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param format    the {@link Format} of the documentation content
     * @param content   a String containing the documentation content
     * @return  a documentation {@link Section}
     */
    public Section addConstraintsSection(SoftwareSystem softwareSystem, Format format, String content) {
        return addSection(softwareSystem, "Constraints", GROUP2, format, content);
    }

    /**
     * Adds a "Principles" section relating to a {@link SoftwareSystem} from a file.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param format    the {@link Format} of the documentation content
     * @param files  one or more File objects that point to the documentation content
     * @return  a documentation {@link Section}
     * @throws IOException  if the file can't be read
     */
    public Section addPrinciplesSection(SoftwareSystem softwareSystem, Format format, File... files) throws IOException {
        return addPrinciplesSection(softwareSystem, format, readFiles(files));
    }

    /**
     * Adds a "Principles" section relating to a {@link SoftwareSystem}.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param format    the {@link Format} of the documentation content
     * @param content   a String containing the documentation content
     * @return  a documentation {@link Section}
     */
    public Section addPrinciplesSection(SoftwareSystem softwareSystem, Format format, String content) {
        return addSection(softwareSystem, "Principles", GROUP2, format, content);
    }

    /**
     * Adds a "Software Architecture" section relating to a {@link SoftwareSystem} from a file.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param format    the {@link Format} of the documentation content
     * @param files  one or more File objects that point to the documentation content
     * @return  a documentation {@link Section}
     * @throws IOException  if the file can't be read
     */
    public Section addSoftwareArchitectureSection(SoftwareSystem softwareSystem, Format format, File... files) throws IOException {
        return addSoftwareArchitectureSection(softwareSystem, format, readFiles(files));
    }

    /**
     * Adds a "Software Architecture" section relating to a {@link SoftwareSystem}.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param format    the {@link Format} of the documentation content
     * @param content   a String containing the documentation content
     * @return  a documentation {@link Section}
     */
    public Section addSoftwareArchitectureSection(SoftwareSystem softwareSystem, Format format, String content) {
        return addSection(softwareSystem, "Software Architecture", GROUP3, format, content);
    }

    /**
     * Adds a "Containers" section relating to a {@link SoftwareSystem} from a file.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param format    the {@link Format} of the documentation content
     * @param files  one or more File objects that point to the documentation content
     * @return  a documentation {@link Section}
     * @throws IOException  if the file can't be read
     */
    public Section addContainersSection(SoftwareSystem softwareSystem, Format format, File... files) throws IOException {
        return addContainersSection(softwareSystem, format, readFiles(files));
    }

    /**
     * Adds a "Containers" section relating to a {@link SoftwareSystem}.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param format    the {@link Format} of the documentation content
     * @param content   a String containing the documentation content
     * @return  a documentation {@link Section}
     */
    public Section addContainersSection(SoftwareSystem softwareSystem, Format format, String content) {
        return addSection(softwareSystem, "Containers", GROUP3, format, content);
    }

    /**
     * Adds a "Components" section relating to a {@link Container} from a file.
     *
     * @param container     the {@link Container} the documentation content relates to
     * @param format    the {@link Format} of the documentation content
     * @param files  one or more File objects that point to the documentation content
     * @return  a documentation {@link Section}
     * @throws IOException  if the file can't be read
     */
    public Section addComponentsSection(Container container, Format format, File... files) throws IOException {
        return addComponentsSection(container, format, readFiles(files));
    }

    /**
     * Adds a "Components" section relating to a {@link Container}.
     *
     * @param container     the {@link Container} the documentation content relates to
     * @param format    the {@link Format} of the documentation content
     * @param content   a String containing the documentation content
     * @return  a documentation {@link Section}
     */
    public Section addComponentsSection(Container container, Format format, String content) {
        return addSection(container, "Components", GROUP3, format, content);
    }

    /**
     * Adds a "Code" section relating to a {@link Component} from a file.
     *
     * @param component     the {@link Component} the documentation content relates to
     * @param format    the {@link Format} of the documentation content
     * @param files  one or more File objects that point to the documentation content
     * @return  a documentation {@link Section}
     * @throws IOException  if the file can't be read
     */
    public Section addCodeSection(Component component, Format format, File... files) throws IOException {
        return addCodeSection(component, format, readFiles(files));
    }

    /**
     * Adds a "Code" section relating to a {@link Component}.
     *
     * @param component     the {@link Component} the documentation content relates to
     * @param format    the {@link Format} of the documentation content
     * @param content   a String containing the documentation content
     * @return  a documentation {@link Section}
     */
    public Section addCodeSection(Component component, Format format, String content) {
        return addSection(component, "Code", GROUP3, format, content);
    }

    /**
     * Adds a "Data" section relating to a {@link SoftwareSystem} from a file.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param format    the {@link Format} of the documentation content
     * @param files  one or more File objects that point to the documentation content
     * @return  a documentation {@link Section}
     * @throws IOException  if the file can't be read
     */
    public Section addDataSection(SoftwareSystem softwareSystem, Format format, File... files) throws IOException {
        return addDataSection(softwareSystem, format, readFiles(files));
    }

    /**
     * Adds a "Data" section relating to a {@link SoftwareSystem}.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param format    the {@link Format} of the documentation content
     * @param content   a String containing the documentation content
     * @return  a documentation {@link Section}
     */
    public Section addDataSection(SoftwareSystem softwareSystem, Format format, String content) {
        return addSection(softwareSystem, "Data", GROUP3, format, content);
    }

    /**
     * Adds an "Infrastructure Architecture" section relating to a {@link SoftwareSystem} from a file.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param format    the {@link Format} of the documentation content
     * @param files  one or more File objects that point to the documentation content
     * @return  a documentation {@link Section}
     * @throws IOException  if the file can't be read
     */
    public Section addInfrastructureArchitectureSection(SoftwareSystem softwareSystem, Format format, File... files) throws IOException {
        return addInfrastructureArchitectureSection(softwareSystem, format, readFiles(files));
    }

    /**
     * Adds a "Infrastructure Architecture" section relating to a {@link SoftwareSystem}.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param format    the {@link Format} of the documentation content
     * @param content   a String containing the documentation content
     * @return  a documentation {@link Section}
     */
    public Section addInfrastructureArchitectureSection(SoftwareSystem softwareSystem, Format format, String content) {
        return addSection(softwareSystem, "Infrastructure Architecture", GROUP4, format, content);
    }

    /**
     * Adds a "Deployment" section relating to a {@link SoftwareSystem} from a file.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param format    the {@link Format} of the documentation content
     * @param files  one or more File objects that point to the documentation content
     * @return  a documentation {@link Section}
     * @throws IOException  if the file can't be read
     */
    public Section addDeploymentSection(SoftwareSystem softwareSystem, Format format, File... files) throws IOException {
        return addDeploymentSection(softwareSystem, format, readFiles(files));
    }

    /**
     * Adds a "Deployment" section relating to a {@link SoftwareSystem}.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param format    the {@link Format} of the documentation content
     * @param content   a String containing the documentation content
     * @return  a documentation {@link Section}
     */
    public Section addDeploymentSection(SoftwareSystem softwareSystem, Format format, String content) {
        return addSection(softwareSystem, "Deployment", GROUP4, format, content);
    }

    /**
     * Adds a "Development Environment" section relating to a {@link SoftwareSystem} from a file.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param format    the {@link Format} of the documentation content
     * @param files  one or more File objects that point to the documentation content
     * @return  a documentation {@link Section}
     * @throws IOException  if the file can't be read
     */
    public Section addDevelopmentEnvironmentSection(SoftwareSystem softwareSystem, Format format, File... files) throws IOException {
        return addDevelopmentEnvironmentSection(softwareSystem, format, readFiles(files));
    }

    /**
     * Adds a "Development Environment" section relating to a {@link SoftwareSystem}.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param format    the {@link Format} of the documentation content
     * @param content   a String containing the documentation content
     * @return  a documentation {@link Section}
     */
    public Section addDevelopmentEnvironmentSection(SoftwareSystem softwareSystem, Format format, String content) {
        return addSection(softwareSystem, "Development Environment", GROUP4, format, content);
    }

    /**
     * Adds an "Operation and Support" section relating to a {@link SoftwareSystem} from a file.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param format    the {@link Format} of the documentation content
     * @param files  one or more File objects that point to the documentation content
     * @return  a documentation {@link Section}
     * @throws IOException  if the file can't be read
     */
    public Section addOperationAndSupportSection(SoftwareSystem softwareSystem, Format format, File... files) throws IOException {
        return addOperationAndSupportSection(softwareSystem, format, readFiles(files));
    }

    /**
     * Adds a "Operation and Support" section relating to a {@link SoftwareSystem}.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param format    the {@link Format} of the documentation content
     * @param content   a String containing the documentation content
     * @return  a documentation {@link Section}
     */
    public Section addOperationAndSupportSection(SoftwareSystem softwareSystem, Format format, String content) {
        return addSection(softwareSystem, "Operation and Support", GROUP4, format, content);
    }

    /**
     * Adds a "Decision Log" section relating to a {@link SoftwareSystem} from a file.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param format    the {@link Format} of the documentation content
     * @param files  one or more File objects that point to the documentation content
     * @return  a documentation {@link Section}
     * @throws IOException  if the file can't be read
     */
    public Section addDecisionLogSection(SoftwareSystem softwareSystem, Format format, File... files) throws IOException {
        return addDecisionLogSection(softwareSystem, format, readFiles(files));
    }

    /**
     * Adds a "Decision Log" section relating to a {@link SoftwareSystem}.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param format    the {@link Format} of the documentation content
     * @param content   a String containing the documentation content
     * @return  a documentation {@link Section}
     */
    public Section addDecisionLogSection(SoftwareSystem softwareSystem, Format format, String content) {
        return addSection(softwareSystem, "Decision Log", GROUP5, format, content);
    }

}
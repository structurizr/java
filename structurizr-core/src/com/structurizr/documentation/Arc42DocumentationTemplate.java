package com.structurizr.documentation;

import com.structurizr.Workspace;
import com.structurizr.model.SoftwareSystem;

import java.io.File;
import java.io.IOException;

/**
 * <p>
 * An implementation of the <a href="http://arc42.org">arc42 documentation template</a>,
 * consisting of the following sections:
 * </p>
 *
 * <ol>
 *     <li>Introduction and Goals (1)</li>
 *     <li>Constraints (2)</li>
 *     <li>Context and Scope (2)</li>
 *     <li>Solution Strategy (3)</li>
 *     <li>Building Block View (3)</li>
 *     <li>Runtime View (3)</li>
 *     <li>Deployment View (3)</li>
 *     <li>Crosscutting Concepts (3)</li>
 *     <li>Architectural Decisions (3)</li>
 *     <li>Quality Requirements (2)</li>
 *     <li>Risks and Technical Debt (4)</li>
 *     <li>Glossary (5)</li>
 * </ol>
 *
 * <p>
 * The number in parentheses () represents the grouping, which is simply used to colour code
 * section navigation buttons when rendered.
 * </p>
 */
public class Arc42DocumentationTemplate extends DocumentationTemplate {

    public Arc42DocumentationTemplate(Workspace workspace) {
        super(workspace);
    }

    /**
     * Adds a "Introduction and Goals" section relating to a {@link SoftwareSystem} from one or more files.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param files             one or more File objects that point to the documentation content
     * @return                  a documentation {@link Section}
     * @throws IOException      if there is an error reading the files
     */
    public Section addIntroductionAndGoalsSection(SoftwareSystem softwareSystem, File... files) throws IOException {
        return addSection(softwareSystem, "Introduction and Goals", files);
    }

    /**
     * Adds a "Introduction and Goals" section relating to a {@link SoftwareSystem}.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param format            the {@link Format} of the documentation content
     * @param content           a String containing the documentation content
     * @return                  a documentation {@link Section}
     */
    public Section addIntroductionAndGoalsSection(SoftwareSystem softwareSystem, Format format, String content) {
        return addSection(softwareSystem, "Introduction and Goals", format, content);
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
    public Section addConstraintsSection(SoftwareSystem softwareSystem, Format format, String content) {
        return addSection(softwareSystem, "Constraints", format, content);
    }

    /**
     * Adds a "Context and Scope" section relating to a {@link SoftwareSystem} from one or more files.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param files             one or more File objects that point to the documentation content
     * @return                  a documentation {@link Section}
     * @throws IOException      if there is an error reading the files
     */
    public Section addContextAndScopeSection(SoftwareSystem softwareSystem, File... files) throws IOException {
        return addSection(softwareSystem, "Context and Scope", files);
    }

    /**
     * Adds a "Context and Scope" section relating to a {@link SoftwareSystem}.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param format            the {@link Format} of the documentation content
     * @param content           a String containing the documentation content
     * @return                  a documentation {@link Section}
     */
    public Section addContextAndScopeSection(SoftwareSystem softwareSystem, Format format, String content) {
        return addSection(softwareSystem, "Context and Scope", format, content);
    }

    /**
     * Adds a "Solution Strategy" section relating to a {@link SoftwareSystem} from one or more files.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param files             one or more File objects that point to the documentation content
     * @return                  a documentation {@link Section}
     * @throws IOException      if there is an error reading the files
     */
    public Section addSolutionStrategySection(SoftwareSystem softwareSystem, File... files) throws IOException {
        return addSection(softwareSystem, "Solution Strategy", files);
    }

    /**
     * Adds a "Solution Strategy" section relating to a {@link SoftwareSystem}.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param format            the {@link Format} of the documentation content
     * @param content           a String containing the documentation content
     * @return                  a documentation {@link Section}
     */
    public Section addSolutionStrategySection(SoftwareSystem softwareSystem, Format format, String content) {
        return addSection(softwareSystem, "Solution Strategy", format, content);
    }

    /**
     * Adds a "Building Block View" section relating to a {@link SoftwareSystem} from one or more files.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param files             one or more File objects that point to the documentation content
     * @return                  a documentation {@link Section}
     * @throws IOException      if there is an error reading the files
     */
    public Section addBuildingBlockViewSection(SoftwareSystem softwareSystem, File... files) throws IOException {
        return addSection(softwareSystem, "Building Block View", files);
    }

    /**
     * Adds a "Building Block View" section relating to a {@link SoftwareSystem}.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param format            the {@link Format} of the documentation content
     * @param content           a String containing the documentation content
     * @return                  a documentation {@link Section}
     */
    public Section addBuildingBlockViewSection(SoftwareSystem softwareSystem, Format format, String content) {
        return addSection(softwareSystem, "Building Block View", format, content);
    }

    /**
     * Adds a "Runtime View" section relating to a {@link SoftwareSystem} from one or more files.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param files             one or more File objects that point to the documentation content
     * @return                  a documentation {@link Section}
     * @throws IOException      if there is an error reading the files
     */
    public Section addRuntimeViewSection(SoftwareSystem softwareSystem, File... files) throws IOException {
        return addSection(softwareSystem, "Runtime View", files);
    }

    /**
     * Adds a "Runtime View" section relating to a {@link SoftwareSystem}.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param format            the {@link Format} of the documentation content
     * @param content           a String containing the documentation content
     * @return                  a documentation {@link Section}
     */
    public Section addRuntimeViewSection(SoftwareSystem softwareSystem, Format format, String content) {
        return addSection(softwareSystem, "Runtime View", format, content);
    }

    /**
     * Adds a "Deployment View" section relating to a {@link SoftwareSystem} from one or more files.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param files             one or more File objects that point to the documentation content
     * @return                  a documentation {@link Section}
     * @throws IOException      if there is an error reading the files
     */
    public Section addDeploymentViewSection(SoftwareSystem softwareSystem, File... files) throws IOException {
        return addSection(softwareSystem, "Deployment View", files);
    }

    /**
     * Adds a "Deployment View" section relating to a {@link SoftwareSystem}.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param format            the {@link Format} of the documentation content
     * @param content           a String containing the documentation content
     * @return                  a documentation {@link Section}
     */
    public Section addDeploymentViewSection(SoftwareSystem softwareSystem, Format format, String content) {
        return addSection(softwareSystem, "Deployment View", format, content);
    }

    /**
     * Adds a "Crosscutting Concepts" section relating to a {@link SoftwareSystem} from one or more files.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param files             one or more File objects that point to the documentation content
     * @return                  a documentation {@link Section}
     * @throws IOException      if there is an error reading the files
     */
    public Section addCrosscuttingConceptsSection(SoftwareSystem softwareSystem, File... files) throws IOException {
        return addSection(softwareSystem, "Crosscutting Concepts", files);
    }

    /**
     * Adds a "Crosscutting Concepts" section relating to a {@link SoftwareSystem}.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param format            the {@link Format} of the documentation content
     * @param content           a String containing the documentation content
     * @return                  a documentation {@link Section}
     */
    public Section addCrosscuttingConceptsSection(SoftwareSystem softwareSystem, Format format, String content) {
        return addSection(softwareSystem, "Crosscutting Concepts", format, content);
    }

    /**
     * Adds an "Architectural Decisions" section relating to a {@link SoftwareSystem} from one or more files.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param files             one or more File objects that point to the documentation content
     * @return                  a documentation {@link Section}
     * @throws IOException      if there is an error reading the files
     */
    public Section addArchitecturalDecisionsSection(SoftwareSystem softwareSystem, File... files) throws IOException {
        return addSection(softwareSystem, "Architectural Decisions", files);
    }

    /**
     * Adds an "Architectural Decisions" section relating to a {@link SoftwareSystem}.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param format            the {@link Format} of the documentation content
     * @param content           a String containing the documentation content
     * @return                  a documentation {@link Section}
     */
    public Section addArchitecturalDecisionsSection(SoftwareSystem softwareSystem, Format format, String content) {
        return addSection(softwareSystem, "Architectural Decisions", format, content);
    }

    /**
     * Adds a "Quality Requirements" section relating to a {@link SoftwareSystem} from one or more files.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param files             one or more File objects that point to the documentation content
     * @return                  a documentation {@link Section}
     * @throws IOException      if there is an error reading the files
     */
    public Section addQualityRequirementsSection(SoftwareSystem softwareSystem, File... files) throws IOException {
        return addSection(softwareSystem, "Quality Requirements", files);
    }

    /**
     * Adds a "Quality Requirements" section relating to a {@link SoftwareSystem}.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param format            the {@link Format} of the documentation content
     * @param content           a String containing the documentation content
     * @return                  a documentation {@link Section}
     */
    public Section addQualityRequirementsSection(SoftwareSystem softwareSystem, Format format, String content) {
        return addSection(softwareSystem, "Quality Requirements", format, content);
    }

    /**
     * Adds a "Risks and Technical Debt" section relating to a {@link SoftwareSystem} from one or more files.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param files             one or more File objects that point to the documentation content
     * @return                  a documentation {@link Section}
     * @throws IOException      if there is an error reading the files
     */
    public Section addRisksAndTechnicalDebtSection(SoftwareSystem softwareSystem, File... files) throws IOException {
        return addSection(softwareSystem, "Risks and Technical Debt", files);
    }

    /**
     * Adds a "Risks and Technical Debt" section relating to a {@link SoftwareSystem}.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param format            the {@link Format} of the documentation content
     * @param content           a String containing the documentation content
     * @return                  a documentation {@link Section}
     */
    public Section addRisksAndTechnicalDebtSection(SoftwareSystem softwareSystem, Format format, String content) {
        return addSection(softwareSystem, "Risks and Technical Debt", format, content);
    }

    /**
     * Adds a "Glossary" section relating to a {@link SoftwareSystem} from one or more files.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param files             one or more File objects that point to the documentation content
     * @return                  a documentation {@link Section}
     * @throws IOException      if there is an error reading the files
     */
    public Section addGlossarySection(SoftwareSystem softwareSystem, File... files) throws IOException {
        return addSection(softwareSystem, "Glossary", files);
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
        return addSection(softwareSystem, "Glossary", format, content);
    }

    @Override
    protected TemplateMetadata getMetadata() {
        return new TemplateMetadata("arc42", "Dr. Gernot Starke and Dr. Peter Hruschka", "http://arc42.org");
    }

}
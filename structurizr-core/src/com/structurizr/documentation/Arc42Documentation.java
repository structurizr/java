package com.structurizr.documentation;

import com.structurizr.model.Model;
import com.structurizr.model.SoftwareSystem;

import java.io.File;
import java.io.IOException;

/**
 * An implementation of the arc42 documentation template.
 *
 * See http://arc42.org for more information.
 */
public final class Arc42Documentation extends Documentation {

    public static final int GROUP1 = 1;
    public static final int GROUP2 = 2;
    public static final int GROUP3 = 3;
    public static final int GROUP4 = 4;
    public static final int GROUP5 = 5;

    Arc42Documentation() {
    }

    public Arc42Documentation(Model model) {
        super(model);
    }

    /**
     * Adds a "Introduction and Goals" section relating to a {@link SoftwareSystem} from a file.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param format    the {@link Format} of the documentation content
     * @param file  a File that points to the documentation content
     * @return  a documentation {@link Section}
     * @throws IOException  if the file can't be read
     */
    public Section addIntroductionAndGoalsSection(SoftwareSystem softwareSystem, Format format, File file) throws IOException {
        return addIntroductionAndGoalsSection(softwareSystem, format, readFile(file));
    }

    /**
     * Adds a "Introduction and Goals" section relating to a {@link SoftwareSystem}.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param format    the {@link Format} of the documentation content
     * @param content   a String containing the documentation content
     * @return  a documentation {@link Section}
     */
    public Section addIntroductionAndGoalsSection(SoftwareSystem softwareSystem, Format format, String content) {
        return addSection(softwareSystem, "Introduction And Goals", GROUP1, format, content);
    }

    /**
     * Adds a "Constraints" section relating to a {@link SoftwareSystem} from a file.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param format    the {@link Format} of the documentation content
     * @param file  a File that points to the documentation content
     * @return  a documentation {@link Section}
     * @throws IOException  if the file can't be read
     */
    public Section addConstraintsSection(SoftwareSystem softwareSystem, Format format, File file) throws IOException {
        return addConstraintsSection(softwareSystem, format, readFile(file));
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
     * Adds a "Context and Scope" section relating to a {@link SoftwareSystem} from a file.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param format    the {@link Format} of the documentation content
     * @param file  a File that points to the documentation content
     * @return  a documentation {@link Section}
     * @throws IOException  if the file can't be read
     */
    public Section addContextAndScopeSection(SoftwareSystem softwareSystem, Format format, File file) throws IOException {
        return addContextAndScopeSection(softwareSystem, format, readFile(file));
    }

    /**
     * Adds a "Context and Scope" section relating to a {@link SoftwareSystem}.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param format    the {@link Format} of the documentation content
     * @param content   a String containing the documentation content
     * @return  a documentation {@link Section}
     */
    public Section addContextAndScopeSection(SoftwareSystem softwareSystem, Format format, String content) {
        return addSection(softwareSystem, "Context And Scope", GROUP2, format, content);
    }

    /**
     * Adds a "Solution Strategy" section relating to a {@link SoftwareSystem} from a file.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param format    the {@link Format} of the documentation content
     * @param file  a File that points to the documentation content
     * @return  a documentation {@link Section}
     * @throws IOException  if the file can't be read
     */
    public Section addSolutionStrategySection(SoftwareSystem softwareSystem, Format format, File file) throws IOException {
        return addSolutionStrategySection(softwareSystem, format, readFile(file));
    }

    /**
     * Adds a "Solution Strategy" section relating to a {@link SoftwareSystem}.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param format    the {@link Format} of the documentation content
     * @param content   a String containing the documentation content
     * @return  a documentation {@link Section}
     */
    public Section addSolutionStrategySection(SoftwareSystem softwareSystem, Format format, String content) {
        return addSection(softwareSystem, "Solution Strategy", GROUP3, format, content);
    }

    /**
     * Adds a "Building Block View" section relating to a {@link SoftwareSystem} from a file.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param format    the {@link Format} of the documentation content
     * @param file  a File that points to the documentation content
     * @return  a documentation {@link Section}
     * @throws IOException  if the file can't be read
     */
    public Section addBuildingBlockViewSection(SoftwareSystem softwareSystem, Format format, File file) throws IOException {
        return addBuildingBlockViewSection(softwareSystem, format, readFile(file));
    }

    /**
     * Adds a "Building Block View" section relating to a {@link SoftwareSystem}.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param format    the {@link Format} of the documentation content
     * @param content   a String containing the documentation content
     * @return  a documentation {@link Section}
     */
    public Section addBuildingBlockViewSection(SoftwareSystem softwareSystem, Format format, String content) {
        return addSection(softwareSystem, "Building Block View", GROUP3, format, content);
    }

    /**
     * Adds a "Runtime View" section relating to a {@link SoftwareSystem} from a file.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param format    the {@link Format} of the documentation content
     * @param file  a File that points to the documentation content
     * @return  a documentation {@link Section}
     * @throws IOException  if the file can't be read
     */
    public Section addRuntimeViewSection(SoftwareSystem softwareSystem, Format format, File file) throws IOException {
        return addRuntimeViewSection(softwareSystem, format, readFile(file));
    }

    /**
     * Adds a "Runtime View" section relating to a {@link SoftwareSystem}.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param format    the {@link Format} of the documentation content
     * @param content   a String containing the documentation content
     * @return  a documentation {@link Section}
     */
    public Section addRuntimeViewSection(SoftwareSystem softwareSystem, Format format, String content) {
        return addSection(softwareSystem, "Runtime View", GROUP3, format, content);
    }

    /**
     * Adds a "Deployment View" section relating to a {@link SoftwareSystem} from a file.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param format    the {@link Format} of the documentation content
     * @param file  a File that points to the documentation content
     * @return  a documentation {@link Section}
     * @throws IOException  if the file can't be read
     */
    public Section addDeploymentViewSection(SoftwareSystem softwareSystem, Format format, File file) throws IOException {
        return addDeploymentViewSection(softwareSystem, format, readFile(file));
    }

    /**
     * Adds a "Deployment View" section relating to a {@link SoftwareSystem}.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param format    the {@link Format} of the documentation content
     * @param content   a String containing the documentation content
     * @return  a documentation {@link Section}
     */
    public Section addDeploymentViewSection(SoftwareSystem softwareSystem, Format format, String content) {
        return addSection(softwareSystem, "Deployment View", GROUP3, format, content);
    }

    /**
     * Adds a "Crosscutting Concepts" section relating to a {@link SoftwareSystem} from a file.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param format    the {@link Format} of the documentation content
     * @param file  a File that points to the documentation content
     * @return  a documentation {@link Section}
     * @throws IOException  if the file can't be read
     */
    public Section addCrosscuttingConceptsSection(SoftwareSystem softwareSystem, Format format, File file) throws IOException {
        return addCrosscuttingConceptsSection(softwareSystem, format, readFile(file));
    }

    /**
     * Adds a "Crosscutting Concepts" section relating to a {@link SoftwareSystem}.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param format    the {@link Format} of the documentation content
     * @param content   a String containing the documentation content
     * @return  a documentation {@link Section}
     */
    public Section addCrosscuttingConceptsSection(SoftwareSystem softwareSystem, Format format, String content) {
        return addSection(softwareSystem, "Crosscutting Concepts", GROUP3, format, content);
    }

    /**
     * Adds an "Architectural Decisions" section relating to a {@link SoftwareSystem} from a file.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param format    the {@link Format} of the documentation content
     * @param file  a File that points to the documentation content
     * @return  a documentation {@link Section}
     * @throws IOException  if the file can't be read
     */
    public Section addArchitecturalDecisionsSection(SoftwareSystem softwareSystem, Format format, File file) throws IOException {
        return addArchitecturalDecisionsSection(softwareSystem, format, readFile(file));
    }

    /**
     * Adds an "Architectural Decisions" section relating to a {@link SoftwareSystem}.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param format    the {@link Format} of the documentation content
     * @param content   a String containing the documentation content
     * @return  a documentation {@link Section}
     */
    public Section addArchitecturalDecisionsSection(SoftwareSystem softwareSystem, Format format, String content) {
        return addSection(softwareSystem, "Architectural Decisions", GROUP3, format, content);
    }

    /**
     * Adds a "Quality Requirements" section relating to a {@link SoftwareSystem} from a file.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param format    the {@link Format} of the documentation content
     * @param file  a File that points to the documentation content
     * @return  a documentation {@link Section}
     * @throws IOException  if the file can't be read
     */
    public Section addQualityRequirementsSection(SoftwareSystem softwareSystem, Format format, File file) throws IOException {
        return addQualityRequirementsSection(softwareSystem, format, readFile(file));
    }

    /**
     * Adds a "Quality Requirements" section relating to a {@link SoftwareSystem}.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param format    the {@link Format} of the documentation content
     * @param content   a String containing the documentation content
     * @return  a documentation {@link Section}
     */
    public Section addQualityRequirementsSection(SoftwareSystem softwareSystem, Format format, String content) {
        return addSection(softwareSystem, "Quality Requirements", GROUP2, format, content);
    }

    /**
     * Adds a "Risks and Technical Debt" section relating to a {@link SoftwareSystem} from a file.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param format    the {@link Format} of the documentation content
     * @param file  a File that points to the documentation content
     * @return  a documentation {@link Section}
     * @throws IOException  if the file can't be read
     */
    public Section addRisksAndTechnicalDebtSection(SoftwareSystem softwareSystem, Format format, File file) throws IOException {
        return addRisksAndTechnicalDebtSection(softwareSystem, format, readFile(file));
    }

    /**
     * Adds a "Risks and Technical Debt" section relating to a {@link SoftwareSystem}.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param format    the {@link Format} of the documentation content
     * @param content   a String containing the documentation content
     * @return  a documentation {@link Section}
     */
    public Section addRisksAndTechnicalDebtSection(SoftwareSystem softwareSystem, Format format, String content) {
        return addSection(softwareSystem, "Risks And Technical Debt", GROUP4, format, content);
    }

    /**
     * Adds a "Glossary" section relating to a {@link SoftwareSystem} from a file.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param format    the {@link Format} of the documentation content
     * @param file  a File that points to the documentation content
     * @return  a documentation {@link Section}
     * @throws IOException  if the file can't be read
     */
    public Section addGlossarySection(SoftwareSystem softwareSystem, Format format, File file) throws IOException {
        return addGlossarySection(softwareSystem, format, readFile(file));
    }

    /**
     * Adds a "Glossary" section relating to a {@link SoftwareSystem}.
     *
     * @param softwareSystem    the {@link SoftwareSystem} the documentation content relates to
     * @param format    the {@link Format} of the documentation content
     * @param content   a String containing the documentation content
     * @return  a documentation {@link Section}
     */
    public Section addGlossarySection(SoftwareSystem softwareSystem, Format format, String content) {
        return addSection(softwareSystem, "Glossary", GROUP5, format, content);
    }

}
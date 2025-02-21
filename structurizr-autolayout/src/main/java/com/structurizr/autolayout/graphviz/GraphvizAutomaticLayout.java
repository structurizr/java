package com.structurizr.autolayout.graphviz;

import com.structurizr.Workspace;
import com.structurizr.export.Diagram;
import com.structurizr.view.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.Locale;

/**
 * Applies the graphviz automatic layout to views in a Structurizr workspace.
 *
 * Note: this class assumes that the "dot" command is available.
 */
public class GraphvizAutomaticLayout {

    private static final Log log = LogFactory.getLog(GraphvizAutomaticLayout.class);

    private static final String DOT_EXECUTABLE = "dot";
    private static final String USE_SVG_OUTPUT_FORMAT_OPTION = "-Tsvg";
    private static final String AUTOMATICALLY_GENERATE_OUTPUT_FILE_OPTION = "-O";
    private static final String DOT_FILE_EXTENSION = ".dot";

    private final File path;

    private RankDirection rankDirection = RankDirection.TopBottom;
    private double rankSeparation = 300;
    private double nodeSeparation = 300;

    private int margin = 400;
    private boolean changePaperSize = true;

    private Locale locale = Locale.US;

    public GraphvizAutomaticLayout() {
        this(new File("."));
    }

    public GraphvizAutomaticLayout(File path) {
        this.path = path;
    }

    public void setRankDirection(RankDirection rankDirection) {
        this.rankDirection = rankDirection;
    }

    public void setRankSeparation(double rankSeparation) {
        this.rankSeparation = rankSeparation;
    }

    public void setNodeSeparation(double nodeSeparation) {
        this.nodeSeparation = nodeSeparation;
    }

    public void setMargin(int margin) {
        this.margin = margin;
    }

    public void setChangePaperSize(boolean changePaperSize) {
        this.changePaperSize = changePaperSize;
    }

    /**
     * Sets the locale used when writing DOT files.
     *
     * @param locale        a Locale instance
     */
    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    private DOTExporter createDOTExporter(AutomaticLayout automaticLayout) {
        DOTExporter exporter;

        if (automaticLayout == null) {
            // use the configured defaults
            exporter = new DOTExporter(rankDirection, rankSeparation, nodeSeparation);
        } else {
            // use the values from the automatic layout configuration associated with the view
            exporter = new DOTExporter(
                    RankDirection.valueOf(automaticLayout.getRankDirection().name()),
                    automaticLayout.getRankSeparation(),
                    automaticLayout.getNodeSeparation()
                );
        }

        exporter.setLocale(locale);

        return exporter;
    }

    private void writeFile(Diagram diagram) throws Exception {
        File file = new File(path, diagram.getKey() + DOT_FILE_EXTENSION);
        log.debug("Writing " + file.getAbsolutePath());
        BufferedWriter writer = Files.newBufferedWriter(file.toPath(), StandardCharsets.UTF_8);
        writer.write(diagram.getDefinition());
        writer.flush();
        writer.close();

        if (!file.exists()) {
            log.error(file.getAbsolutePath() + " does not exist");
        }
    }

    private SVGReader createSVGReader() {
        return new SVGReader(path, margin, changePaperSize);
    }

    private void runGraphviz(View view) throws Exception {
        ProcessBuilder processBuilder = new ProcessBuilder().inheritIO();
        List<String> command = List.of(
                DOT_EXECUTABLE,
                new File(path, view.getKey() + DOT_FILE_EXTENSION).getAbsolutePath(),
                USE_SVG_OUTPUT_FORMAT_OPTION,
                AUTOMATICALLY_GENERATE_OUTPUT_FILE_OPTION
        );

        processBuilder.command(command);

        StringBuilder buf = new StringBuilder();
        for (String s : command) {
            buf.append(s);
            buf.append(" ");
        }
        log.debug(buf);

        Process process = processBuilder.start();
        int exitCode = process.waitFor();
        assert exitCode == 0;

        String input = new String(process.getInputStream().readAllBytes());
        String error = new String(process.getErrorStream().readAllBytes());

        log.debug("stdout: " + input);
        log.debug("stderr: " + error);
    }

    public void apply(CustomView view) throws Exception {
        log.debug("Running Graphviz for view with key " + view.getKey());
        Diagram diagram = createDOTExporter(view.getAutomaticLayout()).export(view);
        writeFile(diagram);
        runGraphviz(view);
        createSVGReader().parseAndApplyLayout(view);
    }

    public void apply(SystemLandscapeView view) throws Exception {
        log.debug("Running Graphviz for view with key " + view.getKey());
        Diagram diagram = createDOTExporter(view.getAutomaticLayout()).export(view);
        writeFile(diagram);
        runGraphviz(view);
        createSVGReader().parseAndApplyLayout(view);
    }

    public void apply(SystemContextView view) throws Exception {
        log.debug("Running Graphviz for view with key " + view.getKey());
        Diagram diagram = createDOTExporter(view.getAutomaticLayout()).export(view);
        writeFile(diagram);
        runGraphviz(view);
        createSVGReader().parseAndApplyLayout(view);
    }

    public void apply(ContainerView view) throws Exception {
        log.debug("Running Graphviz for view with key " + view.getKey());
        Diagram diagram = createDOTExporter(view.getAutomaticLayout()).export(view);
        writeFile(diagram);
        runGraphviz(view);
        createSVGReader().parseAndApplyLayout(view);
    }

    public void apply(ComponentView view) throws Exception {
        log.debug("Running Graphviz for view with key " + view.getKey());
        Diagram diagram = createDOTExporter(view.getAutomaticLayout()).export(view);
        writeFile(diagram);
        runGraphviz(view);
        createSVGReader().parseAndApplyLayout(view);
    }

    public void apply(DynamicView view) throws Exception {
        log.debug("Running Graphviz for view with key " + view.getKey());
        Diagram diagram = createDOTExporter(view.getAutomaticLayout()).export(view);
        writeFile(diagram);
        runGraphviz(view);
        createSVGReader().parseAndApplyLayout(view);
    }

    public void apply(DeploymentView view) throws Exception {
        log.debug("Running Graphviz for view with key " + view.getKey());
        Diagram diagram = createDOTExporter(view.getAutomaticLayout()).export(view);
        writeFile(diagram);
        runGraphviz(view);
        createSVGReader().parseAndApplyLayout(view);
    }

    public void apply(Workspace workspace) throws Exception {
        for (CustomView view : workspace.getViews().getCustomViews()) {
            if (view.getAutomaticLayout() != null && view.getAutomaticLayout().getImplementation() == AutomaticLayout.Implementation.Graphviz) {
                apply(view);
            }
        }

        for (SystemLandscapeView view : workspace.getViews().getSystemLandscapeViews()) {
            if (view.getAutomaticLayout() != null && view.getAutomaticLayout().getImplementation() == AutomaticLayout.Implementation.Graphviz) {
                apply(view);
            }
        }

        for (SystemContextView view : workspace.getViews().getSystemContextViews()) {
            if (view.getAutomaticLayout() != null && view.getAutomaticLayout().getImplementation() == AutomaticLayout.Implementation.Graphviz) {
                apply(view);
            }
        }

        for (ContainerView view : workspace.getViews().getContainerViews()) {
            if (view.getAutomaticLayout() != null && view.getAutomaticLayout().getImplementation() == AutomaticLayout.Implementation.Graphviz) {
                apply(view);
            }
        }

        for (ComponentView view : workspace.getViews().getComponentViews()) {
            if (view.getAutomaticLayout() != null && view.getAutomaticLayout().getImplementation() == AutomaticLayout.Implementation.Graphviz) {
                apply(view);
            }
        }

        for (DynamicView view : workspace.getViews().getDynamicViews()) {
            if (view.getAutomaticLayout() != null && view.getAutomaticLayout().getImplementation() == AutomaticLayout.Implementation.Graphviz) {
                apply(view);
            }
        }

        for (DeploymentView view : workspace.getViews().getDeploymentViews()) {
            if (view.getAutomaticLayout() != null && view.getAutomaticLayout().getImplementation() == AutomaticLayout.Implementation.Graphviz) {
                apply(view);
            }
        }
    }

}
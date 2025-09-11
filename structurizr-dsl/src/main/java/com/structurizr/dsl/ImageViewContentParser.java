package com.structurizr.dsl;

import com.structurizr.export.mermaid.MermaidDiagramExporter;
import com.structurizr.export.plantuml.StructurizrPlantUMLExporter;
import com.structurizr.importer.diagrams.kroki.KrokiImporter;
import com.structurizr.importer.diagrams.mermaid.MermaidImporter;
import com.structurizr.importer.diagrams.plantuml.PlantUMLImporter;
import com.structurizr.util.ImageUtils;
import com.structurizr.util.Url;
import com.structurizr.view.ImageView;
import com.structurizr.view.ModelView;
import com.structurizr.view.View;

import java.io.File;

final class ImageViewContentParser extends AbstractParser {

    private static final String PLANTUML_GRAMMAR = "plantuml <source|file|url|viewKey>";
    private static final String MERMAID_GRAMMAR = "mermaid <source|file|url|viewKey>";
    private static final String KROKI_GRAMMAR = "kroki <format> <source|file|url>";
    private static final String IMAGE_GRAMMAR = "image <file|url>";

    private static final int PLANTUML_SOURCE_INDEX = 1;
    private static final int MERMAID_SOURCE_INDEX = 1;
    private static final int KROKI_FORMAT_INDEX = 1;
    private static final int KROKI_SOURCE_INDEX = 2;
    private static final int IMAGE_SOURCE_INDEX = 1;

    private boolean restricted = false;

    ImageViewContentParser(boolean restricted) {
        this.restricted = restricted;
    }

    void parsePlantUML(ImageViewDslContext context, File dslFile, Tokens tokens) {
        // plantuml <source|file|url|viewKey>

        if (tokens.hasMoreThan(PLANTUML_SOURCE_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: " + PLANTUML_GRAMMAR);
        }

        ImageView view = context.getView();
        if (view != null) {
            if (tokens.size() == 2) {
                String source = tokens.get(PLANTUML_SOURCE_INDEX);

                try {
                    if (source.contains("\n")) {
                        // inline source
                        new PlantUMLImporter().importDiagram(context.getView(), source);
                    } else {
                        View viewWithKey = context.getWorkspace().getViews().getViewWithKey(source);
                        if (viewWithKey instanceof ModelView) {
                            StructurizrPlantUMLExporter exporter = new StructurizrPlantUMLExporter();
                            String plantuml = exporter.export((ModelView) viewWithKey).getDefinition();
                            new PlantUMLImporter().importDiagram(context.getView(), plantuml);
                        } else {
                            if (Url.isUrl(source)) {
                                RemoteContent content = readFromUrl(source);
                                new PlantUMLImporter().importDiagram(context.getView(), content.getContent());
                                context.getView().setTitle(source.substring(source.lastIndexOf("/") + 1));
                            } else {
                                if (!restricted) {
                                    File file = new File(dslFile.getParentFile(), source);
                                    if (file.exists()) {
                                        context.setDslPortable(false);
                                        new PlantUMLImporter().importDiagram(context.getView(), file);
                                    } else {
                                        throw new RuntimeException("The file at " + file.getAbsolutePath() + " does not exist");
                                    }
                                } else {
                                    throw new RuntimeException("PlantUML source must be specified as a URL when running in restricted mode");
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage());
                }
            } else {
                throw new RuntimeException("Expected: " + PLANTUML_GRAMMAR);
            }
        }
    }

    void parseMermaid(ImageViewDslContext context, File dslFile, Tokens tokens) {
        // mermaid <source|file|url|viewKey>

        if (tokens.hasMoreThan(MERMAID_SOURCE_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: " + MERMAID_GRAMMAR);
        }

        ImageView view = context.getView();
        if (view != null) {
            if (tokens.size() == 2) {
                String source = tokens.get(MERMAID_SOURCE_INDEX);

                try {
                    if (source.contains("\n")) {
                        // inline source
                        new MermaidImporter().importDiagram(context.getView(), source);
                    } else {
                        View viewWithKey = context.getWorkspace().getViews().getViewWithKey(source);
                        if (viewWithKey instanceof ModelView) {
                            MermaidDiagramExporter exporter = new MermaidDiagramExporter();
                            String mermaid = exporter.export((ModelView) viewWithKey).getDefinition();
                            new MermaidImporter().importDiagram(context.getView(), mermaid);
                        } else {
                            if (Url.isUrl(source)) {
                                RemoteContent content = readFromUrl(source);
                                new MermaidImporter().importDiagram(context.getView(), content.getContent());
                                context.getView().setTitle(source.substring(source.lastIndexOf("/") + 1));
                            } else {
                                if (!restricted) {
                                    File file = new File(dslFile.getParentFile(), source);
                                    if (file.exists()) {
                                        context.setDslPortable(false);
                                        new MermaidImporter().importDiagram(context.getView(), file);
                                    } else {
                                        throw new RuntimeException("The file at " + file.getAbsolutePath() + " does not exist");
                                    }
                                } else {
                                    throw new RuntimeException("Mermaid source must be specified as a URL when running in restricted mode");
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage());
                }
            } else {
                throw new RuntimeException("Expected: " + MERMAID_GRAMMAR);
            }
        }
    }

    void parseKroki(ImageViewDslContext context, File dslFile, Tokens tokens) {
        // kroki <format> <source|file|url>

        if (tokens.hasMoreThan(KROKI_SOURCE_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: " + KROKI_GRAMMAR);
        }

        ImageView view = context.getView();
        if (view != null) {
            if (tokens.size() == 3) {
                String format = tokens.get(KROKI_FORMAT_INDEX);
                String source = tokens.get(KROKI_SOURCE_INDEX);

                try {
                    if (source.contains("\n")) {
                        // inline source
                        new KrokiImporter().importDiagram(context.getView(), format, source);
                    } else {
                        if (Url.isUrl(source)) {
                            RemoteContent content = readFromUrl(source);
                            new KrokiImporter().importDiagram(context.getView(), format, content.getContent());
                            context.getView().setTitle(source.substring(source.lastIndexOf("/") + 1));
                        } else {
                            if (!restricted) {
                                File file = new File(dslFile.getParentFile(), source);
                                if (file.exists()) {
                                    context.setDslPortable(false);
                                    new KrokiImporter().importDiagram(context.getView(), format, file);
                                } else {
                                    throw new RuntimeException("The file at " + file.getAbsolutePath() + " does not exist");
                                }
                            } else {
                                throw new RuntimeException("Kroki source must be specified as a URL when running in restricted mode");
                            }
                        }
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage());
                }
            } else {
                throw new RuntimeException("Expected: " + KROKI_GRAMMAR);
            }
        }
    }

    void parseImage(ImageViewDslContext context, File dslFile, Tokens tokens) {
        // image <file|url>

        if (tokens.hasMoreThan(IMAGE_SOURCE_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: " + IMAGE_GRAMMAR);
        }

        ImageView view = context.getView();
        if (view != null) {
            if (tokens.size() == 2) {
                String source = tokens.get(IMAGE_SOURCE_INDEX);

                try {
                    if (Url.isUrl(source)) {
                        context.getView().setContent(source);
                        context.getView().setTitle(source.substring(source.lastIndexOf("/")+1));
                    } else {
                        if (!restricted) {
                            File file = new File(dslFile.getParentFile(), source);
                            if (file.exists()) {
                                context.setDslPortable(false);
                                context.getView().setContent(ImageUtils.getImageAsDataUri(file));
                                context.getView().setTitle(file.getName());
                            } else {
                                throw new RuntimeException("The file at " + file.getAbsolutePath() + " does not exist");
                            }
                        } else {
                            throw new RuntimeException("Images must be specified as a URL when running in restricted mode");
                        }
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage());
                }
            } else {
                throw new RuntimeException("Expected: " + IMAGE_GRAMMAR);
            }
        }
    }

}
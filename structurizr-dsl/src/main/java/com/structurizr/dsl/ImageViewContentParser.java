package com.structurizr.dsl;

import com.structurizr.export.mermaid.MermaidDiagramExporter;
import com.structurizr.export.plantuml.StructurizrPlantUMLExporter;
import com.structurizr.http.RemoteContent;
import com.structurizr.importer.diagrams.image.ImageImporter;
import com.structurizr.importer.diagrams.kroki.KrokiImporter;
import com.structurizr.importer.diagrams.mermaid.MermaidImporter;
import com.structurizr.importer.diagrams.plantuml.PlantUMLImporter;
import com.structurizr.util.FeatureNotEnabledException;
import com.structurizr.util.StringUtils;
import com.structurizr.util.Url;
import com.structurizr.view.ColorScheme;
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

    ImageViewContentParser() {
    }

    void parsePlantUML(ImageViewDslContext context, File dslFile, Tokens tokens) {
        // plantuml <source|file|url|viewKey>

        if (!tokens.includes(PLANTUML_SOURCE_INDEX)) {
            throw new RuntimeException("Expected: " + PLANTUML_GRAMMAR);
        }

        if (tokens.hasMoreThan(PLANTUML_SOURCE_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: " + PLANTUML_GRAMMAR);
        }

        String source = tokens.get(PLANTUML_SOURCE_INDEX);
        ColorScheme colorScheme = context.getColorScheme();

        try {
            if (source.contains("\n")) {
                // inline source
                new PlantUMLImporter(context.getHttpClient()).importDiagram(context.getView(), source, colorScheme);
            } else {
                View viewWithKey = context.getWorkspace().getViews().getViewWithKey(source);
                if (viewWithKey instanceof ModelView) {
                    String plantumlLight = new StructurizrPlantUMLExporter(ColorScheme.Light).export((ModelView) viewWithKey).getDefinition();
                    new PlantUMLImporter(context.getHttpClient()).importDiagram(context.getView(), plantumlLight, ColorScheme.Light);

                    String plantumlDark = new StructurizrPlantUMLExporter(ColorScheme.Dark).export((ModelView) viewWithKey).getDefinition();
                    new PlantUMLImporter(context.getHttpClient()).importDiagram(context.getView(), plantumlDark, ColorScheme.Dark);

                    if (!StringUtils.isNullOrEmpty(viewWithKey.getTitle())) {
                        context.getView().setTitle(viewWithKey.getTitle());
                    } else {
                        context.getView().setTitle(viewWithKey.getName());
                    }
                    context.getView().setDescription(viewWithKey.getDescription());
                } else {
                    if (Url.isHttpsUrl(source) || Url.isHttpUrl(source)) {
                        if (Url.isHttpsUrl(source) && !context.getFeatures().isEnabled(Features.HTTPS)) {
                            throw new FeatureNotEnabledException(Features.HTTPS, "Image views via HTTPS are not permitted");
                        }
                        if (Url.isHttpUrl(source) && !context.getFeatures().isEnabled(Features.HTTP)) {
                            throw new FeatureNotEnabledException(Features.HTTP, "Image views via HTTP are not permitted");
                        }

                        RemoteContent content = context.getHttpClient().get(source);
                        new PlantUMLImporter(context.getHttpClient()).importDiagram(context.getView(), content.getContentAsString(), colorScheme);
                        context.getView().setTitle(source.substring(source.lastIndexOf("/") + 1));
                    } else {
                        if (context.getFeatures().isEnabled(Features.FILE_SYSTEM)) {
                            File file = new File(dslFile.getParentFile(), source);
                            if (file.exists()) {
                                context.setDslPortable(false);
                                new PlantUMLImporter(context.getHttpClient()).importDiagram(context.getView(), file, colorScheme);
                            } else {
                                throw new RuntimeException("The file at " + file.getAbsolutePath() + " does not exist");
                            }
                        } else {
                            throw new FeatureNotEnabledException(Features.FILE_SYSTEM, "plantuml <file> is not permitted");
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    void parseMermaid(ImageViewDslContext context, File dslFile, Tokens tokens) {
        // mermaid <source|file|url|viewKey>

        if (!tokens.includes(MERMAID_SOURCE_INDEX)) {
            throw new RuntimeException("Expected: " + MERMAID_GRAMMAR);
        }

        if (tokens.hasMoreThan(MERMAID_SOURCE_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: " + MERMAID_GRAMMAR);
        }

        String source = tokens.get(MERMAID_SOURCE_INDEX);
        ColorScheme colorScheme = context.getColorScheme();

        try {
            if (source.contains("\n")) {
                // inline source
                new MermaidImporter(context.getHttpClient()).importDiagram(context.getView(), source, colorScheme);
            } else {
                View viewWithKey = context.getWorkspace().getViews().getViewWithKey(source);
                if (viewWithKey instanceof ModelView) {
                    MermaidDiagramExporter exporter = new MermaidDiagramExporter();
                    String mermaid = exporter.export((ModelView) viewWithKey).getDefinition();
                    new MermaidImporter(context.getHttpClient()).importDiagram(context.getView(), mermaid, colorScheme);

                    if (!StringUtils.isNullOrEmpty(viewWithKey.getTitle())) {
                        context.getView().setTitle(viewWithKey.getTitle());
                    } else {
                        context.getView().setTitle(viewWithKey.getName());
                    }
                    context.getView().setDescription(viewWithKey.getDescription());
                } else {
                    if (Url.isHttpsUrl(source) || Url.isHttpUrl(source)) {
                        if (Url.isHttpsUrl(source) && !context.getFeatures().isEnabled(Features.HTTPS)) {
                            throw new FeatureNotEnabledException(Features.HTTPS, "Image views via HTTPS are not permitted");
                        }
                        if (Url.isHttpUrl(source) && !context.getFeatures().isEnabled(Features.HTTP)) {
                            throw new FeatureNotEnabledException(Features.HTTP, "Image views via HTTP are not permitted");
                        }

                        RemoteContent content = context.getHttpClient().get(source);
                        new MermaidImporter(context.getHttpClient()).importDiagram(context.getView(), content.getContentAsString(), colorScheme);
                        context.getView().setTitle(source.substring(source.lastIndexOf("/") + 1));
                    } else {
                        if (context.getFeatures().isEnabled(Features.FILE_SYSTEM)) {
                            File file = new File(dslFile.getParentFile(), source);
                            if (file.exists()) {
                                context.setDslPortable(false);
                                new MermaidImporter(context.getHttpClient()).importDiagram(context.getView(), file, colorScheme);
                            } else {
                                throw new RuntimeException("The file at " + file.getAbsolutePath() + " does not exist");
                            }
                        } else {
                            throw new FeatureNotEnabledException(Features.FILE_SYSTEM, "mermaid <file> is not permitted");
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    void parseKroki(ImageViewDslContext context, File dslFile, Tokens tokens) {
        // kroki <format> <source|file|url>

        if (!tokens.includes(KROKI_SOURCE_INDEX)) {
            throw new RuntimeException("Expected: " + KROKI_GRAMMAR);
        }

        if (tokens.hasMoreThan(KROKI_SOURCE_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: " + KROKI_GRAMMAR);
        }

        String format = tokens.get(KROKI_FORMAT_INDEX);
        String source = tokens.get(KROKI_SOURCE_INDEX);
        ColorScheme colorScheme = context.getColorScheme();

        try {
            if (source.contains("\n")) {
                // inline source
                new KrokiImporter(context.getHttpClient()).importDiagram(context.getView(), format, source, colorScheme);
            } else {
                if (Url.isHttpsUrl(source) || Url.isHttpUrl(source)) {
                    if (Url.isHttpsUrl(source) && !context.getFeatures().isEnabled(Features.HTTPS)) {
                        throw new FeatureNotEnabledException(Features.HTTPS, "Image views via HTTPS are not permitted");
                    }
                    if (Url.isHttpUrl(source) && !context.getFeatures().isEnabled(Features.HTTP)) {
                        throw new FeatureNotEnabledException(Features.HTTP, "Image views via HTTP are not permitted");
                    }

                    RemoteContent content = context.getHttpClient().get(source);
                    new KrokiImporter(context.getHttpClient()).importDiagram(context.getView(), format, content.getContentAsString(), colorScheme);
                    context.getView().setTitle(source.substring(source.lastIndexOf("/") + 1));
                } else {
                    if (context.getFeatures().isEnabled(Features.FILE_SYSTEM)) {
                        File file = new File(dslFile.getParentFile(), source);
                        if (file.exists()) {
                            context.setDslPortable(false);
                            new KrokiImporter(context.getHttpClient()).importDiagram(context.getView(), format, file, colorScheme);
                        } else {
                            throw new RuntimeException("The file at " + file.getAbsolutePath() + " does not exist");
                        }
                    } else {
                        throw new FeatureNotEnabledException(Features.FILE_SYSTEM, "kroki " + format + " <file> is not permitted");
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    void parseImage(ImageViewDslContext context, File dslFile, Tokens tokens) {
        // image <file|url>

        if (!tokens.includes(IMAGE_SOURCE_INDEX)) {
            throw new RuntimeException("Expected: " + IMAGE_GRAMMAR);
        }

        if (tokens.hasMoreThan(IMAGE_SOURCE_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: " + IMAGE_GRAMMAR);
        }

        String source = tokens.get(IMAGE_SOURCE_INDEX);
        ColorScheme colorScheme = context.getColorScheme();

        try {
            if (Url.isHttpsUrl(source) || Url.isHttpUrl(source)) {
                if (Url.isHttpsUrl(source) && !context.getFeatures().isEnabled(Features.HTTPS)) {
                    throw new FeatureNotEnabledException(Features.HTTPS, "Image views via HTTPS are not permitted");
                }
                if (Url.isHttpUrl(source) && !context.getFeatures().isEnabled(Features.HTTP)) {
                    throw new FeatureNotEnabledException(Features.HTTP, "Image views via HTTP are not permitted");
                }

                new ImageImporter(context.getHttpClient()).importDiagram(context.getView(), source, colorScheme);
            } else {
                if (context.getFeatures().isEnabled(Features.FILE_SYSTEM)) {
                    File file = new File(dslFile.getParentFile(), source);
                    if (file.exists()) {
                        context.setDslPortable(false);
                        new ImageImporter(context.getHttpClient()).importDiagram(context.getView(), file, colorScheme);
                    } else {
                        throw new RuntimeException("The file at " + file.getAbsolutePath() + " does not exist");
                    }
                } else {
                    throw new FeatureNotEnabledException(Features.FILE_SYSTEM, "image <file> is not permitted");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
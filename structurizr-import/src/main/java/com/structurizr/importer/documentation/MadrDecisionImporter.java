package com.structurizr.importer.documentation;

import com.structurizr.documentation.Decision;
import com.structurizr.documentation.Documentable;
import com.structurizr.documentation.Format;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Imports architecture decision records in MADR format (see https://adr.github.io/madr).
 */
public class MadrDecisionImporter extends AbstractDecisionImporter {

    private static final Log log = LogFactory.getLog(MadrDecisionImporter.class);

    private static final String STATUS_PREFIX = "status: ";
    private static final String DEFAULT_STATUS = "accepted";

    private static final Pattern LINK_REGEX = Pattern.compile("\\[.*]\\((.*)\\)");
    private static final String FRONT_MATTER_SEPARATOR = "---";
    private static final String DEFAULT_LINK_DESCRIPTION = "Links to";

    /**
     * Imports Markdown files from the specified path, one per decision.
     *
     * @param documentable      the item that decisions should be associated with
     * @param path              the path to import decisions from
     */
    @Override
    public void importDocumentation(Documentable documentable, File path) {
        if (documentable == null) {
            throw new IllegalArgumentException("A workspace, software system, container, or component must be specified.");
        }

        if (path == null) {
            throw new IllegalArgumentException("A path must be specified.");
        } else if (!path.exists()) {
            throw new IllegalArgumentException(path.getAbsolutePath() + " does not exist.");
        }

        if (!path.isDirectory()) {
            throw new IllegalArgumentException(path.getAbsolutePath() + " is not a directory.");
        }

            Map<String, Decision> decisionsById = new LinkedHashMap<>();

            File[] markdownFiles = path.listFiles((dir, name) -> name.matches("\\d{4}-.+?.md"));
            if (markdownFiles != null) {
                Map<String, Decision> decisionsByFilename = new HashMap<>();

                Arrays.sort(markdownFiles, Comparator.comparing(File::getName));

                for (File file : markdownFiles) {
                    try {
                        Decision decision = importDecision(file);
                        documentable.getDocumentation().addDecision(decision);

                        decisionsById.put(decision.getId(), decision);
                        decisionsByFilename.put(file.getName(), decision);
                    } catch (Exception e) {
                        throw new DocumentationImportException("Error importing decision from + " + file.getAbsolutePath(), e);
                    }
                }

                for (Decision decision : decisionsById.values()) {
                    try {
                        extractLinks(decision, decisionsByFilename);

                        // and replace file references, for example "0008-some-decision.md" -> "#8"
                        String content = decision.getContent();
                        for (String filename : decisionsByFilename.keySet()) {
                            content = content.replace(filename, calculateUrl(decisionsByFilename.get(filename)));
                        }
                        decision.setContent(content);
                    } catch (Exception e) {
                        log.warn("Error extracting links from decision with ID " + decision.getId());
                    }
                }
            }
    }

    protected Decision importDecision(File file) throws Exception {
        String id = extractIntegerIDFromFileName(file);
        Decision decision = new Decision(id);

        String content = Files.readString(file.toPath(), characterEncoding);
        content = content.replace("\r", "");

        String contentWithoutFrontMatter = content.replaceFirst("(?m)^---\n(^.*\n)*?---\n", "");
        decision.setContent(contentWithoutFrontMatter);

        String[] lines = contentWithoutFrontMatter.split("\\n");
        decision.setTitle(extractTitle(lines));
        decision.setDate(extractDate(file));
        decision.setStatus(extractStatus(content.split("\\n")));
        decision.setFormat(Format.Markdown);

        return decision;
    }

    protected String extractIntegerIDFromFileName(File file) {
        return "" + Integer.parseInt(file.getName().substring(0, 4));
    }

    protected String extractTitle(String[] lines) {
        // the title is assumed to be the first line of the content, in the format:
        // # {DECISION_TITLE}
        for (String line : lines) {
            if (line.startsWith("# ")) {
                return line.substring(2);
            }
        }

        return "Title";
    }

    protected Date extractDate(File file) throws Exception {
        return new Date(file.lastModified());
    }

    protected String extractStatus(String[] lines) {
        // the date is on a line of its own, in the front matter, in the format:
        // status: {DECISION_STATUS}
        if (lines[0].startsWith(FRONT_MATTER_SEPARATOR)) {
            for (int i = 1; i < lines.length; i++) {
                String line = lines[i];

                if (line.startsWith(FRONT_MATTER_SEPARATOR)) {
                    // we've hit the end of the front matter
                    return DEFAULT_STATUS;
                } else if (line.startsWith(STATUS_PREFIX)) {
                    return line.substring(STATUS_PREFIX.length());
                }
            }
        }

        return DEFAULT_STATUS;
    }

    protected void extractLinks(Decision decision, Map<String,Decision> decisionsByFilename) {
        // extracts standard Markdown links from the content
        String[] lines = decision.getContent().split("\\n");
        for (String line : lines) {
            Matcher matcher = LINK_REGEX.matcher(line);
            while (matcher.find()) {
                String target = matcher.group(1);

                Decision targetDecision = decisionsByFilename.get(target);
                if (targetDecision != null) {
                    decision.addLink(targetDecision, DEFAULT_LINK_DESCRIPTION);
                }
            }
        }
    }

    protected String calculateUrl(Decision decision) throws Exception {
        return "#" + urlEncode(decision.getId());
    }

    protected String urlEncode(String value) throws Exception {
        return URLEncoder.encode(value, StandardCharsets.UTF_8.toString()).replaceAll("\\+", "%20");
    }

}
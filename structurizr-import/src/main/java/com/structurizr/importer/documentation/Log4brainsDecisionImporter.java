package com.structurizr.importer.documentation;

import com.structurizr.documentation.Decision;
import com.structurizr.documentation.Documentable;
import com.structurizr.documentation.Format;

import java.io.File;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Imports architecture decision records created/managed by Log4brains (https://github.com/thomvaill/log4brains).
 * See https://github.com/thomvaill/log4brains/blob/master/docs/adr/template.md for the template.
 */
public class Log4brainsDecisionImporter extends AbstractDecisionImporter {

    private static final String DATE_PREFIX = "- Date: ";
    private static final String STATUS_PREFIX = "- Status: ";
    private static final Pattern STATUS_LINK_REGEX = Pattern.compile("- Status: (.*) \\[.*]\\((.*)\\)");
    private static final String SUPERSEDED = "superseded";
    private static final String LINKS_HEADING = "## Links";

    private static final Pattern LINK_REGEX = Pattern.compile("- (.*) \\[.*]\\((.*)\\)");

    private static final String DATE_FORMAT_IN_FILENAME = "yyyyMMdd";
    private static final String DATE_FORMAT_IN_CONTENT = "yyyy-MM-dd";

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

        try {
            Map<String, Decision> decisionsById = new LinkedHashMap<>();

            File[] markdownFiles = path.listFiles((dir, name) -> name.matches("\\d{4}\\d{2}\\d{2}-.+?.md"));
            if (markdownFiles != null) {
                Map<String,Decision> decisionsByFilename = new HashMap<>();

                Arrays.sort(markdownFiles, Comparator.comparing(File::getName));

                int decisionId = 1;
                for (File file : markdownFiles) {
                    Decision decision = importDecision(decisionId, file);
                    documentable.getDocumentation().addDecision(decision);

                    decisionsById.put(decision.getId(), decision);
                    decisionsByFilename.put(file.getName(), decision);
                    decisionId++;
                }

                for (Decision decision : decisionsById.values()) {
                    extractLinks(decision, decisionsByFilename);

                    // and replace file references, for example "0008-some-decision.md" -> "#8"
                    String content = decision.getContent();
                    for (String filename : decisionsByFilename.keySet()) {
                        content = content.replace(filename, calculateUrl(decisionsByFilename.get(filename)));
                    }
                    decision.setContent(content);
                }
            }
        } catch (Exception e) {
            throw new DocumentationImportException(e);
        }
    }

    protected Decision importDecision(int id, File file) throws Exception {
        Decision decision = new Decision("" + id);

        String content = Files.readString(file.toPath(), characterEncoding);
        content = content.replace("\r", "");
        decision.setContent(content);

        String[] lines = content.split("\\n");
        decision.setTitle(extractTitle(lines));

        decision.setDate(extractDateFromFilename(file));
        Date dateFromContent = extractDate(lines);
        if (dateFromContent != null) {
            decision.setDate(dateFromContent);
        }

        decision.setStatus(extractStatus(lines));
        decision.setFormat(Format.Markdown);

        return decision;
    }

    protected Date extractDateFromFilename(File file) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_IN_FILENAME);
        sdf.setTimeZone(timeZone);

        return sdf.parse(file.getName().substring(0, DATE_FORMAT_IN_FILENAME.length()));
    }

    protected String extractTitle(String[] lines) {
        // the title is assumed to be the first line of the content, in the format:
        // # {DECISION_TITLE}
        String titleLine = lines[0];

        return titleLine.substring(2);
    }

    protected Date extractDate(String[] lines) throws Exception {
        // the date can optionally be on a line of its own, in the format:
        // - Date: {DECISION_DATE:YYYY-MM-DD}
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_IN_CONTENT);
        sdf.setTimeZone(timeZone);

        for (String line : lines) {
            if (line.startsWith(DATE_PREFIX)) {
                String dateAsString = line.substring(DATE_PREFIX.length());

                return sdf.parse(dateAsString);
            }
        }

        return null;
    }

    protected String extractStatus(String[] lines) {
        // the date is on a line of its own, in the format:
        // - Status: {DECISION_STATUS}
        for (String line : lines) {
            if (line.startsWith(STATUS_PREFIX)) {
                String status = line.substring(STATUS_PREFIX.length());
                if (status.startsWith(SUPERSEDED)) {
                    // superseded by [slug](filename)
                    return SUPERSEDED;
                } else {
                    return status;
                }
            }
        }

        return "";
    }

    protected void extractLinks(Decision decision, Map<String,Decision> decisionsByFilename) {
        // extracts links from:
        // 1. the status line
        // 2. the final ## Links section (if present)
        boolean inLinksSection = false;
        String[] lines = decision.getContent().split("\\n");
        for (String line : lines) {
            if (line.startsWith(STATUS_PREFIX)) {
                Matcher matcher = STATUS_LINK_REGEX.matcher(line);
                if (matcher.find()) {
                    String linkDescription = matcher.group(1);
                    String markdownFile = matcher.group(2);

                    Decision targetDecision = decisionsByFilename.get(markdownFile);
                    if (targetDecision != null) {
                        decision.addLink(targetDecision, linkDescription);
                    }
                }
            }

            if (line.startsWith(LINKS_HEADING)) {
                inLinksSection = true;
            }

            if (inLinksSection) {
                Matcher matcher = LINK_REGEX.matcher(line);
                if (matcher.find()) {
                    String linkDescription = matcher.group(1);
                    String target = matcher.group(2);

                    Decision targetDecision = decisionsByFilename.get(target);
                    if (targetDecision != null) {
                        decision.addLink(targetDecision, linkDescription);
                    }
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
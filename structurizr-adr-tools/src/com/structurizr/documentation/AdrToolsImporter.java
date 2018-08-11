package com.structurizr.documentation;

import com.structurizr.Workspace;
import com.structurizr.model.SoftwareSystem;

import java.io.File;
import java.io.FilenameFilter;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Imports architecture decision records created/managed by adr-tools (https://github.com/npryce/adr-tools).
 */
public class AdrToolsImporter {

    private static final Pattern titleRegex = Pattern.compile("^# \\d*\\. (.*)$", Pattern.MULTILINE);
    private static final Pattern dateRegex = Pattern.compile("^Date: (\\d\\d\\d\\d-\\d\\d-\\d\\d)$",Pattern.MULTILINE);
    private static final Pattern statusRegex = Pattern.compile("## Status[\\n\\r][\\n\\r](\\w*)");

    private static final String SUPERCEDED_ALTERNATIVE_SPELLING = "Superceded";

    private Workspace workspace;
    private File path;
    private String timeZone = "UTC";

    public AdrToolsImporter(Workspace workspace, File path) {
        if (workspace == null) {
            throw new IllegalArgumentException("A workspace must be specified");
        }

        if (path == null) {
            throw new IllegalArgumentException("The path to the architecture decision records must be specified");
        }

        if (!path.exists()) {
            throw new IllegalArgumentException(path.getAbsolutePath() + " does not exist");
        }

        this.workspace = workspace;
        this.path = path;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public Set<Decision> importArchitectureDecisionRecords() throws Exception {
        return importArchitectureDecisionRecords(null);
    }

    public Set<Decision> importArchitectureDecisionRecords(SoftwareSystem softwareSystem) throws Exception {
        Set<Decision> decisions = new HashSet<>();

        File[] markdownFiles = path.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".md");
            }
        });

        if (markdownFiles != null) {
            // first create an index of filename -> ID
            Map<String,String> index = new HashMap<>();
            for (File file : markdownFiles) {
                index.put(file.getName(), extractIntegerIdFromFileName(file));
            }

            for (File file : markdownFiles) {
                String id = extractIntegerIdFromFileName(file);
                Date date = new Date();
                String title = "";
                DecisionStatus status = DecisionStatus.Proposed;
                String content = new String(Files.readAllBytes(file.toPath()), "UTF-8");
                Format format = Format.Markdown;

                title = extractTitle(content);
                date = extractDate(content);
                status = extractStatus(content);

                for (String filename : index.keySet()) {
                    content = content.replace(filename, calculateUrl(softwareSystem, index.get(filename)));
                }

                Decision decision = workspace.getDocumentation().addDecision(softwareSystem, id, date, title, status, format, content);
                decisions.add(decision);
            }
        }

        return decisions;
    }

    private String calculateUrl(SoftwareSystem softwareSystem, String id) throws Exception {
        if (softwareSystem == null) {
            return "#/:" + urlEncode(id);
        } else {
            return "#" + urlEncode(softwareSystem.getCanonicalName()) + ":" + urlEncode(id);
        }
    }

    private String urlEncode(String value) throws Exception {
        return URLEncoder.encode(value, StandardCharsets.UTF_8.toString()).replaceAll("\\+", "%20");
    }

    private String extractIntegerIdFromFileName(File file) {
        return "" + Integer.parseInt(file.getName().substring(0, 4));
    }

    private String extractTitle(String content) {
        Matcher matcher = titleRegex.matcher(content);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return "Untitled";
        }
    }

    private Date extractDate(String content) throws Exception {
        Matcher matcher = dateRegex.matcher(content);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(TimeZone.getTimeZone(timeZone));

        if (matcher.find()) {
            return sdf.parse(matcher.group(1));
        } else {
            return new Date();
        }
    }

    private DecisionStatus extractStatus(String content) {
        Matcher matcher = statusRegex.matcher(content);
        if (matcher.find()) {
            String status = matcher.group(1);

            if (status.equals(SUPERCEDED_ALTERNATIVE_SPELLING)) {
                return DecisionStatus.Superseded;
            } else {
                return DecisionStatus.valueOf(status);
            }
        } else {
            return DecisionStatus.Proposed;
        }
    }

}
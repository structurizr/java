package com.structurizr.importer.documentation;

import com.structurizr.documentation.Decision;
import com.structurizr.documentation.Documentable;
import com.structurizr.documentation.Format;
import com.structurizr.util.StringUtils;

import java.io.File;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Imports architecture decision records created/managed by adr-tools (https://github.com/npryce/adr-tools).
 * The format for ADRs is as follows:
 *
 * Filename: {DECISION_ID:0000}-*.md
 *
 * Content:
 * # {DECISION_ID}. {DECISION_TITLE}
 *
 * Date: {DECISION_DATE:YYYY-MM-DD}
 *
 * ## Status
 *
 * {DECISION_STATUS and links}
 *
 * ## Context
 * ...
 */
public abstract class AbstractDecisionImporter implements DocumentationImporter {

    protected TimeZone timeZone = TimeZone.getDefault();
    protected Charset characterEncoding = StandardCharsets.UTF_8;

    /**
     * Sets the time zone to use when parsing dates (the default is UTC)
     *
     * @param timeZone      a time zone as a String (e.g. "Europe/London" or "UTC")
     */
    public void setTimeZone(String timeZone) {
        this.timeZone = TimeZone.getTimeZone(timeZone);
    }

    /**
     * Sets the time zone to use when parsing dates.
     *
     * @param timeZone      a TimeZone instance
     */
    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }

    /**
     * Provides a way to change the character encoding used by the DSL parser.
     *
     * @param characterEncoding     a Charset instance
     */
    public void setCharacterEncoding(Charset characterEncoding) {
        if (characterEncoding == null) {
            throw new IllegalArgumentException("A character encoding must be specified");
        }

        this.characterEncoding = characterEncoding;
    }

}
package com.structurizr.documentation;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.structurizr.model.Element;

import java.util.Date;

/**
 * Represents a single (architecture) decision, as described at http://thinkrelevance.com/blog/2011/11/15/documenting-architecture-decisions
 */
public final class Decision {

    private Element element;
    private String elementId;
    private String id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date date;
    private String title;
    private DecisionStatus status;
    private String content;
    private Format format;

    Decision() {
    }

    Decision(Element element, String id, Date date, String title, DecisionStatus status, Format format, String content) {
        this.element = element;
        this.id = id;
        this.date = date;
        this.title = title;
        this.status = status;
        this.format = format;
        this.content = content;
    }

    @JsonIgnore
    public Element getElement() {
        return element;
    }

    void setElement(Element element) {
        this.element = element;
    }

    public String getElementId() {
        if (this.element != null) {
            return this.element.getId();
        } else {
            return elementId;
        }
    }

    void setElementId(String elementId) {
        this.elementId = elementId;
    }

    public String getId() {
        return id;
    }

    void setId(String id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    void setDate(Date date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    void setTitle(String title) {
        this.title = title;
    }

    public DecisionStatus getStatus() {
        return status;
    }

    void setStatus(DecisionStatus status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    void setContent(String content) {
        this.content = content;
    }

    public Format getFormat() {
        return format;
    }

    void setFormat(Format format) {
        this.format = format;
    }

}
package com.structurizr;

import com.structurizr.model.Perspective;
import com.structurizr.util.StringUtils;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public interface PerspectivesHolder {

    /**
     * Gets the set of perspectives associated with this object.
     *
     * @return  a Set of Perspective objects (empty if there are none)
     */
    Set<Perspective> getPerspectives();

    /**
     * Adds a perspective to this object.
     *
     * @param name          the name of the perspective (e.g. "Security", must be unique)
     * @param description   the description of the perspective
     * @return              a Perspective object
     * @throws IllegalArgumentException     if perspective details are not specified, or the named perspective exists already
     */
    Perspective addPerspective(String name, String description);

    /**
     * Adds a perspective to this object.
     *
     * @param name          the name of the perspective (e.g. "Technical Debt", must be unique)
     * @param description   the description of the perspective (e.g. "High")
     * @param value         the value of the perspective
     * @return              a Perspective object
     * @throws IllegalArgumentException     if perspective details are not specified, or the named perspective exists already
     */
    Perspective addPerspective(String name, String description, String value);

}
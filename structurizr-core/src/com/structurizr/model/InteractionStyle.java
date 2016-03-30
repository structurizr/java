package com.structurizr.model;

import com.structurizr.view.RelationshipStyle;

/**
 * Use {@link InteractionStyle}s on {@link Relationship}s to make the difference between synchronous and asynchronous communication
 * visible. Use {@link com.structurizr.view.Styles#add(RelationshipStyle)} and pass either {@link Tags#SYNCHRONOUS} or {@link Tags#ASYNCHRONOUS}
 * to define different styles for synchronous and asynchronous communication.
 *
 * @see com.structurizr.view.Styles#addRelationshipStyle(String)
 * @see Tags#SYNCHRONOUS
 * @see Tags#ASYNCHRONOUS
 */
public enum InteractionStyle {

    /**
     * Denotes synchronous communication. The tag {@link Tags#SYNCHRONOUS} is automatically added to such {@link Relationship}s,
     * so you might use that tag to adapt the relationship style in the diagram
     *
     * @see Tags#SYNCHRONOUS
     */
    Synchronous,

    /**
     * Denotes asynchronous communication. The tag {@link Tags#ASYNCHRONOUS} is automatically added to such {@link Relationship}s,
     * so you might use that tag to adapt the relationship style in the diagram
     *
     * @see Tags#ASYNCHRONOUS
     */
    Asynchronous

}
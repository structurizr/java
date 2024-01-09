package com.structurizr.view;

/**
 * A wrapper for automatic layout configuration.
 */
public final class AutomaticLayout {

    private Implementation implementation;
    private RankDirection rankDirection;
    private int rankSeparation;
    private int nodeSeparation;
    private int edgeSeparation;
    private boolean vertices;
    private boolean applied;

    AutomaticLayout() {
    }

    AutomaticLayout(Implementation implementation, RankDirection rankDirection, int rankSeparation, int nodeSeparation, int edgeSeparation, boolean vertices) {
        setImplementation(implementation);
        setRankDirection(rankDirection);
        setRankSeparation(rankSeparation);
        setNodeSeparation(nodeSeparation);
        setEdgeSeparation(edgeSeparation);
        setVertices(vertices);
        setApplied(false);
    }

    /**
     * Gets the name of the implementation to use.
     *
     * @return  an enum representing Graphviz or Dagre
     */
    public Implementation getImplementation() {
        return implementation;
    }

    void setImplementation(Implementation implementation) {
        this.implementation = implementation;
    }

    /**
     * Gets the rank direction.
     *
     * @return  a RankDirection enum
     */
    public RankDirection getRankDirection() {
        return rankDirection;
    }

    void setRankDirection(RankDirection rankDirection) {
        if (rankDirection == null) {
            throw new IllegalArgumentException("A rank direction must be specified.");
        }

        this.rankDirection = rankDirection;
    }

    /**
     * Gets the rank separation (in pixels).
     *
     * @return      a positive integer
     */
    public int getRankSeparation() {
        return rankSeparation;
    }

    void setRankSeparation(int rankSeparation) {
        if (rankSeparation < 0) {
            throw new IllegalArgumentException("The rank separation must be a positive integer.");
        }

        this.rankSeparation = rankSeparation;
    }

    /**
     * Gets the node separation (in pixels).
     *
     * @return      a positive integer
     */
    public int getNodeSeparation() {
        return nodeSeparation;
    }

    void setNodeSeparation(int nodeSeparation) {
        if (nodeSeparation < 0) {
            throw new IllegalArgumentException("The node separation must be a positive integer.");
        }

        this.nodeSeparation = nodeSeparation;
    }

    /**
     * Gets the edge separation (in pixels).
     *
     * @return      a positive integer
     */
    public int getEdgeSeparation() {
        return edgeSeparation;
    }

    void setEdgeSeparation(int edgeSeparation) {
        if (edgeSeparation < 0) {
            throw new IllegalArgumentException("The edge separation must be a positive integer.");
        }

        this.edgeSeparation = edgeSeparation;
    }

    /**
     * Gets whether the automatic layout algorithm should create vertices.
     *
     * @return      a boolean
     */
    public boolean isVertices() {
        return vertices;
    }

    void setVertices(boolean vertices) {
        this.vertices = vertices;
    }

    /**
     * Returns whether automatic layout has been applied.
     *
     * @return  true if automatic layout has been applied, false otherwise
     */
    public boolean isApplied() {
        return applied;
    }

    /**
     * Sets whether automatic layout has been applied.
     *
     * @param applied   true if automatic layout has been applied, false otherwise
     */
    public void setApplied(boolean applied) {
        this.applied = applied;
    }

    public enum Implementation {
        Graphviz,
        Dagre
    }

    public enum RankDirection {
        TopBottom,
        BottomTop,
        LeftRight,
        RightLeft
    }

}
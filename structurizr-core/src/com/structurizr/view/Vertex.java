package com.structurizr.view;

/**
 * The X, Y coordinate of a bend in a line.
 */
public final class Vertex {

    private int x;
    private int y;

    Vertex() {
    }

    public Vertex(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Gets the horizontal position of the vertex when rendered.
     *
     * @return  the X coordinate, as an int
     */
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    /**
     * Gets the vertical position of the vertex when rendered.
     *
     * @return  the Y coordinate, as an int
     */
    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

}

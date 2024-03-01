package com.structurizr.view;

import java.util.Objects;

/**
 * The X, Y coordinate of a bend in a line.
 */
public final class Vertex implements Comparable<Vertex> {

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Vertex vertex = (Vertex)o;
        return x == vertex.x && y == vertex.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public int compareTo(Vertex vertex) {
        int result = getX() - vertex.getX();
        if (result == 0) {
            result = getY() - vertex.getY();
        }

        return result;
    }

}
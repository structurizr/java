package com.structurizr.view;

public final class Dimensions {

    private int width;
    private int height;

    Dimensions() {
    }

    public Dimensions(int width, int height) {
        setWidth(width);
        setHeight(height);
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        if (width < 0) {
            throw new IllegalArgumentException("The width must be a positive integer.");
        }

        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        if (height < 0) {
            throw new IllegalArgumentException("The height must be a positive integer.");
        }

        this.height = height;
    }

}
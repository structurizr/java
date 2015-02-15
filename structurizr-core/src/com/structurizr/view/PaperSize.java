package com.structurizr.view;

/**
 * These represent paper sizes in pixels at 300dpi.
 */
public enum PaperSize {

    A4_Portrait("A4", Orientation.Portrait, 2480, 3510),
    A4_Landscape("A4", Orientation.Landscape, 3510, 2480),

    A3_Portrait("A3", Orientation.Portrait, 3510, 4950),
    A3_Landscape("A3", Orientation.Landscape, 4950, 3510),

    Letter_Portrait("Letter", Orientation.Portrait, 2550, 3300),
    Letter_Landscape("Letter", Orientation.Landscape, 3300, 2550),

    Slide_4_3("Slide 4:3", Orientation.Landscape, 3306, 2480),
    Slide_16_9("Slide 16:9", Orientation.Landscape, 3510, 1974);

    private String name;
    private Orientation orientation;
    private int width;
    private int height;

    private PaperSize(String name, Orientation orientation, int width, int height) {
        this.name = name;
        this.orientation = orientation;
        this.width = width;
        this.height = height;
    }

    public String getName() {
        return name;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    enum Orientation {
        Portrait,
        Landscape
    }

}

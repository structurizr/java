package com.structurizr.view;

/**
 * These represent paper sizes in pixels at 300dpi.
 */
public enum PaperSize {

    A4_Portrait("A4", Orientation.Portrait, 2480, 3508),
    A4_Landscape("A4", Orientation.Landscape, 3508, 2480),

    A3_Portrait("A3", Orientation.Portrait, 3508, 4961),
    A3_Landscape("A3", Orientation.Landscape, 4961, 3508),

    A2_Portrait("A2", Orientation.Portrait, 4961, 7016),
    A2_Landscape("A2", Orientation.Landscape, 7016, 4961),

    Letter_Portrait("Letter", Orientation.Portrait, 2550, 3300),
    Letter_Landscape("Letter", Orientation.Landscape, 3300, 2550),

    Slide_4_3("Slide 4:3", Orientation.Landscape, 3306, 2480),
    Slide_16_9("Slide 16:9", Orientation.Landscape, 3508, 1973);

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

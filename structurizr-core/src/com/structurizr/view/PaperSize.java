package com.structurizr.view;

import java.util.ArrayList;
import java.util.List;

/**
 * These represent paper sizes in pixels at 300dpi.
 */
public enum PaperSize {

    A6_Portrait("A6", Orientation.Portrait, 1240, 1748),
    A6_Landscape("A6", Orientation.Landscape, 1748, 1240),

    A5_Portrait("A5", Orientation.Portrait, 1748, 2480),
    A5_Landscape("A5", Orientation.Landscape, 2480, 1748),

    A4_Portrait("A4", Orientation.Portrait, 2480, 3508),
    A4_Landscape("A4", Orientation.Landscape, 3508, 2480),

    A3_Portrait("A3", Orientation.Portrait, 3508, 4961),
    A3_Landscape("A3", Orientation.Landscape, 4961, 3508),

    A2_Portrait("A2", Orientation.Portrait, 4961, 7016),
    A2_Landscape("A2", Orientation.Landscape, 7016, 4961),

    A1_Portrait("A1", Orientation.Portrait, 7016, 9933),
    A1_Landscape("A1", Orientation.Landscape, 9933, 7016),

    A0_Portrait("A0", Orientation.Portrait, 9933, 14043),
    A0_Landscape("A0", Orientation.Landscape, 14043, 9933),

    Letter_Portrait("Letter", Orientation.Portrait, 2550, 3300),
    Letter_Landscape("Letter", Orientation.Landscape, 3300, 2550),

    Legal_Portrait("Legal", Orientation.Portrait, 2550, 4200),
    Legal_Landscape("Legal", Orientation.Landscape, 4200, 2550),

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

    public static final List<PaperSize> getOrderedPaperSizes() {
        List<PaperSize> paperSizes = new ArrayList<>();

        for (PaperSize paperSize : values()) {
            if (paperSize.getOrientation() == Orientation.Landscape) {
                paperSizes.add(paperSize);
            }
        }

        for (PaperSize paperSize : values()) {
            if (paperSize.getOrientation() == Orientation.Portrait) {
                paperSizes.add(paperSize);
            }
        }

        return paperSizes;
    }

}

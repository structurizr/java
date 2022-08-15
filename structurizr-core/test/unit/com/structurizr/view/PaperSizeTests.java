package com.structurizr.view;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PaperSizeTests {

    @Test
    void getOrderedPaperSizes_WhenOrientationIsLandscape() {
        List<PaperSize> paperSizes = PaperSize.getOrderedPaperSizes(PaperSize.Orientation.Landscape);
        assertEquals(12, paperSizes.size());

        assertEquals(PaperSize.A6_Landscape, paperSizes.get(0));
        assertEquals(PaperSize.A5_Landscape, paperSizes.get(1));
        assertEquals(PaperSize.A4_Landscape, paperSizes.get(2));
        assertEquals(PaperSize.A3_Landscape, paperSizes.get(3));
        assertEquals(PaperSize.A2_Landscape, paperSizes.get(4));
        assertEquals(PaperSize.A1_Landscape, paperSizes.get(5));
        assertEquals(PaperSize.A0_Landscape, paperSizes.get(6));

        assertEquals(PaperSize.Letter_Landscape, paperSizes.get(7));
        assertEquals(PaperSize.Legal_Landscape, paperSizes.get(8));
        assertEquals(PaperSize.Slide_4_3, paperSizes.get(9));
        assertEquals(PaperSize.Slide_16_9, paperSizes.get(10));
        assertEquals(PaperSize.Slide_16_10, paperSizes.get(11));
    }

    @Test
    void getOrderedPaperSizes_WhenOrientationIsPortrait() {
        List<PaperSize> paperSizes = PaperSize.getOrderedPaperSizes(PaperSize.Orientation.Portrait);
        assertEquals(9, paperSizes.size());

        assertEquals(PaperSize.A6_Portrait, paperSizes.get(0));
        assertEquals(PaperSize.A5_Portrait, paperSizes.get(1));
        assertEquals(PaperSize.A4_Portrait, paperSizes.get(2));
        assertEquals(PaperSize.A3_Portrait, paperSizes.get(3));
        assertEquals(PaperSize.A2_Portrait, paperSizes.get(4));
        assertEquals(PaperSize.A1_Portrait, paperSizes.get(5));
        assertEquals(PaperSize.A0_Portrait, paperSizes.get(6));

        assertEquals(PaperSize.Letter_Portrait, paperSizes.get(7));
        assertEquals(PaperSize.Legal_Portrait, paperSizes.get(8));
    }

    @Test
    void getOrderedPaperSizes() {
        List<PaperSize> paperSizes = PaperSize.getOrderedPaperSizes();
        assertEquals(21, paperSizes.size());

        assertEquals(PaperSize.A6_Landscape, paperSizes.get(0));
        assertEquals(PaperSize.A5_Landscape, paperSizes.get(1));
        assertEquals(PaperSize.A4_Landscape, paperSizes.get(2));
        assertEquals(PaperSize.A3_Landscape, paperSizes.get(3));
        assertEquals(PaperSize.A2_Landscape, paperSizes.get(4));
        assertEquals(PaperSize.A1_Landscape, paperSizes.get(5));
        assertEquals(PaperSize.A0_Landscape, paperSizes.get(6));

        assertEquals(PaperSize.Letter_Landscape, paperSizes.get(7));
        assertEquals(PaperSize.Legal_Landscape, paperSizes.get(8));
        assertEquals(PaperSize.Slide_4_3, paperSizes.get(9));
        assertEquals(PaperSize.Slide_16_9, paperSizes.get(10));
        assertEquals(PaperSize.Slide_16_10, paperSizes.get(11));

        assertEquals(PaperSize.A6_Portrait, paperSizes.get(12));
        assertEquals(PaperSize.A5_Portrait, paperSizes.get(13));
        assertEquals(PaperSize.A4_Portrait, paperSizes.get(14));
        assertEquals(PaperSize.A3_Portrait, paperSizes.get(15));
        assertEquals(PaperSize.A2_Portrait, paperSizes.get(16));
        assertEquals(PaperSize.A1_Portrait, paperSizes.get(17));
        assertEquals(PaperSize.A0_Portrait, paperSizes.get(18));

        assertEquals(PaperSize.Letter_Portrait, paperSizes.get(19));
        assertEquals(PaperSize.Legal_Portrait, paperSizes.get(20));
    }

}
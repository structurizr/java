package com.structurizr.testapp.paperboy.model;

import java.time.LocalDate;

public class Paper {
    private final LocalDate date;
    private final MonetaryAmount unitPriceOfPaper;

    private Paper(LocalDate date, MonetaryAmount unitPriceOfPaper) {
        this.date = date;
        this.unitPriceOfPaper = unitPriceOfPaper;
    }

    public static Paper create(LocalDate date, MonetaryAmount unitPriceOfPaper) {
        return new Paper(date, unitPriceOfPaper);
    }

    public MonetaryAmount getUnitPriceOfPaper() {
        return unitPriceOfPaper;
    }

    public LocalDate getDateTime() {
        return date;
    }

}

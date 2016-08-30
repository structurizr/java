package com.structurizr.testapp.paperboy.model;

import com.google.common.collect.ImmutableList;
import com.structurizr.testapp.paperboy.service.PaperPriceService;

import java.time.LocalDate;
import java.util.List;

public class PaperFactory {

    private final PaperPriceService priceService;

    public PaperFactory(PaperPriceService priceService) {
        this.priceService = priceService;
    }

    public List<Paper> printPapers(int nr, LocalDate date) {
        final ImmutableList.Builder<Paper> builder = ImmutableList.builder();
        for (int i = 0; i < nr; i++) {
            builder.add(createPaper(date));
        }
        return builder.build();
    }

    private Paper createPaper(LocalDate date) {
        final MonetaryAmount paperPrice = priceService.getPaperPrice(date);
        return Paper.create(date, paperPrice);
    }


}

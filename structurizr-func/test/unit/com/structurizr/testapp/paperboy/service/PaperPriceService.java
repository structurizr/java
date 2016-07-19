package com.structurizr.testapp.paperboy.service;

import com.structurizr.testapp.paperboy.model.MonetaryAmount;

import java.time.LocalDate;

public interface PaperPriceService {

    MonetaryAmount getPaperPrice(LocalDate date);
}

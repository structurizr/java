package com.structurizr.testapp.paperboy.service;

public interface DeliveryStrategy {
    void deliverPapers(DeliverPapersCommand command);
}

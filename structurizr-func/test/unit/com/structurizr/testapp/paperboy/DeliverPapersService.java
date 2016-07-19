package com.structurizr.testapp.paperboy;

import com.structurizr.testapp.paperboy.model.PaperBoyRepository;
import com.structurizr.testapp.paperboy.service.DeliverPapersCommand;
import com.structurizr.testapp.paperboy.service.DeliveryStrategy;

import java.util.Set;

class DeliverPapersService implements PaperDeliveryApplicationService {
    private final DeliveryStrategy deliveryStrategy;
    private final PaperBoyRepository paperBoyRepository;

    DeliverPapersService(
            DeliveryStrategy deliveryStrategy,
            PaperBoyRepository paperBoyRepository) {
        this.deliveryStrategy = deliveryStrategy;
        this.paperBoyRepository = paperBoyRepository;
    }

    @Override
    public void deliverPapers(Set<String> streets) {
        final DeliverPapersCommand c = buildCommand(streets);
        deliveryStrategy.deliverPapers(c);
    }

    private DeliverPapersCommand buildCommand(Set<String> streets) {
        return DeliverPapersCommand.builder()
                .withStreets(streets)
                .withPaperBoys(paperBoyRepository.getAll())
                .build();
    }


}

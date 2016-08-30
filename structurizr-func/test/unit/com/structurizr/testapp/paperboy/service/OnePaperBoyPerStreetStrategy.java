package com.structurizr.testapp.paperboy.service;

import com.google.common.collect.Iterables;
import com.structurizr.testapp.paperboy.model.*;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

class OnePaperBoyPerStreetStrategy implements DeliveryStrategy {
    private final PaperFactory paperFactory;
    private final CustomerRepository customerRepository;

    OnePaperBoyPerStreetStrategy(PaperFactory paperFactory,
                                 CustomerRepository customerRepository) {
        this.paperFactory = paperFactory;
        this.customerRepository = customerRepository;
    }

    @Override
    public void deliverPapers(DeliverPapersCommand command) {
        final OnePaperBoyPerStreetConsumer consumer = new OnePaperBoyPerStreetConsumer(command.getPaperBoys());
        command.getStreets()
                .stream()
                .map(customerRepository::getAll)
                .forEach(consumer);
    }


    private class OnePaperBoyPerStreetConsumer implements Consumer<Set<Customer>> {
        private final Iterator<PaperBoy> iterator;

        private OnePaperBoyPerStreetConsumer(Set<PaperBoy> paperBoys) {
            this.iterator = Iterables.cycle(paperBoys).iterator();
        }

        @Override
        public void accept(Set<Customer> customers) {
            final PaperBoy paperBoy = iterator.next();
            deliverPapers(paperBoy, customers);
        }

        private void deliverPapers(PaperBoy paperBoy, Set<Customer> customers) {
            final List<Paper> papers = paperFactory.printPapers(customers.size(), LocalDate.now());
            paperBoy.loadPapers(papers);
            customers.stream()
                    .forEach(x -> x.buyPaper(paperBoy));
        }
    }
}

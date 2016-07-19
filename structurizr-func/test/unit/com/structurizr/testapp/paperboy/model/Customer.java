package com.structurizr.testapp.paperboy.model;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

public class Customer {

    private final Wallet wallet;
    private final Address address;
    private Paper paper;

    Customer(Address address, Wallet wallet) {
        checkNotNull(address);
        checkNotNull(wallet);
        this.address = address;
        this.wallet = wallet;
    }

    public void buyPaper(PaperBoy paperBoy) {
        final MonetaryAmount price = paperBoy.getPaperPrice();
        if (paper == null) {
            final Optional<MonetaryAmount> money = wallet.takeMoney(price.getNumber());
            if (money.isPresent())
                buyPaper(paperBoy, money.get());
        }
    }

    public boolean livesHere(Address address) {
        return this.address.equals(address);
    }

    public boolean hasMoney(MonetaryAmount customerMoney) {
        return wallet.containsAmount(customerMoney);
    }

    public boolean hasPaper() {
        return paper != null;
    }

    public MonetaryAmount getAmountOfMoney() {
        return wallet.getAmountOfMoney();
    }

    public Address getAddress() {
        return address;
    }

    private void buyPaper(PaperBoy paperBoy, MonetaryAmount money) {
        final Optional<Paper> paper = paperBoy.sellPaper(money);
        if (paper.isPresent())
            this.setPaper(paper.get());
        else
            wallet.add(money);
    }

    private void setPaper(Paper x) {
        this.paper = x;
    }

}

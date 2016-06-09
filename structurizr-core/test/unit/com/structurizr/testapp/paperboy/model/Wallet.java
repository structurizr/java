package com.structurizr.testapp.paperboy.model;

import java.util.Optional;

public class Wallet {
    private MonetaryAmount money;

    Wallet(Number amount, CurrencyUnit currency) {
        this.money = new MonetaryAmount();
    }

    public Optional<MonetaryAmount> takeMoney(Number amount) {
        return Optional.empty();
    }


    public boolean containsAmount(MonetaryAmount customerMoney) {
        return true;
    }

    public void add(MonetaryAmount toAdd) {
    }

    public MonetaryAmount getAmountOfMoney() {
        return money;
    }
}

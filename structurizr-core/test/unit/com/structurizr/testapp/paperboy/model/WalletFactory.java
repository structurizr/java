package com.structurizr.testapp.paperboy.model;


public class WalletFactory {

    private final CurrencyUnit currencyCode;

    public WalletFactory(String currencyCode) {
        this.currencyCode = CurrencyUnit.getCurrency(currencyCode);
    }


    public Wallet createWallet() {
        return createWallet(0);
    }

    public Wallet createWallet(int amount) {
        return new Wallet(amount, currencyCode);
    }

}

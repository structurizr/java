package com.structurizr.testapp.paperboy.model;

import java.util.List;
import java.util.Optional;
import java.util.Stack;

public class PaperBoy {
    private final Wallet wallet;
    private final Stack<Paper> papers;

    public PaperBoy(Wallet wallet) {
        this.wallet = wallet;
        this.papers = new Stack<>();
    }

    public void loadPapers(List<Paper> papers) {
        this.papers.addAll(papers);
    }

    public Optional<Paper> sellPaper(MonetaryAmount money) {
        if (hasSufficientMoney(money) && stillHasPapersToSell())
            return makeTransfer(money);
        else
            return Optional.empty();
    }

    public MonetaryAmount getPaperPrice() {
        if (papers.isEmpty())
            throw new RuntimeException(this + " is out of papers!");
        else
            return papers.peek().getUnitPriceOfPaper();
    }

    public MonetaryAmount getAmountOfMoney() {
        return wallet.getAmountOfMoney();
    }

    public int getNrOfPapers() {
        return papers.size();
    }

    private boolean stillHasPapersToSell() {
        return !papers.isEmpty();
    }

    private Optional<Paper> makeTransfer(MonetaryAmount money) {
        wallet.add(money);
        return Optional.of(papers.pop());
    }

    private boolean hasSufficientMoney(MonetaryAmount money) {
        final MonetaryAmount priceOfPaper = getPaperPrice();
        if (priceOfPaper.isGreaterThan(money)) {
            return false;
        }
        return true;

    }



}

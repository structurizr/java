package com.structurizr.testapp.paperboy.model;

import static com.google.common.base.Preconditions.checkNotNull;

public class Address {
    private final String street;
    private final String houseNr;

    private Address(String street, String houseNr) {
        checkNotNull(street);
        checkNotNull(houseNr);
        this.street = street;
        this.houseNr = houseNr;
    }

    public String getStreet() {
        return street;
    }

    public String getHouseNr() {
        return houseNr;
    }

    static Address createAddress(String street, String houseNr) {
        return new Address(street, houseNr);
    }


}

package com.backenddiploma.models.enums;

public enum Currency {
    UAH(980),
    USD(840),
    EUR(978),
    PLN(985);

    private final int isoCode;

    Currency(int isoCode) {
        this.isoCode = isoCode;
    }

    public int getIsoCode() {
        return isoCode;
    }
}

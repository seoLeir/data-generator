package org.bitpioneers.types;

public enum ServiceType {
    DEPOSIT,
    LOAN,
    POST,
    FINE,
    INSURANCE;

    public static ServiceType getRandom() {
        return values()[(int) (Math.random() * values().length)];
    }
}

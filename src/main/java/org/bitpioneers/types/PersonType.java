package org.bitpioneers.types;

public enum PersonType {
    PHYSICAL(0),
    JURIDICAL(1);
    private Integer value;

    PersonType(int value) {
        this.value = value;
    }

    public Integer getValue(){
        return value;
    }
}

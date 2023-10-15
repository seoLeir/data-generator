package org.bitpioneers.types;

/**
* The PersonType enum represents a distinct set of values that categorize individuals or entities into two main types:
 * physical persons and juridical entities. This enumeration is designed to provide a clear and efficient means of
 * distinguishing between these two categories within the context of a larger software system.
* @since 1.0
 * @author Mirolim Mirzayev
*/
public enum PersonType {
    /**
    * Represents the enum constant for physical persons.
    */
    PHYSICAL(0),

    /**
    * Represents the enum constant for juridical entities.
    */
    JURIDICAL(1);

    /**
    * An instance variable that associates an integer value with each enum constant,
    * allowing for differentiation between the two types.
    */
    private final Integer value;

    PersonType(int value) {
        this.value = value;
    }

    /**
    *  A public method that returns the associated integer value for a given enum constant.
     * @return integer for enum constant
    */
    public Integer getValue(){
        return value;
    }
}

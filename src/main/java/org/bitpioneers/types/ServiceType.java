package org.bitpioneers.types;

/**
* The ServiceType enum represents a finite set of service categories that can be offered by an application or
* system. It includes services such as deposit, loan, post, fine, and insurance. This enumeration is designed to
* provide a structured and versatile means of categorizing services and selecting random service types.
 * @since 1.0
* @author Mirolim Mirzayev
*/
public enum ServiceType {
    /**
    * Represents the service category for deposits.
    */
    DEPOSIT,

    /**
    *  Represents the service category for loans.
    */
    LOAN,

    /**
    * Represents the service category for postal services.
    */
    POST,

    /**
    * Represents the service category for fines.
    */
    FINE,

    /**
    * Represents the service category for insurance services.
    */
    INSURANCE;

    /**
    * A public method that returns a random ServiceType by selecting a random enum constant from the available values
    */
    public static ServiceType getRandom() {
        return values()[(int) (Math.random() * values().length)];
    }
}

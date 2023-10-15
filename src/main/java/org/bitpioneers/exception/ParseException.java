package org.bitpioneers.exception;

/**
* The ParseException class is a custom exception class that extends RuntimeException. It is designed to handle
 * and indicate exceptions related to parsing operations within a software application. This documentation
 * provides an in-depth explanation of the class's purpose, constructor, and usage
 *
 * @since 1.0
 * @author Mirolim Mirzayev
*/
public class ParseException extends RuntimeException{
    public ParseException(String message) {
        super(message);
    }
}

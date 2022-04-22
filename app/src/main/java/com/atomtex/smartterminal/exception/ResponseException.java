package com.atomtex.smartterminal.exception;

/**
 * This class is a super class for any exception that can be thrown in case if the message
 * from device is not valid
 *
 * @author stanislav.kleinikov@gmail.com
 */
public abstract class ResponseException extends Exception {

    ResponseException() {
        super();
    }

    ResponseException(String message) {
        super(message);
    }
}

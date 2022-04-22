package com.atomtex.smartterminal.exception;

/**
 * This exception can be thrown if after the device was asked the CRC code
 * of its response does not match
 *
 * @author stanislav.kleinikov@gmail.com
 */
public class IntegrityException extends ResponseException {

    private static final String message = "The response is not integrity";

    public IntegrityException() {
        super(message);
    }
}

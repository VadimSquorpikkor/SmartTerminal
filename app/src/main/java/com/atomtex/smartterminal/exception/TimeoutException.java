package com.atomtex.smartterminal.exception;

/**
 * This exception can be thrown if after the device was asked there was no any answer during the
 * certain time.
 *
 * @author stanislav.kleinikov@gmail.com
 */
public class TimeoutException extends ResponseException {

    private static final String message = "No response or timeout expired";

    public TimeoutException() {
        super(message);
    }
}

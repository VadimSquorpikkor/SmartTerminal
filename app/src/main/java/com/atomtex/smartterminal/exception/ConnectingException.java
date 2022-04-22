package com.atomtex.smartterminal.exception;

/**
 * This exception can be throws in case of {@link java.io.IOException} during the communication
 * with device
 *
 * @author stanislav.kleinikov@gmail.com
 */
public class ConnectingException extends Exception {

    public ConnectingException() {
        super();
    }

    public ConnectingException(String message) {
        super(message);
    }
}

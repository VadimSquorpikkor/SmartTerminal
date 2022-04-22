package com.atomtex.smartterminal.modbus;

import com.atomtex.smartterminal.exception.ConnectingException;
import com.atomtex.smartterminal.exception.ResponseException;

public abstract class AbstractAdapter {

    /**
     * This object perform the communication with adapter and application
     */
    private ModbusTransport mTransport;


    AbstractAdapter() {
    }

    AbstractAdapter(ModbusTransport transport) {
        mTransport = transport;
    }

    /**
     * Allows to establish a connection between an adapter and application.
     * <p>
     * In order to create a connection between two devices, classes that extends
     * this method must implement the server-side or client-side mechanisms
     * because one device must open a server socket, and the other one must
     * initiate the connection using the server device's MAC address.
     * </p>
     *
     * @return whether the connection is successful
     */
    public abstract boolean connect();

    /**
     * This method send message to a connected device, awaits for a response, check it
     * and than return it.
     *
     * @param message message to send to a device
     * @return response from device that have been received
     * @throws ConnectingException in case of loss of connection
     * @throws ResponseException   if the responses from the device are not valid
     */
    public abstract ModbusMessage sendMessageWithResponse(ModbusMessage message)
            throws ConnectingException, ResponseException;

    /**
     * Allows to send a message using the MODBUS protocol to remote device
     *
     * @param message the message to send to device
     * @throws ConnectingException in case of loss of connection
     */
    public abstract void sendMessage(ModbusMessage message) throws ConnectingException;

    /**
     * Receives a message from a connected device.Check the message and return it.
     *
     * @return message that have been received from connected device.
     * @throws ConnectingException in case of loss of connection
     * @throws ResponseException   if the responses from the device are not valid
     */
    public abstract ModbusMessage receiveMessage() throws ConnectingException, ResponseException;

    /**
     * Breaks the existing connection with an adapter.
     */
    public abstract void disconnect();


    //Getters and setters

    public ModbusTransport getTransport() {
        return mTransport;
    }

    public void setTransport(ModbusTransport transport) {
        this.mTransport = transport;
    }



}
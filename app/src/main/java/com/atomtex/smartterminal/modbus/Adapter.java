package com.atomtex.smartterminal.modbus;

import static com.atomtex.smartterminal.App.TAG;

import android.util.Log;

import com.atomtex.smartterminal.exception.ConnectingException;
import com.atomtex.smartterminal.exception.DeviceException;
import com.atomtex.smartterminal.exception.ResponseException;
import com.atomtex.smartterminal.util.CRC16;
import com.atomtex.smartterminal.util.Util;

import java.io.IOException;
import java.util.Arrays;

/**
 * This class was designed to set a connection with any devices which are the server devices and
 * waiting for a connection.
 * <p>
 * Allows to set a connection, send request using the MODBUS protocol and receive a message from it
 * and check for exceptions or integrity
 *
 * @author stanislav.kleinikov@gmail.com
 */
public class Adapter extends com.atomtex.smartterminal.modbus.AbstractAdapter {

    /**
     * The message that has been sent.This value is needed to compare request with response to
     * detect {@link DeviceException}.
     */
    private ModbusMessage requestMessage;

    public Adapter() {
    }

    public Adapter(ModbusTransport transport) {
        super(transport);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean connect() {
        return getTransport().connect();
    }

    /**
     * {@inheritDoc}
     * <p>
     * The method is thread save.
     */
    @Override
    public synchronized ModbusMessage sendMessageWithResponse(ModbusMessage message)
            throws ConnectingException, ResponseException {
        //Log.e("TAG", ">>> "+ Arrays.toString(message.getBuffer()));
        requestMessage = message;
        sendMessage(message);
        return receiveMessage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendMessage(ModbusMessage message) throws ConnectingException {
        try {
            getTransport().sendMessage(message.getBuffer());
        } catch (IOException e) {
            throw new ConnectingException("Connection lost while executing command 0x"
                    + Util.getHexString(message.getBuffer()[1]));

        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ModbusMessage receiveMessage() throws ConnectingException, ResponseException {
        ModbusMessage responseMessage = new ModbusMessage(getTransport().receiveMessage());

        //Log.e("TAG", "<<< "+Arrays.toString(responseMessage.getBuffer()));

//        if (BuildConfig.DEBUG) {
//            Log.e(TAG, "\nRequest " + Arrays.toString(requestMessage.getBuffer()) + "\n"
//                    + "Response " + Arrays.toString(responseMessage.getBuffer()));
//        }
        if (responseMessage.getBuffer() == null) {
            throw new ConnectingException("Connection lost while executing command 0x"
                    + Util.getHexString(requestMessage.getBuffer()[1]));
        }
        if (ModbusMessage.checkDeviceException(requestMessage, responseMessage)) {
            responseMessage.setException(true);
        } else {
            responseMessage.setIntegrity(CRC16.checkCRC(responseMessage.getBuffer()));
        }
        ModbusMessage.checkMessage(responseMessage);
        return responseMessage;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void disconnect() {
        if (getTransport() != null) {
            getTransport().close();
        }
    }
}

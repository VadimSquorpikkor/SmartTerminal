package com.atomtex.smartterminal.modbus;

import static com.atomtex.smartterminal.App.TAG;
import static com.atomtex.smartterminal.util.Constant.CHANGE_STATE_CONTROL_REGISTER;
import static com.atomtex.smartterminal.util.Constant.CHANGE_STATE_CONTROL_REGISTERS;
import static com.atomtex.smartterminal.util.Constant.DIAGNOSTICS;
import static com.atomtex.smartterminal.util.Constant.READ_ACCUMULATED_SPECTRUM;
import static com.atomtex.smartterminal.util.Constant.READ_ACCUMULATED_SPECTRUM_COMPRESSED;
import static com.atomtex.smartterminal.util.Constant.READ_ACCUMULATED_SPECTRUM_COMPRESSED_REBOOT;
import static com.atomtex.smartterminal.util.Constant.READ_STATUS_WORD;
import static com.atomtex.smartterminal.util.Constant.SEND_CONTROL_SIGNAL;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.ParcelUuid;
import android.util.Log;

import com.atomtex.smartterminal.util.BitConverter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.UUID;

/**
 * The implementation of the {@link ModbusTransport}.
 * Implements methods for interaction with devices through the RFCOMM interface
 *
 * @author stanislav.kleinikov@gmail.com
 */
public class ModbusRFCOMMTransport implements ModbusTransport {

    /**
     * Waiting time for response from connected device
     */
    private static final int TIMEOUT_DEFAULT = 1500;
    /**
     * 5 bytes message length
     */
    private static final int MESSAGE_DEFAULT_LENGTH = 5;
    /**
     * 6 bytes message length
     */
    private static final int MESSAGE_MID_LENGTH = 6;
    /**
     * 8 bytes message length
     */
    private static final int MESSAGE_LONG_LENGTH = 8;

    /**
     * The buffer holds the response.
     */
    private final byte[] buffer;

    /**
     * Connected device
     */
    private BluetoothDevice device;
    /**
     * BluetoothSocket
     */
    private BluetoothSocket socket;
    /**
     * InputStream associated with {@link #socket}
     */
    private InputStream inputStream;
    /**
     * OutputStream associated with {@link #socket}
     */
    private OutputStream outputStream;

    private ModbusRFCOMMTransport() {
        buffer = new byte[3084];
    }

    /**
     * Nested class that holds a {@link ModbusRFCOMMTransport} instance.
     */
    private static class ModbusRFCOMMTransportHolder {
        private static final ModbusRFCOMMTransport INSTANCE = new ModbusRFCOMMTransport();
    }

    /**
     * Allows to get instance of this singleton
     *
     * @return {@link ModbusRFCOMMTransport) instance
     */
    static ModbusRFCOMMTransport getInstance(BluetoothDevice device) {
        ModbusRFCOMMTransport instance = ModbusRFCOMMTransportHolder.INSTANCE;
        instance.device = device;
        return instance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean connect() {
        try {
            BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
            ParcelUuid[] idArray = device.getUuids();
            UUID uuid = UUID.fromString(idArray[0].toString());
            //Log.e(TAG, "☻☻☻ connect: UUID = "+uuid);
            socket = device.createRfcommSocketToServiceRecord(uuid);
            if (!socket.isConnected()) socket.connect();
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void sendMessage(byte[] message) throws IOException {
        if (inputStream==null || outputStream==null) return;
        //skip all remaining bytes in the stream
        inputStream.skip(inputStream.available());
        outputStream.write(message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte[] receiveMessage() {

        byte[] buffer = this.buffer;
        int currentPosition = 0;
        long startTime = System.currentTimeMillis();
        int totalByte = MESSAGE_DEFAULT_LENGTH;

        //until time passes or we get all the bytes
        while ((System.currentTimeMillis() - startTime < TIMEOUT_DEFAULT) && currentPosition != totalByte) {

            try {
                if (inputStream.available() > 0) {
                    int x = inputStream.read();
                    buffer[currentPosition] = (byte) x;
                    currentPosition++;
                    //position 1 contains contains command byte
                    //We can figure out the number of bytes in message according to command byte
                    if (currentPosition == 3) {
                        if (buffer[1] == DIAGNOSTICS
                                || buffer[1] == SEND_CONTROL_SIGNAL
                                || buffer[1] == CHANGE_STATE_CONTROL_REGISTER
                                || buffer[1] == CHANGE_STATE_CONTROL_REGISTERS) {
                            totalByte = MESSAGE_LONG_LENGTH;
                        } else if (buffer[1] == READ_STATUS_WORD) {
                            totalByte = MESSAGE_DEFAULT_LENGTH;
                        } else {
                            totalByte = (buffer[2] & 255) + MESSAGE_DEFAULT_LENGTH;
                        }
                    } else if (currentPosition == 4 &&
                            (buffer[1] == READ_ACCUMULATED_SPECTRUM
                                    || buffer[1] == READ_ACCUMULATED_SPECTRUM_COMPRESSED_REBOOT
                                    || buffer[1] == READ_ACCUMULATED_SPECTRUM_COMPRESSED)) {
                        //in these command 3 and 4 bytes shows number of data bytes in message
                        int lengthData = BitConverter.toInt16(new byte[]{buffer[3], buffer[2]}, 0);
                        totalByte = lengthData + MESSAGE_MID_LENGTH;
                    }
                }
            } catch (Exception e) {
                return null;
            }
        }
        return Arrays.copyOf(buffer, currentPosition);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() {
        if (socket != null) {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
//                socket.close();
                if (socket!=null) socket.close();
            } catch (IOException e) {
                //todo
                Log.e(TAG, "close: CrashReporter.logException(e)");
            }
            socket = null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isConnected() {
        if (socket == null) {
            return false;
        } else return socket.isConnected();
    }

}

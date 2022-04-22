package com.atomtex.smartterminal.modbus;

import android.util.Log;

import com.atomtex.smartterminal.exception.DeviceException;
import com.atomtex.smartterminal.exception.IntegrityException;
import com.atomtex.smartterminal.exception.ResponseException;
import com.atomtex.smartterminal.exception.TimeoutException;
import com.atomtex.smartterminal.util.CRC16;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * The object to hold message and additional information about it.
 *
 * @author stanislav.kleinikov@gmail.com
 *
 * <p><img src="{@docRoot}/image/modbus_message.jpg" alt="alternative directory"></p>
 */
public class ModbusMessage {

    /**
     * The byte array to be send.
     */
    private byte[] mBuffer;

    /**
     * Whether the message is not valid message. The CRC value do not match.
     */
    private boolean mIntegrity;

    /**
     * Indicates whether the message contains an exception.
     */
    private boolean mException;

    public ModbusMessage() {

    }

    public ModbusMessage(byte[] buffer) {
        this.mBuffer = buffer;
        mIntegrity = true;
        mException = false;
    }

    /**
     * Creates new ModbusMessage that is ready to be send.
     * <p>
     * Makes new message according the data that passed and add to the message CRC code.
     *
     * @param messageLength the length of the message except CRC code
     * @param address       address of the device to send command
     * @param command       the byte that represents command
     * @param data          data to add to the message
     * @param crcOrder      the order of bytes in crc code to add to the message
     * @return ready message
     */
    public static ModbusMessage createModbusMessage(int messageLength, byte address, byte command,
                                                    byte[] data, boolean crcOrder) {
        ByteBuffer buffer = ByteBuffer.allocate(messageLength)
                .put(address)
                .put(command);
        if (data != null) {
            buffer.put(data);
        }

        ModbusMessage message = new ModbusMessage(CRC16.getMessageWithCRC16(buffer.array(), crcOrder));
        message.mIntegrity = true;
        message.mException = false;
        return message;
    }

    public byte[] getBuffer() {
        return mBuffer;
    }

    public void setBuffer(byte[] buffer) {
        this.mBuffer = buffer;
    }

    public boolean isIntegrity() {
        return mIntegrity;
    }

    public void setIntegrity(boolean integrity) {
        this.mIntegrity = integrity;
    }

    public boolean isException() {
        return mException;
    }

    public void setException(boolean exception) {
        this.mException = exception;
    }

    /**
     * Check whether the message is a valid response from device
     * <p>
     * Method throws {@link TimeoutException} if message length equals 0
     * <p>
     * Method throws {@link DeviceException} if message is a exception response from device
     * <p>
     * Method throws {@link IntegrityException} if CRC code of the message is not match
     *
     * @param message is a response from device to check
     * @throws ResponseException if message is not a valid response from device or contains
     *                           a device exception
     * @see IntegrityException
     * @see DeviceException
     * @see TimeoutException
     */
    public static void checkMessage(ModbusMessage message) throws ResponseException {
        if (message.getBuffer().length == 0) {
            throw new TimeoutException();
        } else if (message.isException()) {
            Log.e("TAG", "error Message: << "+ Arrays.toString(message.getBuffer()));
            throw new DeviceException(DeviceException.getExceptionDescription(message.getBuffer()[2]));
        } else if (!message.isIntegrity()) {
            throw new IntegrityException();
        }
    }

    /**
     * Compare two messages and determines if {@link DeviceException} is contained in one of them.
     * <p>
     * To detect exception from device it's necessary to compare the request message with response message.
     * @param requestMessage  the first message to compare
     * @param responseMessage the second message to compare
     * @return whether the <code>responseMessage</code> parameter contains a device exception
     *
     * @see DeviceException
     */
    public static boolean checkDeviceException(ModbusMessage requestMessage, ModbusMessage responseMessage) {
        try {
            int x = responseMessage.getBuffer()[1] & 255;
            int y = requestMessage.getBuffer()[1] & 255;
            return (x ^ y) == 0x80;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

}

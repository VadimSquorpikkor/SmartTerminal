package com.atomtex.smartterminal.util;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

/**
 * This class contains methods to convert byte[] to various type of data and back.
 *
 * @author stanislav.kleinikov@gmail.com
 */
public final class BitConverter {

    /**
     * Converts short value to byte array. Low byte follows the first
     *
     * @param v value to convert into byte array
     * @return <code>byte[]</code> that represents <code>v</code> value
     */
    public static byte[] getBytes(short v) {
        byte[] writeBuffer = new byte[2];
        writeBuffer[0] = (byte) ((v) & 0xFF);
        writeBuffer[1] = (byte) ((v >>> 8) & 0xFF);
        return writeBuffer;
    }

    /**
     * Gets a short number and returns it's lower byte
     *
     * @param v short value to get lower byte
     * @return lower byte from a given number
     */
    public static byte getLowByte(short v) {
        return (byte) ((v) & 0xFF);
    }

    /**
     * Gets a short number and returns it's higher byte
     *
     * @param v short value to get higher byte
     * @return higher byte from a given number
     */
    public static byte getHighByte(short v) {
        return (byte) ((v >>> 8) & 0xFF);
    }

    /**
     * Converts int value to byte array. Low byte follows the first
     *
     * @param v value to convert into byte array
     * @return <code>byte[]</code> that represents <code>v</code> value
     */
    public static byte[] getBytes(int v) {
        byte[] writeBuffer = new byte[4];
        writeBuffer[3] = (byte) ((v >>> 24) & 0xFF);
        writeBuffer[2] = (byte) ((v >>> 16) & 0xFF);
        writeBuffer[1] = (byte) ((v >>> 8) & 0xFF);
        writeBuffer[0] = (byte) ((v) & 0xFF);
        return writeBuffer;
    }

    /**
     * Converts long value to byte array. Low byte follows the first
     *
     * @param v value to convert into byte array
     * @return <code>byte[]</code> that represents <code>v</code> value
     */

    public static byte[] getBytes(long v) {
        byte[] writeBuffer = new byte[8];
        writeBuffer[7] = (byte) ((v >>> 56) & 0xFF);
        writeBuffer[6] = (byte) ((v >>> 48) & 0xFF);
        writeBuffer[5] = (byte) ((v >>> 40) & 0xFF);
        writeBuffer[4] = (byte) ((v >>> 32) & 0xFF);
        writeBuffer[3] = (byte) ((v >>> 24) & 0xFF);
        writeBuffer[2] = (byte) ((v >>> 16) & 0xFF);
        writeBuffer[1] = (byte) ((v >>> 8) & 0xFF);
        writeBuffer[0] = (byte) ((v) & 0xFF);
        return writeBuffer;
    }

    /**
     * Converts float value to byte array. Low byte follows the first
     *
     * @param v value to convert into byte array
     * @return <code>byte[]</code> that represents <code>v</code> value
     */
    public static byte[] getBytes(float v) {
        return getBytes(Float.floatToRawIntBits(v));
    }

    /**
     * Converts double value to byte array. Low byte follows the first
     *
     * @param v value to convert into byte array
     * @return <code>byte[]</code> that represents <code>v</code> value
     */
    public static byte[] getBytes(double v) {
        return getBytes(Double.doubleToRawLongBits(v));
    }

    /**
     * Converts String value to byte array using <code>UTF-8</code> charset.
     * Low byte follows the first
     *
     * @param v value to convert into byte array
     * @return <code>byte[]</code> that represents <code>v</code> value
     */
    public static byte[] getBytes(String v) {
        if (v == null) {
            v = "";
        }
        byte[] buf = new byte[4 + v.length()];
        System.arraycopy(getBytes(v.length()), 0, buf, 0, 4);
        try {
            byte[] vb = v.getBytes("UTF-8");
            System.arraycopy(vb, 0, buf, 4, vb.length);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("NO UTF-8");
        }
        return buf;
    }

    /**
     * Converts byte array in String using <code>UTF-8</code> charset. Low byte follows the first
     *
     * @param data   to convert in String
     * @param offset the start position in the byte array
     * @return String representation of the byte array
     */
    public static String toString(byte[] data, int offset) {
        int length = toInt32(data, offset);
        try {
            return new String(data, offset + 4, length, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("NO UTF-8");
        }
    }

    /**
     * Converts byte array to float value. The low byte must follow the first
     *
     * @param data   source that contains float value represented by byte array
     * @param offset start byte in <code>data</code> for calculation value
     * @return short value represented by <code>data</code> source
     */
    public static float toFloat(byte[] data, int offset) {
        return Float.intBitsToFloat(toInt32(data, offset));
    }

    /**
     * Converts byte array to double value. The low byte must follow the first
     *
     * @param data   source that contains double value represented by byte array
     * @param offset start byte in <code>data</code> for calculation value
     * @return short value represented by <code>data</code> source
     */
    public static double toDouble(byte[] data, int offset) {
        return Double.longBitsToDouble(toInt64(data, offset));
    }

    /**
     * Converts byte array to short value. The low byte must follow the first
     *
     * @param data   source that contains short value represented by byte array
     * @param offset start byte in <code>data</code> for calculation value
     * @return short value represented by <code>data</code> source
     */
    public static short toInt16(byte[] data, int offset) {
        return (short) (data[offset] & 0xFF | (data[offset + 1] & 0xFF) << 8);
    }

    /**
     * Converts byte array to int value. The low byte must follow the first
     *
     * @param data   source that contains int value represented by byte array
     * @param offset start byte in <code>data</code> for calculation value
     * @return int value represented by <code>data</code> source
     */
    public static int toInt32(byte[] data, int offset) {
        return (data[offset] & 0xFF) | ((data[offset + 1] & 0xFF) << 8)
                | ((data[offset + 2] & 0xFF) << 16)
                | ((data[offset + 3] & 0xFF) << 24);
    }

    /**
     * Converts byte array to long value. The low byte must follow the first
     *
     * @param data   source that contains int value represented by byte array
     * @param offset start byte in <code>data</code> for calculation value
     * @return long value represented by <code>data</code> source
     */
    public static long toInt64(byte[] data, int offset) {
        return (((long) (data[offset + 7] & 0xff) << 56)
                | ((long) (data[offset + 6] & 0xff) << 48)
                | ((long) (data[offset + 5] & 0xff) << 40)
                | ((long) (data[offset + 4] & 0xff) << 32)
                | ((long) (data[offset + 3] & 0xff) << 24)
                | ((long) (data[offset + 2] & 0xff) << 16)
                | ((long) (data[offset + 1] & 0xff) << 8) | (data[offset] & 0xff));
    }

    /**
     * Converts a UUID in byte array
     *
     * @param uuid the value to be converted in byte array
     * @return byte array representation of given <code>UUID</code>
     */
    public static byte[] asByteArray(UUID uuid) {
        long msb = uuid.getMostSignificantBits();
        long lsb = uuid.getLeastSignificantBits();
        byte[] buffer = new byte[16];

        for (int i = 0; i < 8; i++) {
            buffer[i] = (byte) (msb >>> 8 * (7 - i));
        }
        for (int i = 8; i < 16; i++) {
            buffer[i] = (byte) (lsb >>> 8 * (7 - i));
        }
        return buffer;
    }

    /**
     * Converts byte array in UUID
     *
     * @param byteArray the byte array to be converted
     * @return <code>UUID</code> representation of given  byte array
     */
    public static UUID toUUID(byte[] byteArray) {
        return toUUID(byteArray, 0);
    }

    /**
     * Converts byte array in UUID starting from given position
     *
     * @param byteArray the byte array to be converted
     * @param offset    the start position int byte array
     * @return <code>UUID</code> representation of given byte array
     */
    public static UUID toUUID(byte[] byteArray, int offset) {
        long msb = 0;
        long lsb = 0;

        for (int i = offset; i < offset + 8; i++) {
            msb = (msb << 8) | (byteArray[i] & 0xff);
        }

        for (int i = offset + 8; i < offset + 16; i++) {
            lsb = (lsb << 8) | (byteArray[i] & 0xff);
        }

        return new UUID(msb, lsb);
    }
}
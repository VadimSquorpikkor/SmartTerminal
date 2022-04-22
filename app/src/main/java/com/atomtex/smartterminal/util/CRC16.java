package com.atomtex.smartterminal.util;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * This class was designed to count check value by using the CRC-16-IBM algorithm.
 * <p>
 * A cyclic redundancy check (CRC) is an error-detecting code commonly used in digital networks
 * and storage devices to detect accidental changes to raw data.
 * Blocks of data get a short check value attached, based on the remainder of a polynomial division
 * of their contents. On retrieval, the calculation is repeated and, in the event the check
 * values do not match, corrective action can be taken against data corruption.
 *
 * @author stanislav.kleinikov@gmail.com
 */
public class CRC16 {

    /**
     * The order of bytes. The most significant byte comes first.
     */
    public static final boolean BIG_ENDIAN = true;

    /**
     * The order of bytes. The least significant byte comes first.
     */
    public static final boolean LITTLE_ENDIAN = false;

    private CRC16() {
    }

    /**
     * Counts the check value.
     *
     * @param dataBuffer is the source buffer for counting
     * @return int value of a check value
     */
    public static int calcCRC(byte[] dataBuffer) {
        int sum = 0xffff;
        for (byte aDataBuffer : dataBuffer) {
            sum = (sum ^ (aDataBuffer & 255));
            for (int j = 0; j < 8; j++) {
                if ((sum & 0x1) == 1) {
                    sum >>>= 1;
                    sum = (sum ^ 0xA001);
                } else {
                    sum >>>= 1;
                }
            }
        }
        return sum;
    }

    /**
     * Checks, whether the message is a proper one by using CRC-16-IBM algorithm.
     * <p>
     * The order of bytes in CRC code - {@link #BIG_ENDIAN}
     *
     * @param toCheck is the source data for check
     * @return whether the message is valid one
     */
    public static boolean checkCRC(byte[] toCheck) {
        if (toCheck.length < 2) {
            return false;
        }
        int x = toCheck[toCheck.length - 2];
        int y = toCheck[toCheck.length - 1];
        int crcToCheck = (x & 255) * 256 + (y & 255);
        int realCRC = calcCRC(Arrays.copyOf(toCheck, toCheck.length - 2));
        return realCRC == crcToCheck;
    }

    /**
     * Checks, whether the message is a proper one by using CRC-16-IBM algorithm.
     * <p>
     * The order of bytes in CRC code - {@link #LITTLE_ENDIAN}
     *
     * @param toCheck is the source data for check
     * @return whether the message is valid one
     */
    public static boolean checkCRCLittleEndian(byte[] toCheck) {
        if (toCheck.length < 2) {
            return false;
        }
        int x = toCheck[toCheck.length - 1];
        int y = toCheck[toCheck.length - 2];
        int crcToCheck = (x & 255) * 256 + (y & 255);
        int realCRC = calcCRC(Arrays.copyOf(toCheck, toCheck.length - 2));
        return realCRC == crcToCheck;
    }

    /**
     * Counts the CRC code of <code>byte[]</code> that was passed and add it to the and of
     * the array according by CRC order that was passed with the <code>byte[]</code>
     *
     * @param bytes    an array to count CRC code
     * @param crcOrder the byte order in which the code will be added.
     *                 This value is <code>true</code> if the order - BigEndian or
     *                 <code>false</code> if - LittleEndian
     * @return an array with the added CRC code
     */
    public static byte[] getMessageWithCRC16(byte[] bytes, boolean crcOrder) {
        short crc = (short) calcCRC(bytes);
        if (crcOrder) {
            crc = ByteSwapper.swap(crc);
        }
        byte[] bytesCRC = BitConverter.getBytes(crc);
        ByteBuffer buffer = ByteBuffer.allocate(bytes.length + bytesCRC.length);
        buffer.put(bytes);
        buffer.put(bytesCRC);
        return buffer.array();
    }
}


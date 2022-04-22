package com.atomtex.smartterminal.util;

/**
 * Utility class for doing various operation with bytes.
 *
 * @author stanislav.kleinikov@gmail.com
 */
public class Util {

    /**
     * Makes a hex string from byte which given by adding additional "0" if it has just a one digit.
     *
     * @param number byte to make a hex string
     * @return completed string
     */
    public static String getHexString(byte number) {
        String x = Integer.toHexString(number & 255);
        if (x.length() < 2) {
            x = "0" + x;
        }
        return x;
    }

    /**
     * Makes a hex string from bytes array. Separates single values with whitespace
     * and add additional "0" if it has just a one digit
     *
     * @param bytes an array of bytes to make string
     * @return string that represents an array which was passed
     */
    public static String getHexString(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte singleByte : bytes) {
            builder.append(getHexString(singleByte)).append(" ");
        }
        builder.trimToSize();
        return builder.toString();
    }

    /**
     * Converts the number into hex string and adds the leading '0' where number consist
     * of one digit
     *
     * @param number to convert
     * @return String representation of the given number
     */
    public static String getHexString(int number) {
        String x = Integer.toHexString(number);
        if (x.length() < 2) {
            x = "0" + x;
        }
        return x;
    }

    /**
     * Allows to recognise how many digits containing in a certain number
     *
     * @param number to count
     * @return the number of digits in given number
     */
    public static int getNumberOfDigits(float number) {
        number = Math.abs(number);
        if (number < 1) {
            return 0;
        } else if (number < 100000) {
            if (number < 100) {
                if (number < 10) {
                    return 1;
                } else {
                    return 2;
                }
            } else {
                if (number < 1000) {
                    return 3;
                } else {
                    if (number < 10000) {
                        return 4;
                    } else {
                        return 5;
                    }
                }
            }
        } else {
            if (number < 10000000) {
                if (number < 1000000) {
                    return 6;
                } else {
                    return 7;
                }
            } else {
                if (number < 100000000) {
                    return 8;
                } else {
                    if (number < 1000000000) {
                        return 9;
                    } else {
                        return 10;
                    }
                }
            }
        }
    }

    /**
     * Return suitable pattern for "String.format" function according the given number.This method
     * is need to display right number of digit to user depending on the specific number
     *
     * @param numberOfDigits the number of digits in certain number
     * @return the suitable pattern
     */
    public static String getPatternFloat(int numberOfDigits) {
        if (numberOfDigits == 0) {
            return "%.3f";
        } else if (numberOfDigits == 1) {
            return "%.2f";
        } else if (numberOfDigits == 2) {
            return "%.1f";
        } else return "%.0f";
    }

    public static String getPatternFloatForDose(int numberOfDigits) {
        if (numberOfDigits == 0) return "%.4f";
        else if (numberOfDigits == 1) return "%.3f";
        else if (numberOfDigits == 2) return "%.2f";
        else return "%.1f";
    }

    /**
     * Calculates the decimal logarithm of a number
     *
     * @param cbr to calculate
     * @return modified value
     */
    public static float scaleCbr(double cbr) {
        if (cbr == 1) {
            return 0.15f;
        }
        float result = (float) (Math.log10(cbr));
        return result == Double.NEGATIVE_INFINITY ? 0 : result;
    }

    /**
     * Raises the transmitted number to the power of 10
     *
     * @param cbr value to modify
     * @return modified value
     */
    public static float unScaleCbr(double cbr) {
        float result;
        if (cbr == 0) {
            result = 0;
        } else {
            result = (float) (Math.pow(10, cbr));
        }
        return result;
    }

    /**
     * Converts int value to unsigned long
     *
     * @param x to convert
     * @return long value converted from given value
     */
    public static long toUnsignedLong(int x) {
        return ((long) x) & 0xffffffffL;
    }
}

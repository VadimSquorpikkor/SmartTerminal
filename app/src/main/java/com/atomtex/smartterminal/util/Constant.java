package com.atomtex.smartterminal.util;

/**
 * This class contains the constants of the application
 *
 * @author stanislav.kleinikov@gmail.com
 */
public class Constant {

    //Device ID

    /**
     * ID_BTDU3 device ID
     */
    public static final short ID_BTDU3 = 0x30;

    //Detection unit ID

    /**
     * BDKG04 device ID
     */
    public static final short ID_BDKG04 = 0x25;

    /**
     * ID_BDKN05 device ID
     */
    public static final short ID_BDKN05 = 0x2a;

    /**
     * ID_BDKG11M device ID
     */
    public static final short ID_BDKG11M = 0x13;

    /**
     * ID_BDKG11 device ID
     */
    public static final short ID_BDKG11 = 0x0b;

    /**
     * ID_BDKG19M device ID
     */
    public static final short ID_BDKG19M = 0x18;

    /**
     * ID_BDKG28 device ID
     */
    public static final short ID_BDKG28 = 0x31;

    /**
     * ID_BDKG34 device ID
     */
    public static final short ID_BDKG34 = 0x4e;

    /**
     * ID_BDKG35 device ID
     */
    public static final short ID_BDKG35 = 0x1f;

    /**
     * ID_BDKG03 device ID
     */
    public static final short ID_BDKG03 = 0x07;

    /**Неизвестный*/
    public static final short ID_UNKNOWN = 0x0;

    public static final short ID_BDKN01 = 0x28;

    /**
     * ID_STR_I device ID (SrI2 38x38)
     */
    public static final short ID_BDKG05C = 0x6a;


    public static final short ID_BDKG32 = 0x30;//или 72? //48

    //The symbols that describe the type of the detection unit

    public static final char GAMMA_DU = 'S';

    public static final char NEUTRON_DU = 'N';

    public static final char HIGH_DOSE_RATE_DU = 'H';

    public static final char COUNT_DU = 'C';


    //Constant
    public static final short SPECTRUM_BACKGROUND = 2001;

    /**
     * The number of spectrum that keeps dependency channel -  the Sigma Gauss function in channels
     */
    public static final short SPECTRUM_SIGMA = 2097;

    /**
     * The number of spectrum that keeps a dependency channel - energy (Kev)
     */
    public static final short SPECTRUM_ENERGY = 2099;

    /**
     * The number of data registers to read from BTDU3 device
     */
    public static final short BT_DU3_REGISTER_NUMBER = 79;

    /**
     * The number of bytes in the response of firmware
     */
    public static final int FW_SZ = 2;

    /**
     * The max number of byte that detection unit might to send
     */
    public static final int MAX_ID = 100;

    /**
     * The number of detection unit
     */
    public static final int DU_NUM = 3;

    /**
     * The address of a modbus device by default
     */
    public static final byte ADAPTER_ADDRESS = 0x01;

    /**
     * The address of the first detection unit
     */
    public static final byte ADDRESS_DU_1 = (byte) 0x11;

    /**
     * The address of the second detection unit
     */
    public static final byte ADDRESS_DU_2 = (byte) 0x12;

    /**
     * The address of the third detection unit
     */
    public static final byte ADDRESS_DU_3 = (byte) 0x13;

    /**
     * The value of stabilization gain code by default
     */
    public static final float STABILIZATION_CODE_DEFAULT = 1100;

    /**
     * The maximum value that the high voltage can reach
     */
    public static final int MAX_HIGH_VOLTAGE = 8191;

    /**
     * The time of the accumulation spectrum in the stabilization mode
     */
    public static final int STAB_ACCUMULATED_TIME = 60000;

    /**
     * Max time of a stabilization process
     */
    public static final int MAX_STABILIZATION_TIME = 300000;

    /**
     * The time of the accumulation of the instantaneous speed rate in seconds for gamma device
     */
    public static final float DU_TIME_G_MOMCPS = 0.350f;

    /**
     * The time of the accumulation of the instantaneous speed rate in seconds for neutron device
     */
    public static final float DU_TIME_N_MOMCPS = 1.0f;

    /**
     * The maximum value that the stabilization gain code (SGC) can reach
     */
    public static final int MAX_SGC = 2047;


    public static final int GSHPNUM = 512;

    public static final int SRCHGSHPNUM = 800;

    public static final int GSHPSIGM = 150;

    public static final int GSHPSIGMx256x256 = 9830400;

    /**
     * The value of Gauss function.
     */
    public static final int[] GAUSSSHAPE = new int[]

            {
                    255, 255, 255, 255, 255, 255, 255, 255, 255, 255,
                    254, 254, 254, 254, 254, 254, 254, 253, 253, 253,
                    253, 253, 252, 252, 252, 251, 251, 251, 251, 250,
                    250, 250, 249, 249, 249, 248, 248, 247, 247, 247,
                    246, 246, 245, 245, 244, 244, 243, 243, 242, 242,
                    241, 241, 240, 240, 239, 238, 238, 237, 237, 236,
                    235, 235, 234, 233, 233, 232, 231, 231, 230, 229,
                    229, 228, 227, 227, 226, 225, 224, 224, 223, 222,
                    221, 220, 220, 219, 218, 217, 216, 216, 215, 214,
                    213, 212, 211, 210, 210, 209, 208, 207, 206, 205,
                    204, 203, 202, 201, 201, 200, 199, 198, 197, 196,
                    195, 194, 193, 192, 191, 190, 189, 188, 187, 186,
                    185, 184, 183, 182, 181, 180, 179, 178, 177, 176,
                    175, 174, 173, 172, 171, 170, 169, 168, 167, 166,
                    165, 164, 163, 162, 161, 160, 159, 158, 157, 156,
                    155, 154, 153, 152, 151, 150, 148, 147, 146, 145,
                    144, 143, 142, 141, 140, 139, 138, 137, 136, 135,
                    134, 133, 132, 131, 130, 129, 128, 127, 126, 125,
                    124, 123, 122, 121, 120, 119, 118, 117, 116, 115,
                    114, 113, 112, 111, 110, 110, 109, 108, 107, 106,
                    105, 104, 103, 102, 101, 100, 99, 98, 97, 97,
                    96, 95, 94, 93, 92, 91, 90, 90, 89, 88,
                    87, 86, 85, 84, 84, 83, 82, 81, 80, 80,
                    79, 78, 77, 76, 76, 75, 74, 73, 72, 72,
                    71, 70, 69, 69, 68, 67, 66, 66, 65, 64,
                    64, 63, 62, 61, 61, 60, 59, 59, 58, 57,
                    57, 56, 55, 55, 54, 54, 53, 52, 52, 51,
                    50, 50, 49, 49, 48, 47, 47, 46, 46, 45,
                    45, 44, 44, 43, 42, 42, 41, 41, 40, 40,
                    39, 39, 38, 38, 37, 37, 36, 36, 35, 35,
                    35, 34, 34, 33, 33, 32, 32, 31, 31, 31,
                    30, 30, 29, 29, 29, 28, 28, 27, 27, 27,
                    26, 26, 25, 25, 25, 24, 24, 24, 23, 23,
                    23, 22, 22, 22, 21, 21, 21, 20, 20, 20,
                    20, 19, 19, 19, 18, 18, 18, 18, 17, 17,
                    17, 17, 16, 16, 16, 15, 15, 15, 15, 15,
                    14, 14, 14, 14, 13, 13, 13, 13, 13, 12,
                    12, 12, 12, 12, 11, 11, 11, 11, 11, 10,
                    10, 10, 10, 10, 10, 9, 9, 9, 9, 9,
                    9, 9, 8, 8, 8, 8, 8, 8, 8, 7,
                    7, 7, 7, 7, 7, 7, 7, 6, 6, 6,
                    6, 6, 6, 6, 6, 6, 5, 5, 5, 5,
                    5, 5, 5, 5, 5, 5, 5, 4, 4, 4,
                    4, 4, 4, 4, 4, 4, 4, 4, 4, 4,
                    3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
                    3, 3, 3, 3, 3, 3, 3, 2, 2, 2,
                    2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                    2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                    2, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                    1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                    1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                    1, 1
            };

    /**
     * The value of Gauss function.
     */
    public static final int[] SEARCHSHAPE = new int[]{
            -127, -127, -127, -127, -127, -127, -127, -127, -126, -126,
            -126, -126, -126, -126, -125, -125, -125, -125, -124, -124,
            -124, -123, -123, -123, -122, -122, -121, -121, -120, -120,
            -120, -119, -118, -118, -117, -117, -116, -116, -115, -114,
            -114, -113, -113, -112, -111, -110, -110, -109, -108, -108,
            -107, -106, -105, -104, -104, -103, -102, -101, -100, -99,
            -98, -98, -97, -96, -95, -94, -93, -92, -91, -90,
            -89, -88, -87, -86, -85, -84, -83, -82, -81, -80,
            -79, -78, -77, -76, -75, -73, -72, -71, -70, -69,
            -68, -67, -66, -65, -63, -62, -61, -60, -59, -58,
            -56, -55, -54, -53, -52, -51, -50, -48, -47, -46,
            -45, -44, -43, -41, -40, -39, -38, -37, -36, -34,
            -33, -32, -31, -30, -29, -27, -26, -25, -24, -23,
            -22, -21, -19, -18, -17, -16, -15, -14, -13, -12,
            -11, -10, -8, -7, -6, -5, -4, -3, -2, -1,
            0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
            10, 11, 12, 13, 14, 15, 15, 16, 17, 18,
            19, 20, 21, 22, 22, 23, 24, 25, 26, 26,
            27, 28, 29, 29, 30, 31, 32, 32, 33, 34,
            34, 35, 36, 36, 37, 38, 38, 39, 39, 40,
            41, 41, 42, 42, 43, 43, 44, 44, 45, 45,
            46, 46, 47, 47, 48, 48, 48, 49, 49, 50,
            50, 50, 51, 51, 51, 52, 52, 52, 52, 53,
            53, 53, 53, 54, 54, 54, 54, 55, 55, 55,
            55, 55, 55, 56, 56, 56, 56, 56, 56, 56,
            56, 56, 56, 56, 57, 57, 57, 57, 57, 57,
            57, 57, 57, 57, 57, 57, 57, 56, 56, 56,
            56, 56, 56, 56, 56, 56, 56, 56, 56, 55,
            55, 55, 55, 55, 55, 55, 54, 54, 54, 54,
            54, 53, 53, 53, 53, 53, 52, 52, 52, 52,
            52, 51, 51, 51, 51, 50, 50, 50, 50, 49,
            49, 49, 49, 48, 48, 48, 47, 47, 47, 47,
            46, 46, 46, 45, 45, 45, 45, 44, 44, 44,
            43, 43, 43, 42, 42, 42, 42, 41, 41, 41,
            40, 40, 40, 39, 39, 39, 38, 38, 38, 37,
            37, 37, 36, 36, 36, 36, 35, 35, 35, 34,
            34, 34, 33, 33, 33, 32, 32, 32, 31, 31,
            31, 31, 30, 30, 30, 29, 29, 29, 28, 28,
            28, 28, 27, 27, 27, 26, 26, 26, 25, 25,
            25, 25, 24, 24, 24, 24, 23, 23, 23, 22,
            22, 22, 22, 21, 21, 21, 21, 20, 20, 20,
            20, 19, 19, 19, 19, 18, 18, 18, 18, 17,
            17, 17, 17, 17, 16, 16, 16, 16, 15, 15,
            15, 15, 15, 14, 14, 14, 14, 14, 13, 13,
            13, 13, 13, 13, 12, 12, 12, 12, 12, 11,
            11, 11, 11, 11, 11, 10, 10, 10, 10, 10,
            10, 10, 9, 9, 9, 9, 9, 9, 9, 8,
            8, 8, 8, 8, 8, 8, 7, 7, 7, 7,
            7, 7, 7, 7, 7, 6, 6, 6, 6, 6,
            6, 6, 6, 6, 6, 5, 5, 5, 5, 5,
            5, 5, 5, 5, 5, 5, 4, 4, 4, 4,
            4, 4, 4, 4, 4, 4, 4, 4, 4, 4,
            3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
            3, 3, 3, 3, 3, 3, 3, 2, 2, 2,
            2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
            2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
            2, 2, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0
    };


    //Basic commands

    /**
     * To consider the status of the binary signals
     */
    public static final byte READ_STATUS_BINARY_SIGNAL = 0x02;

    /**
     * Consider the state of the control registers
     */
    public static final byte READ_STATE_CONTROL_REGISTERS = 0x03;

    /**
     * Read state of data registers
     */
    public static final byte READ_STATE_DATA_REGISTERS = 0x04;

    /**
     * Send control signal
     */
    public static final byte SEND_CONTROL_SIGNAL = 0x05;

    /**
     * Change the state of the control register
     */
    public static final byte CHANGE_STATE_CONTROL_REGISTER = 0x06;

    /**
     * Read status word
     */
    public static final byte READ_STATUS_WORD = 0x07;

    /**
     * Diagnostics
     */
    public static final byte DIAGNOSTICS = 0x08;

    /**
     * Consider a sample spectrum from the non-volatile memory
     */
    public static final byte READ_SPECTRUM_NON_VOLATILE_MEMORY = 0x09;

    /**
     * To consider the accumulated sample spectrum
     */
    public static final byte READ_SPECTRUM_ACCUMULATED_SAMPLE = 0x0B;

    /**
     * Change the state of control registers
     */
    public static final byte CHANGE_STATE_CONTROL_REGISTERS = 0x10;

    /**
     * Read device identification code
     */
    public static final byte READ_DEVICE_ID = 0x11;

    /**
     * Read calibration data sample
     */
    public static final byte READ_CALIBRATION_DATA_SAMPLE = 0x12;

    /**
     * Record calibration data sample
     */
    public static final byte WRITE_CALIBRATION_DATA_SAMPLE = 0x13;

    /**
     * Read the accumulated spectrum
     */
    public static final byte READ_ACCUMULATED_SPECTRUM = 0x40;

    /**
     * Read the accumulated spectrum in compressed form with the restart of set
     */
    public static final byte READ_ACCUMULATED_SPECTRUM_COMPRESSED_REBOOT = 0x41;

    /**
     * Read the accumulated spectrum in compressed form
     */
    public static final byte READ_ACCUMULATED_SPECTRUM_COMPRESSED = 0x42;


    //Diagnostic masks.Shows problem of a connected adapter.

    /**
     * Error subsystem communication with detection unit.
     */
    public static final int DIAG_MASK_COMM_ERROR = 0b00000001;

    /**
     * Battery monitoring error.
     */
    public static final int DIAG_MASK_BAT_ERROR = 0b00000010;

    /**
     * The error of accessing to the nonvolatile memory.
     */
    public static final int DIAG_MASK_NVM_ERROR = 0b00000100;

    /**
     * Overheat error
     */
    public static final int DIAG_MASK_HEAT_ERROR = 0b00001000;

    /**
     * Destroyed adapter calibration data in non-volatile memory
     */
    public static final int DIAG_MASK_DATA_ERROR = 0b10000000;


    //The Exception codes

    /**
     * Indicates that the command is invalid
     */
    public static final byte EXCEPTION_INVALID_COMMAND = 0x01;

    /**
     * Indicates that the address data is invalid
     */
    public static final byte EXCEPTION_INVALID_DATA_ADDRESS = 0x02;

    /**
     * Indicates that the data value is invalid
     */
    public static final byte EXCEPTION_DATA_VALUE = 0x03;

    /**
     * The failure of the detection unit.
     * Detailed information can be obtained using the diagnostic register read command
     */
    public static final byte EXCEPTION_DETECTION_UNIT_FAILURE = 0x04;

    /**
     * The detection unit is busy executing the previous command
     */
    public static final byte EXCEPTION_DETECTION_UNIT_BUSY = 0x06;

    //The mask is used to show the validity of data that is written into spec file
    /**
     * Indicates that the Priority Average count rate of gamma-radiation spectrometric detection unit
     * is a valid one.
     */
    public static final int MASK_G_CPS = 0b00000001;

    /**
     * Indicates that the Priority-averaged Average dose rate of gamma radiation of a spectrometric
     * detection unit is a valid one.
     */
    public static final int MASK_G_DR = 0b00000010;

    /**
     * Indicates that Priority Average neutron counting rate is a valid one.
     */
    public static final int MASK_N_CPS = 0b00000100;

    /**
     * Indicates that Priority-averaged Average dose rate of neutron radiation is a valid one.
     */
    public static final int MASK_N_DR = 0b00001000;

    /**
     * Priority Average calculation speed of high-load detection unit gamma radiation is a valid one.
     */
    public static final int MASK_H_CPS = 0b00010000;

    /**
     * Priority-averaged Average dose rate of gamma radiation of high-load detection unit is valid one.
     */
    public static final int MASK_H_DR = 0b00100000;


    // The numbers of fields in glasses screen. This constants  are need to use in messages to update
    // glasses screen
    /**
     * The byte is responsible for the field in glasses device application that shows current mode
     * of the application.
     */
    public static final byte GLASSES_MODE = 11;
    /**
     * The byte is responsible for the field in glasses device application that shows whether
     * the application has a valid location data at the moment of not.
     */
    public static final byte GLASSES_GPS = 12;
    /**
     * The byte is responsible for the field in glasses device application that shows whether
     * adapter is connected or not.
     */
    public static final byte GLASSES_BTDU = 13;
    /**
     * The byte is responsible for the field in glasses device application that shows symbol of
     * gamma channel.
     */
    public static final byte GLASSES_G = 21;
    /**
     * The byte is responsible for the field in glasses device application that shows data of
     * measurements of gamma channel.
     */
    public static final byte GLASSES_G_DR = 22;
    /**
     * The byte is responsible for the field in glasses device application that shows measure unit
     * of gamma channel data.
     */
    public static final byte GLASSES_G_SVH = 23;
    /**
     * The byte is responsible for the field in glasses device application that shows error of
     * gamma channel measurements.
     */
    public static final byte GLASSES_G_ERR = 24;
    /**
     * The byte is responsible for the field in glasses device application that shows symbol of
     * neutron channel.
     */
    public static final byte GLASSES_N = 31;
    /**
     * The byte is responsible for the field in glasses device application that shows measure unit
     * of neutron channel data.
     */
    public static final byte GLASSES_N_DR = 32;
    /**
     * The byte is responsible for the field in glasses device application that shows error of
     * neutron channel measurements.
     */
    public static final byte GLASSES_N_SVH = 33;
    /**
     * The byte is responsible for the field in glasses device application that shows error of
     * neutron channel measurements.
     */
    public static final byte GLASSES_N_ERR = 34;
    /**
     * The byte is responsible for the field in glasses device application that shows a result of
     * identification process.
     */
    public static final byte GLASSES_IDEN = 41;
    /**
     * The byte is responsible for the field in glasses device application that shows a status
     * string.
     */
    public static final byte GLASSES_STATUS = 51;
    /**
     * The byte is responsible for the field in glasses device application that shows diagnostic data.
     */
    public static final byte GLASSES_DIAGNOSTIC = 61;
    /**
     * The byte is responsible for the field in glasses device application that shows battery status
     * of a connected adapter.
     */
    public static final byte GLASSES_BAT_BTDU = 71;
    /**
     * The byte is responsible for the field in glasses device application that shows battery status
     * of a PDA.
     */
    public static final byte GLASSES_BAT_PDA = 81;
    /**
     * The byte is responsible for the field in glasses device application that shows remaining time
     * of work the application according to batteries states.
     */
    public static final byte GLASSES_BAT_PACK = 91;

}

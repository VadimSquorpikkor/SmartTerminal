package com.atomtex.smartterminal.modbus;

import android.bluetooth.BluetoothDevice;

/**
 * Contains methods which return the {@link com.atomtex.smartterminal.modbus.ModbusTransport} implementations.
 *
 * @author stanislav.kleinikov@gmail.com
 */
public class ModbusTransportFactory {

    /**
     * Return appropriate transport to communicate with remote device.
     *
     * @param device a remote device to communicate through the transport.
     * @return the instance of ModbusTransport
     */
    public static ModbusTransport getTransport(BluetoothDevice device) {
        return ModbusRFCOMMTransport.getInstance(device);
    }
}

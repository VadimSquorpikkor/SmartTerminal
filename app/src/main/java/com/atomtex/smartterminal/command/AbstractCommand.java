package com.atomtex.smartterminal.command;

import androidx.lifecycle.MutableLiveData;

import com.atomtex.smartterminal.exception.ConnectingException;
import com.atomtex.smartterminal.exception.IntegrityException;
import com.atomtex.smartterminal.exception.ResponseException;
import com.atomtex.smartterminal.exception.TimeoutException;
import com.atomtex.smartterminal.modbus.AbstractAdapter;
import com.atomtex.smartterminal.modbus.Adapter;

import java.util.Arrays;

/**
 * The super class for all command in the application.All classes that inherited from this class
 * must implement {{@link #method()}} method.
 */
public abstract class AbstractCommand implements Command {

    /**
     * The instance of {@link AbstractAdapter} to apply the {@link }.
     */
    private AbstractAdapter mAdapter;

    /**
     * Address of device to apply command.
     *
     * <p>
     * This value is used to send command through the MODBUS protocol to certain device
     */
    private byte mAddress;

    /**
     * The array of bytes that represents a data for executing command.
     */
    private byte[] mData;

    private boolean mCRCOrder;

    private byte mCommand;

    private MutableLiveData<String> mResponse;

    public AbstractAdapter getAdapter() {
        return mAdapter;
    }

    public byte getAddress() {
        return mAddress;
    }

    public byte[] getData() {
        return mData;
    }

    public boolean getCRCOrder() {
        return mCRCOrder;
    }

    public byte getCommand() {
        return mCommand;
    }

    public MutableLiveData<String> getResponse() {
        return mResponse;
    }

    @Override
    public synchronized void execute(Adapter adapter, byte[] fullData, boolean crcOrder)
            throws ConnectingException, ResponseException {
        mAdapter = adapter;
        mAddress = fullData[0];
        mData = fullData.length>2?Arrays.copyOfRange(fullData, 2, fullData.length):null;
        mCRCOrder = crcOrder;
        mCommand = fullData[1];

        for (int i = 0; i < 2; i++) {
            try {
                method();
                return;
            } catch (IntegrityException | TimeoutException e) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {
                }
            }
        }
        method();
    }

    @Override
    public synchronized void execute(Adapter adapter, byte[] fullData, boolean crcOrder, MutableLiveData<String> response)
            throws ConnectingException, ResponseException {
        mAdapter = adapter;
        mAddress = fullData[0];
        mData = fullData.length>2?Arrays.copyOfRange(fullData, 2, fullData.length):null;
        mCRCOrder = crcOrder;
        mCommand = fullData[1];
        mResponse = response;

        for (int i = 0; i < 2; i++) {
            try {
                method();
                return;
            } catch (IntegrityException | TimeoutException e) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {
                }
            }
        }
        method();
    }


    /**
     * This method performs the main action.Each subclass of {@link AbstractCommand} must implement
     * this method.
     *
     * @throws ConnectingException in case of loss of connection
     * @throws ResponseException   if the responses from device are not valid
     */
    public abstract void method() throws ConnectingException, ResponseException;
}

package com.atomtex.smartterminal.command;

import androidx.lifecycle.MutableLiveData;

import com.atomtex.smartterminal.exception.ConnectingException;
import com.atomtex.smartterminal.exception.ResponseException;
import com.atomtex.smartterminal.modbus.Adapter;
import com.atomtex.smartterminal.util.CRC16;

public interface Command {

    /**Альтернативный вариант. Чтобы сразу задать команду в виде 01 08 00 70 00 00 (с адресом, номером команды и данными)*/
    void execute(Adapter adapter, byte[] fullData, boolean crcOrder)
            throws ConnectingException, ResponseException;

    void execute(Adapter adapter, byte[] fullData, boolean crcOrder, MutableLiveData<String> response)
            throws ConnectingException, ResponseException;
}

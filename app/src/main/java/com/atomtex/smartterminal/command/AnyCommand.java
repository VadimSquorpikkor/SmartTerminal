package com.atomtex.smartterminal.command;

import android.util.Log;
import com.atomtex.smartterminal.exception.ConnectingException;
import com.atomtex.smartterminal.exception.ResponseException;
import com.atomtex.smartterminal.modbus.ModbusMessage;

import java.util.Arrays;

/**
 *
 */
public abstract class AnyCommand extends AbstractCommand {

    public AnyCommand() {

    }

    /**Если нужно задать метод, то использую как:
     *
     * new AnyCommand() {
     *             public void onResponse(byte[] response) {
     *                  //что-то делаю
     *             }
     *         }.execute(mAdapter, address, data, crcOrder);
     *(это немного не то, так как уже используется не синглтон, а каждый раз создается объект класса (анонимный))
     *
     *Если нужно потестить что-то, без метода (просто вывод response и request в лог), тогда через синглтон:
     * AnyCommand.getInstance().execute(mAdapter, address, data, crcOrder);
     *
     * Для тестирования удобнее использовать такой вариант (когда вся команда с адресом и номером команды пишется в одном массиве):
     * AnyCommand.getInstance().execute(mAdapter, new byte[]{0x51, 0x04, 0x00, 0x0d, 0x00, 0x03}, crcOrder);
     * */
    private static final class AnyCommandHolder {
        private static final AnyCommand INSTANCE = new AnyCommand() {
            @Override
            public void onResponse(byte[] response) {}
        };
    }

    public static AnyCommand getInstance() {
        return AnyCommandHolder.INSTANCE;
    }

    public void method() throws ConnectingException, ResponseException {
        ModbusMessage request = ModbusMessage.createModbusMessage(getData()==null?2:getData().length+2, getAddress(), getCommand(),
                getData(), getCRCOrder());
        Log.e("TAG", "FUCKING_SPECIAL_COMMAND: request "+ Arrays.toString(request.getBuffer()));
        //getResponse().postValue(">> " + HexTranslate.byteArrayToHexString(request.getBuffer()));

        ModbusMessage response = getAdapter().sendMessageWithResponse(request);
        Log.e("TAG", "FUCKING_SPECIAL_COMMAND: response "+ Arrays.toString(response.getBuffer()));

        //getResponse().postValue(HexTranslate.byteArrayToHexString(response.getBuffer()));
        onResponse(response.getBuffer());
    }

    public abstract void onResponse(byte[] response);
}

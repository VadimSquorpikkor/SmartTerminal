package com.atomtex.smartterminal.exception;

import static com.atomtex.smartterminal.util.Constant.EXCEPTION_DATA_VALUE;
import static com.atomtex.smartterminal.util.Constant.EXCEPTION_DETECTION_UNIT_BUSY;
import static com.atomtex.smartterminal.util.Constant.EXCEPTION_DETECTION_UNIT_FAILURE;
import static com.atomtex.smartterminal.util.Constant.EXCEPTION_INVALID_COMMAND;
import static com.atomtex.smartterminal.util.Constant.EXCEPTION_INVALID_DATA_ADDRESS;

import com.atomtex.smartterminal.util.Constant;

/**
 * Если запрос содержит команду, которую БД не может выполнить, то он формирует ответ специального
 * вида – сообщение об исключении. Адрес устройства и кода команды такого ответа повторяют
 * соответствующие поля запроса, кроме старшего бита кода команды, который устанавливается в «1».
 * Поле данных имеет размер 1 байт и содержит код исключения. Код исключения говорит о причинах,
 * не позволяющих выполнить запрашиваемую команду.
 *
 * @author stanislav.kleinikov@gmail.com
 * @see Constant
 */
public class DeviceException extends ResponseException {

    public DeviceException() {
        super();
    }

    public DeviceException(String message) {
        super(message);
    }

    public static String getExceptionDescription(byte code) {
        String message;
        switch (code) {
            case EXCEPTION_INVALID_COMMAND:
//                message = "The command is invalid";
                message = "Недопустимая команда";
                break;
            case EXCEPTION_INVALID_DATA_ADDRESS:
//                message = "The address data is invalid";
                message = "Недопустимый адрес данных";
                break;
            case EXCEPTION_DATA_VALUE:
//                message = "The data value is invalid";
                message = "Недопустимое значение данных";
                break;
            case EXCEPTION_DETECTION_UNIT_FAILURE:
//                message = "The failure of the detection unit." +
//                        " Detailed information can be obtained using the diagnostic register read command";
                message = "Отказ блока детектирования. \n" +
                        "Детальную информацию можно получить с помощью команды чтения регистра диагностики";
                break;
            case EXCEPTION_DETECTION_UNIT_BUSY:
//                message = "The detection unit is busy executing the previous command";
                message = "Блок детектирования занят выполнением предыдущей команды";
                break;
            default:
                message = "Unknown Exception";
                break;
        }
        return message;
    }
}
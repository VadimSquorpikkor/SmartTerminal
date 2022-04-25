package com.atomtex.smartterminal;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.atomtex.smartterminal.command.AnyCommand;
import com.atomtex.smartterminal.exception.ConnectingException;
import com.atomtex.smartterminal.exception.ResponseException;

import java.util.ArrayList;

public class MainViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<BluetoothDevice>> btPairedDeviceList;
    private final MutableLiveData<ArrayList<BluetoothDevice>> btFoundDeviceList;
    private final MutableLiveData<Boolean>  isDiscovering;
    private final MutableLiveData<Boolean>  isBtSearch;
    private final MutableLiveData<Boolean>  isBtConnected;

    private final MutableLiveData<String> requestText;
    private final MutableLiveData<String> response;

    private BluetoothHelper bluetoothHelper;

    public MainViewModel() {
        this.btPairedDeviceList = new MutableLiveData<>();
        this.btFoundDeviceList  = new MutableLiveData<>();
        this.isDiscovering      = new MutableLiveData<>(false);
        this.isBtSearch         = new MutableLiveData<>(false);
        this.isBtConnected      = new MutableLiveData<>(false);
        this.requestText = new MutableLiveData<>("");
        this.response           = new MutableLiveData<>("");
    }


    public BluetoothHelper getBluetoothHelper() {
        return bluetoothHelper;
    }
    public MutableLiveData<String> getRequestText() {
        return requestText;
    }
    public MutableLiveData<String> getResponse() {
        return response;
    }

    public void sendCommand() {
        byte[] byteCommand = HexTranslate.hexStringToByteArray(stringCommand);
        //[80, 4, 0, 13, 0, 3, 44, 73]
        //[80, 4, 0, 13, 0, 3, 73, 44]
        try {AnyCommand.getInstance().execute(bluetoothHelper.getAdapter(), byteCommand, true, response);}//new byte[]{0x50, 0x04, 0x00, 0x0d, 0x00, 0x03}
        catch (ConnectingException | ResponseException e) {e.printStackTrace();}
    }

    //---------------------- BLUETOOTH -----------------------------------------------------------------
    /**Запуск поиска устройств bluetooth. Можно в фильтре указать имена устройств, которые будут
     * отображаться в результате поиска, все устройства, в имени которых не содержатся такие строки,
     * будут игнорироваться. Если ничего не выбрано — отображаются все найденные устройства*/
    public void startBluetoothSearch() {
        Log.e("TAG", "startBluetoothSearch: ");
        getIsBtSearch().postValue(true);
        bluetoothHelper.disconnect();
//        bluetoothHelper.setNameFilter("BTDU", "BT-DU", "AT6101DR", "SQUORPIKKOR", "X6_BLE");
        bluetoothHelper.queryPairedDevices();
        bluetoothHelper.showDeviceLog();
        bluetoothHelper.startDiscovery();
    }

    public MutableLiveData<Boolean> IsBTConnected() {
        return isBtConnected;
    }

    public MutableLiveData<Boolean> getIsBtSearch() {
        return isBtSearch;
    }

    /**Список ранее подключенных устройств Bluetooth*/
    public MutableLiveData<ArrayList<BluetoothDevice>> getBTPairedDeviceList() {
        return btPairedDeviceList;
    }
    /**Список найденных устройств Bluetooth*/
    public MutableLiveData<ArrayList<BluetoothDevice>> getBTFoundDeviceList() {
        return btFoundDeviceList;
    }
    /**Отслеживание состояния поиска устройств bluetooth (вкл/выкл)*/
    public MutableLiveData<Boolean> getIsDiscovering() {
        return isDiscovering;
    }
    /**Отключение BluetoothBroadCastReceiver*/
    public void unregisterReceiver() {
        if (bluetoothHelper!=null) bluetoothHelper.unregisterReceiver();
    }
    /**Подключение к устройству bluetooth*/
    public void connectToDevice(BluetoothDevice device) {
        bluetoothHelper.connectToDevice(device);
    }
    /**Подключение к последнему подключенному устройству*/
    public void startConnectToLastBTDevice() {
        if (bluetoothHelper!=null) bluetoothHelper.connectToLast();
    }
    /**Активация Bluetooth*/
    public void startBluetooth(Activity activity) {
        bluetoothHelper = new BluetoothHelper(activity, btPairedDeviceList, btFoundDeviceList, isBtSearch, isBtConnected);
        bluetoothHelper.setDiscoveryStartedListener(isDiscovering);
        startConnectToLastBTDevice();
    }


    public void stopBluetoothSearch() {
//        bluetoothHelper.cancelDiscovery();
    }



    public String getBluetoothDeviceName() {
//        return bluetoothHelper.getDeviceName();
        return "";
    }
//--------------------------------------------------------------------------------------------------

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.e("TAG", "main onCleared: ");
        unregisterReceiver();
        bluetoothHelper = null;
        //bound = false;
//        System.exit(0);
    }

    private String stringCommand = "";

    public void addNumber(String n) {
        stringCommand += n;
        requestText.setValue(">>"+stringCommand);
    }

    public void clearText() {
        requestText.setValue("");
        stringCommand = "";
    }
}

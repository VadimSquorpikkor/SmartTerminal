package com.atomtex.smartterminal;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class MainViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<BluetoothDevice>> btPairedDeviceList;
    private final MutableLiveData<ArrayList<BluetoothDevice>> btFoundDeviceList;
    private final MutableLiveData<Boolean>  isDiscovering;
    private final MutableLiveData<Boolean>  isBtSearch;

    public MainViewModel() {
        this.btPairedDeviceList = new MutableLiveData<>();
        this.btFoundDeviceList  = new MutableLiveData<>();
        this.isDiscovering      = new MutableLiveData<>(false);
        this.isBtSearch         = new MutableLiveData<>(false);
    }

//---------------------- BLUETOOTH -----------------------------------------------------------------
    /**Запуск поиска устройств bluetooth. Можно в фильтре указать имена устройств, которые будут
     * отображаться в результате поиска, все устройства, в имени которых не содержатся такие строки,
     * будут игнорироваться. Если ничего не выбрано — отображаются все найденные устройства*/
    public void startBluetoothSearch() {
        Log.e("TAG", "startBluetoothSearch: ");
        getIsBtSearch().postValue(true);
        disconnect();
//        bluetoothHelper.setNameFilter("BTDU", "BT-DU", "AT6101DR", "SQUORPIKKOR", "X6_BLE");
//        bluetoothHelper.queryPairedDevices();
//        bluetoothHelper.showDeviceLog();
//        bluetoothHelper.startDiscovery();
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
//        if (bluetoothHelper!=null) bluetoothHelper.unregisterReceiver();
    }
    /**Подключение к устройству bluetooth*/
    public void connectToDevice(BluetoothDevice device) {
//        bluetoothHelper.connectToDevice(device, service);
    }
    /**Подключение к последнему подключенному устройству*/
    public void startConnectToLastBTDevice() {
//        if (bluetoothHelper!=null) bluetoothHelper.connectToLast(service);
    }
    /**Активация Bluetooth*/
    public void startBluetooth(Activity activity) {
//        bluetoothHelper = new BluetoothHelper(activity, btPairedDeviceList, btFoundDeviceList);
//        bluetoothHelper.setDiscoveryStartedListener(isDiscovering);
    }


    public void stopBluetoothSearch() {
//        bluetoothHelper.cancelDiscovery();
    }

    public void disconnect() {
//        service.disconnect();
    }

    public String getBluetoothDeviceName() {
//        return bluetoothHelper.getDeviceName();
        return "";
    }
//--------------------------------------------------------------------------------------------------



}

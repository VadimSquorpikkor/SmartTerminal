package com.atomtex.smartterminal;

import static com.atomtex.smartterminal.App.TAG;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.MutableLiveData;

import com.atomtex.smartterminal.modbus.Adapter;
import com.atomtex.smartterminal.modbus.ModbusTransportFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**Начал работу над BluetoothHelper — работать с БТ теперь сильно удобнее: все методы (включая
 * проверки, запросы и ресивер) внутри класса, всё автономно. Текущий функционал: проверка БТ
 * (поддерживается ли устройством БТ и включен ли (если выключен — включаю)); получение списка
 * ранее подключенных устройств, поиск устройства.
 * Ещё фишка Хелпера: мьютабл списоки БТ устройств (и paired и найденных) хранятся в вьюМоделе,
 * через конструктор передаются в Хелпер, из него через конструктор внутреннего класса в ресивер.
 * Получая данные по блютуз, ресивер заносит их в эти списки, а так как эти списки являются частью
 * ВьюМодели, менения (по сути) асинхронно обновляются в подписанных ресайклерах (и в других вьюшках).
 * При этом, чтобы вся эта магия заработала, нужно только создать объект класса BluetoothHelper и
 * вызвать метод bluetoothHelper.startDiscovery(). Гарри Поттер нервно курит
 *
 *
 * Для примера:
 * public void startBluetoothSearch() {
 *         data.getIsBtSearch().postValue(true);
 *         disconnect();
 *         bluetoothHelper.setNameFilter("BTDU", "BT-DU", "AT6101DR", "SQUORPIKKOR", "X6_BLE");
 *         bluetoothHelper.queryPairedDevices();
 *         bluetoothHelper.showDeviceLog();
 *         bluetoothHelper.startDiscovery();
 *     }*/
public class BluetoothHelper {

    private static final int REQUEST_ENABLE_BT = 100;
    private static final String ACTION_START_CONNECT = "action_start_connect";
    private static final String LAST_ADDRESS = "last_address";
    private BluetoothAdapter bluetoothAdapter;
    private final Activity activity;
    private BluetoothBroadCastReceiver mBroadcastReceiver;
    private MutableLiveData<ArrayList<BluetoothDevice>> btPairedDeviceList;
    private MutableLiveData<ArrayList<BluetoothDevice>> btFoundDeviceList;//todo возможно есть смысл заменить ArrayList на Set
    private ArrayList<String> availableNames;

    private MutableLiveData<Boolean> isBtSearch;
    private MutableLiveData<Boolean> isBtConnected;
    /**Девайс, к которому подключились (а не те устройства,которые видим в списках)*/
    private BluetoothDevice mDevice;

    /**В конструкторе сразу проверяю, поддерживается устройством БТ и включен ли (если выключен — включаю)*/
    public BluetoothHelper(Activity activity) {
        this.activity = activity;
        checkBluetooth();
    }

    /**В конструкторе сразу проверяю, поддерживается устройством БТ и включен ли (если выключен — включаю)
     * Сразу назначаю список ранее подключенных и список найденных устройств. Если не нужен какой-то из списков, назначить null
     * Хелпер при получении данных будет сам обновлять списки, при этом recycler подписанный (observe) на этот список будет автоматом заполняться этими новыми данными*/
    /*public BluetoothHelper(Activity activity, MutableLiveData<ArrayList<BluetoothDevice>> btPairedDeviceList, MutableLiveData<ArrayList<BluetoothDevice>> btFoundDeviceList) {
        this.activity = activity;
        this.btPairedDeviceList = btPairedDeviceList;
        this.btFoundDeviceList = btFoundDeviceList;
        checkBluetooth();
    }*/
    public BluetoothHelper(Activity activity,
                           MutableLiveData<ArrayList<BluetoothDevice>> btPairedDeviceList,
                           MutableLiveData<ArrayList<BluetoothDevice>> btFoundDeviceList,
                           MutableLiveData<Boolean> isBtSearch,
                           MutableLiveData<Boolean> isBtConnected) {
        this.activity = activity;
        this.btPairedDeviceList = btPairedDeviceList;
        this.btFoundDeviceList = btFoundDeviceList;
        this.isBtSearch = isBtSearch;
        this.isBtConnected = isBtConnected;
        checkBluetooth();
    }

    /**Установить пароль для связи с устройством. Если не задан, будет использоваться пароль по умолчанию: "0000"*/
    public void setKeyPin(String newKey) {
        mBroadcastReceiver.setKeyPin(newKey);
    }

    /**Проверяем, поддерживает ли устройство БТ и, если поддерживает включает БТ, если он не включен*/
    private void checkBluetooth() {
        Log.e(TAG, "startBluetooth: ");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 0);
            }

            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.BLUETOOTH_SCAN}, 0);
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Log.e(TAG, "Device doesn't support Bluetooth");
            Toast.makeText(activity, "Устройство не поддерживает Bluetooth", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!bluetoothAdapter.isEnabled()) {
            Log.e(TAG, "startBluetooth: !bluetoothAdapter.isEnabled()");
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            Log.e(TAG, "bluetoothAdapter.isEnabled()");
            registerBroadcast();
        }
    }

    public BluetoothAdapter getBluetoothAdapter() {
        return bluetoothAdapter;
    }

    public void connectToDevice(BluetoothDevice device) {
        mDevice = device;
        bluetoothAdapter.cancelDiscovery();
        Log.e(TAG, "соединяемся...");

        if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
            device.createBond();
        } else {
            Log.e(TAG, "♣connectToDevice: " + device.getName());
            connect(device);
        }
    }

    public void startDiscovery() {
        if (bluetoothAdapter.isDiscovering()) bluetoothAdapter.cancelDiscovery();
        btFoundDeviceList = new MutableLiveData<>();//обнуление списка
        Log.e(TAG, "♦♦♦startDiscovery: ");
        bluetoothAdapter.startDiscovery();
    }

    public void startDiscovery(MutableLiveData<ArrayList<BluetoothDevice>> btFoundDeviceList) {
        this.btFoundDeviceList = btFoundDeviceList;
        bluetoothAdapter.startDiscovery();
    }

    private void registerBroadcast() {
        mBroadcastReceiver = new BluetoothBroadCastReceiver(btFoundDeviceList);//передаю ссылку на мьютабл во внутренний класс
        this.activity.registerReceiver(mBroadcastReceiver, mBroadcastReceiver.getFilter());
    }

    /**Метод для добавления имен или частей имен разрешенных устройств.
     * Если добавить "БТДУ3", то в список будут добавляться только устройства, имена которых содержат такую строку
     * Если не добавлять ничего, то в список будут добавляться любые устройства*/
    public void setNameFilter(String...names) {
        availableNames = new ArrayList<>();
        Collections.addAll(availableNames, names);
    }

    /**В MutableLiveData<Boolean> передается булевое значение состояния окончания поиска устройств.
     * Т.е. если true, значит поиск завершен (можно отключать индикатор поиска, например)*/
    public void setDiscoveryStartedListener(MutableLiveData<Boolean> discoveryStartedFlag) {
        mBroadcastReceiver.setDiscoveryStartedFlag(discoveryStartedFlag);
    }

    /**Не забыть отключить ресивер (может при закрытии диалога поиска?)*/
    public void unregisterReceiver() {
        try {
            bluetoothAdapter.cancelDiscovery();//todo??? повтор
            this.activity.unregisterReceiver(mBroadcastReceiver);
            Log.e(TAG, "***************** UNREGISTER BLUETOOTH CLASS INNER RECEIVER");
        } catch (Exception e) {
            Log.e(TAG, "unregisterReceiver: Exception e");
        }
    }

    /**список ранее подключенных устройств заносится в Мьютабл Сет, который был передан в конструкторе.
     * */
    public void queryPairedDevices() {
        ArrayList<BluetoothDevice> list = new ArrayList<>();
        for (BluetoothDevice dev : bluetoothAdapter.getBondedDevices()) {
            if (isAvailableName(dev.getName()))list.add(dev);
        }
        btPairedDeviceList.setValue(list);
    }

    /**Вывожу в лог список ранее подключенных устройств, в списке отображаются только разрешенные устройства*/
    public void showDeviceLog() {
        ArrayList<BluetoothDevice> devices = btPairedDeviceList.getValue();
        if (devices!=null&&devices.size() > 0) {
            // There are paired devices. Get the name and address of each paired device.
            Log.e(TAG, "---------------------------------------------------------------");
            for (BluetoothDevice device : devices) {
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
                Log.e(TAG, "PairedDevices: name - "+deviceName + " address - "+ deviceHardwareAddress);
            }
            Log.e(TAG, "---------------------------------------------------------------");
        }
    }

    /**Альтернативный вариант для варианта конструктора с одним параметром. Пока не буду использовать*/
    public void queryPairedDevices(MutableLiveData<ArrayList<BluetoothDevice>> btPairedDeviceSet) {
        btPairedDeviceSet.setValue(new ArrayList<>(bluetoothAdapter.getBondedDevices()));
    }

    /**Список ранее подключенных устройств*/
    public ArrayList<BluetoothDevice> getPairedDevicesSet() {
        return new ArrayList<>(bluetoothAdapter.getBondedDevices());
    }

    /**Метод для проверки имен или частей имен разрешенных устройств.*/
    private boolean isAvailableName(String deviceName) {
        if (availableNames==null || availableNames.size() == 0) return true;
        for (String s:availableNames) {
            if (deviceName.contains(s)) return true;
            else Log.e(TAG, "такие не нужны! ("+deviceName+")");
        }
        return false;
    }


    private Adapter mAdapter;

    public Adapter getAdapter() {
        return mAdapter;
    }

    public void disconnect() {
        if (mAdapter!=null) mAdapter.disconnect();
    }

    public void startConnect() {
        Log.e(TAG, "♦startConnect: ..........");
        ExecutorService mMainExecutor = Executors.newSingleThreadExecutor();
        mMainExecutor.execute(new ConnectionThread());
    }

    class ConnectionThread extends Thread {

        @Override
        public void run() {
            boolean isConnected = false;
            while (!isConnected) {
                isConnected = mAdapter.connect();
                Log.e(TAG, "♦♦♦ isConnected: " + isConnected);
                if (!isConnected) {
                    mAdapter.disconnect();
                    try {Thread.sleep(3000);}
                    catch (InterruptedException e) {return;}
                }
            }
            isBtConnected.postValue(true);
            isBtSearch.postValue(false);
            //todo ready();
        }
    }
    public void start(BluetoothDevice device) {
        mAdapter = new Adapter(ModbusTransportFactory.getTransport(device));
        startConnect();
    }




    /**Если сервис не задан, (ViewModel общается с Сервисом через BroadcastReceiver) шлем броадкаст.
     * Если задан, напрямую сервису передаем*/
    private void connect(BluetoothDevice bluetoothDevice) {
        bluetoothAdapter.cancelDiscovery();//todo??? повтор
        saveDeviceAddress(bluetoothDevice.getAddress());
        start(bluetoothDevice);
    }

    private void saveDeviceAddress(String address) {

        SaveLoad.save(LAST_ADDRESS, address);
    }

    private String loadDeviceAddress() {
        return SaveLoad.loadString(LAST_ADDRESS);
    }

    //TODO объединить connectToLast и connectToDevice
    public void connectToLast() {
        String address = loadDeviceAddress();
        if (!address.equals("")) {
            BluetoothDevice device = bluetoothAdapter.getRemoteDevice(address);
            mDevice = device;
            //обязательный IF: Без него после перезагрузки телефона в выскакивающей месаге о
            // сопряжении ЕСЛИ НЕ СОГЛАСИТЬСЯ, то к БТДУ больше нельзя будет подключиться, следующая
            // месага появится только после перезагрузки телефона
            if (mDevice.getBondState() != BluetoothDevice.BOND_BONDED) mDevice.createBond();
            else connect(device);
        }
    }

    public String getDeviceName() {
        return mDevice.getName();
    }

    public void cancelDiscovery() {
        bluetoothAdapter.cancelDiscovery();
    }

//--------------------------------------------------------------------------------------------------

    private class BluetoothBroadCastReceiver extends BroadcastReceiver {

        private final IntentFilter filter;
        private final MutableLiveData<ArrayList<BluetoothDevice>> btFoundDeviceList;
        private MutableLiveData<Boolean> discoveryStartedFlag;
        private String keyPin;

        private BluetoothBroadCastReceiver(MutableLiveData<ArrayList<BluetoothDevice>> btFoundDeviceList) {
            filter = new IntentFilter();
            filter.addAction(BluetoothDevice.ACTION_FOUND);
            filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
            filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
            filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
            filter.addAction(BluetoothDevice.ACTION_PAIRING_REQUEST);

            this.btFoundDeviceList = btFoundDeviceList;
            this.discoveryStartedFlag = new MutableLiveData<>();
            this.keyPin = "0000"; //пароль по умолчанию
        }

        public void setKeyPin(String key) {
            this.keyPin = key;
        }

        public void setDiscoveryStartedFlag(MutableLiveData<Boolean> discoveryStartedFlag) {
            this.discoveryStartedFlag = discoveryStartedFlag;
        }

        private IntentFilter getFilter() {
            return filter;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                Log.e(TAG, "- ACTION_FOUND -");
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                addToFoundList(device);
            } else if (BluetoothDevice.ACTION_PAIRING_REQUEST.equals(action)) {
                Log.e(TAG, "- ACTION_PAIRING_REQUEST -");
                BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                bluetoothDevice.setPin(keyPin.getBytes());
            } else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                Log.e(TAG, "- ACTION_BOND_STATE_CHANGED -");
                BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                switch (bluetoothDevice.getBondState()) {
                    case BluetoothDevice.BOND_BONDED:
                        Log.e(TAG, "- BOND_BONDED -");
                        connect(bluetoothDevice);
                        break;
                    case BluetoothDevice.BOND_NONE:
                        Log.e(TAG, "- BOND_NONE -");
                        connect(bluetoothDevice);
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                Log.e(TAG, "onReceive: FINISHED");
                discoveryStartedFlag.setValue(false);
            } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                Log.e(TAG, "onReceive: STARTED");
                discoveryStartedFlag.setValue(true);
            }
        }



        private void addToFoundList(BluetoothDevice device) {
            Log.e(TAG, "addToFoundList: "+device.getName());
            if (device.getName()==null) return;
            if (btFoundDeviceList==null) return;
            ArrayList<BluetoothDevice> list;
            if (btFoundDeviceList.getValue()==null) list = new ArrayList<>();
            else list = btFoundDeviceList.getValue();
            if (list.contains(device)) {
                Log.e(TAG, "есть такой уже!");
                return;
            }
            if (isAvailableName(device.getName())) {
                list.add(device);
                btFoundDeviceList.setValue(list);
            }
        }
    }

}

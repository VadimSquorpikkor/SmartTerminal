package com.atomtex.smartterminal;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.os.Environment;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.atomtex.smartterminal.command.AnyCommand;
import com.atomtex.smartterminal.exception.ConnectingException;
import com.atomtex.smartterminal.exception.ResponseException;

import java.util.ArrayList;

public class MainViewModel extends ViewModel {

    public static final String OUTER_SIGN = ">> ";
    public static final String INNER_SIGN = "<< ";

    private final MutableLiveData<ArrayList<BluetoothDevice>> btPairedDeviceList;
    private final MutableLiveData<ArrayList<BluetoothDevice>> btFoundDeviceList;
    private final MutableLiveData<Boolean>  isDiscovering;
    private final MutableLiveData<Boolean>  isBtSearch;
    private final MutableLiveData<Boolean>  isBtConnected;
    private final MutableLiveData<Boolean>  isConnecting;
    private final MutableLiveData<Integer>  shareDialog;
    private final MutableLiveData<Boolean>  isWrongInput;
    private final MutableLiveData<String> requestText;
//    private final MutableLiveData<String> response;
    private final MutableLiveData<ArrayList<FavCommand>> favList;

    private BluetoothHelper bluetoothHelper;

    private MutableLiveData<ArrayList<String>> allCommandsList;

    public MainViewModel() {
        this.btPairedDeviceList = new MutableLiveData<>();
        this.btFoundDeviceList  = new MutableLiveData<>();
        this.isDiscovering      = new MutableLiveData<>(false);
        this.isConnecting       = new MutableLiveData<>(false);
        this.isBtSearch         = new MutableLiveData<>(false);
        this.isBtConnected      = new MutableLiveData<>(false);
        this.requestText        = new MutableLiveData<>("");
        this.allCommandsList    = new MutableLiveData<>(new ArrayList<>());
        this.isWrongInput       = new MutableLiveData<>(false);
        this.shareDialog        = new MutableLiveData<>(-1);
        this.favList            = new MutableLiveData<>();
        loadFavList();
    }

    public MutableLiveData<ArrayList<String>> getAllCommandsList() {
        return allCommandsList;
    }

    private void updateReceivingList(String s) {
        ArrayList<String> list = allCommandsList.getValue();
        list.add(s);
        allCommandsList.setValue(list);
    }

    public MutableLiveData<Boolean> getIsConnecting() {
        return isConnecting;
    }
    public BluetoothHelper getBluetoothHelper() {
        return bluetoothHelper;
    }
    public MutableLiveData<String> getRequestText() {
        return requestText;
    }
    public MutableLiveData<Boolean> getIsWrongInput() {
        return isWrongInput;
    }
    public MutableLiveData<Integer> getShareDialog() {
        return shareDialog;
    }
    public MutableLiveData<ArrayList<FavCommand>> getFavList() {
        return favList;
    }
    //    public MutableLiveData<String> getResponse() {
//        return response;
//    }

    //0x50 0x04 0x00 0x0d 0x00 0x03

    public void sendCommand(String prefix) {
        String pref = "";
        if (prefix.length()%2==0) pref = prefix;
        if (stringCommand.equals("")||stringCommand.length()<4) {
            isWrongInput.setValue(true);
            return;
        }
        if (stringCommand.length()%2!=0) {
            isWrongInput.setValue(true);
            return;
        }
        Log.e("TAG", "sendCommand: "+stringCommand);
        updateReceivingList(OUTER_SIGN+pref+" "+requestText.getValue());
        byte[] byteCommand = HexTranslate.hexStringToByteArray(pref+stringCommand);
        try {
            new AnyCommand() {
                @Override
                public void onResponse(byte[] response) {
                    updateReceivingList(INNER_SIGN+HexTranslate.byteArrayToHexString(response));
                }
            }.execute(bluetoothHelper.getAdapter(), byteCommand, true);}
        catch (ConnectingException | ResponseException e) {e.printStackTrace();}
    }

    //---------------------- BLUETOOTH -----------------------------------------------------------------
    /**Запуск поиска устройств bluetooth. Можно в фильтре указать имена устройств, которые будут
     * отображаться в результате поиска, все устройства, в имени которых не содержатся такие строки,
     * будут игнорироваться. Если ничего не выбрано — отображаются все найденные устройства*/
    public void startBluetoothSearch() {
        Log.e("TAG", "startBluetoothSearch: ");
        bluetoothHelper.disconnect();
        getIsBtSearch().postValue(true);
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
        bluetoothHelper.setConnectingListener(isConnecting);
        startConnectToLastBTDevice();
    }


    public void stopBluetoothSearch() {
        bluetoothHelper.cancelDiscovery();
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
        System.exit(0);
    }

    private String stringCommand = "";

    public void addNumber(String n) {
        isWrongInput.setValue(false);
        stringCommand += n;
//        if (stringCommand.length()%2==0) stringCommand+=" ";
        requestText.setValue(convertStringToLooksLikeCommand(stringCommand));
    }

    private String convertStringToLooksLikeCommand(String s) {
        if (s.length()<3) return s;
        String out = "";
        for (int i = 0; i < s.length(); i++) {
            if (i%2==0&&i!=0) out+=" "+s.charAt(i);
            else out+=s.charAt(i);
        }
        return out;
    }

    public void clearText() {
        requestText.setValue("");
        stringCommand = "";
    }

    public void clickOnCommandList(int position) {
        String command = allCommandsList.getValue().get(position);
        if (command.startsWith(INNER_SIGN)) return;//ответ не нужно копировать в ввод
        command = command.replace(OUTER_SIGN, "");
        command = command.replace(" ", "");
        clearText();
        addNumber(command);
    }

    public void longClickOnCommandList(int position) {
        shareDialog.setValue(position);
    }

    public void loadFavList() {
        favList.setValue(SaveLoadFav.parseFile(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Command.txt"));
    }

}

package com.atomtex.smartterminal;

import android.app.Dialog;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SearchDeviceDialog extends BaseDialog{

    private BluetoothDevicesAdapter pairedAdapter;
    private BluetoothDevicesAdapter foundAdapter;

    public SearchDeviceDialog() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeWithVM(R.layout.dialog_bluetooth);

        Button searchButton = view.findViewById(R.id.button_search);
        searchButton.setOnClickListener(v->mViewModel.startBluetoothSearch());
        Button stopButton = view.findViewById(R.id.button_stop);
        stopButton.setOnClickListener(v->mViewModel.stopBluetoothSearch());
        Button closeButton = view.findViewById(R.id.button_close);
        closeButton.setOnClickListener(v->dismiss());


        RecyclerView pairedRecyclerView = view.findViewById(R.id.paired_list);
        pairedAdapter = new BluetoothDevicesAdapter();
        pairedRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        pairedRecyclerView.setAdapter(pairedAdapter);
        pairedAdapter.setOnDeviceClickListener(this::connectTo);

        RecyclerView foundRecyclerView = view.findViewById(R.id.found_list);
        foundAdapter = new BluetoothDevicesAdapter();
        foundRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        foundRecyclerView.setAdapter(foundAdapter);
        foundAdapter.setOnDeviceClickListener(this::connectTo);

        mViewModel.getBTPairedDeviceList().observe(this, list1 -> pairedAdapter.setList(list1));
        mViewModel.getBTFoundDeviceList().observe(this, list -> foundAdapter.setList(list));

        ProgressBar progressBar = view.findViewById(R.id.progressBar);
        mViewModel.getIsDiscovering().observe(this, indeterminate -> {
            progressBar.setIndeterminate(indeterminate);
            searchButton.setVisibility(indeterminate? View.GONE:View.VISIBLE);
            stopButton.setVisibility(indeterminate? View.VISIBLE:View.GONE);
        });


        mViewModel.startBluetoothSearch();

        return dialog;
    }

    private void connectTo(BluetoothDevice device) {
        mViewModel.connectToDevice(device);
        dismiss();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mViewModel.stopBluetoothSearch();
    }

}

package com.atomtex.smartterminal.adapter;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.atomtex.smartterminal.R;

import java.util.ArrayList;

public class BluetoothDevicesAdapter extends RecyclerView.Adapter<BluetoothDevicesAdapter.AdapterViewHolder>{

    ArrayList<BluetoothDevice> deviceList = new ArrayList<>();

    public BluetoothDevicesAdapter() {
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setList(ArrayList<BluetoothDevice> deviceList) {
        if (deviceList==null) deviceList = new ArrayList<>();
        this.deviceList = deviceList;
        notifyDataSetChanged();
    }

    private OnItemClickListener onItemClickListener;
    private OnDeviceClickListener onDeviceClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public interface OnDeviceClickListener {
        void onDeviceClick(BluetoothDevice device);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnDeviceClickListener(OnDeviceClickListener onDeviceClickListener) {
        this.onDeviceClickListener = onDeviceClickListener;
    }

    @NonNull
    @Override
    public AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bt_found, parent, false);
        return new AdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterViewHolder holder, int position) {
        BluetoothDevice device = deviceList.get(position);
        holder.name.setText(device.getName());
        holder.address.setText(device.getAddress());
    }

    @Override
    public int getItemCount() {
        return deviceList.size();
    }

    public class AdapterViewHolder extends RecyclerView.ViewHolder {
        private final TextView name;
        private final TextView address;

        public AdapterViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.dev_name);
            address = itemView.findViewById(R.id.dev_address);

            /**Если задан ItemClickListener, то клик по пункту списка возвращает номер позиции;
             * если задан DeviceClickListener, то клик по пункту списка возвращает номер позиции возвращает объект BluetoothDevice соответствующий позиции в списке*/
            itemView.setOnClickListener(view -> {
                if (onItemClickListener != null) onItemClickListener.onItemClick(getAdapterPosition());
                if (onDeviceClickListener != null) onDeviceClickListener.onDeviceClick(deviceList.get(getAdapterPosition()));
            });
        }
    }
}

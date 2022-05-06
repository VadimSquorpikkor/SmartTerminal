package com.atomtex.smartterminal.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.atomtex.smartterminal.FavCommand;
import com.atomtex.smartterminal.R;

import java.util.ArrayList;

public class FavoriteCommandListAdapter extends RecyclerView.Adapter<FavoriteCommandListAdapter.AdapterViewHolder>{

    ArrayList<FavCommand> commands = new ArrayList<>();

    public FavoriteCommandListAdapter() {
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setList(ArrayList<FavCommand> commands) {
        if (commands==null) commands = new ArrayList<>();
        this.commands = commands;
        notifyDataSetChanged();
    }

    private OnItemClickListener onItemClickListener;
    private OnDeviceClickListener onDeviceClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public interface OnDeviceClickListener {
        void onDeviceClick(FavCommand favCommand);
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite, parent, false);
        return new AdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterViewHolder holder, int position) {
        FavCommand favCommand = commands.get(position);
        holder.command.setText(favCommand.getCommand());
        holder.description.setText(favCommand.getDescription());
    }

    @Override
    public int getItemCount() {
        return commands.size();
    }

    public class AdapterViewHolder extends RecyclerView.ViewHolder {
        private final TextView command;
        private final TextView description;

        public AdapterViewHolder(View itemView) {
            super(itemView);
            command = itemView.findViewById(R.id.command);
            description = itemView.findViewById(R.id.description);

            /**Если задан ItemClickListener, то клик по пункту списка возвращает номер позиции;
             * если задан DeviceClickListener, то клик по пункту списка возвращает номер позиции возвращает объект BluetoothDevice соответствующий позиции в списке*/
            itemView.setOnClickListener(view -> {
                if (onItemClickListener != null) onItemClickListener.onItemClick(getAdapterPosition());
                if (onDeviceClickListener != null) onDeviceClickListener.onDeviceClick(commands.get(getAdapterPosition()));
            });
        }
    }
}

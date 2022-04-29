package com.atomtex.smartterminal.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.atomtex.smartterminal.R;

import java.util.ArrayList;

public class CommandListAdapter extends RecyclerView.Adapter<CommandListAdapter.AdapterViewHolder>{

    private ArrayList<String> list = new ArrayList<>();

    public CommandListAdapter() {
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    public interface OnItemLongClickListener {
        void onItemLongClick(int position);
    }
    /**Лисенер, который будет возвращать позицию выбранного элемента*/
    private OnItemClickListener onItemClickListener;
    /**Сеттер на лисенер1*/
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    /**Лисенер, который будет возвращать позицию выбранного элемента*/
    private OnItemLongClickListener onItemLongClickListener;
    /**Сеттер на лисенер2*/
    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }


    @SuppressLint("NotifyDataSetChanged")
    public void setList(ArrayList<String> list) {
        if (list==null) list = new ArrayList<>();
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_commands, parent, false);
        return new AdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterViewHolder holder, int position) {
        String s = list.get(position);
        holder.comm.setText(s);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class AdapterViewHolder extends RecyclerView.ViewHolder {

        private final TextView comm;

        public AdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            comm = itemView.findViewById(R.id.comm);

            //Если задан ItemClickListener, то клик по пункту списка возвращает номер позиции
            itemView.setOnClickListener(view -> {
                if (onItemClickListener != null) onItemClickListener.onItemClick(getAdapterPosition());
            });
            itemView.setOnLongClickListener(view -> {
                if (onItemLongClickListener != null) onItemLongClickListener.onItemLongClick(getAdapterPosition());
                return false;
            });
        }
    }
}

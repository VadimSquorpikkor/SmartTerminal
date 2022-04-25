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

    public static class AdapterViewHolder extends RecyclerView.ViewHolder {

        private final TextView comm;

        public AdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            comm = itemView.findViewById(R.id.comm);

        }
    }
}

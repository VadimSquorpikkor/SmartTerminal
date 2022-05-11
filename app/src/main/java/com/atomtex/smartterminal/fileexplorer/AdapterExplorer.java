package com.atomtex.smartterminal.fileexplorer;

import static com.atomtex.smartterminal.fileexplorer.ExplorerViewModel.UP_LEVEL_STRING;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.atomtex.smartterminal.R;

import java.util.ArrayList;

class AdapterExplorer extends RecyclerView.Adapter<AdapterExplorer.AdapterViewHolder> {

    private ArrayList<FileInfo> list = new ArrayList<>();

    @SuppressLint("NotifyDataSetChanged")
    public void setList(ArrayList<FileInfo> list) {
        if (list==null) this.list = new ArrayList<>();
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_file_explorer, parent, false);
        return new AdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterViewHolder holder, int position) {
        FileInfo info = list.get(position);

        holder.icon.setImageResource(getIcon(info));
        holder.name.setText(info.getFileName());
        holder.checkBox.setVisibility(info.isChecked()?View.VISIBLE:View.GONE);
        holder.details.setText(info.getFileName().equals(UP_LEVEL_STRING)?"":info.isFolder()?"Folder":info.getFileSize());

    }

    private int getIcon(FileInfo info) {
        if (info.isFolder()){ return R.drawable.explorer_folder_open;}
        else {
            if (info.getFileName().equals(UP_LEVEL_STRING)) return R.drawable.explorer_arrow_back;
            return R.drawable.explorer_insert_drive_file;
        }

        //TODO добавить иконки по расширению (String name = option.getName().toLowerCase();
        //                    if (name.endsWith(Constants.XLS)
        //                            || name.endsWith(Constants.XLSX))
        //                        return R.drawable.xls;)
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnItemClickListener {
        void onItemClick(FileInfo fileInfo);
    }

    /**Лисенер, который будет возвращать позицию выбранного элемента*/
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class AdapterViewHolder extends RecyclerView.ViewHolder {

        private final ImageView icon;
        private final TextView name;
        private final TextView details;
        private final ImageView checkBox;

        public AdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            icon = itemView.findViewById(R.id.icon);
            name = itemView.findViewById(R.id.name);
            details = itemView.findViewById(R.id.details);
            checkBox = itemView.findViewById(R.id.check_box);

            itemView.setOnClickListener(view -> {
                if (onItemClickListener != null)
                    onItemClickListener.onItemClick(list.get(getAdapterPosition()));
            });
        }
    }
}

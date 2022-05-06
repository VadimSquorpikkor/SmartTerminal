package com.atomtex.smartterminal.dialog;

import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.atomtex.smartterminal.FavCommand;
import com.atomtex.smartterminal.R;
import com.atomtex.smartterminal.adapter.FavoriteCommandListAdapter;

public class FavoriteListDialog extends BaseDialog{

    public FavoriteListDialog() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeWithVM(R.layout.dialog_favorite);

        RecyclerView foundRecyclerView = view.findViewById(R.id.recycler);
        FavoriteCommandListAdapter adapter = new FavoriteCommandListAdapter();
        foundRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        foundRecyclerView.setAdapter(adapter);
        adapter.setOnDeviceClickListener(this::addCommand);
        mViewModel.getFavList().observe(this, adapter::setList);

        view.findViewById(R.id.button_close).setOnClickListener(v->dismiss());

        return dialog;
    }

    private void addCommand(FavCommand favCommand) {
        String comm = favCommand.getCommand();
        comm = comm.replace(" ", "");
        mViewModel.clearText();
        mViewModel.addNumber(comm);
        dismiss();
    }

}

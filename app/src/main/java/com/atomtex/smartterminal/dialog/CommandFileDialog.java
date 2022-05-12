package com.atomtex.smartterminal.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.atomtex.smartterminal.FavCommand;
import com.atomtex.smartterminal.R;
import com.atomtex.smartterminal.SaveLoad;
import com.atomtex.smartterminal.adapter.FavoriteCommandListAdapter;

public class CommandFileDialog extends BaseDialog{

    public CommandFileDialog() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeWithVM(R.layout.dialog_command);

        RecyclerView foundRecyclerView = view.findViewById(R.id.recycler);
        FavoriteCommandListAdapter adapter = new FavoriteCommandListAdapter();
        foundRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        foundRecyclerView.setAdapter(adapter);
        adapter.setOnDeviceClickListener(this::addCommand);
        mViewModel.getCommandList().observe(this, adapter::setList);

        view.findViewById(R.id.button_close).setOnClickListener(v->dismiss());

        //чекбокс "Открыть автоматом": если активировать, то при последующем тапе по кнопке
        //"Открыть" открывается не проводник, а сразу открывается и парсится последний открытый файл
        CheckBox checkBox = view.findViewById(R.id.check_set);
        checkBox.setChecked(SaveLoad.getPrefBoolean(R.string.pref_command_load));
        checkBox.setOnCheckedChangeListener((compoundButton, b) -> mViewModel.setAutoOpenFile(b));

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

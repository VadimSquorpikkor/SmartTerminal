package com.atomtex.smartterminal;

import android.content.Context;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

/**Базовый класс для диалогов. Есть варианты с ViewModel и без*/
class BaseDialog extends DialogFragment {

    Context mContext;
    MainViewModel mViewModel;
    View view;
    AlertDialog dialog;

    @Override
    public void onAttach(@NonNull Context context) {
        mContext = context;
        super.onAttach(context);
    }

    /**Добавляет диалогу лэйаут и задает параметры*/
    @SuppressWarnings("unused")
    public void initialize(int layout) {
        dialog = new AlertDialog.Builder(mContext).create();
        Window window = dialog.getWindow();
        view = requireActivity().getLayoutInflater().inflate(layout, null);
        dialog.setView(view, 0, 0, 0, 0);
    }

    /**Дать диалогу лэйаут и задать параметры. Добавление ViewModel*/
    public void initializeWithVM(int layout) {
        dialog = new AlertDialog.Builder(mContext).create();
        view = requireActivity().getLayoutInflater().inflate(layout, null);
        dialog.setView(view, 0, 0, 0, 0);
        mViewModel = new ViewModelProvider((MainActivity) mContext).get(MainViewModel.class);
    }
}

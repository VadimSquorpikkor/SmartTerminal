package com.atomtex.smartterminal.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import com.atomtex.smartterminal.R;

public class ShareCommandDialog extends BaseDialog {

    public ShareCommandDialog() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeWithVM(R.layout.dialog_share);

        int position = mViewModel.getShareDialog().getValue();

        view.findViewById(R.id.button_cancel).setOnClickListener(view -> {
            mViewModel.getShareDialog().postValue(-1);
            dismiss();
        });


        return dialog;
    }

}

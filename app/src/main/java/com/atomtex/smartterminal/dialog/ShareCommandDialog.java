package com.atomtex.smartterminal.dialog;

import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.atomtex.smartterminal.R;

public class ShareCommandDialog extends BaseDialog {

    public static final String COMMAND = "command";

    private String commandText = "";

    public static ShareCommandDialog newInstance(String command) {
        ShareCommandDialog instance = new ShareCommandDialog();
        Bundle bundle = new Bundle();
        bundle.putString(COMMAND, command);
        instance.setArguments(bundle);
        return instance;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeWithVM(R.layout.dialog_share);
        if (getArguments()!=null) commandText = getArguments().getString(COMMAND, "");

        view.findViewById(R.id.button_share).setOnClickListener(v->share(getCommand()));
        view.findViewById(R.id.button_input).setOnClickListener(v->input(getCommand()));
        view.findViewById(R.id.button_favorite).setOnClickListener(v->favorite(getCommand()));
        view.findViewById(R.id.button_copy).setOnClickListener(v->copy(getCommand()));

        return dialog;
    }

    private void copy(String command) {
        ClipboardManager clipboard = (android.content.ClipboardManager) requireActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", command);
        clipboard.setPrimaryClip(clip);
        dismiss();
    }

    private void favorite(String command) {

    }

    private void input(String command) {
        mViewModel.insertCommandToInput(command);
        dismiss();
    }

    private void share(String command) {
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "share_subject");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, command);
        startActivity(Intent.createChooser(intent, "R.string.share_using"));
        dismiss();
    }

    private String getCommand() {
        return commandText;
    }
}

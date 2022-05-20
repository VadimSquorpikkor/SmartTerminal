package com.atomtex.smartterminal.dialog;

import static android.content.ClipDescription.MIMETYPE_TEXT_PLAIN;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

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

        showCommandText(commandText);

        view.findViewById(R.id.button_share).setOnClickListener(v->share(getCommand()));
        view.findViewById(R.id.button_input).setOnClickListener(v->input(getCommand()));
        view.findViewById(R.id.button_favorite).setOnClickListener(v->favorite(getCommand()));
        view.findViewById(R.id.button_copy).setOnClickListener(v->copy(getCommand()));
        view.findViewById(R.id.button_paste).setOnClickListener(v->paste());

        if (getStringFromClipboard()==null||
            getStringFromClipboard().equals("")||
            !isRightCommand(getStringFromClipboard())
        ) view.findViewById(R.id.button_paste).setVisibility(View.GONE);

        return dialog;
    }

    private void showCommandText(String commandText) {
        String text;
        if (commandText.length()>30) text=commandText.substring(0, 30)+" ...";
        else text = commandText;
        ((TextView)view.findViewById(R.id.command)).setText(text);
    }

    private void copy(String command) {
        ClipboardManager clipboard = (ClipboardManager) requireActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", command);
        clipboard.setPrimaryClip(clip);
        dismiss();
    }

    private void favorite(String command) {
        AddToFavoriteDialog.newInstance(command).show(getParentFragmentManager(), null);
        dismiss();
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

    private void paste() {
        String command = getStringFromClipboard();
        if (isRightCommand(command)) mViewModel.insertCommandToInput(command);//не нужно, если команда неправильная, то  кнопка вообще не будет отображаться
        dismiss();
    }

    private boolean isRightCommand(String command) {
        if (command==null) return false;
        Log.e("TAG", "isRightCommand: [a-fA-F0-9\\s<>]* \""+command+"\" = "+command.matches("[a-fA-F0-9\\s<>]*"));
        return command.matches("[a-fA-F0-9\\s<>]*");
    }

    private String getStringFromClipboard() {
        ClipboardManager clipboard = (ClipboardManager) requireActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        // If it does contain data, decide if you can handle the data.
        if (!(clipboard.hasPrimaryClip())) return null;
        // since the clipboard has data but it is not plain text
        else if (!(clipboard.getPrimaryClipDescription().hasMimeType(MIMETYPE_TEXT_PLAIN))) return null;
        //since the clipboard contains plain text.
        else {
            ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
            // Gets the clipboard as text.
            return item.getText().toString();
        }
    }

    private String getCommand() {
        return commandText;
    }
}

package com.atomtex.smartterminal.dialog;

import static com.atomtex.smartterminal.TerminalUtils.convertStringToLooksLikeCommand;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.atomtex.smartterminal.FavCommand;
import com.atomtex.smartterminal.R;

import java.util.Locale;

public class AddToFavoriteDialog extends BaseDialog {

    public static final String COMMAND = "command";

    public static AddToFavoriteDialog newInstance(String command) {
        AddToFavoriteDialog instance = new AddToFavoriteDialog();
        Bundle bundle = new Bundle();
        bundle.putString(COMMAND, command);
        instance.setArguments(bundle);
        return instance;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeWithVM(R.layout.dialog_add_to_favorites);

        EditText comEdit = view.findViewById(R.id.command);
        EditText desEdit = view.findViewById(R.id.description);

        String commandText = "";
        if (getArguments()!=null) commandText = getArguments().getString(COMMAND, "");
        commandText = commandText.toLowerCase(Locale.ROOT);
        commandText = commandText.replace(" ", "");
        commandText = commandText.replace(">", "");
        commandText = commandText.replace("<", "");
        comEdit.setText(convertStringToLooksLikeCommand(commandText));

        view.findViewById(R.id.button_cancel).setOnClickListener(v->dismiss());
        view.findViewById(R.id.button_ok).setOnClickListener(v->saveCommand(comEdit.getText().toString(), desEdit.getText().toString()));

        return dialog;
    }

    private void saveCommand(String command, String description) {
        mViewModel.getData().addCommand(new FavCommand(command, description));
        dismiss();
    }

}

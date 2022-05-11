package com.atomtex.smartterminal.fileexplorer;

import static com.atomtex.smartterminal.fileexplorer.ExplorerViewModel.EXTRA_EXTENSION_LIST;
import static com.atomtex.smartterminal.fileexplorer.ExplorerViewModel.EXTRA_INITIAL_DIRECTORY;
import static com.atomtex.smartterminal.fileexplorer.ExplorerViewModel.EXTRA_SHOWS_FOLDER_ONLY;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.atomtex.smartterminal.R;
import com.atomtex.smartterminal.ThemeUtils;

/**
 * Параметры проводника задаются через интенты: если нужно показывать только .spe файлы:
 * intent.putExtra(EXTRA_EXTENSION_LIST, new String[]{".spe"});
 * Список расширений файлов задается массивом, т.е. может быть одно расширение или несколько
 *
 * Папка, из которой будет открываться проводник:
 * intent.putExtra(EXTRA_INITIAL_DIRECTORY, App.getSessionDir().getAbsolutePath());
 *
 * Если в проводнике нужно отображать только папки (например нужно выбрать папку):
 * intent.putExtra(EXTRA_SHOWS_FOLDER_ONLY, true);
 *
 * Пример:
 * private void loadSpectrum() {
 *         Intent intent = new Intent(requireActivity(), ExplorerActivity.class);
 *         intent.putExtra(EXTRA_EXTENSION_LIST, new String[]{".spe"});
 *         intent.putExtra(EXTRA_INITIAL_DIRECTORY, App.getSessionDir().getAbsolutePath());
 *         getSpeLauncher.launch(intent);
 * }
 * */
public class ExplorerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtils.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_explorer);

        String[] extension = getIntent().getStringArrayExtra(EXTRA_EXTENSION_LIST);
        String initDirectory = getIntent().getStringExtra(EXTRA_INITIAL_DIRECTORY);
        boolean foldersOnly = getIntent().getBooleanExtra(EXTRA_SHOWS_FOLDER_ONLY, false);

        ExplorerViewModel explorerViewModel = new ViewModelProvider(this).get(ExplorerViewModel.class);
        explorerViewModel.init(extension, initDirectory, foldersOnly);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragment_container, ExplorerFragment.class, null)
                    .commit();
        }
    }

}
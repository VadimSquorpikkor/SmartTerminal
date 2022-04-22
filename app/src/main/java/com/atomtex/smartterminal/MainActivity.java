package com.atomtex.smartterminal;

import static android.provider.Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION;
import static com.atomtex.smartterminal.App.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private MainViewModel mViewModel;
    private int thisTheme;
    private static final int REQUEST_WRITE_MEMORY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //при первом запуске установить значения настроек по умолчанию, без этого обращение к
        // настройкам будет возвращать null до тех пор, пока пользователь не зайдет в настройки (SettingsActivity)
        //todo        PreferenceManager.setDefaultValues(this, R.xml.root_preferences, false);
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        ThemeUtils.onActivityCreateSetTheme(this);
        thisTheme = ThemeUtils.getTheme();
        setContentView(R.layout.activity_main);

        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            fragment = TerminalFragment.newInstance();
            manager.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }

        if (savedInstanceState == null) check();
    }

    private void check() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                Intent intent = new Intent(ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, Uri.parse("package:" + BuildConfig.APPLICATION_ID));
                startActivity(intent);
            }
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "☻check: Permission NOT Granted!");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_MEMORY);
        } else {
            Log.e(TAG, "☻already had been granted");
            init();
        }
    }

    private void openBluetoothDialog() {
//        new SearchDeviceDialog().show(getSupportFragmentManager(), null);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_WRITE_MEMORY) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.e(TAG, "☻granted");
                init();
            }
        }
    }

    @Override
    protected void onResume() {
        if (thisTheme != ThemeUtils.getTheme()) this.recreate();
        super.onResume();
    }

    private void init() {
//        mViewModel.startBluetooth(this);
    }
}
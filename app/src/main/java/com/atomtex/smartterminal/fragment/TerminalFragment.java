package com.atomtex.smartterminal.fragment;

import static com.atomtex.smartterminal.App.TAG;
import static com.atomtex.smartterminal.fileexplorer.ExplorerViewModel.EXTRA_EXTENSION_LIST;
import static com.atomtex.smartterminal.fileexplorer.ExplorerViewModel.KEY_FILE_SELECTED;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.atomtex.smartterminal.MainViewModel;
import com.atomtex.smartterminal.R;
import com.atomtex.smartterminal.SaveLoad;
import com.atomtex.smartterminal.adapter.CommandListAdapter;
import com.atomtex.smartterminal.dialog.CommandFileDialog;
import com.atomtex.smartterminal.dialog.FavoriteListDialog;
import com.atomtex.smartterminal.dialog.SearchDeviceDialog;
import com.atomtex.smartterminal.dialog.ShareCommandDialog;
import com.atomtex.smartterminal.fileexplorer.ExplorerActivity;

public class TerminalFragment extends Fragment {

   public static TerminalFragment newInstance() {
      return new TerminalFragment();
   }

   private MainViewModel mViewModel;
   private TextView name;
   private CheckBox checkPrefix;
   private EditText editPrefix;
   private Vibrator vibe;
   private ImageView redLight, greenLight, errorInput;
   public static final int VIBE_TIME = 40;
   private ActivityResultLauncher<Intent> getFileLauncher;

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState
   ) {
      View view = inflater.inflate(R.layout.fragment_terminal, container, false);
      mViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

      vibe = (Vibrator) requireActivity().getSystemService(Context.VIBRATOR_SERVICE);

      TextView input = view.findViewById(R.id.input);
//      output = view.findViewById(R.id.output);
      name = view.findViewById(R.id.connected_name);
      redLight = view.findViewById(R.id.red_light);
      greenLight = view.findViewById(R.id.green_light);
      errorInput = view.findViewById(R.id.show_error_input);
      checkPrefix = view.findViewById(R.id.check_prefix);
      editPrefix = view.findViewById(R.id.prefix);

      view.findViewById(R.id.button_search).setOnClickListener(v -> startSearch());

      RecyclerView recycler = view.findViewById(R.id.basic_nuc_recycler);
      CommandListAdapter adapter = new CommandListAdapter();
      recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
      recycler.setAdapter(adapter);
      mViewModel.getAllCommandsList().observe(getViewLifecycleOwner(), adapter::setList);
      adapter.setOnItemClickListener(position -> mViewModel.clickOnCommandList(position));
      adapter.setOnItemLongClickListener(this::longClick);

      mViewModel.getRequestText().observe(getViewLifecycleOwner(), input::setText);
      mViewModel.getIsDiscovering().observe(getViewLifecycleOwner(), this::showDiscovering);
      mViewModel.getIsConnecting().observe(getViewLifecycleOwner(), this::showConnecting);
      mViewModel.IsBTConnected().observe(getViewLifecycleOwner(), this::showConnected);
      mViewModel.getIsWrongInput().observe(getViewLifecycleOwner(), this::setWrongInput);
      mViewModel.getShareDialog().observe(getViewLifecycleOwner(), this::openShareDialog);

      view.findViewById(R.id.button_0).setOnClickListener(view1 -> addNumber("0"));
      view.findViewById(R.id.button_1).setOnClickListener(view1 -> addNumber("1"));
      view.findViewById(R.id.button_2).setOnClickListener(view1 -> addNumber("2"));
      view.findViewById(R.id.button_3).setOnClickListener(view1 -> addNumber("3"));
      view.findViewById(R.id.button_4).setOnClickListener(view1 -> addNumber("4"));
      view.findViewById(R.id.button_5).setOnClickListener(view1 -> addNumber("5"));
      view.findViewById(R.id.button_6).setOnClickListener(view1 -> addNumber("6"));
      view.findViewById(R.id.button_7).setOnClickListener(view1 -> addNumber("7"));
      view.findViewById(R.id.button_8).setOnClickListener(view1 -> addNumber("8"));
      view.findViewById(R.id.button_9).setOnClickListener(view1 -> addNumber("9"));
      view.findViewById(R.id.button_a).setOnClickListener(view1 -> addNumber("a"));
      view.findViewById(R.id.button_b).setOnClickListener(view1 -> addNumber("b"));
      view.findViewById(R.id.button_c).setOnClickListener(view1 -> addNumber("c"));
      view.findViewById(R.id.button_d).setOnClickListener(view1 -> addNumber("d"));
      view.findViewById(R.id.button_e).setOnClickListener(view1 -> addNumber("e"));
      view.findViewById(R.id.button_f).setOnClickListener(view1 -> addNumber("f"));
      view.findViewById(R.id.button_cl).setOnClickListener(view1 -> clear());
      view.findViewById(R.id.button_back).setOnClickListener(view1 -> back());
      view.findViewById(R.id.button_mem).setOnClickListener(view1 -> memory());
      view.findViewById(R.id.button_enter).setOnClickListener(v -> send());
      view.findViewById(R.id.button_open).setOnClickListener(v -> openCommandFile());

      /**Лаунчер для внутреннего проводника. Метод getFolder или getFile открывает проводник,
       * который возвращает путь выбранного в проводнике файла. Для назначения действия
       * переопределить метод pathReturnListener*/
      getFileLauncher = registerForActivityResult(
              new ActivityResultContracts.StartActivityForResult(),
              result -> {
                 if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    String path = "";
                    if (data!=null) path = data.getStringExtra(KEY_FILE_SELECTED);
                    pathReturnListener(path);
                 }
              });

      return view;
   }

   /**Запуск внутреннего проводника для получения пути выбранного файла. Можно задать расширение
    *  (расширения), файлы с которыми будут отображаться в проводнике. Для отображения всех файлов
    *  использовать метод без параметров или ввести null*/
   public void getFile(String... ext) {
      Intent intent = new Intent(requireActivity(), ExplorerActivity.class);
      intent.putExtra(EXTRA_EXTENSION_LIST, ext);
      getFileLauncher.launch(intent);
   }

   public void pathReturnListener(String path) {
      Log.e(TAG, "pathReturnListener: "+path);
      mViewModel.loadCommandList(path);
      new CommandFileDialog().show(getParentFragmentManager(), null);
   }

   private void openCommandFile() {
      vibe.vibrate(VIBE_TIME);
      if (SaveLoad.getPrefBoolean(R.string.pref_command_load)) pathReturnListener(SaveLoad.getPrefString(R.string.pref_command_path));
      else getFile("txt");
   }

   private void longClick(int position) {
      vibe.vibrate(VIBE_TIME);
      mViewModel.longClickOnCommandList(position);
   }

   private void openShareDialog(int position) {
      if (position==-1) return;
      new ShareCommandDialog().show(getParentFragmentManager(), null);
      mViewModel.getShareDialog().postValue(-1);
   }

   private void setWrongInput(boolean state) {
      errorInput.setVisibility(state?View.VISIBLE:View.GONE);
   }

   int count = 0;
   private void showConnecting(boolean state) {
      if (state) {
         count++;
         name.setText(String.format(getString(R.string.connecting), count));
      } else count = 0;
   }

   private void clear() {
      vibe.vibrate(VIBE_TIME);
      mViewModel.clearText();
   }

   private void send() {
      vibe.vibrate(VIBE_TIME);
      if (checkPrefix.isChecked()) mViewModel.sendCommand(editPrefix.getText().toString());
      else mViewModel.sendCommand("");
   }

   private void memory() {
      vibe.vibrate(VIBE_TIME);
      new FavoriteListDialog().show(getParentFragmentManager(), null);
   }

   private void back() {
      vibe.vibrate(VIBE_TIME);
      mViewModel.doBackspace();
   }

   private void addNumber(String n) {
      vibe.vibrate(VIBE_TIME);
      mViewModel.addNumber(n);
   }

   private void showConnected(boolean state) {
      setConnectedIndicator(state);
      if (state) name.setText(String.format(getString(R.string.string_connected_to), mViewModel.getBluetoothHelper().getDeviceName()));
   }

   private void setConnectedIndicator(boolean isConnected) {
      redLight.setVisibility(isConnected?View.GONE:View.VISIBLE);
      greenLight.setVisibility(isConnected?View.VISIBLE:View.GONE);
   }

   private void showDiscovering(boolean state) {
      if (state) name.setText(R.string.searching);
   }

   private void startSearch() {
      new SearchDeviceDialog().show(getParentFragmentManager(), null);
   }

}

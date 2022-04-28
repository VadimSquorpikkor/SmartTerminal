package com.atomtex.smartterminal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.atomtex.smartterminal.adapter.CommandListAdapter;

public class TerminalFragment extends Fragment {

   public static TerminalFragment newInstance() {
      return new TerminalFragment();
   }
   MainViewModel mViewModel;
   TextView name;
   Vibrator vibe;
   ImageView redLight, greenLight, errorInput;
   public static final int VIBE_TIME = 40;

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

      view.findViewById(R.id.button_search).setOnClickListener(v -> startSearch());

      RecyclerView recycler = view.findViewById(R.id.basic_nuc_recycler);
      CommandListAdapter adapter = new CommandListAdapter();
      recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
      recycler.setAdapter(adapter);
      mViewModel.getAllCommandsList().observe(getViewLifecycleOwner(), adapter::setList);

      mViewModel.getRequestText().observe(getViewLifecycleOwner(), input::setText);
      mViewModel.getIsDiscovering().observe(getViewLifecycleOwner(), this::showDiscovering);
      mViewModel.getIsConnecting().observe(getViewLifecycleOwner(), this::showConnecting);
      mViewModel.IsBTConnected().observe(getViewLifecycleOwner(), this::showConnected);
      mViewModel.getIsWrongInput().observe(getViewLifecycleOwner(), this::setWrongInput);

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


      return view;
   }

   private void setWrongInput(boolean state) {
      errorInput.setVisibility(state?View.VISIBLE:View.GONE);
   }

   int count = 0;
   @SuppressLint("SetTextI18n")
   private void showConnecting(boolean state) {
      if (state) {
         count++;
         name.setText("подключение... ["+count+"]");
      } else count = 0;
   }

   private void clear() {
      vibe.vibrate(VIBE_TIME);
      mViewModel.clearText();
   }

   private void send() {
      vibe.vibrate(VIBE_TIME);
      mViewModel.sendCommand();
   }

   private void memory() {
      vibe.vibrate(VIBE_TIME);

   }

   private void back() {
      vibe.vibrate(VIBE_TIME);

   }

   private void addNumber(String n) {
      vibe.vibrate(VIBE_TIME);
      mViewModel.addNumber(n);
   }

   private void showConnected(boolean state) {
      setConnectedIndicator(state);
      if (state) name.setText("Подключен "+mViewModel.getBluetoothHelper().getDeviceName());
   }

   private void setConnectedIndicator(boolean isConnected) {
      redLight.setVisibility(isConnected?View.GONE:View.VISIBLE);
      greenLight.setVisibility(isConnected?View.VISIBLE:View.GONE);
   }

   private void showDiscovering(boolean state) {
      if (state) name.setText("поиск...");
   }

   private void startSearch() {
      new SearchDeviceDialog().show(getParentFragmentManager(), null);
   }

}

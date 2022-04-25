package com.atomtex.smartterminal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

public class TerminalFragment extends Fragment {

   public static TerminalFragment newInstance() {
      return new TerminalFragment();
   }
   MainViewModel mViewModel;
   TextView name;
   TextView output;

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState
   ) {
      View view = inflater.inflate(R.layout.fragment_terminal, container, false);
      mViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

      TextView input = view.findViewById(R.id.input);
      output = view.findViewById(R.id.output);
      name = view.findViewById(R.id.connected_name);

      view.findViewById(R.id.button_search).setOnClickListener(v -> startSearch());


      mViewModel.getRequestText().observe(getViewLifecycleOwner(), input::setText);
      mViewModel.getResponse().observe(getViewLifecycleOwner(), this::addToOutput);
      mViewModel.getIsBtSearch().observe(getViewLifecycleOwner(), this::showSearch);
      mViewModel.IsBTConnected().observe(getViewLifecycleOwner(), this::showConnected);

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
      view.findViewById(R.id.button_cl).setOnClickListener(view1 -> mViewModel.clearText());
      view.findViewById(R.id.button_back).setOnClickListener(view1 -> back());
      view.findViewById(R.id.button_space).setOnClickListener(view1 -> space());
      view.findViewById(R.id.button_enter).setOnClickListener(v -> mViewModel.sendCommand());


      return view;
   }

   private void addToOutput(String s) {
      output.setText("<<"+s);
   }

   private void space() {
      mViewModel.addNumber(" ");
   }

   private void back() {

   }

   private void addNumber(String n) {
      mViewModel.addNumber(n);
   }

   private void showConnected(boolean state) {
      if (state) name.setText("Подключен "+mViewModel.getBluetoothHelper().getDeviceName());
   }

   private void showSearch(boolean state) {
      if (state) name.setText("поиск...");
   }

   private void startSearch() {
      new SearchDeviceDialog().show(getParentFragmentManager(), null);
   }

}

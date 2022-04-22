package com.atomtex.smartterminal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class TerminalFragment extends Fragment {

   public static TerminalFragment newInstance() {
      return new TerminalFragment();
   }

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState
   ) {
      View view = inflater.inflate(R.layout.fragment_terminal, container, false);
      MainViewModel mViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

      view.findViewById(R.id.button_search).setOnClickListener(v -> startSearch());

      return view;
   }

   private void startSearch() {
      new SearchDeviceDialog().show(getParentFragmentManager(), null);
   }

}

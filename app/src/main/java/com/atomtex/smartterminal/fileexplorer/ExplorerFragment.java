package com.atomtex.smartterminal.fileexplorer;

import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.atomtex.smartterminal.App;
import com.atomtex.smartterminal.R;

public class ExplorerFragment extends Fragment {

    private Button mBtnSDCard;
    private Button mSelectFolder;
    private View mReadOnlyText;
    private View mOptionsPanel;
    private View mOkCancel;
    private TextView mTxtSelectedFolder;
    private Button mBtnHome;
    private Button mBtnStorage;
    //    private Button mBtnConfirm;
//    private Button mBtnCancel;
//    private Button mBtnCreateFolder;
//    private View mDivider;
//    private Button mZipButton;
//    private Button mShareButton;
//    private Button mDeleteButton;
    ExplorerViewModel explorerViewModel;

    public static ExplorerFragment newInstance() {
        return new ExplorerFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explorer, container, false);

        explorerViewModel = new ViewModelProvider(requireActivity()).get(ExplorerViewModel.class);

        mBtnSDCard = view.findViewById(R.id.btnSDCard);
        mReadOnlyText = view.findViewById(R.id.read_only_msg);
        mOptionsPanel = view.findViewById(R.id.optionsPanel);
        mOkCancel = view.findViewById(R.id.footer);
        mSelectFolder = view.findViewById(R.id.button_select_folder);
        mTxtSelectedFolder = view.findViewById(R.id.txtvSelectedFolder);
        mBtnHome = view.findViewById(R.id.btnHome);
        mBtnStorage = view.findViewById(R.id.btnStorage);
//        mBtnConfirm = view.findViewById(R.id.button_ok);
//        mBtnCancel = view.findViewById(R.id.button_cancel);
//        mBtnCreateFolder = view.findViewById(R.id.btnCreateFolder);
//        mDivider = view.findViewById(R.id.divider2);
//        mZipButton = view.findViewById(R.id.zip_button);
//        mShareButton = view.findViewById(R.id.share_button);
//        mDeleteButton = view.findViewById(R.id.delete_button);

        RecyclerView recyclerView = view.findViewById(R.id.directoryList);
        AdapterExplorer adapter = new AdapterExplorer();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(fileInfo -> explorerViewModel.clickItem(fileInfo, requireActivity()));
        explorerViewModel.getFileList().observe(getViewLifecycleOwner(), adapter::setList);
        explorerViewModel.getThisFolderPath().observe(getViewLifecycleOwner(), s -> mTxtSelectedFolder.setText(s));

        explorerViewModel.getButtons().observe(getViewLifecycleOwner(), letItBe -> mOptionsPanel.setVisibility(letItBe ? View.VISIBLE : View.GONE));
        explorerViewModel.getMessage().observe(getViewLifecycleOwner(), letItBe -> mReadOnlyText.setVisibility(letItBe ? View.VISIBLE : View.GONE));
        explorerViewModel.getSdButton().observe(getViewLifecycleOwner(), letItBe -> mBtnSDCard.setVisibility(letItBe ? View.VISIBLE : View.GONE));
        explorerViewModel.getOkCancel().observe(getViewLifecycleOwner(), letItBe -> mOkCancel.setVisibility(letItBe ? View.VISIBLE : View.GONE));
        explorerViewModel.getSelectButton().observe(getViewLifecycleOwner(), letItBe -> mSelectFolder.setVisibility(letItBe ? View.VISIBLE : View.GONE));
        explorerViewModel.getObserveEvent().observe(getViewLifecycleOwner(), letItBe -> updateFolder());

        mSelectFolder.setOnClickListener(v -> explorerViewModel.returnThisFolder(requireActivity()));
        mBtnHome.setOnClickListener(v->explorerViewModel.fill(App.getMainDir()));
        mBtnStorage.setOnClickListener(v->explorerViewModel.fill(Environment.getExternalStorageDirectory()));
        return view;
    }

    private void updateFolder() {
        if (getActivity() != null /*&& event <= 512*/ /* && !mOptionsPanelIsSown*/) {
            getActivity().runOnUiThread(() -> explorerViewModel.fillCurrent());
        }
    }
}

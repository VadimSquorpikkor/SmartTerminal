package com.atomtex.smartterminal.fileexplorer;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Environment;
import android.os.FileObserver;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class ExplorerViewModel extends AndroidViewModel {

    public static final String KEY_FILE_SELECTED = "key_file_selected";
    public static final String EXTRA_EXTENSION_LIST = "extra_extension_list";
    public static final String EXTRA_INITIAL_DIRECTORY = "extra_initial_directory";
    public static final String EXTRA_SHOWS_FOLDER_ONLY = "extra_shows_folder_only";
    public static final String UP_LEVEL_STRING = "  . .";

    private boolean showsFolderOnly;
    private String[] extensionsList;
    private FileObserver mFileObserver;
    boolean alreadyInitialized;
    private File mCurrentDirectory;

    private final MutableLiveData<ArrayList<FileInfo>> fileList;
    private final MutableLiveData<Boolean> message;
    private final MutableLiveData<Boolean> buttons;
    private final MutableLiveData<Boolean> sdButton;
    private final MutableLiveData<Boolean> okCancel;
    private final MutableLiveData<Boolean> selectButton;
    private final MutableLiveData<String> thisFolderPath;
    private final MutableLiveData<Integer> observeEvent;

    public MutableLiveData<Boolean> getMessage() {
        return message;
    }
    public MutableLiveData<Boolean> getButtons() {
        return buttons;
    }
    public MutableLiveData<Boolean> getSdButton() {
        return sdButton;
    }
    public MutableLiveData<Boolean> getOkCancel() {
        return okCancel;
    }
    public MutableLiveData<Boolean> getSelectButton() {
        return selectButton;
    }
    public LiveData<ArrayList<FileInfo>> getFileList() {
        return fileList;
    }
    public MutableLiveData<String> getThisFolderPath() {
        return thisFolderPath;
    }
    public MutableLiveData<Integer> getObserveEvent() {
        return observeEvent;
    }

    public ExplorerViewModel(@NonNull Application application) {
        super(application);

        fileList = new MutableLiveData<>();
        message = new MutableLiveData<>();
        buttons = new MutableLiveData<>();
        sdButton = new MutableLiveData<>();
        okCancel = new MutableLiveData<>();
        selectButton = new MutableLiveData<>();
        thisFolderPath = new MutableLiveData<>();
        observeEvent = new MutableLiveData<>();

        message.setValue(false);
        buttons.setValue(false);
        sdButton.setValue(false);
        okCancel.setValue(false);
    }


    public void init(String[] extList, String initDirectory, boolean folderOnly) {
        Log.e("TAG", "****************init: "+initDirectory);
        if (alreadyInitialized) return;
        alreadyInitialized = true;

        extensionsList = extList;
        String newDirectoryName = "Folder";//TODO надо ли?
        String initialDirectory = initDirectory;
        showsFolderOnly = folderOnly;
        selectButton.setValue(folderOnly);//если выбрано folderOnly, показывать кнопку выбора папки
        if (initDirectory==null)initDirectory=Environment.getExternalStorageDirectory().getAbsolutePath();

        thisFolderPath.setValue(initDirectory);

        fill(new File(initDirectory));
    }

    /*if (mLogger != null) {
        new Thread(() -> mLogger.logThresholdNeutron(mNeutronThresholds)).start();
    }*/

    public void fill(File f) {
        refreshButtonState(f);
        thisFolderPath.setValue(f.getAbsolutePath());
        Log.e("TAG", "fill: "+f);
        ArrayList<FileInfo> dirs  = new ArrayList<>();
        ArrayList<FileInfo> files = new ArrayList<>();
        if (f.listFiles()==null) return;//todo как-то сообщить пользователю, что папка не открывается
        for (File file:f.listFiles()) {
            if (file.isDirectory() && !file.isHidden()) {
                dirs.add(new FileInfo(file.getName(), -1, file.getAbsolutePath(), true, false));
            } else if (!file.isHidden() && isInExtList(file) && !showsFolderOnly) {
                files.add(new FileInfo(file.getName(), file.length(), file.getAbsolutePath(), false, false));
            }
        }
        sortList(dirs);
        sortList(files);
        dirs.addAll(files);
        if (f.getParent() != null) dirs.add(0, new FileInfo(UP_LEVEL_STRING,-1, f.getParent(),false, true));
        Log.e("TAG", "*****fill: "+dirs.size());
        fileList.setValue(dirs);
        if (f != mCurrentDirectory) {
            mFileObserver = createFileObserver(f.getAbsolutePath());
            mFileObserver.startWatching();
            mCurrentDirectory = f;
        }
    }

    /**Сортировка списка в алфавитном порядке. Сортируются в не зависимости от регистра*/
    public void sortList(ArrayList<FileInfo> list) {
        //noinspection ComparatorCombinators
        Collections.sort(list, (o1, o2) -> o1.getFileName().toLowerCase().compareTo(o2.getFileName().toLowerCase()));
    }

    private void refreshButtonState(File f) {
        if (isValidFile(f)) {
            Log.e("TAG", "***Valid: ");
//            mReadOnlyText.setVisibility(View.GONE);
//            mDivider.setVisibility(View.GONE);
        } else {
            Log.e("TAG", "***Not Valid: ");//todo если
//            mReadOnlyText.setVisibility(View.VISIBLE);
//            mDivider.setVisibility(View.VISIBLE);
        }
    }

    private boolean isValidFile(final File file) {
        return (file != null && file.isDirectory() && file.canRead() && file.canWrite());
    }

    /**Если массив с расширениями == null или пустой, значит при любом расширении(ext) возвращает true},
     // иначе перебираем лист и сравниваем с каждым элементом списка*/
    boolean isInExtList(File file) {
        if (extensionsList == null || extensionsList.length == 0) return true;
        else for (String s : extensionsList) if (file.getName().endsWith(s)) return true;
        return false;
    }

    public void clickItem(FileInfo fileInfo, Activity activity) {
        if (fileInfo.isFolder() || fileInfo.isParent()) {
            fill(new File(fileInfo.getPath()));
        } else {
            File fileSelected = new File(fileInfo.getPath());
            returnPathIntent(fileSelected.getAbsolutePath(), activity);
        }
    }

    public void returnThisFolder(Activity activity) {
        returnPathIntent(thisFolderPath.getValue(), activity);
    }

    private void returnPathIntent(String path, Activity activity) {
        Intent intent = new Intent();
        intent.putExtra(KEY_FILE_SELECTED, path);
        activity.setResult(Activity.RESULT_OK, intent);
        activity.finish();
    }

    private FileObserver createFileObserver(final String path) {
        Log.e("TAG", "♦♦♦createFileObserver: START");
        return new FileObserver(path, FileObserver.CREATE | FileObserver.DELETE | FileObserver.MOVED_FROM | FileObserver.MOVED_TO) {
            @Override
            public void onEvent(final int event, final String path) {
                Log.e("TAG", "♦♦♦onEvent: " + event);
                observeEvent.postValue(event);
            }
        };

    }

    public void fillCurrent() {
        fill(mCurrentDirectory);
    }
}

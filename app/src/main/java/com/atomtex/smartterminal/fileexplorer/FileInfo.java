package com.atomtex.smartterminal.fileexplorer;

import java.util.Locale;

class FileInfo {
    private String fileName;
    private String fileSize;
    private String path;
    private boolean folder;
    private boolean parent;
    private boolean isChecked;

    FileInfo(String fileName, long fileSize, String filePath, boolean isFolder, boolean parent) {
        this.fileName = fileName;
        if (isFolder) this.fileSize = "";
        else this.fileSize = makeFileSize(fileSize);
        this.path = filePath;
        this.folder = isFolder;
        this.parent = parent;
    }

    //Переводит размер файла в нормально читаемый вид: "11856478" --> "11.30 Mb"
    String makeFileSize(long size) {
        if (size < 1000) return size + " b";
        else if (size < 1000000)
            return String.format(Locale.US, "%.2f", (double) size / 1024) + " Kb";
        else return String.format(Locale.US, "%.2f", (double) size / 1048576) + " Mb";
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileSize() {
        return fileSize;
    }

    public String getPath() {
        return path;
    }

    boolean isFolder() {
        return folder;
    }

    boolean isParent() {
        return parent;
    }

    public boolean isChecked() {
        return isChecked;
    }

    void setChecked() {
        this.isChecked = true;
    }

    void uncheck() {
        this.isChecked = false;
    }
}

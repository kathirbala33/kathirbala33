package com.myconsole.app.fragment;

import android.graphics.Bitmap;

public class FileModel {
    String fileName;
    String filePath;
    Bitmap thumbNail;
    String extension;
    String fileSize;

    public FileModel() {
    }

    public FileModel(String name, String path, Bitmap bitmap, String extension, String fileSize) {
        this.fileName = name;
        this.filePath = path;
        this.thumbNail = bitmap;
        this.extension = extension;
        this.fileSize = fileSize;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Bitmap getThumbNail() {
        return thumbNail;
    }

    public void setThumbNail(Bitmap thumbNail) {
        this.thumbNail = thumbNail;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

}

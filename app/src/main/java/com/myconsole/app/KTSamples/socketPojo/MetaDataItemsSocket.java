

package com.myconsole.app.KTSamples.socketPojo;

import com.google.gson.annotations.SerializedName;


public class MetaDataItemsSocket {

    @SerializedName("fileName")
    private String fileName;

    @SerializedName("extension")
    private String extension;

    @SerializedName("sourcefileName")
    private String sourcefileName;

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getExtension() {
        return extension;
    }

    public String getSourcefileName() {
        return sourcefileName;
    }

    public void setSourcefileName(String sourcefileName) {
        this.sourcefileName = sourcefileName;
    }

    public MetaDataItemsSocket() {

    }

    public MetaDataItemsSocket(String fileName, String extension, String sourcefileName) {
        this.fileName = fileName;
        this.extension = extension;
        this.sourcefileName = sourcefileName;
    }
}
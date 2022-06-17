package com.myconsole.app.Retrofit;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PostRequest {

    @SerializedName("documents")
    private List<DocumentsItem> documents;


    public PostRequest(List<DocumentsItem> documentsItems) {
        this.documents = documentsItems;
    }


    public List<DocumentsItem> getDocuments() {
        return documents;
    }
}
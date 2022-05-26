
package com.myconsole.app.KTSamples.socketPojo;


import com.google.gson.annotations.SerializedName;

import java.util.List;


public class MsgSendSocket {
    @SerializedName("token")
    private String token;

    @SerializedName("senderId")
    private String senderId;

    @SerializedName("recipients")
    private List<String> recipients;

    @SerializedName("content")
    private String content;

    @SerializedName("contentType")
    private String contentType;

    @SerializedName("metaData")
    private MetaDataItemsSocket metaDataItemsSocket;

    @SerializedName("envDetails")
    private EnvDetailsSocket envDetailsSocket;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("baseURL")
    private String baseUrl;

    public MsgSendSocket() {
    }

    public MsgSendSocket(String token, String senderId, List<String> recipients, String content, String contentType, MetaDataItemsSocket metaDataItemsSocket, EnvDetailsSocket envDetailsSocket, String baseApiUrl, String createdAt) {
        this.token = token;
        this.senderId = senderId;
        this.recipients = recipients;
        this.content = content;
        this.contentType = contentType;
        this.metaDataItemsSocket = metaDataItemsSocket;
        this.createdAt = createdAt;
        this.baseUrl = baseApiUrl;
        this.envDetailsSocket = envDetailsSocket;
    }
}
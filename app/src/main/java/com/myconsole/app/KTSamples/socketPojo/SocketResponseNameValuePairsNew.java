

package com.myconsole.app.KTSamples.socketPojo;

import com.google.gson.annotations.SerializedName;


public class SocketResponseNameValuePairsNew {

    @SerializedName("content")
    private String content;

    @SerializedName("organizationId")
    private String organizationId;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("senderId")
    private String senderId;

    @SerializedName("isRemoved")
    private boolean isRemoved;

    @SerializedName("messageType")
    private String messageType;

    @SerializedName("viewedBy")
    private SocketResponseViewedBy viewedBy;

    @SerializedName("recipients")
    private SocketResponseRecipients recipients;

    @SerializedName("participients")
    private SocketResponseParticipients participients;

    @SerializedName("__v")
    private int V;

    @SerializedName("_id")
    private String id;

    @SerializedName("contentType")
    private String contentType;

    @SerializedName("updatedAt")
    private String updatedAt;

    @SerializedName("metaData")
    private Object metaDataItemsSocket;

    public String getContent() {
        return content;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getMessageType() {
        return messageType;
    }

    public SocketResponseViewedBy getViewedBy() {
        return viewedBy;
    }

    public SocketResponseRecipients getRecipients() {
        return recipients;
    }

    public SocketResponseParticipients getParticipients() {
        return participients;
    }

    public int getV() {
        return V;
    }

    public String getId() {
        return id;
    }

    public String getContentType() {
        return contentType;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public boolean isRemoved() {
        return isRemoved;
    }

    public Object getMetaDataItemsSocket() {
        return metaDataItemsSocket;
    }

}
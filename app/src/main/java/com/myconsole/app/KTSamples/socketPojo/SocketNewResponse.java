

package com.myconsole.app.KTSamples.socketPojo;

import com.google.gson.annotations.SerializedName;


public class SocketNewResponse {

    @SerializedName("nameValuePairs")
    private SocketResponseNameValuePairsNew nameValuePairs;

    public SocketResponseNameValuePairsNew getNameValuePairs() {
        return nameValuePairs;
    }
}


package com.myconsole.app.KTSamples.socketPojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class SocketResponseParticipients {

    @SerializedName("values")
    private List<String> values;

    public List<String> getValues() {
        return values;
    }
}
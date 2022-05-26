

package com.myconsole.app.KTSamples.socketPojo;

import com.google.gson.annotations.SerializedName;


public class EnvDetailsSocket {

    @SerializedName("tenant_key")
    private String tenant_key;

    @SerializedName("DOMAIN_KEY")
    private String domain_key;

    @SerializedName("tenantId")
    private String tenantId;

    public void setTenant_key(String tenant_key) {
        this.tenant_key = tenant_key;
    }

    public void setDomain_key(String domain_key) {
        this.domain_key = domain_key;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public EnvDetailsSocket() {
    }
}
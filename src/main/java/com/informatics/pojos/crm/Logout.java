package com.informatics.pojos.crm;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Logout {
    @SerializedName("refreshToken")
    @Expose
    private String refreshToken;

    @SerializedName("logoutStatus")
    @Expose
    private String logoutStatus;

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getLogoutStatus() {
        return logoutStatus;
    }

    public void setLogoutStatus(String logoutStatus) {
        this.logoutStatus = logoutStatus;
    }
}

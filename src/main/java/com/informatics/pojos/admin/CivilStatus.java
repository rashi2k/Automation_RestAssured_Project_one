package com.informatics.pojos.admin;

import com.google.gson.annotations.SerializedName;

public class CivilStatus {

    @SerializedName("civilStatusId")
    private Integer civilStatusId;

    @SerializedName("civilStatus")
    private String civilStatus;

    @SerializedName("isActive")
    private Boolean isActive;

    public Integer getCivilStatusId() {
        return civilStatusId;
    }

    public void setCivilStatusId(Integer civilStatusId) {
        this.civilStatusId = civilStatusId;
    }

    public String getCivilStatus() {
        return civilStatus;
    }

    public void setCivilStatus(String civilStatus) {
        this.civilStatus = civilStatus;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }
}

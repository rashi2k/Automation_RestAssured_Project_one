package com.informatics.pojos.crm;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StatusChange {

    @SerializedName("statusId")
    private Integer statusId;

    @SerializedName("remark")
    private String remark;

    @SerializedName("restrictedTransIds")
    private List<Integer> restrictedTransIds;

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<Integer> getRestrictedTransIds() {
        return restrictedTransIds;
    }

    public void setRestrictedTransIds(List<Integer> restrictedTransIds) {
        this.restrictedTransIds = restrictedTransIds;
    }
}

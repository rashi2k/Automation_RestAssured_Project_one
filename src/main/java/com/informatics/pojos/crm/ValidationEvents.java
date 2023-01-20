package com.informatics.pojos.crm;

import com.google.gson.annotations.SerializedName;

public class ValidationEvents {

    @SerializedName("fieldId")
    private Integer fieldId;

    @SerializedName("isUsed")
    private Boolean isUsed;

    @SerializedName("isEditAllowed")
    private Boolean isEditAllowed;

    @SerializedName("isApprovalRequired")
    private Boolean isApprovalRequired;

    public Integer getFieldId() {
        return fieldId;
    }

    public void setFieldId(Integer fieldId) {
        this.fieldId = fieldId;
    }

    public Boolean getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(Boolean isUsed) {
        this.isUsed = isUsed;
    }

    public Boolean getIsEditAllowed() {
        return isEditAllowed;
    }

    public void setIsEditAllowed(Boolean isEditAllowed) {
        this.isEditAllowed = isEditAllowed;
    }

    public Boolean getIsApprovalRequired() {
        return isApprovalRequired;
    }

    public void setIsApprovalRequired(Boolean isApprovalRequired) {
        this.isApprovalRequired = isApprovalRequired;
    }
}

package com.informatics.pojos.crm;

import com.google.gson.annotations.SerializedName;

public class CustomerAdditionalInfo {

    @SerializedName("customerAdditionalInfoId")
    private Integer customerAdditionalInfoId;

    @SerializedName("additionalInfoValue")
    private String additionalInfoValue;

    @SerializedName("isActive")
    private Boolean isActive;

    @SerializedName("cmRAdditionalInfoId")
    private Integer cmRAdditionalInfoId;

    public Integer getCustomerAdditionalInfoId() {
        return customerAdditionalInfoId;
    }

    public void setCustomerAdditionalInfoId(Integer customerAdditionalInfoId) {
        this.customerAdditionalInfoId = customerAdditionalInfoId;
    }

    public String getAdditionalInfoValue() {
        return additionalInfoValue;
    }

    public void setAdditionalInfoValue(String additionalInfoValue) {
        this.additionalInfoValue = additionalInfoValue;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }

    public Integer getCmRAdditionalInfoId() {
        return cmRAdditionalInfoId;
    }

    public void setCmRAdditionalInfoId(Integer cmRAdditionalInfoId) {
        this.cmRAdditionalInfoId = cmRAdditionalInfoId;
    }
}

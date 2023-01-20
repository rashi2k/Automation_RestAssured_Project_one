package com.informatics.pojos.crm;

import com.google.gson.annotations.SerializedName;

public class TaxRegistration {

    @SerializedName("id")
    private Integer id;

    @SerializedName("taxTypeId")
    private Integer taxTypeId;

    @SerializedName("referenceNumber")
    private String referenceNumber;

    @SerializedName("isActive")
    private Boolean isActive;

    @SerializedName("customerId")
    private Integer customerId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getTaxTypeId() {
        return taxTypeId;
    }

    public void setTaxTypeId(Integer taxTypeId) {
        this.taxTypeId = taxTypeId;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

}

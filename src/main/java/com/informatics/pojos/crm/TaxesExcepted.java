package com.informatics.pojos.crm;

import com.google.gson.annotations.SerializedName;

public class TaxesExcepted {
    @SerializedName("id")
    private Integer id;

    @SerializedName("customerId")
    private Integer customerId;

    @SerializedName("taxTypeId")
    private Integer taxTypeId;

    @SerializedName("referenceNumber")
    private String referenceNumber;

    @SerializedName("from")
    private String from;

    @SerializedName("to")
    private String to;

    @SerializedName("isApprovalRequired")
    private Boolean isApprovalRequired;

    @SerializedName("isActive")
    private Boolean isActive;

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

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getIsApprovalRequired() {
        return isApprovalRequired;
    }

    public void setIsApprovalRequired(Boolean isApprovalRequired) {
        this.isApprovalRequired = isApprovalRequired;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}

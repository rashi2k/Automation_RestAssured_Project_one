package com.informatics.pojos.crm;

import com.google.gson.annotations.SerializedName;

public class CustomerNotes
{
    @SerializedName("contactNoteId")
    private Integer contactNoteId;

    @SerializedName("customerNote")
    private String customerNote;

    @SerializedName("isActive")
    private Boolean isActive;

    @SerializedName("customerId")
    private Integer customerId;

    public Integer getContactNoteId() {
        return contactNoteId;
    }

    public void setContactNoteId(Integer contactNoteId) {
        this.contactNoteId = contactNoteId;
    }

    public String getCustomerNote() {
        return customerNote;
    }

    public void setCustomerNote(String customerNote) {
        this.customerNote = customerNote;
    }

    public Boolean getIsActive()
    {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }
}

package com.informatics.pojos.crm;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class ContactInfo {

//    @SerializedName("email")
//    private Map email;
//
//    @SerializedName("address")
//    private Map address;
//
//    @SerializedName("contact")
//    private Map contact;
//
//    @SerializedName("contactPerson")
//    private Map contactPerson;
//
//    public Map getEmail() {
//        return email;
//    }
//
//    public void setEmail(Map email) {
//        this.email = email;
//    }
//
//    public Map getAddress() {
//        return address;
//    }
//
//    public void setAddress(Map address) {
//        this.address = address;
//    }
//
//    public Map getContact() {
//        return contact;
//    }
//
//    public void setContact(Map contact) {
//        this.contact = contact;
//    }
//
//    public Map getContactPerson() {
//        return contactPerson;
//    }
//
//    public void setContactPerson(Map contactPerson) {
//        this.contactPerson = contactPerson;
//    }

    @SerializedName("id")
    private Integer id;

    @SerializedName("emailTypeId")
    private Integer emailTypeId;

    @SerializedName("email")
    private String email;

    @SerializedName("isReceiveNotification")
    private Boolean isReceiveNotification;

    @SerializedName("isPrimary")
    private Boolean isPrimary;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEmailTypeId() {
        return emailTypeId;
    }

    public void setEmailTypeId(Integer emailTypeId) {
        this.emailTypeId = emailTypeId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getIsReceiveNotification() {
        return isReceiveNotification;
    }

    public void setIsReceiveNotification(Boolean receiveNotification) {
        isReceiveNotification = receiveNotification;
    }

    public Boolean getIsPrimary() {
        return isPrimary;
    }

    public void setIsPrimary(Boolean primary) {
        isPrimary = primary;
    }
}

package com.informatics.pojos.crm;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class CustomerBasicInfo {

    @SerializedName("customerId")
    private Integer customerId;

    @SerializedName("customerTypeCode")
    private String customerTypeCode;

    @SerializedName("firstName")
    private String firstName;

    @SerializedName("lastName")
    private String lastName;

    @SerializedName("middleName")
    private String middleName;

    @SerializedName("callingName")
    private String callingName;

    @SerializedName("surnameWithInitials")
    private String surnameWithInitials;

    @SerializedName("salutationCode")
    private String salutationCode;

    @SerializedName("salutationOther")
    private String salutationOther;

    @SerializedName("gender")
    private String gender;

    @SerializedName("uwCategories")
    private List<Integer> uwCategories;

    @SerializedName("customerGroups")
    private List<Integer> customerGroups;

    @SerializedName("dob")
    private String dob;

    @SerializedName("nationality")
    private String nationality;

    @SerializedName("civilStatusId")
    private Integer civilStatusId;

    @SerializedName("policyholderPreferredLanguage")
    private String policyholderPreferredLanguage;

    @SerializedName("customerPortalAccess")
    private Boolean customerPortalAccess;

    @SerializedName("idNumbers")
    private List<Map> idNumbers;

    @SerializedName("relatedCustomers")
    private List<Map> relatedCustomers;

    @SerializedName("customerNatureId")
    private int customerNatureId;

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getCustomerTypeCode() {
        return customerTypeCode;
    }

    public void setCustomerTypeCode(String customerTypeCode) {
        this.customerTypeCode = customerTypeCode;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCallingName() {
        return callingName;
    }

    public void setCallingName(String callingName) {
        this.callingName = callingName;
    }

    public String getSurnameWithInitials() {
        return surnameWithInitials;
    }

    public void setSurnameWithInitials(String surnameWithInitials) {
        this.surnameWithInitials = surnameWithInitials;
    }

    public String getSalutationCode() {
        return salutationCode;
    }

    public void setSalutationCode(String salutationCode) {
        this.salutationCode = salutationCode;
    }

    public String getSalutationOther() {
        return salutationOther;
    }

    public void setSalutationOther(String salutationOther) {
        this.salutationOther = salutationOther;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public List<Integer> getUwCategories() {
        return uwCategories;
    }

    public void setUwCategories(List<Integer> uwCategories) {
        this.uwCategories = uwCategories;
    }

    public List<Integer> getCustomerGroups() {
        return customerGroups;
    }

    public void setCustomerGroups(List<Integer> customerGroups) {
        this.customerGroups = customerGroups;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public Integer getCivilStatusId() {
        return civilStatusId;
    }

    public void setCivilStatusId(Integer civilStatusId) {
        this.civilStatusId = civilStatusId;
    }

    public String getPolicyholderPreferredLanguage() {
        return policyholderPreferredLanguage;
    }

    public void setPolicyholderPreferredLanguage(String policyholderPreferredLanguage) {
        this.policyholderPreferredLanguage = policyholderPreferredLanguage;
    }

    public Boolean getCustomerPortalAccess() {
        return customerPortalAccess;
    }

    public void setCustomerPortalAccess(Boolean customerPortalAccess) {
        this.customerPortalAccess = customerPortalAccess;
    }

    public List<Map> getIdNumbers() {
        return idNumbers;
    }

    public void setIdNumbers(List<Map> idNumbers) {
        this.idNumbers = idNumbers;
    }

    public List<Map> getRelatedCustomers() {
        return relatedCustomers;
    }

    public void setRelatedCustomers(List<Map> relatedCustomers) {
        this.relatedCustomers = relatedCustomers;
    }

    public int getCustomerNatureId() {
        return customerNatureId;
    }

    public void setCustomerNatureId(int customerNatureId) {
        this.customerNatureId = customerNatureId;
    }

}


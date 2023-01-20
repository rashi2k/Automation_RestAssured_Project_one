package com.informatics.pojos.crm;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CoporateCutomerBasicInfo {

    @SerializedName("customerId")
    private Integer customerId;

    @SerializedName("corporateCustomerId")
    private Integer corporateCustomerId;

    @SerializedName("leadCode")
    private String leadCode;

    @SerializedName("customerTypeCode")
    private String customerTypeCode;

    @SerializedName("name")
    private String name;

    @SerializedName("registrationNumber")
    private String registrationNumber;

    @SerializedName("parentCompanyId")
    private Integer parentCompanyId;

    @SerializedName("businessTypeId")
    private Integer businessTypeId;

    @SerializedName("uwCategories")
    private List<Integer> uwCategories;

    @SerializedName("deletingUwCategories")
    private List<Integer> deletingUwCategories;

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getCorporateCustomerId() {
        return corporateCustomerId;
    }

    public void setCorporateCustomerId(Integer corporateCustomerId) {
        this.corporateCustomerId = corporateCustomerId;
    }

    public String getLeadCode() {
        return leadCode;
    }

    public void setLeadCode(String leadCode) {
        this.leadCode = leadCode;
    }

    public String getCustomerTypeCode() {
        return customerTypeCode;
    }

    public void setCustomerTypeCode(String customerTypeCode) {
        this.customerTypeCode = customerTypeCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public Integer getParentCompanyId() {
        return parentCompanyId;
    }

    public void setParentCompanyId(Integer parentCompanyId) {
        this.parentCompanyId = parentCompanyId;
    }

    public Integer getBusinessTypeId() {
        return businessTypeId;
    }

    public void setBusinessTypeId(Integer businessTypeId) {
        this.businessTypeId = businessTypeId;
    }

    public List<Integer> getUwCategories() {
        return uwCategories;
    }

    public void setUwCategories(List<Integer> uwCategories) {
        this.uwCategories = uwCategories;
    }

    public List<Integer> getDeletingUwCategories() {
        return deletingUwCategories;
    }

    public void setDeletingUwCategories(List<Integer> deletingUwCategories) {
        this.deletingUwCategories = deletingUwCategories;
    }
}

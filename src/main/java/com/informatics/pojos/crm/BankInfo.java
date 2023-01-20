package com.informatics.pojos.crm;

import com.google.gson.annotations.SerializedName;

public class BankInfo {

    @SerializedName("customerBankId")
    private Integer customerBankId;

    @SerializedName("bankAccountName")
    private String bankAccountName;

    @SerializedName("bankAccountNo")
    private String bankAccountNo;

    @SerializedName("bankId")
    private Integer bankId;

    @SerializedName("branchId")
    private Integer branchId;

    @SerializedName("isPrimary")
    private Boolean isPrimary;

    @SerializedName("isActive")
    private Boolean isActive;

    public Integer getCustomerBankId() {
        return customerBankId;
    }

    public void setCustomerBankId(Integer customerBankId) {
        this.customerBankId = customerBankId;
    }

    public String getBankAccountName() {
        return bankAccountName;
    }

    public void setBankAccountName(String bankAccountName) {
        this.bankAccountName = bankAccountName;
    }

    public String getBankAccountNo() {
        return bankAccountNo;
    }

    public void setBankAccountNo(String bankAccountNo) {
        this.bankAccountNo = bankAccountNo;
    }

    public Integer getBankId() {
        return bankId;
    }

    public void setBankId(Integer bankId) {
        this.bankId = bankId;
    }

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    public Boolean getIsPrimary() {
        return isPrimary;
    }

    public void setIsPrimary(Boolean primary) {
        isPrimary = primary;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }
}

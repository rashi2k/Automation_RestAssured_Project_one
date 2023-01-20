package com.informatics.pojos.admin;

import com.google.gson.annotations.SerializedName;

public class BankBranch {

    @SerializedName("id")
    private Integer id;

    @SerializedName("bankId")
    private Integer bankId;

    @SerializedName("bankName")
    private String bankName;

    @SerializedName("code")
    private String code;

    @SerializedName("name")
    private String name;

    @SerializedName("swiftCode")
    private String swiftCode;

    @SerializedName("isActive")
    private Boolean isActive;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBankId() {
        return bankId;
    }

    public void setBankId(Integer bankId) {
        this.bankId = bankId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSwiftCode() {
        return swiftCode;
    }

    public void setSwiftCode(String swiftCode) {
        this.swiftCode = swiftCode;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }
}

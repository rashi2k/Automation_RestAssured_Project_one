package com.informatics.pojos.crm;

import com.google.gson.annotations.SerializedName;

public class EmploymentInfo {

    @SerializedName("customerEmploymentId")
    private Integer customerEmploymentId;

    @SerializedName("employmentName")
    private String employmentName;

    @SerializedName("startDate")
    private String startDate;

    @SerializedName("endDate")
    private String endDate;

    @SerializedName("occupationId")
    private Integer occupationId;

    @SerializedName("isActive")
    private Boolean isActive;

    public Integer getCustomerEmploymentId() {
        return customerEmploymentId;
    }

    public void setCustomerEmploymentId(Integer customerEmploymentId) {
        this.customerEmploymentId = customerEmploymentId;
    }

    public String getEmploymentName() {
        return employmentName;
    }

    public void setEmploymentName(String employmentName) {
        this.employmentName = employmentName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Integer getOccupationId() {
        return occupationId;
    }

    public void setOccupationId(Integer occupationId) {
        this.occupationId = occupationId;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }
}

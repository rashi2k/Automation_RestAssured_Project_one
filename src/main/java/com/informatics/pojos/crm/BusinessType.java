package com.informatics.pojos.crm;

import com.google.gson.annotations.SerializedName;

public class BusinessType
{

    @SerializedName("businessType")
    private String businessType;

    @SerializedName("id")
    private String id;

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

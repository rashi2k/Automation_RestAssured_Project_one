package com.informatics.pojos.crm;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class JointParticipation
{

    @SerializedName("id")
    private Integer id;

    @SerializedName("code")
    private String code;

    @SerializedName("isActive")
    private Boolean isActive;

    @SerializedName("partnershipName")
    private String partnershipName;

    @SerializedName("participation")
    private List<Map> participation;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getPartnershipName() {
        return partnershipName;
    }

    public void setPartnershipName(String partnershipName) {
        this.partnershipName = partnershipName;
    }

    public List<Map> getParticipation() {
        return participation;
    }

    public void setParticipation(List<Map> participation) {
        this.participation = participation;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}

package com.informatics.pojos.admin;

import com.google.gson.annotations.SerializedName;

public class Occupation {

    @SerializedName("id")
    private Integer id;

    @SerializedName("occupation")
    private String occupation;

    @SerializedName("name")
    private String name;

    @SerializedName("isActive")
    private Boolean isActive;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }
}

package com.informatics.pojos.crm;

import com.google.gson.annotations.SerializedName;

public class RelationshipTypes {

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("isActive")
    private String isActive;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }
}

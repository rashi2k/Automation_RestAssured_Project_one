package com.informatics.pojos.crm;

import com.google.gson.annotations.SerializedName;

public class DocumentList {
    @SerializedName("id")
    private Integer id;

    @SerializedName("moduleId")
    private Integer moduleId;

    @SerializedName("moduleCode")
    private String moduleCode;

    @SerializedName("subModuleId")
    private Integer subModuleId;

    @SerializedName("subModuleCode")
    private String subModuleCode;

    @SerializedName("functionId")
    private Integer functionId;

    @SerializedName("functionCode")
    private String functionCode;

    @SerializedName("documentId")
    private Integer documentId;

    @SerializedName("documentCode")
    private String documentCode;

    @SerializedName("documentType")
    private String documentType;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getModuleId() {
        return moduleId;
    }

    public void setModuleId(Integer moduleId) {
        this.moduleId = moduleId;
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }

    public Integer getSubModuleId() {
        return subModuleId;
    }

    public void setSubModuleId(Integer subModuleId) {
        this.subModuleId = subModuleId;
    }

    public String getSubModuleCode() {
        return subModuleCode;
    }

    public void setSubModuleCode(String subModuleCode) {
        this.subModuleCode = subModuleCode;
    }

    public Integer getFunctionId() {
        return functionId;
    }

    public void setFunctionId(Integer functionId) {
        this.functionId = functionId;
    }

    public String getFunctionCode() {
        return functionCode;
    }

    public void setFunctionCode(String functionCode) {
        this.functionCode = functionCode;
    }

    public Integer getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Integer documentId) {
        this.documentId = documentId;
    }

    public String getDocumentCode() {
        return documentCode;
    }

    public void setDocumentCode(String documentCode) {
        this.documentCode = documentCode;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }
}

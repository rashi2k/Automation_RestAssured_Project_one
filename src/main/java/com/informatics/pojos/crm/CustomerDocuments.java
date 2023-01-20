package com.informatics.pojos.crm;

import com.google.gson.annotations.SerializedName;

public class CustomerDocuments {

    @SerializedName("id")
    private Integer id;

    @SerializedName("documentPath")
    private String documentPath;

    @SerializedName("remark")
    private String remark;

    @SerializedName("receivedDate")
    private String receivedDate;

    @SerializedName("functionDocumentId")
    private Integer functionDocumentId;

    @SerializedName("documentTypeId")
    private Integer documentTypeId;

    @SerializedName("isReceived")
    private Boolean isReceived;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDocumentPath() {
        return documentPath;
    }

    public void setDocumentPath(String documentPath) {
        this.documentPath = documentPath;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(String receivedDate) {
        this.receivedDate = receivedDate;
    }

    public Integer getFunctionDocumentId() {
        return functionDocumentId;
    }

    public void setFunctionDocumentId(Integer functionDocumentId) {
        this.functionDocumentId = functionDocumentId;
    }

    public Integer getDocumentTypeId() {
        return documentTypeId;
    }

    public void setDocumentTypeId(Integer documentTypeId) {
        this.documentTypeId = documentTypeId;
    }

    public Boolean getIsReceived() {
        return isReceived;
    }

    public void setIsReceived(Boolean received) {
        isReceived = received;
    }
}

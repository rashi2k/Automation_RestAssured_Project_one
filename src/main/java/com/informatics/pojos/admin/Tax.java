package com.informatics.pojos.admin;

import com.google.gson.annotations.SerializedName;

public class Tax{

	@SerializedName("taxTypeId")
	private String taxTypeId;

	@SerializedName("taxTypeCode")
	private String taxTypeCode;

	@SerializedName("taxType")
	private String taxType;

	@SerializedName("isActive")
	private Boolean isActive;

	@SerializedName("countryId")
	private Integer countryId;

	@SerializedName("countryName")
	private String countryName;

	public String getTaxTypeId() {
		return taxTypeId;
	}

	public void setTaxTypeId(String taxTypeId) {
		this.taxTypeId = taxTypeId;
	}

	public String getTaxTypeCode() {
		return taxTypeCode;
	}

	public void setTaxTypeCode(String taxTypeCode) {
		this.taxTypeCode = taxTypeCode;
	}

	public String getTaxType() {
		return taxType;
	}

	public void setTaxType(String taxType) {
		this.taxType = taxType;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Integer getCountryId() {
		return countryId;
	}

	public void setCountryId(Integer countryId) {
		this.countryId = countryId;
	}
}
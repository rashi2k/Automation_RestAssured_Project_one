package com.informatics.endpoints;

import com.informatics.utils.DataUtils;

public class APIConstants {

	public final static String baseUrl = DataUtils.getTestData("TestData", "BaseUrl", "Value");
	public final static String baseUrlAllEmployees = DataUtils.getTestData("TestData", "BaseUrl", "Value");
	public final static String baseUrlAnEmployee = DataUtils.getTestData("TestData", "EmployeeUrl", "Value");
	public final static String getRepos = "/users/" + DataUtils.getTestData("TestData", "Username", "Value") + "/repos";
	public final static String token = DataUtils.getTestData("TestData", "Token", "Value");

	//CUSTOMER GROUP ENDPOINTS
	public final static String GET_ALL_CUSTOMER_GROUPS = "customer-center/references/customer-groups";
	public final static String GET_SINGLE_CUSTOMER_GROUP_ID = "customer-center/references/customer-groups/{id}";
	public final static String GET_SINGLE_CUSTOMER_GROUP_CODE = "customer-center/references/customer-groups/group-code/{code}";
	public final static String GET_SINGLE_CUSTOMER_GROUP_NAME = "customer-center/references/customer-groups/search/{name}";
	public final static String CREATE_CUSTOMER_GROUP = "customer-center/references/customer-groups";
	public final static String UPDATE_CUSTOMER_GROUP = "customer-center/references/customer-groups/{id}";
	public final static String DELETE_CUSTOMER_GROUP = "customer-center/references/customer-groups/{id}";

	//UW ENDPOINTS
	public final static String GET_UW_CATEGORIES = "customer-center/references/underwriting-categories";
	public final static String GET_UW_CATEGORIES_ID = "customer-center/references/underwriting-categories/{id}";
	public final static String GET_UW_CATEGORIES_CODE = "customer-center/references/underwriting-categories/code/{code}";
	public final static String GET_UW_CATEGORIES_WORD = "customer-center/references/underwriting-categories/search/{word}";
	public final static String CREATE_UW_CATEGORY = "customer-center/references/underwriting-categories";
	public final static String UPDATE_UW_CATEGORY = "customer-center/references/underwriting-categories/{id}";
	public final static String DELETE_UW_CATEGORY = "customer-center/references/underwriting-categories/{id}";

	//CUSTOMER BASIC INFO ENDPOINTS
	public final static String CREATE_CUS_BASIC_INFO = "customer-center/customer/customer-type/individual/basic-info/creation";
	public final static String GET_CUS_BASIC_INFO_ID = "customer-center/customer/id/{id}/basic-info";
	public final static String GET_CUS_BASIC_INFO_LEAD_CODE = "customer-center/customer/lead/{code}/basic-info";
	public final static String GET_CUS_BASIC_INFO_TEMPORARY_CODE = "customer-center/customer/temporary/{code}/basic-info";
	public final static String GET_CUS_BASIC_INFO_PERMANENT_CODE = "customer-center/customer/permanent/{code}/basic-info";
	public final static String SEARCH_CUS_BASIC_INFO = "customer-center/customer/search";
	public final static String SEARCH_INDIVIDUAL_CUS_BASIC_INFO = "customer-center/customer/search/individual/{searchBy}";
	public final static String UPDATE_INDIVIDUAL_CUS_BASIC_INFO = "customer-center/customer/{id}/customer-type/individual/basic-info/change";

	//CUSTOMER CREATION ENDPOINTS
	public final static String GET_CUS_TYPE = "customer-center/references/customer-types";
	public final static String GET_CUS_TITLE = "control-center/person/title";
	public final static String GET_CUS_GENDER = "control-center/person/gender";
	public final static String GET_CUS_NATIONALITY = "control-center/geo-personal/nationality";
	public final static String GET_CUS_CIVIL_STATUS = "control-center/person/civil-status";
	public final static String GET_CUS_PREF_LANG = "control-center/geo-personal/language";
	public final static String GET_CUS_ID_TYPE = "control-center/identity";
	public final static String GET_CUS_RELATIONSHIP_TYPE = "customer-center/references/relationship-types";

	//RELATIONSHIP TYPES ENDPOINTS
	public final static String GET_ALL_RELATIONSHIP_TYPES = "customer-center/references/relationship-types";
	public final static String GET_RELATIONSHIP_TYPE_ID = "customer-center/references/relationship-types/{id}";
	public final static String GET_RELATIONSHIP_TYPE_PAGE  = "customer-center/references/relationship-types/page";
	public final static String CREATE_RELATIONSHIP_TYPE = "customer-center/references/relationship-types";
	public final static String UPDATE_RELATIONSHIP_TYPE = "customer-center/references/relationship-types/{id}";
	public final static String DELETE_RELATIONSHIP_TYPE = "customer-center/references/relationship-types/{id}";

	//CUSTOMER TYPE ENDPOINTS
	public final static String GET_CUS_TYPES = "customer-center/references/customer-types";
	public final static String GET_CUS_TYPES_ID = "customer-center/references/customer-types/{id}";
	public final static String GET_CUS_TYPES_TYPE = "customer-center/references/customer-types/{type}/additional-info/fields";

	//SETTINGS-CONTROLLER ENDPOINTS
	public final static String GET_ALL_VALIDATION_EVENTS = "customer-center/references/validation-events/all";
	public final static String GET_VALIDATION_EVENTS = "customer-center/references/validation-events";
	public final static String GET_VALIDATION_EVENTS_CUSTOMER_TYPE_ID = "customer-center/references/customer-fields/customer-type/{customerTypeId}";
	public final static String GET_VALIDATION_EVENTS_EVENT_CODE = "customer-center/references/validation-events/{eventCode}/customer-type/{customerTypeId}";
	public final static String GET_VALIDATION_EVENTS_SEARCH_BY = "customer-center/references/validation-events/search/{searchBy}";
	public final static String UPDATE_VALIDATION_EVENT = "customer-center/references/validation-events/{eventCode}/customer-type/{customerTypeId}";

	//TAX TYPE CREATION
	public final static String GET_COUNTRY = "control-center/location/country";

	//TAX ENDPOINTS
	public final static String GET_TAX = "control-center/tax";
	public final static String GET_TAX_TYPE_ID = "control-center/tax/{taxTypeId}";
	public final static String GET_TAX_TYPE_CODE = "control-center/tax/code/{taxTypeCode}";
	public final static String CREATE_TAX = "control-center/tax";
	public final static String UPDATE_TAX = "control-center/tax/{taxTypeId}";
	public final static String DELETE_TAX = "control-center/tax/{taxTypeId}";

	//TAX REGISTRATION ENDPOINTS
//	public final static String CREATE_TAX_REGISTRATION_ADDITION = "customer-center/customer/{id}/taxes/registration/addition";
	public final static String GET_TAX_REGISTRATION_CUS_ID = "customer-center/customer/{id}/taxes/registration";
	public final static String GET_TAX_REGISTRATION_ID = "customer-center/customer/taxes/registration/{id}";
	public final static String UPDATE_TAX_REGISTRATION = "customer-center/customer/{customerId}/taxes/registration";
	public final static String DELETE_TAX_REGISTRATION = "customer-center/customer/taxes/registration/{id}/removal";

	//TAXES EXCEPTED ADDITION ENDPOINTS
	public final static String GET_TAXES_EXCEPTED_CUSTOMER_ID = "customer-center/customer/{customerId}/taxes/exemption";
	public final static String GET_TAXES_EXCEPTED_EXCEPTED_ID = "customer-center/customer/taxes/exemption/{exemptionId}";
	public final static String PUT_TAXES_EXCEPTED_CUSTOMER_ID = "customer-center/customer/{customerId}/taxes/exemption";
//	public final static String CREATE_TAXES_EXCEPTED_ADDITION = "customer-center/customer/{customerId}/taxes/exemption";
//	public final static String DELETE_TAXES_EXCEPTED_EXCEPTED_ID = "customer-center/customer/{customerId}/taxes/exemption";

	//DOCUMENT LIST ENDPOINTS
	public final static String GET_DOCUMENT_LIST = "control-center/customer-document/{type}/documents";

	//CUSTOMER NOTES ENDPOINTS
	public final static String CREATE_CUSTOMER_NOTES = "customer-center/customer/{customerId}/notes/add";
	public final static String GET_CUSTOMER_NOTES = "customer-center/customer/{customerId}/notes";
	public final static String PUT_CUSTOMER_NOTES = "customer-center/customer/notes/{noteId}";

	//JOINT PARTICIPATION ENDPOINTS
	public final static String CREATE_JOINT_PARTICIPATION = "customer-center/customer/joint/addition";
	public final static String GET_JOINT_PARTICIPATION_CODE = "customer-center/customer/joint/code/{code}";
	public final static String GET_JOINT_PARTICIPATION_ID = "customer-center/customer/joint/{partnershipId}";
	public final static String PUT_JOINT_PARTICIPATION = "customer-center/customer/joint/{partnershipId}/change";
	public final static String DELETE_JOINT_PARTICIPATION = "customer-center/customer/joint/{partnershipId}/removal";

	//BANK
	public final static String CREATE_BANK = "control-center/references/banks";
	public final static String GET_BANK = "control-center/references/banks";
	public final static String GET_BANK_ID= "control-center/references/banks/{id}";
	public final static String GET_BANK_CODE = "control-center/references/banks/code/{code}";
	public final static String GET_BANK_SEARCH = "control-center/references/banks/search/{name}";
	public final static String UPDATE_BANK= "control-center/references/banks/{id}";
	public final static String DELETE_BANK= "control-center/references/banks/{id}";

	//BANK BRANCH
	public final static String CREATE_BANK_BRANCH = "control-center/bank-branches";
	public final static String GET_BANK_BRANCH = "control-center/bank-branches";
	public final static String GET_BANK_BRANCH_ID = "control-center/bank-branches/{id}";
	public final static String GET_BANK_BRANCH_CODE = "control-center/bank-branches/branchCode/{branchCode}";
	public final static String GET_BANK_BRANCH_SEARCH = "control-center/bank-branches/search/{name}";
	public final static String UPDATE_BANK_BRANCH = "control-center/bank-branches/{branchId}";
	public final static String DELETE_BANK_BRANCH = "control-center/bank-branches/{branchId}";

	//OCCUPATION
	public final static String CREATE_OCCUPATION = "control-center/occupation";
	public final static String GET_OCCUPATION = "control-center/occupation";
	public final static String GET_OCCUPATION_ID = "control-center/occupation/{id}";
	public final static String GET_OCCUPATION_CODE = "control-center/occupation/code/{code}";
	public final static String GET_OCCUPATION_SEARCH = "control-center/occupation/search/{name}";
	public final static String UPDATE_OCCUPATION = "control-center/occupation/{id}";
	public final static String DELETE_OCCUPATION = "control-center/occupation/{id}";

	//CUSTOMER BANK INFO
	public final static String CREATE_CUSTOMER_BANK_INFO = "customer-center/customer/{customerId}/bank-info/addition";
	public final static String GET_CUSTOMER_BANK_INFO = "customer-center/customer/{customerId}/bank-info";
	public final static String GET_CUSTOMER_BANK_INFO_BANK_ID = "customer-center/customer/bank-info/{bankId}";
	public final static String UPDATE_CUSTOMER_BANK_INFO	= "customer-center/customer/{customerId}/bank-info/change";
	public final static String DELETE_CUSTOMER_BANK_INFO	= "customer-center/customer/bank-info/{bankId}/removal";

	//CUSTOMER EMPLOYMENT INFO
	public final static String CREATE_CUSTOMER_EMPLOYMENT_INFO = "customer-center/customer/{customerId}/employment/addition";
	public final static String GET_CUSTOMER_EMPLOYMENT_INFO = "customer-center/customer/{customerId}/employment";
	public final static String GET_CUSTOMER_EMPLOYMENT_INFO_EMP_ID = "customer-center/customer/employment/{employmentId}";
	public final static String UPDATE_CUSTOMER_EMPLOYMENT_INFO = "customer-center/customer/{customerId}/employment/change";
	public final static String DELETE_CUSTOMER_EMPLOYMENT_INFO = "customer-center/customer/employment/{employmentId}/removal";

	//ADDITIONAL INFO
	public final static String CREATE_CUSTOMER_ADDITIONAL_INFO = "customer-center/customer/{customerId}/additional-info/data/addition";
	public final static String GET_CUSTOMER_ADDITIONAL_INFO_CUS_ID = "customer-center/customer/{id}/additional-info/data";
	public final static String GET_CUSTOMER_ADDITIONAL_INFO_ID = "customer-center/customer/additional-info/data/{id}";
	public final static String UPDATE_CUSTOMER_ADDITIONAL_INFO = "customer-center/customer/{customerId}/additional-info/data/change";
	public final static String DELETE_CUSTOMER_ADDITIONAL_INFO = "customer-center/customer/additional-info/data/{id}/removal";

	//ADDITIONAL INFO FIELD REF
	public final static String GET_CUSTOMER_ADDITIONAL_INFO_FIELD = "customer-center/references/additional-info/field/{id}";
	public final static String GET_CUSTOMER_ADDITIONAL_INFO_FIELD_CUS_TYPE = "customer-center/references/customer-types/{type}/additional-info/fields";

	//STATUS CHANGE
	public final static String CREATE_STATUS_CHANGE = "customer-center/customer/{customerId}/status/change";
	public final static String GET_CUSTOMER_STATUS = "customer-center/references/customer-status";
	public final static String GET_RESTRICTED_TRANSACTIONS = "customer-center/references/restricted-trans";

	//CUSTOMER CONTACT INFO
	public final static String UPDATE_CUS_CONTACT_INFO = "customer-center/customer/{id}/contact-info/update";
	public final static String GET_CUS_CONTACT_INFO = "customer-center/customer/{id}/contact-info";
	public final static String GET_EMAIL_TYPE_BULK = "control-center/contact/email-type/getBulk"; //get emailTypeId bulk

	//CUSTOMER DOCUMENTS
	public final static String UPDATE_CUS_DOCUMENTS = "customer-center/customer/{customerId}/documents/upload";
	public final static String GET_CUS_DOCUMENTS = "customer-center/customer/{customerId}/documents";
	public final static String GET_CUS_DOCUMENTS_BULK = "control-center/document/type/getBulk";

	//BUSINESS TYPE ENDPOINTS
	public final static String CREATE_BUSINESS_TYPE = "customer-center/references/business-types";
	public final static String GET_BUSINESS_TYPE = "customer-center/references/business-types";
	public final static String GET_BUSINESS_TYPE_ID = "customer-center/references/business-types/{id}";
	public final static String PUT_BUSINESS_TYPE = "customer-center/references/business-types/{id}";

	//CORPORATE CUSTOMER
	public final static String CREATE_CORPORATE_CUSTOMER = "customer-center/customer/customer-type/corporate/basic-info/creation";
	public final static String SEARCH_CORPORATE_CUSTOMER_CRITERIA = "customer-center/customer/corporate/search";
	public final static String UPDATE_CORPORATE_CUSTOMER = "customer-center/customer/{corporateCustomerId}/customer-type/corporate/basic-info/change";

	//CIVIL STATUS
	public final static String CREATE_ADMIN_CIVIL_STATUS = "control-center/person/civil-status";
	public final static String GET_ADMIN_CIVIL_STATUS = "control-center/person/civil-status";
	public final static String GET_ADMIN_CIVIL_STATUS_ID = "control-center/person/civil-status/{civilStatusId}";
	public final static String UPDATE_ADMIN_CIVIL_STATUS_ID = "control-center/person/civil-status/{civilStatusId}";
	public final static String DELETE_ADMIN_CIVIL_STATUS_ID = "control-center/person/civil-status/{civilStatusId}";
}


package com.informatics.utils;


public class PropertiesUtils {

    //CUSTOMER TYPE PROPERTIES

    public final static String INDIVIDUAL_CUS_TYPE= "I";
    public final static String CORPORATE_CUS_TYPE= "C";
    public final static String SPECIAL_CUS_TYPE= "S";

    public final static String GET_CUS_TYPES_id0 = "1";
    public final static String GET_CUS_TYPES_typeCode0 = "I";
    public final static String GET_CUS_TYPES_name0 = "Individual";

    public final static String GET_CUS_TYPES_id1 = "2";
    public final static String GET_CUS_TYPES_typeCode1 = "C";
    public final static String GET_CUS_TYPES_name1 = "Corporate";

    public final static String GET_CUS_TYPES_id2 = "3";
    public final static String GET_CUS_TYPES_typeCode2 = "S";
    public final static String GET_CUS_TYPES_name2 = "Special";

    public final static String GET_CUS_TYPES_ID_id = "1";
    public final static String GET_CUS_TYPES_ID_typeCode = "I";
    public final static String GET_CUS_TYPES_ID_name = "Individual";

    public final static String GET_CUS_TYPES_TYPE_id0 = "1";
    public final static String GET_CUS_TYPES_TYPE_name0 = "Blood Group";
    public final static String GET_CUS_TYPES_TYPE_isMandatory0= "false";
    public final static String GET_CUS_TYPES_TYPE_isAvailableInList0 = "true";

    public final static String GET_CUS_TYPES_TYPE_possibleValues_refInfoId = "3";
    public final static String GET_CUS_TYPES_TYPE_possibleValues_code = "blood_group";
    public final static String GET_CUS_TYPES_TYPE_possibleValues_name = "Blood Type";
    public final static String GET_CUS_TYPES_TYPE_possibleValues_lovs = "null";

    public final static String GET_CUS_TYPES_TYPE_id1 = "4";
    public final static String GET_CUS_TYPES_TYPE_name1 = "Smoker";
    public final static String GET_CUS_TYPES_TYPE_isMandatory1 = "false";
    public final static String GET_CUS_TYPES_TYPE_isAvailableInList1 = "false";
    public final static String GET_CUS_TYPES_TYPE_possibleValues1 = "null";

    //Setting controller - validation-events
    public static String validationEventId1 = "1";
    public static String validationEventCode1 = "quotation_issuance";
    public static String validationEventName1 = "Quotation Issuance";
    public static String validationEventId2 = "2";
    public static String validationEventCode2 = "policy_issuance";
    public static String validationEventName2 = "Policy Issuance";
    public static String validationEventId3 = "3";
    public static String validationEventCode3 = "lead_customer";
    public static String validationEventName3 = "Lead Customer";

    //Search customer permanentCode - customerCode and temporary code
    public static String searchId = "1";
    public static String searchLeadCode = "L00000000000003";
    public static String searchTempCode = "T00000000000001";
    public static String searchPermCode = "P00000000000001";
    public static String searchName = "QA_DHA_001";
    public static String searchType = "I";
    public static String searchSalutation = "1";
    public static String searchSalutationOther = "Mr.";
    public static String searchGender = "M";
    public static String searchUwCategories = "5";
    //public static String searchCustomerGroups = "1";
    public static String searchDob = "1996-10-04";
    public static String searchNationality = "SL";
    public static String searchCivilStatus = "1";
    public static String searchLang = "SIN";
    public static String searchIdentityId = "8061";
    public static String searchIdType = "62";
    public static String searchIdNumber = "967521795V";
    public static String searchPortalAccess = "true";
    public static String getSalutation = "0";
    public static String getSurnameWithInitials = "Q.  Q.  QA_DHA_001";
    public static String getIdType = "1";

    //Document List individual
    public static String id = "3";
    public static String moduleId = "13";
    public static String moduleCode = "DB";
    public static String subModuleId = "10";
    public static String subModuleCode = "CM001";
    public static String functionId = "26";
    public static String functionCode = "CM004";
    public static String documentId = "1";
    public static String documentCode = "NIC";
    public static String documentType = "NIC";


    //Document List Corporate
    public static String idCorporate = "4";
    public static String moduleIdCorporate = "13";
    public static String moduleCodeCorporate = "DB";
    public static String subModuleIdCorporate = "10";
    public static String subModuleCodeCorporate = "CM001";
    public static String functionIdCorporate = "27";
    public static String functionCodeCorporate = "CM005";
    public static String documentIdCorporate = "61";
    public static String documentCodeCorporate = "BRD";
    public static String documentTypeCorporate = "Business Registration";

    //Document List Special
    public static String idSpecial = "5";
    public static String moduleIdSpecial = "13";
    public static String moduleCodeSpecial = "DB";
    public static String subModuleIdSpecial = "10";
    public static String subModuleCodeSpecial = "CM001";
    public static String functionIdSpecial = "28";
    public static String functionCodeSpecial = "CM006";
    public static String documentIdSpecial = "1";
    public static String documentCodeSpecial = "NIC";
    public static String documentTypeSpecial = "NIC";

    //Joint Participation
    public static String jointPartnershipId = "1";
    public static String percentage = "65";
    public static String isDefault = "true";

    //ADDITIONAL INFO FIELD REF
    public static String fieldId0 = "1";
    public static String fieldName0 = "Blood Group";
    public static String fieldIsMandatory0 = "false";
    public static String fieldIsAvailableInList0 = "true";
    public static String pvRefInfoId0 = "3";
    public static String pvCode0 = "blood_group";
    public static String pvName0 = "Blood Type";
    public static String pvLovs0 = "null";

    public static String fieldId1 = "4";
    public static String fieldName1 = "Smoker";
    public static String fieldIsMandatory1 = "false";
    public static String fieldIsAvailableInList1 = "false";
    public static String possibleValues1 = "null";

    //CUSTOMER STATUS
    public static String activeStatusId0 = "1";
    public static String activeStatusCode0 = "A";
    public static String activeStatus0 = "Active";
    public static String activeStatusId1 = "4";
    public static String activeStatusCode1 = "P";
    public static String activeStatus1 = "Prospect";
    public static String activeStatusId2 = "3";
    public static String activeStatusCode2 = "I";
    public static String activeStatus2 = "In-actived Permanently";
    public static String activeStatusId3 = "2";
    public static String activeStatusCode3 = "T";
    public static String activeStatus3 = "In-actived Temporarily";

    //RESTRICTED TRANSACTIONS
    public static String resTransId0 = "1";
    public static String resTransCode0 = "quotation_issuance";
    public static String resTransStatus0 = "Quotation Issuance";
    public static String resTransId1 = "2";
    public static String resTransCode1 = "new_business Issuance";
    public static String resTransStatus1 = "New Business Issuance";
    public static String resTransId2 = "3";
    public static String resTransCode2 = "renew_policy";
    public static String resTransStatus2 = "Renew Policy";
    public static String resTransId3 = "4";
    public static String resTransCode3 = "release_payment";
    public static String resTransStatus3 = "Release Payment";

    //CUSTOMER NOTES
    public static String cusNoteIsPrimary = "true";
    public static String cusNoteReceivedNotification = "true";

    //CUSTOMER DOCUMENTS
    public static String cusDocsIsReceived = "true";
    public static String cusDocsDocumentPath = "https://docs.org/xyz.jpg";
    public static String cusDocsFunctionId = "3"; //mapped with documentTypeId 1

    //Business Type
    public static String businessType = "Construction";
    public static String updateBusinessType = "Textile";

    //CORPORATE CUSTOMER
    public static String corporateCustomerPortalAccess = "false";
    public static String corporateCustomerStatus = "In-actived Temporarily";
    public static String corporateCustomerProfileStatus = "Draft";

    //Search corporate customer permanentCode/temporaryCode
    public static String searchCorporateCustomerId = "3083";
    public static String searchCorporateCustomerPermanentCode = "P00000000000002";
    public static String searchCorporateCustomerLeadCode = "L00000000003108";
    public static String searchCorporateCustomerTemporaryCode = "T00000000000002";
    public static String searchCorporateCustomerTypeCode= "C";
    public static String searchCorporateCustomerUwCategories = "92";
    public static String searchCorporateCustomerPortalAccess = "false";
    public static String searchCorporateCustomerName =  "Franecki-Franecki Group";
    public static String searchCorporateCustomerRegNumber =  "AUTO6940937";
    public static String searchCorporateCustomerBusinessTypeId = "22";
}

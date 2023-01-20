package tests.crm.customerBasicInfo;

import com.informatics.endpoints.APIConstants;
import com.informatics.pojos.crm.CustomerBasicInfo;
import com.informatics.utils.PropertiesUtils;
import com.jayway.jsonpath.JsonPath;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import tests.BaseTest;

public class GetCustomerBasicInfo extends BaseTest {

    public Integer customerId;

    @BeforeClass
    public void initiate() {
        getBaseCustomerInfo();
    }

    @BeforeMethod
    public void createParentCustomer() {
        //create test customer to get customerID
        CustomerBasicInfo createTestCustomer = requestPayload.createTestCustomer(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, idNumber);
        Response response = RestAssured.given().spec(repoSpec).when()
                .body(createTestCustomer)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        customerId = Integer.parseInt(JsonPath.read(response.asString(), "$.result.customerId").toString());
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-347 [R1][CRM_API] Verify the GET - get_customer_basic_info with valid ID")
    public void testGetCustomerBasicInfoWithValidId() {
        CustomerBasicInfo createCustomerBasicInfo = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, customerId);

        Response responseA = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomerBasicInfo)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + responseA.getStatusCode());
        responseA.getBody().prettyPrint();
        Assert.assertEquals(responseA.statusCode(), 201);

        String saveCustomerId = JsonPath.read(responseA.asString(), "$.result.customerId").toString();
        String saveCustomerLeadCode = JsonPath.read(responseA.asString(), "$.result.leadCode").toString();
        String updatedURI = APIConstants.GET_CUS_BASIC_INFO_ID.replace("{id}", saveCustomerId);

        Response responseB = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("\n" + "Status Code: " + responseB.getStatusCode());
        responseB.getBody().prettyPrint();
        Assert.assertEquals(responseB.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = responseB.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.customerId").toString()), saveCustomerId, "Invalid customerId");
        try{
            softAssert.assertEquals(String.valueOf(JsonPath.read(responseToString, "$.result.permanentCode")), null , "Invalid customerCode");
        }
        catch (Exception e) {
        }
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.leadCode").toString()), saveCustomerLeadCode, "Invalid leadCode");
        try{
            softAssert.assertEquals((JsonPath.read(responseToString, "$.result.temporaryCode").toString()), null , "Invalid temporaryCode");
            softAssert.assertEquals((JsonPath.read(responseToString, "$.result.debtorCode").toString()), null , "Invalid debtorCode");
        }
        catch (Exception e){
        }
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.customerTypeCode").toString()), createCustomerBasicInfo.getCustomerTypeCode(), "Invalid customerTypeCode");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.uwCategories").toString()), String.valueOf(createCustomerBasicInfo.getUwCategories()), "Invalid uwCategories");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.customerPortalAccess").toString()), String.valueOf(createCustomerBasicInfo.getCustomerPortalAccess()), "Invalid customerPortalAccess");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.firstName").toString()), createCustomerBasicInfo.getFirstName(), "Invalid firstName");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.middleName").toString()), createCustomerBasicInfo.getMiddleName(), "Invalid middleName");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.lastName").toString()), createCustomerBasicInfo.getLastName(), "Invalid lastName");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.callingName").toString()), createCustomerBasicInfo.getCallingName(), "Invalid callingName");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.surnameWithInitials").toString()), createCustomerBasicInfo.getSurnameWithInitials(), "Invalid surnameWithInitials");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.salutation").toString()), createCustomerBasicInfo.getSalutationCode(), "Invalid salutation");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.salutationOther").toString()), salutation, "Invalid salutationOther");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.gender").toString()), createCustomerBasicInfo.getGender(), "Invalid gender");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.customerGroups").toString()), String.valueOf(createCustomerBasicInfo.getCustomerGroups()), "Invalid customerGroups");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.dob").toString()), createCustomerBasicInfo.getDob(), "Invalid dob");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.nationality").toString()), createCustomerBasicInfo.getNationality(), "Invalid nationality");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.civilStatusId").toString()), String.valueOf(createCustomerBasicInfo.getCivilStatusId()), "Invalid civilStatusId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.policyHolderPreferredLanguage").toString()), createCustomerBasicInfo.getPolicyholderPreferredLanguage(), "Invalid policyHolderPreferredLanguage");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.idNumbers[0].idType").toString()), String.valueOf(idType), "Invalid idType");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.idNumbers[0].idNumber").toString()), idNumber, "Invalid idNumber");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.relatedCustomers[0].relationshipTypeId").toString()), String.valueOf(relationshipTypeId), "Invalid relationshipTypeId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.relatedCustomers[0].customerId").toString()), String.valueOf(customerId), "Invalid customerId");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-349 [R1][CRM_API] Verify the GET- get_customer_basic_info with null ID")
    public void testGetCustomerBasicInfoWithNullId() {
        String updatedURI = APIConstants.GET_CUS_BASIC_INFO_ID.replace("{id}", "");

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("\n" + "Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-353 [R1][CRM_API] Verify the GET - get_customer_basic_info with invalid ID")
    public void testGetCustomerBasicInfoWithInvalidId() {
        String updatedURI = APIConstants.GET_CUS_BASIC_INFO_ID.replace("{id}", "A");

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("\n" + "Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-361 [R1][CRM_API] Verify the GET - get_customer_basic_info with valid Lead Code")
    public void testGetCustomerBasicInfoWithValidLeadCode() {
        CustomerBasicInfo createCustomerBasicInfo = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, customerId);

        Response responseA = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomerBasicInfo)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + responseA.getStatusCode());
        responseA.getBody().prettyPrint();
        Assert.assertEquals(responseA.statusCode(), 201);

        String saveCustomerId = JsonPath.read(responseA.asString(), "$.result.customerId").toString();
        String saveCustomerLeadCode = JsonPath.read(responseA.asString(), "$.result.leadCode").toString();
        String updatedURI = APIConstants.GET_CUS_BASIC_INFO_LEAD_CODE.replace("{code}", saveCustomerLeadCode);

        Response responseB = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("\n" + "Status Code: " + responseB.getStatusCode());
        responseB.getBody().prettyPrint();
        Assert.assertEquals(responseB.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = responseB.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.customerId").toString()), saveCustomerId, "Invalid customerId");
        try{
            softAssert.assertEquals(String.valueOf(JsonPath.read(responseToString, "$.result.permanentCode")), null , "Invalid customerCode");
        }
        catch (Exception e) {
        }
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.leadCode").toString()), saveCustomerLeadCode, "Invalid leadCode");
        try{
            softAssert.assertEquals((JsonPath.read(responseToString, "$.result.temporaryCode").toString()), null , "Invalid temporaryCode");
            softAssert.assertEquals((JsonPath.read(responseToString, "$.result.debtorCode").toString()), null , "Invalid debtorCode");
        }
        catch (Exception e){
        }
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.customerTypeCode").toString()), createCustomerBasicInfo.getCustomerTypeCode(), "Invalid customerTypeCode");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.uwCategories").toString()), String.valueOf(createCustomerBasicInfo.getUwCategories()), "Invalid uwCategories");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.customerPortalAccess").toString()), String.valueOf(createCustomerBasicInfo.getCustomerPortalAccess()), "Invalid customerPortalAccess");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.firstName").toString()), createCustomerBasicInfo.getFirstName(), "Invalid firstName");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.middleName").toString()), createCustomerBasicInfo.getMiddleName(), "Invalid middleName");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.lastName").toString()), createCustomerBasicInfo.getLastName(), "Invalid lastName");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.callingName").toString()), createCustomerBasicInfo.getCallingName(), "Invalid callingName");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.surnameWithInitials").toString()), createCustomerBasicInfo.getSurnameWithInitials(), "Invalid surnameWithInitials");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.salutation").toString()), createCustomerBasicInfo.getSalutationCode(), "Invalid salutation");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.salutationOther").toString()), salutation, "Invalid salutationOther");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.gender").toString()), createCustomerBasicInfo.getGender(), "Invalid gender");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.customerGroups").toString()), String.valueOf(createCustomerBasicInfo.getCustomerGroups()), "Invalid customerGroups");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.dob").toString()), createCustomerBasicInfo.getDob(), "Invalid dob");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.nationality").toString()), createCustomerBasicInfo.getNationality(), "Invalid nationality");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.civilStatusId").toString()), String.valueOf(createCustomerBasicInfo.getCivilStatusId()), "Invalid civilStatusId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.policyHolderPreferredLanguage").toString()), createCustomerBasicInfo.getPolicyholderPreferredLanguage(), "Invalid policyHolderPreferredLanguage");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.idNumbers[0].idType").toString()), String.valueOf(idType), "Invalid idType");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.idNumbers[0].idNumber").toString()), idNumber, "Invalid idNumber");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.relatedCustomers[0].relationshipTypeId").toString()), String.valueOf(relationshipTypeId), "Invalid relationshipTypeId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.relatedCustomers[0].customerId").toString()), String.valueOf(customerId), "Invalid customerId");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-363 [R1][CRM_API] Verify the GET - get_customer_basic_info with null Lead Code")
    public void testGetCustomerBasicInfoWithNullLeadCode() {
        String updatedURI = APIConstants.GET_CUS_BASIC_INFO_LEAD_CODE.replace("{code}", "");

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("\n" + "Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-365 [R1][CRM_API] Verify the GET - get_customer_basic_info with invalid Lead Code")
    public void testGetCustomerBasicInfoWithInvalidLeadCode() {
        String invalidLeadCode = "@QA99999";
        String updatedURI = APIConstants.GET_CUS_BASIC_INFO_LEAD_CODE.replace("{code}", invalidLeadCode);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("\n" + "Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);

        String statusCode = String.valueOf(response.getStatusCode());
        SoftAssert softAssert = new SoftAssert();
        String responseToString = response.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), statusCode, "Invalid status code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "CM_M_CUSTOMER_NOT_FOUND", "Invalid error");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "There is no customer related data for the given lead code - " + invalidLeadCode, "Invalid description");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-367 [R1][CRM_API] Verify the GET - get_customer_basic_info with valid Temporary Code")
    public void testGetCustomerBasicInfoWithValidTemporaryCode() {
        String validTemporaryCode = "T00000000000001";
        String updatedURI = APIConstants.GET_CUS_BASIC_INFO_TEMPORARY_CODE.replace("{code}", validTemporaryCode);

        Response responseB = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("\n" + "Status Code: " + responseB.getStatusCode());
        responseB.getBody().prettyPrint();
        Assert.assertEquals(responseB.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = responseB.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.customerId").toString()), PropertiesUtils.searchId, "Invalid customerId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.permanentCode").toString()), PropertiesUtils.searchPermCode , "Invalid customerCode");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.leadCode").toString()), PropertiesUtils.searchLeadCode, "Invalid leadCode");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.temporaryCode").toString()), PropertiesUtils.searchTempCode , "Invalid temporaryCode");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.customerTypeCode").toString()), PropertiesUtils.searchType, "Invalid customerTypeCode");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.uwCategories[0]").toString()), PropertiesUtils.searchUwCategories, "Invalid uwCategories");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.customerPortalAccess").toString()), PropertiesUtils.searchPortalAccess, "Invalid customerPortalAccess");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.firstName").toString()), PropertiesUtils.searchName, "Invalid firstName");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.middleName").toString()), PropertiesUtils.searchName, "Invalid middleName");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.lastName").toString()), PropertiesUtils.searchName, "Invalid lastName");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.callingName").toString()), PropertiesUtils.searchName, "Invalid callingName");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.surnameWithInitials").toString()), PropertiesUtils.getSurnameWithInitials, "Invalid surnameWithInitials");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.salutation").toString()), PropertiesUtils.getSalutation, "Invalid salutation");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.salutationOther").toString()), PropertiesUtils.searchSalutationOther, "Invalid salutationOther");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.gender").toString()), PropertiesUtils.searchGender, "Invalid gender");
        //softAssert.assertEquals((JsonPath.read(responseToString, "$.result.customerGroups[0]").toString()), PropertiesUtils.searchCustomerGroups, "Invalid customerGroups");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.dob").toString()), PropertiesUtils.searchDob, "Invalid dob");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.nationality").toString()), PropertiesUtils.searchNationality, "Invalid nationality");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.civilStatusId").toString()), PropertiesUtils.searchCivilStatus, "Invalid civilStatusId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.policyHolderPreferredLanguage").toString()), PropertiesUtils.searchLang, "Invalid policyHolderPreferredLanguage");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.idNumbers[0].identityId").toString()), PropertiesUtils.searchIdentityId, "Invalid identityId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.idNumbers[0].idType").toString()), PropertiesUtils.getIdType, "Invalid idType");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.idNumbers[0].idNumber").toString()), PropertiesUtils.searchIdNumber, "Invalid idNumber");
        try{
            softAssert.assertEquals((JsonPath.read(responseToString, "$.result.debtorCode").toString()), null , "Invalid debtorCode");
        }
        catch (Exception e){
        }
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-369 [R1][CRM_API] Verify the GET - get_customer_basic_info with invalid Temporary Code")
    public void testGetCustomerBasicInfoWithInvalidTemporaryCode() {
        String invalidTempCode = "@QA99999";
        String updatedURI = APIConstants.GET_CUS_BASIC_INFO_TEMPORARY_CODE.replace("{code}", invalidTempCode);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("\n" + "Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);

        String statusCode = String.valueOf(response.getStatusCode());
        SoftAssert softAssert = new SoftAssert();
        String responseToString = response.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), statusCode, "Invalid status code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "CM_M_CUSTOMER_NOT_FOUND", "Invalid error");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "There is no customer related data for the given temporary code - " + invalidTempCode, "Invalid description");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-378 [R1][CRM_API] Verify the GET - get_customer_basic_info with valid Permanent Code")
    public void testGetCustomerBasicInfoWithValidPermanentCode() {
        String validPermanentCode = "P00000000000001";
        String updatedURI = APIConstants.GET_CUS_BASIC_INFO_PERMANENT_CODE.replace("{code}", validPermanentCode);

        Response responseB = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("\n" + "Status Code: " + responseB.getStatusCode());
        responseB.getBody().prettyPrint();
        Assert.assertEquals(responseB.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = responseB.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.customerId").toString()), PropertiesUtils.searchId, "Invalid customerId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.permanentCode").toString()), PropertiesUtils.searchPermCode , "Invalid customerCode");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.leadCode").toString()), PropertiesUtils.searchLeadCode, "Invalid leadCode");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.temporaryCode").toString()), PropertiesUtils.searchTempCode , "Invalid temporaryCode");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.customerTypeCode").toString()), PropertiesUtils.searchType, "Invalid customerTypeCode");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.uwCategories[0]").toString()), PropertiesUtils.searchUwCategories, "Invalid uwCategories");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.customerPortalAccess").toString()), PropertiesUtils.searchPortalAccess, "Invalid customerPortalAccess");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.firstName").toString()), PropertiesUtils.searchName, "Invalid firstName");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.middleName").toString()), PropertiesUtils.searchName, "Invalid middleName");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.lastName").toString()), PropertiesUtils.searchName, "Invalid lastName");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.callingName").toString()), PropertiesUtils.searchName, "Invalid callingName");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.surnameWithInitials").toString()), PropertiesUtils.getSurnameWithInitials, "Invalid surnameWithInitials");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.salutation").toString()), PropertiesUtils.getSalutation, "Invalid salutation");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.salutationOther").toString()), PropertiesUtils.searchSalutationOther, "Invalid salutationOther");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.gender").toString()), PropertiesUtils.searchGender, "Invalid gender");
        //softAssert.assertEquals((JsonPath.read(responseToString, "$.result.customerGroups[0]").toString()), PropertiesUtils.searchCustomerGroups, "Invalid customerGroups");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.dob").toString()), PropertiesUtils.searchDob, "Invalid dob");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.nationality").toString()), PropertiesUtils.searchNationality, "Invalid nationality");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.civilStatusId").toString()), PropertiesUtils.searchCivilStatus, "Invalid civilStatusId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.policyHolderPreferredLanguage").toString()), PropertiesUtils.searchLang, "Invalid policyHolderPreferredLanguage");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.idNumbers[0].identityId").toString()), PropertiesUtils.searchIdentityId, "Invalid identityId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.idNumbers[0].idType").toString()), PropertiesUtils.getIdType, "Invalid idType");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.idNumbers[0].idNumber").toString()), PropertiesUtils.searchIdNumber, "Invalid idNumber");
        try{
            softAssert.assertEquals((JsonPath.read(responseToString, "$.result.debtorCode").toString()), null , "Invalid debtorCode");
        }
        catch (Exception e){
        }
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-495 [R1][CRM_API] Verify the GET - get_customer_basic_info with invalid Permanent Code")
    public void testGetCustomerBasicInfoWithInvalidPermanentCode() {
        String invalidPermCode = "@QA99999";
        String updatedURI = APIConstants.GET_CUS_BASIC_INFO_PERMANENT_CODE.replace("{code}", invalidPermCode);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("\n" + "Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);

        String statusCode = String.valueOf(response.getStatusCode());
        SoftAssert softAssert = new SoftAssert();
        String responseToString = response.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), statusCode, "Invalid status code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "CM_M_CUSTOMER_NOT_FOUND", "Invalid error");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "There is no customer related data for the given permanent code - " + invalidPermCode, "Invalid description");
        softAssert.assertAll();
    }

}

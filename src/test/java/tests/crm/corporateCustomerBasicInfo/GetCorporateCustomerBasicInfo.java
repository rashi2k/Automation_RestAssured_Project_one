package tests.crm.corporateCustomerBasicInfo;

import com.informatics.endpoints.APIConstants;
import com.informatics.pojos.crm.CoporateCutomerBasicInfo;
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

public class GetCorporateCustomerBasicInfo extends BaseTest {

    // ParentCompanyId = CustomerId
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
    @Description("IQ-980 [R3] [CRM_API] Verify GET - get customer details (coporate) with Valid id(customerId)")
    public void testGetCorporateCustomerBasicInfoWithValidId() {
        CoporateCutomerBasicInfo coporateCutomerBasicInfo  = requestPayload.coporateCutomerBasicInfo(corporateCustomerType, businessTypeId, uwCategoryId, customerId);
        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(coporateCutomerBasicInfo)
                .post(APIConstants.CREATE_CORPORATE_CUSTOMER);
        System.out.println("\n" + "Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        String saveCustomerId = JsonPath.read(resCreate.asString(), "$.result.customerId").toString();
        String saveCustomerLeadCode = JsonPath.read(resCreate.asString(), "$.result.leadCode").toString();
        String updatedURI = APIConstants.GET_CUS_BASIC_INFO_ID.replace("{id}", saveCustomerId);

        Response resGet = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("\n" + "Status Code: " + resGet.getStatusCode());
        resGet.getBody().prettyPrint();
        Assert.assertEquals(resGet.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = resGet.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.customerId").toString()), saveCustomerId, "Invalid customerId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.leadCode").toString()), saveCustomerLeadCode, "Invalid leadCode");
        try{
            softAssert.assertEquals((JsonPath.read(responseToString, "$.result.permanentCode")).toString(), null , "Invalid permanentCode");
            softAssert.assertEquals((JsonPath.read(responseToString, "$.result.temporaryCode").toString()), null , "Invalid temporaryCode");
            softAssert.assertEquals((JsonPath.read(responseToString, "$.result.debtorCode").toString()), null, "Invalid debtorCode");
            softAssert.assertEquals((JsonPath.read(responseToString, "$.result.parentCompanyId").toString()), null, "Invalid parentCompanyId");
        }
        catch (Exception e) {
        }
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.customerTypeCode").toString()), String.valueOf(coporateCutomerBasicInfo.getCustomerTypeCode()), "Invalid customerTypeCode");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.uwCategories").toString()), String.valueOf(coporateCutomerBasicInfo.getUwCategories()), "Invalid uwCategories");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.customerPortalAccess").toString()), PropertiesUtils.corporateCustomerPortalAccess, "Invalid customerPortalAccess");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.name").toString()), coporateCutomerBasicInfo.getName(), "Invalid name");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.registrationNumber").toString()), coporateCutomerBasicInfo.getRegistrationNumber(), "Invalid registrationNumber");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.businessTypeId").toString()), String.valueOf(coporateCutomerBasicInfo.getBusinessTypeId()), "Invalid businessTypeId");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-981 [R3] [CRM_API] Verify GET - get customer details (coporate) with Null id(customerId)")
    public void testGetCorporateCustomerBasicInfoWithNullId() {
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
    @Description("IQ-982 [R3] [CRM_API] Verify GET - get customer details (coporate) with Invalid id(customerId)")
    public void testGetCorporateCustomerBasicInfoWithInvalidId() {
        String invalidId = "9A";
        String updatedURI = APIConstants.GET_CUS_BASIC_INFO_ID.replace("{id}", invalidId);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("\n" + "Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);
    }


    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-983 [R3] [CRM_API] Verify GET - get customer details (coporate) with Valid code(leadCode)")
    public void testGetCorporateCustomerBasicInfoWithValidLeadCode() {
        CoporateCutomerBasicInfo coporateCutomerBasicInfo  = requestPayload.coporateCutomerBasicInfo(corporateCustomerType, businessTypeId, uwCategoryId, customerId);

        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(coporateCutomerBasicInfo)
                .post(APIConstants.CREATE_CORPORATE_CUSTOMER);
        System.out.println("\n" + "Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        String saveCustomerId = JsonPath.read(resCreate.asString(), "$.result.customerId").toString();
        String saveCustomerLeadCode = JsonPath.read(resCreate.asString(), "$.result.leadCode").toString();
        String updatedURI = APIConstants.GET_CUS_BASIC_INFO_LEAD_CODE.replace("{code}", saveCustomerLeadCode);

        Response resGet = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("\n" + "Status Code: " + resGet.getStatusCode());
        resGet.getBody().prettyPrint();
        Assert.assertEquals(resGet.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = resGet.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.customerId").toString()), saveCustomerId, "Invalid customerId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.leadCode").toString()), saveCustomerLeadCode, "Invalid leadCode");
        try{
            softAssert.assertEquals((JsonPath.read(responseToString, "$.result.permanentCode").toString()), null , "Invalid permanentCode");
            softAssert.assertEquals((JsonPath.read(responseToString, "$.result.temporaryCode").toString()), null , "Invalid temporaryCode");
            softAssert.assertEquals((JsonPath.read(responseToString, "$.result.debtorCode").toString()), null, "Invalid debtorCode");
            softAssert.assertEquals((JsonPath.read(responseToString, "$.result.parentCompanyId").toString()), null, "Invalid parentCompanyId");
        }
        catch (Exception e) {
        }
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.customerTypeCode").toString()), String.valueOf(coporateCutomerBasicInfo.getCustomerTypeCode()), "Invalid customerTypeCode");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.uwCategories").toString()), String.valueOf(coporateCutomerBasicInfo.getUwCategories()), "Invalid uwCategories");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.customerPortalAccess").toString()), PropertiesUtils.corporateCustomerPortalAccess, "Invalid customerPortalAccess");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.name").toString()), coporateCutomerBasicInfo.getName(), "Invalid name");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.registrationNumber").toString()), coporateCutomerBasicInfo.getRegistrationNumber(), "Invalid registrationNumber");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.businessTypeId").toString()), String.valueOf(coporateCutomerBasicInfo.getBusinessTypeId()), "Invalid businessTypeId");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-985 [R3] [CRM_API] Verify GET - get customer details (coporate) with Null code(leadCode)")
    public void testGetCorporateCustomerBasicInfoWithNullLeadCode() {
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
    @Description("IQ-989 [R3] [CRM_API] Verify GET - get customer details (coporate) with Invalid code(leadCode)")
    public void testGetCorporateCustomerBasicInfoWithInvalidLeadCode() {
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
    @Description("IQ-986 [R3] [CRM_API] Verify GET - get customer details (coporate) with Valid code(temporaryCode)")
    public void testGetCorporateCustomerBasicInfoWithValidTemporaryCode() {
        String validTemporaryCode = "T00000000000002";
        String updatedURI = APIConstants.GET_CUS_BASIC_INFO_TEMPORARY_CODE.replace("{code}", validTemporaryCode);

        Response resGet = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("\n" + "Status Code: " + resGet.getStatusCode());
        resGet.getBody().prettyPrint();
        Assert.assertEquals(resGet.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = resGet.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.customerId").toString()), PropertiesUtils.searchCorporateCustomerId, "Invalid customerId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.permanentCode").toString()), PropertiesUtils.searchCorporateCustomerPermanentCode, "Invalid permanentCode");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.leadCode").toString()), PropertiesUtils.searchCorporateCustomerLeadCode, "Invalid leadCode");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.temporaryCode").toString()), PropertiesUtils.searchCorporateCustomerTemporaryCode, "Invalid temporaryCode");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.customerTypeCode").toString()), PropertiesUtils.searchCorporateCustomerTypeCode, "Invalid customerTypeCode");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.uwCategories[0]").toString()), PropertiesUtils.searchCorporateCustomerUwCategories, "Invalid uwCategories");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.customerPortalAccess").toString()), PropertiesUtils.searchCorporateCustomerPortalAccess, "Invalid customerPortalAccess");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.name").toString()), PropertiesUtils.searchCorporateCustomerName, "Invalid name");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.registrationNumber").toString()), PropertiesUtils.searchCorporateCustomerRegNumber, "Invalid registrationNumber");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.businessTypeId").toString()), PropertiesUtils.searchCorporateCustomerBusinessTypeId, "Invalid businessTypeId");
        try{
            softAssert.assertEquals((JsonPath.read(responseToString, "$.result.debtorCode").toString()), null, "Invalid debtorCode");
            softAssert.assertEquals((JsonPath.read(responseToString, "$.result.customerNatureId").toString()), null, "Invalid customerNatureId");
            softAssert.assertEquals((JsonPath.read(responseToString, "$.result.parentCompanyId").toString()), null, "Invalid parentCompanyId");
        }
        catch (Exception e) {
        }
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-990 [R3] [CRM_API] Verify GET - get customer details (coporate) with Invalid code(temporaryCode)")
    public void testGetCorporateCustomerBasicInfoWithInvalidTemporaryCode() {
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
    @Description("IQ-984 [R3] [CRM_API] Verify GET - get customer details (coporate) with Null code(temporaryCode))")
    public void testGetCorporateCustomerBasicInfoWithNullTemporaryCode() {
        String updatedURI = APIConstants.GET_CUS_BASIC_INFO_TEMPORARY_CODE.replace("{code}", "");

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("\n" + "Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-987 [R3] [CRM_API] Verify GET - get customer details (coporate) with Valid code(permanentCode)")
    public void testGetCorporateCustomerBasicInfoWithValidPermanentCode() {
        String validPermanentCode = "P00000000000002";
        String updatedURI = APIConstants.GET_CUS_BASIC_INFO_PERMANENT_CODE.replace("{code}", validPermanentCode);

        Response resGet = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("\n" + "Status Code: " + resGet.getStatusCode());
        resGet.getBody().prettyPrint();
        Assert.assertEquals(resGet.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = resGet.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.customerId").toString()), PropertiesUtils.searchCorporateCustomerId, "Invalid customerId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.permanentCode").toString()), PropertiesUtils.searchCorporateCustomerPermanentCode, "Invalid permanentCode");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.leadCode").toString()), PropertiesUtils.searchCorporateCustomerLeadCode, "Invalid leadCode");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.temporaryCode").toString()), PropertiesUtils.searchCorporateCustomerTemporaryCode, "Invalid temporaryCode");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.customerTypeCode").toString()), PropertiesUtils.searchCorporateCustomerTypeCode, "Invalid customerTypeCode");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.uwCategories[0]").toString()), PropertiesUtils.searchCorporateCustomerUwCategories, "Invalid uwCategories");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.customerPortalAccess").toString()), PropertiesUtils.searchCorporateCustomerPortalAccess, "Invalid customerPortalAccess");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.name").toString()), PropertiesUtils.searchCorporateCustomerName, "Invalid name");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.registrationNumber").toString()), PropertiesUtils.searchCorporateCustomerRegNumber, "Invalid registrationNumber");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.businessTypeId").toString()), PropertiesUtils.searchCorporateCustomerBusinessTypeId, "Invalid businessTypeId");
        try{
            softAssert.assertEquals((JsonPath.read(responseToString, "$.result.debtorCode").toString()), null, "Invalid debtorCode");
            softAssert.assertEquals((JsonPath.read(responseToString, "$.result.customerNatureId").toString()), null, "Invalid customerNatureId");
            softAssert.assertEquals((JsonPath.read(responseToString, "$.result.parentCompanyId").toString()), null, "Invalid parentCompanyId");
        }
        catch (Exception e) {
        }
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-991 [R3] [CRM_API] Verify GET - get customer details (coporate) with Invalid code(permanentCode)")
    public void testGetCorporateCustomerBasicInfoWithInvalidPermanentCode() {
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

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-988 [R3] [CRM_API] Verify GET - get customer details (coporate) with Null code(permanentCode)")
    public void testGetCorporateCustomerBasicInfoWithNullPermanentCode() {
        String updatedURI = APIConstants.GET_CUS_BASIC_INFO_PERMANENT_CODE.replace("{code}", "");

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("\n" + "Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);
    }

}

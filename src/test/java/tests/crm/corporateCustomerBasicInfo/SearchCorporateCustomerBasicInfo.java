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

public class SearchCorporateCustomerBasicInfo extends BaseTest {

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

    /*
    //TCs  deleted by QA
    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-992 [R3] [CRM_API] Verify GET - Search_corporate_customer (corporate/special) with Valid orgname")
    public void testSearchCorporateCustomerBasicInfoWithValidOrgName() {
        CoporateCutomerBasicInfo coporateCutomerBasicInfo  = requestPayload.coporateCutomerBasicInfo(corporateCustomerType, businessTypeId, uwCategoryId,customerId);

        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(coporateCutomerBasicInfo)
                .post(APIConstants.CREATE_CORPORATE_CUSTOMER);
        System.out.println("\n" + "Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        String saveOrgName = coporateCutomerBasicInfo.getName();
        String saveCustomerId = JsonPath.read(resCreate.asString(), "$.result.customerId").toString();
        String saveCustomerLeadCode = JsonPath.read(resCreate.asString(), "$.result.leadCode").toString();
        String updatedURI = APIConstants.SEARCH_CORPORATE_CUSTOMER.replace("{searchBy}", saveOrgName) + "?page=0&size=100000";

        Response resGet = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("\n" + "Status Code: " + resGet.getStatusCode());
        resGet.getBody().prettyPrint();
        Assert.assertEquals(resGet.statusCode(), 200);

        String responseToString = resGet.asString();
        Boolean isCustomerFound = false;
        int length = JsonPath.read(responseToString, "$.result.length()");

        SoftAssert softAssert = new SoftAssert();
        for (int i = 0; i < length; i++) {
            String leadCode = JsonPath.read(responseToString, "$.result[" + i + "].leadCode").toString();
            if (leadCode.equals(saveCustomerLeadCode)) {
                isCustomerFound = true;
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].id").toString()), saveCustomerId, "Invalid id");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].leadCode").toString()), saveCustomerLeadCode, "Invalid leadCode");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].name").toString()), coporateCutomerBasicInfo.getName(), "Invalid name");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].registrationNumber").toString()), coporateCutomerBasicInfo.getRegistrationNumber(), "Invalid registrationNumber");
                try{
                    softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].customerCode").toString()), null , "Invalid customerCode");
                    softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].temporaryCode").toString()), null , "Invalid temporaryCode");
                }
                catch (Exception e) {
                }
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].parentCompanyId").toString()), customerId, "Invalid parentCompanyId");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].businessTypeId").toString()), String.valueOf(coporateCutomerBasicInfo.getBusinessTypeId()), "Invalid businessTypeId");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].uwCategories").toString()), String.valueOf(coporateCutomerBasicInfo.getUwCategories()), "Invalid uwCategories");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].customerStatus").toString()), PropertiesUtils.corporateCustomerStatus, "Invalid customerStatus");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].profileStatus").toString()), PropertiesUtils.corporateCustomerProfileStatus, "Invalid profileStatus");
                softAssert.assertAll();
            }
        }
        softAssert.assertTrue(isCustomerFound, "Customer not found");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-993 [R3] [CRM_API] Verify GET - search_corporate_customer (corporate/special) with Invalid orgname")
    public void testSearchCorporateCustomerBasicInfoWithInvalidOrgName() {
        String invalidOrgName = "Err@44sad@";
        String updatedURI = APIConstants.SEARCH_CORPORATE_CUSTOMER.replace("{searchBy}", invalidOrgName) + "?page=0&size=100000";

        Response resGet = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("\n" + "Status Code: " + resGet.getStatusCode());
        resGet.getBody().prettyPrint();
        Assert.assertEquals(resGet.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = resGet.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result").toString()), "[]", "Invalid result");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-994 [R3] [CRM_API] Verify GET - search_corporate_customer (coporate/special) with null value")
    public void testSearchCorporateCustomerBasicInfoWithNullValue() {
        String updatedURI = APIConstants.SEARCH_CORPORATE_CUSTOMER.replace("{searchBy}", "") + "?page=0&size=100000";

        Response resGet = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("\n" + "Status Code: " + resGet.getStatusCode());
        resGet.getBody().prettyPrint();
        Assert.assertEquals(resGet.statusCode(), 404);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-995 [R3] [CRM_API] Verify GET - search_corporate_customer (corporate/special) with similar orgname")
    public void testSearchCorporateCustomerBasicInfoWithSimilarOrgName() {
        CoporateCutomerBasicInfo coporateCutomerBasicInfo  = requestPayload.coporateCutomerBasicInfo(corporateCustomerType, businessTypeId, uwCategoryId,customerId);

        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(coporateCutomerBasicInfo)
                .post(APIConstants.CREATE_CORPORATE_CUSTOMER);
        System.out.println("\n" + "Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        String saveOrgName = coporateCutomerBasicInfo.getName();
        String saveCustomerId = JsonPath.read(resCreate.asString(), "$.result.customerId").toString();
        String saveCustomerLeadCode = JsonPath.read(resCreate.asString(), "$.result.leadCode").toString();
        String updatedURI = APIConstants.SEARCH_CORPORATE_CUSTOMER.replace("{searchBy}", saveOrgName.substring(0,12)) + "?page=0&size=100000";

        Response resGet = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("\n" + "Status Code: " + resGet.getStatusCode());
        resGet.getBody().prettyPrint();
        Assert.assertEquals(resGet.statusCode(), 200);

        String responseToString = resGet.asString();
        Boolean isCustomerFound = false;
        int length = JsonPath.read(responseToString, "$.result.length()");

        SoftAssert softAssert = new SoftAssert();
        for (int i = 0; i < length; i++) {
            String leadCode = JsonPath.read(responseToString, "$.result[" + i + "].leadCode").toString();
            if (leadCode.equals(saveCustomerLeadCode)) {
                isCustomerFound = true;
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].id").toString()), saveCustomerId, "Invalid id");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].leadCode").toString()), saveCustomerLeadCode, "Invalid leadCode");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].name").toString()), coporateCutomerBasicInfo.getName(), "Invalid name");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].registrationNumber").toString()), coporateCutomerBasicInfo.getRegistrationNumber(), "Invalid registrationNumber");
                try{
                    softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].customerCode").toString()), null , "Invalid customerCode");
                    softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].temporaryCode").toString()), null , "Invalid temporaryCode");
                }
                catch (Exception e) {
                }
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].parentCompanyId").toString()), customerId, "Invalid parentCompanyId");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].businessTypeId").toString()), String.valueOf(coporateCutomerBasicInfo.getBusinessTypeId()), "Invalid businessTypeId");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].uwCategories").toString()), String.valueOf(coporateCutomerBasicInfo.getUwCategories()), "Invalid uwCategories");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].customerStatus").toString()), PropertiesUtils.corporateCustomerStatus, "Invalid customerStatus");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].profileStatus").toString()), PropertiesUtils.corporateCustomerProfileStatus, "Invalid profileStatus");
                softAssert.assertAll();
            }
        }
        softAssert.assertTrue(isCustomerFound, "Customer not found");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-997 [R3] [CRM_API] Verify GET - search_corporate_customer (corporate/special) with valid business reg number")
    public void testSearchCorporateCustomerBasicInfoWithValidBusinessRegNumber() {
        CoporateCutomerBasicInfo coporateCutomerBasicInfo  = requestPayload.coporateCutomerBasicInfo(corporateCustomerType, businessTypeId, uwCategoryId,customerId);

        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(coporateCutomerBasicInfo)
                .post(APIConstants.CREATE_CORPORATE_CUSTOMER);
        System.out.println("\n" + "Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        String saveRegNo = coporateCutomerBasicInfo.getRegistrationNumber();
        String saveCustomerId = JsonPath.read(resCreate.asString(), "$.result.customerId").toString();
        String saveCustomerLeadCode = JsonPath.read(resCreate.asString(), "$.result.leadCode").toString();
        String updatedURI = APIConstants.SEARCH_CORPORATE_CUSTOMER.replace("{searchBy}", saveRegNo) + "?page=0&size=100000";

        Response resGet = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("\n" + "Status Code: " + resGet.getStatusCode());
        resGet.getBody().prettyPrint();
        Assert.assertEquals(resGet.statusCode(), 200);

        String responseToString = resGet.asString();
        Boolean isCustomerFound = false;
        int length = JsonPath.read(responseToString, "$.result.length()");

        SoftAssert softAssert = new SoftAssert();
        for (int i = 0; i < length; i++) {
            String leadCode = JsonPath.read(responseToString, "$.result[" + i + "].leadCode").toString();
            if (leadCode.equals(saveCustomerLeadCode)) {
                isCustomerFound = true;
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].id").toString()), saveCustomerId, "Invalid id");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].leadCode").toString()), saveCustomerLeadCode, "Invalid leadCode");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].name").toString()), coporateCutomerBasicInfo.getName(), "Invalid name");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].registrationNumber").toString()), coporateCutomerBasicInfo.getRegistrationNumber(), "Invalid registrationNumber");
                try{
                    softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].customerCode").toString()), null , "Invalid customerCode");
                    softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].temporaryCode").toString()), null , "Invalid temporaryCode");
                }
                catch (Exception e) {
                }
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].parentCompanyId").toString()), customerId, "Invalid parentCompanyId");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].businessTypeId").toString()), String.valueOf(coporateCutomerBasicInfo.getBusinessTypeId()), "Invalid businessTypeId");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].uwCategories").toString()), String.valueOf(coporateCutomerBasicInfo.getUwCategories()), "Invalid uwCategories");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].customerStatus").toString()), PropertiesUtils.corporateCustomerStatus, "Invalid customerStatus");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].profileStatus").toString()), PropertiesUtils.corporateCustomerProfileStatus, "Invalid profileStatus");
                softAssert.assertAll();
            }
        }
        softAssert.assertTrue(isCustomerFound, "Customer not found");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-998 [R3] [CRM_API] Verify GET - search_corporate_customer (corporate/special) with Invalid business reg number")
    public void testSearchCorporateCustomerBasicInfoWithInvalidBusinessRegNumber() {
        String invalidRegName = "99$@$46";
        String updatedURI = APIConstants.SEARCH_CORPORATE_CUSTOMER.replace("{searchBy}", invalidRegName) + "?page=0&size=100000";

        Response resGet = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("\n" + "Status Code: " + resGet.getStatusCode());
        resGet.getBody().prettyPrint();
        Assert.assertEquals(resGet.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = resGet.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result").toString()), "[]", "Invalid result");
        softAssert.assertAll();
    }
    */

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-1876 [R3] [CRM_API] Verify the GET - Search_corporate_customer with Valid corporateCustomerId, parentId")
    public void testSearchCorporateCustomerBasicInfoWithValidCorporateCustomerIdAndParentId() {
        CoporateCutomerBasicInfo coporateCutomerBasicInfo  = requestPayload.coporateCutomerBasicInfo(corporateCustomerType, businessTypeId, uwCategoryId,customerId);

        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(coporateCutomerBasicInfo)
                .post(APIConstants.CREATE_CORPORATE_CUSTOMER);
        System.out.println("\n" + "Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        String saveCustomerId = JsonPath.read(resCreate.asString(), "$.result.customerId").toString();
        String saveCorporateCustomerId = JsonPath.read(resCreate.asString(), "$.result.corporateCustomerId").toString();
        String saveCustomerLeadCode = JsonPath.read(resCreate.asString(), "$.result.leadCode").toString();

        //Valid CorporateCustomerId
        System.out.println("\n" + "--------------------Valid CorporateCustomerId------------------- ");
        String customerBody = "corporateCustomerId.equals=" + saveCorporateCustomerId + "&distinct=true";
        String pagination = "&pageNumber=0&pageSize=10&paged=true";
        String updateSearchCustomerURI = APIConstants.SEARCH_CORPORATE_CUSTOMER_CRITERIA + "?" + customerBody + pagination;

        Response resSearchCustomer = RestAssured.given().spec(repoSpec)
                .when()
                .post(updateSearchCustomerURI);
        System.out.println("\n" + "Status Code: " + resSearchCustomer.getStatusCode());
        resSearchCustomer.getBody().prettyPrint();
        Assert.assertEquals(resSearchCustomer.statusCode(), 200);

        String responseToString = resSearchCustomer.asString();
        Boolean isCustomerFound = false;
        int length = JsonPath.read(responseToString, "$.result.length()");

        SoftAssert softAssert = new SoftAssert();
        for (int i = 0; i < length; i++) {
            String corporateCustomerId = JsonPath.read(responseToString, "$.result[" + i + "].corporateCustomerId").toString();
            if (corporateCustomerId.equals(saveCorporateCustomerId)) {
                isCustomerFound = true;
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].customerId").toString()), saveCustomerId, "Invalid customerId");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].leadCode").toString()), saveCustomerLeadCode, "Invalid leadCode");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].name").toString()), coporateCutomerBasicInfo.getName(), "Invalid name");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].registrationNumber").toString()), coporateCutomerBasicInfo.getRegistrationNumber(), "Invalid registrationNumber");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].customerTypeCode").toString()), coporateCutomerBasicInfo.getCustomerTypeCode(), "Invalid customerTypeCode");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].businessTypeId").toString()), String.valueOf(coporateCutomerBasicInfo.getBusinessTypeId()), "Invalid businessTypeId");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].uwCategories").toString()), String.valueOf(coporateCutomerBasicInfo.getUwCategories()), "Invalid uwCategories");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].parentCompanyId").toString()), String.valueOf(customerId), "Invalid parentCompanyId");
                try {
                    softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].permanentCode").toString()), null, "Invalid permanentCode");
                    softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].temporaryCode").toString()), null, "Invalid temporaryCode");
                    softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].parentId").toString()), null, "Invalid parentId");
                    softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].customerGroups").toString()), null, "Invalid customerGroups");
                    softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].policyholderPreferredLanguage").toString()), null, "Invalid policyholderPreferredLanguage");
                    softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].customerPortalAccess").toString()), null, "Invalid customerPortalAccess");
                } catch (Exception e) {
                }
                softAssert.assertAll();
            }
        }
        softAssert.assertTrue(isCustomerFound, "Customer not found");

        //Valid ParentId
        System.out.println("\n" + "--------------------Valid ParentId------------------- ");
        String parentBody = "parentId.equals=" + saveCustomerId + "&distinct=true";
        String updateSearchParentURI = APIConstants.SEARCH_CORPORATE_CUSTOMER_CRITERIA + "?" + parentBody + pagination;

        Response resSearchParent = RestAssured.given().spec(repoSpec)
                .when()
                .post(updateSearchParentURI);
        System.out.println("\n" + "Status Code: " + resSearchParent.getStatusCode());
        resSearchParent.getBody().prettyPrint();
        Assert.assertEquals(resSearchCustomer.statusCode(), 200);

        String responseToStringReg = resSearchParent.asString();
        Boolean isCustomersFound = false;
        int len = JsonPath.read(responseToStringReg, "$.result.length()");

        for (int i = 0; i < len; i++) {
            String corporateCustomerId = JsonPath.read(responseToString, "$.result[" + i + "].corporateCustomerId").toString();
            if (corporateCustomerId.equals(saveCorporateCustomerId)) {
                isCustomerFound = true;
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].customerId").toString()), saveCustomerId, "Invalid customerId");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].leadCode").toString()), saveCustomerLeadCode, "Invalid leadCode");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].name").toString()), coporateCutomerBasicInfo.getName(), "Invalid name");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].registrationNumber").toString()), coporateCutomerBasicInfo.getRegistrationNumber(), "Invalid registrationNumber");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].customerTypeCode").toString()), coporateCutomerBasicInfo.getCustomerTypeCode(), "Invalid customerTypeCode");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].businessTypeId").toString()), String.valueOf(coporateCutomerBasicInfo.getBusinessTypeId()), "Invalid businessTypeId");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].uwCategories").toString()), String.valueOf(coporateCutomerBasicInfo.getUwCategories()), "Invalid uwCategories");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].parentCompanyId").toString()), String.valueOf(customerId), "Invalid parentCompanyId");
                try {
                    softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].permanentCode").toString()), null, "Invalid permanentCode");
                    softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].temporaryCode").toString()), null, "Invalid temporaryCode");
                    softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].parentId").toString()), null, "Invalid parentId");
                    softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].customerGroups").toString()), null, "Invalid customerGroups");
                    softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].policyholderPreferredLanguage").toString()), null, "Invalid policyholderPreferredLanguage");
                    softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].customerPortalAccess").toString()), null, "Invalid customerPortalAccess");
                } catch (Exception e) {
                }
                softAssert.assertAll();
            }
        }
        softAssert.assertTrue(isCustomersFound, "Customer not found");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-1877 [R3] [CRM_API] Verify the GET - Search_corporate_customer with null corporateCustomerId, parentId")
    public void testSearchCorporateCustomerBasicInfoWithInvalidCorporateCustomerIdAndParentId() {
        //Invalid CorporateCustomerId
        System.out.println("\n" + "--------------------Invalid CorporateCustomerId------------------- ");
        Integer invalidId = 9999999;
        String customerBody = "corporateCustomerId.equals=" + invalidId + "&distinct=true";
        String pagination = "&pageNumber=0&pageSize=10&paged=true";
        String updateSearchCustomerURI = APIConstants.SEARCH_CORPORATE_CUSTOMER_CRITERIA + "?" + customerBody + pagination;

        Response resSearchCustomer = RestAssured.given().spec(repoSpec)
                .when()
                .post(updateSearchCustomerURI);
        System.out.println("\n" + "Status Code: " + resSearchCustomer.getStatusCode());
        resSearchCustomer.getBody().prettyPrint();
        Assert.assertEquals(resSearchCustomer.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals((JsonPath.read(resSearchCustomer.asString(), "$.result").toString()), "[]", "Invalid result");
        softAssert.assertAll();

        //Invalid ParentId
        System.out.println("\n" + "--------------------Invalid ParentId------------------- ");
        String parentBody = "parentId.equals=" + invalidId + "&distinct=true";
        String updateSearchParentURI = APIConstants.SEARCH_CORPORATE_CUSTOMER_CRITERIA + "?" + parentBody + pagination;

        Response resSearchParent = RestAssured.given().spec(repoSpec)
                .when()
                .post(updateSearchParentURI);
        System.out.println("\n" + "Status Code: " + resSearchParent.getStatusCode());
        resSearchParent.getBody().prettyPrint();
        Assert.assertEquals(resSearchCustomer.statusCode(), 200);

        softAssert.assertEquals((JsonPath.read(resSearchParent.asString(), "$.result").toString()), "[]", "Invalid result");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-1878 [R3] [CRM_API] Verify the GET - Search_corporate_customer with null corporateCustomerId, parentId")
    public void testSearchCorporateCustomerBasicInfoWithNullCorporateCustomerIdAndParentId() {
        //null CorporateCustomerId
        System.out.println("\n" + "--------------------null CorporateCustomerId------------------- ");
        Integer invalidId = 9999999;
        String customerBody = "corporateCustomerId.equals=" + null + "&distinct=true";
        String pagination = "&pageNumber=0&pageSize=10&paged=true";
        String updateSearchCustomerURI = APIConstants.SEARCH_CORPORATE_CUSTOMER_CRITERIA + "?" + customerBody + pagination;

        Response resSearchCustomer = RestAssured.given().spec(repoSpec)
                .when()
                .post(updateSearchCustomerURI);
        System.out.println("\n" + "Status Code: " + resSearchCustomer.getStatusCode());
        resSearchCustomer.getBody().prettyPrint();
        Assert.assertEquals(resSearchCustomer.statusCode(), 400);

        //null ParentId
        System.out.println("\n" + "--------------------null ParentId------------------- ");
        String parentBody = "parentId.equals=" + null + "&distinct=true";
        String updateSearchParentURI = APIConstants.SEARCH_CORPORATE_CUSTOMER_CRITERIA + "?" + parentBody + pagination;

        Response resSearchParent = RestAssured.given().spec(repoSpec)
                .when()
                .post(updateSearchParentURI);
        System.out.println("\n" + "Status Code: " + resSearchParent.getStatusCode());
        resSearchParent.getBody().prettyPrint();
        Assert.assertEquals(resSearchCustomer.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-1880 [R3] [CRM_API] Verify the GET - Search_corporate_customer with null businessName, businessRegNo")
    public void testSearchCorporateCustomerBasicInfoWithNullBusinessNameAndBusinessRegNo() {
        //null BusinessName
        System.out.println("\n" + "--------------------null BusinessName------------------- ");
        String customerBody = "businessName.contains=" + null + "&distinct=true";
        String pagination = "&pageNumber=0&pageSize=10&paged=true";
        String updateSearchBusinessURI = APIConstants.SEARCH_CORPORATE_CUSTOMER_CRITERIA + "?" + customerBody + pagination;

        Response resSearchBusiness = RestAssured.given().spec(repoSpec)
                .when()
                .post(updateSearchBusinessURI);
        System.out.println("\n" + "Status Code: " + resSearchBusiness.getStatusCode());
        resSearchBusiness.getBody().prettyPrint();
        Assert.assertEquals(resSearchBusiness.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals((JsonPath.read(resSearchBusiness.asString(), "$.result").toString()), "[]", "Invalid result");
        softAssert.assertAll();

        //null businessRegNo
        System.out.println("\n" + "--------------------null businessRegNo------------------- ");
        String parentBody = "businessRegNo.contains=" + null + "&distinct=true";
        String updateSearchRegURI = APIConstants.SEARCH_CORPORATE_CUSTOMER_CRITERIA + "?" + parentBody + pagination;

        Response resSearchReg = RestAssured.given().spec(repoSpec)
                .when()
                .post(updateSearchRegURI);
        System.out.println("\n" + "Status Code: " + resSearchReg.getStatusCode());
        resSearchReg.getBody().prettyPrint();
        Assert.assertEquals(resSearchReg.statusCode(), 200);

        softAssert.assertEquals((JsonPath.read(resSearchReg.asString(), "$.result").toString()), "[]", "Invalid result");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-1879 [R3] [CRM_API] Verify the GET - Search_corporate_customer with Valid businessName, businessRegNo")
    public void testSearchCorporateCustomerBasicInfoWithValidBusinessNameAndBusinessRegNo() {
        CoporateCutomerBasicInfo coporateCutomerBasicInfo  = requestPayload.coporateCutomerBasicInfo(corporateCustomerType, businessTypeId, uwCategoryId,customerId);

        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(coporateCutomerBasicInfo)
                .post(APIConstants.CREATE_CORPORATE_CUSTOMER);
        System.out.println("\n" + "Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        String saveBusinessName = coporateCutomerBasicInfo.getName();
        String saveBusinessRegNo = coporateCutomerBasicInfo.getRegistrationNumber();
        String saveCustomerId = JsonPath.read(resCreate.asString(), "$.result.customerId").toString();
        String saveCorporateCustomerId = JsonPath.read(resCreate.asString(), "$.result.corporateCustomerId").toString();
        String saveCustomerLeadCode = JsonPath.read(resCreate.asString(), "$.result.leadCode").toString();

        //Valid BusinessName
        System.out.println("\n" + "--------------------Valid BusinessName------------------- ");
        String customerBody = "businessName.contains=" + saveBusinessName + "&distinct=true";
        String pagination = "&pageNumber=0&pageSize=10&paged=true";
        String updateSearchBusinessURI = APIConstants.SEARCH_CORPORATE_CUSTOMER_CRITERIA + "?" + customerBody + pagination;

        Response resSearchBusiness = RestAssured.given().spec(repoSpec)
                .when()
                .post(updateSearchBusinessURI);
        System.out.println("\n" + "Status Code: " + resSearchBusiness.getStatusCode());
        resSearchBusiness.getBody().prettyPrint();
        Assert.assertEquals(resSearchBusiness.statusCode(), 200);

        String responseToString = resSearchBusiness.asString();
        Boolean isCustomerFound = false;
        int length = JsonPath.read(responseToString, "$.result.length()");

        SoftAssert softAssert = new SoftAssert();
        for (int i = 0; i < length; i++) {
            String corporateCustomerId = JsonPath.read(responseToString, "$.result[" + i + "].corporateCustomerId").toString();
            if (corporateCustomerId.equals(saveCorporateCustomerId)) {
                isCustomerFound = true;
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].customerId").toString()), saveCustomerId, "Invalid customerId");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].leadCode").toString()), saveCustomerLeadCode, "Invalid leadCode");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].name").toString()), coporateCutomerBasicInfo.getName(), "Invalid name");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].registrationNumber").toString()), coporateCutomerBasicInfo.getRegistrationNumber(), "Invalid registrationNumber");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].customerTypeCode").toString()), coporateCutomerBasicInfo.getCustomerTypeCode(), "Invalid customerTypeCode");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].businessTypeId").toString()), String.valueOf(coporateCutomerBasicInfo.getBusinessTypeId()), "Invalid businessTypeId");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].uwCategories").toString()), String.valueOf(coporateCutomerBasicInfo.getUwCategories()), "Invalid uwCategories");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].parentCompanyId").toString()), String.valueOf(customerId), "Invalid parentCompanyId");
                try {
                    softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].permanentCode").toString()), null, "Invalid permanentCode");
                    softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].temporaryCode").toString()), null, "Invalid temporaryCode");
                    softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].parentId").toString()), null, "Invalid parentId");
                    softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].customerGroups").toString()), null, "Invalid customerGroups");
                    softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].policyholderPreferredLanguage").toString()), null, "Invalid policyholderPreferredLanguage");
                    softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].customerPortalAccess").toString()), null, "Invalid customerPortalAccess");
                } catch (Exception e) {
                }
                softAssert.assertAll();
            }
        }
        softAssert.assertTrue(isCustomerFound, "Customer not found");

        //valid businessRegNo
        System.out.println("\n" + "--------------------Valid businessRegNo------------------- ");
        String parentBody = "businessRegNo.contains=" + saveBusinessRegNo + "&distinct=true";
        String updateSearchRegURI = APIConstants.SEARCH_CORPORATE_CUSTOMER_CRITERIA + "?" + parentBody + pagination;

        Response resSearchReg = RestAssured.given().spec(repoSpec)
                .when()
                .post(updateSearchRegURI);
        System.out.println("\n" + "Status Code: " + resSearchReg.getStatusCode());
        resSearchReg.getBody().prettyPrint();
        Assert.assertEquals(resSearchReg.statusCode(), 200);

        String responseToStringReg = resSearchReg.asString();
        Boolean isCustomersFound = false;
        int len = JsonPath.read(responseToStringReg, "$.result.length()");

        for (int i = 0; i < len; i++) {
            String corporateCustomerId = JsonPath.read(responseToString, "$.result[" + i + "].corporateCustomerId").toString();
            if (corporateCustomerId.equals(saveCorporateCustomerId)) {
                isCustomerFound = true;
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].customerId").toString()), saveCustomerId, "Invalid customerId");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].leadCode").toString()), saveCustomerLeadCode, "Invalid leadCode");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].name").toString()), coporateCutomerBasicInfo.getName(), "Invalid name");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].registrationNumber").toString()), coporateCutomerBasicInfo.getRegistrationNumber(), "Invalid registrationNumber");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].customerTypeCode").toString()), coporateCutomerBasicInfo.getCustomerTypeCode(), "Invalid customerTypeCode");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].businessTypeId").toString()), String.valueOf(coporateCutomerBasicInfo.getBusinessTypeId()), "Invalid businessTypeId");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].uwCategories").toString()), String.valueOf(coporateCutomerBasicInfo.getUwCategories()), "Invalid uwCategories");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].parentCompanyId").toString()), String.valueOf(customerId), "Invalid parentCompanyId");
                try {
                    softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].permanentCode").toString()), null, "Invalid permanentCode");
                    softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].temporaryCode").toString()), null, "Invalid temporaryCode");
                    softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].parentId").toString()), null, "Invalid parentId");
                    softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].customerGroups").toString()), null, "Invalid customerGroups");
                    softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].policyholderPreferredLanguage").toString()), null, "Invalid policyholderPreferredLanguage");
                    softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].customerPortalAccess").toString()), null, "Invalid customerPortalAccess");
                } catch (Exception e) {
                }
                softAssert.assertAll();
            }
        }
        softAssert.assertTrue(isCustomersFound, "Customer not found");
    }
}


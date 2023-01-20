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

import java.util.Collections;

public class PostCorporateCustomerBasicInfo extends BaseTest {

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
    @Description("IQ-1862 [R3][CRM_API] Verify the POST - create_corporate_customer_basic with valid payload ")
    public void testPostCorporateCustomerWithValidPayLoad()
    {
        CoporateCutomerBasicInfo coporateCutomerBasicInfo  = requestPayload.coporateCutomerBasicInfo(corporateCustomerType, businessTypeId, uwCategoryId,customerId);

        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(coporateCutomerBasicInfo)
                .post(APIConstants.CREATE_CORPORATE_CUSTOMER);
        System.out.println("\n" + "Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-1864 [R3][CRM_API] Verify the POST - create_corporate_customer_basic with empty payload")
    public void testPostCorporateCustomerWithEmptyPayLoad()
    {
        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body("{ }")
                .post(APIConstants.CREATE_CORPORATE_CUSTOMER);
        System.out.println("\n" + "Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-1006 [R3][CRM_API] Verify the POST - create_corporate_customer_basic with invalid input in customerTypeCode")
    public void testPostCorporateCustomerWithInvalidCustomerTypeCode() {
        CoporateCutomerBasicInfo coporateCutomerBasicInfo  = requestPayload.coporateCutomerBasicInfo(corporateCustomerType, businessTypeId, uwCategoryId,customerId);
        coporateCutomerBasicInfo.setCustomerTypeCode("999999");

        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(coporateCutomerBasicInfo)
                .post(APIConstants.CREATE_CORPORATE_CUSTOMER);
        System.out.println("\n" + "Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-1001 [R3][CRM_API] Verify the POST - create_corporate_customer_basic with null input in customerTypeCode")
    public void testPostCorporateCustomerWithNullCustomerTypeCode() {
        CoporateCutomerBasicInfo coporateCutomerBasicInfo  = requestPayload.coporateCutomerBasicInfo(corporateCustomerType, businessTypeId, uwCategoryId,customerId);
        coporateCutomerBasicInfo.setCustomerTypeCode(null);

        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(coporateCutomerBasicInfo)
                .post(APIConstants.CREATE_CORPORATE_CUSTOMER);
        System.out.println("\n" + "Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 400);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = resCreate.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), "400", "Invalid httpCode");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "CM_R_CUSTOMER_TYPE_NOT_FOUND", "Invalid errorCode");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "There is no customer type related data for customer type code -null", "Invalid description");

        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-1008 [R3][CRM_API] Verify the POST - create_corporate_customer_basic with null input in name")
    public void testPostCorporateCustomerWithNullName() {
        CoporateCutomerBasicInfo coporateCutomerBasicInfo  = requestPayload.coporateCutomerBasicInfo(corporateCustomerType, businessTypeId, uwCategoryId,customerId);
        coporateCutomerBasicInfo.setName(null);

        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(coporateCutomerBasicInfo)
                .post(APIConstants.CREATE_CORPORATE_CUSTOMER);
        System.out.println("\n" + "Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 400);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = resCreate.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), "400", "Invalid httpCode");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "FIELD_CANNOT_BE_NULL", "Invalid errorCode");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "name field cannot null", "Invalid description");

        softAssert.assertAll();
    }


    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-1011 [R3][CRM_API] Verify the POST - create_corporate_customer_basic with null input in registrationNumber")
    public void testPostCorporateCustomerWithNullRegistrationNumber() {
        CoporateCutomerBasicInfo coporateCutomerBasicInfo  = requestPayload.coporateCutomerBasicInfo(corporateCustomerType, businessTypeId, uwCategoryId,customerId);
        coporateCutomerBasicInfo.setRegistrationNumber(null);

        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(coporateCutomerBasicInfo)
                .post(APIConstants.CREATE_CORPORATE_CUSTOMER);
        System.out.println("\n" + "Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 400);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = resCreate.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), "400", "Invalid httpCode");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "FIELD_CANNOT_BE_NULL", "Invalid errorCode");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "registrationNumber field cannot null", "Invalid description");

        softAssert.assertAll();
    }


    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-1018 [R3][CRM_API] Verify the POST - create_corporate_customer_basic with invalid input in businessTypeId")
    public void testPostCorporateCustomerWithInvalidBusinessTypeId() {
        CoporateCutomerBasicInfo coporateCutomerBasicInfo  = requestPayload.coporateCutomerBasicInfo(corporateCustomerType, businessTypeId, uwCategoryId,customerId);
        coporateCutomerBasicInfo.setBusinessTypeId(999999);

        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(coporateCutomerBasicInfo)
                .post(APIConstants.CREATE_CORPORATE_CUSTOMER);
        System.out.println("\n" + "Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-1017 [R3][CRM_API] Verify the POST - create_corporate_customer_basic with null input in businessTypeId")
    public void testPostCorporateCustomerWithNullBusinessTypeId() {
        CoporateCutomerBasicInfo coporateCutomerBasicInfo  = requestPayload.coporateCutomerBasicInfo(corporateCustomerType, businessTypeId, uwCategoryId,customerId);
        coporateCutomerBasicInfo.setBusinessTypeId(null);

        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(coporateCutomerBasicInfo)
                .post(APIConstants.CREATE_CORPORATE_CUSTOMER);
        System.out.println("\n" + "Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 400);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = resCreate.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), "400", "Invalid httpCode");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "FIELD_CANNOT_BE_NULL", "Invalid errorCode");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "businessTypeId field cannot null", "Invalid description");

        softAssert.assertAll();
    }


    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-1021 [R3][CRM_API] Verify the POST - create_corporate_customer_basic with invalid input in uwCategories")
    public void testPostCorporateCustomerWithInvalidUwCategories() {
        CoporateCutomerBasicInfo coporateCutomerBasicInfo  = requestPayload.coporateCutomerBasicInfo(corporateCustomerType, businessTypeId, uwCategoryId,customerId);
        coporateCutomerBasicInfo.setUwCategories(Collections.singletonList(999999));

        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(coporateCutomerBasicInfo)
                .post(APIConstants.CREATE_CORPORATE_CUSTOMER);
        System.out.println("\n" + "Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-1020 [R3][CRM_API] Verify the POST - create_corporate_customer_basic with null input in uwCategories")
    public void testPostCorporateCustomerWithNullUwCategories() {
        CoporateCutomerBasicInfo coporateCutomerBasicInfo  = requestPayload.coporateCutomerBasicInfo(corporateCustomerType, businessTypeId, uwCategoryId,customerId);
        coporateCutomerBasicInfo.setUwCategories(Collections.singletonList(null));

        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(coporateCutomerBasicInfo)
                .post(APIConstants.CREATE_CORPORATE_CUSTOMER);
        System.out.println("\n" + "Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 400);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = resCreate.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), "400", "Invalid httpCode");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "CM_R_UW_CATEGORY_NATURE_NOT_FOUND", "Invalid errorCode");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "There is no underwriting category related data for underwriting category id -null", "Invalid description");

        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-1882 [R3][CRM_API] Verify the POST - create_corporate_customer_basic with invalid input in parentCompanyId")
    public void testPostCorporateCustomerWithInvalidParentCompanyId() {
        CoporateCutomerBasicInfo coporateCutomerBasicInfo  = requestPayload.coporateCutomerBasicInfo(corporateCustomerType, businessTypeId, uwCategoryId,customerId);
        coporateCutomerBasicInfo.setParentCompanyId(999999);

        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(coporateCutomerBasicInfo)
                .post(APIConstants.CREATE_CORPORATE_CUSTOMER);
        System.out.println("\n" + "Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 400);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = resCreate.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), "400", "Invalid httpCode");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "CM_R_CUSTOMER_PARENT_COMPANY_ID_NOT_FOUND", "Invalid errorCode");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "There is no customer parent related data for parent company id -999999", "Invalid description");

        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-1881 [R3][CRM_API] Verify the POST - create_corporate_customer_basic with null input in parentCompanyId")
    public void testPostCorporateCustomerWithNullParentCompanyId()
    {
        CoporateCutomerBasicInfo coporateCutomerBasicInfo  = requestPayload.coporateCutomerBasicInfo(corporateCustomerType, businessTypeId, uwCategoryId,customerId);
        coporateCutomerBasicInfo.setParentCompanyId(null);

        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(coporateCutomerBasicInfo)
                .post(APIConstants.CREATE_CORPORATE_CUSTOMER);
        System.out.println("\n" + "Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);
    }

}

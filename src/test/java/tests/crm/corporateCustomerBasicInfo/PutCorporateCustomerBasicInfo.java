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

import java.util.ArrayList;
import java.util.List;

public class PutCorporateCustomerBasicInfo extends BaseTest {

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
    @Description("IQ-942 [R3] [CRM_API] Verify the PUT - update_corporate_customer_basic_info with valid Payload")
    public void testPutCorporateCustomerBasicInfoWithValidPayLoad() {
        CoporateCutomerBasicInfo corporateCustomerBasicInfo = requestPayload.coporateCutomerBasicInfo(corporateCustomerType, businessTypeId, uwCategoryId,customerId);

        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(corporateCustomerBasicInfo)
                .post(APIConstants.CREATE_CORPORATE_CUSTOMER);
        System.out.println("\n" + "Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        String saveCustomerId = JsonPath.read(resCreate.asString(), "$.result.customerId").toString();
        String saveCorporateCustomerId = JsonPath.read(resCreate.asString(), "$.result.corporateCustomerId").toString();
        String saveLeadCode = JsonPath.read(resCreate.asString(), "$.result.leadCode").toString();
        String updatedURI = APIConstants.UPDATE_CORPORATE_CUSTOMER.replace("{corporateCustomerId}", saveCorporateCustomerId);

        CoporateCutomerBasicInfo updateCorporateCustomerBasicInfo = requestPayload.coporateCutomerBasicInfo(corporateCustomerType, businessTypeId, uwCategoryId, customerId);
        updateCorporateCustomerBasicInfo.setCustomerId(Integer.parseInt(saveCustomerId));
        updateCorporateCustomerBasicInfo.setCorporateCustomerId(Integer.parseInt(saveCorporateCustomerId));
        updateCorporateCustomerBasicInfo.setLeadCode(saveLeadCode);

        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateCorporateCustomerBasicInfo)
                .put(updatedURI);
        System.out.println("\n" + "Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = resUpdate.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.customerId").toString()), saveCustomerId, "Invalid customerId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.corporateCustomerId").toString()), saveCorporateCustomerId, "Invalid corporateCustomerId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.leadCode").toString()), saveLeadCode, "Invalid leadCode");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-943 [R3] [CRM_API] Verify the PUT - update_corporate_customer_basic_info with empty Payload")
    public void testPutCorporateCustomerBasicInfoWithEmptyPayLoad() {
        CoporateCutomerBasicInfo corporateCustomerBasicInfo = requestPayload.coporateCutomerBasicInfo(corporateCustomerType, businessTypeId, uwCategoryId, customerId);
        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(corporateCustomerBasicInfo)
                .post(APIConstants.CREATE_CORPORATE_CUSTOMER);
        System.out.println("\n" + "Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        String saveCorporateCustomerId = JsonPath.read(resCreate.asString(), "$.result.corporateCustomerId").toString();
        String updatedURI = APIConstants.UPDATE_CORPORATE_CUSTOMER.replace("{corporateCustomerId}", saveCorporateCustomerId);

        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body("{ }")
                .put(updatedURI);
        System.out.println("\n" + "Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 400);

        String statusCode = String.valueOf(resUpdate.getStatusCode());
        SoftAssert softAssert = new SoftAssert();
        String responseToString = resUpdate.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), statusCode, "Invalid status code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "FIELD_CANNOT_BE_NULL", "Invalid error");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "name field cannot null", "Invalid description");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-946 [R3] [CRM_API] Verify the PUT - update_corporate_customer_basic_info with null Lead Code")
    public void testPutCorporateCustomerBasicInfoWithNullLeadCode() {
        CoporateCutomerBasicInfo corporateCustomerBasicInfo = requestPayload.coporateCutomerBasicInfo(corporateCustomerType, businessTypeId, uwCategoryId, customerId);
        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(corporateCustomerBasicInfo)
                .post(APIConstants.CREATE_CORPORATE_CUSTOMER);
        System.out.println("\n" + "Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        String saveCorporateCustomerId = JsonPath.read(resCreate.asString(), "$.result.corporateCustomerId").toString();
        String saveCustomerId = JsonPath.read(resCreate.asString(), "$.result.customerId").toString();
        String updatedURI = APIConstants.UPDATE_CORPORATE_CUSTOMER.replace("{corporateCustomerId}", saveCorporateCustomerId);

        CoporateCutomerBasicInfo updateCorporateCustomerBasicInfo = requestPayload.coporateCutomerBasicInfo(corporateCustomerType, businessTypeId, uwCategoryId, customerId);
        updateCorporateCustomerBasicInfo.setCustomerId(Integer.parseInt(saveCustomerId));
        updateCorporateCustomerBasicInfo.setCorporateCustomerId(Integer.parseInt(saveCorporateCustomerId));
        updateCorporateCustomerBasicInfo.setLeadCode(null);

        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateCorporateCustomerBasicInfo)
                .put(updatedURI);
        System.out.println("\n" + "Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-949 [R3] [CRM_API] Verify the PUT - update_corporate_customer_basic_info with null Customer Type Code")
    public void testPutCorporateCustomerBasicInfoWithNullCustomerTypeCode() {
        CoporateCutomerBasicInfo corporateCustomerBasicInfo = requestPayload.coporateCutomerBasicInfo(corporateCustomerType, businessTypeId, uwCategoryId, customerId);
        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(corporateCustomerBasicInfo)
                .post(APIConstants.CREATE_CORPORATE_CUSTOMER);
        System.out.println("\n" + "Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        String saveCorporateCustomerId = JsonPath.read(resCreate.asString(), "$.result.corporateCustomerId").toString();
        String saveCustomerId = JsonPath.read(resCreate.asString(), "$.result.customerId").toString();
        String saveLeadCode = JsonPath.read(resCreate.asString(), "$.result.leadCode").toString();
        String updatedURI = APIConstants.UPDATE_CORPORATE_CUSTOMER.replace("{corporateCustomerId}", saveCorporateCustomerId);

        CoporateCutomerBasicInfo updateCorporateCustomerBasicInfo = requestPayload.coporateCutomerBasicInfo(corporateCustomerType, businessTypeId, uwCategoryId, customerId);
        updateCorporateCustomerBasicInfo.setCustomerId(Integer.parseInt(saveCustomerId));
        updateCorporateCustomerBasicInfo.setCorporateCustomerId(Integer.parseInt(saveCorporateCustomerId));
        updateCorporateCustomerBasicInfo.setLeadCode(saveLeadCode);
        updateCorporateCustomerBasicInfo.setCustomerTypeCode(null);

        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateCorporateCustomerBasicInfo)
                .put(updatedURI);
        System.out.println("\n" + "Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 400);

        String statusCode = String.valueOf(resUpdate.getStatusCode());
        SoftAssert softAssert = new SoftAssert();
        String responseToString = resUpdate.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), statusCode, "Invalid status code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "CM_R_CUSTOMER_TYPE_NOT_FOUND", "Invalid error");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "There is no customer type related data for customer type code -null", "Invalid description");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-954 [R3] [CRM_API] Verify the PUT - update_corporate_customer_basic_info with invalid Customer Type Code")
    public void testPutCorporateCustomerBasicInfoWithInvalidCustomerTypeCode() {
        CoporateCutomerBasicInfo corporateCustomerBasicInfo = requestPayload.coporateCutomerBasicInfo(corporateCustomerType, businessTypeId, uwCategoryId, customerId);
        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(corporateCustomerBasicInfo)
                .post(APIConstants.CREATE_CORPORATE_CUSTOMER);
        System.out.println("\n" + "Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        String saveCorporateCustomerId = JsonPath.read(resCreate.asString(), "$.result.corporateCustomerId").toString();
        String saveCustomerId = JsonPath.read(resCreate.asString(), "$.result.customerId").toString();
        String saveLeadCode = JsonPath.read(resCreate.asString(), "$.result.leadCode").toString();
        String updatedURI = APIConstants.UPDATE_CORPORATE_CUSTOMER.replace("{corporateCustomerId}", saveCorporateCustomerId);

        CoporateCutomerBasicInfo updateCorporateCustomerBasicInfo = requestPayload.coporateCutomerBasicInfo(corporateCustomerType, businessTypeId, uwCategoryId, customerId);
        updateCorporateCustomerBasicInfo.setCustomerId(Integer.parseInt(saveCustomerId));
        updateCorporateCustomerBasicInfo.setCorporateCustomerId(Integer.parseInt(saveCorporateCustomerId));
        updateCorporateCustomerBasicInfo.setLeadCode(saveLeadCode);
        updateCorporateCustomerBasicInfo.setCustomerTypeCode("PQ");

        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateCorporateCustomerBasicInfo)
                .put(updatedURI);
        System.out.println("\n" + "Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-958 [R3] [CRM_API] Verify the PUT - update_corporate_customer_basic_info with null Name")
    public void testPutCorporateCustomerBasicInfoWithNullName() {
        CoporateCutomerBasicInfo corporateCustomerBasicInfo = requestPayload.coporateCutomerBasicInfo(corporateCustomerType, businessTypeId, uwCategoryId, customerId);
        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(corporateCustomerBasicInfo)
                .post(APIConstants.CREATE_CORPORATE_CUSTOMER);
        System.out.println("\n" + "Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        String saveCustomerId = JsonPath.read(resCreate.asString(), "$.result.customerId").toString();
        String saveCorporateCustomerId = JsonPath.read(resCreate.asString(), "$.result.corporateCustomerId").toString();
        String saveLeadCode = JsonPath.read(resCreate.asString(), "$.result.leadCode").toString();
        String updatedURI = APIConstants.UPDATE_CORPORATE_CUSTOMER.replace("{corporateCustomerId}", saveCorporateCustomerId);

        CoporateCutomerBasicInfo updateCorporateCustomerBasicInfo = requestPayload.coporateCutomerBasicInfo(corporateCustomerType, businessTypeId, uwCategoryId, customerId);
        updateCorporateCustomerBasicInfo.setCustomerId(Integer.parseInt(saveCustomerId));
        updateCorporateCustomerBasicInfo.setCorporateCustomerId(Integer.parseInt(saveCorporateCustomerId));
        updateCorporateCustomerBasicInfo.setLeadCode(saveLeadCode);
        updateCorporateCustomerBasicInfo.setName(null);

        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateCorporateCustomerBasicInfo)
                .put(updatedURI);
        System.out.println("\n" + "Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 400);

        String statusCode = String.valueOf(resUpdate.getStatusCode());
        SoftAssert softAssert = new SoftAssert();
        String responseToString = resUpdate.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), statusCode, "Invalid status code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "FIELD_CANNOT_BE_NULL", "Invalid error");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "name field cannot null", "Invalid description");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-968 [R3] [CRM_API] Verify the PUT - update_corporate_customer_basic_info with null Registration Number")
    public void testPutCorporateCustomerBasicInfoWithNullRegistrationNumber() {
        CoporateCutomerBasicInfo corporateCustomerBasicInfo = requestPayload.coporateCutomerBasicInfo(corporateCustomerType, businessTypeId, uwCategoryId, customerId);
        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(corporateCustomerBasicInfo)
                .post(APIConstants.CREATE_CORPORATE_CUSTOMER);
        System.out.println("\n" + "Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        String saveCustomerId = JsonPath.read(resCreate.asString(), "$.result.customerId").toString();
        String saveCorporateCustomerId = JsonPath.read(resCreate.asString(), "$.result.corporateCustomerId").toString();
        String saveLeadCode = JsonPath.read(resCreate.asString(), "$.result.leadCode").toString();
        String updatedURI = APIConstants.UPDATE_CORPORATE_CUSTOMER.replace("{corporateCustomerId}", saveCorporateCustomerId);

        CoporateCutomerBasicInfo updateCorporateCustomerBasicInfo = requestPayload.coporateCutomerBasicInfo(corporateCustomerType, businessTypeId, uwCategoryId, customerId);
        updateCorporateCustomerBasicInfo.setCustomerId(Integer.parseInt(saveCustomerId));
        updateCorporateCustomerBasicInfo.setCorporateCustomerId(Integer.parseInt(saveCorporateCustomerId));
        updateCorporateCustomerBasicInfo.setLeadCode(saveLeadCode);
        updateCorporateCustomerBasicInfo.setRegistrationNumber(null);

        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateCorporateCustomerBasicInfo)
                .put(updatedURI);
        System.out.println("\n" + "Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 400);

        String statusCode = String.valueOf(resUpdate.getStatusCode());
        SoftAssert softAssert = new SoftAssert();
        String responseToString = resUpdate.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), statusCode, "Invalid status code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "FIELD_CANNOT_BE_NULL", "Invalid error");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "registrationNumber field cannot null", "Invalid description");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-969 [R3] [CRM_API] Verify the PUT - update_corporate_customer_basic_info with null Parent Company ID")
    public void testPutCorporateCustomerBasicInfoWithNullParentCompanyId() {
        CoporateCutomerBasicInfo corporateCustomerBasicInfo = requestPayload.coporateCutomerBasicInfo(corporateCustomerType, businessTypeId, uwCategoryId, customerId);
        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(corporateCustomerBasicInfo)
                .post(APIConstants.CREATE_CORPORATE_CUSTOMER);
        System.out.println("\n" + "Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        String saveCustomerId = JsonPath.read(resCreate.asString(), "$.result.customerId").toString();
        String saveCorporateCustomerId = JsonPath.read(resCreate.asString(), "$.result.corporateCustomerId").toString();
        String saveLeadCode = JsonPath.read(resCreate.asString(), "$.result.leadCode").toString();
        String updatedURI = APIConstants.UPDATE_CORPORATE_CUSTOMER.replace("{corporateCustomerId}", saveCorporateCustomerId);

        CoporateCutomerBasicInfo updateCorporateCustomerBasicInfo = requestPayload.coporateCutomerBasicInfo(corporateCustomerType, businessTypeId, uwCategoryId, null);
        updateCorporateCustomerBasicInfo.setCustomerId(Integer.parseInt(saveCustomerId));
        updateCorporateCustomerBasicInfo.setCorporateCustomerId(Integer.parseInt(saveCorporateCustomerId));
        updateCorporateCustomerBasicInfo.setLeadCode(saveLeadCode);
        updateCorporateCustomerBasicInfo.setParentCompanyId(null);

        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateCorporateCustomerBasicInfo)
                .put(updatedURI);
        System.out.println("\n" + "Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = resUpdate.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.customerId").toString()), saveCustomerId, "Invalid customerId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.corporateCustomerId").toString()), saveCorporateCustomerId, "Invalid corporateCustomerId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.leadCode").toString()), saveLeadCode, "Invalid leadCode");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-970 [R3] [CRM_API] Verify the PUT - update_corporate_customer_basic_info with invalid Parent Company ID")
    public void testPutCorporateCustomerBasicInfoWithInvalidParentCompanyId() {
        CoporateCutomerBasicInfo corporateCustomerBasicInfo = requestPayload.coporateCutomerBasicInfo(corporateCustomerType, businessTypeId, uwCategoryId, customerId);
        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(corporateCustomerBasicInfo)
                .post(APIConstants.CREATE_CORPORATE_CUSTOMER);
        System.out.println("\n" + "Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        String saveCustomerId = JsonPath.read(resCreate.asString(), "$.result.customerId").toString();
        String saveCorporateCustomerId = JsonPath.read(resCreate.asString(), "$.result.corporateCustomerId").toString();
        String saveLeadCode = JsonPath.read(resCreate.asString(), "$.result.leadCode").toString();
        String updatedURI = APIConstants.UPDATE_CORPORATE_CUSTOMER.replace("{corporateCustomerId}", saveCorporateCustomerId);

        Integer invalidId = 999999;
        CoporateCutomerBasicInfo updateCorporateCustomerBasicInfo = requestPayload.coporateCutomerBasicInfo(corporateCustomerType, businessTypeId, uwCategoryId, invalidId);
        updateCorporateCustomerBasicInfo.setCustomerId(Integer.parseInt(saveCustomerId));
        updateCorporateCustomerBasicInfo.setCorporateCustomerId(Integer.parseInt(saveCorporateCustomerId));
        updateCorporateCustomerBasicInfo.setLeadCode(saveLeadCode);
        updateCorporateCustomerBasicInfo.setParentCompanyId(invalidId);

        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateCorporateCustomerBasicInfo)
                .put(updatedURI);
        System.out.println("\n" + "Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 400);

        String statusCode = String.valueOf(resUpdate.getStatusCode());
        SoftAssert softAssert = new SoftAssert();
        String responseToString = resUpdate.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), statusCode, "Invalid status code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "CM_R_CUSTOMER_PARENT_COMPANY_ID_NOT_FOUND", "Invalid error");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "There is no customer parent related data for parent company id -" + invalidId, "Invalid description");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-971 [R3] [CRM_API] Verify the PUT - update_corporate_customer_basic_info with null Business Type ID")
    public void testPutCorporateCustomerBasicInfoWithNullBusinessTypeId() {
        CoporateCutomerBasicInfo corporateCustomerBasicInfo = requestPayload.coporateCutomerBasicInfo(corporateCustomerType, businessTypeId, uwCategoryId, customerId);
        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(corporateCustomerBasicInfo)
                .post(APIConstants.CREATE_CORPORATE_CUSTOMER);
        System.out.println("\n" + "Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        String saveCustomerId = JsonPath.read(resCreate.asString(), "$.result.customerId").toString();
        String saveCorporateCustomerId = JsonPath.read(resCreate.asString(), "$.result.corporateCustomerId").toString();
        String saveLeadCode = JsonPath.read(resCreate.asString(), "$.result.leadCode").toString();
        String updatedURI = APIConstants.UPDATE_CORPORATE_CUSTOMER.replace("{corporateCustomerId}", saveCorporateCustomerId);

        CoporateCutomerBasicInfo updateCorporateCustomerBasicInfo = requestPayload.coporateCutomerBasicInfo(corporateCustomerType, businessTypeId, uwCategoryId, customerId);
        updateCorporateCustomerBasicInfo.setCustomerId(Integer.parseInt(saveCustomerId));
        updateCorporateCustomerBasicInfo.setCorporateCustomerId(Integer.parseInt(saveCorporateCustomerId));
        updateCorporateCustomerBasicInfo.setLeadCode(saveLeadCode);
        updateCorporateCustomerBasicInfo.setBusinessTypeId(null);

        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateCorporateCustomerBasicInfo)
                .put(updatedURI);
        System.out.println("\n" + "Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 400);

        String statusCode = String.valueOf(resUpdate.getStatusCode());
        SoftAssert softAssert = new SoftAssert();
        String responseToString = resUpdate.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), statusCode, "Invalid status code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "FIELD_CANNOT_BE_NULL", "Invalid error");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "businessTypeId field cannot null", "Invalid description");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-972 [R3] [CRM_API] Verify the PUT - update_corporate_customer_basic_info with invalid /non existing Business Type ID")
    public void testPutCorporateCustomerBasicInfoWithInvalidBusinessTypeId() {
        CoporateCutomerBasicInfo corporateCustomerBasicInfo = requestPayload.coporateCutomerBasicInfo(corporateCustomerType, businessTypeId, uwCategoryId, customerId);
        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(corporateCustomerBasicInfo)
                .post(APIConstants.CREATE_CORPORATE_CUSTOMER);
        System.out.println("\n" + "Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        String saveCustomerId = JsonPath.read(resCreate.asString(), "$.result.customerId").toString();
        String saveCorporateCustomerId = JsonPath.read(resCreate.asString(), "$.result.corporateCustomerId").toString();
        String saveLeadCode = JsonPath.read(resCreate.asString(), "$.result.leadCode").toString();
        String updatedURI = APIConstants.UPDATE_CORPORATE_CUSTOMER.replace("{corporateCustomerId}", saveCorporateCustomerId);

        Integer invalidId = 9999999;
        CoporateCutomerBasicInfo updateCorporateCustomerBasicInfo = requestPayload.coporateCutomerBasicInfo(corporateCustomerType, businessTypeId, uwCategoryId, customerId);
        updateCorporateCustomerBasicInfo.setCustomerId(Integer.parseInt(saveCustomerId));
        updateCorporateCustomerBasicInfo.setCorporateCustomerId(Integer.parseInt(saveCorporateCustomerId));
        updateCorporateCustomerBasicInfo.setLeadCode(saveLeadCode);
        updateCorporateCustomerBasicInfo.setBusinessTypeId(invalidId);

        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateCorporateCustomerBasicInfo)
                .put(updatedURI);
        System.out.println("\n" + "Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 400);

        String statusCode = String.valueOf(resUpdate.getStatusCode());
        SoftAssert softAssert = new SoftAssert();
        String responseToString = resUpdate.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), statusCode, "Invalid status code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "CM_R_CUSTOMER_BUSINESS_TYPE_ID_NOT_FOUND", "Invalid error");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "There is no customer business type related data for business type id -" + invalidId , "Invalid description");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-973 [R3] [CRM_API] Verify the PUT - update_corporate_customer_basic_info with null UW Categories")
    public void testPutCorporateCustomerBasicInfoWithNullUwCategories() {
        CoporateCutomerBasicInfo corporateCustomerBasicInfo = requestPayload.coporateCutomerBasicInfo(corporateCustomerType, businessTypeId, uwCategoryId, customerId);
        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(corporateCustomerBasicInfo)
                .post(APIConstants.CREATE_CORPORATE_CUSTOMER);
        System.out.println("\n" + "Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        String saveCorporateCustomerId = JsonPath.read(resCreate.asString(), "$.result.corporateCustomerId").toString();
        String saveCustomerId = JsonPath.read(resCreate.asString(), "$.result.customerId").toString();
        String saveLeadCode = JsonPath.read(resCreate.asString(), "$.result.leadCode").toString();
        String updatedURI = APIConstants.UPDATE_CORPORATE_CUSTOMER.replace("{corporateCustomerId}", saveCorporateCustomerId);

        List<Integer> emptyUwCategories = new ArrayList<Integer>();

        CoporateCutomerBasicInfo updateCorporateCustomerBasicInfo = requestPayload.coporateCutomerBasicInfo(corporateCustomerType, businessTypeId, uwCategoryId, customerId);
        updateCorporateCustomerBasicInfo.setCustomerId(Integer.parseInt(saveCustomerId));
        updateCorporateCustomerBasicInfo.setCorporateCustomerId(Integer.parseInt(saveCorporateCustomerId));
        updateCorporateCustomerBasicInfo.setLeadCode(saveLeadCode);
        updateCorporateCustomerBasicInfo.setUwCategories(emptyUwCategories);

        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateCorporateCustomerBasicInfo)
                .put(updatedURI);
        System.out.println("\n" + "Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = resUpdate.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.customerId").toString()), saveCustomerId, "Invalid customerId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.corporateCustomerId").toString()), saveCorporateCustomerId, "Invalid corporateCustomerId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.leadCode").toString()), saveLeadCode, "Invalid leadCode");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-974 [R3] [CRM_API] Verify the PUT - update_corporate_customer_basic_info with valid two UW Categories")
    public void testPutCorporateCustomerBasicInfoWithTwoUwCategories() {
        CoporateCutomerBasicInfo corporateCustomerBasicInfo = requestPayload.coporateCutomerBasicInfo(corporateCustomerType, businessTypeId, uwCategoryId, customerId);
        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(corporateCustomerBasicInfo)
                .post(APIConstants.CREATE_CORPORATE_CUSTOMER);
        System.out.println("\n" + "Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        String saveCustomerId = JsonPath.read(resCreate.asString(), "$.result.customerId").toString();
        String saveCorporateCustomerId = JsonPath.read(resCreate.asString(), "$.result.corporateCustomerId").toString();
        String saveLeadCode = JsonPath.read(resCreate.asString(), "$.result.leadCode").toString();
        String updatedURI = APIConstants.UPDATE_CORPORATE_CUSTOMER.replace("{corporateCustomerId}", saveCorporateCustomerId);

        Response responseUG = RestAssured.given().spec(repoSpec).when().get(APIConstants.GET_UW_CATEGORIES);
        Integer newUwCategoryId = Integer.parseInt(JsonPath.read(responseUG.asString(), "$.result[1].id").toString());

        List<Integer> newUwCategories = new ArrayList<Integer>();
        newUwCategories.add(uwCategoryId);
        newUwCategories.add(newUwCategoryId);

        CoporateCutomerBasicInfo updateCorporateCustomerBasicInfo = requestPayload.coporateCutomerBasicInfo(corporateCustomerType, businessTypeId, uwCategoryId, customerId);
        updateCorporateCustomerBasicInfo.setCustomerId(Integer.parseInt(saveCustomerId));
        updateCorporateCustomerBasicInfo.setCorporateCustomerId(Integer.parseInt(saveCorporateCustomerId));
        updateCorporateCustomerBasicInfo.setLeadCode(saveLeadCode);
        updateCorporateCustomerBasicInfo.setUwCategories(newUwCategories);

        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateCorporateCustomerBasicInfo)
                .put(updatedURI);
        System.out.println("\n" + "Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = resUpdate.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.customerId").toString()), saveCustomerId, "Invalid customerId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.corporateCustomerId").toString()), saveCorporateCustomerId, "Invalid corporateCustomerId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.leadCode").toString()), saveLeadCode, "Invalid leadCode");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-975 [R3] [CRM_API] Verify the PUT - update_corporate_customer_basic_info with invalid/ non existing UW Categories")
    public void testPutCorporateCustomerBasicInfoWithInvalidUwCategory() {
        CoporateCutomerBasicInfo corporateCustomerBasicInfo = requestPayload.coporateCutomerBasicInfo(corporateCustomerType, businessTypeId, uwCategoryId, customerId);
        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(corporateCustomerBasicInfo)
                .post(APIConstants.CREATE_CORPORATE_CUSTOMER);
        System.out.println("\n" + "Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        String saveCustomerId = JsonPath.read(resCreate.asString(), "$.result.customerId").toString();
        String saveCorporateCustomerId = JsonPath.read(resCreate.asString(), "$.result.corporateCustomerId").toString();
        String saveLeadCode = JsonPath.read(resCreate.asString(), "$.result.leadCode").toString();
        String updatedURI = APIConstants.UPDATE_CORPORATE_CUSTOMER.replace("{corporateCustomerId}", saveCorporateCustomerId);

        Integer invalidId = 9999999;
        CoporateCutomerBasicInfo updateCorporateCustomerBasicInfo = requestPayload.coporateCutomerBasicInfo(corporateCustomerType, businessTypeId, invalidId, customerId);
        updateCorporateCustomerBasicInfo.setCustomerId(Integer.parseInt(saveCustomerId));
        updateCorporateCustomerBasicInfo.setCorporateCustomerId(Integer.parseInt(saveCorporateCustomerId));
        updateCorporateCustomerBasicInfo.setLeadCode(saveLeadCode);

        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateCorporateCustomerBasicInfo)
                .put(updatedURI);
        System.out.println("\n" + "Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 400);

        String statusCode = String.valueOf(resUpdate.getStatusCode());
        SoftAssert softAssert = new SoftAssert();
        String responseToString = resUpdate.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), statusCode, "Invalid status code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "CM_R_UW_CATEGORY_NATURE_NOT_FOUND", "Invalid error");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "There is no underwriting category related data for underwriting category id -" + invalidId, "Invalid description");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-976 [R3] [CRM_API] Verify the PUT - update_corporate_customer_basic_info with valid and non existing UW Categories")
    public void testPutCorporateCustomerBasicInfoWithValidAndInvalidUwCategory() {
        CoporateCutomerBasicInfo corporateCustomerBasicInfo = requestPayload.coporateCutomerBasicInfo(corporateCustomerType, businessTypeId, uwCategoryId, customerId);
        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(corporateCustomerBasicInfo)
                .post(APIConstants.CREATE_CORPORATE_CUSTOMER);
        System.out.println("\n" + "Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        String saveCustomerId = JsonPath.read(resCreate.asString(), "$.result.customerId").toString();
        String saveCorporateCustomerId = JsonPath.read(resCreate.asString(), "$.result.corporateCustomerId").toString();
        String saveLeadCode = JsonPath.read(resCreate.asString(), "$.result.leadCode").toString();
        String updatedURI = APIConstants.UPDATE_CORPORATE_CUSTOMER.replace("{corporateCustomerId}", saveCorporateCustomerId);

        Integer invalidId = 9999999;
        List<Integer> newUwCategories = new ArrayList<Integer>();
        newUwCategories.add(uwCategoryId);
        newUwCategories.add(invalidId);

        CoporateCutomerBasicInfo updateCorporateCustomerBasicInfo = requestPayload.coporateCutomerBasicInfo(corporateCustomerType, businessTypeId, uwCategoryId, customerId);
        updateCorporateCustomerBasicInfo.setCustomerId(Integer.parseInt(saveCustomerId));
        updateCorporateCustomerBasicInfo.setCorporateCustomerId(Integer.parseInt(saveCorporateCustomerId));
        updateCorporateCustomerBasicInfo.setLeadCode(saveLeadCode);
        updateCorporateCustomerBasicInfo.setUwCategories(newUwCategories);

        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateCorporateCustomerBasicInfo)
                .put(updatedURI);
        System.out.println("\n" + "Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 400);

        String statusCode = String.valueOf(resUpdate.getStatusCode());
        SoftAssert softAssert = new SoftAssert();
        String responseToString = resUpdate.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), statusCode, "Invalid status code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "CM_R_UW_CATEGORY_NATURE_NOT_FOUND", "Invalid error");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "There is no underwriting category related data for underwriting category id -" + invalidId, "Invalid description");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-977 [R3] [CRM_API] Verify the PUT - update_corporate_customer_basic_info with null deletingUwCategories")
    public void testPutCorporateCustomerBasicInfoWithNullDeletingUwCategories() {
        CoporateCutomerBasicInfo corporateCustomerBasicInfo = requestPayload.coporateCutomerBasicInfo(corporateCustomerType, businessTypeId, uwCategoryId, customerId);
        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(corporateCustomerBasicInfo)
                .post(APIConstants.CREATE_CORPORATE_CUSTOMER);
        System.out.println("\n" + "Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        String saveCustomerId = JsonPath.read(resCreate.asString(), "$.result.customerId").toString();
        String saveCorporateCustomerId = JsonPath.read(resCreate.asString(), "$.result.corporateCustomerId").toString();
        String saveLeadCode = JsonPath.read(resCreate.asString(), "$.result.leadCode").toString();
        String updatedURI = APIConstants.UPDATE_CORPORATE_CUSTOMER.replace("{corporateCustomerId}", saveCorporateCustomerId);

        CoporateCutomerBasicInfo updateCorporateCustomerBasicInfo = requestPayload.coporateCutomerBasicInfo(corporateCustomerType, businessTypeId, uwCategoryId, customerId);
        updateCorporateCustomerBasicInfo.setCustomerId(Integer.parseInt(saveCustomerId));
        updateCorporateCustomerBasicInfo.setCorporateCustomerId(Integer.parseInt(saveCorporateCustomerId));
        updateCorporateCustomerBasicInfo.setLeadCode(saveLeadCode);

        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateCorporateCustomerBasicInfo)
                .put(updatedURI);
        System.out.println("\n" + "Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = resUpdate.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.customerId").toString()), saveCustomerId, "Invalid customerId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.corporateCustomerId").toString()), saveCorporateCustomerId, "Invalid corporateCustomerId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.leadCode").toString()), saveLeadCode, "Invalid leadCode");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-978 [R3] [CRM_API] Verify the PUT - update_corporate_customer_basic_info with not assigned deletingUwCategories")
    public void testPutCorporateCustomerBasicInfoWithInvalidDeletingUwCategories() {
        CoporateCutomerBasicInfo corporateCustomerBasicInfo = requestPayload.coporateCutomerBasicInfo(corporateCustomerType, businessTypeId, uwCategoryId, customerId);
        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(corporateCustomerBasicInfo)
                .post(APIConstants.CREATE_CORPORATE_CUSTOMER);
        System.out.println("\n" + "Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        String saveCustomerId = JsonPath.read(resCreate.asString(), "$.result.customerId").toString();
        String saveCorporateCustomerId = JsonPath.read(resCreate.asString(), "$.result.corporateCustomerId").toString();
        String saveLeadCode = JsonPath.read(resCreate.asString(), "$.result.leadCode").toString();
        String updatedURI = APIConstants.UPDATE_CORPORATE_CUSTOMER.replace("{corporateCustomerId}", saveCorporateCustomerId);

        Integer invalidId = 999999;
        List<Integer> deletingUwCategories = new ArrayList<Integer>();
        deletingUwCategories.add(invalidId);

        CoporateCutomerBasicInfo updateCorporateCustomerBasicInfo = requestPayload.coporateCutomerBasicInfo(corporateCustomerType, businessTypeId, uwCategoryId, customerId);
        updateCorporateCustomerBasicInfo.setCustomerId(Integer.parseInt(saveCustomerId));
        updateCorporateCustomerBasicInfo.setCorporateCustomerId(Integer.parseInt(saveCorporateCustomerId));
        updateCorporateCustomerBasicInfo.setLeadCode(saveLeadCode);
        updateCorporateCustomerBasicInfo.setDeletingUwCategories(deletingUwCategories);

        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateCorporateCustomerBasicInfo)
                .put(updatedURI);
        System.out.println("\n" + "Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 400);

        String statusCode = String.valueOf(resUpdate.getStatusCode());
        SoftAssert softAssert = new SoftAssert();
        String responseToString = resUpdate.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), statusCode, "Invalid status code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "CM_M_CUSTOMER_UW_CATEGORY_NOT_FOUND", "Invalid error");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "There is no customer underwriting category related data for underwriting category id -" + invalidId, "Invalid description");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-979 [R3] [CRM_API] Verify the PUT - update_corporate_customer_basic_info with assigned and not assigned deletingUwCategories")
    public void testPutCorporateCustomerBasicInfoWithValidAndInvalidDeletingUwCategories() {
        CoporateCutomerBasicInfo corporateCustomerBasicInfo = requestPayload.coporateCutomerBasicInfo(corporateCustomerType, businessTypeId, uwCategoryId, customerId);
        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(corporateCustomerBasicInfo)
                .post(APIConstants.CREATE_CORPORATE_CUSTOMER);
        System.out.println("\n" + "Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        String saveCustomerId = JsonPath.read(resCreate.asString(), "$.result.customerId").toString();
        String saveCorporateCustomerId = JsonPath.read(resCreate.asString(), "$.result.corporateCustomerId").toString();
        String saveLeadCode = JsonPath.read(resCreate.asString(), "$.result.leadCode").toString();
        String updatedURI = APIConstants.UPDATE_CORPORATE_CUSTOMER.replace("{corporateCustomerId}", saveCorporateCustomerId);

        Integer invalidId = 999999;
        List<Integer> deletingUwCategories = new ArrayList<Integer>();
        deletingUwCategories.add(uwCategoryId);
        deletingUwCategories.add(invalidId);

        CoporateCutomerBasicInfo updateCorporateCustomerBasicInfo = requestPayload.coporateCutomerBasicInfo(corporateCustomerType, businessTypeId, uwCategoryId, customerId);
        updateCorporateCustomerBasicInfo.setCustomerId(Integer.parseInt(saveCustomerId));
        updateCorporateCustomerBasicInfo.setCorporateCustomerId(Integer.parseInt(saveCorporateCustomerId));
        updateCorporateCustomerBasicInfo.setLeadCode(saveLeadCode);
        updateCorporateCustomerBasicInfo.setDeletingUwCategories(deletingUwCategories);

        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateCorporateCustomerBasicInfo)
                .put(updatedURI);
        System.out.println("\n" + "Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 400);

        String statusCode = String.valueOf(resUpdate.getStatusCode());
        SoftAssert softAssert = new SoftAssert();
        String responseToString = resUpdate.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), statusCode, "Invalid status code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "CM_M_CUSTOMER_UW_CATEGORY_NOT_FOUND", "Invalid error");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "There is no customer underwriting category related data for underwriting category id -" + invalidId, "Invalid description");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-1863 [R3] [CRM_API] Verify the PUT - update_corporate_customer_basic_info with valid UW Categories")
    public void testPutCorporateCustomerBasicInfoWithValidUwCategory() {
        CoporateCutomerBasicInfo corporateCustomerBasicInfo = requestPayload.coporateCutomerBasicInfo(corporateCustomerType, businessTypeId, uwCategoryId, customerId);
        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(corporateCustomerBasicInfo)
                .post(APIConstants.CREATE_CORPORATE_CUSTOMER);
        System.out.println("\n" + "Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        String saveCustomerId = JsonPath.read(resCreate.asString(), "$.result.customerId").toString();
        String saveCorporateCustomerId = JsonPath.read(resCreate.asString(), "$.result.corporateCustomerId").toString();
        String saveLeadCode = JsonPath.read(resCreate.asString(), "$.result.leadCode").toString();
        String updatedURI = APIConstants.UPDATE_CORPORATE_CUSTOMER.replace("{corporateCustomerId}", saveCorporateCustomerId);

        Response responseUG = RestAssured.given().spec(repoSpec).when().get(APIConstants.GET_UW_CATEGORIES);
        Integer newUwCategoryId = Integer.parseInt(JsonPath.read(responseUG.asString(), "$.result[1].id").toString());
        List<Integer> newUwCategories = new ArrayList<Integer>();
        newUwCategories.add(newUwCategoryId);

        CoporateCutomerBasicInfo updateCorporateCustomerBasicInfo = requestPayload.coporateCutomerBasicInfo(corporateCustomerType, businessTypeId, uwCategoryId, customerId);
        updateCorporateCustomerBasicInfo.setCustomerId(Integer.parseInt(saveCustomerId));
        updateCorporateCustomerBasicInfo.setCorporateCustomerId(Integer.parseInt(saveCorporateCustomerId));
        updateCorporateCustomerBasicInfo.setLeadCode(saveLeadCode);
        updateCorporateCustomerBasicInfo.setUwCategories(newUwCategories);

        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateCorporateCustomerBasicInfo)
                .put(updatedURI);
        System.out.println("\n" + "Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = resUpdate.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.customerId").toString()), saveCustomerId, "Invalid customerId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.corporateCustomerId").toString()), saveCorporateCustomerId, "Invalid corporateCustomerId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.leadCode").toString()), saveLeadCode, "Invalid leadCode");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-1865 [R3] [CRM_API] Verify the PUT - update_corporate_customer_basic_info with valid deletingUwCategories")
    public void testPutCorporateCustomerBasicInfoWithValidDeletingUwCategories() {
        CoporateCutomerBasicInfo corporateCustomerBasicInfo = requestPayload.coporateCutomerBasicInfo(corporateCustomerType, businessTypeId, uwCategoryId, customerId);
        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(corporateCustomerBasicInfo)
                .post(APIConstants.CREATE_CORPORATE_CUSTOMER);
        System.out.println("\n" + "Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        String saveCustomerId = JsonPath.read(resCreate.asString(), "$.result.customerId").toString();
        String saveCorporateCustomerId = JsonPath.read(resCreate.asString(), "$.result.corporateCustomerId").toString();
        String saveLeadCode = JsonPath.read(resCreate.asString(), "$.result.leadCode").toString();
        String updatedURI = APIConstants.UPDATE_CORPORATE_CUSTOMER.replace("{corporateCustomerId}", saveCorporateCustomerId);

        List<Integer> deletingUwCategories = new ArrayList<Integer>();
        deletingUwCategories.add(uwCategoryId);

        CoporateCutomerBasicInfo updateCorporateCustomerBasicInfo = requestPayload.coporateCutomerBasicInfo(corporateCustomerType, businessTypeId, uwCategoryId, customerId);
        updateCorporateCustomerBasicInfo.setCustomerId(Integer.parseInt(saveCustomerId));
        updateCorporateCustomerBasicInfo.setCorporateCustomerId(Integer.parseInt(saveCorporateCustomerId));
        updateCorporateCustomerBasicInfo.setLeadCode(saveLeadCode);
        updateCorporateCustomerBasicInfo.setDeletingUwCategories(deletingUwCategories);

        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateCorporateCustomerBasicInfo)
                .put(updatedURI);
        System.out.println("\n" + "Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = resUpdate.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.customerId").toString()), saveCustomerId, "Invalid customerId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.corporateCustomerId").toString()), saveCorporateCustomerId, "Invalid corporateCustomerId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.leadCode").toString()), saveLeadCode, "Invalid leadCode");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-1866 [R3] [CRM_API] Verify the PUT - update_corporate_customer_basic_info with valid two deletingUwCategories")
    public void testPutCorporateCustomerBasicInfoWithTwoDeletingUwCategories() {
        CoporateCutomerBasicInfo corporateCustomerBasicInfo = requestPayload.coporateCutomerBasicInfo(corporateCustomerType, businessTypeId, uwCategoryId, customerId);
        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(corporateCustomerBasicInfo)
                .post(APIConstants.CREATE_CORPORATE_CUSTOMER);
        System.out.println("\n" + "Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        String saveCustomerId = JsonPath.read(resCreate.asString(), "$.result.customerId").toString();
        String saveCorporateCustomerId = JsonPath.read(resCreate.asString(), "$.result.corporateCustomerId").toString();
        String saveLeadCode = JsonPath.read(resCreate.asString(), "$.result.leadCode").toString();
        String updatedURI = APIConstants.UPDATE_CORPORATE_CUSTOMER.replace("{corporateCustomerId}", saveCorporateCustomerId);

        Response responseUG = RestAssured.given().spec(repoSpec).when().get(APIConstants.GET_UW_CATEGORIES);
        Integer newUwCategoryId = Integer.parseInt(JsonPath.read(responseUG.asString(), "$.result[1].id").toString());

        List<Integer> newUwCategories = new ArrayList<Integer>();
        newUwCategories.add(uwCategoryId);
        newUwCategories.add(newUwCategoryId);

        List<Integer> deletingUwCategories = new ArrayList<Integer>();
        deletingUwCategories.add(uwCategoryId);
        deletingUwCategories.add(newUwCategoryId);

        CoporateCutomerBasicInfo updateCorporateCustomerBasicInfo = requestPayload.coporateCutomerBasicInfo(corporateCustomerType, businessTypeId, uwCategoryId, customerId);
        updateCorporateCustomerBasicInfo.setCustomerId(Integer.parseInt(saveCustomerId));
        updateCorporateCustomerBasicInfo.setCorporateCustomerId(Integer.parseInt(saveCorporateCustomerId));
        updateCorporateCustomerBasicInfo.setLeadCode(saveLeadCode);
        updateCorporateCustomerBasicInfo.setUwCategories(newUwCategories);
        updateCorporateCustomerBasicInfo.setDeletingUwCategories(deletingUwCategories);

        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateCorporateCustomerBasicInfo)
                .put(updatedURI);
        System.out.println("\n" + "Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = resUpdate.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.customerId").toString()), saveCustomerId, "Invalid customerId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.corporateCustomerId").toString()), saveCorporateCustomerId, "Invalid corporateCustomerId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.leadCode").toString()), saveLeadCode, "Invalid leadCode");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-1939 [R4] [CRM_API] Verify the PUT - update_corporate_customer_basic_info with valid corporateCustomerId")
    public void testPutCorporateCustomerBasicInfoWithValidCorporateCustomerId() {
        CoporateCutomerBasicInfo corporateCustomerBasicInfo = requestPayload.coporateCutomerBasicInfo(corporateCustomerType, businessTypeId, uwCategoryId,customerId);

        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(corporateCustomerBasicInfo)
                .post(APIConstants.CREATE_CORPORATE_CUSTOMER);
        System.out.println("\n" + "Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        String saveCustomerId = JsonPath.read(resCreate.asString(), "$.result.customerId").toString();
        String saveCorporateCustomerId = JsonPath.read(resCreate.asString(), "$.result.corporateCustomerId").toString();
        String saveLeadCode = JsonPath.read(resCreate.asString(), "$.result.leadCode").toString();
        String updatedURI = APIConstants.UPDATE_CORPORATE_CUSTOMER.replace("{corporateCustomerId}", saveCorporateCustomerId);

        CoporateCutomerBasicInfo updateCorporateCustomerBasicInfo = requestPayload.coporateCutomerBasicInfo(corporateCustomerType, businessTypeId, uwCategoryId, customerId);
        updateCorporateCustomerBasicInfo.setCustomerId(Integer.parseInt(saveCustomerId));
        updateCorporateCustomerBasicInfo.setCorporateCustomerId(Integer.parseInt(saveCorporateCustomerId));
        updateCorporateCustomerBasicInfo.setLeadCode(saveLeadCode);

        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateCorporateCustomerBasicInfo)
                .put(updatedURI);
        System.out.println("\n" + "Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = resUpdate.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.customerId").toString()), saveCustomerId, "Invalid customerId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.corporateCustomerId").toString()), saveCorporateCustomerId, "Invalid corporateCustomerId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.leadCode").toString()), saveLeadCode, "Invalid leadCode");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-1940 [R4] [CRM_API] Verify the PUT - update_corporate_customer_basic_info with empty corporateCustomerId")
    public void testPutCorporateCustomerBasicInfoWithEmptyCorporateCustomerId() {
        CoporateCutomerBasicInfo corporateCustomerBasicInfo = requestPayload.coporateCutomerBasicInfo(corporateCustomerType, businessTypeId, uwCategoryId,customerId);

        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(corporateCustomerBasicInfo)
                .post(APIConstants.CREATE_CORPORATE_CUSTOMER);
        System.out.println("\n" + "Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        String saveCustomerId = JsonPath.read(resCreate.asString(), "$.result.customerId").toString();
        String saveCorporateCustomerId = JsonPath.read(resCreate.asString(), "$.result.corporateCustomerId").toString();
        String saveLeadCode = JsonPath.read(resCreate.asString(), "$.result.leadCode").toString();
        String updatedURI = APIConstants.UPDATE_CORPORATE_CUSTOMER.replace("{corporateCustomerId}", saveCorporateCustomerId);

        CoporateCutomerBasicInfo updateCorporateCustomerBasicInfo = requestPayload.coporateCutomerBasicInfo(corporateCustomerType, businessTypeId, uwCategoryId, customerId);
        updateCorporateCustomerBasicInfo.setCustomerId(Integer.parseInt(saveCustomerId));
        updateCorporateCustomerBasicInfo.setCorporateCustomerId(null);
        updateCorporateCustomerBasicInfo.setLeadCode(saveLeadCode);

        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateCorporateCustomerBasicInfo)
                .put(updatedURI);
        System.out.println("\n" + "Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 404);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = resUpdate.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.message").toString()), "Invalid id", "Invalid message");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.error").toString()), "ID_NULL", "Invalid error");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-1941 [R4] [CRM_API] Verify the PUT - update_corporate_customer_basic_info with invalid corporateCustomerId")
    public void testPutCorporateCustomerBasicInfoWithInvalidCorporateCustomerId() {
        CoporateCutomerBasicInfo corporateCustomerBasicInfo = requestPayload.coporateCutomerBasicInfo(corporateCustomerType, businessTypeId, uwCategoryId,customerId);

        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(corporateCustomerBasicInfo)
                .post(APIConstants.CREATE_CORPORATE_CUSTOMER);
        System.out.println("\n" + "Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        String saveCustomerId = JsonPath.read(resCreate.asString(), "$.result.customerId").toString();
        String saveCorporateCustomerId = JsonPath.read(resCreate.asString(), "$.result.corporateCustomerId").toString();
        String saveLeadCode = JsonPath.read(resCreate.asString(), "$.result.leadCode").toString();
        String updatedURI = APIConstants.UPDATE_CORPORATE_CUSTOMER.replace("{corporateCustomerId}", saveCorporateCustomerId);

        Integer invalidId = 999999999;
        CoporateCutomerBasicInfo updateCorporateCustomerBasicInfo = requestPayload.coporateCutomerBasicInfo(corporateCustomerType, businessTypeId, uwCategoryId, customerId);
        updateCorporateCustomerBasicInfo.setCustomerId(Integer.parseInt(saveCustomerId));
        updateCorporateCustomerBasicInfo.setCorporateCustomerId(invalidId);
        updateCorporateCustomerBasicInfo.setLeadCode(saveLeadCode);

        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateCorporateCustomerBasicInfo)
                .put(updatedURI);
        System.out.println("\n" + "Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 404);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = resUpdate.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.message").toString()), "Invalid id", "Invalid message");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.error").toString()), "ID_MISMATCH", "Invalid error");
        softAssert.assertAll();
    }
}

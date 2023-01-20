package tests.crm.customerAdditionalInfo;

import com.informatics.endpoints.APIConstants;
import com.informatics.pojos.crm.CustomerAdditionalInfo;
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

public class PutCustomerAdditionalInfo extends BaseTest {

    // ParentCompanyId = CustomerId
    public String customerId;

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
        customerId = JsonPath.read(response.asString(), "$.result.customerId").toString();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-771 [R2][CRM_API] [Put] Validate the process of updating a additional_info_data_crud with valid payload")
    public void testPutAdditionalInfoWithValidPayLoad() {
        String updateCreateURI = APIConstants.CREATE_CUSTOMER_ADDITIONAL_INFO.replace("{customerId}", customerId);
        CustomerAdditionalInfo customerAdditionalInfo = requestPayload.customerAdditionalInfo();

        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(customerAdditionalInfo)
                .post(updateCreateURI);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        String saveInfoId = JsonPath.read(resCreate.asString(), "$.result.customerAdditionalInfoId").toString();
        String updatePutURI = APIConstants.UPDATE_CUSTOMER_ADDITIONAL_INFO.replace("{customerId}", customerId);

        CustomerAdditionalInfo updateCustomerAdditionalInfo = requestPayload.customerAdditionalInfo();
        updateCustomerAdditionalInfo.setCustomerAdditionalInfoId(Integer.parseInt(saveInfoId));
        updateCustomerAdditionalInfo.setAdditionalInfoValue("B-");

        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateCustomerAdditionalInfo)
                .put(updatePutURI);
        System.out.println("Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = resUpdate.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.customerAdditionalInfoId").toString()), saveInfoId, "Invalid customerAdditionalInfoId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.additionalInfoValue").toString()), updateCustomerAdditionalInfo.getAdditionalInfoValue(), "Invalid additionalInfoValue");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.isActive").toString()), String.valueOf(updateCustomerAdditionalInfo.getIsActive()), "Invalid isActive");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.cmRAdditionalInfoId").toString()), String.valueOf(updateCustomerAdditionalInfo.getCmRAdditionalInfoId()), "Invalid cmRAdditionalInfoId");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-774 [R2][CRM_API] Verify the PUT - Additional_info_data_crud with null ID")
    public void testPutAdditionalInfoWithNullId() {
        String updateCreateURI = APIConstants.CREATE_CUSTOMER_ADDITIONAL_INFO.replace("{customerId}", customerId);
        CustomerAdditionalInfo customerAdditionalInfo = requestPayload.customerAdditionalInfo();

        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(customerAdditionalInfo)
                .post(updateCreateURI);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        String saveInfoId = JsonPath.read(resCreate.asString(), "$.result.customerAdditionalInfoId").toString();
        String updatePutURI = APIConstants.UPDATE_CUSTOMER_ADDITIONAL_INFO.replace("{customerId}", "");

        CustomerAdditionalInfo updateCustomerAdditionalInfo = requestPayload.customerAdditionalInfo();
        updateCustomerAdditionalInfo.setCustomerAdditionalInfoId(Integer.parseInt(saveInfoId));

        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateCustomerAdditionalInfo)
                .put(updatePutURI);
        System.out.println("Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-775 [R2][CRM_API] Verify the PUT- Additional_info_data_crud with Invalid data type ID")
    public void testPutAdditionalInfoWithInvalidId() {
        String updateCreateURI = APIConstants.CREATE_CUSTOMER_ADDITIONAL_INFO.replace("{customerId}", customerId);
        CustomerAdditionalInfo customerAdditionalInfo = requestPayload.customerAdditionalInfo();

        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(customerAdditionalInfo)
                .post(updateCreateURI);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        String saveInfoId = JsonPath.read(resCreate.asString(), "$.result.customerAdditionalInfoId").toString();

        String invalidId = "9999999";
        String updatePutURI = APIConstants.UPDATE_CUSTOMER_ADDITIONAL_INFO.replace("{customerId}", invalidId);

        CustomerAdditionalInfo updateCustomerAdditionalInfo = requestPayload.customerAdditionalInfo();
        updateCustomerAdditionalInfo.setCustomerAdditionalInfoId(Integer.parseInt(saveInfoId));
        updateCustomerAdditionalInfo.setAdditionalInfoValue("B-");

        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateCustomerAdditionalInfo)
                .put(updatePutURI);
        System.out.println("Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 400);

        String statusCode = String.valueOf(resUpdate.getStatusCode());
        SoftAssert softAssert = new SoftAssert();
        String responseToString = resUpdate.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), statusCode, "Invalid status code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "Cm_M_Customer_Not_Found", "Invalid error");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "There is No customer  related data found -" + invalidId, "Invalid description");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-776 [R2][CRM_API] Verify the PUT Additional_info_data_crud with empty payload")
    public void testPutAdditionalInfoWithEmptyPayLoad() {
        String updateCreateURI = APIConstants.CREATE_CUSTOMER_ADDITIONAL_INFO.replace("{customerId}", customerId);
        CustomerAdditionalInfo customerAdditionalInfo = requestPayload.customerAdditionalInfo();

        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(customerAdditionalInfo)
                .post(updateCreateURI);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        String updatePutURI = APIConstants.UPDATE_CUSTOMER_ADDITIONAL_INFO.replace("{customerId}", customerId);

        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body("{ }")
                .put(updatePutURI);
        System.out.println("Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 404);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = resUpdate.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.message").toString()), "Null Id Found", "Invalid message");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.error").toString()), "ID_NULL", "Invalid error");
        softAssert.assertAll();
    }

}

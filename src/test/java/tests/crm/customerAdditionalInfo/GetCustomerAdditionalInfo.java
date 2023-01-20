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

public class GetCustomerAdditionalInfo extends BaseTest {

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
    @Description("IQ-697 [R2][CRM_API] Verify the GET - Additional_info_data_crud with Valid inputs fields in ID")
    public void testGetAdditionalInfoWithValidCustomerId() {
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
        String updateGetURI = APIConstants.GET_CUSTOMER_ADDITIONAL_INFO_CUS_ID.replace("{id}", customerId);

        Response resGet = RestAssured.given().spec(repoSpec)
                .when()
                .get(updateGetURI);
        System.out.println("Status Code: " + resGet.getStatusCode());
        resGet.getBody().prettyPrint();
        Assert.assertEquals(resGet.statusCode(), 200);

        String responseToString = resGet.asString();
        Boolean isAdditionalInfoFound = false;
        int length = JsonPath.read(responseToString, "$.result.length()");

        SoftAssert softAssert = new SoftAssert();
        for (int i = 0; i < length; i++) {
            String infoId = JsonPath.read(responseToString, "$.result[" + i + "].id").toString();
            if (infoId.equals(saveInfoId)) {
                isAdditionalInfoFound = true;
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].customerId").toString()), String.valueOf(customerId), "Invalid customerId");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].additionalInfoId").toString()), String.valueOf(customerAdditionalInfo.getCmRAdditionalInfoId()), "Invalid additionalInfoId");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].additionalInfoName").toString()), PropertiesUtils.fieldName0, "Invalid additionalInfoName");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].value").toString()), String.valueOf(customerAdditionalInfo.getAdditionalInfoValue()), "Invalid value");
                break;
            }
        }
        softAssert.assertTrue(isAdditionalInfoFound, "Customer Additional Info not found");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-712 [R2][CRM_API] [Get] Validate the process of getting additional_info_data_crud with Invalid ID")
    public void testGetAdditionalInfoWithInvalidCustomerId() {
        String invalidId = "9999999";
        String updateGetURI = APIConstants.GET_CUSTOMER_ADDITIONAL_INFO_CUS_ID.replace("{id}", invalidId);

        Response resGet = RestAssured.given().spec(repoSpec)
                .when()
                .get(updateGetURI);
        System.out.println("Status Code: " + resGet.getStatusCode());
        resGet.getBody().prettyPrint();
        Assert.assertEquals(resGet.statusCode(), 400);

        String statusCode = String.valueOf(resGet.getStatusCode());
        SoftAssert softAssert = new SoftAssert();
        String responseToString = resGet.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), statusCode, "Invalid status code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "CM_M_CUSTOMER_NOT_FOUND", "Invalid error");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "There is no related data for the given customerId - " + invalidId, "Invalid description");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-719 [R2][CRM_API] Verify the GET process of getting Additional_info_data_crud with with empty ID")
    public void testGetAdditionalInfoWithEmptyCustomerId() {
        String updateGetURI = APIConstants.GET_CUSTOMER_ADDITIONAL_INFO_CUS_ID.replace("{id}", "");

        Response resGet = RestAssured.given().spec(repoSpec)
                .when()
                .get(updateGetURI);
        System.out.println("Status Code: " + resGet.getStatusCode());
        resGet.getBody().prettyPrint();
        Assert.assertEquals(resGet.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-793 [R2]*[CRM_API] Verify the GET - Additional_info_data_crud with Valid inputs fields in ID")
    public void testGetAdditionalInfoWithValidId() {
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
        String updateGetURI = APIConstants.GET_CUSTOMER_ADDITIONAL_INFO_ID.replace("{id}", saveInfoId);

        Response resGet = RestAssured.given().spec(repoSpec)
                .when()
                .get(updateGetURI);
        System.out.println("Status Code: " + resGet.getStatusCode());
        resGet.getBody().prettyPrint();
        Assert.assertEquals(resGet.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = resGet.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.id").toString()), saveInfoId, "Invalid id");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.customerId").toString()), String.valueOf(customerId), "Invalid customerId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.additionalInfoId").toString()), String.valueOf(customerAdditionalInfo.getCmRAdditionalInfoId()), "Invalid additionalInfoId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.additionalInfoName").toString()), PropertiesUtils.fieldName0, "Invalid additionalInfoName");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.value").toString()), String.valueOf(customerAdditionalInfo.getAdditionalInfoValue()), "Invalid value");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-792 [R2]*[CRM_API] [Get] Validate the process of getting additional_info_data_crud with Invalid ID")
    public void testGetAdditionalInfoWithInvalidId() {
        String invalidId = "9999999";
        String updateGetURI = APIConstants.GET_CUSTOMER_ADDITIONAL_INFO_ID.replace("{id}", invalidId);

        Response resGet = RestAssured.given().spec(repoSpec)
                .when()
                .get(updateGetURI);
        System.out.println("Status Code: " + resGet.getStatusCode());
        resGet.getBody().prettyPrint();
        Assert.assertEquals(resGet.statusCode(), 400);

        String statusCode = String.valueOf(resGet.getStatusCode());
        SoftAssert softAssert = new SoftAssert();
        String responseToString = resGet.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), statusCode, "Invalid status code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "ADDITIONAL_INFORMATION_NOT_FOUND", "Invalid error");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "There is no additional information related data for the given id - " + invalidId, "Invalid description");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-794 [R2]*[CRM_API] Verify the GET process of getting Additional_info_data_crud with with empty ID")
    public void testGetAdditionalInfoWithEmptyId() {
        String updateGetURI = APIConstants.GET_CUSTOMER_ADDITIONAL_INFO_ID.replace("{id}", "");

        Response resGet = RestAssured.given().spec(repoSpec)
                .when()
                .get(updateGetURI);
        System.out.println("Status Code: " + resGet.getStatusCode());
        resGet.getBody().prettyPrint();
        Assert.assertEquals(resGet.statusCode(), 404);
    }
}

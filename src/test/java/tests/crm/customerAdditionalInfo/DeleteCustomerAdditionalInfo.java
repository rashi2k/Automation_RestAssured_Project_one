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

public class DeleteCustomerAdditionalInfo extends BaseTest {

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
    @Description("IQ-779 [R2][CRM_API] Verify DELETE- Additional_info_data_crud with Valid ID")
    public void testDeleteAdditionalInfoWithValidId() {
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
        String updateDeleteRI = APIConstants.DELETE_CUSTOMER_ADDITIONAL_INFO.replace("{id}", saveInfoId);

        Response resDelete = RestAssured.given().spec(repoSpec)
                .when()
                .delete(updateDeleteRI);
        System.out.println("Status Code: " + resDelete.getStatusCode());
        resDelete.getBody().prettyPrint();
        Assert.assertEquals(resDelete.statusCode(), 200);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-778 [R2][CRM_API] Verify DELETE- Additional_info_data_crud with with Invalid ID")
    public void testDeleteAdditionalInfoWithInvalidId() {
        String invalidId = "9999999";
        String updateDeleteRI = APIConstants.DELETE_CUSTOMER_ADDITIONAL_INFO.replace("{id}", invalidId);

        Response resDelete = RestAssured.given().spec(repoSpec)
                .when()
                .delete(updateDeleteRI);
        System.out.println("Status Code: " + resDelete.getStatusCode());
        resDelete.getBody().prettyPrint();
        Assert.assertEquals(resDelete.statusCode(), 400);

        String statusCode = String.valueOf(resDelete.getStatusCode());
        SoftAssert softAssert = new SoftAssert();
        String responseToString = resDelete.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), statusCode, "Invalid status code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "ADDITIONAL_INFORMATION_NOT_FOUND", "Invalid error");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "There is no additional information related data for the given id - " + invalidId, "Invalid description");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-777 [R2][CRM_API] Verify DELETE- Additional_info_data_crud with Null ID")
    public void testDeleteAdditionalInfoWithNullId() {
        String updateDeleteRI = APIConstants.DELETE_CUSTOMER_ADDITIONAL_INFO.replace("{id}", "");

        Response resDelete = RestAssured.given().spec(repoSpec)
                .when()
                .delete(updateDeleteRI);
        System.out.println("Status Code: " + resDelete.getStatusCode());
        resDelete.getBody().prettyPrint();
        Assert.assertEquals(resDelete.statusCode(), 400);
    }
}

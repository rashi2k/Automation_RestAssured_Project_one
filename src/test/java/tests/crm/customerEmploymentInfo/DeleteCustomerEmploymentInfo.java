package tests.crm.customerEmploymentInfo;

import com.informatics.endpoints.APIConstants;
import com.informatics.pojos.crm.CustomerBasicInfo;
import com.informatics.pojos.crm.EmploymentInfo;
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

public class DeleteCustomerEmploymentInfo extends BaseTest {

    public String customerId;

    @BeforeClass
    public void initiate() {
        getBaseCustomerInfo();
        getBaseOccupationInfo();
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
    @Description("IQ-1487 [R2] [CRM_API] Verify the DELETE - employment_bank_info with valid employmentId (employment)")
    public void testDeleteEmploymentInfoWithValidId() {
        String updateCreateURI = APIConstants.CREATE_CUSTOMER_EMPLOYMENT_INFO.replace("{customerId}", customerId);
        EmploymentInfo createEmploymentInfo = requestPayload.newEmploymentIno(occupationId);

        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(createEmploymentInfo)
                .post(updateCreateURI);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        String employmentId = JsonPath.read(resCreate.asString(), "$.result.customerEmploymentId").toString();

        EmploymentInfo updateEmploymentInfo = requestPayload.newEmploymentIno(occupationId);
        updateEmploymentInfo.setCustomerEmploymentId(Integer.parseInt(employmentId));
        updateEmploymentInfo.setIsActive(false);

        String updatePutURI = APIConstants.UPDATE_CUSTOMER_EMPLOYMENT_INFO.replace("{customerId}", customerId);
        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateEmploymentInfo)
                .put(updatePutURI);
        System.out.println("Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 200);

        String updateDeleteURI = APIConstants.DELETE_CUSTOMER_EMPLOYMENT_INFO.replace("{employmentId}", employmentId);
        Response resDelete = RestAssured.given().spec(repoSpec)
                .when()
                .delete(updateDeleteURI);
        System.out.println("Status Code: " + resDelete.getStatusCode());
        resDelete.getBody().prettyPrint();
        Assert.assertEquals(resDelete.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = resUpdate.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.customerEmploymentId").toString()), String.valueOf(employmentId), "Invalid customerEmploymentId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.employmentName").toString()), updateEmploymentInfo.getEmploymentName(), "Invalid employmentName");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.startDate").toString()), updateEmploymentInfo.getStartDate(), "Invalid startDate");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.endDate").toString()), String.valueOf(updateEmploymentInfo.getEndDate()), "Invalid endDate");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.occupationId").toString()), String.valueOf(updateEmploymentInfo.getOccupationId()), "Invalid occupationId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.isActive").toString()), String.valueOf(updateEmploymentInfo.getIsActive()), "Invalid isActive");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-580 [R2] [CRM_API] Verify the DELETE - employment_bank_info with null ID (employment)")
    public void testDeleteEmploymentInfoWithNullId() {
        String updateDeleteURI = APIConstants.DELETE_CUSTOMER_EMPLOYMENT_INFO.replace("{employmentId}", "");
        Response resDelete = RestAssured.given().spec(repoSpec)
                .when()
                .delete(updateDeleteURI);
        System.out.println("Status Code: " + resDelete.getStatusCode());
        resDelete.getBody().prettyPrint();
        Assert.assertEquals(resDelete.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-587 [R2] [CRM_API] Verify the DELETE - employment_bank_info with invalid ID (employment)")
    public void testDeleteEmploymentInfoWithInvalidId() {
        String invalidId = "999999";
        String updateDeleteURI = APIConstants.DELETE_CUSTOMER_EMPLOYMENT_INFO.replace("{employmentId}", invalidId);
        Response resDelete = RestAssured.given().spec(repoSpec)
                .when()
                .delete(updateDeleteURI);
        System.out.println("Status Code: " + resDelete.getStatusCode());
        resDelete.getBody().prettyPrint();
        Assert.assertEquals(resDelete.statusCode(), 400);

        String statusCode = String.valueOf(resDelete.getStatusCode());
        SoftAssert softAssert = new SoftAssert();
        String responseToString = resDelete.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), statusCode, "Invalid status code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "CM_M_CUSTOMER_EMPLOYMENT_NOT_FOUND", "Invalid error");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "There is no related data for the given employmentId - " + invalidId, "Invalid description");
        softAssert.assertAll();
    }
}

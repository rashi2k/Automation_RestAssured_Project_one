package tests.crm.customerEmploymentInfo;

import com.informatics.endpoints.APIConstants;
import com.informatics.pojos.crm.BankInfo;
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

public class GetCustomerEmploymentInfo extends BaseTest {

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
    @Description("IQ-1480 [R2] [CRM_API] Verify the GET - employment_bank_info with valid Customer ID (employment)")
    public void testGetEmploymentInfoWithValidCustomerId() {
        String updateCreateURI = APIConstants.CREATE_CUSTOMER_EMPLOYMENT_INFO.replace("{customerId}", customerId);
        EmploymentInfo createEmploymentInfo = requestPayload.newEmploymentIno(occupationId);

        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(createEmploymentInfo)
                .post(updateCreateURI);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        String saveEmploymentId = JsonPath.read(resCreate.asString(), "$.result.customerEmploymentId").toString();

        String updateURI = APIConstants.GET_CUSTOMER_EMPLOYMENT_INFO.replace("{customerId}", customerId) + "?page=0&size=999999";
        Response resGet = RestAssured.given().spec(repoSpec)
                .when()
                .get(updateURI);
        System.out.println("Status Code: " + resGet.getStatusCode());
        resGet.getBody().prettyPrint();
        Assert.assertEquals(resGet.statusCode(), 200);

        String responseToString = resGet.asString();
        Boolean isEmpInfoFound = false;
        int length = JsonPath.read(responseToString, "$.result.length()");

        SoftAssert softAssert = new SoftAssert();
        for (int i = 0; i < length; i++) {
            String empId = JsonPath.read(responseToString, "$.result[" + i + "].customerEmploymentId").toString();
            if (empId.equals(saveEmploymentId)) {
                isEmpInfoFound = true;
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].employmentName").toString()), createEmploymentInfo.getEmploymentName(), "Invalid employmentName");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].startDate").toString()), createEmploymentInfo.getStartDate(), "Invalid startDate");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].endDate").toString()), createEmploymentInfo.getEndDate(), "Invalid endDate");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].occupationId").toString()), String.valueOf(createEmploymentInfo.getOccupationId()), "Invalid occupationId");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].isActive").toString()), String.valueOf(createEmploymentInfo.getIsActive()), "Invalid isActive");
                break;
            }
        }
        softAssert.assertTrue(isEmpInfoFound, "Employment Info not found");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-498 [R2] [CRM_API] Verify the GET - employment_bank_info with null Customer ID (employment)")
    public void testGetEmploymentInfoWithNullCustomerId() {
        String updateURI = APIConstants.GET_CUSTOMER_EMPLOYMENT_INFO.replace("{customerId}", "");

        Response resGet = RestAssured.given().spec(repoSpec)
                .when()
                .get(updateURI);
        System.out.println("Status Code: " + resGet.getStatusCode());
        resGet.getBody().prettyPrint();
        Assert.assertEquals(resGet.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-500 [R2] [CRM_API] Verify the GET - employment_bank_info with invalid Customer ID (employment)")
    public void testGetEmploymentInfoWithInvalidCustomerId() {
        String invalidId = "999999";
        String updateURI = APIConstants.GET_CUSTOMER_EMPLOYMENT_INFO.replace("{customerId}", invalidId);

        Response resGet = RestAssured.given().spec(repoSpec)
                .when()
                .get(updateURI);
        System.out.println("Status Code: " + resGet.getStatusCode());
        resGet.getBody().prettyPrint();
        Assert.assertEquals(resGet.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals((JsonPath.read(resGet.asString(), "$.result").toString()), "[]", "Invalid result");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-1482 [R2] [CRM_API] Verify the GET - employment_bank_info with valid customerEmploymentId (employment)")
    public void testGetEmploymentInfoWithValidEmploymentId() {
        String updateCreateURI = APIConstants.CREATE_CUSTOMER_EMPLOYMENT_INFO.replace("{customerId}", customerId);
        EmploymentInfo createEmploymentInfo = requestPayload.newEmploymentIno(occupationId);

        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(createEmploymentInfo)
                .post(updateCreateURI);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        String saveEmploymentId = JsonPath.read(resCreate.asString(), "$.result.customerEmploymentId").toString();

        String updateURI = APIConstants.GET_CUSTOMER_EMPLOYMENT_INFO_EMP_ID.replace("{employmentId}", saveEmploymentId);
        Response resGet = RestAssured.given().spec(repoSpec)
                .when()
                .get(updateURI);
        System.out.println("Status Code: " + resGet.getStatusCode());
        resGet.getBody().prettyPrint();
        Assert.assertEquals(resGet.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = resGet.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.customerEmploymentId").toString()), saveEmploymentId, "Invalid customerEmploymentId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.employmentName").toString()), createEmploymentInfo.getEmploymentName(), "Invalid employmentName");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.startDate").toString()), createEmploymentInfo.getStartDate(), "Invalid startDate");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.endDate").toString()), createEmploymentInfo.getEndDate(), "Invalid endDate");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.occupationId").toString()), String.valueOf(createEmploymentInfo.getOccupationId()), "Invalid occupationId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.isActive").toString()), String.valueOf(createEmploymentInfo.getIsActive()), "Invalid isActive");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-501 [R2] [CRM_API] Verify the GET - employment_bank_info with null ID (employment)")
    public void testGetEmploymentInfoWithNullEmploymentId() {
        String updateURI = APIConstants.GET_CUSTOMER_EMPLOYMENT_INFO_EMP_ID.replace("{employmentId}", "");

        Response resGet = RestAssured.given().spec(repoSpec)
                .when()
                .get(updateURI);
        System.out.println("Status Code: " + resGet.getStatusCode());
        resGet.getBody().prettyPrint();
        Assert.assertEquals(resGet.statusCode(), 404);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-500 [R2] [CRM_API] Verify the GET - employment_bank_info with invalid Customer ID (employment)")
    public void testGetEmploymentInfoWithInvalidEmploymentId() {
        String invalidId = "999999";
        String updateURI = APIConstants.GET_CUSTOMER_EMPLOYMENT_INFO_EMP_ID.replace("{employmentId}", invalidId);

        Response resGet = RestAssured.given().spec(repoSpec)
                .when()
                .get(updateURI);
        System.out.println("Status Code: " + resGet.getStatusCode());
        resGet.getBody().prettyPrint();
        Assert.assertEquals(resGet.statusCode(), 400);

        String statusCode = String.valueOf(resGet.getStatusCode());
        SoftAssert softAssert = new SoftAssert();
        String responseToString = resGet.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), statusCode, "Invalid status code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "CM_M_CUSTOMER_EMPLOYMENT_NOT_FOUND", "Invalid error");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "There is no related data for the given employmentId - " + invalidId, "Invalid description");
        softAssert.assertAll();
    }

}

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

public class PostCustomerEmploymentInfo extends BaseTest {

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
    @Description("IQ-1383 [R2] [CRM_API] Verify the POST - employment_bank_info with valid payload (Employment)")
    public void testPostEmploymentInfoWithValidPayLoad() {
        String updateCreateURI = APIConstants.CREATE_CUSTOMER_EMPLOYMENT_INFO.replace("{customerId}", customerId);
        EmploymentInfo createEmploymentInfo = requestPayload.newEmploymentIno(occupationId);

        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(createEmploymentInfo)
                .post(updateCreateURI);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = resCreate.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.employmentName").toString()), createEmploymentInfo.getEmploymentName(), "Invalid employmentName");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.startDate").toString()), createEmploymentInfo.getStartDate(), "Invalid startDate");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.endDate").toString()), String.valueOf(createEmploymentInfo.getEndDate()), "Invalid endDate");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.occupationId").toString()), String.valueOf(createEmploymentInfo.getOccupationId()), "Invalid occupationId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.isActive").toString()), String.valueOf(createEmploymentInfo.getIsActive()), "Invalid isActive");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-1383 [R2] [CRM_API] Verify the POST - employment_bank_info with valid payload (Employment)")
    public void testPostEmploymentInfoWithEmptyPayLoad() {
        String updateCreateURI = APIConstants.CREATE_CUSTOMER_EMPLOYMENT_INFO.replace("{customerId}", customerId);
        EmploymentInfo createEmploymentInfo = requestPayload.newEmploymentIno(occupationId);
        createEmploymentInfo.setEmploymentName(null);
        createEmploymentInfo.setStartDate(null);
        createEmploymentInfo.setEndDate(null);
        createEmploymentInfo.setOccupationId(null);
        createEmploymentInfo.setIsActive(null);

        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(createEmploymentInfo)
                .post(updateCreateURI);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 500);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-504 [R2] [CRM_API] Verify the POST - employment_bank_info with null Occupation ID")
    public void testPostEmploymentInfoWithNullOccupationId() {
        String updateCreateURI = APIConstants.CREATE_CUSTOMER_EMPLOYMENT_INFO.replace("{customerId}", customerId);
        EmploymentInfo createEmploymentInfo = requestPayload.newEmploymentIno(occupationId);
        createEmploymentInfo.setOccupationId(null);

        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(createEmploymentInfo)
                .post(updateCreateURI);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 500);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-506 [R2] [CRM_API] Verify the POST - employment_bank_info with invalid Occupation ID")
    public void testPostEmploymentInfoWithInvalidOccupationId() {
        String updateCreateURI = APIConstants.CREATE_CUSTOMER_EMPLOYMENT_INFO.replace("{customerId}", customerId);
        EmploymentInfo createEmploymentInfo = requestPayload.newEmploymentIno(occupationId);
        createEmploymentInfo.setOccupationId(999999);

        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(createEmploymentInfo)
                .post(updateCreateURI);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-507 [R2] [CRM_API] Verify the POST - employment_bank_info with null Employment Name")
    public void testPostEmploymentInfoWithNullEmploymentName() {
        String updateCreateURI = APIConstants.CREATE_CUSTOMER_EMPLOYMENT_INFO.replace("{customerId}", customerId);
        EmploymentInfo createEmploymentInfo = requestPayload.newEmploymentIno(occupationId);
        createEmploymentInfo.setEmploymentName(null);

        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(createEmploymentInfo)
                .post(updateCreateURI);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 406);
    }

    /*
    //SKIPPED - Can not insert String or Integer value into IsActive - type: boolean
    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-509 [R2] [CRM_API] Verify the POST - employment_bank_info with invalid Is Active")
    public void testPostEmploymentInfoWithInvalidIsActive() {
        String updateCreateURI = APIConstants.CREATE_CUSTOMER_EMPLOYMENT_INFO.replace("{customerId}", customerId);
        EmploymentInfo createEmploymentInfo = requestPayload.newEmploymentIno(occupationId);
        createEmploymentInfo.setIsActive(Boolean.valueOf("y"));

        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(createEmploymentInfo)
                .post(updateCreateURI);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 400);
    }
     */

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-510 [R2] [CRM_API] Verify the POST - employment_bank_info with null Is Active")
    public void testPostEmploymentInfoWithNullIsActive() {
        String updateCreateURI = APIConstants.CREATE_CUSTOMER_EMPLOYMENT_INFO.replace("{customerId}", customerId);
        EmploymentInfo createEmploymentInfo = requestPayload.newEmploymentIno(occupationId);
        createEmploymentInfo.setIsActive(null);

        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(createEmploymentInfo)
                .post(updateCreateURI);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 406);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-512 [R2] [CRM_API] Verify the POST - employment_bank_info with null Start Date")
    public void testPostEmploymentInfoWithNullStartDate() {
        String updateCreateURI = APIConstants.CREATE_CUSTOMER_EMPLOYMENT_INFO.replace("{customerId}", customerId);
        EmploymentInfo createEmploymentInfo = requestPayload.newEmploymentIno(occupationId);
        createEmploymentInfo.setStartDate(null);

        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(createEmploymentInfo)
                .post(updateCreateURI);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 406);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-514 [R2] [CRM_API] Verify the POST - employment_bank_info with invalid Start Date)")
    public void testPostEmploymentInfoWithInvalidStartDate() {
        System.out.println("\n" + "--------------------Start date as QA_DHA_SD------------------- ");
        String updateCreateURI = APIConstants.CREATE_CUSTOMER_EMPLOYMENT_INFO.replace("{customerId}", customerId);
        EmploymentInfo createEmploymentInfoA = requestPayload.newEmploymentIno(occupationId);
        createEmploymentInfoA.setStartDate("QA_DHA_SD");

        Response resCreateA = RestAssured.given().spec(repoSpec)
                .when()
                .body(createEmploymentInfoA)
                .post(updateCreateURI);
        System.out.println("Status Code: " + resCreateA.getStatusCode());
        resCreateA.getBody().prettyPrint();
        Assert.assertEquals(resCreateA.statusCode(), 400);

        System.out.println("\n" + "--------------------Start date as 2021-13-04------------------- ");
        EmploymentInfo createEmploymentInfoB = requestPayload.newEmploymentIno(occupationId);
        createEmploymentInfoB.setStartDate("2021-13-04");

        Response resCreateB = RestAssured.given().spec(repoSpec)
                .when()
                .body(createEmploymentInfoB)
                .post(updateCreateURI);
        System.out.println("Status Code: " + resCreateB.getStatusCode());
        resCreateB.getBody().prettyPrint();
        Assert.assertEquals(resCreateB.statusCode(), 400);

        System.out.println("\n" + "--------------------Start date as 13-04-2021------------------- ");
        EmploymentInfo createEmploymentInfoC = requestPayload.newEmploymentIno(occupationId);
        createEmploymentInfoC.setStartDate("13-04-2021");

        Response resCreateC = RestAssured.given().spec(repoSpec)
                .when()
                .body(createEmploymentInfoC)
                .post(updateCreateURI);
        System.out.println("Status Code: " + resCreateC.getStatusCode());
        resCreateC.getBody().prettyPrint();
        Assert.assertEquals(resCreateC.statusCode(), 400);

        System.out.println("\n" + "--------------------Start date as 42-49-2021------------------- ");
        EmploymentInfo createEmploymentInfoD = requestPayload.newEmploymentIno(occupationId);
        createEmploymentInfoD.setStartDate("42-49-2021");

        Response resCreateD = RestAssured.given().spec(repoSpec)
                .when()
                .body(createEmploymentInfoD)
                .post(updateCreateURI);
        System.out.println("Status Code: " + resCreateD.getStatusCode());
        resCreateD.getBody().prettyPrint();
        Assert.assertEquals(resCreateD.statusCode(), 400);

        System.out.println("\n" + "--------------------Start date as 04-13-2021------------------- ");
        EmploymentInfo createEmploymentInfoE = requestPayload.newEmploymentIno(occupationId);
        createEmploymentInfoE.setStartDate("04-13-2021");

        Response resCreateE = RestAssured.given().spec(repoSpec)
                .when()
                .body(createEmploymentInfoE)
                .post(updateCreateURI);
        System.out.println("Status Code: " + resCreateE.getStatusCode());
        resCreateE.getBody().prettyPrint();
        Assert.assertEquals(resCreateE.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-515 [R2] [CRM_API] Verify the POST - employment_bank_info with null End Date")
    public void testPostEmploymentInfoWithNullEndDate() {
        String updateCreateURI = APIConstants.CREATE_CUSTOMER_EMPLOYMENT_INFO.replace("{customerId}", customerId);
        EmploymentInfo createEmploymentInfo = requestPayload.newEmploymentIno(occupationId);
        createEmploymentInfo.setEndDate(null);

        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(createEmploymentInfo)
                .post(updateCreateURI);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = resCreate.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.employmentName").toString()), createEmploymentInfo.getEmploymentName(), "Invalid employmentName");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.startDate").toString()), createEmploymentInfo.getStartDate(), "Invalid startDate");
        try {
            softAssert.assertEquals((JsonPath.read(responseToString, "$.result.endDate").toString()), String.valueOf(createEmploymentInfo.getEndDate()), "Invalid endDate");
        }
        catch (Exception e){
        }
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.occupationId").toString()), String.valueOf(createEmploymentInfo.getOccupationId()), "Invalid occupationId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.isActive").toString()), String.valueOf(createEmploymentInfo.getIsActive()), "Invalid isActive");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-853 [R2] [CRM][API] Verify the POST - employment_bank_info with invalid End Date")
    public void testPostEmploymentInfoWithInvalidEndDate() {
        System.out.println("\n" + "--------------------End date as QA_DHA_ED------------------- ");
        String updateCreateURI = APIConstants.CREATE_CUSTOMER_EMPLOYMENT_INFO.replace("{customerId}", customerId);
        EmploymentInfo createEmploymentInfoA = requestPayload.newEmploymentIno(occupationId);
        createEmploymentInfoA.setEndDate("QA_DHA_ED");

        Response resCreateA = RestAssured.given().spec(repoSpec)
                .when()
                .body(createEmploymentInfoA)
                .post(updateCreateURI);
        System.out.println("Status Code: " + resCreateA.getStatusCode());
        resCreateA.getBody().prettyPrint();
        Assert.assertEquals(resCreateA.statusCode(), 400);

        System.out.println("\n" + "--------------------End date as 18-04-2024------------------- ");
        EmploymentInfo createEmploymentInfoB = requestPayload.newEmploymentIno(occupationId);
        createEmploymentInfoB.setEndDate("18-04-2024");

        Response resCreateB = RestAssured.given().spec(repoSpec)
                .when()
                .body(createEmploymentInfoB)
                .post(updateCreateURI);
        System.out.println("Status Code: " + resCreateB.getStatusCode());
        resCreateB.getBody().prettyPrint();
        Assert.assertEquals(resCreateB.statusCode(), 400);

        System.out.println("\n" + "--------------------End date as 32-01-2024------------------- ");
        EmploymentInfo createEmploymentInfoC = requestPayload.newEmploymentIno(occupationId);
        createEmploymentInfoC.setEndDate("32-01-2024");

        Response resCreateC = RestAssured.given().spec(repoSpec)
                .when()
                .body(createEmploymentInfoC)
                .post(updateCreateURI);
        System.out.println("Status Code: " + resCreateC.getStatusCode());
        resCreateC.getBody().prettyPrint();
        Assert.assertEquals(resCreateC.statusCode(), 400);

        System.out.println("\n" + "--------------------End date as 01-40-2024------------------- ");
        EmploymentInfo createEmploymentInfoD = requestPayload.newEmploymentIno(occupationId);
        createEmploymentInfoD.setEndDate("01-40-2024");

        Response resCreateD = RestAssured.given().spec(repoSpec)
                .when()
                .body(createEmploymentInfoD)
                .post(updateCreateURI);
        System.out.println("Status Code: " + resCreateD.getStatusCode());
        resCreateD.getBody().prettyPrint();
        Assert.assertEquals(resCreateD.statusCode(), 400);

        System.out.println("\n" + "--------------------End date as 01-04-2024------------------- ");
        EmploymentInfo createEmploymentInfoE = requestPayload.newEmploymentIno(occupationId);
        createEmploymentInfoE.setEndDate("01-04-2024");

        Response resCreateE = RestAssured.given().spec(repoSpec)
                .when()
                .body(createEmploymentInfoE)
                .post(updateCreateURI);
        System.out.println("Status Code: " + resCreateE.getStatusCode());
        resCreateE.getBody().prettyPrint();
        Assert.assertEquals(resCreateE.statusCode(), 400);

        System.out.println("\n" + "--------------------End date as 38-18-2021------------------- ");
        EmploymentInfo createEmploymentInfoF = requestPayload.newEmploymentIno(occupationId);
        createEmploymentInfoF.setEndDate("38-18-2021");

        Response resCreateF = RestAssured.given().spec(repoSpec)
                .when()
                .body(createEmploymentInfoF)
                .post(updateCreateURI);
        System.out.println("Status Code: " + resCreateF.getStatusCode());
        resCreateF.getBody().prettyPrint();
        Assert.assertEquals(resCreateF.statusCode(), 400);
    }
}

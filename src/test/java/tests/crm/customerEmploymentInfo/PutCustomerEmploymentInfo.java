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

public class PutCustomerEmploymentInfo extends BaseTest {

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
    @Description("IQ-1475 [R2] [CRM_API] Verify the PUT - employment_bank_info with valid payload (Employment))")
    public void testPutEmploymentInfoWithValidPayLoad() {
        String updateCreateURI = APIConstants.CREATE_CUSTOMER_EMPLOYMENT_INFO.replace("{customerId}", customerId);
        EmploymentInfo createEmploymentInfo = requestPayload.newEmploymentIno(occupationId);

        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(createEmploymentInfo)
                .post(updateCreateURI);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        Integer employmentId = Integer.parseInt(JsonPath.read(resCreate.asString(), "$.result.customerEmploymentId").toString());

        EmploymentInfo updateEmploymentInfo = requestPayload.newEmploymentIno(occupationId);
        updateEmploymentInfo.setCustomerEmploymentId(employmentId);

        String updatePutURI = APIConstants.UPDATE_CUSTOMER_EMPLOYMENT_INFO.replace("{customerId}", customerId);
        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateEmploymentInfo)
                .put(updatePutURI);
        System.out.println("Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 200);

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
    @Description("IQ-1476 [R2] [CRM_API] Verify the PUT - employment_bank_info with empty payload (Employment)")
    public void testPutEmploymentInfoWithEmptyPayLoad() {
        String updateCreateURI = APIConstants.CREATE_CUSTOMER_EMPLOYMENT_INFO.replace("{customerId}", customerId);
        EmploymentInfo createEmploymentInfo = requestPayload.newEmploymentIno(occupationId);

        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(createEmploymentInfo)
                .post(updateCreateURI);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        Integer employmentId = Integer.parseInt(JsonPath.read(resCreate.asString(), "$.result.customerEmploymentId").toString());

        EmploymentInfo updateEmploymentInfo = requestPayload.newEmploymentIno(occupationId);
        updateEmploymentInfo.setCustomerEmploymentId(employmentId);
        updateEmploymentInfo.setEmploymentName(null);
        updateEmploymentInfo.setStartDate(null);
        updateEmploymentInfo.setEndDate(null);
        updateEmploymentInfo.setOccupationId(null);
        updateEmploymentInfo.setIsActive(null);

        String updatePutURI = APIConstants.UPDATE_CUSTOMER_EMPLOYMENT_INFO.replace("{customerId}", customerId);
        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateEmploymentInfo)
                .put(updatePutURI);
        System.out.println("Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 500);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-517 [R2] [CRM_API] Verify the PUT - employment_bank_info with null Occupation ID")
    public void testPutEmploymentInfoWithNullOccupationId() {
        String updateCreateURI = APIConstants.CREATE_CUSTOMER_EMPLOYMENT_INFO.replace("{customerId}", customerId);
        EmploymentInfo createEmploymentInfo = requestPayload.newEmploymentIno(occupationId);

        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(createEmploymentInfo)
                .post(updateCreateURI);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        Integer employmentId = Integer.parseInt(JsonPath.read(resCreate.asString(), "$.result.customerEmploymentId").toString());

        EmploymentInfo updateEmploymentInfo = requestPayload.newEmploymentIno(occupationId);
        updateEmploymentInfo.setCustomerEmploymentId(employmentId);
        updateEmploymentInfo.setEmploymentName(createEmploymentInfo.getEmploymentName());
        updateEmploymentInfo.setStartDate(createEmploymentInfo.getStartDate());
        updateEmploymentInfo.setEndDate(createEmploymentInfo.getEndDate());
        updateEmploymentInfo.setOccupationId(null);
        updateEmploymentInfo.setIsActive(createEmploymentInfo.getIsActive());

        String updatePutURI = APIConstants.UPDATE_CUSTOMER_EMPLOYMENT_INFO.replace("{customerId}", customerId);
        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateEmploymentInfo)
                .put(updatePutURI);
        System.out.println("Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 500);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-520 [R2] [CRM_API] Verify the PUT - employment_bank_info with invalid Occupation ID")
    public void testPutEmploymentInfoWithInvalidOccupationId() {
        String updateCreateURI = APIConstants.CREATE_CUSTOMER_EMPLOYMENT_INFO.replace("{customerId}", customerId);
        EmploymentInfo createEmploymentInfo = requestPayload.newEmploymentIno(occupationId);

        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(createEmploymentInfo)
                .post(updateCreateURI);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        Integer employmentId = Integer.parseInt(JsonPath.read(resCreate.asString(), "$.result.customerEmploymentId").toString());

        EmploymentInfo updateEmploymentInfo = requestPayload.newEmploymentIno(occupationId);
        updateEmploymentInfo.setCustomerEmploymentId(employmentId);
        updateEmploymentInfo.setEmploymentName(createEmploymentInfo.getEmploymentName());
        updateEmploymentInfo.setStartDate(createEmploymentInfo.getStartDate());
        updateEmploymentInfo.setEndDate(createEmploymentInfo.getEndDate());
        updateEmploymentInfo.setOccupationId(9999999);
        updateEmploymentInfo.setIsActive(createEmploymentInfo.getIsActive());

        String updatePutURI = APIConstants.UPDATE_CUSTOMER_EMPLOYMENT_INFO.replace("{customerId}", customerId);
        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateEmploymentInfo)
                .put(updatePutURI);
        System.out.println("Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-521 [R2] [CRM_API] Verify the PUT - employment_bank_info with null Employment Name")
    public void testPutEmploymentInfoWithNullEmploymentName() {
        String updateCreateURI = APIConstants.CREATE_CUSTOMER_EMPLOYMENT_INFO.replace("{customerId}", customerId);
        EmploymentInfo createEmploymentInfo = requestPayload.newEmploymentIno(occupationId);

        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(createEmploymentInfo)
                .post(updateCreateURI);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        Integer employmentId = Integer.parseInt(JsonPath.read(resCreate.asString(), "$.result.customerEmploymentId").toString());

        EmploymentInfo updateEmploymentInfo = requestPayload.newEmploymentIno(occupationId);
        updateEmploymentInfo.setCustomerEmploymentId(employmentId);
        updateEmploymentInfo.setCustomerEmploymentId(employmentId);
        updateEmploymentInfo.setEmploymentName(null);
        updateEmploymentInfo.setStartDate(createEmploymentInfo.getStartDate());
        updateEmploymentInfo.setEndDate(createEmploymentInfo.getEndDate());
        updateEmploymentInfo.setOccupationId(createEmploymentInfo.getOccupationId());
        updateEmploymentInfo.setIsActive(createEmploymentInfo.getIsActive());

        String updatePutURI = APIConstants.UPDATE_CUSTOMER_EMPLOYMENT_INFO.replace("{customerId}", customerId);
        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateEmploymentInfo)
                .put(updatePutURI);
        System.out.println("Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-568 [R2] [CRM_API] Verify the PUT - employment_bank_info with null Is Active)")
    public void testPutEmploymentInfoWithNullIsActive() {
        String updateCreateURI = APIConstants.CREATE_CUSTOMER_EMPLOYMENT_INFO.replace("{customerId}", customerId);
        EmploymentInfo createEmploymentInfo = requestPayload.newEmploymentIno(occupationId);

        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(createEmploymentInfo)
                .post(updateCreateURI);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        Integer employmentId = Integer.parseInt(JsonPath.read(resCreate.asString(), "$.result.customerEmploymentId").toString());

        EmploymentInfo updateEmploymentInfo = requestPayload.newEmploymentIno(occupationId);
        updateEmploymentInfo.setCustomerEmploymentId(employmentId);
        updateEmploymentInfo.setEmploymentName(createEmploymentInfo.getEmploymentName());
        updateEmploymentInfo.setStartDate(createEmploymentInfo.getStartDate());
        updateEmploymentInfo.setEndDate(createEmploymentInfo.getEndDate());
        updateEmploymentInfo.setOccupationId(createEmploymentInfo.getOccupationId());
        updateEmploymentInfo.setIsActive(null);

        String updatePutURI = APIConstants.UPDATE_CUSTOMER_EMPLOYMENT_INFO.replace("{customerId}", customerId);
        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateEmploymentInfo)
                .put(updatePutURI);
        System.out.println("Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-570 [R2] [CRM_API] Verify the PUT - employment_bank_info with null Start Date")
    public void testPutEmploymentInfoWithNullStartDate() {
        String updateCreateURI = APIConstants.CREATE_CUSTOMER_EMPLOYMENT_INFO.replace("{customerId}", customerId);
        EmploymentInfo createEmploymentInfo = requestPayload.newEmploymentIno(occupationId);

        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(createEmploymentInfo)
                .post(updateCreateURI);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        Integer employmentId = Integer.parseInt(JsonPath.read(resCreate.asString(), "$.result.customerEmploymentId").toString());

        EmploymentInfo updateEmploymentInfo = requestPayload.newEmploymentIno(occupationId);
        updateEmploymentInfo.setCustomerEmploymentId(employmentId);
        updateEmploymentInfo.setEmploymentName(createEmploymentInfo.getEmploymentName());
        updateEmploymentInfo.setStartDate(null);
        updateEmploymentInfo.setEndDate(createEmploymentInfo.getEndDate());
        updateEmploymentInfo.setOccupationId(createEmploymentInfo.getOccupationId());
        updateEmploymentInfo.setIsActive(createEmploymentInfo.getIsActive());

        String updatePutURI = APIConstants.UPDATE_CUSTOMER_EMPLOYMENT_INFO.replace("{customerId}", customerId);
        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateEmploymentInfo)
                .put(updatePutURI);
        System.out.println("Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-573 [R2] [CRM_API] Verify the PUT - employment_bank_info with invalid Start Date")
    public void testPutEmploymentInfoWithInvalidStartDate() {
        String updateCreateURI = APIConstants.CREATE_CUSTOMER_EMPLOYMENT_INFO.replace("{customerId}", customerId);
        EmploymentInfo createEmploymentInfo = requestPayload.newEmploymentIno(occupationId);

        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(createEmploymentInfo)
                .post(updateCreateURI);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        Integer employmentId = Integer.parseInt(JsonPath.read(resCreate.asString(), "$.result.customerEmploymentId").toString());
        String updatePutURI = APIConstants.UPDATE_CUSTOMER_EMPLOYMENT_INFO.replace("{customerId}", customerId);

        System.out.println("\n" + "--------------------Start date as QA_DHA_SD------------------- ");
        EmploymentInfo updateEmploymentInfoA = requestPayload.newEmploymentIno(occupationId);
        updateEmploymentInfoA.setCustomerEmploymentId(employmentId);
        updateEmploymentInfoA.setEmploymentName(createEmploymentInfo.getEmploymentName());
        updateEmploymentInfoA.setStartDate("QA_DHA_SD");
        updateEmploymentInfoA.setEndDate(createEmploymentInfo.getEndDate());
        updateEmploymentInfoA.setOccupationId(createEmploymentInfo.getOccupationId());
        updateEmploymentInfoA.setIsActive(createEmploymentInfo.getIsActive());

        Response resUpdateA = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateEmploymentInfoA)
                .put(updatePutURI);
        System.out.println("Status Code: " + resUpdateA.getStatusCode());
        resUpdateA.getBody().prettyPrint();
        Assert.assertEquals(resUpdateA.statusCode(), 400);

        System.out.println("\n" + "--------------------Start date as 2021-13-04------------------- ");
        EmploymentInfo updateEmploymentInfoB = requestPayload.newEmploymentIno(occupationId);
        updateEmploymentInfoB.setCustomerEmploymentId(employmentId);
        updateEmploymentInfoB.setEmploymentName(createEmploymentInfo.getEmploymentName());
        updateEmploymentInfoB.setStartDate("2021-13-04");
        updateEmploymentInfoB.setEndDate(createEmploymentInfo.getEndDate());
        updateEmploymentInfoB.setOccupationId(createEmploymentInfo.getOccupationId());
        updateEmploymentInfoB.setIsActive(createEmploymentInfo.getIsActive());

        Response resUpdateB = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateEmploymentInfoB)
                .put(updatePutURI);
        System.out.println("Status Code: " + resUpdateB.getStatusCode());
        resUpdateB.getBody().prettyPrint();
        Assert.assertEquals(resUpdateB.statusCode(), 400);

        System.out.println("\n" + "--------------------Start date as 13-04-2021------------------- ");
        EmploymentInfo updateEmploymentInfoC = requestPayload.newEmploymentIno(occupationId);
        updateEmploymentInfoC.setCustomerEmploymentId(employmentId);
        updateEmploymentInfoC.setEmploymentName(createEmploymentInfo.getEmploymentName());
        updateEmploymentInfoC.setStartDate("13-04-2021");
        updateEmploymentInfoC.setEndDate(createEmploymentInfo.getEndDate());
        updateEmploymentInfoC.setOccupationId(createEmploymentInfo.getOccupationId());
        updateEmploymentInfoC.setIsActive(createEmploymentInfo.getIsActive());

        Response resUpdateC = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateEmploymentInfoC)
                .put(updatePutURI);
        System.out.println("Status Code: " + resUpdateC.getStatusCode());
        resUpdateC.getBody().prettyPrint();
        Assert.assertEquals(resUpdateC.statusCode(), 400);

        System.out.println("\n" + "--------------------Start date as 42-49-2021------------------- ");
        EmploymentInfo updateEmploymentInfoD = requestPayload.newEmploymentIno(occupationId);
        updateEmploymentInfoD.setCustomerEmploymentId(employmentId);
        updateEmploymentInfoD.setEmploymentName(createEmploymentInfo.getEmploymentName());
        updateEmploymentInfoD.setStartDate("42-49-2021");
        updateEmploymentInfoD.setEndDate(createEmploymentInfo.getEndDate());
        updateEmploymentInfoD.setOccupationId(createEmploymentInfo.getOccupationId());
        updateEmploymentInfoD.setIsActive(createEmploymentInfo.getIsActive());

        Response resUpdateD = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateEmploymentInfoD)
                .put(updatePutURI);
        System.out.println("Status Code: " + resUpdateD.getStatusCode());
        resUpdateD.getBody().prettyPrint();
        Assert.assertEquals(resUpdateD.statusCode(), 400);

        System.out.println("\n" + "--------------------Start date as 04-13-2021------------------- ");
        EmploymentInfo updateEmploymentInfoE = requestPayload.newEmploymentIno(occupationId);
        updateEmploymentInfoE.setCustomerEmploymentId(employmentId);
        updateEmploymentInfoE.setEmploymentName(createEmploymentInfo.getEmploymentName());
        updateEmploymentInfoE.setStartDate("04-13-2021");
        updateEmploymentInfoE.setEndDate(createEmploymentInfo.getEndDate());
        updateEmploymentInfoE.setOccupationId(createEmploymentInfo.getOccupationId());
        updateEmploymentInfoE.setIsActive(createEmploymentInfo.getIsActive());

        Response resUpdateE = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateEmploymentInfoE)
                .put(updatePutURI);
        System.out.println("Status Code: " + resUpdateE.getStatusCode());
        resUpdateE.getBody().prettyPrint();
        Assert.assertEquals(resUpdateE.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-574 [R2] [CRM_API] Verify the PUT - employment_bank_info with null End Date")
    public void testPutEmploymentInfoWithNullEndDate() {
        String updateCreateURI = APIConstants.CREATE_CUSTOMER_EMPLOYMENT_INFO.replace("{customerId}", customerId);
        EmploymentInfo createEmploymentInfo = requestPayload.newEmploymentIno(occupationId);

        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(createEmploymentInfo)
                .post(updateCreateURI);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        Integer employmentId = Integer.parseInt(JsonPath.read(resCreate.asString(), "$.result.customerEmploymentId").toString());

        EmploymentInfo updateEmploymentInfo = requestPayload.newEmploymentIno(occupationId);
        updateEmploymentInfo.setCustomerEmploymentId(employmentId);
        updateEmploymentInfo.setEmploymentName(createEmploymentInfo.getEmploymentName());
        updateEmploymentInfo.setStartDate(createEmploymentInfo.getStartDate());
        updateEmploymentInfo.setEndDate(null);
        updateEmploymentInfo.setOccupationId(createEmploymentInfo.getOccupationId());
        updateEmploymentInfo.setIsActive(createEmploymentInfo.getIsActive());

        String updatePutURI = APIConstants.UPDATE_CUSTOMER_EMPLOYMENT_INFO.replace("{customerId}", customerId);
        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateEmploymentInfo)
                .put(updatePutURI);
        System.out.println("Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = resUpdate.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.customerEmploymentId").toString()), String.valueOf(employmentId), "Invalid customerEmploymentId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.employmentName").toString()), updateEmploymentInfo.getEmploymentName(), "Invalid employmentName");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.startDate").toString()), updateEmploymentInfo.getStartDate(), "Invalid startDate");
        try {
            softAssert.assertEquals((JsonPath.read(responseToString, "$.result.endDate").toString()), String.valueOf(updateEmploymentInfo.getEndDate()), "Invalid endDate");
        }
        catch (Exception e){
        }
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.occupationId").toString()), String.valueOf(updateEmploymentInfo.getOccupationId()), "Invalid occupationId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.isActive").toString()), String.valueOf(updateEmploymentInfo.getIsActive()), "Invalid isActive");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-578 [R2] [CRM_API] Verify the PUT - employment_bank_info with invalid End Date")
    public void testPutEmploymentInfoWithInvalidEndDate() {
        String updateCreateURI = APIConstants.CREATE_CUSTOMER_EMPLOYMENT_INFO.replace("{customerId}", customerId);
        EmploymentInfo createEmploymentInfo = requestPayload.newEmploymentIno(occupationId);

        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(createEmploymentInfo)
                .post(updateCreateURI);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        Integer employmentId = Integer.parseInt(JsonPath.read(resCreate.asString(), "$.result.customerEmploymentId").toString());
        String updatePutURI = APIConstants.UPDATE_CUSTOMER_EMPLOYMENT_INFO.replace("{customerId}", customerId);

        System.out.println("\n" + "--------------------End date as QA_DHA_ED------------------- ");
        EmploymentInfo updateEmploymentInfoA = requestPayload.newEmploymentIno(occupationId);
        updateEmploymentInfoA.setCustomerEmploymentId(employmentId);
        updateEmploymentInfoA.setEmploymentName(createEmploymentInfo.getEmploymentName());
        updateEmploymentInfoA.setStartDate(createEmploymentInfo.getStartDate());
        updateEmploymentInfoA.setEndDate("QA_DHA_ED");
        updateEmploymentInfoA.setOccupationId(createEmploymentInfo.getOccupationId());
        updateEmploymentInfoA.setIsActive(createEmploymentInfo.getIsActive());

        Response resUpdateA = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateEmploymentInfoA)
                .put(updatePutURI);
        System.out.println("Status Code: " + resUpdateA.getStatusCode());
        resUpdateA.getBody().prettyPrint();
        Assert.assertEquals(resUpdateA.statusCode(), 400);

        System.out.println("\n" + "--------------------End date as 18-04-2024------------------- ");
        EmploymentInfo updateEmploymentInfoB = requestPayload.newEmploymentIno(occupationId);
        updateEmploymentInfoB.setCustomerEmploymentId(employmentId);
        updateEmploymentInfoB.setEmploymentName(createEmploymentInfo.getEmploymentName());
        updateEmploymentInfoB.setStartDate(createEmploymentInfo.getStartDate());
        updateEmploymentInfoB.setEndDate("18-04-2024");
        updateEmploymentInfoB.setOccupationId(createEmploymentInfo.getOccupationId());
        updateEmploymentInfoB.setIsActive(createEmploymentInfo.getIsActive());

        Response resUpdateB = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateEmploymentInfoB)
                .put(updatePutURI);
        System.out.println("Status Code: " + resUpdateB.getStatusCode());
        resUpdateB.getBody().prettyPrint();
        Assert.assertEquals(resUpdateB.statusCode(), 400);

        System.out.println("\n" + "--------------------Start date as 32-01-2024------------------- ");
        EmploymentInfo updateEmploymentInfoC = requestPayload.newEmploymentIno(occupationId);
        updateEmploymentInfoC.setCustomerEmploymentId(employmentId);
        updateEmploymentInfoC.setEmploymentName(createEmploymentInfo.getEmploymentName());
        updateEmploymentInfoC.setStartDate(createEmploymentInfo.getStartDate());
        updateEmploymentInfoC.setEndDate("32-01-2024");
        updateEmploymentInfoC.setOccupationId(createEmploymentInfo.getOccupationId());
        updateEmploymentInfoC.setIsActive(createEmploymentInfo.getIsActive());

        Response resUpdateC = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateEmploymentInfoC)
                .put(updatePutURI);
        System.out.println("Status Code: " + resUpdateC.getStatusCode());
        resUpdateC.getBody().prettyPrint();
        Assert.assertEquals(resUpdateC.statusCode(), 400);

        System.out.println("\n" + "--------------------End date as 01-40-2024------------------- ");
        EmploymentInfo updateEmploymentInfoD = requestPayload.newEmploymentIno(occupationId);
        updateEmploymentInfoD.setCustomerEmploymentId(employmentId);
        updateEmploymentInfoD.setEmploymentName(createEmploymentInfo.getEmploymentName());
        updateEmploymentInfoD.setStartDate(createEmploymentInfo.getStartDate());
        updateEmploymentInfoD.setEndDate("01-40-2024");
        updateEmploymentInfoD.setOccupationId(createEmploymentInfo.getOccupationId());
        updateEmploymentInfoD.setIsActive(createEmploymentInfo.getIsActive());

        Response resUpdateD = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateEmploymentInfoD)
                .put(updatePutURI);
        System.out.println("Status Code: " + resUpdateD.getStatusCode());
        resUpdateD.getBody().prettyPrint();
        Assert.assertEquals(resUpdateD.statusCode(), 400);

        System.out.println("\n" + "--------------------End date as 04-01-2024------------------- ");
        EmploymentInfo updateEmploymentInfoE = requestPayload.newEmploymentIno(occupationId);
        updateEmploymentInfoE.setCustomerEmploymentId(employmentId);
        updateEmploymentInfoE.setEmploymentName(createEmploymentInfo.getEmploymentName());
        updateEmploymentInfoE.setStartDate(createEmploymentInfo.getStartDate());
        updateEmploymentInfoE.setEndDate("04-01-2024");
        updateEmploymentInfoE.setOccupationId(createEmploymentInfo.getOccupationId());
        updateEmploymentInfoE.setIsActive(createEmploymentInfo.getIsActive());

        Response resUpdateE = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateEmploymentInfoE)
                .put(updatePutURI);
        System.out.println("Status Code: " + resUpdateE.getStatusCode());
        resUpdateE.getBody().prettyPrint();
        Assert.assertEquals(resUpdateE.statusCode(), 400);
    }

    /*
    //Can not insert String or Integer value into IsActive - type: boolean
    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-855 [R2] [CRM][API] Verify the PUT - employment_bank_info with invalid Is Active")
    public void testPutEmploymentInfoWithInvalidIsActive() {
        String updateCreateURI = APIConstants.CREATE_CUSTOMER_EMPLOYMENT_INFO.replace("{customerId}", customerId);
        EmploymentInfo createEmploymentInfo = requestPayload.newEmploymentIno(occupationId);

        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(createEmploymentInfo)
                .post(updateCreateURI);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        Integer employmentId = Integer.parseInt(JsonPath.read(resCreate.asString(), "$.result.customerEmploymentId").toString());

        EmploymentInfo updateEmploymentInfo = requestPayload.newEmploymentIno(occupationId);
        updateEmploymentInfo.setCustomerEmploymentId(employmentId);
        updateEmploymentInfo.setIsActive(Boolean.valueOf("y"));

        String updatePutURI = APIConstants.UPDATE_CUSTOMER_EMPLOYMENT_INFO.replace("{customerId}", customerId);
        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateEmploymentInfo)
                .put(updatePutURI);
        System.out.println("Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 500);
    }
    */

}

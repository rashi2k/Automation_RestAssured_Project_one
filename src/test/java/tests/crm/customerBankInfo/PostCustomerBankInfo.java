package tests.crm.customerBankInfo;

import com.informatics.endpoints.APIConstants;
import com.informatics.pojos.crm.BankInfo;
import com.informatics.pojos.crm.CustomerBasicInfo;
import com.informatics.pojos.crm.TaxRegistration;
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

public class PostCustomerBankInfo extends BaseTest {

    public String customerId, fName, lName;

    @BeforeClass
    public void initiate() {
        getBaseCustomerInfo();
        getBaseBankBranchInfo();
    }

    @BeforeMethod
    public void createParentCustomer() {
        //create test customer to get customerID
        CustomerBasicInfo createTestCustomer = requestPayload.createTestCustomer(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, idNumber);
        Response response = RestAssured.given().spec(repoSpec).when()
                .body(createTestCustomer)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        customerId = JsonPath.read(response.asString(), "$.result.customerId").toString();

        //get customer fName and lName
        Response responseCN = RestAssured.given().spec(repoSpec).when().get(APIConstants.GET_CUS_BASIC_INFO_ID.replace("{id}", customerId));
        fName = JsonPath.read(responseCN.asString(), "$.result.firstName").toString();
        lName = JsonPath.read(responseCN.asString(), "$.result.lastName").toString();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-1385 [R2] [CRM_API] Verify the POST - employment_bank_info with valid payload (Bank info)")
    public void testPostBankInfoWithValidPayLoad() {
        String updateCreateURI = APIConstants.CREATE_CUSTOMER_BANK_INFO.replace("{customerId}", customerId);
        BankInfo createBankInfo = requestPayload.newBankInfo(bankId, branchId, fName, lName);

        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(createBankInfo)
                .post(updateCreateURI);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-1386 [R2] [CRM_API] Verify the POST - employment_bank_info with empty payload (Bank info)")
    public void testPostBankInfoWithEmptyPayLoad() {
        String updateCreateURI = APIConstants.CREATE_CUSTOMER_BANK_INFO.replace("{customerId}", customerId);
        BankInfo createBankInfo = requestPayload.newBankInfo(bankId, branchId, fName, lName);
        createBankInfo.setBankAccountName(null);
        createBankInfo.setBankAccountNo(null);
        createBankInfo.setBankId(null);
        createBankInfo.setBranchId(null);
        createBankInfo.setIsPrimary(null);
        createBankInfo.setIsActive(null);

        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(createBankInfo)
                .post(updateCreateURI);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-665 [R2] [CRM_API] Verify the POST - employment_bank_info with null Bank Account Name")
    public void testPostBankInfoWithNullBankAccountName() {
        String updateCreateURI = APIConstants.CREATE_CUSTOMER_BANK_INFO.replace("{customerId}", customerId);
        BankInfo createBankInfo = requestPayload.newBankInfo(bankId, branchId, fName, lName);
        createBankInfo.setBankAccountName(null);

        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(createBankInfo)
                .post(updateCreateURI);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 406);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-667 [R2] [CRM_API] Verify the POST - employment_bank_info with null Bank ID")
    public void testPostBankInfoWithNullBankId() {
        String updateCreateURI = APIConstants.CREATE_CUSTOMER_BANK_INFO.replace("{customerId}", customerId);
        BankInfo createBankInfo = requestPayload.newBankInfo(bankId, branchId, fName, lName);
        createBankInfo.setBankId(null);

        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(createBankInfo)
                .post(updateCreateURI);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-669 [R2] [CRM_API] Verify the POST - employment_bank_info with invalid Bank ID")
    public void testPostBankInfoWithInvalidBankId() {
        Integer invalidId = 999999;
        String updateCreateURI = APIConstants.CREATE_CUSTOMER_BANK_INFO.replace("{customerId}", customerId);
        BankInfo createBankInfo = requestPayload.newBankInfo(bankId, branchId, fName, lName);
        createBankInfo.setBankId(invalidId);

        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(createBankInfo)
                .post(updateCreateURI);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 400);

        String statusCode = String.valueOf(resCreate.getStatusCode());
        SoftAssert softAssert = new SoftAssert();
        String responseToString = resCreate.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), statusCode, "Invalid status code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "AD_BANK_REFERENCE_NOT_FOUND", "Invalid error");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "There is no bank reference data found - ", "Invalid description");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-670 [R2] [CRM_API] Verify the POST - employment_bank_info with null Branch ID")
    public void testPostBankInfoWithNullBranchId() {
        String updateCreateURI = APIConstants.CREATE_CUSTOMER_BANK_INFO.replace("{customerId}", customerId);
        BankInfo createBankInfo = requestPayload.newBankInfo(bankId, branchId, fName, lName);
        createBankInfo.setBranchId(null);

        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(createBankInfo)
                .post(updateCreateURI);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-672 [R2] [CRM_API] Verify the POST - employment_bank_info with invalid Branch ID")
    public void testPostBankInfoWithInvalidBranchId() {
        Integer invalidId = 999999;
        String updateCreateURI = APIConstants.CREATE_CUSTOMER_BANK_INFO.replace("{customerId}", customerId);
        BankInfo createBankInfo = requestPayload.newBankInfo(bankId, branchId, fName, lName);
        createBankInfo.setBranchId(invalidId);

        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(createBankInfo)
                .post(updateCreateURI);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 400);

        String statusCode = String.valueOf(resCreate.getStatusCode());
        SoftAssert softAssert = new SoftAssert();
        String responseToString = resCreate.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), statusCode, "Invalid status code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "AD_BRANCH_REFERENCE_NOT_FOUND", "Invalid error");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "There is no branch reference data found - ", "Invalid description");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-674 [R2] [CRM_API] Verify the POST - employment_bank_info with null Bank Account Number")
    public void testPostBankInfoWithNullBankAccountNumber() {
        String updateCreateURI = APIConstants.CREATE_CUSTOMER_BANK_INFO.replace("{customerId}", customerId);
        BankInfo createBankInfo = requestPayload.newBankInfo(bankId, branchId, fName, lName);
        createBankInfo.setBankAccountNo(null);

        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(createBankInfo)
                .post(updateCreateURI);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 406);
    }

    /*
    //TC not valid
    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-676 [R2] [CRM_API] Verify the POST - employment_bank_info with invalid Account Number")
    public void testPostBankInfoWithInvalidBankAccountNumber() {
        String invalidAccountNo = "QA_DHA_BANo@@";
        String updateCreateURI = APIConstants.CREATE_CUSTOMER_BANK_INFO.replace("{customerId}", customerId);
        BankInfo createBankInfo = requestPayload.newBankInfo(bankId, branchId, fName, lName);
        createBankInfo.setBankAccountNo(invalidAccountNo);

        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(createBankInfo)
                .post(updateCreateURI);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 400);
    }
     */

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-677 [R2] [CRM_API] Verify the POST - employment_bank_info with null Is Primary")
    public void testPostBankInfoWithNullIsPrimary() {
        String updateCreateURI = APIConstants.CREATE_CUSTOMER_BANK_INFO.replace("{customerId}", customerId);
        BankInfo createBankInfo = requestPayload.newBankInfo(bankId, branchId, fName, lName);
        createBankInfo.setIsPrimary(null);

        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(createBankInfo)
                .post(updateCreateURI);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 406);
    }

    /*
    //Can not insert String or Integer value into IsPrimary - type: boolean
    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-681 [R2] [CRM_API] Verify the POST - employment_bank_info with invalid Is Primary")
    public void testPostBankInfoWithInvalidIsPrimary() {
        String updateCreateURI = APIConstants.CREATE_CUSTOMER_BANK_INFO.replace("{customerId}", customerId);
        BankInfo createBankInfo = requestPayload.newBankInfo(bankId, branchId, fName, lName);
        createBankInfo.setIsPrimary(Boolean.valueOf("yes"));

        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(createBankInfo)
                .post(updateCreateURI);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 400);
    }
     */

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-887 [R2] [CRM_API] Verify the POST - employment_bank_info with null Is Active")
    public void testPostBankInfoWithNullIsActive() {
        String updateCreateURI = APIConstants.CREATE_CUSTOMER_BANK_INFO.replace("{customerId}", customerId);
        BankInfo createBankInfo = requestPayload.newBankInfo(bankId, branchId, fName, lName);
        createBankInfo.setIsActive(null);

        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(createBankInfo)
                .post(updateCreateURI);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 406);
    }

    /*
    //Can not insert String or Integer value into IsActive - type: boolean
    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-890 [R2] [CRM_API] Verify the POST - employment_bank_info with invalid Is Active")
    public void testPostBankInfoWithInvalidIsActive() {
        String updateCreateURI = APIConstants.CREATE_CUSTOMER_BANK_INFO.replace("{customerId}", customerId);
        BankInfo createBankInfo = requestPayload.newBankInfo(bankId, branchId, fName, lName);
        createBankInfo.setIsActive(Boolean.valueOf("yes"));

        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(createBankInfo)
                .post(updateCreateURI);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 400);
    }
     */
}

package tests.crm.customerBankInfo;

import com.informatics.endpoints.APIConstants;
import com.informatics.pojos.crm.BankInfo;
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

public class GetCustomerBankInfo extends BaseTest {

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
    @Description("IQ-1477 [R2] [CRM_API] Verify the GET - employment_bank_info with valid Customer ID (bank-info)")
    public void testGetBankInfoWithValidCustomerId() {
        String updateCreateURI = APIConstants.CREATE_CUSTOMER_BANK_INFO.replace("{customerId}", customerId);
        BankInfo createBankInfo = requestPayload.newBankInfo(bankId, branchId, fName, lName);

        Response resCreate = RestAssured.given().spec(repoSpec)//create customer bank info
                .when()
                .body(createBankInfo)
                .post(updateCreateURI);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        String updateURI = APIConstants.GET_CUSTOMER_BANK_INFO.replace("{customerId}", customerId) + "?page=0&size=999999";
        Response resGet = RestAssured.given().spec(repoSpec)//get cus bank info
                .when()
                .get(updateURI);
        System.out.println("Status Code: " + resGet.getStatusCode());
        resGet.getBody().prettyPrint();
        Assert.assertEquals(resGet.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = resGet.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].bankAccountName").toString()), createBankInfo.getBankAccountName(), "Invalid bankAccountName");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].bankAccountNo").toString()), createBankInfo.getBankAccountNo(), "Invalid bankAccountNo");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].bankId").toString()), String.valueOf(createBankInfo.getBankId()), "Invalid bankId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].branchId").toString()), String.valueOf(createBankInfo.getBranchId()), "Invalid branchId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].isPrimary").toString()), String.valueOf(createBankInfo.getIsPrimary()), "Invalid isPrimary");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].isActive").toString()), String.valueOf(createBankInfo.getIsActive()), "Invalid isActive");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-657 [R2] [CRM_API] Verify the GET - employment_bank_info with null Customer ID (bank-info)")
    public void testGetBankInfoWithNullCustomerId() {
        String updateURI = APIConstants.GET_CUSTOMER_BANK_INFO.replace("{customerId}", "");

        Response resGet = RestAssured.given().spec(repoSpec)
                .when()
                .get(updateURI);
        System.out.println("Status Code: " + resGet.getStatusCode());
        resGet.getBody().prettyPrint();
        Assert.assertEquals(resGet.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-661 [R2] [CRM_API] Verify the GET - employment_bank_info with invalid Customer ID (bank-info)")
    public void testGetBankInfoWithInvalidCustomerId() {
        String invalidId = "999999";
        String updateURI = APIConstants.GET_CUSTOMER_BANK_INFO.replace("{customerId}", invalidId);

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
    @Description("IQ-1479 [R2] [CRM_API] Verify the GET - employment_bank_info with valid CustomerBankId (bank-info)")
    public void testGetBankInfoWithValidBankId() {
        String updateCreateURI = APIConstants.CREATE_CUSTOMER_BANK_INFO.replace("{customerId}", customerId);
        BankInfo createBankInfo = requestPayload.newBankInfo(bankId, branchId, fName, lName);

        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(createBankInfo)
                .post(updateCreateURI);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        String updateGetURI = APIConstants.GET_CUSTOMER_BANK_INFO.replace("{customerId}", customerId) + "?page=0&size=999999";
        Response resGetCudId = RestAssured.given().spec(repoSpec)
                .when()
                .get(updateGetURI);
        System.out.println("Status Code: " + resGetCudId.getStatusCode());
        resGetCudId.getBody().prettyPrint();
        Assert.assertEquals(resGetCudId.statusCode(), 200);

        String cusBankId = JsonPath.read(resGetCudId.asString(), "$.result[0].customerBankId").toString(); //get cus bank id

        String updateURI = APIConstants.GET_CUSTOMER_BANK_INFO_BANK_ID.replace("{bankId}", cusBankId);
        Response resGetBankId = RestAssured.given().spec(repoSpec)
                .when()
                .get(updateURI);
        System.out.println("Status Code: " + resGetBankId.getStatusCode());
        resGetBankId.getBody().prettyPrint();
        Assert.assertEquals(resGetBankId.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = resGetBankId.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.customerBankId").toString()), cusBankId, "Invalid customerBankId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.bankAccountName").toString()), createBankInfo.getBankAccountName(), "Invalid bankAccountName");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.bankAccountNo").toString()), createBankInfo.getBankAccountNo(), "Invalid bankAccountNo");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.bankId").toString()), String.valueOf(createBankInfo.getBankId()), "Invalid bankId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.branchId").toString()), String.valueOf(createBankInfo.getBranchId()), "Invalid branchId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.isPrimary").toString()), String.valueOf(createBankInfo.getIsPrimary()), "Invalid isPrimary");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.isActive").toString()), String.valueOf(createBankInfo.getIsActive()), "Invalid isActive");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-662 [R2] [CRM_API] Verify the GET - employment_bank_info with null ID (bank-info)")
    public void testGetBankInfoWithNullId() {
        String updateURI = APIConstants.GET_CUSTOMER_BANK_INFO_BANK_ID.replace("{bankId}", "");

        Response resGet = RestAssured.given().spec(repoSpec)
                .when()
                .get(updateURI);
        System.out.println("Status Code: " + resGet.getStatusCode());
        resGet.getBody().prettyPrint();
        Assert.assertEquals(resGet.statusCode(), 404);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-664 [R2] [CRM_API] Verify the GET - employment_bank_info with invalid ID (bank-info)")
    public void testGetBankInfoWithInvalidId() {
        String invalidId = "999999";
        String updateURI = APIConstants.GET_CUSTOMER_BANK_INFO_BANK_ID.replace("{bankId}", invalidId);

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
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "CM_M_CUSTOMER_BANK_NOT_FOUND", "Invalid error");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "There is no related data for the given bankId - " + invalidId, "Invalid description");
        softAssert.assertAll();
    }
}

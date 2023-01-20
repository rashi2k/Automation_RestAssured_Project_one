package tests.crm.bankBranch;

import com.informatics.endpoints.APIConstants;
import com.informatics.pojos.admin.Bank;
import com.informatics.pojos.admin.BankBranch;
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

public class PostBankBranch extends BaseTest {

    @BeforeClass
    public void initiate() {
        //get bank details
        getBaseBankBranchInfo();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-655 [R2][CRM_API] [Post] Validate the process of creating a bank branch with valid payload")
    public void testPostBankBranchWithValidPayLoad() {
        BankBranch createBankBranch = requestPayload.newBankBranch(bankId, bankName);
        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createBankBranch)
                .post(APIConstants.CREATE_BANK_BRANCH);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 201);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = response.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.bankId").toString()), String.valueOf(bankId), "Invalid bankId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.bankName").toString()), bankName, "Invalid bankName");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.code").toString()), createBankBranch.getCode(), "Invalid code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.name").toString()), createBankBranch.getName(), "Invalid name");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.swiftCode").toString()), createBankBranch.getSwiftCode(), "Invalid swiftCode");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.isActive").toString()), String.valueOf(createBankBranch.getIsActive()), "Invalid isActive");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-656 [R2][CRM_API] [Post] Validate the process of creating a bank branch with null payload")
    public void testPostBankBranchWithNullPayLoad() {
        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body("{ }")
                .post(APIConstants.CREATE_BANK_BRANCH);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);

        String statusCode = String.valueOf(response.getStatusCode());
        SoftAssert softAssert = new SoftAssert();
        String responseToString = response.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), statusCode, "Invalid status code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "SY_R_BANK_NOT_FOUND", "Invalid error");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "There is no bank related data for the given bank id -null", "Invalid description");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-673 [R2][CRM_API] [Post] Validate the process of creating a bank branch with Invalid Bank ID")
    public void testPostBankBranchWithInvalidBankId() {
        Integer invalidId = 99999;
        BankBranch createBankBranch = requestPayload.newBankBranch(bankId, bankName);
        createBankBranch.setBankId(invalidId);
        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createBankBranch)
                .post(APIConstants.CREATE_BANK_BRANCH);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);

        String statusCode = String.valueOf(response.getStatusCode());
        SoftAssert softAssert = new SoftAssert();
        String responseToString = response.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), statusCode, "Invalid status code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "SY_R_BANK_NOT_FOUND", "Invalid error");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "There is no bank related data for the given bank id -" + invalidId, "Invalid description");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-678 [R2][CRM_API] [Post] Validate the process of creating a bank branch with null Bank ID")
    public void testPostBankBranchWithNullBankId() {
        BankBranch createBankBranch = requestPayload.newBankBranch(bankId, bankName);
        createBankBranch.setBankId(null);
        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createBankBranch)
                .post(APIConstants.CREATE_BANK_BRANCH);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);

        String statusCode = String.valueOf(response.getStatusCode());
        SoftAssert softAssert = new SoftAssert();
        String responseToString = response.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), statusCode, "Invalid status code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "SY_R_BANK_NOT_FOUND", "Invalid error");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "There is no bank related data for the given bank id -null", "Invalid description");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-689 [R2][CRM_API] [Post] Validate the process of creating a bank branch with null code")
    public void testPostBankBranchWithNullCode() {
        BankBranch createBankBranch = requestPayload.newBankBranch(bankId, bankName);
        createBankBranch.setCode(null);
        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createBankBranch)
                .post(APIConstants.CREATE_BANK_BRANCH);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 406);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-691 [R2][CRM_API] [Post] Validate the process of creating a bank branch with null name")
    public void testPostBankBranchWithNullName() {
        BankBranch createBankBranch = requestPayload.newBankBranch(bankId, bankName);
        createBankBranch.setName(null);
        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createBankBranch)
                .post(APIConstants.CREATE_BANK_BRANCH);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 406);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-692 [R2][CRM_API] [Post] Validate the process of creating a bank branch with null swiftCod")
    public void testPostBankBranchWithNullSwiftCode() {
        BankBranch createBankBranch = requestPayload.newBankBranch(bankId, bankName);
        createBankBranch.setSwiftCode(null);
        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createBankBranch)
                .post(APIConstants.CREATE_BANK_BRANCH);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 406);
    }
}

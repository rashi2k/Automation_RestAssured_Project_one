package tests.crm.bankBranch;

import com.informatics.endpoints.APIConstants;
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

public class PutBankBranch extends BaseTest {

    @BeforeClass
    public void initiate() {
        //get bank details
        getBaseBankBranchInfo();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-706 [R2][CRM_API] [Put] Validate the process of updating a bank branch with valid payload")
    public void testPutBankBranchWithValidPayLoad() {
        BankBranch createBankBranch = requestPayload.newBankBranch(bankId, bankName);
        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(createBankBranch)
                .post(APIConstants.CREATE_BANK_BRANCH);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        String saveBranchId = JsonPath.read(resCreate.asString(), "$.result.id").toString();
        String updatedURI = APIConstants.UPDATE_BANK_BRANCH.replace("{branchId}", saveBranchId);

        BankBranch updateBankBranch = requestPayload.newBankBranch(bankId, bankName);
        updateBankBranch.setId(Integer.parseInt(saveBranchId));
        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateBankBranch)
                .put(updatedURI);
        System.out.println("Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = resUpdate.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.id").toString()), saveBranchId, "Invalid id");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.bankId").toString()), String.valueOf(bankId), "Invalid bankId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.bankName").toString()), updateBankBranch.getBankName(), "Invalid bankName");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.code").toString()), updateBankBranch.getCode(), "Invalid code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.name").toString()), updateBankBranch.getName(), "Invalid name");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.swiftCode").toString()), updateBankBranch.getSwiftCode(), "Invalid swiftCode");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.isActive").toString()), String.valueOf(updateBankBranch.getIsActive()), "Invalid isActive");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-717 [R2][CRM_API] [Put] Validate the process of updating a bank branch with NULL ID")
    public void testPutBankBranchWithNullId() {
        BankBranch createBankBranch = requestPayload.newBankBranch(bankId, bankName);
        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(createBankBranch)
                .post(APIConstants.CREATE_BANK_BRANCH);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        String saveBranchId = JsonPath.read(resCreate.asString(), "$.result.id").toString();
        String updatedURI = APIConstants.UPDATE_BANK_BRANCH.replace("{branchId}", saveBranchId);

        BankBranch updateBankBranch = requestPayload.newBankBranch(bankId, bankName);
        updateBankBranch.setId(null);
        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateBankBranch)
                .put(updatedURI);
        System.out.println("Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 404);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = resUpdate.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.message").toString()), "Null Id Found", "Invalid message");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.error").toString()), "ID_NULL", "Invalid error");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-720 [R2][CRM_API] [Put] Validate the process of updating a bank branch with Invalid ID")
    public void testPutBankBranchWithInvalidId() {
        String invalidId = "999999";
        String updatedURI = APIConstants.UPDATE_BANK_BRANCH.replace("{branchId}", invalidId);

        BankBranch updateBankBranch = requestPayload.newBankBranch(bankId, bankName);
        updateBankBranch.setId(Integer.parseInt(invalidId));
        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateBankBranch)
                .put(updatedURI);
        System.out.println("Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 404);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = resUpdate.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.message").toString()), "Entity not found", "Invalid message");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.error").toString()), "ID_NOT_FOUND", "Invalid error");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-721 [R2][CRM_API] [Put] Validate the process of updating a bank branch with Valid ID")
    public void testPutBankBranchWithValidId() {
        BankBranch createBankBranch = requestPayload.newBankBranch(bankId, bankName);
        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(createBankBranch)
                .post(APIConstants.CREATE_BANK_BRANCH);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        String saveBranchId = JsonPath.read(resCreate.asString(), "$.result.id").toString();
        String updatedURI = APIConstants.UPDATE_BANK_BRANCH.replace("{branchId}", saveBranchId);

        BankBranch updateBankBranch = requestPayload.newBankBranch(bankId, bankName);
        updateBankBranch.setId(Integer.parseInt(saveBranchId));
        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateBankBranch)
                .put(updatedURI);
        System.out.println("Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = resUpdate.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.id").toString()), saveBranchId, "Invalid id");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.bankId").toString()), String.valueOf(bankId), "Invalid bankId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.bankName").toString()), updateBankBranch.getBankName(), "Invalid bankName");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.code").toString()), updateBankBranch.getCode(), "Invalid code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.name").toString()), updateBankBranch.getName(), "Invalid name");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.swiftCode").toString()), updateBankBranch.getSwiftCode(), "Invalid swiftCode");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.isActive").toString()), String.valueOf(updateBankBranch.getIsActive()), "Invalid isActive");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-722 [R2][CRM_API] [Put] Validate the process of updating a bank branch with empty payload")
    public void testPutBankBranchWithEmptyPayLoad() {
        BankBranch createBankBranch = requestPayload.newBankBranch(bankId, bankName);
        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(createBankBranch)
                .post(APIConstants.CREATE_BANK_BRANCH);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        String saveBranchId = JsonPath.read(resCreate.asString(), "$.result.id").toString();
        String updatedURI = APIConstants.UPDATE_BANK_BRANCH.replace("{branchId}", saveBranchId);

        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body("{ }")
                .put(updatedURI);
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

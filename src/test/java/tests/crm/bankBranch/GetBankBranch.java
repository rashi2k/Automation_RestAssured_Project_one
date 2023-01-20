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

public class GetBankBranch extends BaseTest {

    @BeforeClass
    public void initiate() {
        //get bank details
        getBaseBankBranchInfo();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-525 [R2][CRM API] [Get] Validate the process of getting all the branches with valid payload")
    public void testGetBankBranchWithValidPayLoad() {
        BankBranch createBankBranch = requestPayload.newBankBranch(bankId, bankName);
        Response resCreate = RestAssured.given().spec(repoSpec)//create branch
                .when()
                .body(createBankBranch)
                .post(APIConstants.CREATE_BANK_BRANCH);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        String saveBranchId = JsonPath.read(resCreate.asString(), "$.result.id").toString();
        String updatedURI = APIConstants.GET_BANK_BRANCH+ "?page=0&size=999999";

        Response resGet = RestAssured.given().spec(repoSpec)//get all branches
                .when()
                .get(updatedURI);
        System.out.println("Status Code: " + resGet.getStatusCode());
        resGet.getBody().prettyPrint();
        Assert.assertEquals(resGet.statusCode(), 200);

        String responseToString = resGet.asString();
        Boolean isBankBranchFound = false;
        int length = JsonPath.read(responseToString, "$.result.length()");

        SoftAssert softAssert = new SoftAssert();
        for (int i = 0; i < length; i++) {
            String branchId = JsonPath.read(responseToString, "$.result[" + i + "].id").toString();
            if (branchId.equals(saveBranchId)) {
                isBankBranchFound = true;
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].id").toString()), saveBranchId, "Invalid id");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].bankId").toString()), String.valueOf(createBankBranch.getBankId()), "Invalid bankId");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].bankName").toString()), createBankBranch.getBankName(), "Invalid bankName");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].code").toString()), createBankBranch.getCode(), "Invalid code");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].name").toString()), createBankBranch.getName(), "Invalid name");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].swiftCode").toString()), createBankBranch.getSwiftCode(), "Invalid swiftCode");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].isActive").toString()), String.valueOf(createBankBranch.getIsActive()), "Invalid isActive");
                break;
            }
        }
        softAssert.assertTrue(isBankBranchFound, "Bank Branch not found");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-529 [R2][CRM_API] [Get] Validate the process of getting bank-branch details with valid ID")
    public void testGetBankBranchWithValidId() {
        BankBranch createBankBranch = requestPayload.newBankBranch(bankId, bankName);
        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(createBankBranch)
                .post(APIConstants.CREATE_BANK_BRANCH);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        String saveBranchId = JsonPath.read(resCreate.asString(), "$.result.id").toString();
        String updatedURI = APIConstants.GET_BANK_BRANCH_ID.replace("{id}", saveBranchId);

        Response resGet = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("Status Code: " + resGet.getStatusCode());
        resGet.getBody().prettyPrint();
        Assert.assertEquals(resGet.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = resGet.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.id").toString()), saveBranchId, "Invalid id");
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
    @Description("IQ-528 [R2][CRM_API] [Get] Validate the process of getting bank-branch details with Invalid ID")
    public void testGetBankBranchWithInvalidId() {
        String invalidId = "999999";
        String updatedURI = APIConstants.GET_BANK_BRANCH_ID.replace("{id}", invalidId);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);

        String statusCode = String.valueOf(response.getStatusCode());
        SoftAssert softAssert = new SoftAssert();
        String responseToString = response.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), statusCode, "Invalid status code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "BRANCH_ID_DOES_NOT_EXIST", "Invalid error");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "Branch id does not exist - ", "Invalid description");
        softAssert.assertAll();
    }

    /*
    //IGNORED - ISSUE WITH REQUEST URL WHEN SENDING EMPTY LOAD
    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-527 [R2][CRM_API] [Get] Validate the process of getting bank-branch details with empty ID")
    public void testGetBankBranchWithEmptyId() {
        String updatedURI = APIConstants.GET_BANK_BRANCH_ID.replace("{id}", "");

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);
    }
     */

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-645 [R2][CRM_API] [Get] Validate the process of getting bank-branch details with Valid bank code")
    public void testGetBankBranchWithValidCode() {
        BankBranch createBankBranch = requestPayload.newBankBranch(bankId, bankName);
        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(createBankBranch)
                .post(APIConstants.CREATE_BANK_BRANCH);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        String saveBranchId = JsonPath.read(resCreate.asString(), "$.result.id").toString();
        String saveBranchCode = JsonPath.read(resCreate.asString(), "$.result.code").toString();
        String updatedURI = APIConstants.GET_BANK_BRANCH_CODE.replace("{branchCode}", saveBranchCode);

        Response resGet = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("Status Code: " + resGet.getStatusCode());
        resGet.getBody().prettyPrint();
        Assert.assertEquals(resGet.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = resGet.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.id").toString()), saveBranchId, "Invalid id");
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
    @Description("IQ-643 [R2][CRM_API] [Get] Validate the process of getting bank-branch details with non exist bank code")
    public void testGetBankBranchWithNonExistingCode() {
        String invalidCode = "X@999999";
        String updatedURI = APIConstants.GET_BANK_BRANCH_CODE.replace("{branchCode}", invalidCode);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);

        String statusCode = String.valueOf(response.getStatusCode());
        SoftAssert softAssert = new SoftAssert();
        String responseToString = response.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), statusCode, "Invalid status code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "BRANCH_CODE_DOES_NOT_EXIST", "Invalid error");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "Branch code does not exist - ", "Invalid description");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-642 [R2][CRM_API] [Get] Validate the process of getting bank-branch details with NULL bank code")
    public void testGetBankBranchWithNullCode() {
        String updatedURI = APIConstants.GET_BANK_BRANCH_CODE.replace("{branchCode}", "");

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-628 [R2][CRM_API] [Get] Validate the process of getting bank-branch details with Valid bank name")
    public void testGetBankBranchWithValidName() {
        BankBranch createBankBranch = requestPayload.newBankBranch(bankId, bankName);
        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(createBankBranch)
                .post(APIConstants.CREATE_BANK_BRANCH);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        String saveBranchId = JsonPath.read(resCreate.asString(), "$.result.id").toString();
        String saveBranchName = JsonPath.read(resCreate.asString(), "$.result.name").toString();
        String updatedURI = APIConstants.GET_BANK_BRANCH_SEARCH.replace("{name}", saveBranchName);

        Response resGet = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("Status Code: " + resGet.getStatusCode());
        resGet.getBody().prettyPrint();
        Assert.assertEquals(resGet.statusCode(), 200);

        String responseToString = resGet.asString();
        Boolean isBankBranchFound = false;
        int length = JsonPath.read(responseToString, "$.result.length()");

        SoftAssert softAssert = new SoftAssert();
        for (int i = 0; i < length; i++) {
            String branchId = JsonPath.read(responseToString, "$.result[" + i + "].id").toString();
            if (branchId.equals(saveBranchId)) {
                isBankBranchFound = true;
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].id").toString()), saveBranchId, "Invalid id");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].bankId").toString()), String.valueOf(createBankBranch.getBankId()), "Invalid bankId");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].bankName").toString()), createBankBranch.getBankName(), "Invalid bankName");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].code").toString()), createBankBranch.getCode(), "Invalid code");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].name").toString()), createBankBranch.getName(), "Invalid name");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].swiftCode").toString()), createBankBranch.getSwiftCode(), "Invalid swiftCode");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].isActive").toString()), String.valueOf(createBankBranch.getIsActive()), "Invalid isActive");
                break;
            }
        }
        softAssert.assertTrue(isBankBranchFound, "Bank Branch not found");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-621 [R2][CRM_API] [Get] Validate the process of getting bank-branch details with non exist name")
    public void testGetBankBranchWithNonExistingName() {
        String invalidName = "@X@6767@#";
        String updatedURI = APIConstants.GET_BANK_BRANCH_SEARCH.replace("{name}", invalidName);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = response.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result").toString()), "[]", "Invalid result");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-603 [R2][CRM_API] [Get] Validate the process of getting bank-branch details with NULL bank name")
    public void testGetBankBranchWithNullName() {
        String updatedURI = APIConstants.GET_BANK_BRANCH_SEARCH.replace("{name}", "");

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);
    }

}

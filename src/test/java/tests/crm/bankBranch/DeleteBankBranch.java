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

public class DeleteBankBranch extends BaseTest {


    @BeforeClass
    public void initiate() {
        //get bank details
        getBaseBankBranchInfo();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-600 [R2][CRM_API] [Delete] Validate the process of deleting branch details with Valid ID")
    public void testDeleteBankBranchWithValidId() {
        BankBranch createBankBranch = requestPayload.newBankBranch(bankId, bankName);
        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(createBankBranch)
                .post(APIConstants.CREATE_BANK_BRANCH);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        String saveBranchId = JsonPath.read(resCreate.asString(), "$.result.id").toString();
        String updatedURI = APIConstants.DELETE_BANK_BRANCH.replace("{branchId}", saveBranchId);

        Response resDelete = RestAssured.given().spec(repoSpec)
                .when()
                .delete(updatedURI);
        System.out.println("Status Code: " + resDelete.getStatusCode());
        resDelete.getBody().prettyPrint();
        Assert.assertEquals(resDelete.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = resDelete.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result").toString()), saveBranchId, "Invalid result");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-531 [R2][CRM_API] [Delete] Validate the process of deleting branch details with Invalid ID")
    public void testDeleteBankBranchWithInvalidId() {
        String invalidId = "999999";
        String updatedURI = APIConstants.DELETE_BANK_BRANCH.replace("{branchId}", invalidId);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .delete(updatedURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);

        String statusCode = String.valueOf(response.getStatusCode());
        SoftAssert softAssert = new SoftAssert();
        String responseToString = response.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), statusCode, "Invalid status code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "SY_R_BANK_BRANCH_NOT_FOUND", "Invalid error");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "There is no bank branch related data for the given branch id -" + invalidId, "Invalid description");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-530 [R2][CRM_API] [Delete] Validate the process of deleting branch details with Null ID")
    public void testDeleteBankBranchWithNullId() {
        String updatedURI = APIConstants.DELETE_BANK_BRANCH.replace("{branchId}", "");

        Response resDelete = RestAssured.given().spec(repoSpec)
                .when()
                .delete(updatedURI);
        System.out.println("Status Code: " + resDelete.getStatusCode());
        resDelete.getBody().prettyPrint();
        Assert.assertEquals(resDelete.statusCode(), 405);
    }
}

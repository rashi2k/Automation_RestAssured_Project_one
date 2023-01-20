package tests.crm.bank;

import com.informatics.endpoints.APIConstants;
import com.informatics.pojos.admin.Bank;
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

public class DeleteBank extends BaseTest {

    @BeforeClass
    public void initiate() {
        //get country data
        getBaseCountryInfo();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-552 [R2] [CRM_API] Verify the DELETE - banks/{id} with Valid Pay Load")
    public void testDeleteBankUsingValidPayLoad() {
        Bank createBank = requestPayload.newBank(country);
        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(createBank)
                .post(APIConstants.CREATE_BANK);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 200);

        String saveBankId = JsonPath.read(resCreate.asString(), "$.result.id").toString();
        String updatedURI = APIConstants.DELETE_BANK.replace("{id}", saveBankId);

        Response resDelete = RestAssured.given().spec(repoSpec)
                .when()
                .delete(updatedURI);
        System.out.println("Status Code: " + resDelete.getStatusCode());
        resDelete.getBody().prettyPrint();
        Assert.assertEquals(resDelete.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = resDelete.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result").toString()), saveBankId, "Invalid result");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-553 [R2] [CRM_API] Verify the DELETE - banks/{id} with empty Pay Load")
    public void testDeleteBankUsingEmptyPayLoad() {
        String updatedURI = APIConstants.DELETE_BANK.replace("{id}", "");

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .delete(updatedURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 405);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-555 [R2] [CRM_API] Verify the DELETE - banks/{id} using invalid id")
    public void testDeleteBankUsingInvalidId() {
        String invalidId = "99999";
        String updatedURI = APIConstants.DELETE_BANK.replace("{id}", invalidId);

        Bank updateBank = requestPayload.newBank(country);
        updateBank.setId(Integer.parseInt(invalidId));
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
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "BANK_ID_DOES_NOT_EXIST", "Invalid error");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "Bank id does not exist - " + invalidId, "Invalid description");
        softAssert.assertAll();
    }
}

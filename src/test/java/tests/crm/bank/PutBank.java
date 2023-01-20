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

public class PutBank extends BaseTest {

    @BeforeClass
    public void initiate() {
        //get country data
        getBaseCountryInfo();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-549 [R2] [CRM_API] Verify the PUT - banks/{id} with Valid Pay Load")
    public void testPutBankUsingValidPayLoad() {
        Bank createBank = requestPayload.newBank(country);
        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(createBank)
                .post(APIConstants.CREATE_BANK);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 200);

        String saveBankId = JsonPath.read(resCreate.asString(), "$.result.id").toString();
        String updatedURI = APIConstants.UPDATE_BANK.replace("{id}", saveBankId);

        Bank updateBank = requestPayload.newBank(country);
        updateBank.setId(Integer.parseInt(saveBankId));
        Response resGet = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateBank)
                .put(updatedURI);
        System.out.println("Status Code: " + resGet.getStatusCode());
        resGet.getBody().prettyPrint();
        Assert.assertEquals(resGet.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = resGet.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.id").toString()), saveBankId, "Invalid code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.code").toString()), updateBank.getCode(), "Invalid code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.name").toString()), updateBank.getName(), "Invalid name");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.country").toString()), updateBank.getCountry(), "Invalid country");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-549 [R2] [CRM_API] Verify the PUT - banks/{id} with Valid Pay Load")
    public void testPutBankUsingEmptyPayLoad() {
        Bank createBank = requestPayload.newBank(country);
        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(createBank)
                .post(APIConstants.CREATE_BANK);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 200);

        String saveBankId = JsonPath.read(resCreate.asString(), "$.result.id").toString();
        String updatedURI = APIConstants.UPDATE_BANK.replace("{id}", saveBankId);

        Response resGet = RestAssured.given().spec(repoSpec)
                .when()
                .body("{ }")
                .put(updatedURI);
        System.out.println("Status Code: " + resGet.getStatusCode());
        resGet.getBody().prettyPrint();
        Assert.assertEquals(resGet.statusCode(), 404);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = resGet.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.message").toString()), "Null Id Found", "Invalid status code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.error").toString()), "ID_NULL", "Invalid error");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-549 [R2] [CRM_API] Verify the PUT - banks/{id} with Valid Pay Load")
    public void testPutBankUsingInvalidId() {
        String invalidId = "99999";
        String updatedURI = APIConstants.UPDATE_BANK.replace("{id}", invalidId);

        Bank updateBank = requestPayload.newBank(country);
        updateBank.setId(Integer.parseInt(invalidId));
        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateBank)
                .put(updatedURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 404);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = response.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.message").toString()), "Entity not found", "Invalid status code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.error").toString()), "ID_NOT_FOUND", "Invalid error");
        softAssert.assertAll();
    }
}

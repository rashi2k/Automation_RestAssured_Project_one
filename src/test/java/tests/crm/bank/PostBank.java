package tests.crm.bank;

import com.informatics.endpoints.APIConstants;
import com.informatics.pojos.admin.Bank;
import com.informatics.pojos.admin.Tax;
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

public class PostBank extends BaseTest {

    @BeforeClass
    public void initiate() {
        //get country data
        getBaseCountryInfo();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-556 [R2] [CRM_API] Verify the POST - banks with Valid Pay Load")
    public void testPostBankWithValidPayLoad() {
        Bank createBank = requestPayload.newBank(country);
        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createBank)
                .post(APIConstants.CREATE_BANK);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = response.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.code").toString()), createBank.getCode(), "Invalid code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.name").toString()), createBank.getName(), "Invalid name");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.country").toString()), createBank.getCountry(), "Invalid country");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-557 [R2] [CRM_API] Verify the POST - banks with empty Pay Load")
    public void testPostBankWithEmptyPayLoad() {
        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body("{ }")
                .post(APIConstants.CREATE_BANK);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);

        String statusCode = String.valueOf(response.getStatusCode());
        SoftAssert softAssert = new SoftAssert();
        String responseToString = response.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), statusCode, "Invalid status code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "COUNTRY_CODE_DOES_NOT_EXIST", "Invalid error");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "country code does not exist - null", "Invalid description");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-558 [R2] [CRM_API] Verify the POST - banks with invalid data for id, code & country")
    public void testPostBankWithInvalidIdCodeAndCountry() {
        String invalidCode = "X0000001";
        String invalidCountry = "exx";

        Bank createBank = requestPayload.newBank(country);
        createBank.setCode(invalidCode);
        createBank.setCountry(invalidCountry);
        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createBank)
                .post(APIConstants.CREATE_BANK);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);

        String statusCode = String.valueOf(response.getStatusCode());
        SoftAssert softAssert = new SoftAssert();
        String responseToString = response.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), statusCode, "Invalid status code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "COUNTRY_CODE_DOES_NOT_EXIST", "Invalid error");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "country code does not exist - " + invalidCountry, "Invalid description");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-560 [R2] [CRM_API] Verify the POST - banks with invalid data for country & valid id,code")
    public void testPostBankWithInvalidIdCountry() {
        String invalidCountry = "exx";
        Bank createBank = requestPayload.newBank(country);
        createBank.setCountry(invalidCountry);
        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createBank)
                .post(APIConstants.CREATE_BANK);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);

        String statusCode = String.valueOf(response.getStatusCode());
        SoftAssert softAssert = new SoftAssert();
        String responseToString = response.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), statusCode, "Invalid status code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "COUNTRY_CODE_DOES_NOT_EXIST", "Invalid error");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "country code does not exist - " + invalidCountry, "Invalid description");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-561 [R2] [CRM_API] Verify the POST - banks with valid data for id, country & empty code")
    public void testPostBankWithEmptyCode() {
        Bank createBank = requestPayload.newBank(country);
        createBank.setCode("");
        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createBank)
                .post(APIConstants.CREATE_BANK);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 406);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-562 [R2] [CRM_API] Verify the POST - banks with valid data for id, code & empty country")
    public void testPostBankWithEmptyCountry() {
        Bank createBank = requestPayload.newBank(country);
        createBank.setCountry("");
        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createBank)
                .post(APIConstants.CREATE_BANK);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);

        String statusCode = String.valueOf(response.getStatusCode());
        SoftAssert softAssert = new SoftAssert();
        String responseToString = response.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), statusCode, "Invalid status code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "COUNTRY_CODE_DOES_NOT_EXIST", "Invalid error");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "country code does not exist - ", "Invalid description");
        softAssert.assertAll();
    }
}

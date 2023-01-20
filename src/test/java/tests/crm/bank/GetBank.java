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

public class GetBank extends BaseTest {

    @BeforeClass
    public void initiate() {
        getBaseCountryInfo();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-538 [R2] [CRM_API] Verify the GET - banks with Valid Pay Load")
    public void testGetAllBanksWithValidPayLoad() {
        //create bank
        Bank createBank = requestPayload.newBank(country);
        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(createBank)
                .post(APIConstants.CREATE_BANK);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 200);

        String saveBankId = JsonPath.read(resCreate.asString(), "$.result.id").toString();
        String updatedURI = APIConstants.GET_BANK + "?page=0&size=999999";

        //get all banks
        Response resGet = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("Status Code: " + resGet.getStatusCode());
        resGet.getBody().prettyPrint();
        Assert.assertEquals(resGet.statusCode(), 200);

        String responseToString = resGet.asString();
        Boolean isBankFound = false;
        int length = JsonPath.read(responseToString, "$.result.length()");

        SoftAssert softAssert = new SoftAssert();
        for (int i = 0; i < length; i++) {
            String bankId = JsonPath.read(responseToString, "$.result[" + i + "].id").toString();
            if (bankId.equals(saveBankId)) {
                isBankFound = true;
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].id").toString()), saveBankId, "Invalid id");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].code").toString()), createBank.getCode(), "Invalid code");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].name").toString()), createBank.getName(), "Invalid name");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].country").toString()), createBank.getCountry(), "Invalid country");
                break;
            }
        }
        softAssert.assertTrue(isBankFound, "Bank not found");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-543 [R2] [CRM_API] Verify the GET - banks /{id} with Valid Pay Load")
    public void testGetBankUsingValidId() {
        Bank createBank = requestPayload.newBank(country);
        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(createBank)
                .post(APIConstants.CREATE_BANK);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 200);

        String saveBankId = JsonPath.read(resCreate.asString(), "$.result.id").toString();
        String updatedURI = APIConstants.GET_BANK_ID.replace("{id}", saveBankId);

        Response resGet = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("Status Code: " + resGet.getStatusCode());
        resGet.getBody().prettyPrint();
        Assert.assertEquals(resGet.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = resGet.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.id").toString()), saveBankId, "Invalid code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.code").toString()), createBank.getCode(), "Invalid code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.name").toString()), createBank.getName(), "Invalid name");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.country").toString()), createBank.getCountry(), "Invalid country");
        softAssert.assertAll();
    }

    /*
    //IGNORED - ISSUE WITH REQUEST URL WHEN SENDING EMPTY LOAD
    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-544 [R2] [CRM_API] Verify the GET - banks /{id} with empty Pay Load")
    public void testGetBankUsingEmptyId() {
        String updatedURI = APIConstants.GET_BANK_ID.replace("{id}", "");

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
    @Description("IQ-545 [R2] [CRM_API] Verify the GET - banks /{id} using invalid ID")
    public void testGetBankUsingInvalidId() {
        String invalidId = "99999";
        String updatedURI = APIConstants.GET_BANK_ID.replace("{id}", invalidId);

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
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "BANK_ID_DOES_NOT_EXIST", "Invalid error");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "Bank id does not exist - " + invalidId, "Invalid description");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-546 [R2] [CRM_API] Verify the GET - banks /{code} with Valid Pay Load")
    public void testGetBankUsingValidCode() {
        Bank createBank = requestPayload.newBank(country);
        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(createBank)
                .post(APIConstants.CREATE_BANK);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 200);

        String saveBankId = JsonPath.read(resCreate.asString(), "$.result.id").toString();
        String saveBankCode = JsonPath.read(resCreate.asString(), "$.result.code").toString();
        String updatedURI = APIConstants.GET_BANK_CODE.replace("{code}", saveBankCode);

        Response resGet = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("Status Code: " + resGet.getStatusCode());
        resGet.getBody().prettyPrint();
        Assert.assertEquals(resGet.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = resGet.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.id").toString()), saveBankId, "Invalid code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.code").toString()), saveBankCode, "Invalid code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.name").toString()), createBank.getName(), "Invalid name");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.country").toString()), createBank.getCountry(), "Invalid country");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-547 [R2] [CRM_API] Verify the GET - banks /{code} with empty Pay Load")
    public void testGetBankUsingEmptyCode() {
        String updatedURI = APIConstants.GET_BANK_CODE.replace("{code}", "");;

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-548 [R2] [CRM_API] Verify the GET - banks /{code} using invalid code")
    public void testGetBankUsingInvalidCode() {
        String invalidCode = "X99999";
        String updatedURI = APIConstants.GET_BANK_CODE.replace("{code}", invalidCode);

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
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "BANK_CODE_DOES_NOT_EXIST", "Invalid error");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "Bank code does not exist - " + invalidCode, "Invalid description");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-539 [R2] [CRM_API] Verify the GET - banks /search/{name} with valid data")
    public void testGetBankUsingValidSearchName() {
        Bank createBank = requestPayload.newBank(country);
        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(createBank)
                .post(APIConstants.CREATE_BANK);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 200);

        String saveBankId = JsonPath.read(resCreate.asString(), "$.result.id").toString();
        String saveBankName = JsonPath.read(resCreate.asString(), "$.result.name").toString();
        String updatedURI = APIConstants.GET_BANK_SEARCH.replace("{name}", saveBankName);

        Response resGet = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("Status Code: " + resGet.getStatusCode());
        resGet.getBody().prettyPrint();
        Assert.assertEquals(resGet.statusCode(), 200);

        String responseToString = resGet.asString();
        Boolean isBankFound = false;
        int length = JsonPath.read(responseToString, "$.result.length()");

        SoftAssert softAssert = new SoftAssert();
        for (int i = 0; i < length; i++) {
            String bankId = JsonPath.read(responseToString, "$.result[" + i + "].id").toString();
            if (bankId.equals(saveBankId)) {
                isBankFound = true;
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].id").toString()), saveBankId, "Invalid id");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].code").toString()), createBank.getCode(), "Invalid code");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].name").toString()), saveBankName, "Invalid name");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].country").toString()), createBank.getCountry(), "Invalid country");
                break;
            }
        }
        softAssert.assertTrue(isBankFound, "Bank not found");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-542 [R2] [CRM_API] Verify the GET - banks /search/{name} with similar data")
    public void testGetBankUsingSimilarSearchName() {
        Bank createBank = requestPayload.newBank(country);
        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(createBank)
                .post(APIConstants.CREATE_BANK);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 200);

        String saveBankId = JsonPath.read(resCreate.asString(), "$.result.id").toString();
        String saveBankName = JsonPath.read(resCreate.asString(), "$.result.name").toString();
        String updatedURI = APIConstants.GET_BANK_SEARCH.replace("{name}", saveBankName.substring(0,11));

        Response resGet = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("Status Code: " + resGet.getStatusCode());
        resGet.getBody().prettyPrint();
        Assert.assertEquals(resGet.statusCode(), 200);

        String responseToString = resGet.asString();
        Boolean isBankFound = false;
        int length = JsonPath.read(responseToString, "$.result.length()");

        SoftAssert softAssert = new SoftAssert();
        for (int i = 0; i < length; i++) {
            String bankId = JsonPath.read(responseToString, "$.result[" + i + "].id").toString();
            if (bankId.equals(saveBankId)) {
                isBankFound = true;
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].id").toString()), saveBankId, "Invalid id");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].code").toString()), createBank.getCode(), "Invalid code");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].name").toString()), saveBankName, "Invalid name");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].country").toString()), createBank.getCountry(), "Invalid country");
                break;
            }
        }
        softAssert.assertTrue(isBankFound, "Bank not found");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-540 [R2] [CRM_API] Verify the GET - banks /search/{name} with invalid data")
    public void testGetBankUsingInvalidSearchName() {
        String invalidName = "EX_AUT_1234";
        String updatedURI = APIConstants.GET_BANK_SEARCH.replace("{name}", invalidName);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = response.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result").toString()), "[]", "Invalid status code");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-541 [R2] [CRM_API] Verify the GET - banks /search/{name} with empty data")
    public void testGetBankUsingEmptySearchName() {
        String updatedURI = APIConstants.GET_BANK_SEARCH.replace("{name}", "");

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);
    }
}

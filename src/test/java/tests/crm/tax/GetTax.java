package tests.crm.tax;

import com.informatics.endpoints.APIConstants;
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

public class GetTax extends BaseTest {

    @BeforeClass
    public void initiate() {
        //get country data
        getBaseCountryInfo();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-554 [R2] [CRM_API] Verify GET - (taxes_list)tax_types")
    public void testGetAllTaxTypes() {
        Tax createTaxType = requestPayload.createTaxType(countryId, countryName);
        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(createTaxType)
                .post(APIConstants.CREATE_TAX);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        String saveTaxTypeId = JsonPath.read(resCreate.asString(), "$.result.taxTypeId").toString();

        Response resGet = RestAssured.given().spec(repoSpec)
                .when()
                .get(APIConstants.GET_TAX);
        System.out.println("Status Code: " + resGet.getStatusCode());
        resGet.getBody().prettyPrint();
        Assert.assertEquals(resGet.statusCode(), 200);

        String responseToString = resGet.asString();
        Boolean isTaxTypeFound= false;
        int length = JsonPath.read(responseToString, "$.result.length()");

        SoftAssert softAssert = new SoftAssert();
        for (int i = 0; i < length; i++) {
            String taxTypeId = JsonPath.read(responseToString, "$.result[" + i + "].taxTypeId").toString();
            if (taxTypeId.equals(saveTaxTypeId)) {
                isTaxTypeFound = true;
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].taxTypeId").toString()), saveTaxTypeId, "Invalid taxTypeId");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].taxTypeCode").toString()), createTaxType.getTaxTypeCode(), "Invalid taxTypeCode");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].taxType").toString()), createTaxType.getTaxType(), "Invalid taxType");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].isActive").toString()), String.valueOf(createTaxType.getIsActive()), "Invalid isActive");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].countryId").toString()), String.valueOf(createTaxType.getCountryId()), "Invalid countryId");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].countryName").toString()), createTaxType.getCountryName(), "Invalid countryName");
                break;
            }
        }
        softAssert.assertTrue(isTaxTypeFound, "Tax Type not found");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-613 [R2] [CRM_API] Verify GET - (taxes_list)tax_types{id} with Valid id")
    public void testGetTaxTypeWithValidId() {
        Tax createTaxType = requestPayload.createTaxType(countryId, countryName);
        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(createTaxType)
                .post(APIConstants.CREATE_TAX);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        String saveTaxTypeId = JsonPath.read(resCreate.asString(), "$.result.taxTypeId").toString();
        String updateURI = APIConstants.GET_TAX_TYPE_ID.replace("{taxTypeId}", saveTaxTypeId);

        Response resGet = RestAssured.given().spec(repoSpec)
                .when()
                .get(updateURI);
        System.out.println("Status Code: " + resGet.getStatusCode());
        resGet.getBody().prettyPrint();
        Assert.assertEquals(resGet.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = resGet.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.taxTypeId").toString()), saveTaxTypeId, "Invalid taxTypeId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.taxTypeCode").toString()), createTaxType.getTaxTypeCode(), "Invalid taxTypeCode");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.taxType").toString()), createTaxType.getTaxType(), "Invalid taxType");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.isActive").toString()), String.valueOf(createTaxType.getIsActive()), "Invalid isActive");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.countryId").toString()), String.valueOf(createTaxType.getCountryId()), "Invalid countryId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.countryName").toString()), createTaxType.getCountryName(), "Invalid countryName");;
        softAssert.assertAll();
    }

    /*
    //IGNORED - ISSUE WITH REQUEST URL WHEN SENDING EMPTY LOAD
    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-614 [R2] [CRM_API] Verify GET - (taxes_list)tax_types{id} with Null id")
    public void testGetTaxTypeWithNullId() {
        String updateURI = APIConstants.GET_TAX_TYPE_ID.replace("{taxTypeId}", "");

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .get(updateURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);
    }
     */

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-615 [R2] [CRM_API] Verify GET - (taxes_list)tax_types{id} with Invalid id")
    public void testGetTaxTypeWithInvalidId() {
        String invalidId = "99999";
        String updateURI = APIConstants.GET_TAX_TYPE_ID.replace("{taxTypeId}", invalidId);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .get(updateURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);

        String statusCode = String.valueOf(response.getStatusCode());
        SoftAssert softAssert = new SoftAssert();
        String responseToString = response.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), statusCode, "Invalid status code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "SY_R_TAX_TYPE_NOT_FOUND", "Invalid error");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "There is no  related data for the given tax type id - " + invalidId, "Invalid description");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-617 [R2] [CRM_API] Verify GET - (taxes_list)tax_types{code} with Valid code")
    public void testGetTaxTypeWithValidCode() {
        Tax createTaxType = requestPayload.createTaxType(countryId, countryName);
        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(createTaxType)
                .post(APIConstants.CREATE_TAX);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        String saveTaxTypeId = JsonPath.read(resCreate.asString(), "$.result.taxTypeId").toString();
        String saveTaxTypeCode = JsonPath.read(resCreate.asString(), "$.result.taxTypeCode").toString();
        String updateURI = APIConstants.GET_TAX_TYPE_CODE.replace("{taxTypeCode}", saveTaxTypeCode);

        Response resGet = RestAssured.given().spec(repoSpec)
                .when()
                .get(updateURI);
        System.out.println("Status Code: " + resGet.getStatusCode());
        resGet.getBody().prettyPrint();
        Assert.assertEquals(resGet.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = resGet.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.taxTypeId").toString()), saveTaxTypeId, "Invalid taxTypeId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.taxTypeCode").toString()), saveTaxTypeCode, "Invalid taxTypeCode");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.taxType").toString()), createTaxType.getTaxType(), "Invalid taxType");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.isActive").toString()), String.valueOf(createTaxType.getIsActive()), "Invalid isActive");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.countryId").toString()), String.valueOf(createTaxType.getCountryId()), "Invalid countryId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.countryName").toString()), createTaxType.getCountryName(), "Invalid countryName");;
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-619 [R2] [CRM_API] Verify GET - (taxes_list)tax_types{code} with Null code")
    public void testGetTaxTypeWithNullCode() {
        String updateURI = APIConstants.GET_TAX_TYPE_CODE.replace("{taxTypeCode}", "");

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .get(updateURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-620 [R2] [CRM_API] Verify GET - (taxes_list)tax_types{code} with Invalid code")
    public void testGetTaxTypeWithInvalidCode() {
        String invalidCode = "TEST_CODE";
        String updateURI = APIConstants.GET_TAX_TYPE_CODE.replace("{taxTypeCode}", invalidCode);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .get(updateURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);

        String statusCode = String.valueOf(response.getStatusCode());
        SoftAssert softAssert = new SoftAssert();
        String responseToString = response.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), statusCode, "Invalid status code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "SY_R_TAX_TYPE_NOT_FOUND", "Invalid error");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "There is no  related data for the given tax type code - " + invalidCode, "Invalid description");
        softAssert.assertAll();
    }

}

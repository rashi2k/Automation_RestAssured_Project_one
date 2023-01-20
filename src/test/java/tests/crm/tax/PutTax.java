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

public class PutTax extends BaseTest {

    @BeforeClass
    public void initiate() {
        //get country data
        getBaseCountryInfo();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-630 [R2] [CRM_API] Verify Put - (taxes_list)tax_types{id} with Valid Pay Load")
    public void testPutTaxTypeWithValidPayLoad() {
        Tax createTaxType = requestPayload.createTaxType(countryId, countryName);
        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(createTaxType)
                .post(APIConstants.CREATE_TAX);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        String saveTaxTypeId = JsonPath.read(resCreate.asString(), "$.result.taxTypeId").toString();
        String updateURI = APIConstants.UPDATE_TAX.replace("{taxTypeId}", saveTaxTypeId);

        Tax updateTaxType = requestPayload.createTaxType(countryId, countryName);
        updateTaxType.setTaxTypeId(saveTaxTypeId);

        Response resGet = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateTaxType)
                .put(updateURI);
        System.out.println("Status Code: " + resGet.getStatusCode());
        resGet.getBody().prettyPrint();
        Assert.assertEquals(resGet.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = resGet.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.taxTypeId").toString()), saveTaxTypeId, "Invalid taxTypeId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.taxTypeCode").toString()), updateTaxType.getTaxTypeCode(), "Invalid taxTypeCode");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.taxType").toString()), updateTaxType.getTaxType(), "Invalid taxType");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.isActive").toString()), String.valueOf(updateTaxType.getIsActive()), "Invalid isActive");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.countryId").toString()), String.valueOf(updateTaxType.getCountryId()), "Invalid countryId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.countryName").toString()), updateTaxType.getCountryName(), "Invalid countryName");;
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-631 [R2] [CRM_API] Verify Put - (taxes_list)tax_types{id} with Empty Pay Load")
    public void testPutTaxTypeWithEmptyPayLoad() {
        String updateURI = APIConstants.UPDATE_TAX.replace("{taxTypeId}", "");

        Tax updateTaxType = requestPayload.createTaxType(countryId, countryName);
        updateTaxType.setTaxTypeId(null);
        updateTaxType.setTaxTypeCode(null);
        updateTaxType.setTaxType(null);
        updateTaxType.setIsActive(null);
        updateTaxType.setCountryId(null);
        updateTaxType.setCountryName(null);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateTaxType)
                .put(updateURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 405);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-632 [R2] [CRM_API] Verify Put - (taxes_list)tax_types{id} with Invalid id")
    public void testPutTaxTypeWithInvalidId() {
        String invalidId = "99999";
        String updateURI = APIConstants.UPDATE_TAX.replace("{taxTypeId}", invalidId);

        Tax updateTaxType = requestPayload.createTaxType(countryId, countryName);
        updateTaxType.setTaxTypeId(invalidId);
        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateTaxType)
                .put(updateURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 404);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = response.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.message").toString()), "Entity not found", "Invalid message");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.error").toString()), "ID_NOT_FOUND", "Invalid error");
        softAssert.assertAll();
    }
}

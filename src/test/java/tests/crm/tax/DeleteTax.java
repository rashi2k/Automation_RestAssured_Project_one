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

public class DeleteTax extends BaseTest {

    @BeforeClass
    public void initiate() {
        //get country data
        getBaseCountryInfo();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-633 [R2] [CRM_API] Verify Delete - (taxes_list)tax_types{id} with Valid id")
    public void testDeleteTaxTypeWithValidId() {
        Tax createTaxType = requestPayload.createTaxType(countryId, countryName);
        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(createTaxType)
                .post(APIConstants.CREATE_TAX);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        String saveTaxTypeId = JsonPath.read(resCreate.asString(), "$.result.taxTypeId").toString();
        String updateURI = APIConstants.DELETE_TAX.replace("{taxTypeId}", saveTaxTypeId);

        Response resGet = RestAssured.given().spec(repoSpec)
                .when()
                .delete(updateURI);
        System.out.println("Status Code: " + resGet.getStatusCode());
        resGet.getBody().prettyPrint();
        Assert.assertEquals(resGet.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = resGet.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result").toString()), saveTaxTypeId, "Invalid result");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-634 [R2] [CRM_API] Verify Delete - (taxes_list)tax_types{id} with null id")
    public void testDeleteTaxTypeWithNullId() {
        String updateURI = APIConstants.DELETE_TAX.replace("{taxTypeId}", "");

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .delete(updateURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 405);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-635 [R2] [CRM_API] Verify Delete - (taxes_list)tax_types{id} with Invalid id")
    public void testDeleteTaxTypeWithInvalidId() {
        String invalidId = "99999";
        String updateURI = APIConstants.DELETE_TAX.replace("{taxTypeId}", invalidId);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .delete(updateURI);
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
}


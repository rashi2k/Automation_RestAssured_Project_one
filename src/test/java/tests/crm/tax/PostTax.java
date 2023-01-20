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

public class PostTax extends BaseTest {

    @BeforeClass
    public void initiate() {
        //get country data
        getBaseCountryInfo();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-623 [R2] [CRM_API] Verify Post - (taxes_list)tax_types with Valid Pay Load")
    public void testPostTaxTypeWithValidPayLoad() {
        Tax createTaxType = requestPayload.createTaxType(countryId, countryName);
        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createTaxType)
                .post(APIConstants.CREATE_TAX);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 201);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = response.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.taxTypeCode").toString()), createTaxType.getTaxTypeCode(), "Invalid taxTypeCode");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.taxType").toString()), createTaxType.getTaxType(), "Invalid taxType");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.isActive").toString()), String.valueOf(createTaxType.getIsActive()), "Invalid isActive");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.countryId").toString()), String.valueOf(createTaxType.getCountryId()), "Invalid countryId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.countryName").toString()), createTaxType.getCountryName(), "Invalid countryName");;
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-624 [R2] [CRM_API] Verify Post - (taxes_list)tax_types with Empty Pay Load")
    public void testPostTaxTypeWithEmptyPayLoad() {
        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body("{ }")
                .post(APIConstants.CREATE_TAX);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);

        String statusCode = String.valueOf(response.getStatusCode());
        SoftAssert softAssert = new SoftAssert();
        String responseToString = response.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), statusCode, "Invalid status code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "SY_R_COUNTRY_NOT_FOUND", "Invalid error");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "There is no  related data for the given country id - null", "Invalid description");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-625 [R2] [CRM_API] Verify Post - (taxes_list)tax_types with Valid code and Empty name")
    public void testPostTaxTypeWithValidCodeAndEmptyName() {
        Tax createTaxType = requestPayload.createTaxType(countryId, countryName);
        createTaxType.setTaxType("");
        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createTaxType)
                .post(APIConstants.CREATE_TAX);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 406);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-626 [R2] [CRM_API] Verify Post - (taxes_list)tax_types with Valid name and Invalid code")
    public void testPostTaxTypeWithValidNameAndInvalidCode() {
        Tax createTaxType = requestPayload.createTaxType(countryId, countryName);
        createTaxType.setTaxTypeCode("VA@");
        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createTaxType)
                .post(APIConstants.CREATE_TAX);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 406);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = response.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.message").toString()), "ORA-00001: unique constraint (SICI.TAX_TYPE_CODE_UNIQUE) violated\n", "Invalid message");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-627 [R2] [CRM_API] Verify Post - (taxes_list)tax_types with Valid name and Empty code")
    public void testPostTaxTypeWithValidNameAndEmptyCode() {
        Tax createTaxType = requestPayload.createTaxType(countryId, countryName);
        createTaxType.setTaxTypeCode("");
        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createTaxType)
                .post(APIConstants.CREATE_TAX);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 406);
    }
}

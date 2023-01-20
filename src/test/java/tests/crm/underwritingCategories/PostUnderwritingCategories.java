package tests.crm.underwritingCategories;

import com.informatics.endpoints.APIConstants;
import com.informatics.pojos.crm.UnderwritingCategories;
import com.jayway.jsonpath.JsonPath;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import tests.BaseTest;

public class PostUnderwritingCategories extends BaseTest {

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-352 [CRM_API] Verify the POST - uw_categories with valid code and name")
    public void testPostUWCategoriesWithValidPayLoad() { //POST - uw_categories with valid code and name
        UnderwritingCategories createUnderwritingCategories = requestPayload.createUnderwritingCategory();
        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createUnderwritingCategories)
                .post(APIConstants.CREATE_UW_CATEGORY);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 201);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = response.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.code").toString()), createUnderwritingCategories.getCode(), "Invalid code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.name").toString()), createUnderwritingCategories.getName(), "Invalid name");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-364 [CRM_API] Verify the POST - uw_categories with empty code and valid name")
    public void testPostUWCategoriesWithEmptyCodeAndValidName() { //POST - uw_categories with empty code and valid name
        UnderwritingCategories createUnderwritingCategories = requestPayload.createUnderwritingCategory();
        createUnderwritingCategories.setCode("");
        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createUnderwritingCategories)
                .post(APIConstants.CREATE_UW_CATEGORY);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 406);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-368 [CRM_API] Verify the POST - uw_categories with empty data for name, valid code")
    public void testPostUWCategoriesWithEmptyNameAndValidCode() { //POST - uw_categories with empty data for name, valid code
        UnderwritingCategories createUnderwritingCategories = requestPayload.createUnderwritingCategory();
        createUnderwritingCategories.setName("");
        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createUnderwritingCategories)
                .post(APIConstants.CREATE_UW_CATEGORY);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 406);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-362 [CRM_API] Verify the POST - uw_categories with invalid code and valid name")
    public void testPostUWCategoriesWithInvalidCodeAndValidName() { //POST - uw_categories with invalid code and valid name
        String invalidCode = "VLP146";
        UnderwritingCategories createUnderwritingCategories = requestPayload.createUnderwritingCategory();
        createUnderwritingCategories.setCode(invalidCode);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createUnderwritingCategories)
                .post(APIConstants.CREATE_UW_CATEGORY);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);

        String statusCode = String.valueOf(response.getStatusCode());
        String responseToString = response.asString();
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), statusCode, "Invalid status code");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-354 [CRM_API] Verify the POST - uw_categories with empty data")
    public void testPostUWCategoriesWithEmptyData() { //POST - uw_categories with empty data
        UnderwritingCategories createUnderwritingCategories = requestPayload.createUnderwritingCategory();
        createUnderwritingCategories.setCode("");
        createUnderwritingCategories.setName("");

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createUnderwritingCategories)
                .post(APIConstants.CREATE_UW_CATEGORY);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 406);
    }
}
